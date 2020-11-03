package com.example.pokeApi.entities;

import java.util.ArrayList;
import java.util.List;

public class PokemonList {
    private List<PokemonInfo> results;

    public PokemonList(List<PokemonInfo> results) {
        this.results = new ArrayList<>();
    }

    public List<PokemonInfo> getResults() {
        return results;
    }

    public void setResults(List<PokemonInfo> results) {
        this.results = results;
    }
}
