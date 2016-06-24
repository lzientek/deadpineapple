package com.deadpineapple.rabbitmq.RabbitReceiver;

import com.deadpineapple.dal.RabbitMqEntities.FileToConvert;

/**
 * Created by 15256 on 10/03/2016.
 */
public class FileToConvertReceiver extends AbstractRabbitReceiver<FileToConvert> {
    public FileToConvertReceiver(String configPath) {
        this.configPath = configPath;
    }

    public FileToConvertReceiver() {

    }

    @Override
    public String getQueueName() {
        return "fileToConvertQueue";
    }
}
