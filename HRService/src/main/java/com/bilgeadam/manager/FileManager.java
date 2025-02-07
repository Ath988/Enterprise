package com.bilgeadam.manager;

import com.bilgeadam.dto.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@FeignClient(url = "http://localhost:9090/v1/dev/file", name = "fileManager")
public interface FileManager {

    @PostMapping("/upload-file")
    ResponseEntity<BaseResponse<String>> uploadFile(@RequestParam("file") MultipartFile file);

    @GetMapping("download-file" + "/{fileName}")
    ResponseEntity<byte[]> downloadFile(@PathVariable("fileName") String fileName);

    @DeleteMapping("delete-file" + "/{fileName}")
    ResponseEntity<BaseResponse<String>> deleteFile(@PathVariable("fileName") String fileName);
}
