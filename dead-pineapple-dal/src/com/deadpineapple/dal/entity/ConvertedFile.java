package com.deadpineapple.dal.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by 15256 on 01/03/2016.
 */
@Entity
public class ConvertedFile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "convertedFile", targetEntity = SplitFile.class,fetch =FetchType.EAGER )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<SplitFile> splitFiles;

    @ManyToOne(targetEntity = UserAccount.class)
    @JoinColumn(name = "userAccountId")
    private UserAccount userAccount;

    private double size;

    private Boolean isConverted;

    private Boolean isInConvertion;

    private String filePath;

    private String originalName;

    private String oldType;

    private String newType;

    private Date creationDate;

    private Date convertedDate;
    private String newEncoding;

    public ConvertedFile() {
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<SplitFile> getSplitFiles() {
        return splitFiles;
    }

    public void setSplitFiles(List<SplitFile> splitFiles) {
        this.splitFiles = splitFiles;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public Boolean getConverted() {
        return isConverted;
    }

    public void setConverted(Boolean converted) {
        isConverted = converted;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getOldType() {
        return oldType;
    }

    public void setOldType(String oldType) {
        this.oldType = oldType;
    }

    public String getNewType() {
        return newType;
    }

    public void setNewType(String newType) {
        this.newType = newType;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getConvertedDate() {
        return convertedDate;
    }

    public void setConvertedDate(Date convertedDate) {
        this.convertedDate = convertedDate;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Boolean getInConvertion() {
        return isInConvertion;
    }

    public void setInConvertion(Boolean inConvertion) {
        isInConvertion = inConvertion;
    }

    public String getNewEncoding() {
        return newEncoding;
    }

    public void setNewEncoding(String newEncoding) {
        this.newEncoding = newEncoding;
    }
}
