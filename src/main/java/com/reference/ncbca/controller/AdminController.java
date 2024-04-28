package com.reference.ncbca.controller;


import com.reference.ncbca.handlers.LoadExportHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public void loadExport(@RequestParam("file") MultipartFile file,
                           @RequestParam("loadTeams") Boolean loadTeams) throws IOException {
        loadExportHandler.loadExport(file, loadTeams);
    }
}
