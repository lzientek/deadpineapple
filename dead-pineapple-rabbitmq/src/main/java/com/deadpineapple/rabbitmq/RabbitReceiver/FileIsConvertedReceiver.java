package com.deadpineapple.rabbitmq.RabbitReceiver;

import com.deadpineapple.dal.RabbitMqEntities.FileIsConverted;
import com.deadpineapple.rabbitmq.RabbitSender.AbstractRabbitSender;

/**
 * Created by 15256 on 10/03/2016.
 */
public class FileIsConvertedReceiver extends AbstractRabbitReceiver<FileIsConverted> {
    public FileIsConvertedReceiver(String configPath) {
        super.configPath = configPath;
    }

    public FileIsConvertedReceiver() {

    }

    @Override
    public String getQueueName() {
        return "fileIsConverted";
    }
}
