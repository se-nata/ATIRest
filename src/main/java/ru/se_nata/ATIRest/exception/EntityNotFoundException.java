package ru.se_nata.ATIRest.exception;

public class EntityNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(Integer id) {
		super("Could not find  a line with an entry" + id);
		
	}


}
