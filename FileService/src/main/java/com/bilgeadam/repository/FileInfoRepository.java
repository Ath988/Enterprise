package com.bilgeadam.repository;

import com.bilgeadam.entity.FileInfo;
import com.bilgeadam.entity.enums.EState;
import com.bilgeadam.view.VwFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
	
	FileInfo findByFileNameAndState(String fileName, EState state);

    @Query(value = "SELECT new com.bilgeadam.view.VwFile (f.fileName, f.url) FROM FileInfo f WHERE f.id IN(?)")
    List<VwFile> findAllByIdList(List<String> fileIds);
}