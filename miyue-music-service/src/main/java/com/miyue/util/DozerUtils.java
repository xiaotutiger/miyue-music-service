package com.miyue.util;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

public class DozerUtils<T> {
	private static final Mapper mapper = new DozerBeanMapper();
	
	public static <T> T convertSourceObjToDesObj(Object source,Class<T> destinationClass){
		return mapper.map(source, destinationClass);
	}
	public static void main(String[] args) {
		B1 b = new B1();
		b.setName("sa");
		B2 b2 = convertSourceObjToDesObj(b, B2.class);
		System.out.println("======" + b2.getName());
		System.out.println("======" + b2.getAge());
	}
	
}
