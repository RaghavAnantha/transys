package com.transys.core.report.generator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.transys.model.vo.MonthlyIntakeReportVO;
import com.transys.model.vo.RollOffBoxesPerYardVO;

public class TransferStationIntakeReportGenerator extends ExcelReportGenerator {

	@Override
	public ByteArrayOutputStream exportReport(String title, Map<String, String> headers, List<?> data) {

		return generateSimpleTable(title, headers, data);
	}

	private ByteArrayOutputStream generateSimpleTable(String title, Map<String, String> headers, List<?> genericData) {
		
		List<MonthlyIntakeReportVO> data = (List<MonthlyIntakeReportVO>)genericData;
		
		XSSFWorkbook wb = new XSSFWorkbook();
		Map<String, CellStyle> styles = createStyles(wb);

		Sheet sheet = wb.createSheet();

		// turn off gridlines
		sheet.setDisplayGridlines(false);
		sheet.setPrintGridlines(false);
		sheet.setFitToPage(true);
		sheet.setHorizontallyCenter(true);
		PrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(true);

		Row titleRow = sheet.createRow(0);
		titleRow.setHeightInPoints(45);
		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue(title);
		titleCell.setCellStyle(styles.get("title"));
		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$L$1"));

		// the header row: centered text in 48pt font
		Row headerRow = sheet.createRow(1);
		Row headerRow1 = sheet.createRow(2);
		headerRow.setHeightInPoints(12.75f);

		Iterator<String> headersSet = headers.keySet().iterator();

		int columnIndex = 1;
		LinkedList<Integer> columnWidths = new LinkedList<>();
		
		int totalMergedColumns = 0;

		while (headersSet.hasNext()) {
			System.out.println("Creating columnIndex = " + columnIndex);
			Cell cell = headerRow.createCell(columnIndex);
			String headerName = headersSet.next();
			cell.setCellValue(headerName);
			cell.setCellStyle(styles.get("header"));
			
			if (columnIndex == 3) {
				int toMergeCount = 0;
				if (data != null && data.size() > 0) {
					List<RollOffBoxesPerYardVO> rollOffBoxesPerYardList = data.get(0).getRollOffBoxesPerYard();
					toMergeCount = rollOffBoxesPerYardList.size();
					int rollOffIndex = 0;
					for (int mergeCol = columnIndex; mergeCol < columnIndex+toMergeCount; mergeCol++) {
						Cell cell1 = headerRow1.createCell(mergeCol);
						cell1.setCellStyle(styles.get("header"));
						String valueForCell = rollOffBoxesPerYardList.get(rollOffIndex++).getYardSize();
						cell1.setCellValue(valueForCell);
						columnWidths.add(256 * valueForCell.length());
					}
					
				}
				sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow.getRowNum(), columnIndex, columnIndex + (toMergeCount-1)));
				columnIndex += toMergeCount;
				totalMergedColumns += toMergeCount;
			} else {
				sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow1.getRowNum(), columnIndex, columnIndex));
				Cell cell1 = headerRow1.createCell(columnIndex);
				cell1.setCellStyle(styles.get("header"));
				columnWidths.add(256 * headerName.length());
				columnIndex++;
			}
		}
		
		// add Waste total headers
		Cell additionalCell = headerRow.createCell(columnIndex);
		String headerName = "Total Waste Cubic Yards";
		additionalCell.setCellValue(headerName);
		additionalCell.setCellStyle(styles.get("header"));
		sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow1.getRowNum(), columnIndex, columnIndex));
		Cell cell1 = headerRow1.createCell(columnIndex);
		cell1.setCellStyle(styles.get("header"));
		columnWidths.add(256 * headerName.length());
		columnIndex++;
		
		additionalCell = headerRow.createCell(columnIndex);
		headerName = "Total Waste Tonnage";
		additionalCell.setCellValue(headerName);
		additionalCell.setCellStyle(styles.get("header"));
		sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow1.getRowNum(), columnIndex, columnIndex));
		cell1 = headerRow1.createCell(columnIndex);
		cell1.setCellStyle(styles.get("header"));
		columnWidths.add(256 * headerName.length());
		columnIndex++;
		
		// sheet.createFreezePane(0, 1);

		Row row;
		Cell cell;
		int rownum = 3;
		int rowOffset = 2;
		for (int i = 0; i < data.size(); i++, rownum++) {
			row = sheet.createRow(rownum);
			row.setHeightInPoints(12.75f);

			for (MonthlyIntakeReportVO everyDataObject : data) {

				columnIndex = 1;
				Iterator<String> fieldSet = headers.values().iterator();

				while (fieldSet.hasNext()) {
					try {
						
						String fieldName = fieldSet.next();
						
						if (fieldName.equals("rollOffBoxesPerYard")) {
							List<RollOffBoxesPerYardVO> rollOffBoxesPerYardList = everyDataObject.getRollOffBoxesPerYard();
							for (RollOffBoxesPerYardVO boxForEachYard : rollOffBoxesPerYardList) {
								cell = row.createCell(columnIndex);
								cell.setCellStyle(styles.get("cell_normal_centered"));
								cell.setCellValue(boxForEachYard.getNumBoxes());
								columnIndex++;
							}
						} else {
						
							Field field = everyDataObject.getClass().getDeclaredField(fieldName);
							field.setAccessible(true);
							
							System.out.println("Creating index = " + columnIndex + " for value = " + fieldName);
							cell = row.createCell(columnIndex);
							cell.setCellStyle(styles.get("cell_normal_centered"));
							
							Object value = field.get(everyDataObject);
	
							if (value instanceof String) {
								cell.setCellValue(value.toString());
								if (columnWidths.get(columnIndex-rowOffset) < value.toString().length()) {
									columnWidths.set(columnIndex-rowOffset, value.toString().length());
								}
							} else if (value instanceof BigDecimal) {
								cell.setCellValue(((BigDecimal) value).doubleValue());
							} else if (value instanceof Integer) {
								cell.setCellValue(Integer.parseInt(value.toString()));
							} else if (value instanceof Date) {
								cell.setCellValue(fmt.parse("01-Jan"));
							} else {
								String valueStr = "Unknown data type for " + field.getName();
								cell.setCellValue(valueStr);
							}
							columnIndex++;
						}
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				// add waste total data
				cell = row.createCell(columnIndex);
				
				cell.setCellStyle(styles.get("formula"));
				String ref = ((char)('A' + cell.getColumnIndex()-1)) + "" + (cell.getRowIndex()+1) + ","  + (char)('A' + cell.getColumnIndex()-4) + (cell.getRowIndex()+1) ;
            System.out.println("Formula = " + ref);
				cell.setCellFormula("SUM(" + ref + ")");
				columnIndex++;
				
				cell = row.createCell(columnIndex);
				
				cell.setCellStyle(styles.get("formula"));
				ref = ((char)('A' + cell.getColumnIndex()-3)) + "" + (cell.getRowIndex()+1) + ","  + (char)('A' + cell.getColumnIndex()-4) + (cell.getRowIndex()+1) ;
            System.out.println("Formula = " + ref);
				cell.setCellFormula("SUM(" + ref + ")");
				columnIndex++;

			}
		}

		// set column widths, the width is measured in units of 1/256th of a
		// character width

		int index = 1;
		for (Integer columnWidth : columnWidths) {
			sheet.setColumnWidth(index, columnWidth);
			index++;
		}

		// sheet.setZoom(3, 4);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			wb.write(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;
	}
}
