package com.deadpineapple.transcoder;


import com.deadpineapple.dal.RabbitMqEntities.FileIsConverted;
import com.deadpineapple.dal.RabbitMqEntities.FileToConvert;
import com.deadpineapple.dal.dao.ConvertedFileDao;
import com.deadpineapple.dal.dao.SplitFileDao;
import com.deadpineapple.dal.entity.SplitFile;
import com.deadpineapple.rabbitmq.RabbitInit;
import com.deadpineapple.rabbitmq.RabbitReceiver.IReceiver;
import com.deadpineapple.videoHelper.FfmpegException;
import com.deadpineapple.videoHelper.converter.Conversion;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 15256 on 12/03/2016.
 */
public class ConversionLauncher implements IReceiver<FileToConvert> {

    public static final String RESULT_FOLDER = "/conversion";

    public RabbitInit rabbitInit;

    public ConversionLauncher(String configPath) {
        rabbitInit = new RabbitInit(configPath);
    }

    public ConversionLauncher() {
        rabbitInit = new RabbitInit();
    }


    public void start() {
        rabbitInit.getFileToConvertReceiver().receiver(this);
    }

    @Override
    public void execute(FileToConvert result) {
        Conversion conv = new Conversion(result.getFileName(), generateNewFileName(result)
                , result.getConvertionType(), result.getConvertionEncoding());
        FileIsConverted convertedReport = null;

        try {
            Boolean conversionSuccess = conv.start();

            convertedReport = new FileIsConverted(
                    result.getFileId()
                    , conv.getFileDestinationPath()
                    , conversionSuccess
                    , !conversionSuccess ? "FFMPEG conversion error" : null, result.getSplitFileId());
            System.out.println(result);
            System.out.println(conversionSuccess);
        } catch (IOException e) {
            convertedReport = new FileIsConverted(result.getFileId(), conv.getFileDestinationPath()
                    , false, "IO error :" + e.getMessage(), result.getSplitFileId());
        } catch (InterruptedException e) {
            convertedReport = new FileIsConverted(result.getFileId(), conv.getFileDestinationPath()
                    , false, "Interrupt error :" + e.getMessage(), result.getSplitFileId());
        } catch (FfmpegException e) {
            convertedReport = new FileIsConverted(result.getFileId(), conv.getFileDestinationPath()
                    , false, "Conversion error :" + e.getMessage(), result.getSplitFileId());
        }
        if (convertedReport != null) {
            rabbitInit.getFileIsConvertedSender().send(convertedReport);
        }

    }

    public String generateNewFileName(FileToConvert fileToConvert) {
        File oldFile = new File(fileToConvert.getFileName());
        String oldName = oldFile.getName();
        int indexOfFirstPoint = oldName.indexOf('.');
        String oldNameWithoutExt = oldName.substring(0, indexOfFirstPoint);
        String newName = oldNameWithoutExt;
        String newFullName = newName + fileToConvert.getConvertionType();

        //creation du dossier de reception 1 par jour
        Date actualDate = new Date();
        SimpleDateFormat inputDf = new SimpleDateFormat("yyyy-MM-dd");
        File folder = new File(RESULT_FOLDER + "\\" + inputDf.format(actualDate) + "\\");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File newFile = new File(folder.getPath() + "\\" + newFullName);
        int i = 0;
        while (newFile.exists()) {
            newFullName = newName + "_" + i + fileToConvert.getConvertionType();
            i++;
            newFile = new File(folder.getPath() + "\\" + newFullName);
        }

        return newFile.getPath();
    }
}
