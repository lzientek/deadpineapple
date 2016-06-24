package com.deadpineapple.rabbitmq.RabbitSender;

import com.deadpineapple.dal.RabbitMqEntities.FileToConvert;

/**
 * Created by 15256 on 10/03/2016.
 */
public class FileToConvertSender extends AbstractRabbitSender<FileToConvert> {
    public FileToConvertSender(String configPath) {
        this.configPath = configPath;
    }

    public FileToConvertSender() {

    }

    @Override
    public String getQueueName() {
        return "fileToConvertQueue";
    }
}
