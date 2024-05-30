package com.reference.ncbca.controller;


import com.opencsv.exceptions.CsvException;
import com.reference.ncbca.handlers.BackloadingHandler;
import com.reference.ncbca.handlers.LoadExportHandler;
import com.reference.ncbca.handlers.LoadNBLHandler;
import com.reference.ncbca.handlers.NBLHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class AdminController {

    private final LoadExportHandler loadExportHandler;
    private final LoadNBLHandler loadNBLHandler;
    private final BackloadingHandler backloadingHandler;

    public AdminController(LoadExportHandler loadExportHandler, LoadNBLHandler loadNBLHandler, BackloadingHandler backloadingHandler) {
        this.loadExportHandler = loadExportHandler;
        this.loadNBLHandler = loadNBLHandler;
        this.backloadingHandler = backloadingHandler;
    }
    /**
     * Loads the export file and performs various data loading operations based on the provided parameters.
     *
     * @param  export         the multipart file containing the export data
     * @param  loadTeams      indicates whether to load teams data or not
     * @param  loadSeasons    indicates whether to load seasons data or not
     * @param  loadGames      indicates whether to load games data or not
     * @param  loadSchedules  indicates whether to load schedules data or not
     * @param  loadCoaches     indicates whether to load coaches data or not
     * @param  season         the season for which to load the data
     * @param  loadCT         indicates whether to load CT data or not
     * @param  loadNIT        indicates whether to load NIT data or not
     * @param  loadFirstFour  indicates whether to load first four data or not
     * @param  loadNT         indicates whether to load NT data or not
     * @param  loadNTSeeds    indicates whether to load NT seeds data or not
     * @throws IOException    if an I/O error occurs while reading the export file
     * @throws CsvException   if a CSV parsing error occurs
     */
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
                           @RequestParam("loadNT") Boolean loadNT,
                           @RequestParam("loadNTSeeds") Boolean loadNTSeeds,
                           @RequestParam("loadStats") Boolean loadStats) throws IOException, CsvException {
        loadExportHandler.loadExport(export, loadTeams, loadSeasons, loadGames, loadSchedules, loadCoaches, season, loadCT, loadNIT, loadFirstFour, loadNT, loadNTSeeds, loadStats);
    }

    /**
     * Loads the NBL export file and optionally loads draft picks for a given season.
     *
     * @param  export         the multipart file containing the NBL export
     * @param  season         the season for which to load the export and draft picks
     * @param  loadDraftPicks whether to load draft picks or not
     * @throws IOException if an I/O error occurs while reading the export file
     */
    @PostMapping("/loadNBL")
    public void loadExport(@RequestParam("export") MultipartFile export,
                           @RequestParam("season") Integer season,
                           @RequestParam("loadDraftPicks") Boolean loadDraftPicks) throws IOException {
        loadNBLHandler.loadNblExport(export, season, loadDraftPicks);
    }

    // DO NOT ENABLE THIS WHEN PUSHING TO SERVER
//    @GetMapping("/backload")
//    public void backload() {
//        backloadingHandler.backloadGames();
//    }
}
