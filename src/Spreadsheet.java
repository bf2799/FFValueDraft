import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Spreadsheet {

    public static final String MASTER_SHEET_NAME = "FFValueDraftMaster.xlsx";
    public static final String STAT_DATABASE_NAME = "FFYearlyStatDatabase.xlsx";

    // Master I/O streams and workbooks
    public static FileInputStream masterExcelIn;
    public static FileOutputStream masterExcelOut;
    public static XSSFWorkbook masterWorkbook;

    // Database input stream and workbooks
    public static FileInputStream databaseExcelIn;
    public static XSSFWorkbook databaseWorkbook;

    // Files of spreadsheets
    public static File masterSheetFile;
    public static File databaseSheetFile;

    // Master spreadsheet sheets
    public static XSSFSheet leagueSettingsSheet;
    public static XSSFSheet liveDraftSheet;
    public static XSSFSheet defaultRankingsSheet;
    public static XSSFSheet fantasyTeamsSheet;

    // Database spreadsheet sheets
    public static XSSFSheet qbHistorySheet;
    public static XSSFSheet rbHistorySheet;
    public static XSSFSheet wrHistorySheet;
    public static XSSFSheet teHistorySheet;
    public static XSSFSheet kHistorySheet;
    public static XSSFSheet dstHistorySheet;

    /**
     * Opens FFValueDraftMaster spreadsheet masterWorkbook and sheets
     * @throws Exception
     */
    public static void initMasterSheet() throws Exception {

        if (masterExcelOut != null) {
            masterExcelOut.close();
        }
        masterSheetFile = new File(MASTER_SHEET_NAME);
        masterExcelIn = new FileInputStream(masterSheetFile);
        masterWorkbook = new XSSFWorkbook(masterExcelIn);
        leagueSettingsSheet = masterWorkbook.getSheet("League Settings");
        liveDraftSheet = masterWorkbook.getSheet("Live Draft Picks");
        defaultRankingsSheet = masterWorkbook.getSheet("Default Rankings");
        fantasyTeamsSheet = masterWorkbook.getSheet("Fantasy Teams");
    }

    /**
     * Opens FFYearlyStatDatabase spreadsheet masterWorkbook and sheets
     * @throws Exception
     */
    public static void initDatabaseSheet() throws Exception {

        databaseSheetFile = new File(STAT_DATABASE_NAME);
        databaseExcelIn = new FileInputStream(databaseSheetFile);
        databaseWorkbook = new XSSFWorkbook(databaseExcelIn);
        qbHistorySheet = databaseWorkbook.getSheet("QB");
        rbHistorySheet = databaseWorkbook.getSheet("RB");
        wrHistorySheet = databaseWorkbook.getSheet("WR");
        teHistorySheet = databaseWorkbook.getSheet("TE");
        kHistorySheet = databaseWorkbook.getSheet("K");
        dstHistorySheet = databaseWorkbook.getSheet("DST");

    }

    /**
     * Close the master input stream if open
     * Open the output stream
     * Write all changes to the spreadsheet
     * Close file output stream
     * @throws IOException
     */
    public static void writeMasterOutputStream() throws IOException {
        if (masterExcelIn != null)
            masterExcelIn.close();
        masterExcelOut = new FileOutputStream(masterSheetFile);
        Spreadsheet.masterWorkbook.write(Spreadsheet.masterExcelOut);
        masterExcelOut.close();
    }

    /**
     * Opens the master spreadsheet again after it has been written to
     * Uses java.awt Desktop API
     * @throws IOException
     */
    public static void openMasterSpreadsheet() throws IOException {
        if (!Desktop.isDesktopSupported()) {
            System.out.println("Your computer does not support some features of this program. \n" +
                    "Please open " + MASTER_SHEET_NAME + " manually.");
        } else {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(Spreadsheet.masterSheetFile);
        }
    }

    // Spreadsheet-labeled row where the draft log starts in the spreadsheet
    public static final int DRAFT_LOG_START_ROW = 3;

    // Spreadsheet-labeled row where the fantasy teams start in the spreadsheet
    public static final int FANTASY_TEAM_START_ROW = 3;
}
