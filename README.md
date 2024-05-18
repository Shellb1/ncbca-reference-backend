# ncbca-reference-backend

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


