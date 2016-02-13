package org.lib.app.category.services.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
// Static imports are like initialing the Class. From which we can call the static methods of it
import static org.lib.app.commontests.category.CategoryForTestsRepo.*;
import static org.mockito.Mockito.*;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.lib.app.category.exception.CategoryExistentException;
import org.lib.app.category.model.Category;
import org.lib.app.category.repository.CategoryRepo;
import org.lib.app.category.services.CategoryServices;
import org.lib.app.common.exception.FieldNotValidException;


public class CategoryServicesUTest {

	// Interface
	private CategoryServices categoryServices;
	private CategoryRepo categoryRepo;
	private Validator validator;
	
	@Before
	public void initTestCase() {
		
		validator = Validation.buildDefaultValidatorFactory().getValidator();
		
		// Mockito is used to mock the CategoryRepo Class
		categoryRepo = mock(CategoryRepo.class);
		
		// Instance of an Interface through Service Class 
		categoryServices = new CategoryServicesImpl();
		
		// Cannot use Interface (categoryServices) to access variables. Hence, use casting
		/*
		 * Setting Validator and CategoryRepo. The properties in the LHS are of CategoryServicesImpl, and RHS are of CategoryServicesUTest  
		 */
		((CategoryServicesImpl) categoryServices).validator = validator;
		((CategoryServicesImpl) categoryServices).categoryRepo = categoryRepo;
		
	}
	
	
	@Test
	public void addCategoryWithNullName() {
		
		addCategoryWithInvalidName(null);
	}
	
	
	@Test
	public void addCategoryWithShortName() {
		
		addCategoryWithInvalidName("A");
	}
	
	
	@Test
	public void addCategoryWithLongName() {
		
		addCategoryWithInvalidName("This is a long name that will cause an exception to be thrown");
	}
	
	
	@Test(expected = CategoryExistentException.class)
	public void addCategoryWithExistantName() {
		
		// when() is the operation from Mockito. Stubs => Methods/ Operations i guess
		when(categoryRepo.alreadyExists(java())).thenReturn(true);
		categoryServices.add(java());
	}
	
	
	@Test
	public void addValidCategory() {
		
		when(categoryRepo.alreadyExists(java())).thenReturn(false);
		when(categoryRepo.add(java())).thenReturn(categoryWithId(java(), 1L));
		
		Category categoryAdded = categoryServices.add(java());
		
		// From org.hamcrest.CoreMatchers.*;
		assertThat(categoryAdded.getId(), is(equalTo(1L)));
	}
	
	public void addCategoryWithInvalidName(String name) {
		try {
			
			// fail("An error should have been thrown"); we get an error here
			categoryServices.add( new Category(name)); 
			fail("An error should have been thrown"); // we get an error here only if the above fails
			
		} catch(FieldNotValidException e) {
			
			assertThat(e.getFieldName(), is(equalTo("name")));
		}
		
	}
	
}






