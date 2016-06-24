package com.deadpineapple.rabbitmq.test;


import com.deadpineapple.dal.RabbitMqEntities.FileIsUploaded;
import com.deadpineapple.rabbitmq.RabbitInit;
import com.deadpineapple.rabbitmq.RabbitReceiver.IReceiver;

import java.util.concurrent.TimeoutException;

/**
 * Created by 15256 on 27/02/2016.
 */
public class RecvMessage {

    public static void main(String[] argv)
            throws java.io.IOException,
            java.lang.InterruptedException, TimeoutException {

        RabbitInit rabbitInit = new RabbitInit();
        rabbitInit.getFileUploadedReceiver().receiver(
                new IReceiver<FileIsUploaded>() {
                    @Override
                    public void execute(FileIsUploaded result) {
                        System.out.println(result);
                    }
                }
        );
    }
}