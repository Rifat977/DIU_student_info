package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.StudentResult;

public interface StudentResultRepository extends JpaRepository<StudentResult, Long> {

}
