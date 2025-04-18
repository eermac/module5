package main.java;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.errors.WakeupException;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer;


import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class ConsumerWithSR {

    public static void main(String[] args) {
        String bootstrapServers = "localhost:9094,localhost:9095,localhost:9096";
        String schemaRegistryUrl = "http://localhost:8081";
        String topic = "user";
        String groupId = "group";

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaJsonSchemaDeserializer.class.getName());
        props.put("schema.registry.url", schemaRegistryUrl);

        KafkaConsumer<String, User> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));

        final CountDownLatch latch = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Caught shutdown signal, closing consumer...");
            consumer.wakeup();
            latch.countDown();
        }));

        try {
            while (true) {
                ConsumerRecords<String, User> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, User> record : records) {
                    System.out.printf("Message on %s:%n%s%n", record.topic(), record.value());
                    if (record.headers() != null) {
                        System.out.printf("Headers: %s%n", record.headers());
                    }
                }
            }
        } catch (WakeupException e) {
            // Expected during shutdown
        } finally {
            consumer.close();
            System.out.println("Consumer closed");
            latch.countDown();
        }
    }
}
