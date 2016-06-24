package com.deadpineapple.dal.dao;

import com.deadpineapple.dal.entity.SplitFile;

/**
 * Created by mikael on 31/03/16.
 */
public interface ISplitFileDao {
    public SplitFile createFile(SplitFile file);
    public SplitFile updateFile(SplitFile file);
    public SplitFile findById(Long id);
    public void deleteFile(SplitFile splitFile);
    public void deleteFileById(Long id);
}