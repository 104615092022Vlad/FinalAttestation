package com.example.shop;

import  org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Properties;

public class Configuration {
    public static Properties getProps() {
        Properties props = new Properties();
        props.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        props.setProperty("hibernate.connection.url", "jdbc:postgresql://stampy.db.elephantsql.com:5432/nykbjfyu");
        props.setProperty("hibernate.connection.username", "nykbjfyu");
        props.setProperty("hibernate.connection.password", "BJ0QQMsy2UK45xWFY1PP9GNxa3yNr2jh");
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.setProperty("hibernate.show_sql", "true");
        props.setProperty("hibernate.hbm2ddl.auto", "update");
        props.setProperty("hibernate.connection.autocommit", "true");
        return props;
    }

    public static SessionFactory buildFactory() {
        try {
            final org.hibernate.cfg.Configuration config = new org.hibernate.cfg.Configuration()
                    .addAnnotatedClass(com.example.shop.models.ShopPojo.class)
                    .addProperties(getProps());
            return config.buildSessionFactory();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Session createNewSession(SessionFactory factory) {
        return factory.openSession();
    }

}
