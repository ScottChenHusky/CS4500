package edu.northeastern.cs4500.controllers.hello;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Simple example of CRU services on an object.
 */
@Entity(name="hello")
public class HelloObject {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	/*
	 * Read function
	 */
	public String getMessage() {
		return message;
	}
	
	/*
	 * Update function
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/*
	 * Create function within an initialized value
	 */
	public HelloObject(String message) {
		this.message = message;
	}
	
	/*
	 * Create function
	 */
	public HelloObject() {
	}
	
	private String message;
}
