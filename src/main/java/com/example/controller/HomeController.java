package com.example.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.example.model.StudentResult;
import com.example.repository.StudentResultRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private StudentResultRepository studentResultRepository;

    @GetMapping("/")
    public String showForm() {
        return "index";
    }

    @PostMapping("/")
    public String makeApiCalls(
            @RequestParam String studentId,
            @RequestParam String semesterId,
            Model model
    ) throws IOException {
        String apiUrl1 = "http://software.diu.edu.bd:8189/result/studentInfo?studentId=" + studentId;
        String apiUrl2 = "http://software.diu.edu.bd:8189/result" +
                "?grecaptcha=" + "" +
                "&semesterId=" + semesterId +
                "&studentId=" + studentId;

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity1 = restTemplate.getForEntity(apiUrl1, String.class);
        ResponseEntity<String> responseEntity2 = restTemplate.getForEntity(apiUrl2, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> studentInfo = objectMapper.readValue(responseEntity1.getBody(), new TypeReference<Map<String, Object>>() {});
        List<Map<String, Object>> courseInfoList = objectMapper.readValue(responseEntity2.getBody(), new TypeReference<List<Map<String, Object>>>() {});

        StudentResult studentResult = new StudentResult();
        studentResult.setStudentId(studentId);
        studentResult.setStudentName((String) studentInfo.get("studentName"));
        studentResult.setSemesterId(semesterId);
        studentResultRepository.save(studentResult);

        model.addAttribute("studentInfo", studentInfo);
        model.addAttribute("courseInfoList", courseInfoList);
        model.addAttribute("studentId", studentId);
        model.addAttribute("semesterId", semesterId);

        return "index";
    }
}
