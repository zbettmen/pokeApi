package com.example.pokeApi.services;

import com.example.pokeApi.entities.PokemonInfo;
import com.example.pokeApi.entities.PokemonList;
import com.example.pokeApi.repositories.PokemonApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;


@Service
public class PokemonApiService {
    private final RestTemplate restTemplate;
    public static final String POKEMONS_URL = "https://pokeapi.co/api/v2/pokemon?offset=0&limit=1050";

    public PokemonApiService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Autowired
    PokemonApiRepository pokemonApiRepository;

    @Cacheable(value = "pokemonCache")
    public List<PokemonInfo> getAllPokemonsAvailable() {
        List<PokemonInfo> pokemonsInfoFromDB = pokemonApiRepository.findAll();
        if (pokemonsInfoFromDB.isEmpty()) {
            PokemonList response = restTemplate.getForObject(POKEMONS_URL, PokemonList.class);
            List<PokemonInfo> pokemonList = response.getResults();
            for (var p : pokemonList) {
                pokemonApiRepository.save(p);

            }

            return pokemonList;
        }
        return pokemonsInfoFromDB;
    }


}
