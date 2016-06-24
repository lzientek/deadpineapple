package com.deadpineapple.rabbitmq.XmlConfig;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;

/**
 * Created by 15256 on 21/05/2016.
 */
public class RabbitConfig {
    private String host;
    private int port;
    private String username;
    private String password;

    public RabbitConfig(String host, int port, String username, String password) {

        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public static RabbitConfig read(){
        return read(System.getProperty("user.dir")+ "/dead-pineapple-rabbitmq/src/main/java/com/deadpineapple/rabbitmq/rabbitConfig.xml");
    }

    public static RabbitConfig read(String path) {
        SAXBuilder sxb = new SAXBuilder();
        try {

            Document document = sxb.build(new File(path).toString());
            Element racine = document.getRootElement();
            Element connectionInfo = racine.getChild("connection");
            Element server  = connectionInfo.getChild("serveur");
            Element user  = connectionInfo.getChild("user");
            return new RabbitConfig(server.getAttributeValue("host"),Integer.parseInt(server.getAttributeValue("port")),user.getAttributeValue("name"),user.getAttributeValue("password"));

        } catch (Exception e) {
            return  null;
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
