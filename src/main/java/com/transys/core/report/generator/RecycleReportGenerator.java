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

public class RecycleReportGenerator extends ExcelReportGenerator {
	
	String aggregationHeader = null;
	
	@Override
	public ByteArrayOutputStream exportReport(String title, Map<String, String> headers, List<?> data) {
			
			XSSFWorkbook wb = new XSSFWorkbook();
			Map<String, CellStyle> styles = createStyles(wb);
			
			Sheet sheet = wb.createSheet();
			
			//turn off gridlines
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
	      sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$E$1"));
	      
	      // aggregation data - Date Range
	      int colIndex = 1;
	      
	      Row aggregationRow = sheet.createRow(1);
	      aggregationRow.setHeightInPoints(22);
	      Cell aggregationCell = aggregationRow.createCell(colIndex++);
	      aggregationCell.setCellStyle(styles.get("cell_b"));
	      aggregationCell.setCellValue(this.aggregationHeader);
	      
	      for (int i = 2; i <= 4; i++) {
	      	aggregationRow.createCell(i).setCellStyle(styles.get("cell_b"));
			}
			sheet.addMergedRegion(new CellRangeAddress(aggregationRow.getRowNum(), aggregationRow.getRowNum(), 1, 4));

	    //the header row: centered text in 48pt font
	      Row headerRow = sheet.createRow(2);
//	      headerRow.setHeightInPoints(12.75f);
	      
	      Iterator<String> headersSet = headers.keySet().iterator();
	      
	      
	      LinkedList<Integer> columnWidths = new LinkedList<>();
	      
	      colIndex = 1;
	      while(headersSet.hasNext()){
	      	Cell cell = headerRow.createCell(colIndex++);
	      	String headerName = headersSet.next();
	         cell.setCellValue(headerName);
	         cell.setCellStyle(styles.get("header"));
	         columnWidths.add(headerName.length());
	      }
	      
	//      sheet.createFreezePane(0, 1);
	      
	      Row row;
	      Cell cell;
	      int rownum = 3;
	      for (int i = 0; i < data.size(); i++, rownum++) {
	          row = sheet.createRow(rownum);
	          
	          Object everyDataObject = data.get(i);
	         	 
	         	 // invoke all fields using reflection on everyDataObject
	         	 
	         	 int columnIndex = 1;
	         	 Iterator<String> fieldSet = headers.values().iterator();
	            	 
	         	 while(fieldSet.hasNext()) {
	            	 try {
							Field field = everyDataObject.getClass().getDeclaredField(fieldSet.next());
							 field.setAccessible(true);
							 
							 Object value = field.get(everyDataObject);
							 
							 cell = row.createCell(columnIndex);
							 cell.setCellStyle(styles.get("cell_normal_centered"));
							 
							 if (value instanceof String) {
								 cell.setCellValue(value.toString());
								 if (columnWidths.get(columnIndex-1) < value.toString().length()) {
									 columnWidths.set(columnIndex-1, value.toString().length());
								 }
							 } else if (value instanceof BigDecimal) {
								 cell.setCellValue(((BigDecimal)value).doubleValue());
							 } else if (value instanceof Integer) {
								 cell.setCellValue(Integer.parseInt(value.toString()));
							 } else { 
								 String valueStr = "Unknown data type for " + field.getName();
								 cell.setCellValue(valueStr);
							 }
							 columnIndex++;
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
						} 
	             }
	      }
	
	      //set column widths, the width is measured in units of 1/256th of a character width
	
	      int index = 1;
	      for (Integer columnWidth : columnWidths) {
	      	sheet.setColumnWidth(index, 256*columnWidth);
	      	index++;
	      }
	
	//      sheet.setZoom(3, 4);
	
	      ByteArrayOutputStream out = new ByteArrayOutputStream();
	      try {
				wb.write(out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      return out;
		}
	
	public void setAggregationHeader(String data) {
		this.aggregationHeader = data;
	}
 
}
