package br.com.springkafka.consumer;

import br.com.springkafka.Car;
import br.com.springkafka.Person;
import br.com.springkafka.PersonRepository.CarRepository;
import br.com.springkafka.PersonRepository.PersonRepository;
import br.com.springkafka.model.entity.BookEntity;
import br.com.springkafka.model.entity.CarEntity;
import br.com.springkafka.model.entity.PersonEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
@KafkaListener(topics = "${topic.name}")
public class Consumer {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    CarRepository carRepository;

    @KafkaHandler
    public void peopleConsumer(ConsumerRecord<String, Person> record, Acknowledgment ack) {
        Person person = record.value();
        log.info("Mensagem consumida" + person.toString());

        PersonEntity personEntity = new PersonEntity(person.getId().toString(), person.getName().toString(), person.getCpf().toString(), null);
        personEntity.setBookEntities(person.getBooks().stream()
                .map(book -> BookEntity.builder().personEntity(personEntity)
                        .name(book.toString())
                        .build())
                .collect(Collectors.toList()));

        personRepository.save(personEntity);
        ack.acknowledge();
    }

    @KafkaHandler
    public void carConsumer(ConsumerRecord<String, Car> record, Acknowledgment ack) {
        Car car = record.value();
        log.info("Mensagem consumida" + car.toString());

        CarEntity carEntity = new CarEntity(car.getId().toString(), car.getModel().toString(), car.getBranch().toString());

        carRepository.save(carEntity);
        ack.acknowledge();
    }

    @KafkaHandler(isDefault = true)
    public void unknown(Object object, Acknowledgment ack) {
        log.info("Mensagem consumida" + object.toString());
        ack.acknowledge();
    }
}
