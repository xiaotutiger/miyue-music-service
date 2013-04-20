package com.miyue.util;

import org.junit.Assert;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class DozerUtilsTest extends UnitilsJUnit4{
	@Test
	public void testDozer(){
		B1 b = new B1();
		b.setName("sa");
		B2 b2 = DozerUtils.convertSourceObjToDesObj(b, B2.class);
		Assert.assertEquals("sa", b2.getAge());
	}
}
