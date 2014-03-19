package by.creepid.docgeneration.validation;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import by.creepid.docgeneration.validation.GtinValidator;

public class GtinValidatorTest {

	private final String message = "GTIN is invalid:";

	private GtinValidator validator;

	@Before
	public void setUp() throws Exception {
		validator = new GtinValidator();
	}

	@After
	public void tearDown() throws Exception {
		validator = null;
	}

	@Test
	public final void testIsValid() {

		String gtin = "2222222222222";
		assertTrue(message + gtin, validator.isValid(gtin, null));

		gtin = "1111784555552";
		assertTrue(message + gtin, validator.isValid(gtin, null));

		gtin = "1111784";
		assertFalse(message + gtin, validator.isValid(gtin, null));

		gtin = "111";
		assertFalse(message + gtin, validator.isValid(gtin, null));

		gtin = null;
		assertTrue(message + gtin, validator.isValid(gtin, null));

		gtin = "";
		assertTrue(message + gtin, validator.isValid(gtin, null));

	}

}
