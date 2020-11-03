package com.example.pokeApi.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class PokemonInfo {
    @Id
    String id;
    String name;
    String url;
}
