package com.reference.ncbca.controller;


import com.reference.ncbca.handlers.LoadExportHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class AdminController {

    private final LoadExportHandler loadExportHandler;

    public AdminController(LoadExportHandler loadExportHandler) {
        this.loadExportHandler = loadExportHandler;
    }

    @PostMapping("/load")
    public void loadExport(@RequestParam("export") MultipartFile export,
                           @RequestParam("loadTeams") Boolean loadTeams,
                           @RequestParam("loadSeasons") Boolean loadSeasons,
                           @RequestParam("loadGames") Boolean loadGames,
                           @RequestParam("loadSchedules") Boolean loadSchedules,
                           @RequestParam("loadCoaches") Boolean loadCoaches,
                           @RequestParam("season") Integer season) throws IOException {
        loadExportHandler.loadExport(export, loadTeams, loadSeasons, loadGames, loadSchedules, loadCoaches, season);
    }
}
