package com.deadpineapple.rabbitmq.RabbitReceiver;

/**
 * Created by 15256 on 10/03/2016.
 */
public interface IReceiver<T> {
     void execute(T result);
}
