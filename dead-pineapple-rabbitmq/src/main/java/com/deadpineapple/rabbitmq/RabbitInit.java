package com.deadpineapple.rabbitmq;

import com.deadpineapple.dal.RabbitMqEntities.FileIsConverted;
import com.deadpineapple.rabbitmq.RabbitReceiver.FileIsConvertedReceiver;
import com.deadpineapple.rabbitmq.RabbitReceiver.FileToConvertReceiver;
import com.deadpineapple.rabbitmq.RabbitReceiver.FileUploadedReceiver;
import com.deadpineapple.rabbitmq.RabbitSender.FileIsConvertedSender;
import com.deadpineapple.rabbitmq.RabbitSender.FileToConvertSender;
import com.deadpineapple.rabbitmq.RabbitSender.FileUploadedSender;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by 15256 on 10/03/2016.
 */
public class RabbitInit {

    public RabbitInit(String configPath) {
        fileIsConvertedReceiver = new FileIsConvertedReceiver(configPath);
        fileToConvertReceiver = new FileToConvertReceiver(configPath);
        fileUploadedReceiver = new FileUploadedReceiver(configPath);

        fileIsConvertedSender = new FileIsConvertedSender(configPath);
        fileToConvertSender = new FileToConvertSender(configPath);
        fileUploadedSender = new FileUploadedSender(configPath);
    }
    public RabbitInit(){
         fileIsConvertedReceiver = new FileIsConvertedReceiver();
         fileToConvertReceiver = new FileToConvertReceiver();
         fileUploadedReceiver = new FileUploadedReceiver();

         fileIsConvertedSender = new FileIsConvertedSender();
         fileToConvertSender = new FileToConvertSender();
         fileUploadedSender = new FileUploadedSender();
    }

    private FileIsConvertedReceiver fileIsConvertedReceiver;
    private FileToConvertReceiver fileToConvertReceiver;
    private FileUploadedReceiver fileUploadedReceiver;

    private FileIsConvertedSender fileIsConvertedSender;
    private FileToConvertSender fileToConvertSender;
    private FileUploadedSender fileUploadedSender;

    public FileIsConvertedReceiver getFileIsConvertedReceiver() {
        return fileIsConvertedReceiver;
    }

    public FileToConvertReceiver getFileToConvertReceiver() {
        return fileToConvertReceiver;
    }

    public FileUploadedReceiver getFileUploadedReceiver() {
        return fileUploadedReceiver;
    }

    public FileIsConvertedSender getFileIsConvertedSender() {
        return fileIsConvertedSender;
    }

    public FileToConvertSender getFileToConvertSender() {
        return fileToConvertSender;
    }

    public FileUploadedSender getFileUploadedSender() {
        return fileUploadedSender;
    }


    public void closeAll() {
        try {
            getFileIsConvertedSender().close();
            getFileToConvertSender().close();
            getFileUploadedSender().close();
            getFileIsConvertedReceiver().close();
            getFileToConvertReceiver().close();
            getFileUploadedReceiver().close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
