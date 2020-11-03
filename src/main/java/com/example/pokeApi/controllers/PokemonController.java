package com.example.pokeApi.controllers;


import com.example.pokeApi.entities.Pokemon;
import com.example.pokeApi.services.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/rest/v1/pokemon")
public class PokemonController {

    @Autowired
    private PokemonService pokemonService;


    @GetMapping("/{name}")
    public ResponseEntity<List<Pokemon>> findPokemon(@PathVariable String name){
        List<Pokemon> pokemons = pokemonService.search(name);
        return ResponseEntity.ok(pokemons);
    }


   @GetMapping("/open")
    public ResponseEntity<List<Pokemon>> findAll(
            @RequestParam(required = false) String weight,
            @RequestParam(required = false) String height,
           @RequestParam(required = false) String name){
        return ResponseEntity.ok(pokemonService.findAll(weight,height,name));


    }
    @Secured("ROLE_ADMIN")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteBook(@PathVariable @RequestBody String id) {
        try{
            pokemonService.delete(id);
        } catch(NumberFormatException ex){

        }
    }


}