package com.reference.ncbca.controller;

import com.reference.ncbca.model.Team;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class TeamsController {

    @GetMapping("/teams/tid")
    public Team getTeam(String id) {
        return null;
    }

}
