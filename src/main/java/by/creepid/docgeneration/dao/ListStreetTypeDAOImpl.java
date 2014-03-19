package by.creepid.docgeneration.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import by.creepid.docgeneration.dao.entity.ListStreetType;

@Repository("listStreetTypeDAO")
@Transactional
public class ListStreetTypeDAOImpl implements ListStreetTypeDAO {

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
	public List<ListStreetType> findAll() {
		return sessionFactory.getCurrentSession()
				.getNamedQuery("ListStreetType.findAll").list();
	}

	@Override
	@Transactional(readOnly = true)
	public ListStreetType findById(Long id) {
		if (id == null) {
			return new ListStreetType();
		}

		ListStreetType type = (ListStreetType) sessionFactory
				.getCurrentSession().getNamedQuery("ListStreetType.findById")
				.setParameter("id", id).uniqueResult();

		return (type != null) ? type : new ListStreetType();
	}

}
