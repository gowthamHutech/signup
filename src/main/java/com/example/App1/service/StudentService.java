package com.example.App1.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.App1.Entity.Student;
import com.example.App1.dto.LoginDto;
import com.example.App1.dto.LogoutDto;
import com.example.App1.dto.changepasswordDto;
import com.example.App1.repo.StudentRepo;


@Service
public class StudentService {

	@Autowired
	private StudentRepo studentRepo;
	
	@Autowired
	private MailService mailService;
	
	Student st;
	
	public Student save(Student student) throws Exception
	{
		studentRepo.save(student);
		String activationCode = mailService.sendEmailToVerify(student.getEmail()) ;				
		student.setActivationCode(activationCode);
		studentRepo.save(student) ;	
		return student ;
	}
	
	public Student findByEmail(String email)
	{
		Student str = studentRepo.findByEmail(email);
		String Message ="user doesn't exist";
		if(str==null) throw new NoSuchElementException(Message);
		return str;
	}
	
	public Student update(Student student)
	{
		return studentRepo.save(student);
	}
	
	public Student login(LoginDto loginDto) throws Exception
	{
		st = findByEmail(loginDto.getEmail());
		Student student = studentRepo.findByEmailAndPassWord(loginDto.getEmail(),loginDto.getPassWord());
		if(student!=null)
		{
			loginDto.setStatus("welcome");
			if(student.getStatus()==0)
			{
				loginDto.setStatus("please Activate your mail");
				return st;
			}
			st.setCount(0);
			st.setLogin(1);
		}
		else
		{
			
			st.setCount(st.getCount()+1);
			if(st.getCount()>=5 && st.getStatus()==1)
			{
				st.setStatus(0);
				loginDto.setStatus("your acount is deavtivated");
				String code = mailService.sendEmailToVerify(st.getEmail());
				st.setActivationCode(code);
			}
			else if(st.getStatus()==0)
			{
				loginDto.setStatus("please activate your account");
			}
			else
			{
				loginDto.setStatus("login failed " + (5 - st.getCount())+" attempts");
				
			}
		
		}
		return st;
	}

	public Optional<Student> findbyid(Integer id) {
		
		return studentRepo.findById(id);
	}
	public Student changepass(changepasswordDto changepasswordDto)
	{
		Student st = findByEmail(changepasswordDto.getEmail());
		if(st.getLogin()==1) {
			changepasswordDto.setMessage("old password is incorrect");
			if(changepasswordDto.getOldPassword().equals(st.getPassWord()))
			{
				if(changepasswordDto.getNewPassword().equals(changepasswordDto.getOldPassword()))
				{
					changepasswordDto.setMessage("you cant use old password try different password");
				}
				else
				{
					st.setPassWord(changepasswordDto.getNewPassword());
					changepasswordDto.setMessage("password changed successfully");
				}
			}
		}
		else
		{
			changepasswordDto.setMessage("login first");
		}
		return st;
	}
	public Student logout(LogoutDto logoutDto)
	{
		Student student = findByEmail(logoutDto.getEmail());
		student.setLogin(0);
		return student;
	}

}
