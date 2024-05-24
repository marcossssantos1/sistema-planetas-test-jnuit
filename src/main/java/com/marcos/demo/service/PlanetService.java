package com.marcos.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.marcos.demo.domain.Planet;
import com.marcos.demo.domain.QueryBuilder;
import com.marcos.demo.repository.PlanetRepository;

@Service
public class PlanetService {
	
	@Autowired
	private PlanetRepository repository;
	
	public PlanetService(PlanetRepository repository) {
		this.repository = repository;
	}
	
	
	public Planet create(Planet planet) {
		return repository.save(planet);
	}
	
	public Optional<Planet> getId(Long id){
		return repository.findById(id);
	}
	
	public Optional<Planet> getByName(String name){
		return repository.findByName(name);
	}
	
	public List<Planet> list(String climate, String terrain){
		Example<Planet> query = QueryBuilder.makeQuery(new Planet(climate, terrain));
		return repository.findAll(query);
	}

	
	public void delete(Long id) {
		repository.deleteById(id);
	}
}
