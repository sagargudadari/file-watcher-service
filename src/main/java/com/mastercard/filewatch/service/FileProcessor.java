package com.mastercard.filewatch.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileProcessor implements MessageHandler {

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        File file = (File) message.getPayload();
        System.out.println("File received name " + file.getName());
        System.out.println("File received Location" + file.getAbsolutePath());
        System.out.println("File received type " + FilenameUtils.getExtension(file.getName()));
    }
}
