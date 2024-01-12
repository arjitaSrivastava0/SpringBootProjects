package com.student.management.controller;

import java.util.List;

import com.student.management.entity.Subject;
import com.student.management.service.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class SubjectController {

	private final SubjectService subjectService;

	@GetMapping("/subjects")
	public List<Subject> getAllSubjects()
	{
		return subjectService.getAllSubjects();
	}
	
	@PostMapping("/subjects")
	public String addSubject(@RequestBody Subject subject)
	{
		log.info("subjectService: "+subjectService.toString());
		log.info("subject: "+subject.toString());
		subjectService.addSubject(subject);
		return "subject added succssfully";
	}
	
	@PutMapping("/subjects/{id}")
	public void updateSubject(@PathVariable String id, @RequestBody Subject subject)
	{
		subjectService.updateSubject(id, subject);
	}
	@DeleteMapping("/subjects/{id}")
	public void DeleteSubject(@PathVariable String id)
	{
		subjectService.deleteSubject(id);
	}
	
	
	
	
	
	
	
	
}
