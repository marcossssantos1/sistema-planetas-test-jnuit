package com.marcos.demo.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "planets")
public class Planet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Column(nullable = false, unique = true)
	private String name;
	@Column(nullable = false)
	@NotEmpty
	private String climate;
	@NotEmpty
	@Column(nullable = false)
	private String terain;

	public Planet() {
		// TODO Auto-generated constructor stub
	}
	
	

	public Planet(Long id, @NotEmpty String name, @NotEmpty String climate, @NotEmpty String terain) {
		this.id = id;
		this.name = name;
		this.climate = climate;
		this.terain = terain;
	}



	public Planet(String name, String climate, String terain) {
		this.name = name;
		this.climate = climate;
		this.terain = terain;
	}
	
	public Planet(String climate, String terain) {
		this.climate = climate;
		this.terain = terain;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClimate() {
		return climate;
	}

	public void setClimate(String climate) {
		this.climate = climate;
	}

	public String getTerain() {
		return terain;
	}

	public void setTerain(String terain) {
		this.terain = terain;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(obj, this);
	}
	
	

}
