package com.spring.springbootsecurity;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/student")
public class StudentController {
	
	private static final List<Student> students = List.of(new Student(1L,"Sujitha", "Sujitha@gmail.com"),
			new Student(2l,"Pradeep", "pradeep@gmail.com"),
			new Student(3l,"Kiara", "kiara@gmail.com"));
	
	@GetMapping
	public List<Student> getStudents() {
		return students;
	}
	
	@GetMapping(path="{studentId}")
	public Student getStudent(@PathVariable("studentId") Long id) {
		return students.stream().filter(s->s.getId().equals(id)).findFirst().orElseThrow();
	}
}
