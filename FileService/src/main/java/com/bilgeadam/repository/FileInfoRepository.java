package com.bilgeadam.repository;

import com.bilgeadam.entity.FileInfo;
import com.bilgeadam.entity.Folder;
import com.bilgeadam.entity.enums.EState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
	
	FileInfo findByFileNameAndState(String fileName, EState state);

	@Query(value = "SELECT f.id FROM FileInfo f WHERE f.isInTheRoot = ?1")
	List<String> findAllIdByIsInTheRoot(Boolean isInTheRoot);

	@Query(value = "SELECT f.fileName FROM FileInfo f WHERE f.id IN :ids")
	List<String> findFileNamesByIdIn(@Param("ids") List<String> ids);

}