import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.ArrayList;

public class FantasyTeam {


    private static XSSFRow curRow;
    private static int curOVR;

    private ArrayList<Integer> teamOVR = new ArrayList<Integer>();
    private ArrayList<Integer> teamQB = new ArrayList<Integer>();
    private ArrayList<Integer> teamRB = new ArrayList<Integer>();
    private ArrayList<Integer> teamWR = new ArrayList<Integer>();
    private ArrayList<Integer> teamTE = new ArrayList<Integer>();
    private ArrayList<Integer> teamDST = new ArrayList<Integer>();
    private ArrayList<Integer> teamK = new ArrayList<Integer>();
    private ArrayList<Integer> teamFLEX = new ArrayList<Integer>();
    private ArrayList<Integer> teamBench = new ArrayList<Integer>();
    private int draftPos;

    public FantasyTeam(int draftPos) {
        this.draftPos = draftPos;
    }

    /**
     * Reads fantasy team from full draft log in spreadsheet
     * Puts team in order based on drafted round in teamOVR
     * @throws Exception
     */
    public void readFantasyTeam() throws Exception {

        // Clear the array of the team overalls
        // Generate a new one with the correct number of players (OVR 0)
        teamOVR.clear();
        for (int i = 0; i < LeagueSettings.rosterNum; i++) {
            teamOVR.add(0);
        }

        // Loop through odd rounds for team
        for (int row = SpreadsheetSettings.DRAFT_LOG_START_ROW + draftPos - 2;
             row < LeagueSettings.rosterNum * LeagueSettings.numTeams() + SpreadsheetSettings.DRAFT_LOG_START_ROW - 1;
             row += LeagueSettings.numTeams() * 2) {

            // Get next row to read
            curRow = SpreadsheetSettings.liveDraftSheet.getRow(row);

            // If the Position column is empty, this and all future rounds are empty.
            // The loop can be broken
            if (curRow.getCell(3).getStringCellValue().length() == 0) {
                break;
            }

            // Get the overall of the current player
            curOVR = (int) curRow.getCell(5).getNumericCellValue();

            // Set the team overall in the correct round
            // The draft log's current row overall will be the set integer
            teamOVR.set((int) curRow.getCell(2).getNumericCellValue() - 1, (int) curOVR);

        }

        // Loop through even rounds for team
        for (int row = SpreadsheetSettings.DRAFT_LOG_START_ROW + (2 * LeagueSettings.numTeams()) - draftPos - 1;
             row < LeagueSettings.rosterNum * LeagueSettings.numTeams() + SpreadsheetSettings.DRAFT_LOG_START_ROW - 1;
             row += LeagueSettings.numTeams() * 2) {

            // Get next row to read
            curRow = SpreadsheetSettings.liveDraftSheet.getRow(row);

            // If the Position column is empty, this and all future rounds are empty.
            // The loop can be broken
            if (curRow.getCell(3).getStringCellValue().length() == 0) {
                break;
            }

            // Get the overall of the current player
            curOVR = (int) curRow.getCell(5).getNumericCellValue();

            // Set the team overall in the correct round
            // The draft log's current row overall will be the set integer
            teamOVR.set((int) curRow.getCell(2).getNumericCellValue() - 1, curOVR);
        }

    }

