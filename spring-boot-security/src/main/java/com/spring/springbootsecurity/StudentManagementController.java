package com.spring.springbootsecurity;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/management/student")
public class StudentManagementController {
	
	private static final List<Student> students = List.of(new Student(1L,"Sujitha", "Sujitha@gmail.com"),
			new Student(2l,"Pradeep", "pradeep@gmail.com"),
			new Student(3l,"Kiara", "kiara@gmail.com"));
	
	
	/*
	 * @preAuthorize 
	 * 
	 * hasRole('ROLE_'), hasAnyRole(ROLE_), hasAuthority('permission'), hasAnyAuthority('permission')
	 */
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ADMINTRAINEE')")
	public List<Student> getStudents() {
		return students;
	}
	@PostMapping
	@PreAuthorize("hasAuthority('course:write')")
	public String registerNewStudent(@RequestBody Student student) {	
		//TODO				
		return student.getName() +" registered!!!";
		
	}
	@DeleteMapping(path ="{studentId}")
	@PreAuthorize("hasAuthority('course:write')")
	public String deleteStudent(@PathVariable("studentId") Integer studentId) {
		//TODO
		return "Student Id : "+studentId +" deleted!!!";
	}
	@PutMapping(path ="{studentId}")
	@PreAuthorize("hasAuthority('course:write')")
	public String updateStudent(@PathVariable("studentId") Integer studentId,@RequestBody Student student) {
		//TODO
		return studentId +" updated!!!";
	}

}
