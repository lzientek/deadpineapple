package com.deadpineapple.core;

import com.deadpineapple.dal.entity.ConvertedFile;
import com.deadpineapple.dal.entity.SplitFile;
import com.deadpineapple.dal.entity.Transaction;
import com.deadpineapple.dal.entity.UserAccount;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by mikael on 14/04/16.
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;

    static{
        try {
            // Création de la SessionFactory
            Properties prop= new Properties();
            prop.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/dead_pineapple?zeroDateTimeBehavior=convertToNull");
            prop.setProperty("hibernate.connection.username", "root");
            prop.setProperty("hibernate.connection.password", "PYcqu6rzRL8ZFM8q");
            prop.setProperty("dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");

            sessionFactory = new AnnotationConfiguration()
                    .addPackage("com.deadpineapple.dal.entity")
                    .addProperties(prop)
                    .addAnnotatedClass(UserAccount.class)
                    .addAnnotatedClass(ConvertedFile.class)
                    .addAnnotatedClass(SplitFile.class)
                    .addAnnotatedClass(Transaction.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
