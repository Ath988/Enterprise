package com.bilgeadam.controller;

import com.bilgeadam.constants.RestApis;
import com.bilgeadam.dto.request.SaveFileRequestDto;
import com.bilgeadam.dto.response.BaseResponse;
import com.bilgeadam.entity.FileInfo;
import com.bilgeadam.service.FileService;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import static com.bilgeadam.constants.RestApis.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(FILE)
public class FileController {
	private final FileService fileService;
	
	@PostMapping(UPLOAD_FILE)
	public ResponseEntity<BaseResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
		
		return ResponseEntity.ok(BaseResponse.<String>builder()
				                         .code(200)
				                         .message("File uploaded succesfully")
				                         .data(fileService.uploadFile(file))
				                         .success(true)
		                                     .build());
	}

	@PostMapping(UPLOAD_FILE_ + "/{folderPath}")
	public ResponseEntity<BaseResponse<String>> uploadFileWithFolderManagement(
			@RequestParam("file") MultipartFile file,
			@PathVariable("folderPath") String folderPath) throws Exception {
		SaveFileRequestDto dto = new SaveFileRequestDto(file, folderPath);
		return ResponseEntity.ok(BaseResponse.<String>builder()
				.code(200)
				.message("File uploaded succesfully")
				.data(fileService.uploadFileWithFolderManagement(dto))
				.success(true)
				.build());
	}




	@GetMapping(DOWNLOAD_FILE+"/{fileName}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable("fileName") String fileName)  {
		try(InputStream stream = fileService.downloadFile(fileName)){
			return ResponseEntity.ok()
			                     .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + fileName + "\"")
					.body(stream.readAllBytes());
		} catch (Exception e) {
			return ResponseEntity.status(500).body(null);
		}
	}
	
	@DeleteMapping(DELETE_FILE+"/{fileName}")
	public ResponseEntity<BaseResponse<String>> deleteFile(@PathVariable("fileName") String fileName) {
		try {
			fileService.deleteFile(fileName);
			return ResponseEntity.ok(BaseResponse.<String>builder()
			                                     .code(200)
			                                     .message("File deleted succesfully")
			                                     .data("File deleted succesfully")
			                                     .success(true)
			                                     .build());
		}catch (Exception e){
			return ResponseEntity.status(500).body(BaseResponse.<String>builder()
					                                       .code(500)
					                                       .message("File deletion failed : "+e.getMessage())
					                                       .data("File deletion failed : "+e.getMessage())
					                                       .success(false)
			                                                   .build());
		}
		
	}
	
	@GetMapping(GET_ALL_FILES)
	public ResponseEntity<BaseResponse<List<String>>> getAllFiles() throws MinioException, IOException {
		return ResponseEntity.ok(BaseResponse.<List<String>>builder()
				                         .code(200)
				                         .success(true)
				                         .message("All files were successfully fetched.")
				                         .data(fileService.getAllFilesInBucket())
		                                     .build());
	}

	@GetMapping(GET_ALL_FILES+"/{folderPath}")
	public ResponseEntity<BaseResponse<List<String>>> getAllFilesWithFolderManagement(@PathVariable("folderPath") String folderPath) throws MinioException, IOException {
		return ResponseEntity.ok(BaseResponse.<List<String>>builder()
				.code(200)
				.success(true)
				.message("All files were successfully fetched.")
				.data(fileService.getAllFilesInBucketWithFolderManagement(folderPath))
				.build());
	}
	
	@PutMapping(RENAME_FILE+"/{oldFileName}")
	public ResponseEntity<BaseResponse<String>> renameFile(@PathVariable("oldFileName") String oldFileName, @RequestParam("newFileName") String newFileName) {
		try{
			fileService.renameFile(oldFileName, newFileName);
			return ResponseEntity.ok(BaseResponse.<String>builder()
			                                     .code(200)
						                         .message("File renamed successfully")
						                         .data("File renamed successfully")
						                         .success(true)
			                                     .build());
		}catch (Exception e){
			return ResponseEntity.status(500).body(BaseResponse.<String>builder()
					                                       .code(500)
					                                       .message("File rename failed : "+e.getMessage())
					                                       .data("File rename failed : "+e.getMessage())
					                                       .success(false)
			                                               .build());
		}
	}
	
	@GetMapping(GET_FILE_URL+"/{fileName}")
	public ResponseEntity<BaseResponse<String>> getFileUrl(@PathVariable("fileName") String fileName) {
		try{
			return ResponseEntity.ok(BaseResponse.<String>builder()
					                         .code(200)
					                         .message("File URL retrieved successfully")
					                         .data(fileService.getFileUrl(fileName))
					                         .success(true)
			                                 .build());
		}
		catch (Exception e){
			return ResponseEntity.status(404).body(BaseResponse.<String>builder()
					                                       .code(404)
					                                       .message("File not found : "+e.getMessage())
					                                       .data(null)
					                                       .success(false)
			                                               .build());
		}
	}
	
	
	}