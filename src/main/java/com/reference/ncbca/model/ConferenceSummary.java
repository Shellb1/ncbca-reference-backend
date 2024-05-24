package com.reference.ncbca.model;

import java.util.List;

public record ConferenceSummary(String conferenceName, List<ConferenceRecord> conferenceRecordList, Integer season) {
}
