package com.bilgeadam.controller;

import com.bilgeadam.constants.RestApis;
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
	
}