package com.reference.ncbca.controller;

import com.reference.ncbca.handlers.LoadExportHandler;
import com.reference.ncbca.handlers.SeasonsHandler;
import com.reference.ncbca.handlers.TeamsHandler;
import com.reference.ncbca.model.Season;
import com.reference.ncbca.model.Team;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class SeasonsController {

    private final SeasonsHandler seasonsHandler;

    public SeasonsController(SeasonsHandler seasonsHandler) {
        this.seasonsHandler = seasonsHandler;
    }
    @GetMapping("/listSeasons")
    public List<Season> getTeam(@RequestParam("year") Integer year) {
        return seasonsHandler.listSeasonsForYear(year);
    }
}
