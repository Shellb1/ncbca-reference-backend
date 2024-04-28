package com.reference.ncbca.controller;

import com.reference.ncbca.dao.TeamsDao;
import com.reference.ncbca.model.Team;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/team")
public class TeamsController {

    @GetMapping("/tid")
    public Team getTeam(String id) {
        return null;
    }
}
