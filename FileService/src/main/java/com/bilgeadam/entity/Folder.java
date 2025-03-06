package com.bilgeadam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tbl_folder")
public class Folder extends BaseEntity {
    private String folderName;
    private String folderPath;
    @ElementCollection
    private List<String> fileIdList;

}

