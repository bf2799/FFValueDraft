import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import

/*
Features to add
    Clear Fantasy teams at beginning
    Check draft log for incorrect use before continuing
 */

public class Main {

    public static void main(String[] args) throws IOException {

        try {

            // Initialize master and database spreadsheets
            Spreadsheet.initMasterSheet();
            Spreadsheet.initDatabaseSheet();

            // Create correct number of new fantasy teams
            // For each team,
            //      Read in the fantasy team from the spreadsheet Live Draft tab's draft log
            //      Generate the fantasy team positions
            //      Update the correct cells in the spreadsheet Fantasy Teams tab
            for (int team = 0; team < LeagueSettings.numTeams(); team++) {
                FantasyTeam.fantasyTeams.add(new FantasyTeam(team + 1));
                FantasyTeam.fantasyTeams.get(team).readFantasyTeam();
                FantasyTeam.fantasyTeams.get(team).generateFantasyTeam();
                FantasyTeam.fantasyTeams.get(team).writeFantasyTeam();
            }

            // Write all changes to the spreadsheet
            Spreadsheet.writeMasterOutputStream();

            // Open spreadsheet on desktop upon program completion
            Spreadsheet.openMasterSpreadsheet();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // At end of program, close file output and file input streams if not already closed
            if (Spreadsheet.masterExcelOut != null)
                Spreadsheet.masterExcelOut.close();
            if (Spreadsheet.masterExcelIn != null)
                Spreadsheet.masterExcelIn.close();
            if (Spreadsheet.databaseExcelIn != null)
                Spreadsheet.databaseExcelIn.close();
        }
    }
}
