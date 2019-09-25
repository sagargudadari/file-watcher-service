package com.mastercard.billing.filewatcherservice.repository;

import com.mastercard.billing.filewatcherservice.entity.FileDetail;
import com.mastercard.billing.filewatcherservice.entity.FileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileDetailRepository extends JpaRepository<FileDetail, Long> {

    FileDetail findTop1DataFileByStatus(String fileStatus);

    Optional<FileDetail> findByName(String fileName);

    Optional<FileDetail> findTop1ByStatus(FileStatus status);
}
