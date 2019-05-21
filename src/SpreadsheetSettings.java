import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.server.ExportException;

public class SpreadsheetSettings {

    public static final String SPREADSHEET_NAME = "FFValueDraftMaster.xlsx";

    public static FileInputStream excelIn;
    public static FileOutputStream excelOut;
    public static File sheetFile;
    public static XSSFWorkbook workbook;
    public static XSSFSheet leagueSettingsSheet;
    public static XSSFSheet liveDraftSheet;
    public static XSSFSheet defaultRankingsSheet;
    public static XSSFSheet fantasyTeamsSheet;

    /**
     * Opens spreadsheet workbook and sheets
     * @throws Exception
     */
    public static void initSpreadsheet() throws Exception {

        sheetFile = new File(SpreadsheetSettings.SPREADSHEET_NAME);
        openInputStream();
        workbook = new XSSFWorkbook(excelIn);
        leagueSettingsSheet = workbook.getSheet("League Settings");
        liveDraftSheet = workbook.getSheet("Live Draft");
        defaultRankingsSheet = workbook.getSheet("Default Rankings");
        fantasyTeamsSheet = workbook.getSheet("Fantasy Teams");
    }

    public static void openInputStream() throws IOException {
        if (excelOut != null) {
            excelOut.close();
        }
        excelIn = new FileInputStream(sheetFile);
    }

    public static void openOutputStream() throws IOException {
        if (excelIn != null) {
            excelIn.close();
        }
        excelOut = new FileOutputStream(sheetFile);
    }

    // Spreadsheet-labeled row where the draft log starts in the spreadsheet
    public static final int DRAFT_LOG_START_ROW = 81;

    // Spreadsheet-labeled row where the fantasy teams start in the spreadsheet
    public static final int FANTASY_TEAM_START_ROW = 3;
}
