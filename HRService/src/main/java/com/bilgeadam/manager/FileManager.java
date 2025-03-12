package com.bilgeadam.manager;

import com.bilgeadam.config.FeignSupportConfig;
import com.bilgeadam.dto.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@FeignClient(url = "http://localhost:9090/v1/dev/file", name = "fileManager", configuration = FeignSupportConfig.class)
public interface FileManager {

    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<BaseResponse<String>> uploadFile(@RequestPart("file") MultipartFile file);

    @GetMapping(value = "download-file" + "/{fileName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<byte[]> downloadFile(@PathVariable("fileName") String fileName);

    @DeleteMapping("delete-file" + "/{fileName}")
    ResponseEntity<BaseResponse<String>> deleteFile(@PathVariable("fileName") String fileName);
}
