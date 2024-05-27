package com.reference.ncbca.model;

import java.util.List;

public record TeamSummary(Team team, List<Season> seasons, List<DraftPick> draftPicks, List<Game> games, List<NTSeed> ntSeeds) {
}
