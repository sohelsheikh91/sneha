package com.accenture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * This is a utility class that writes data in an excel file. It has a straight
 * forward approach to its intent. It contains static methods that take data to
 * be written as input parameters along with formatting options.
 * 
 * @author sneha duseja
 * @version 1.0
 */
public class WriteToExcel {
	/**
	 * @param sheetName
	 *            Name of the sheet within the workbook
	 * @param headerNames
	 *            Array List of header column names
	 * @param data
	 *            One dimensional Array List of the data to be written into the
	 *            cells of the table
	 * @param hyperlinkCols
	 *            Array List containing the numbers of the columns that will
	 *            have a hyperLink functionality
	 * @param destinationPath
	 *            The path to the folder where the output file will be created
	 * @throws IOException
	 *             in scenarios such as file already being used by another
	 *             program or when trying to create file in a folder where the
	 *             user doesn't have required permissions
	 */
	public static void writeXLSFile(String sheetName, ArrayList<String> headerNames, ArrayList<Object> data,
			ArrayList<Integer> hyperlinkCols, String destinationPath) throws IOException {
		// Location of the output file
		String excelFileName = destinationPath + "/Validation_Check_Output.xls";

		int sheetNumber = -1;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;

		// Check if file exists and create workbook accordingly
		if (new File(excelFileName).exists()) {
			// Update existing file
			FileInputStream inputStream = new FileInputStream(new File(excelFileName));
			wb = new HSSFWorkbook(inputStream);
		} else {
			// Create new file
			wb = new HSSFWorkbook();
		}

		HSSFPalette palette = wb.getCustomPalette();

		// used to create Hyperlinks
		CreationHelper createHelper = wb.getCreationHelper();

		// Check if sheet exists
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			if (sheetName.equals(wb.getSheetName(i))) {
				sheetNumber = i;
				break;
			}
		}

		if (sheetNumber != -1) // sheet already exists
			sheet = wb.getSheetAt(sheetNumber);
		else
			sheet = wb.createSheet(sheetName); // create new sheet

		// Create Header cell Style
		CellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		headerStyle.setBorderBottom((short) 2);
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerStyle.setFont(font);

		HSSFRow row = sheet.createRow(0);
		row.setHeightInPoints(20);
		HSSFCell cell;

		// Header cell creation
		for (int i = 0; i < headerNames.size(); i++) {
			cell = row.createCell(i);
			cell.setCellValue(headerNames.get(i).toString());
			cell.setCellStyle(headerStyle);
		}

		// Data Population
		int counter = 0;
		int r;
		CellStyle oddRowStyle = wb.createCellStyle();
		oddRowStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		oddRowStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());

		CellStyle evenRowStyle = wb.createCellStyle();
		evenRowStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		evenRowStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());

		for (r = 1; r < (data.size() / headerNames.size()) + 1; r++) {
			row.setHeightInPoints(15);
			row = sheet.createRow(r);

			for (int j = 0; j < headerNames.size(); j++) {
				cell = row.createCell(j);

				// Add Hyperlink functionality for the resource file column
				if (hyperlinkCols.contains(j)) {
					Hyperlink resFileLink = createHelper.createHyperlink(Hyperlink.LINK_FILE);
					resFileLink.setAddress(data.get(counter).toString());
					cell.setHyperlink(resFileLink);
				}
				if (r % 2 == 0) {
					cell.setCellStyle(evenRowStyle);
				} else {
					cell.setCellStyle(oddRowStyle);

				}
				cell.setCellValue(data.get(counter).toString());

				counter++;
			}
		}

		// Message row creation if hyperlink columns exist
		if (hyperlinkCols.size() != 0) {
			row = sheet.createRow(r + 2);
			cell = row.createCell(0);
			cell.setCellValue("NOTE: Click on Resource File name to open the file");
			CellStyle message = wb.createCellStyle();
			Font noteFont = wb.createFont();
			noteFont.setColor(IndexedColors.RED.getIndex());
			noteFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
			message.setFont(noteFont);
			cell.setCellStyle(message);
		}

		// Resize columns to fit data
		for (int i = 0; i < r; i++) {
			sheet.autoSizeColumn(i);
		}

		palette.setColorAtIndex(HSSFColor.AQUA.index, (byte) 23, (byte) 178, (byte) 85);
		palette.setColorAtIndex(HSSFColor.LIGHT_CORNFLOWER_BLUE.index, (byte) 163, (byte) 255, (byte) 211);
		palette.setColorAtIndex(HSSFColor.LIGHT_TURQUOISE.index, (byte) 255, (byte) 255, (byte) 255);
		wb.setSelectedTab(0);
		FileOutputStream fileOut = new FileOutputStream(new File(excelFileName));
		// write this workbook to an Outputstream.
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
	}
}
