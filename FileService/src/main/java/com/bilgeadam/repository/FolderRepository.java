package com.bilgeadam.repository;

import com.bilgeadam.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder, String> {
    List<Folder> findAllByFolderPathIsNull();

    List<Folder> findAllByFolderPath(String folderPath);

    Optional<Folder> findByFolderNameAndFolderPath(String folderName, String folderPath);
}
