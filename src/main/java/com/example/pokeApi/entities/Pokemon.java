package com.example.pokeApi.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Slf4j
@AllArgsConstructor

public class Pokemon {

    @Id
    private String id;
    private String name;
    private Object species;
    private int weight;
    private int height;
    private List<Object> abilities;

    public Pokemon(){

    }


    public Pokemon(String id, Object species, int weight, int height, List<Object> abilities) {
    }
}
