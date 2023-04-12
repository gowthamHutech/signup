package com.example.App1.controller;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.App1.Entity.Student;
import com.example.App1.dto.LoginDto;
import com.example.App1.dto.LogoutDto;
import com.example.App1.dto.changepasswordDto;
import com.example.App1.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("student")
public class StudentController {

	@Autowired
	private StudentService studentService;

	@PostMapping
	public ResponseEntity<Student> save(@RequestBody @Valid Student student) throws Exception 
	{
		return new ResponseEntity<>(studentService.save(student),HttpStatus.ACCEPTED);
	}

	@GetMapping("/verification/email/{email}/activationCode/{activationCode}")
	public ResponseEntity<String> verifyEmail(@PathVariable String email , @PathVariable String activationCode ) throws Exception
	{
		String status = "Email Not Verified" ;
		Student student = studentService.findByEmail(email);

		if(student.getActivationCode().equals(activationCode))
		{
			student.setStatus(1) ;
			student.setCount(0);
			studentService.update(student);
			status = "Email Verified Succesfully";
		}
		return ResponseEntity.ok(status) ;
	}


	@PostMapping("login")
	public ResponseEntity<String> login(@RequestBody @Valid LoginDto loginDto) throws Exception
	{
		Student student = studentService.login(loginDto);
		studentService.update(student);
		return ResponseEntity.ok(loginDto.getStatus());
	}

	@GetMapping("find/{id}")
	public Optional<Student> find(@PathVariable @Valid Integer id)
	{
		Optional<Student> st = studentService.findbyid(id);
		System.out.println(st);
		if(st.isEmpty())
		{
			
			throw new NoSuchElementException("id doesn't exist");
		}
		else return st;
	}
	@PostMapping("change")
	public ResponseEntity<String> change(@RequestBody changepasswordDto changepasswordDto)
	{
		
		Student st = studentService.changepass(changepasswordDto);
		studentService.update(st);
		return ResponseEntity.ok(changepasswordDto.getMessage());
	}
	@PostMapping("logout")
	public ResponseEntity<String> logout(@RequestBody LogoutDto logoutDto)
	{
		Student st =  studentService.logout(logoutDto);
		logoutDto.setMessage("logout successfully");
		studentService.update(st);
		return ResponseEntity.ok(logoutDto.getMessage());
	}
}
