import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.ArrayList;

public class FantasyTeam {

    // List of all fantasy teams
    public static ArrayList<FantasyTeam> fantasyTeams = new ArrayList<FantasyTeam>();

    // Static temporary variables
    private static XSSFRow curRow;
    private static int curOVR;

    // Each fantasy team has list of overall IDs at each position and overall
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
        for (int row = Spreadsheet.DRAFT_LOG_START_ROW + draftPos - 2;
             row < LeagueSettings.rosterNum * LeagueSettings.numTeams() + Spreadsheet.DRAFT_LOG_START_ROW - 1;
             row += LeagueSettings.numTeams() * 2) {

            // Get next row to read
            curRow = Spreadsheet.liveDraftSheet.getRow(row);

            // If the Position column is empty, this and all future rounds are empty.
            // The loop can be broken
            if (curRow.getCell(5).getStringCellValue().length() == 0) {
                break;
            }

            // Get the overall of the current player
            curOVR = (int) curRow.getCell(3).getNumericCellValue();

            // Set the team overall in the correct round
            // The draft log's current row overall will be the set integer
            teamOVR.set((int) curRow.getCell(2).getNumericCellValue() - 1, (int) curOVR);

        }

        // Loop through even rounds for team
        for (int row = Spreadsheet.DRAFT_LOG_START_ROW + (2 * LeagueSettings.numTeams()) - draftPos - 1;
             row < LeagueSettings.rosterNum * LeagueSettings.numTeams() + Spreadsheet.DRAFT_LOG_START_ROW - 1;
             row += LeagueSettings.numTeams() * 2) {

            // Get next row to read
            curRow = Spreadsheet.liveDraftSheet.getRow(row);

            // If the Position column is empty, this and all future rounds are empty.
            // The loop can be broken
            if (curRow.getCell(5).getStringCellValue().length() == 0) {
                break;
            }

            // Get the overall of the current player
            curOVR = (int) curRow.getCell(3).getNumericCellValue();

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
            switch (Spreadsheet.defaultRankingsSheet.getRow(ovr + 1).getCell(4).getStringCellValue()) {

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
            playerName = Spreadsheet.defaultRankingsSheet.getRow(teamQB.get(qb) + 1).getCell(2)
                    .getStringCellValue() + " " + Spreadsheet.defaultRankingsSheet
                    .getRow(teamQB.get(qb) + 1).getCell(3).getStringCellValue();
            playerPosition = Spreadsheet.defaultRankingsSheet.getRow(teamQB.get(qb) + 1)
                    .getCell(4).getStringCellValue();
            Spreadsheet.fantasyTeamsSheet.getRow(qb + Spreadsheet.FANTASY_TEAM_START_ROW - 1)
                    .getCell(2 + 5 * (draftPos - 1)).setCellValue(playerName);
            Spreadsheet.fantasyTeamsSheet.getRow(qb + Spreadsheet.FANTASY_TEAM_START_ROW - 1)
                    .getCell(3 + 5 * (draftPos - 1)).setCellValue(playerPosition);
            Spreadsheet.fantasyTeamsSheet.getRow(qb + Spreadsheet.FANTASY_TEAM_START_ROW - 1)
                    .getCell(4 + 5 * (draftPos - 1)).setCellValue(teamQB.get(qb));
        }

        // Loop through starting RBs
        // Find player name, team, and position in Default Rankings based on overall
        // Set player name, position, and overall in fantasy team in correct row
        for (int rb = 0; rb < teamRB.size(); rb++) {
            playerName = Spreadsheet.defaultRankingsSheet.getRow(teamRB.get(rb) + 1).getCell(2)
                    .getStringCellValue() +
                    " " + Spreadsheet.defaultRankingsSheet.getRow(teamRB.get(rb) + 1).getCell(3)
                    .getStringCellValue();
            playerPosition = Spreadsheet.defaultRankingsSheet.getRow(teamRB.get(rb) + 1)
                    .getCell(4).getStringCellValue();
            curRow = Spreadsheet.fantasyTeamsSheet.getRow(rb + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() - 1);
            Spreadsheet.fantasyTeamsSheet.getRow(rb + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() - 1).getCell(2 + 5 * (draftPos - 1)).setCellValue(playerName);
            Spreadsheet.fantasyTeamsSheet.getRow(rb + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() - 1).getCell(3 + 5 * (draftPos - 1)).setCellValue(playerPosition);
            Spreadsheet.fantasyTeamsSheet.getRow(rb + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() - 1).getCell(4 + 5 * (draftPos - 1)).setCellValue(teamRB.get(rb));
        }

        // Loop through starting WRs
        // Find player name, team, and position in Default Rankings based on overall
        // Set player name, position, and overall in fantasy team in correct row
        for (int wr = 0; wr < teamWR.size(); wr++) {
            playerName = Spreadsheet.defaultRankingsSheet.getRow(teamWR.get(wr) + 1).getCell(2)
                    .getStringCellValue() + " " + Spreadsheet.defaultRankingsSheet
                    .getRow(teamWR.get(wr) + 1).getCell(3).getStringCellValue();
            playerPosition = Spreadsheet.defaultRankingsSheet.getRow(teamWR.get(wr) + 1)
                    .getCell(4).getStringCellValue();
            Spreadsheet.fantasyTeamsSheet.getRow(wr + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() - 1).getCell(2 + 5 * (draftPos - 1)).setCellValue(playerName);
            Spreadsheet.fantasyTeamsSheet.getRow(wr + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() - 1).getCell(3 + 5 * (draftPos - 1)).setCellValue(playerPosition);
            Spreadsheet.fantasyTeamsSheet.getRow(wr + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() - 1).getCell(4 + 5 * (draftPos - 1)).setCellValue(teamWR.get(wr));
        }

        // Loop through starting TEs
        // Find player name, team, and position in Default Rankings based on overall
        // Set player name, position, and overall in fantasy team in correct row
        for (int te = 0; te < teamTE.size(); te++) {
            playerName = Spreadsheet.defaultRankingsSheet.getRow(teamTE.get(te) + 1).getCell(2)
                    .getStringCellValue() + " " + Spreadsheet.defaultRankingsSheet
                    .getRow(teamTE.get(te) + 1).getCell(3).getStringCellValue();
            playerPosition = Spreadsheet.defaultRankingsSheet.getRow(teamTE.get(te) + 1)
                    .getCell(4).getStringCellValue();
            Spreadsheet.fantasyTeamsSheet.getRow(te + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() - 1)
                    .getCell(2 + 5 * (draftPos - 1)).setCellValue(playerName);
            Spreadsheet.fantasyTeamsSheet.getRow(te + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() - 1)
                    .getCell(3 + 5 * (draftPos - 1)).setCellValue(playerPosition);
            Spreadsheet.fantasyTeamsSheet.getRow(te + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() - 1)
                    .getCell(4 + 5 * (draftPos - 1)).setCellValue(teamTE.get(te));
        }

        // Loop through starting FLEXs
        // Find player name, team, and position in Default Rankings based on overall
        // Set player name, position, and overall in fantasy team in correct row
        for (int flex = 0; flex < teamFLEX.size(); flex++) {
            playerName = Spreadsheet.defaultRankingsSheet.getRow(teamFLEX.get(flex) + 1)
                    .getCell(2).getStringCellValue() + " " + Spreadsheet.defaultRankingsSheet
                    .getRow(teamFLEX.get(flex) + 1).getCell(3).getStringCellValue();
            playerPosition = Spreadsheet.defaultRankingsSheet.getRow(teamFLEX.get(flex) + 1)
                    .getCell(4).getStringCellValue();
            Spreadsheet.fantasyTeamsSheet.getRow(flex + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() - 1)
                    .getCell(2 + 5 * (draftPos - 1)).setCellValue(playerName);
            Spreadsheet.fantasyTeamsSheet.getRow(flex + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() - 1)
                    .getCell(3 + 5 * (draftPos - 1)).setCellValue(playerPosition);
            Spreadsheet.fantasyTeamsSheet.getRow(flex + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() - 1)
                    .getCell(4 + 5 * (draftPos - 1)).setCellValue(teamFLEX.get(flex));
        }

        // Loop through starting DSTs
        // Find player name, team, and position in Default Rankings based on overall
        // Set player name, position, and overall in fantasy team in correct row
        for (int dst = 0; dst < teamDST.size(); dst++) {
            playerName = Spreadsheet.defaultRankingsSheet.getRow(teamDST.get(dst) + 1)
                    .getCell(2).getStringCellValue() + " " + Spreadsheet.defaultRankingsSheet
                    .getRow(teamDST.get(dst) + 1).getCell(3).getStringCellValue();
            playerPosition = Spreadsheet.defaultRankingsSheet.getRow(teamDST.get(dst) + 1)
                    .getCell(4).getStringCellValue();
            Spreadsheet.fantasyTeamsSheet.getRow(dst + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() +
                    LeagueSettings.numFLEX() - 1).getCell(2 + 5 * (draftPos - 1)).setCellValue(playerName);
            Spreadsheet.fantasyTeamsSheet.getRow(dst + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() +
                    LeagueSettings.numFLEX() - 1).getCell(3 + 5 * (draftPos - 1)).setCellValue(playerPosition);
            Spreadsheet.fantasyTeamsSheet.getRow(dst + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() +
                    LeagueSettings.numFLEX() - 1).getCell(4 + 5 * (draftPos - 1)).setCellValue(teamDST.get(dst));
        }

        // Loop through starting Ks
        // Find player name, team, and position in Default Rankings based on overall
        // Set player name, position, and overall in fantasy team in correct row
        for (int k = 0; k < teamK.size(); k++) {
            playerName = Spreadsheet.defaultRankingsSheet.getRow(teamK.get(k) + 1)
                    .getCell(2).getStringCellValue() + " " + Spreadsheet.defaultRankingsSheet
                    .getRow(teamK.get(k) + 1).getCell(3).getStringCellValue();
            playerPosition = Spreadsheet.defaultRankingsSheet.getRow(teamK.get(k) + 1)
                    .getCell(4).getStringCellValue();
            Spreadsheet.fantasyTeamsSheet.getRow(k + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() +
                    LeagueSettings.numFLEX() + LeagueSettings.numDST() - 1).getCell(2 + 5 * (draftPos - 1)).setCellValue(playerName);
            Spreadsheet.fantasyTeamsSheet.getRow(k + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() +
                    LeagueSettings.numFLEX() + LeagueSettings.numDST() - 1).getCell(3 + 5 * (draftPos - 1)).setCellValue(playerPosition);
            Spreadsheet.fantasyTeamsSheet.getRow(k + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() +
                    LeagueSettings.numFLEX() + LeagueSettings.numDST() - 1).getCell(4 + 5 * (draftPos - 1)).setCellValue(teamK.get(k));
        }

        // Loop through bench
        // Find player name, team, and position in Default Rankings based on overall
        // Set player name, position, and overall in fantasy team in correct row
        for (int bn = 0; bn < teamBench.size(); bn++) {
            playerName = Spreadsheet.defaultRankingsSheet.getRow(teamBench.get(bn) + 1)
                    .getCell(2).getStringCellValue() + " " + Spreadsheet.defaultRankingsSheet
                    .getRow(teamBench.get(bn) + 1).getCell(3).getStringCellValue();
            playerPosition = Spreadsheet.defaultRankingsSheet.getRow(teamBench.get(bn) + 1)
                    .getCell(4).getStringCellValue();
            Spreadsheet.fantasyTeamsSheet.getRow(bn + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() +
                    LeagueSettings.numFLEX() + LeagueSettings.numDST() + LeagueSettings.numK() - 1)
                    .getCell(2 + 5 * (draftPos - 1)).setCellValue(playerName);
            Spreadsheet.fantasyTeamsSheet.getRow(bn + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() +
                    LeagueSettings.numFLEX() + LeagueSettings.numDST() + LeagueSettings.numK() - 1)
                    .getCell(3 + 5 * (draftPos - 1)).setCellValue(playerPosition);
            Spreadsheet.fantasyTeamsSheet.getRow(bn + Spreadsheet.FANTASY_TEAM_START_ROW +
                    LeagueSettings.numQB() + LeagueSettings.numRB() + LeagueSettings.numWR() + LeagueSettings.numTE() +
                    LeagueSettings.numFLEX() + LeagueSettings.numDST() + LeagueSettings.numK()- 1)
                    .getCell(4 + 5 * (draftPos - 1)).setCellValue(teamBench.get(bn));
        }

    }

}
