package by.creepid.docgeneration.dao;

import java.util.List;

import by.creepid.docgeneration.dao.entity.Regions;

public interface RegionsDAO {
	
	public List<Regions> findAll();
	
	public Regions findById(Long id);
}
