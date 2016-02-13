package org.lib.app.category.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.lib.app.category.model.Category;

public class CategoryRepo {

	EntityManager em;
	
	public Category add(final Category category){
		em.persist(category);
		
		return category;
		//return null;
	}

	public Category findById(final Long id) {
		
		if(id == null){
			return null;
		}
		return em.find(Category.class, id);
	}

	public void update(final Category category) {

		em.merge(category);
	}

	@SuppressWarnings("unchecked")
	public List<Category> findAll(final String orderField) {
		
		//ORDER BY enity.name, here orderField => name
		//And Category should start with Capitals, no matter what the table name is
		return em.createQuery("SELECT entity FROM Category entity ORDER BY entity."+ orderField).getResultList();
	}

	// Rename alreadyExists => duplicateRowCheck()
	/*
	 * if(duplicateRowCheck == false) => One unique Category AlreadyExists in CategoryRepo (DB)
	 * if(duplicateRowCheck == true) => Category is unique where ids are different 
	 * 									and names might be same in CategoryRepo (DB)
	 */
	public boolean alreadyExists(final Category category) {
		
		final StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT 1 FROM Category entity WHERE entity.name = :name");
		
		// Updating should be done, only if the Category Name is being changed. 
		// Hence, we are verifying if the id's of the category are different
		if(category.getId() != null){
			jpql.append(" AND entity.id != :id");
		}
		
		final Query query = em.createQuery(jpql.toString());
		query.setParameter("name", category.getName());
		
		if(category.getId() != null){
			query.setParameter("id", category.getId());
		}
		
		return query.setMaxResults(1).getResultList().size() > 0;
		
		
	}

	public Boolean existsById(final Long id) {
		
		return em.createQuery("SELECT 1 FROM Category entity WHERE entity.id = :id")
				.setParameter("id", id).
				setMaxResults(1).
				getResultList().size() > 0;
	}

}



