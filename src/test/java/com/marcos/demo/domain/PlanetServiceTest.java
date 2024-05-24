package com.marcos.demo.domain;

import static com.marcos.demo.common.PlanetConstants.INVALID_PLANET;
import static com.marcos.demo.common.PlanetConstants.PLANET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import com.marcos.demo.repository.PlanetRepository;
import com.marcos.demo.service.PlanetService;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {

	@InjectMocks
	private PlanetService service;

	@Mock
	private PlanetRepository repository;

	@Test
	public void createPlanet_WithValidData_ReturnPlanet() {

		when(repository.save(PLANET)).thenReturn(PLANET);

		Planet sut = service.create(PLANET);

		assertThat(sut).isEqualTo(PLANET);
	}

	@Test
	public void createPlanet_WithInvalidDate_ReturnException() {

		when(repository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

		assertThatThrownBy(() -> service.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);

	}

	@Test
	public void getPlanet_ByIdExisting_ReturnPlanet() {
		when(repository.findById(1L)).thenReturn(Optional.of(PLANET));

		Optional<Planet> sut = service.getId(1L);

		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(PLANET);

	}

	@Test
	public void getPlanet_ByIdInexistente_ReturnException() {
		when(repository.findById(3L)).thenThrow(RuntimeException.class);

		assertThatThrownBy(() -> service.getId(3L)).isInstanceOf(RuntimeException.class);
	}

	@Test
	public void getPlanet_ByIdInexistente2_ReturnException() {
		when(repository.findById(3L)).thenReturn(Optional.empty());

		Optional<Planet> sut = service.getId(3L);

		assertThat(sut).isEmpty();
	}

	@Test
	public void getPlanet_ByNameExisting_ReturnPlanet() {
		when(repository.findByName("maco")).thenReturn(Optional.of(PLANET));

		Optional<Planet> sut = service.getByName("maco");

		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(PLANET);
	}

	@Test
	public void getPlanet_ByNameExisting2_ReturnPlanet() {
		when(repository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

		Optional<Planet> sut = service.getByName(PLANET.getName());

		assertThat(sut).isNotEmpty();
		assertThat(sut.get()).isEqualTo(PLANET);
	}

	@Test
	public void getPlanet_ByNameInexistente_ReturnException() {
		when(repository.findByName("mac")).thenReturn(Optional.empty());

		Optional<Planet> sut = service.getByName("mac");

		assertThat(sut).isEmpty();
	}

	@Test
	public void getPlanet_ByNameInexistente2_ReturnException() {
		final String name = "Invalid name";
		when(repository.findByName(name)).thenReturn(Optional.empty());

		Optional<Planet> sut = service.getByName(name);

		assertThat(sut).isEmpty();
	}

	@Test
	public void listPlanets_ReturnPlanets() {

		List<Planet> planets = new ArrayList<>() {
			{
				add(PLANET);
			}
		};

		Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getClimate(), PLANET.getTerain()));

		when(repository.findAll(query)).thenReturn(planets);

		List<Planet> sut = service.list(PLANET.getClimate(), PLANET.getTerain());

		assertThat(sut).isEmpty();
		assertThat(sut).hasSize(1);
		assertThat(sut.get(0)).isEqualTo(PLANET);

	}

	/*@Test
	public void listNoPlanets_ReturnPlanets() {

		when(repository.findAll(any())).thenReturn(Collections.EMPTY_LIST);

		List<Planet> sut = service.list(PLANET.getClimate(), PLANET.getTerain());

		assertThat(sut).isEmpty();

	}*/
	
	
	@Test
	public void removePlanet_WithIdExisting_DoesNotThrowAnyException() {
		
		assertThatCode(() -> service.delete(1L)).doesNotThrowAnyException();
	}
	
	@Test
	public void removePlanet_WithInvalidId_ThrowException() {
		
		doThrow(new RuntimeException()).when(repository).deleteById(99L);
		
		assertThatThrownBy( () -> service.delete(99L)).isInstanceOf(RuntimeException.class);
		
		
	}
	
	

}
