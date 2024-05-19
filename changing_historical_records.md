
# Change Team's Games
UPDATE Games
SET
home_team_name = CASE WHEN home_team_name = 'Western Kentucky Hilltoppers' THEN 'Kansas City Roos' ELSE home_team_name END,
away_team_name = CASE WHEN away_team_name = 'Western Kentucky Hilltoppers' THEN 'Kansas City Roos' ELSE away_team_name END,
winning_team_name = CASE WHEN winning_team_name = 'Western Kentucky Hilltoppers' THEN 'Kansas City Roos' ELSE winning_team_name END,
losing_team_name = CASE WHEN losing_team_name = 'Western Kentucky Hilltoppers' THEN 'Kansas City Roos' ELSE losing_team_name END,
winning_coach_name = CASE WHEN winning_coach_name = 'Western Kentucky Hilltoppers' THEN 'Kansas City Roos' ELSE winning_coach_name END,
losing_coach_name = CASE WHEN losing_coach_name = 'Western Kentucky Hilltoppers' THEN 'Kansas City Roos' ELSE losing_coach_name END
WHERE
season = 2078
AND (
home_team_name = 'Western Kentucky Hilltoppers'
OR away_team_name = 'Western Kentucky Hilltoppers'
OR winning_team_name = 'Western Kentucky Hilltoppers'
OR losing_team_name = 'Western Kentucky Hilltoppers'
OR winning_coach_name = 'Western Kentucky Hilltoppers'
OR losing_coach_name = 'Western Kentucky Hilltoppers'
);

# Update historic records by changing coaches:
To change historic records for user

UPDATE Games
SET winning_coach_name = null
WHERE winning_team_name = 'Howard Bison'
AND season = 2078;

UPDATE Games
SET losing_coach_name = null
WHERE losing_team_name = 'Howard Bison'
AND season = 2078;

UPDATE Seasons
SET coach_name = null
WHERE team_name = 'Howard Bison'
AND season = 2078;

UPDATE drafted_players
SET coach_name = null
WHERE college_team_name = 'Howard'
AND season = 2078;

COMMIT;