import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.server.ExportException;

public class SpreadsheetSettings {

    public static final String SPREADSHEET_NAME = "FFVeDraftMaster.xlsx";

    public static FileInputStream excelIn;
    public static FileOutputStream excelOut;
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

        // Input files and sheets
        excelIn = new FileInputStream(new File(SpreadsheetSettings.SPREADSHEET_NAME));
        excelOut = new FileOutputStream(new File(SpreadsheetSettings.SPREADSHEET_NAME));
        workbook = new XSSFWorkbook(excelIn);
        leagueSettingsSheet = workbook.getSheet("League Settings");
        liveDraftSheet = workbook.getSheet("Live Draft");
        defaultRankingsSheet = workbook.getSheet("Default Rankings");
        fantasyTeamsSheet = workbook.getSheet("Fantasy Teams");
    }

    // Spreadsheet-labeled row where the draft log starts in the spreadsheet
    public static final int DRAFT_LOG_START_ROW = 81;

    // Spreadsheet-labeled row where the fantasy teams start in the spreadsheet
    public static final int FANTASY_TEAM_START_ROW = 3;
}
