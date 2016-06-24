package com.deadpineapple.dal.dao;

import com.deadpineapple.dal.entity.SplitFile;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 * Created by mikael on 31/03/16.
 */

public class SplitFileDao implements ISplitFileDao {

    private SessionFactory sessFact;

    public SplitFileDao(){}

    public SplitFileDao (SessionFactory sessionFactory)
    {
        this.sessFact = sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessFact = sessionFactory;
    }


    @Override
    public SplitFile createFile(SplitFile file) {
        org.hibernate.Session sess = sessFact.openSession();
        Transaction tx = sess.beginTransaction();

        sess.saveOrUpdate(file);

        tx.commit();
        sess.close();
        return file;
    }

    @Override
    public SplitFile updateFile(SplitFile file) {
        org.hibernate.Session sess = sessFact.openSession();
        Transaction tx = sess.beginTransaction();

        sess.update(file);

        tx.commit();
        sess.close();
        return file;
    }

    @Override
    public SplitFile findById(Long id) {
        org.hibernate.Session sess = sessFact.openSession();
        Transaction tx = sess.beginTransaction();
        SplitFile sFile = null;

        Criteria criteria = sess.createCriteria(SplitFile.class);
        criteria.add(Restrictions.eq("id",id));

        sFile = (SplitFile) criteria.uniqueResult();

        sess.close();
        return sFile;
    }

    @Override
    public void deleteFile(SplitFile splitFile)
    {
        org.hibernate.Session sess = sessFact.openSession();
        Transaction tx = sess.beginTransaction();
        sess.delete(splitFile);
        tx.commit();
        sess.close();
    }

    @Override
    public void deleteFileById(Long id)
    {
        org.hibernate.Session sess = sessFact.openSession();
        Transaction tx = sess.beginTransaction();

        // only need to specify the ID to delete it
        SplitFile splitFile = (SplitFile) sess.createCriteria(SplitFile.class)
                .add(Restrictions.eq("id", id)).uniqueResult();
        sess.delete(splitFile);

        tx.commit();
        sess.close();
    }

}