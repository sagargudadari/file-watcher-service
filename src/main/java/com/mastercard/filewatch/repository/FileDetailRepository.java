package com.mastercard.filewatch.repository;

import com.mastercard.filewatch.entity.FileDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileDetailRepository extends JpaRepository<FileDetail, String> {

    FileDetail findTop1DataFileByStatus(String fileStatus);
    Optional<FileDetail> findByName(String fileName);
}
