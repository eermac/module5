package main.java;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.header.internals.RecordHeader;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer;

import java.util.Properties;

public class ProducerWithSR {

    public static void main(String[] args) {
        String bootstrapServers = "localhost:9094";
        String schemaRegistryUrl = "http://localhost:8081";
        String topic = "user";

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSchemaSerializer.class.getName());
        props.put("schema.registry.url", schemaRegistryUrl);
        props.put("auto.register.schemas", true);
        props.put("use.latest.version", true);


        SchemaRegistryClient schemaRegistryClient = new CachedSchemaRegistryClient(schemaRegistryUrl, 10);
        KafkaProducer<String, User> producer = new KafkaProducer<>(props);

        // Register schema
        String userSchema = "{ \"$schema\": \"http://json-schema.org/draft-07/schema#\", \"title\": \"User\", \"type\": \"object\", \"properties\": { \"name\": { \"type\": \"string\" }, \"favoriteNumber\": { \"type\": \"integer\" }, \"favoriteColor\": { \"type\": \"string\" } }, \"required\": [\"name\", \"favoriteNumber\", \"favoriteColor\"] }";
        SchemaRegistryHelper.registerSchema(schemaRegistryClient, topic, userSchema);


        User user = new User("First user", 42, "blue");


        ProducerRecord<String, User> record = new ProducerRecord<>(topic, "first-user", user);
        record.headers().add(new RecordHeader("myTestHeader", "header values are binary".getBytes()));


        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                System.err.println("Delivery failed: " + exception.getMessage());
                exception.printStackTrace(System.err);
            } else {
                System.out.printf("Delivered message to topic %s [%d] at offset %d%n",
                        metadata.topic(), metadata.partition(), metadata.offset());
            }
        });


        producer.close();
    }
}