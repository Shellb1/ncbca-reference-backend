package com.reference.ncbca.handlers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.reference.ncbca.model.dao.DraftPick;
import com.reference.ncbca.model.dao.Team;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LoadNBLHandler {

    private final NBLHandler nblHandler;
    private final TeamsHandler teamsHandler;

    public LoadNBLHandler(NBLHandler nblHandler, TeamsHandler teamsHandler) {
        this.nblHandler = nblHandler;
        this.teamsHandler = teamsHandler;
    }

    public void loadNblExport(MultipartFile file, Integer season, Boolean loadDraftPicks) throws IOException {
        byte[] fileBytes = file.getBytes();

        // Remove BOM if present
        if (fileBytes.length > 3 && fileBytes[0] == (byte) 0xEF && fileBytes[1] == (byte) 0xBB && fileBytes[2] == (byte) 0xBF) {
            fileBytes = Arrays.copyOfRange(fileBytes, 3, fileBytes.length);
        }

        List<DraftPick> draftPickList = new ArrayList<>();
        List<Team> allTeams = teamsHandler.listAllActiveTeams();
        String json = new String(fileBytes, StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(json);
        ObjectNode export = mapper.readTree(parser);

        for (JsonNode player: export.get("players")) {
            if (player.get("draft").get("year").intValue() == season) {
                JsonNode draft = player.get("draft");
                if (draft.get("round").intValue() != 0) {
                    String collegeTeamName = getFullCollegeName(player.get("college").asText(), allTeams);
                    String firstName = player.get("firstName").asText();
                    String lastName = player.get("lastName").asText();
                    String fullName = firstName + " " + lastName;
                    int round = draft.get("round").intValue();
                    int pick = draft.get("pick").intValue();
                    String position = player.get("ratings").get(0).get("pos").asText();
                    String coachName = getActiveCoachFromTeam(collegeTeamName, allTeams);
                    DraftPick draftPick = new DraftPick(fullName, collegeTeamName, round, pick, season, position, coachName);
                    draftPickList.add(draftPick);
                }
            }
        }

        nblHandler.loadDraftPicks(draftPickList);
    }

    private String getActiveCoachFromTeam(String collegeTeamName, List<Team> allTeams) {
        for (Team team: allTeams) {
            if (team.name().startsWith(collegeTeamName)) {
                return team.coach();
            }
        }
        return null;
    }

    private String getFullCollegeName(String college, List<Team> allTeams) {
        for (Team team: allTeams) {
            if (team.name().startsWith(college)) {
                return team.name();
            }
        }
        return "";
    }
}
