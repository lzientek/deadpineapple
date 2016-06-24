package com.deadpineapple.rabbitmq.RabbitSender;

import com.deadpineapple.dal.RabbitMqEntities.FileIsUploaded;

/**
 * Created by 15256 on 10/03/2016.
 */
public class FileUploadedSender extends AbstractRabbitSender<FileIsUploaded> {

    public FileUploadedSender(String configPath) {
        this.configPath = configPath;
    }

    public FileUploadedSender() {

    }

    @Override
    public String getQueueName() {
        return "fileUploadQueue";
    }
}
