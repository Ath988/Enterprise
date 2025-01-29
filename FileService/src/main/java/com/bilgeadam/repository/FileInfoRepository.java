package com.bilgeadam.repository;

import com.bilgeadam.entity.FileInfo;
import com.bilgeadam.entity.enums.EState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
	
	FileInfo findByFileNameAndState(String fileName, EState state);
}