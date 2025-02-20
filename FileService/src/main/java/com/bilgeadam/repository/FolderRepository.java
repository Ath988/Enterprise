package com.bilgeadam.repository;

import com.bilgeadam.entity.FolderManagement;
import com.bilgeadam.entity.enums.EState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends JpaRepository<FolderManagement, Long> {
    int countByOrganizationId(long organizationId);

    Optional<FolderManagement> findByFolderName(String folderName);

    List<FolderManagement> findAllByParentIdAndOrganizationIdAndState(long parentId, long organizationId, EState eState);
    @Query("SELECT f.id FROM FolderManagement f WHERE f.folderName = :folderName")
    Optional<Long> findFolderIdByFolderName(@Param("folderName") String folderName);

}
