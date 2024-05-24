package com.reference.ncbca.handlers;

import com.reference.ncbca.model.ConferenceSummary;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ConferenceHandler {

    public ConferenceHandler() {

    }

    public List<ConferenceSummary> getConferenceSummaries(Integer season) {
        return Collections.emptyList();
    }

}
