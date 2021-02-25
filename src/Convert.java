import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

public class Convert {

  public static void main(String args[]) throws IOException {

    FileInputStream file  = new FileInputStream(new File("./resources/language_numberzilla_game.xlsx"));
    XSSFWorkbook workbook = new XSSFWorkbook(file);

    //Get first/desired sheet from the workbook
    XSSFSheet sheet       = workbook.getSheetAt(0);

    //get key
    String key = getKey(sheet);

    //get idLanguage
    HashMap<Integer, String> hmIdLanguage = getIdLanguage(sheet);
    getContent(sheet, hmIdLanguage);

    StringBuilder finalString = new StringBuilder();
    finalString
        .append(key)
        .append("\n");

    for (String s : hmIdLanguage.values()) {
      finalString
          .append(s, 0, s.length()-2)
          .append("\n");
    }

    createFile();
    writeFile(finalString.toString());
  }

  private static HashMap<Integer, String> getIdLanguage(XSSFSheet sheet) {
    HashMap<Integer, String> hm = new HashMap<>();
    for (Row row : sheet) {
      Iterator<Cell> cellIterator = row.cellIterator();
      boolean isNext              = true;
      while (cellIterator.hasNext()) {
        Cell cell = cellIterator.next();
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
          hm.put(cell.getColumnIndex(), "");
          isNext = false;
        }
      }

      if (!isNext) break;
    }

    return hm;
  }

  private static String getKey(XSSFSheet sheet) {
    StringBuilder stringBuilder = new StringBuilder();
    for (Row row : sheet) {
      Iterator<Cell>  cellIterator  = row.cellIterator();
      boolean         isNext        = true;

      while (cellIterator.hasNext() && isNext) {
        Cell cell = cellIterator.next();

        switch (cell.getCellType()) {
          case Cell.CELL_TYPE_NUMERIC:
            isNext = false;
            stringBuilder
                .append(cell.getNumericCellValue())
                .append("__");
            break;
          case Cell.CELL_TYPE_STRING:
            if (cell.getStringCellValue().equals("vi") || cell.getStringCellValue().equals("en")) break;

            isNext = false;
            stringBuilder
                .append(cell.getStringCellValue())
                .append("__");
            break;
        }
      }
    }

    stringBuilder.deleteCharAt(stringBuilder.length()-1);
    stringBuilder.deleteCharAt(stringBuilder.length()-1);
    return stringBuilder.toString();
  }

  private static HashMap<Integer, String> getContent(XSSFSheet sheet, HashMap<Integer, String> hmIdLanguage) {
    HashMap<Integer, String> hmS = new HashMap<>();

    for (Row row : sheet) {
      for (Cell cell : row)
        if (cell.getCellType() == Cell.CELL_TYPE_STRING && hmIdLanguage.containsKey(cell.getColumnIndex())) {
          String s = hmIdLanguage.get(cell.getColumnIndex()) + cell.getStringCellValue() + "__";
          hmIdLanguage.put(cell.getColumnIndex(), s);
        }
    }

    return hmIdLanguage;
  }

  public static void createFile() {
    try {
      File myObj = new File("./resources/language.txt");
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
      } else {
        System.out.println("File already exists.");
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  public static void writeFile(String text) {
    try {
      FileWriter myWriter = new FileWriter("./resources/language.txt");
      myWriter.write(text);
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }
}
