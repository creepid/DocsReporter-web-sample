package by.creepid.docgeneration.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import by.creepid.docgeneration.dao.ListStreetTypeDAO;
import by.creepid.docgeneration.dao.entity.ListStreetType;

public class ListStreetTypeDAOTest extends AbstractDAOTest {
	
	@Autowired
	private ListStreetTypeDAO listStreetTypeDAO;

	@Test
	public final void testFindAll() {
		int size = listStreetTypeDAO.findAll().size();
		assertEquals(15, size);
	}

	@Test
	public final void testFindById() {
		ListStreetType type  = listStreetTypeDAO.findById(8l);
		
		assertNotNull(type);
		
		assertEquals("туп.", type.getValue());
		assertEquals("Тупик", type.getTitle());	
	}

	public void setListStreetTypeDAOTDAO(ListStreetTypeDAO listStreetTypeDAOTDAO) {
		this.listStreetTypeDAO = listStreetTypeDAOTDAO;
	}
}
