package com.reference.ncbca.model;

import com.reference.ncbca.model.dao.Season;

import java.util.List;

public record CoachSummary(List<Season> seasonsCoached, String coachName) {

}
