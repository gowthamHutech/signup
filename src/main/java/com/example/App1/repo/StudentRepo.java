package com.example.App1.repo;

import org.springframework.data.repository.CrudRepository;

import com.example.App1.Entity.Student;


public interface StudentRepo extends CrudRepository<Student, Integer> {

	public Student findByEmail(String email);
	public Student findByEmailAndPassWord(String email , String passWord) ;
}
