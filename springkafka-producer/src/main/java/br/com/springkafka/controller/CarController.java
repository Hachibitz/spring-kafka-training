package br.com.springkafka.controller;

import br.com.springkafka.Car;
import br.com.springkafka.model.dto.CarDTO;
import br.com.springkafka.producer.CarProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarProducer carProducer;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendMessage(@RequestBody CarDTO carDTO) {
        String id = UUID.randomUUID().toString();

        Car message = Car.newBuilder()
                .setId(id)
                .setModel(carDTO.getModel())
                .setBranch(carDTO.getBranch())
                .build();

        carProducer.sendMessage(message);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
