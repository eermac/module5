package main.java;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerApp {

    public static void main(String[] args) {

        int MSG_COUNT = 5;

        String HOST = "rc1a-0dgajci51b68rpmn.mdb.yandexcloud.net:9091";
        String TOPIC = "test";
        String USER = "user";
        String PASS = "password";

        String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, USER, PASS);
        String KEY = "key";

        String serializer = StringSerializer.class.getName();
        Properties props = new Properties();
        props.put("bootstrap.servers", HOST);
        props.put("acks", "all");
        props.put("key.serializer", serializer);
        props.put("value.serializer", serializer);
        props.put("security.protocol", "SASL_SSL");
        props.put("ssl.truststore.location", "./secret/kafka.truststore.jks");
        props.put("ssl.truststore.password", "password");
        props.put("sasl.mechanism", "SCRAM-SHA-512");
        props.put("sasl.jaas.config", jaasCfg);

        Producer<String, String> producer = new KafkaProducer<>(props);

        try {
            for (int i = 1; i <= MSG_COUNT; i++){
                producer.send(new ProducerRecord<String, String>(TOPIC, KEY, "test message 2")).get();
                System.out.println("Test message " + i);
            }
            producer.flush();
            producer.close();
        } catch (Exception ex) {
            System.out.println(ex);
            producer.close();
        }
    }
}

