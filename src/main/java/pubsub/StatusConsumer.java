/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pubsub;

/**
 *
 * @author asimkaymak
 */

import connections.IAppConfigs;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import message.types.Status;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import serialization.ObjectDeserializer;

public class StatusConsumer {
    static Logger logger = LogManager.getLogger(StatusProducer.class.getName());
    public static void main(String[] args) {
        logger.info("Status consumer starting...");
        startConsumer();
    }

    public static void startConsumer() {
        KafkaConsumer<String, Status> kafkaConsumer = getKafkaConsumer();
        kafkaConsumer.subscribe(Collections.singletonList(IAppConfigs.STATUS_TOPIC));
        logger.info("Consumer initialized!");
        while (true) {
            try {
                ConsumerRecords<String, Status> orderRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
                orderRecords.forEach(record -> {
                    System.out.println(record.value().toString());
                });
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }
    }

    public static KafkaConsumer<String, Status> getKafkaConsumer() {
        KafkaConsumer<String, Status> kafkaConsumer = new KafkaConsumer<String, Status>(getKafkaConsumerConfig());
        return kafkaConsumer;
    }

    private static Properties getKafkaConsumerConfig() {
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.CLIENT_ID_CONFIG, IAppConfigs.APPLICATION_ID_CONFIG);
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, IAppConfigs.BOOTSTAP_SERVER);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ObjectDeserializer.class);
        // consumerProps.put(CustomDeserializer.VALUE_CLASS_NAME_CONFIG, OrderInvoice.class);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "Sample-grp_id");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return consumerProps;
    }
}
