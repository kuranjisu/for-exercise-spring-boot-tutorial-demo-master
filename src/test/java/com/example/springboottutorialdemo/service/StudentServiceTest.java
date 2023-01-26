package com.example.springboottutorialdemo.service;

import com.example.springboottutorialdemo.entity.StudentEntity;
import com.example.springboottutorialdemo.exception.StudentNotFoundException;
import com.example.springboottutorialdemo.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@SpringBootTest
class StudentServiceTest {

    @Autowired
    StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    @DisplayName("This tests that if student id is existing, getStudentById will return the student entity")
    public void testGetStudentById_Success() {
        //given: student_id is existing
        int existing_student_id = 1;
        StudentEntity studentEntity = new StudentEntity(1,"Test Name",1, "Test Address");
        given(studentRepository.findById(existing_student_id)).willReturn(Optional.of(studentEntity));
        //when: studentService.getStudentById is executed
        StudentEntity studentServicesResult = studentService.getStudentById(existing_student_id);
        //then: return of studentService.getStudentById should be equal to return of studentRepository.findById
        assertEquals(studentServicesResult, studentEntity);
    }

    @Test
    @DisplayName("This tests that if student id is non-existing, getStudentById will throw StudentNotFoundException")
    public void testGetStudentById_Fail() {
        //given: student_id is non-existing
        int non_existing_student_id = 1;
        given(studentRepository.findById(non_existing_student_id)).willThrow(new StudentNotFoundException("Student with id : " + non_existing_student_id + " doesn't exist."));
        //when: studentService.getStudentById is executed
        StudentNotFoundException result = assertThrows(StudentNotFoundException.class, () -> {
            studentService.getStudentById(non_existing_student_id);
        });
        //then: studentService.getStudentById will throw a StudentNotFoundException with message "Student with id : <non_existing student_id> doesn't exist."
        assertEquals("Student with id : 1 doesn't exist.", result.getMessage());
    }

    // Test 1
    @Test
    @DisplayName("This tests that if student is saved in the studentRepository")
    public void testAddStudent() {
        // Given that there is a new student entity
        StudentEntity studentInput = new StudentEntity("Test Name", 1, "Test Address");
        StudentEntity student = new StudentEntity(1, "Test Name", 1, "Test Address");
        given(studentRepository.save(student)).willReturn(student);
        given(studentRepository.save(studentInput)).willReturn(student);

        // When the studentService add student method is executed
        StudentEntity savedStudent = studentService.addStudent(studentInput);

        // Then it should return the new student entity saved in the studentRepository
        assertEquals(student, savedStudent);
    }
    // Test 2
    @Test
    @DisplayName("This tests that if student id is existing, deleteStudentById will return null")
    public void testDeleteStudentById_Success() {
        // Given that there is an existing student id
        int existing_student_id = 1;
        StudentEntity student = new StudentEntity(1, "Test Name", 1, "Test Address");
        given(studentRepository.findById(existing_student_id)).willReturn(Optional.of(student));

        // When the studentService deleteStudentById method is executed
        StudentEntity result = studentService.deleteStudentById(existing_student_id);

        // Then it should return null
        assertEquals(result, null);
        verify(studentRepository, atLeastOnce()).delete(student);
    }

    // Test 3
    @Test
    @DisplayName("This tests that if student id is existing, deleteStudentById will throw StudentNotFoundException")
    public void testDeleteStudentById_Fail() {
        // Given that the student id is non-existing
        int non_existing_student_id = 1;
        given(studentRepository.findById(non_existing_student_id)).willThrow(new StudentNotFoundException("Student with id : " + non_existing_student_id + " doesn't exist."));

        // When the studentService deleteStudentById method is executed
        StudentNotFoundException result = assertThrows(StudentNotFoundException.class, () -> {
            studentService.getStudentById(non_existing_student_id);
        });

        // Then it should throw a StudentNotFoundException exception with message, "Student with id : <non-existing_id> doesn't exist."
        assertEquals("Student with id : 1 doesn't exist.", result.getMessage());
    }
}