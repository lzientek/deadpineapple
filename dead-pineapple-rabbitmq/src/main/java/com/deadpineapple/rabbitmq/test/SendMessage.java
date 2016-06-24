package com.deadpineapple.rabbitmq.test;

import com.deadpineapple.dal.RabbitMqEntities.FileIsConverted;
import com.deadpineapple.dal.RabbitMqEntities.FileIsUploaded;
import com.deadpineapple.dal.RabbitMqEntities.FileToConvert;
import com.deadpineapple.rabbitmq.RabbitConnection;
import com.deadpineapple.rabbitmq.RabbitInit;
import com.deadpineapple.rabbitmq.Serializer.ObjectByteSerializer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by 15256 on 27/02/2016.
 */
public class SendMessage {


    public static void main(String[] args) {
        RabbitInit rabbitInit = new RabbitInit();
        rabbitInit.getFileToConvertSender().send(new FileToConvert(12,"C:\\Users\\15256\\Videos\\01_02-How to Access Your Working Files.mp4",".oma",""));
        rabbitInit.closeAll();
    }
}
