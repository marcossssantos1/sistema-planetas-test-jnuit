package com.marcos.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marcos.demo.domain.Planet;
import com.marcos.demo.service.PlanetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/planets")
public class PlanetController {

	@Autowired
	private PlanetService service;

	@PostMapping
	public ResponseEntity<Planet> create(@RequestBody @Valid Planet planet) {
		Planet planetCreated = service.create(planet);
		return ResponseEntity.status(HttpStatus.CREATED).body(planetCreated);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Planet> getById(@PathVariable @Valid Long id) {
		return service.getId(id).map(planet -> ResponseEntity.ok(planet))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<Planet> getByName(@PathVariable String name) {
		return service.getByName(name).map(planet -> ResponseEntity.ok(planet))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<List<Planet>> getAll(@RequestParam(required = false) String climate, 
			@RequestParam (required = false) String terrain) {
		List<Planet> planets = service.list(climate, terrain);
		return (ResponseEntity<List<Planet>>) ResponseEntity.ok();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
