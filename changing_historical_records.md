
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

START TRANSACTION;

UPDATE Games
SET winning_coach_name = ?
WHERE winning_team_name = ?;

UPDATE Games
SET losing_coach_name = ?
WHERE losing_team_name = ?;

UPDATE Seasons
SET coach_name = ?
WHERE team_name = ?;

COMMIT;
