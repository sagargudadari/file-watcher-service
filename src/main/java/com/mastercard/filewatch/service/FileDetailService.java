package com.mastercard.filewatch.service;

import com.mastercard.filewatch.constants.FileStatus;
import com.mastercard.filewatch.entity.FileDetail;
import com.mastercard.filewatch.repository.FileDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class FileDetailService implements MessageHandler {

    @Value("${file.regexIgnore.pattern}")
    private String fileIgnorePattern;

    @Value("${type.regex.pattern}")
    private String[] typeRegexPattern;

    @Autowired
    private FileDetailRepository fileDetailRepository;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        File file = (File) message.getPayload();
        log.info("Process Start. File info Name : {} and Location : {}", file.getName(), file.getAbsolutePath());

        FileDetail fileDetailResult;
        Pattern pattern = Pattern.compile(fileIgnorePattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(FilenameUtils.removeExtension(file.getName()));
        if (!matcher.matches()) {
            FileDetail fileDetail = FileDetail.builder()
                    .location(file.getAbsolutePath())
                    .name(FilenameUtils.removeExtension(file.getName()))
                    .type(file.getName().split(typeRegexPattern[0])[Integer.parseInt(typeRegexPattern[1])])
                    .status(FileStatus.READY)
                    .build();

            fileDetailResult = createDataFileInfo(fileDetail);
            log.info("Process completed Successfully. File Info {}", fileDetailResult);
        } else {
            log.info("Process completed. Ignoring as file name contains 'TEMP' keyword {}",
                    FilenameUtils.removeExtension(file.getName()));
        }
    }

    public synchronized FileDetail getFileToBeProcessed() {
        FileDetail readyStatusFile = fileDetailRepository.findTop1DataFileByStatus(FileStatus.READY);
        if(readyStatusFile!=null){
            readyStatusFile.setStatus(FileStatus.PROCESSING);
            fileDetailRepository.save(readyStatusFile);
        }
        return readyStatusFile;
    }

    private FileDetail createDataFileInfo(FileDetail fileDetail){
        log.info("Checking if file name already exists, if not then persist file info to database where "
                + "File name : {}", fileDetail.getName());
        Optional<FileDetail> file = fileDetailRepository.findByName(fileDetail.getName());
        return file.orElse(fileDetailRepository.save(fileDetail));
    }

    public void setFileDetailRepository(FileDetailRepository fileDetailRepository) {
        this.fileDetailRepository = fileDetailRepository;
    }

    public void setFileIgnorePattern(String fileIgnorePattern) {
        this.fileIgnorePattern = fileIgnorePattern;
    }

    public void setTypeRegexPattern(String[] typeRegexPattern) {
        this.typeRegexPattern = typeRegexPattern;
    }
}
