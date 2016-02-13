package org.lib.app.common.exception;

public class FieldNotValidException extends RuntimeException {

	private static final long serialVersionUID = -8769114703428572449L;

	private String fieldName;

	public FieldNotValidException(String fieldName, String message) {
	
		/*
		 * Message from the Exception. Constructor for Run-Time Exception
		 */
		super(message);
		this.fieldName = fieldName;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	
	@Override
	public String toString() {
		
		return "FieldNotValidException [fieldName=" + fieldName + "]";
	}
	

}
