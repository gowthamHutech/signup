package com.example.App1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class LoginDto {
	
	@Email(message = "check your id and write again")
	private String email;
	@Size(min = 6,message = "please enter a valid password")
	private String passWord;
	private String status;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
