package com.deadpineapple.core;

import com.deadpineapple.videoHelper.email.EmailSender;
import com.deadpineapple.videoHelper.email.MailGenerator;
import com.deadpineapple.dal.RabbitMqEntities.FileIsConverted;
import com.deadpineapple.dal.RabbitMqEntities.FileIsUploaded;
import com.deadpineapple.dal.RabbitMqEntities.FileToConvert;
import com.deadpineapple.dal.dao.ConvertedFileDao;
import com.deadpineapple.dal.dao.IConvertedFileDao;
import com.deadpineapple.dal.dao.ISplitFileDao;
import com.deadpineapple.dal.dao.SplitFileDao;
import com.deadpineapple.dal.entity.ConvertedFile;
import com.deadpineapple.dal.entity.SplitFile;
import com.deadpineapple.rabbitmq.RabbitInit;
import com.deadpineapple.rabbitmq.RabbitReceiver.IReceiver;
import com.deadpineapple.videoHelper.fileEdit.FileJoiner;
import com.deadpineapple.videoHelper.fileEdit.FileSplitter;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.SessionFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by 15256 on 18/03/2016.
 */
public class ConversionLauncher {
    private RabbitInit rabbitInit;

    private static final int MaximumPartSize = 2 * 1024 * 1024;
    IConvertedFileDao convertedFileDao;

    ISplitFileDao splitFileDao;

    public ConversionLauncher(String configPath) {
        rabbitInit = new RabbitInit(configPath);
    }

    public ConversionLauncher() {
        rabbitInit = new RabbitInit();
    }

    public void start() {
        if (convertedFileDao == null) {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            convertedFileDao = new ConvertedFileDao(sessionFactory);
            splitFileDao = new SplitFileDao(sessionFactory);
        }
        rabbitInit.getFileUploadedReceiver().receiver(new IReceiver<FileIsUploaded>() {
            @Override
            public void execute(FileIsUploaded result) {
                exeFileIsUploaded(result);
            }
        });

        rabbitInit.getFileIsConvertedReceiver().receiver(new IReceiver<FileIsConverted>() {
            @Override
            public void execute(FileIsConverted result) {
                exeFileIsConverted(result);
            }
        });
    }

    public void exeFileIsUploaded(FileIsUploaded result) {
        try {
            FileSplitter splitter = new FileSplitter(result.getFileName());
            ConvertedFile mainFile = convertedFileDao.findById(result.getFileId());
            List<File> fileParts = splitter.splitFile();

            for (File fileToConvert : fileParts) {
                //on enregistrer le split en bdd
                SplitFile sfile = new SplitFile();
                sfile.setSize(fileToConvert.length());
                sfile.setConverted(false);
                sfile.setSplitFilePath(fileToConvert.getAbsolutePath());
                sfile.setConvertedFile(mainFile);
                sfile = splitFileDao.createFile(sfile);

                //on envoie la requete rabbitmq
                FileToConvert fileToSend = new FileToConvert();
                fileToSend.setFileId(mainFile.getId());
                fileToSend.setSplitFileId(sfile.getId());
                fileToSend.setFileName(sfile.getSplitFilePath());
                fileToSend.setConvertionType(result.getNewFileType());
                fileToSend.setConvertionEncoding(result.getNewEncoding());
                rabbitInit.getFileToConvertSender().send(fileToSend);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //// TODO: 18/03/2016 envoyer le mail de confirmation de payment

    }


    public File joinFile(List<File> files, String filePath) throws IOException, InterruptedException {
        FileJoiner joiner = new FileJoiner(files, filePath);
        return joiner.joinFiles();
    }

    public void exeFileIsConverted(FileIsConverted result) {
        if (result.getWasSuccessFull()) {
            SplitFile file = splitFileDao.findById(result.getSplitFileId());
            file.setConverted(result.getWasSuccessFull());
            splitFileDao.updateFile(file);
            ConvertedFile convertedFile = convertedFileDao.findById(result.getFileId());
            List<SplitFile> splitFiles = convertedFile.getSplitFiles();
            if (allSplitAreConverted(splitFiles)) { //tout les fichiers sont convertiss
                try {
                    convertedFile.setFilePath(convertedFile.getFilePath().replace( convertedFile.getOldType(), convertedFile.getNewType()));
                    File joinFile = joinFile(splitFilesToFiles(splitFiles), convertedFile.getFilePath());
                    if (joinFile.exists()) {
                        //sending the email
                        Hashtable<String, String> generatorHashTable = MailGenerator.getConvertedFileConrespondanceTable(
                                convertedFile.getUserAccount().getFirstName() + " " + convertedFile.getUserAccount().getLastName(),
                                convertedFile.getOriginalName(), convertedFile.getFilePath(), "https://photos-6.dropbox.com/t/2/AAAEFI367SZuTY4cByBgm67lc9aFD7eu8hVPDisYUb4DBg/12/114048003/png/32x32/3/1463083200/0/2/donwload.png/EOnYhFgY7M8DIAIoAigE/orL1LMvZy8K1hgt2nVJbAo4dikC9dbhuFvM90hFuzn0?size_mode=5&size=32x32");
                        MailGenerator generator = new MailGenerator(MailGenerator.FICHIER_CONVERTIE_TEMPLATE, generatorHashTable);
                        EmailSender sender = new EmailSender(convertedFile.getUserAccount().getEmail(), "Votre fichier est convertie!", generator.generateTheEmail());
                        sender.send();

                        //mis a jour en bdd
                        System.out.println("joined");
                        convertedFile.setConverted(true);
                        convertedFile.setConvertedDate(new Date());
                        convertedFile.setFilePath(joinFile.getAbsolutePath());
                        convertedFileDao.updateFile(convertedFile);//todo:voir le bug avec mika demain
                        System.out.println("file is converted " + joinFile.getAbsolutePath());
                        return;
                    }

                    //if the joined failed
                    result.setConversionError("join error");
                } catch (Exception e) {
                    result.setConversionError("join error");
                    e.printStackTrace();
                }
            }
        }

        // TODO: 03/04/2016 send error mail with refund fuck them

    }

    private List<File> splitFilesToFiles(List<SplitFile> splitFiles) {
        List<File> resultList = new ArrayList<File>(splitFiles.size());
        for (SplitFile splitFile : splitFiles) {
            resultList.add(new File(splitFile.getSplitFilePath()));
        }
        return resultList;
    }

    private boolean allSplitAreConverted(List<SplitFile> splitFiles) {
        for (SplitFile splitFile : splitFiles) {
            if (!splitFile.getConverted()) {
                return false;
            }
        }
        return true;
    }
}


