package com.deadpineapple.dal.persistence;

import com.deadpineapple.dal.dao.ConvertedFileDao;
import com.deadpineapple.dal.dao.TransactionDao;
import com.deadpineapple.dal.dao.UserDao;
import com.deadpineapple.dal.entity.ConvertedFile;
import com.deadpineapple.dal.entity.SplitFile;
import com.deadpineapple.dal.entity.Transaction;
import com.deadpineapple.dal.entity.UserAccount;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.xml.registry.infomodel.User;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

/**
 * Created by mikael on 30/03/16.
 */
@Service
public class main {

    public static void main(String[] args) {

        Properties prop= new Properties();
        prop.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/dead_pineapple?zeroDateTimeBehavior=convertToNull");
        prop.setProperty("hibernate.connection.username", "root");
        prop.setProperty("hibernate.connection.password", "PYcqu6rzRL8ZFM8q");
        prop.setProperty("dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");

        SessionFactory sessionFactory = new AnnotationConfiguration()
                .addPackage("com.deadpineapple.dal.entity")
                .addProperties(prop)
                .addAnnotatedClass(UserAccount.class)
                .addAnnotatedClass(ConvertedFile.class)
                .addAnnotatedClass(SplitFile.class)
                .addAnnotatedClass(Transaction.class)
                .buildSessionFactory();


        /*UserDao dao = new UserDao(sessionFactory);
        UserAccount u = dao.find("email@email.com");

        dao.deleteUser(u);*/

        TransactionDao dao = new TransactionDao(sessionFactory);
        System.out.println(dao.getTransByIdTransaction(2));
    }
}
