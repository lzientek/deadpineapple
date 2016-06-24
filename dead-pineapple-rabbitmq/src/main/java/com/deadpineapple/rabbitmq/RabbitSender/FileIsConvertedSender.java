package com.deadpineapple.rabbitmq.RabbitSender;

import com.deadpineapple.dal.RabbitMqEntities.FileIsConverted;

/**
 * Created by 15256 on 10/03/2016.
 */
public class FileIsConvertedSender extends AbstractRabbitSender<FileIsConverted> {
    public FileIsConvertedSender(String configPath) {
        this.configPath = configPath;
    }

    public FileIsConvertedSender() {

    }

    @Override
    public String getQueueName() {
        return "fileIsConverted";
    }
}
