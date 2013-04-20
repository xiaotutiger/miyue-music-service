package com.miyue.util;

import org.dozer.Mapping;

public class B1{
	@Mapping("age")
	private String name;
	private String company;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
}