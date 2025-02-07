package com.bilgeadam.service;


import com.bilgeadam.entity.FileInfo;

import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.FileServiceException;
import com.bilgeadam.repository.FileInfoRepository;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
	private final MinioClient minioClient;
	private final FileInfoRepository fileInfoRepository;
	
	@Value("${minio.bucket-name}")
	private String bucketName;
	
	/**
	 * Dosyayı bucket'a yükleyen metot. Geriye presigned URL dönüyor. Bu url ile yüklediğiniz dosyaya ulaşabilirsiniz.
	 * Şuanlık bu URL 1 saat geçerli durumda. Test ederken bunu değiştirebilir ya da tamamen kaldırabilirsiniz.
	 *
	 * @param file
	 * @return Dosyanın url'sini döner.
	 * @throws Exception
	 */
	public String uploadFile(MultipartFile file) throws Exception {
		String fileName = file.getOriginalFilename();
		
		if (isFileExist(fileName)) {
			throw new FileServiceException(ErrorType.FILE_ALREADY_EXIST);
		}
		
		if (!isBucketExist(bucketName)) {
			minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
		}
		
		// Dosyayı MinIO'ya yükleme işlemi
		minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(fileName)
		                                   .stream(file.getInputStream(), file.getSize(), -1)
		                                   .contentType(file.getContentType()).build());
		
		// Dosyanın presigned URL'sini döndüren kısım
		String url = getFileUrl(fileName);
		
		// Dosyanın bazı bilgilerini veritabanına kaydeden kısım
		FileInfo fileInfo = FileInfo.builder().url(url).fileName(fileName).size(file.getSize()).build();
		fileInfoRepository.save(fileInfo);
		
		return url;
	}
	
	
	/**
	 * Verilen bucketName ve fileName'i kontrol ederek, eğer verilen isimde bir dosya varsa o dosyanın url'sini dönen
	 * metot.
	 * @param fileName
	 * @return Geriye presignedUrl döner.
	 * @throws Exception
	 */
	public String getFileUrl(String fileName) throws Exception {
		if (!isFileExist(fileName)) {
			throw new FileServiceException(ErrorType.FILE_NOT_FOUND);
		}
		String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName)
		                                                                        .object(fileName).method(Method.GET)
		                                                                        .expiry(60 * 60) // URL'nin 1 saat
		                                                                        // geçerli olması için. Test ederken
		                                                                        // kaldırılabilir.
		                                                                        .build());
		return url;
	}
	
	/**
	 * bucketName parametresi ile Bucket'ın var olup olmadığını kontrol eden yardımcı metot.
	 *
	 * @param bucketName
	 * @return true or false
	 * @throws Exception
	 */
	private Boolean isBucketExist(String bucketName) throws Exception {
		return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
	}
	
	/**
	 * Dosya'nın ismi doğru girildiğinde dosya indirme bağlantısı veren metot.
	 *
	 * @param fileName
	 * @return Dosya indirme bağlantısını geri döner.
	 * @throws Exception
	 */
	public InputStream downloadFile(String fileName) throws Exception {
		
		return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
	}
	
	/**
	 * Dosya'yı bucket'tan silen metot.
	 *
	 * @param fileName
	 * @throws Exception
	 */
	public void deleteFile(String fileName) throws Exception {
		
		minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
		FileInfo fileInfo = getFileInfo(fileName);
		fileInfo.setState(EState.PASSIVE);
		fileInfoRepository.save(fileInfo);
	}
	
	
	/**
	 * Bucket'taki bütün dosyaların isimlerini dönen metot.
	 * @return Dosya isimlerinin bulunduğu listeyi geri döner.
	 */
	public List<String> getAllFilesInBucket() {
		List<String> fileNames = new ArrayList<>();
		Iterable<Result<Item>> objects = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
		
		for (Result<Item> result : objects) {
			try {
				Item item = result.get();
				fileNames.add(item.objectName());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fileNames;
	}
	
	private FileInfo getFileInfo(String fileName) {
		return fileInfoRepository.findByFileNameAndState(fileName,EState.ACTIVE);
	}
	
	
	/**
	 * Bucket'taki dosyanın ismini değiştiren metot.
	 * Aslında direkt olarak dosyanın ismini değiştirmiyor.
	 * Öncelikle dosyayı kopyalayıp yeni isimle aynı dosyadan bir tane daha oluşturuyor. Daha sonra eski isimli
	 * dosyayı siliyor ve DB'den state'ini passive'e çekiyor daha sonra yeni eklenen dosyayı da DB'ye kaydediyor.
	 * renameFile işlemini bu şekilde yapmamızın sebebi: MinIO'da dosyalar key value pair olarak tutuluyor. Yani dosya
	 * isimleri key olduğu için değiştirmeye izin verilmiyor.
	 * @param oldFileName
	 * @param newFileName
	 * @throws Exception
	 */
	public void renameFile(String oldFileName, String newFileName) throws Exception {
		FileInfo oldFileInfo = getFileInfo(oldFileName);
		if (oldFileInfo==null){
			throw new FileServiceException(ErrorType.FILE_NOT_FOUND);
		}
		if(isFileExist(newFileName)){
			throw new FileServiceException(ErrorType.FILE_ALREADY_EXIST);
		}
		minioClient.copyObject(CopyObjectArgs.builder().bucket(bucketName).object(newFileName)
		                                     .source(CopySource.builder().bucket(bucketName).object(oldFileName)
		                                                       .build()).build());
		String newFileUrl = getFileUrl(newFileName);
		
		FileInfo newFileInfo =
				FileInfo.builder().fileName(newFileName).size(oldFileInfo.getSize()).url(newFileUrl).build();
		fileInfoRepository.save(newFileInfo);
		deleteFile(oldFileName);
	}
	
	/**
	 * Bucket'ta o dosyanının olup olmadığını kontrol eden yardımcı metot.
	 * @param fileName
	 * @return true or false
	 */
	private boolean isFileExist(String fileName) {
		try {
			minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(fileName).build());
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	
}