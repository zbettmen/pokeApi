package com.example.pokeApi.services;

import com.example.pokeApi.entities.Pokemon;
import com.example.pokeApi.repositories.PokemonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@CacheConfig(cacheNames = {"pokemons"})
public class PokemonDetailService {
    PokemonRepository pokemonRepository;

    @Cacheable(value = "pokemonCache")
    public List<Pokemon> findAll() {
        log.info("Requesting to find all pokemons...");
        log.info("Fresh data...");
        var pokemons = pokemonRepository.findAll();
        return pokemons;
    }

    @Cacheable(value = "pokemonCache", key = "#id")
    public Pokemon findById(String id){
        return pokemonRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No pokemon found."));
    }

    @Cacheable(value = "pokemonCache")
    public List<Pokemon> findByName(String name) {
        var pokemons = pokemonRepository.findAll();
        pokemons = pokemons.stream()
                .filter(pokemon -> pokemon.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
        return pokemons;
    }

    @Cacheable(value = "pokemonCache")
    public List<Pokemon> findByAbilities(String abilities) {
        var pokemons = pokemonRepository.findAll();
        pokemons = pokemons.stream()
                .filter(pokemon -> pokemon.getAbilities().equals(abilities))
                .collect(Collectors.toList());
        return pokemons;
    }

    @Cacheable(value = "pokemonCache")
    public List<Pokemon> findByHeight(int height){
        var pokemons = pokemonRepository.findAll();
        pokemons = pokemons.stream()
                .filter(pokemon -> pokemon.getHeight() == height)
                .collect(Collectors.toList());
        return pokemons;
    }


    @Cacheable(value = "pokemonCache")
    public List<Pokemon> findByWeight(int weight){
        var pokemons = pokemonRepository.findAll();
        pokemons = pokemons.stream()
                .filter(pokemon -> pokemon.getWeight() == weight)
                .collect(Collectors.toList());
        return pokemons;
    }

    @Cacheable(value = "pokemonCache")
    public List<Pokemon> findBySpecies(String species) {
        var pokemons = pokemonRepository.findAll();
        pokemons = pokemons.stream()
                .filter(pokemon -> pokemon.getSpecies() == species)
                .collect(Collectors.toList());
        return pokemons;
    }


    @CachePut(value = "pokemonCache", key = "#id")
    public void update(String id, Pokemon pokemon) {
        if (!pokemonRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find pokemon %s by id", id));
        }
        pokemon.setId(id);
        pokemonRepository.save(pokemon);
    }

    @CacheEvict(value = "pokemonCache", key = "#id")
    public void delete(String id) {
        if(!pokemonRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find pokemon %s by id", id));
        }
        pokemonRepository.deleteById(id);
    }
}
