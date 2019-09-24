package com.mastercard.filewatch.controller;


import com.mastercard.filewatch.entity.FileDetail;
import com.mastercard.filewatch.service.FileDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
public class FileDetailController {

    @Autowired
    FileDetailService fileDetailService;

    @GetMapping("/file-details")
    public ResponseEntity<FileDetail> getNewFileDetails(){

        return new ResponseEntity(fileDetailService.getFileToBeProcessed(), OK);
    }

    @PostMapping("/process-completed")
    public ResponseEntity<FileDetail> updateFileDetails(@RequestParam(name="fileId") String fileId){

        return new ResponseEntity(fileDetailService.getFileToBeProcessed(), OK);
    }
}
