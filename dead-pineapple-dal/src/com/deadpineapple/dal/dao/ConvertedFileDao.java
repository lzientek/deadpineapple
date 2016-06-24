package com.deadpineapple.dal.dao;

import com.deadpineapple.dal.entity.ConvertedFile;
import com.deadpineapple.dal.entity.UserAccount;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.mapping.List;


/**
 * Created by mikael on 31/03/16.
 */

public class ConvertedFileDao implements IConvertedFileDao {

    private SessionFactory sessFact;

    public ConvertedFileDao(){}

    public ConvertedFileDao (SessionFactory sessionFactory)
    {
        this.sessFact = sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessFact = sessionFactory;
    }

    @Override
    public ConvertedFile createFile(ConvertedFile file) {
        org.hibernate.Session sess = sessFact.openSession();
        Transaction tx = sess.beginTransaction();

        sess.saveOrUpdate(file);

        tx.commit();
        sess.close();
        return file;
    }

    @Override
    public ConvertedFile updateFile(ConvertedFile file) {
        org.hibernate.Session sess = sessFact.openSession();
        Transaction tx = sess.beginTransaction();

        sess.update(file);

        tx.commit();
        sess.close();
        return file;
    }

    @Override
    public ConvertedFile findById(Long id) {
        org.hibernate.Session sess = sessFact.openSession();
        Transaction tx = sess.beginTransaction();
        ConvertedFile cFile = null;

        Criteria criteria = sess.createCriteria(ConvertedFile.class);
        criteria.add(Restrictions.eq("id",id));

        cFile = (ConvertedFile) criteria.uniqueResult();

        sess.close();
        return cFile;
    }

    @Override
    public java.util.List<ConvertedFile> findByUser(UserAccount user)
    {
        org.hibernate.Session sess = sessFact.openSession();
        Transaction tx = sess.beginTransaction();
        java.util.List<ConvertedFile> cFile = null;

        Criteria criteria = sess.createCriteria(ConvertedFile.class);
        criteria.add(Restrictions.eq("userAccount",user));

        cFile = (java.util.List<ConvertedFile>) criteria.list();

        sess.close();
        return cFile;
    }

    @Override
    public void deleteFile(ConvertedFile convertedFile)
    {
        org.hibernate.Session sess = sessFact.openSession();
        Transaction tx = sess.beginTransaction();

        // set the file to null on Transaction, if the transaction is already create
        setNullOnTransaction(convertedFile);

        // delete the file and commit
        sess.delete(convertedFile);
        tx.commit();
        sess.close();
    }

    @Override
    public void deleteFileById(Long id)
    {
        org.hibernate.Session sess = sessFact.openSession();
        Transaction tx = sess.beginTransaction();

        // only need to specify the ID to delete it
        ConvertedFile splitFile = (ConvertedFile) sess.createCriteria(ConvertedFile.class)
                .add(Restrictions.eq("id", id)).uniqueResult();

        // set the file to null on Transaction, if the transaction is already create
        setNullOnTransaction(splitFile);

        // delete the file
        sess.delete(splitFile);

        tx.commit();
        sess.close();
    }

    private void setNullOnTransaction(ConvertedFile file)
    {
        org.hibernate.Session sess = sessFact.openSession();
        Transaction tx = sess.beginTransaction();

        // set the ConvertedFile to null on Transaction table
        // allow the transaction row to persist in the DB
        Criteria criteria = sess.createCriteria(com.deadpineapple.dal.entity.Transaction.class);
        criteria.add(Restrictions.eq("convertedFiles",file));

        // get the transaction, set file to null and update
        try {
            java.util.List<com.deadpineapple.dal.entity.Transaction> listTrans =
                    (java.util.List<com.deadpineapple.dal.entity.Transaction>) criteria.list();

            for (com.deadpineapple.dal.entity.Transaction t : listTrans) {
                t.setConvertedFiles(null);
                sess.update(t);
            }
            tx.commit();
        }
        catch (Exception e){}

        sess.close();
    }
}