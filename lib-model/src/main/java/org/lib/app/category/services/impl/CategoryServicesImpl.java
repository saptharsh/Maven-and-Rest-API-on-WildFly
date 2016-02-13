package org.lib.app.category.services.impl;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.lib.app.category.exception.CategoryExistentException;
import org.lib.app.category.model.Category;
import org.lib.app.category.repository.CategoryRepo;
import org.lib.app.category.services.CategoryServices;
import org.lib.app.common.exception.FieldNotValidException;

public class CategoryServicesImpl implements CategoryServices{

	Validator validator;
	CategoryRepo categoryRepo;
	
	@Override
	public Category add(Category category) { // throws FieldNotValidException, CategoryExistentException { Not needed as it is mentioned in the interface

		// Validating the Hibernate Annotations
		Set<ConstraintViolation<Category>> errors = validator.validate(category);
		Iterator<ConstraintViolation<Category>> itErrors = errors.iterator();
		
		if(itErrors.hasNext()) {
			ConstraintViolation<Category> violation = itErrors.next();
			throw new FieldNotValidException(violation.getPropertyPath().toString(), violation.getMessage());
			
			// violation.getPropertyPath().toString() => 
			//								return "Category [id=" + id + ", name=" + name + "]";
		}
		
		if(categoryRepo.alreadyExists(category)){
			throw new CategoryExistentException();
		}
		
		return categoryRepo.add(category);
	}

}
