package com.example.pokeApi.repositories;

import com.example.pokeApi.entities.PokemonInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PokemonApiRepository extends MongoRepository<PokemonInfo,String> {
}
