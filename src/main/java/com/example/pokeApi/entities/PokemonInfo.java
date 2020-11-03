package com.example.pokeApi.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class PokemonInfo {
    String name;
    String url;
}
