package com.bilgeadam.repository;

import com.bilgeadam.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
	
	FileInfo findByFileName(String fileName);
}