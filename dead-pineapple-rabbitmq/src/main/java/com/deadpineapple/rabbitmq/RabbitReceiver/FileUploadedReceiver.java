package com.deadpineapple.rabbitmq.RabbitReceiver;

import com.deadpineapple.dal.RabbitMqEntities.FileIsUploaded;

/**
 * Created by 15256 on 10/03/2016.
 */
public class FileUploadedReceiver extends AbstractRabbitReceiver<FileIsUploaded> {
    public FileUploadedReceiver(String configPath) {
        this.configPath = configPath;
    }

    public FileUploadedReceiver() {

    }

    @Override
    public String getQueueName() {
        return "fileUploadQueue";
    }
}