    /**
     * Creates correct starter and bench slots based on teamOVR
     */
    public void generateFantasyTeam() {

        // Clear position lists
        teamQB.clear();
        teamRB.clear();
        teamWR.clear();
        teamTE.clear();
        teamDST.clear();
        teamK.clear();
        teamFLEX.clear();
        teamBench.clear();

        // Loop through overalls in team
        for (int ovr : teamOVR) {

            // If no player selected, no future players have been selected
            // Break the loop in that case
            if (ovr == 0) {
                break;
            }

            // Get the player's position based on overall
            // For each position, add to starter if available
            // Otherwise, add to bench
            switch (SpreadsheetSettings.defaultRankingsSheet.getRow(ovr + 1).getCell(4).getStringCellValue()) {

                case "QB":
                    if (teamQB.size() < LeagueSettings.numQB()) {
                        teamQB.add(ovr);
                    } else {
                        teamBench.add(ovr);
                    }
                    break;

                case "RB":
                    if (teamRB.size() < LeagueSettings.numRB()) {
                        teamRB.add(ovr);
                    } else if (teamFLEX.size() < LeagueSettings.numFLEX()){
                        teamFLEX.add(ovr);
                    } else {
                        teamBench.add(ovr);
                    }
                    break;

                case "WR":
                    if (teamWR.size() < LeagueSettings.numWR()) {
                        teamWR.add(ovr);
                    } else if (teamFLEX.size() < LeagueSettings.numFLEX()){
                        teamFLEX.add(ovr);
                    } else {
                        teamBench.add(ovr);
                    }
                    break;

                case "TE":
                    if (teamTE.size() < LeagueSettings.numTE()) {
                        teamTE.add(ovr);
                    } else {
                        teamBench.add(ovr);
                    }
                    break;

                case "DST":
                    if (teamDST.size() < LeagueSettings.numDST()) {
                        teamDST.add(ovr);
                    } else {
                        teamBench.add(ovr);
                    }
                    break;

                case "K":
                    if (teamK.size() < LeagueSettings.numK()) {
                        teamK.add(ovr);
                    } else {
                        teamBench.add(ovr);
                    }
                    break;
            }
        }

    }

