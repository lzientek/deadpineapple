package com.deadpineapple.dal.entity;

import org.hibernate.annotations.Formula;
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
public class UserAccount implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String password;

    private Date creationDate;

    private String firstName;

    private String lastName;

    private String phone;

    private String adresse;

    private int codePostal;

    private String ville;

    @OneToMany(mappedBy = "userAccount")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ConvertedFile> convertedFiles;

    @Formula("(SELECT COALESCE(SUM(cf.size),0) FROM ConvertedFile cf WHERE id=cf.userAccountId)")
    private double totalSize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ConvertedFile> getConvertedFiles() {
        return convertedFiles;
    }

    public void setConvertedFiles(List<ConvertedFile> convertedFiles) {
        this.convertedFiles = convertedFiles;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public double getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(double totalSize) {
        this.totalSize = totalSize;
    }
}
