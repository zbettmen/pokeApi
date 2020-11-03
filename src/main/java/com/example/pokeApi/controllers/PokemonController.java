package com.example.pokeApi.controllers;

import com.example.pokeApi.entities.Pokemon;
import com.example.pokeApi.entities.PokemonInfo;
import com.example.pokeApi.services.PokemonApiService;
import com.example.pokeApi.services.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/rest/v1/pokemon")
public class PokemonController {

    @Autowired
    private PokemonService pokemonService;

    @Autowired
    PokemonApiService pokemonApiService;

    @GetMapping("/open")
    public ResponseEntity<List<Pokemon>> findAll(
            @RequestParam(required = false) String weight,
            @RequestParam(required = false) String height,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String id) {

        return ResponseEntity.ok(pokemonService.findAll(weight, height, name, id));

    }

    @GetMapping("/api")
    public ResponseEntity<List<PokemonInfo>> getAllPokemonsAvailable() {
        List<PokemonInfo> pokemonsAvailable = pokemonApiService.getAllPokemonsAvailable();
        return ResponseEntity.ok(pokemonsAvailable);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deletePokemon(@PathVariable @RequestBody String id) {
        try {
            pokemonService.delete(id);
        } catch (NumberFormatException ex) {

        }
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{name}")
    public ResponseEntity<List<Pokemon>> findPokemon(@PathVariable String name) {
        List<Pokemon> pokemons = pokemonService.search(name);
        return ResponseEntity.ok(pokemons);
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable String id, @RequestBody Pokemon pokemon) {
        pokemonService.update(id, pokemon);
    }


}