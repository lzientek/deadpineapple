package com.deadpineapple.dal.entity;

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;
import org.hibernate.hql.internal.ast.tree.BooleanLiteralNode;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by mikael on 02/05/16.
 */
@Entity
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = UserAccount.class)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserAccount userAccount;

    @ManyToOne(targetEntity = ConvertedFile.class)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private ConvertedFile convertedFiles;

    private Date date;

    private double prix;

    private int idTransaction;

    private Boolean isPayed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public ConvertedFile getConvertedFiles() {
        return convertedFiles;
    }

    public void setConvertedFiles(ConvertedFile convertedFiles) {
        this.convertedFiles = convertedFiles;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(int idTransaction) {
        this.idTransaction = idTransaction;
    }

    public Boolean getPayed() {
        return isPayed;
    }

    public void setPayed(Boolean payed) {
        isPayed = payed;
    }
}
