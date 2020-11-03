package com.example.pokeApi.services;


import com.example.pokeApi.entities.Pokemon;
import com.example.pokeApi.repositories.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@ConfigurationProperties(value = "pokemon.url", ignoreUnknownFields = false)
public class PokemonService {
    private final RestTemplate restTemplate;
    private String url = "https://pokeapi.co/api/v2/pokemon/";

    @Autowired
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
                        pokemonDto.getName(),
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

    public Pokemon save(Pokemon pokemon){
        return pokemonRepository.save(pokemon);
    }

    @Cacheable(cacheNames = "pokemonCache",key = "#id",value = "pokemonCache")
    public Pokemon findById(String id){
        return pokemonRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No pokemon found."));
    }
    @Cacheable(cacheNames = "pokemonCache",key = "#id",value = "pokemonCache")
    public List<Pokemon> findAll(String weight,String height,String name){
        var pokemons = pokemonRepository.findAll();


        if(weight != null){
            pokemons = pokemons.stream().filter(p -> p.getWeight() == Integer.parseInt(weight)).collect(Collectors.toList());
            if (pokemons.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find any pokemon in your database.");
            }
        }
        if (height != null){
            pokemons = pokemons.stream().filter(p -> p.getHeight() == Integer.parseInt(height)).collect(Collectors.toList());
            if (pokemons.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find any pokemon in your database.");
            }


        }
        if (name != null){
            pokemons = pokemons.stream().filter(p -> p.getName().contains(name.toLowerCase())).collect(Collectors.toList());
            if (pokemons.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find any pokemon in your database.");

            }


        }

        return pokemons;

    }
    @CacheEvict(cacheNames = "pokemonCache",value = "pokemonCache", key = "#id")
    public void delete(String id) {
        if(!pokemonRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find pokemon %s by id", id));
        }
        pokemonRepository.deleteById(id);
    }

}






