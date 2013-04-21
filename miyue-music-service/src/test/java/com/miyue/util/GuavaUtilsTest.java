package com.miyue.util;


import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class GuavaUtilsTest extends UnitilsJUnit4{
	
	@Test
	public void isPresentTest(){
		B1 b = null;
		assertEquals(false, GuavaUtils.isPresent(b));
		b = new B1();
		assertEquals(true, GuavaUtils.isPresent(b));
	}
}
