package br.com.springkafka.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class PersonDTO {

    private String name;
    private String cpf;
    private List<String> books;
}
