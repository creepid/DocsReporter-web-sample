package by.creepid.docgeneration.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import by.creepid.docgeneration.dao.RegionsDAO;
import by.creepid.docgeneration.dao.entity.Regions;

public class RegionsDAOImolTest extends AbstractDAOTest {
	
	@Autowired
	private RegionsDAO regionsDAO;

	@Test
	public final void testFindAll() {
		int size = regionsDAO.findAll().size();
		assertEquals(7, size);
	}

	@Test
	public final void testFindById() {
		Regions reg = regionsDAO.findById(2l);
		assertEquals("Гомельская обл.", reg.getValue());
	}

	public void setRegionsDAO(RegionsDAO regionsDAO) {
		this.regionsDAO = regionsDAO;
	}
	
	

}
