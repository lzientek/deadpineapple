package com.deadpineapple.dal.dao;

import com.deadpineapple.dal.entity.ConvertedFile;
import com.deadpineapple.dal.entity.UserAccount;

import java.util.List;

/**
 * Created by mikael on 31/03/16.
 */
public interface IConvertedFileDao {
    public ConvertedFile createFile(ConvertedFile file);
    public ConvertedFile updateFile(ConvertedFile file);
    public ConvertedFile findById(Long id);
    public List<ConvertedFile> findByUser(UserAccount user);
    public void deleteFile(ConvertedFile convertedFile);
    public void deleteFileById(Long id);
}