package com.example.pokeApi.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class Pokemon {
    //id's in MongoDb are stored as ObjectIds (uuid)
    @Id
    private String id;
    private String name;
    private Object species;
    private int weight;
    private int height;
    private List<Object> abilities;

    public Pokemon(){

    }
}
