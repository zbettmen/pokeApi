package com.example.pokeApi.controllers;


import com.example.pokeApi.entities.Pokemon;
import com.example.pokeApi.services.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/v1/pokemon")
public class PokemonController {

    @Autowired
    private PokemonService pokemonService;

    //find pokemon by name or id
    @GetMapping("/{name}")
    public ResponseEntity<List<Pokemon>> findPokemon(@PathVariable String name){
        List<Pokemon> pokemons = pokemonService.search(name);
        return ResponseEntity.ok(pokemons);
    }



}
