package com.marcos.demo.web;

import static com.marcos.demo.common.PlanetConstants.PLANET;
import static com.marcos.demo.common.PlanetConstants.PLANETS;
import static com.marcos.demo.common.PlanetConstants.TATOOINE;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcos.demo.controller.PlanetController;
import com.marcos.demo.domain.Planet;
import com.marcos.demo.service.PlanetService;

@WebMvcTest(PlanetController.class)
public class PlanetControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private PlanetService service;

	@Test
	public void createPlanet_WithDataValid_ReturnCreated() throws Exception {

		when(service.create(PLANET)).thenReturn(PLANET);

		mvc.perform(post("/planets").content(mapper.writeValueAsString(PLANET)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
		// .andExpect(jsonPath("$").value(PLANET));

	}

	@Test
	public void createPlanet_WithInvalidData_ReturnsBadRequest() throws JsonProcessingException, Exception {
		Planet nullPlanet = new Planet();
		Planet invalidPlanet = new Planet("", "", "");

		mvc.perform(
				post("/planets").content(mapper.writeValueAsString(nullPlanet)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity());

		mvc.perform(post("/planets").content(mapper.writeValueAsString(invalidPlanet))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void createPlanet_WithExistingName_ReturnsConflict() throws Exception{
		when(service.create(PLANET)).thenThrow(DataIntegrityViolationException.class);	
		
		mvc.perform(post("/planets").content(mapper.writeValueAsString(PLANET))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict());
		}
	
	@Test
	public void getPlanet_ByIdExisting_ReturnsPlanet() throws Exception {		
		when(service.getId(1L)).thenReturn(Optional.of(PLANET));

		mvc.perform(get("/planets/1")).andExpect(status().isOk());
	}
	
	@Test
	public void getPlanet_ByIduExisting_ReturnsPlanet() throws Exception {		
  	    mvc.perform(get("/planets/1")).andExpect(status().isNotFound());
	}
	
	@Test
	public void getPlanet_ByNameExisting_ReturnsPlanet() throws Exception {		
		when(service.getByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

		mvc.perform(get("/planets/name/ " + PLANET.getName())).andExpect(status().isOk());
	}
	
	@Test
	public void getPlanet_ByIdNameInvalid_ReturnsPlanet() throws Exception {		
  	    mvc.perform(get("/planets/name/1")).andExpect(status().isNotFound());
	}
	
	@Test
	  public void listPlanets_ReturnsFilteredPlanets() throws Exception {
	    when(service.list(null, null)).thenReturn(PLANETS);
	    when(service.list(TATOOINE.getTerain(), TATOOINE.getClimate())).thenReturn(List.of(TATOOINE));

	    mvc
	        .perform(
	            get("/planets"))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$", hasSize(3)));

	    mvc
	        .perform(
	            get("/planets?" + String.format("terrain=%s&climate=%s", TATOOINE.getTerain(), TATOOINE.getClimate())))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$", hasSize(1)))
	        .andExpect(jsonPath("$[0]").value(TATOOINE));
	  }

	  @Test
	  public void listPlanets_ReturnsNoPlanets() throws Exception {
	    when(service.list(null, null)).thenReturn(Collections.emptyList());

	    mvc
	        .perform(
	            get("/planets"))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$", hasSize(0)));
	  }
	  
	  @Test
	  public void removePlanet_WithExistingId_ReturnsNoContent() throws Exception {
	    mvc.perform(delete("/planets/1"))
	        .andExpect(status().isNoContent());
	  }

	  @Test
	  public void removePlanet_WithUnexistingId_ReturnsNotFound() throws Exception {
	    final Long planetId = 1L;

	    doThrow(new EmptyResultDataAccessException(1)).when(service).delete(planetId);

	    mvc.perform(delete("/planets/" + planetId))
	        .andExpect(status().isNotFound());
	  }

}
