package com.example.pokeApi.repositories;

import com.example.pokeApi.entities.Pokemon;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PokemonRepository extends MongoRepository<Pokemon, String> {

    public List<Pokemon> findAllByName(String name);


    Optional<Object> findById(int id);
}
