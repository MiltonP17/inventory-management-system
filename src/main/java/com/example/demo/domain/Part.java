package com.example.demo.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.example.demo.validators.ValidDeletePart;

@Entity
@ValidDeletePart
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "part_type", discriminatorType = DiscriminatorType.INTEGER)
@Table(name = "Parts")
public abstract class Part implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	String name;
	@Min(value = 0, message = "Price value must be positive")
	double price;
	@Min(value = 0, message = "Inventory value must be positive")
	int inv;

	@NotNull(message = "Min is required")
	@PositiveOrZero(message = "Min must be ≥ 0")
	private Integer minInv;

	@NotNull(message = "Max is required")
	@PositiveOrZero(message = "Max must be ≥ 0")
	private Integer maxInv;

	@ManyToMany
	@JoinTable(name = "product_part", joinColumns = @JoinColumn(name = "part_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
	Set<Product> products = new HashSet<>();

	public Part() {
	}

	public Part(String name, double price, int inv) {
		this.name = name;
		this.price = price;
		this.inv = inv;
	}

	public Part(long id, String name, double price, int inv) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.inv = inv;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getInv() {
		return inv;
	}

	public void setInv(int inv) {
		this.inv = inv;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public Integer getMinInv() {
		return minInv;
	}

	public void setMinInv(Integer minInv) {
		this.minInv = minInv;
	}

	public Integer getMaxInv() {
		return maxInv;
	}

	public void setMaxInv(Integer maxInv) {
		this.maxInv = maxInv;
	}

	public String toString() {
		return this.name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Part part = (Part) o;

		return id == part.id;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}
}
