package com.deadpineapple.dal.dao;

import com.deadpineapple.dal.entity.ConvertedFile;
import com.deadpineapple.dal.entity.Transaction;
import com.deadpineapple.dal.entity.UserAccount;

import java.util.List;

/**
 * Created by mikael on 03/05/16.
 */
public interface ITransactionDao {
    public Transaction createTransaction(Transaction transaction);
    public int getNextIdTransaction();
    public List<Transaction> getTransByIdTransaction(int id);
    public List<Transaction> getTransByUser (UserAccount user);
}
