package com.br.cesar.plantech.models;

public class Question {
	private long id;
	private String text;
	private int idResponses ;
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public int getIdResponses() {
		return idResponses;
	}
	
	public void setIdResponses(int idResponses) {
		this.idResponses = idResponses;
	}
	
	
}
