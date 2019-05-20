import java.io.IOException;

public class MainTest {

    public static void main(String[] args) throws IOException {

        FantasyTeam team1 = new FantasyTeam(1);
        try {
            SpreadsheetSettings.initSpreadsheet();
            team1.readFantasyTeam();
            team1.generateFantasyTeam();
            team1.writeFantasyTeam();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SpreadsheetSettings.excelIn.close();
            SpreadsheetSettings.excelOut.close();
        }
    }
}
