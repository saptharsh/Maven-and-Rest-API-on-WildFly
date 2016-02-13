package org.lib.app.category.repository;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.lib.app.commontests.category.CategoryForTestsRepo.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lib.app.category.model.Category;
import org.lib.app.commontests.utils.DBCommandTransExecutor;
import org.lib.app.commontests.utils.DBcommand;

public class CategoryRepoUTest {

	//We need entity manager to test the JPA
	private EntityManagerFactory emf;
	private EntityManager em;
	private CategoryRepo categoryRepo;
	private DBCommandTransExecutor dBCommandTransExecutor;
	/*
	 * Executed before each test case
	 */
	@Before
	public void initTestCase() {

		// libraryPU : Name of the persistence unit that we will be using
		emf = Persistence.createEntityManagerFactory("libraryPU");
		em = emf.createEntityManager();
		
		categoryRepo = new CategoryRepo();
		categoryRepo.em = em;
		
		dBCommandTransExecutor = new DBCommandTransExecutor(em);
	}

	@After
	public void closeEntityManager() {
		em.close();
		emf.close();
	}
	
	@Test
	public void addCategoryAndFindIt() {

		final Long categoryAddedId = dBCommandTransExecutor.executeCommand(() -> {
			
			return categoryRepo.add(java()).getId();
		});
		  
		assertThat(categoryAddedId, is(notNullValue()));  
		 
		final Category category = categoryRepo.findById(categoryAddedId);
		assertThat(category, is(notNullValue()));
		assertThat(category.getName(), is(equalTo(java().getName())));
		
	}

	@Test
	public void findCategoryByIdNotFound(){
		final Category category = categoryRepo.findById(999L);
		assertThat(category, is(nullValue()));
	}
	
	@Test
	public void findCategoryByIdWithNullId() {
		final Category category = categoryRepo.findById(null);
		assertThat(category, is(nullValue()));
	}
	
	@Test
	public void updateCategory(){
		
		//JAVA 7 and earlier
		final Long categoryAddedId = dBCommandTransExecutor.executeCommand(new DBcommand<Long>() {

			@Override
			public Long execute() {
			
				return categoryRepo.add(java()).getId();
			}
		});
		
		
		//JAVA 8
		/*
		final Long categoryAddedId = dBCommandTransExecutor.executeCommand(() -> {
			
			return categoryRepo.add(java()).getId();
		});
		*/
		final Category categoryAfterAdd = categoryRepo.findById(categoryAddedId);
		assertThat(categoryAfterAdd.getName(), is(equalTo(java().getName())));
		
		
		categoryAfterAdd.setName(cleanCode().getName());
		dBCommandTransExecutor.executeCommand(() -> {
			
			categoryRepo.update(categoryAfterAdd);
			return null;
		});
		
		final Category categoryAfterUpdate = categoryRepo.findById(categoryAddedId);
		assertThat(categoryAfterUpdate.getName(), is(equalTo(cleanCode().getName())));
		
	}
	
	@Test
	public void findAllCategories(){
		
		dBCommandTransExecutor.executeCommand(() -> {
			
			//JAVA 7 or earlier
			for(final Category category : allCategories()){
				categoryRepo.add(category);
			}
			
			//JAVA 8
			//allCategories().forEach(categoryRepo::add);
			
			return null;
		});
		
		final List<Category> categories = categoryRepo.findAll("name");
		assertThat(categories.size(), is(equalTo(4)));
		assertThat(categories.get(0).getName(), is(equalTo(architecture().getName())));
		assertThat(categories.get(1).getName(), is(equalTo(cleanCode().getName())));
		assertThat(categories.get(2).getName(), is(equalTo(java().getName())));
		assertThat(categories.get(3).getName(), is(equalTo(networks().getName())));
	}
	
	@Test
	public void alreadyExistsForAdd(){
		
		//Java 7 and earlier
		dBCommandTransExecutor.executeCommand(new DBcommand<Boolean>() {
			@Override
			public Boolean execute() {
				
				categoryRepo.add(java());
				return null;
			}
		
		});
		
		//Java 8
		/*
		dBCommandTransExecutor.executeCommand(() -> {
			
			categoryRepo.add(java());
			return null;
		});
		*/
		
		assertThat(categoryRepo.alreadyExists(java()), is(equalTo(true)));
		assertThat(categoryRepo.alreadyExists(cleanCode()), is(equalTo(false)));

	}
	
	@Test
	public void alreadyExistsCategoryWithId(){
		
		
		final Category java = dBCommandTransExecutor.executeCommand(new DBcommand<Category>() {
			@Override
			public Category execute() {
				
				categoryRepo.add(cleanCode());
				return categoryRepo.add(java());
			}
		
		});
		
		/*
		 * Java category is returned to local var => "java." It has been added to repository.
		 * 	We need to UPDATE the category only if the Name of the Category is changed 
		 */
		
		// Name of the Category is same the same. Do not update in this case
		assertThat(categoryRepo.alreadyExists(java), is(equals(false)));
		
		//When name of the Category "Java" is changed to "Clean Code."  
		java.setName(cleanCode().getName());
		//alreadyExists(java) => returns true because, java.getId() != cleanCode.getId().
		assertThat(categoryRepo.alreadyExists(java), is(equalTo(true)));
		
		//Networks category has not been added to repo, hence its false
		java.setName(networks().getName());
		assertThat(categoryRepo.alreadyExists(java), is(equalTo(false)));
		
	}
	
	@Test
	//Checking if category exists by ID
	public void existsById(){
		
		final Long categoryAddedId = dBCommandTransExecutor.executeCommand(() -> {
			
			return categoryRepo.add(java()).getId();
		});
		
		assertThat(categoryRepo.existsById(categoryAddedId), is(equalTo(true)));
		assertThat(categoryRepo.existsById(999L), is(equalTo(false)));
	}
	
	
}





