package com.reference.ncbca.controller;

import com.reference.ncbca.handlers.ConferenceHandler;
import com.reference.ncbca.model.ConferenceSummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ConferencesController {

    private final ConferenceHandler conferenceHandler;

    public ConferencesController(ConferenceHandler conferenceHandler) {
        this.conferenceHandler = conferenceHandler;
    }

    /**
     * Retrieves the summaries of conferences for a specific season.
     *
     * @param season The season for which to retrieve the conference summaries.
     * @return A list of ConferenceSummary objects representing the conferences for the specified season.
     */
    @GetMapping("/conferencesSummary")
    public List<ConferenceSummary> getConferenceSummariesForYear(@RequestParam("season") Integer season) {
        return conferenceHandler.getConferenceSummaries(season);
    }

    @GetMapping("/conferenceSummary")
    public ConferenceSummary getConferenceSummary(@RequestParam("conferenceName") String conferenceName, @RequestParam("season") Optional<Integer> season) {
        return conferenceHandler.getConferenceSummary(conferenceName, season.orElse(null));
    }
}
