package com.deadpineapple.front.tools;

import com.deadpineapple.dal.entity.ConvertedFile;
import com.deadpineapple.dal.entity.Transaction;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by saziri on 16/05/2016.
 */
public class Invoice {
    private ArrayList<ConvertedFile>convertedFiles;
    private Date date;
    private double price;

    private Long invoiceId;

    public Invoice() {
        this.convertedFiles = new ArrayList();
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<ConvertedFile> getConvertedFiles() {
        return convertedFiles;
    }

    public void addConvertedFile(ConvertedFile convertedFile) {
        convertedFiles.add(convertedFile);
    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
