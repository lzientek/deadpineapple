package com.deadpineapple.core;

import com.deadpineapple.dal.RabbitMqEntities.FileIsUploaded;

/**
 * Created by 15256 on 30/03/2016.
 */
public class TestMain {
    public static void main(String[] args) {
        try {
            ConversionLauncher conversionLauncher = new ConversionLauncher();
            FileIsUploaded fileIsUploaded = new FileIsUploaded(1, "E:\\serie\\into badlands\\s1\\izi.mkv", ".avi", "");
            conversionLauncher.start();
            conversionLauncher.exeFileIsUploaded(fileIsUploaded);
        } catch (Exception ex) {
            ex.printStackTrace();
            main(args);
        }
    }
}
