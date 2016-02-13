package org.lib.app.category.services;

import org.lib.app.category.exception.CategoryExistentException;
import org.lib.app.category.model.Category;
import org.lib.app.common.exception.FieldNotValidException;

public interface CategoryServices {

	Category add(Category category) throws FieldNotValidException, CategoryExistentException;
	
}
