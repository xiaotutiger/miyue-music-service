package com.miyue.util;

import junit.framework.Assert;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class GuavaUtilsTest extends UnitilsJUnit4{
	
	@Test
	public void isPresentTest(){
		B1 b = null;
		Assert.assertEquals(false, GuavaUtils.isPresent(b));
		b = new B1();
		Assert.assertEquals(true, GuavaUtils.isPresent(b));
	}
}
