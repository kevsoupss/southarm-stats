package com.southarmsite.backend.controllers;


import com.southarmsite.backend.domain.dto.QueryRequest;
import com.southarmsite.backend.domain.dto.QueryResult;
import com.southarmsite.backend.services.NL2SQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nl2sql")
public class NL2SQLController {

    private NL2SQLService nl2sqlService;

    public NL2SQLController(NL2SQLService nl2sqlService) {
        this.nl2sqlService = nl2sqlService;
    }

    @PostMapping("/query")
    public ResponseEntity<?> processQuery(@RequestBody QueryRequest request) {

        QueryResult result = nl2sqlService.processNaturalLanguageQuery(request.getQuery());
        if (result.isSuccess()) {
            System.out.println(result);
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}
