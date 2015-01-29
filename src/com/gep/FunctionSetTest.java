package com.gep;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FunctionSetTest {

	public static FunctionSet Fun=new FunctionSet();
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsFunction() {
		   FunctionSet Fun=new FunctionSet();
		 
		   
		   assertEquals(true,Fun.IsFunction("pow2"));
	}

	@Test
	public void testGetParamCount() {
		assertEquals(0,Fun.GetParamCount("12"));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetResult() {
		double[] Data={1,1};
		double res=Fun.GetResult("+", Data);
		assertEquals(2,res);
	   
	}

}
