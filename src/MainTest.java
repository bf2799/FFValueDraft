import org.apache.commons.compress.utils.IOUtils;

import java.io.IOException;

public class MainTest {

    public static void main(String[] args) throws IOException {

        FantasyTeam team1 = new FantasyTeam(1);
        try {
            SpreadsheetSettings.initSpreadsheet();
            team1.readFantasyTeam();
            team1.generateFantasyTeam();
            team1.writeFantasyTeam();
            SpreadsheetSettings.openInputStream();
            SpreadsheetSettings.workbook.write(SpreadsheetSettings.excelOut);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(SpreadsheetSettings.excelIn);
            IOUtils.closeQuietly(SpreadsheetSettings.excelOut);
        }
    }
}
