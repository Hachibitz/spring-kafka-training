package br.com.springkafka.controller;

import br.com.springkafka.Person;
import br.com.springkafka.model.dto.PersonDTO;
import br.com.springkafka.producer.PersonProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/people")
public class PeopleController {

    @Autowired
    private PersonProducer personProducer;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendMessage(@RequestBody PersonDTO personDTO) {
        String id = UUID.randomUUID().toString();

        Person message = Person.newBuilder()
                .setId(id)
                .setCpf(personDTO.getCpf())
                .setName(personDTO.getName())
                .setBooks(personDTO.getBooks().stream().map(p -> (CharSequence) p).collect(Collectors.toList()))
                .build();

        personProducer.sendMessage(message);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
