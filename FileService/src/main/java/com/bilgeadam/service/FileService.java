package com.bilgeadam.service;


import com.bilgeadam.entity.FileInfo;

import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.FileServiceException;
import com.bilgeadam.repository.FileInfoRepository;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(MultipartFile file) throws Exception {
		String fileName = file.getOriginalFilename();
		
		if (!isBucketExist(bucketName)) {
			minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
		}
		
		// Dosyayı MinIO'ya yükleme işlemi
		minioClient.putObject(
				PutObjectArgs.builder()
				             .bucket(bucketName)
				             .object(fileName)
				             .stream(file.getInputStream(), file.getSize(), -1)
				             .contentType(file.getContentType())
				             .build()
		);
		
		// Dosyanın presigned URL'sini döndüren kısım
		String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
				   .bucket(bucketName)
				   .object(fileName)
				   .method(Method.GET)
				   .expiry(60*60) // URL'nin 1 saat geçerli olması için. Test ederken kaldırılabilir.
		           .build());
		
		// Dosyanın bazı bilgilerini veritabanına kaydeden kısım
		FileInfo fileInfo = FileInfo.builder().url(url).fileName(fileName).size(file.getSize()).build();
		fileInfoRepository.save(fileInfo);
		
		return url;
	}
	
	/**
	 * bucketName parametresi ile Bucket'ın var olup olmadığını kontrol eden yardımcı metot.
	 * @param bucketName
	 * @return
	 * @throws Exception
	 */
	private Boolean isBucketExist(String bucketName) throws Exception {
		return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
	}
	
	/**
	 * Dosya'nın ismi doğru girildiğinde dosya indirme bağlantısı veren metot.
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public InputStream downloadFile(String fileName) throws Exception {
		
		return minioClient.getObject(GetObjectArgs.builder()
				                             .bucket(bucketName)
				                             .object(fileName)
		                                          .build());
	}
	
	/**
	 * Dosya'yı bucket'tan silen metot.
	 * @param fileName
	 * @throws Exception
	 */
	public void deleteFile(String fileName) throws Exception {
		
		minioClient.removeObject(RemoveObjectArgs.builder()
		                                         .bucket(bucketName)
		                                         .object(fileName)
		                                         .build());
		FileInfo fileInfo = getFileInfo(fileName);
		fileInfo.setState(EState.PASSIVE);
		fileInfoRepository.save(fileInfo);
	}
	
	public List<String> getAllFilesInBucket()  {
		List<String> fileNames = new ArrayList<>();
		Iterable<Result<Item>> objects = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
		
		for (Result<Item> result:objects){
			try {
				Item item = result.get();
				fileNames.add(item.objectName());
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return fileNames;
	}
	
	private FileInfo getFileInfo(String fileName)  {
		 return fileInfoRepository.findByFileName(fileName);
	}
	
	
}