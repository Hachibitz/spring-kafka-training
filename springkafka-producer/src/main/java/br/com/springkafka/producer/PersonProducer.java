package br.com.springkafka.producer;

import br.com.springkafka.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PersonProducer {

    private final String topicName;
    private final KafkaTemplate<String, Person> kafkaTemplate;

    public PersonProducer(@Value("${topic.name}") String topicName, KafkaTemplate<String, Person> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Person people) {
        kafkaTemplate.send(topicName, people).addCallback(
                success -> log.info("Mensagem enviada com sucesso"),
                failure -> log.error("Falha ao enviar mensagem")
        );
    }
}
