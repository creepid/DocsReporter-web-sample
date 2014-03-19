package by.creepid.docgeneration.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import by.creepid.docgeneration.dao.entity.ListStreetType;
import by.creepid.docgeneration.dao.entity.Regions;

@Repository("regionsDAO")
@Transactional
public class RegionsDAOImol implements RegionsDAO {

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Resource(name = "sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Regions> findAll() {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("Regions.findAll").list();
	}

	@Override
	@Transactional(readOnly = true)
	public Regions findById(Long id) {
		if (id == null ){
			return new Regions();
		}
		
		Regions region = (Regions) sessionFactory.getCurrentSession()
				.getNamedQuery("Regions.findById").setParameter("id", id)
				.uniqueResult();
		
		return (region != null) ? region : new Regions();
	}

}
