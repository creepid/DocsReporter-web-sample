package by.creepid.docgeneration.dao;

import java.util.List;

import by.creepid.docgeneration.dao.entity.ListStreetType;

public interface ListStreetTypeDAO {
	
	public List<ListStreetType> findAll();
	
	public ListStreetType findById(Long id);

}
