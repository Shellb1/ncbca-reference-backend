package com.reference.ncbca.controller;


import com.reference.ncbca.handlers.LoadExportHandler;
import com.reference.ncbca.handlers.LoadNBLHandler;
import com.reference.ncbca.handlers.NBLHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class AdminController {

    private final LoadExportHandler loadExportHandler;
    private final LoadNBLHandler loadNBLHandler;

    public AdminController(LoadExportHandler loadExportHandler, LoadNBLHandler loadNBLHandler) {
        this.loadExportHandler = loadExportHandler;
        this.loadNBLHandler = loadNBLHandler;

    }

    @PostMapping("/load")
    public void loadExport(@RequestParam("export") MultipartFile export,
                           @RequestParam("loadTeams") Boolean loadTeams,
                           @RequestParam("loadSeasons") Boolean loadSeasons,
                           @RequestParam("loadGames") Boolean loadGames,
                           @RequestParam("loadSchedules") Boolean loadSchedules,
                           @RequestParam("loadCoaches") Boolean loadCoaches,
                           @RequestParam("season") Integer season,
                           @RequestParam("loadCT") Boolean loadCT,
                           @RequestParam("loadNIT") Boolean loadNIT,
                           @RequestParam("loadFirstFour") Boolean loadFirstFour,
                           @RequestParam("loadNT") Boolean loadNT) throws IOException {
        loadExportHandler.loadExport(export, loadTeams, loadSeasons, loadGames, loadSchedules, loadCoaches, season, loadCT, loadNIT, loadFirstFour, loadNT);
    }

    @PostMapping("/loadNBL")
    public void loadExport(@RequestParam("export") MultipartFile export,
                           @RequestParam("season") Integer season,
                           @RequestParam("loadDraftPicks") Boolean loadDraftPicks) throws IOException {
        loadNBLHandler.loadNblExport(export, season, loadDraftPicks);
    }
}
