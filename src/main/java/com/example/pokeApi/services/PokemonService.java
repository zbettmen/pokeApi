package com.example.pokeApi.services;


import com.example.pokeApi.entities.Pokemon;
import com.example.pokeApi.repositories.PokemonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@ConfigurationProperties(value = "pokemon.url", ignoreUnknownFields = false)
public class PokemonService {
    private final RestTemplate restTemplate;
    private String url = "https://pokeapi.co/api/v2/pokemon/";

    PokemonRepository pokemonRepository;

    public PokemonService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Pokemon> search(String name){
        var urlWithTitleQuery = url + name;

        //check the database
        var pokemons = pokemonRepository.findAllByName(name);
        //if the pokemon doesn't exist in the db - check PokeApi
        if(pokemons.isEmpty()){
            System.out.println("no pokemon in db");
            var pokemonDto = restTemplate.getForObject(urlWithTitleQuery, Pokemon.class);
            //if pokemon not found on PokeApi throw exception
            if(pokemonDto == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No pokemon found.");
            } //if found create new pokemon and save to db
            else {
                var newPokemon = new Pokemon(

                        pokemonDto.getId(),
                        pokemonDto.getSpecies(),
                        pokemonDto.getWeight(),
                        pokemonDto.getHeight(),
                        pokemonDto.getAbilities()
                );
                pokemons.add(this.save(newPokemon));
            }
        }
        return pokemons;
    }


    //@CachePut(value = "pokemonCache", key = "#result.id")
    public Pokemon save(Pokemon pokemon){
        return pokemonRepository.save(pokemon);
    }

    //@Cacheable(value = "pokemonCache")
    public List<Pokemon> findAll(String name, String ability, int height, int weight, String species) {
        var pokemons = pokemonRepository.findAll();
        if(name != null) {
            pokemons = pokemons.stream()
                    .filter(pokemon -> pokemon.getName().startsWith(name) ||
                            pokemon.getAbilities().equals(ability) ||
                            pokemon.getHeight() == height ||
                            pokemon.getWeight() == weight ||
                            pokemon.getSpecies().equals(species))
                    .collect(Collectors.toList());
        }
        return pokemons;
    }
}
