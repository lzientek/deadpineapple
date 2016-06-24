package com.deadpineapple.dal.RabbitMqEntities;

import java.io.Serializable;

/**
 * Created by 15256 on 10/03/2016.
 */
public class FileIsUploaded implements Serializable {
    private Long fileId;
    private String fileName;
    private String newFileType;
    private String newEncoding;

    public FileIsUploaded(long fileId, String fileName, String newFileType, String newEncoding) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.newFileType = newFileType;
        this.newEncoding = newEncoding;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getNewFileType() {
        return newFileType;
    }

    public void setNewFileType(String newFileType) {
        this.newFileType = newFileType;
    }

    @Override
    public String toString() {
        return "FileIsUploaded{" +
                "fileId=" + fileId +
                ", fileName='" + fileName + '\'' +
                ", newFileType='" + newFileType + '\'' +
                '}';
    }

    public String getNewEncoding() {
        return newEncoding;
    }

    public void setNewEncoding(String newEncoding) {
        this.newEncoding = newEncoding;
    }
}