    /**
     * Writes generated fantasy team into spreadsheet on "Fantasy Teams" sheet
     * @throws Exception
     */
    public void writeFantasyTeam() throws Exception {

        // Initialize temporary string variables
        String playerName;
        String playerPosition;

        // Loop through starting QBs
        // Find player name, team, and position in Default Rankings based on overall
        // Set player name, position, and overall in fantasy team in correct row
        for (int qb = 0; qb < teamQB.size(); qb++) {
            playerName = SpreadsheetSettings.defaultRankingsSheet.getRow(teamQB.get(qb) + 1).getCell(2)
                    .getStringCellValue() + " " + SpreadsheetSettings.defaultRankingsSheet
                    .getRow(teamQB.get(qb) + 1).getCell(3).getStringCellValue();
            playerPosition = SpreadsheetSettings.defaultRankingsSheet.getRow(teamQB.get(qb) + 1)
                    .getCell(4).getStringCellValue();
            SpreadsheetSettings.fantasyTeamsSheet.getRow(qb + SpreadsheetSettings.FANTASY_TEAM_START_ROW - 1)
                    .getCell(2).setCellValue(playerName);
            SpreadsheetSettings.fantasyTeamsSheet.getRow(qb + SpreadsheetSettings.FANTASY_TEAM_START_ROW - 1)
                    .getCell(3).setCellValue(playerPosition);
            SpreadsheetSettings.fantasyTeamsSheet.getRow(qb + SpreadsheetSettings.FANTASY_TEAM_START_ROW - 1)
                    .getCell(4).setCellValue(teamQB.get(qb));
        }

        // Loop through starting RBs
        // Find player name, team, and position in Default Rankings based on overall
        // Set player name, position, and overall in fantasy team in correct row
        for (int rb = 0; rb < teamRB.size(); rb++) {
            playerName = SpreadsheetSettings.defaultRankingsSheet.getRow(teamRB.get(rb) + 1).getCell(2)
                    .getStringCellValue() +
                    " " + SpreadsheetSettings.defaultRankingsSheet.getRow(teamRB.get(rb) + 1).getCell(3)
                    .getStringCellValue();
            playerPosition = SpreadsheetSettings.defaultRankingsSheet.getRow(teamRB.get(rb) + 1)
                    .getCell(4).getStringCellValue();
            curRow = SpreadsheetSettings.fantasyTeamsSheet.getRow(rb + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() - 1);
            SpreadsheetSettings.fantasyTeamsSheet.getRow(rb + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() - 1).getCell(2).setCellValue(playerName);
            SpreadsheetSettings.fantasyTeamsSheet.getRow(rb + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() - 1).getCell(3).setCellValue(playerPosition);
            SpreadsheetSettings.fantasyTeamsSheet.getRow(rb + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() - 1).getCell(4).setCellValue(teamRB.get(rb));
        }

        // Loop through starting WRs
        // Find player name, team, and position in Default Rankings based on overall
        // Set player name, position, and overall in fantasy team in correct row
        for (int wr = 0; wr < teamWR.size(); wr++) {
            playerName = SpreadsheetSettings.defaultRankingsSheet.getRow(teamWR.get(wr) + 1).getCell(2)
                    .getStringCellValue() + " " + SpreadsheetSettings.defaultRankingsSheet
                    .getRow(teamWR.get(wr) + 1).getCell(3).getStringCellValue();
            playerPosition = SpreadsheetSettings.defaultRankingsSheet.getRow(teamWR.get(wr) + 1)
                    .getCell(4).getStringCellValue();
            SpreadsheetSettings.fantasyTeamsSheet.getRow(wr + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() - 1).getCell(2).setCellValue(playerName);
            SpreadsheetSettings.fantasyTeamsSheet.getRow(wr + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() - 1).getCell(3).setCellValue(playerPosition);
            SpreadsheetSettings.fantasyTeamsSheet.getRow(wr + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() - 1).getCell(4).setCellValue(teamWR.get(wr));
        }

        // Loop through starting TEs
        // Find player name, team, and position in Default Rankings based on overall
        // Set player name, position, and overall in fantasy team in correct row
        for (int te = 0; te < teamTE.size(); te++) {
            playerName = SpreadsheetSettings.defaultRankingsSheet.getRow(teamTE.get(te) + 1).getCell(2)
                    .getStringCellValue() + " " + SpreadsheetSettings.defaultRankingsSheet
                    .getRow(teamTE.get(te) + 1).getCell(3).getStringCellValue();
            playerPosition = SpreadsheetSettings.defaultRankingsSheet.getRow(teamTE.get(te) + 1)
                    .getCell(4).getStringCellValue();
            SpreadsheetSettings.fantasyTeamsSheet.getRow(te + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() - 1)
                    .getCell(2).setCellValue(playerName);
            SpreadsheetSettings.fantasyTeamsSheet.getRow(te + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() - 1)
                    .getCell(3).setCellValue(playerPosition);
            SpreadsheetSettings.fantasyTeamsSheet.getRow(te + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() - 1)
                    .getCell(4).setCellValue(teamTE.get(te));
        }

        // Loop through starting FLEXs
        // Find player name, team, and position in Default Rankings based on overall
        // Set player name, position, and overall in fantasy team in correct row
        for (int flex = 0; flex < teamFLEX.size(); flex++) {
            playerName = SpreadsheetSettings.defaultRankingsSheet.getRow(teamFLEX.get(flex) + 1)
                    .getCell(2).getStringCellValue() + " " + SpreadsheetSettings.defaultRankingsSheet
                    .getRow(teamFLEX.get(flex) + 1).getCell(3).getStringCellValue();
            playerPosition = SpreadsheetSettings.defaultRankingsSheet.getRow(teamFLEX.get(flex) + 1)
                    .getCell(4).getStringCellValue();
            SpreadsheetSettings.fantasyTeamsSheet.getRow(flex + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() - 1)
                    .getCell(2).setCellValue(playerName);
            SpreadsheetSettings.fantasyTeamsSheet.getRow(flex + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() - 1)
                    .getCell(3).setCellValue(playerPosition);
            SpreadsheetSettings.fantasyTeamsSheet.getRow(flex + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() - 1)
                    .getCell(4).setCellValue(teamFLEX.get(flex));
        }

        // Loop through starting DSTs
        // Find player name, team, and position in Default Rankings based on overall
        // Set player name, position, and overall in fantasy team in correct row
        for (int dst = 0; dst < teamDST.size(); dst++) {
            playerName = SpreadsheetSettings.defaultRankingsSheet.getRow(teamDST.get(dst) + 1)
                    .getCell(2).getStringCellValue() + " " + SpreadsheetSettings.defaultRankingsSheet
                    .getRow(teamDST.get(dst) + 1).getCell(3).getStringCellValue();
            playerPosition = SpreadsheetSettings.defaultRankingsSheet.getRow(teamDST.get(dst) + 1)
                    .getCell(4).getStringCellValue();
            SpreadsheetSettings.fantasyTeamsSheet.getRow(dst + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() +
                    LeagueSettings.numFLEX() - 1).getCell(2).setCellValue(playerName);
            SpreadsheetSettings.fantasyTeamsSheet.getRow(dst + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() +
                    LeagueSettings.numFLEX() - 1).getCell(3).setCellValue(playerPosition);
            SpreadsheetSettings.fantasyTeamsSheet.getRow(dst + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() +
                    LeagueSettings.numFLEX() - 1).getCell(4).setCellValue(teamDST.get(dst));
        }

        // Loop through starting Ks
        // Find player name, team, and position in Default Rankings based on overall
        // Set player name, position, and overall in fantasy team in correct row
        for (int k = 0; k < teamK.size(); k++) {
            playerName = SpreadsheetSettings.defaultRankingsSheet.getRow(teamK.get(k) + 1)
                    .getCell(2).getStringCellValue() + " " + SpreadsheetSettings.defaultRankingsSheet
                    .getRow(teamK.get(k) + 1).getCell(3).getStringCellValue();
            playerPosition = SpreadsheetSettings.defaultRankingsSheet.getRow(teamK.get(k) + 1)
                    .getCell(4).getStringCellValue();
            SpreadsheetSettings.fantasyTeamsSheet.getRow(k + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() +
                    LeagueSettings.numFLEX() + LeagueSettings.numDST() - 1).getCell(2).setCellValue(playerName);
            SpreadsheetSettings.fantasyTeamsSheet.getRow(k + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() +
                    LeagueSettings.numFLEX() + LeagueSettings.numDST() - 1).getCell(3).setCellValue(playerPosition);
            SpreadsheetSettings.fantasyTeamsSheet.getRow(k + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() +
                    LeagueSettings.numFLEX() + LeagueSettings.numDST() - 1).getCell(4).setCellValue(teamK.get(k));
        }

        // Loop through bench
        // Find player name, team, and position in Default Rankings based on overall
        // Set player name, position, and overall in fantasy team in correct row
        for (int bn = 0; bn < teamBench.size(); bn++) {
            playerName = SpreadsheetSettings.defaultRankingsSheet.getRow(teamBench.get(bn) + 1)
                    .getCell(2).getStringCellValue() + " " + SpreadsheetSettings.defaultRankingsSheet
                    .getRow(teamBench.get(bn) + 1).getCell(3).getStringCellValue();
            playerPosition = SpreadsheetSettings.defaultRankingsSheet.getRow(teamBench.get(bn) + 1)
                    .getCell(4).getStringCellValue();
            SpreadsheetSettings.fantasyTeamsSheet.getRow(bn + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() +
                    LeagueSettings.numFLEX() + LeagueSettings.numDST() + LeagueSettings.numK() - 1)
                    .getCell(2).setCellValue(playerName);
            SpreadsheetSettings.fantasyTeamsSheet.getRow(bn + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() +
                    LeagueSettings.numFLEX() + LeagueSettings.numDST() + LeagueSettings.numK() - 1)
                    .getCell(3).setCellValue(playerPosition);
            SpreadsheetSettings.fantasyTeamsSheet.getRow(bn + SpreadsheetSettings.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() +
                    LeagueSettings.numFLEX() + LeagueSettings.numDST() + LeagueSettings.numK()- 1)
                    .getCell(4).setCellValue(teamBench.get(bn));
        }

        SpreadsheetSettings.workbook.write(SpreadsheetSettings.excelOut);

    }

}
