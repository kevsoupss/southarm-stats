package com.southarmsite.backend.controllers;


import com.southarmsite.backend.services.GoalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


public class GoalController {

    private GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }


    @PostMapping(path="/goals")
    public ResponseEntity<GoalDto> createMatch(@RequestBody GoalDto goalDto){
        return new ResponseEntity<>(goalService.createGoal(goalDto), HttpStatus.CREATED);
    }
}
