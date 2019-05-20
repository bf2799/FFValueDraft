public class LeagueSettings {

    // Number of starting QBs on fantasy roster
    public static int numQB() {
        return (int) SpreadsheetSettings.leagueSettingsSheet.getRow(3).getCell(5).getNumericCellValue();
    }


    // Number of starting RBs on fantasy roster
    public static int numRB() {
        return (int) SpreadsheetSettings.leagueSettingsSheet.getRow(4).getCell(5).getNumericCellValue();
    }

    // Number of starting WRs on fantasy roster
    public static int numWR() {
        return (int) SpreadsheetSettings.leagueSettingsSheet.getRow(5).getCell(5).getNumericCellValue();
    }

    // Number of starting TEs on fantasy roster
    public static int numTE() {
        return (int) SpreadsheetSettings.leagueSettingsSheet.getRow(6).getCell(5).getNumericCellValue();
    }

    // Number of starting RB/WR FLEXs on fantasy roster
    public static int numFLEX() {
        return (int) SpreadsheetSettings.leagueSettingsSheet.getRow(7).getCell(5).getNumericCellValue();
    }

    // Number of starting DSTs on fantasy roster
    public static int numDST() {
        return (int) SpreadsheetSettings.leagueSettingsSheet.getRow(8).getCell(5).getNumericCellValue();
    }

    // Number of starting Ks on fantasy roster
    public static int numK() {
        return (int) SpreadsheetSettings.leagueSettingsSheet.getRow(9).getCell(5).getNumericCellValue();
    }

    // Number of Bench slots on fantasy roster
    public static int numBench() {
        return (int) SpreadsheetSettings.leagueSettingsSheet.getRow(10).getCell(5).getNumericCellValue();
    }

    // Number of fantasy teams competing in league
    public static int numTeams() {
        return (int) SpreadsheetSettings.leagueSettingsSheet.getRow(1).getCell(8).getNumericCellValue();
    }

    // Draft position of application user
    public static int userDraftPosition() {
        return 0;
    }

    // Number of players on fantasy roster
    public static int rosterNum = numQB() + numRB() + numWR() + numTE() + numFLEX() + numDST() + numK() + numBench();
}