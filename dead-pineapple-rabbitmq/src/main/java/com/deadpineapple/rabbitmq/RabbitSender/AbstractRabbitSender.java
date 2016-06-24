package com.deadpineapple.rabbitmq.RabbitSender;

import com.deadpineapple.rabbitmq.RabbitConnection;
import com.deadpineapple.rabbitmq.Serializer.ObjectByteSerializer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by 15256 on 10/03/2016.
 */
public abstract class AbstractRabbitSender<T> {
    protected String configPath;

    public abstract String getQueueName();


    public String getConfigPath() {
        return configPath;
    }

    private RabbitConnection rabbitConnection;

    public RabbitConnection getRabbitConnection() {
        if (rabbitConnection == null) {
            try {
                if (getConfigPath() != null) {
                    rabbitConnection = new RabbitConnection(getConfigPath());
                } else {
                    rabbitConnection = new RabbitConnection();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            rabbitConnection.declareQueue(getQueueName());
        }
        return rabbitConnection;
    }

    public boolean send(T obj) {
        try {
            publish(ObjectByteSerializer.Serialize(obj));
            System.out.println(" [x] Sent '" + obj + "'");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void publish(byte[] bytes) throws IOException {
        getRabbitConnection().publishOnQueue(getQueueName(), bytes);
    }

    public void close() throws IOException, TimeoutException {
        getRabbitConnection().close();
    }

}
