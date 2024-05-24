package com.marcos.demo.domain;

import static com.marcos.demo.common.PlanetConstants.PLANET;
import static com.marcos.demo.common.PlanetConstants.TATOOINE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;

import com.marcos.demo.repository.PlanetRepository;

@DataJpaTest
public class PlanetRepositoryTest {

	@Autowired
	private PlanetRepository repository;

	@Autowired
	private TestEntityManager manager;
	
	@AfterEach
	public void afterEach() {
		PLANET.setId(null);
	}

	@Test
	public void createPlanet_WithDataValid_ReturnPlanet() {

		Planet planet = repository.save(PLANET);

		Planet sut = manager.find(Planet.class, planet.getId());

		assertThat(sut).isNotNull();
		assertThat(sut.getName()).isEqualTo(PLANET.getName());
		assertThat(sut.getClimate()).isEqualTo(PLANET.getClimate());
		assertThat(sut.getTerain()).isEqualTo(PLANET.getTerain());
	}

	@Test
	public void createPlanet_WithInvalidData_ThrowsException() {
		Planet nullPlanet = new Planet();
		Planet invalidPlanet = new Planet("", "", "");

		assertThatThrownBy(() -> repository.save(nullPlanet)).isInstanceOf(RuntimeException.class);
		assertThatThrownBy(() -> repository.save(invalidPlanet)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void createPlanet_WithNameExisting_ThrowsException() {
		Planet planet = manager.persistFlushFind(PLANET);
		manager.detach(planet);
		planet.setId(null);

		assertThatThrownBy(() -> repository.save(planet)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void getPlanet_WithExistingId_ReturnsPlanet() {
		Planet planet = manager.persistFlushFind(PLANET);

		Optional<Planet> sut = repository.findById(planet.getId());

		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(PLANET.getId());
	}

	@Test
	public void getPlanet_ByUexistingId_ReturnsEmpty() {
		Optional<Planet> sut = repository.findById(1L);

		assertThat(sut).isNotEmpty();

	}
	
	@Test
	public void getPlanet_WithNameExisting_ReturnsPlanet() {
		Planet planet = manager.persistFlushFind(PLANET);

		Optional<Planet> sut = repository.findByName(planet.getName());

		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(PLANET.getName());
	}

	@Test
	public void getPlanet_ByUexistingName_ReturnsEmpty() {
		Optional<Planet> sut = repository.findByName("name");

		assertThat(sut).isEmpty();

	}
	
	@Sql(scripts = "/import_planets.sql")
	  @Test
	  public void listPlanets_ReturnsFilteredPlanets() {
	    Example<Planet> queryWithoutFilters = QueryBuilder.makeQuery(new Planet());
	    Example<Planet> queryWithFilters = QueryBuilder.makeQuery(new Planet(TATOOINE.getClimate(), TATOOINE.getTerain()));

	    List<Planet> responseWithoutFilters = repository.findAll(queryWithoutFilters);
	    List<Planet> responseWithFilters = repository.findAll(queryWithFilters);

	    assertThat(responseWithoutFilters).isNotEmpty();
	    assertThat(responseWithoutFilters).hasSize(3);
	    assertThat(responseWithFilters).isNotEmpty();
	    assertThat(responseWithFilters).hasSize(1);
	    assertThat(responseWithFilters.get(0)).isEqualTo(TATOOINE);
	  }

	  @Test
	  public void listPlanets_ReturnsNoPlanets() {
	    Example<Planet> query = QueryBuilder.makeQuery(new Planet());

	    List<Planet> response = repository.findAll(query);

	    assertThat(response).isEmpty();
	  }

	  @Test
	  public void removePlanet_WithExistingId_RemovesPlanetFromDatabase() {
	    Planet planet = manager.persistFlushFind(PLANET);

	    repository.deleteById(planet.getId());

	    Planet removedPlanet = manager.find(Planet.class, planet.getId());
	    assertThat(removedPlanet).isNull();
	  }

	  @Test
	  public void removePlanet_WithUnexistingId_ThrowsException() {
	    assertThatThrownBy(() -> repository.deleteById(1L)).isInstanceOf(EmptyResultDataAccessException.class);
	  }
}
