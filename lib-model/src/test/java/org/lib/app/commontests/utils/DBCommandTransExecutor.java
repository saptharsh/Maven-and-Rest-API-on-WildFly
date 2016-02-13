package org.lib.app.commontests.utils;

import javax.persistence.EntityManager;

import org.junit.Ignore;

@Ignore
public class DBCommandTransExecutor {

	private EntityManager em;

	public DBCommandTransExecutor(final EntityManager em) {
	
		this.em = em;
	}
	
	//This method returns an object of type T
	public <T> T executeCommand(DBcommand<T> dbCommand) {
		
		try{
			em.getTransaction().begin();
			
			//Captures the returned category added into the DB
			final T toReturn = dbCommand.execute();
			
			em.getTransaction().commit();
			em.clear();
			return toReturn;
			
		} catch(final Exception e){
			
			e.printStackTrace();
			em.getTransaction().rollback();
			throw new IllegalStateException(e);
		}

		
	}
	
}
