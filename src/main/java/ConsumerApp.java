package main.java;

import java.util.*;
import org.apache.kafka.common.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.clients.consumer.*;

public class ConsumerApp {

    public static void main(String[] args) {

        String HOST = "rc1a-0dgajci51b68rpmn.mdb.yandexcloud.net:9091,rc1b-fl8llba4fvqd168b.mdb.yandexcloud.net:9091,rc1d-lk1gpmvi02nts9d0.mdb.yandexcloud.net:9091";
        String TOPIC = "test";
        String USER = "userConsumer";
        String PASS = "123321123";
        String TS_FILE = "./secret/kafka.truststore.jks";
        String TS_PASS = "password";

        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, USER, PASS);
        String GROUP = "demo";

        String deserializer = StringDeserializer.class.getName();
        Properties props = new Properties();
        props.put("bootstrap.servers", HOST);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put("group.id", GROUP);
        props.put("key.deserializer", deserializer);
        props.put("value.deserializer", deserializer);
        props.put("security.protocol", "SASL_SSL");
        props.put("ssl.truststore.location", "./secret/kafka.truststore.jks");
        props.put("ssl.truststore.password", "password");
        props.put("sasl.mechanism", "SCRAM-SHA-512");
        props.put("sasl.jaas.config", jaasCfg);
        props.put("ssl.truststore.location", TS_FILE);
        props.put("ssl.truststore.password", TS_PASS);

        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(new String[] {TOPIC}));

        while(true) {
            ConsumerRecords<String, String> records = consumer.poll(10001);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.key() + ":" + record.value());
            }
        }
    }
}

