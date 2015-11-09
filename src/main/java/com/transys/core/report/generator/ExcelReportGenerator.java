package com.transys.core.report.generator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.transys.model.vo.MonthlyIntakeReportVO;

public class ExcelReportGenerator {
	
	public static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	String mergeCellRange = "$A$1:$E$1";
	
	public ExcelReportGenerator() {
		
	}
	
	
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
      sheet.addMergedRegion(CellRangeAddress.valueOf(this.mergeCellRange));
      
    //the header row: centered text in 48pt font
      Row headerRow = sheet.createRow(1);
//      headerRow.setHeightInPoints(12.75f);
      
      Iterator<String> headersSet = headers.keySet().iterator();
      
      int i = 1;
      LinkedList<Integer> columnWidths = new LinkedList<>();
      
      while(headersSet.hasNext()){
      	Cell cell = headerRow.createCell(i++);
      	String headerName = headersSet.next();
         cell.setCellValue(headerName);
         cell.setCellStyle(styles.get("header"));
         columnWidths.add(headerName.length());
      }
      
//      sheet.createFreezePane(0, 1);
      
      Row row;
      Cell cell;
      int rownum = 2;
      for (i = 0; i < data.size(); i++, rownum++) {
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
							 //String valueStr = "Unknown data type for " + field.getName();
							 String valueStr = (value == null ? "" : value.toString());
							 cell.setCellValue(valueStr);
							 if (columnWidths.get(columnIndex-1) < valueStr.length()) {
								 columnWidths.set(columnIndex-1, valueStr.length());
							 }
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

	/**
    * create a library of cell styles
    */
   public static Map<String, CellStyle> createStyles(Workbook wb) {
   	Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
      DataFormat df = wb.createDataFormat();
      
      CellStyle style;
      
      Font titleFont = wb.createFont();
      titleFont.setFontHeightInPoints((short)18);
      titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
      style = wb.createCellStyle();
      style.setAlignment(CellStyle.ALIGN_CENTER);
      style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
      style.setFont(titleFont);
      styles.put("title", style);
      
      Font headerFont = wb.createFont();
      headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
      style = createBorderedStyle(wb);
      style.setAlignment(CellStyle.ALIGN_CENTER);
      style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      style.setFillPattern(CellStyle.SOLID_FOREGROUND);
      style.setFont(headerFont);
      styles.put("header", style);
      
      Font font1 = wb.createFont();
      font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
      font1.setFontHeightInPoints((short)14);
      style = createBorderedStyle(wb);
      style.setAlignment(CellStyle.ALIGN_LEFT);
      style.setFont(font1);
      styles.put("cell_b", style);

      style = createBorderedStyle(wb);
      style.setAlignment(CellStyle.ALIGN_CENTER);
      style.setFont(font1);
      styles.put("cell_b_centered", style);

      style = createBorderedStyle(wb);
      style.setAlignment(CellStyle.ALIGN_RIGHT);
      style.setFont(font1);
      style.setDataFormat(df.getFormat("dd-mmm"));
      styles.put("cell_b_date", style);
      
      style = createBorderedStyle(wb);
      style.setAlignment(CellStyle.ALIGN_LEFT);
      style.setWrapText(true);
      styles.put("cell_normal", style);

      style = createBorderedStyle(wb);
      style.setAlignment(CellStyle.ALIGN_CENTER);
      style.setWrapText(true);
      styles.put("cell_normal_centered", style);

      style = createBorderedStyle(wb);
      style.setAlignment(CellStyle.ALIGN_RIGHT);
      style.setWrapText(true);
      style.setDataFormat(df.getFormat("d-mmm"));
      styles.put("cell_normal_date", style);

      style = createBorderedStyle(wb);
      style.setAlignment(CellStyle.ALIGN_LEFT);
      style.setIndention((short)1);
      style.setWrapText(true);
      styles.put("cell_indented", style);
      
      style = wb.createCellStyle();
      style = createBorderedStyle(wb);
      style.setAlignment(CellStyle.ALIGN_CENTER);
      style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//      style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//      style.setFillPattern(CellStyle.SOLID_FOREGROUND);
      style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
      styles.put("formula", style);
      
      return styles;
   }
   
   private static CellStyle createBorderedStyle(Workbook wb){
      CellStyle style = wb.createCellStyle();
      style.setBorderRight(CellStyle.BORDER_THIN);
      style.setRightBorderColor(IndexedColors.BLACK.getIndex());
      style.setBorderBottom(CellStyle.BORDER_THIN);
      style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
      style.setBorderLeft(CellStyle.BORDER_THIN);
      style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
      style.setBorderTop(CellStyle.BORDER_THIN);
      style.setTopBorderColor(IndexedColors.BLACK.getIndex());
      return style;
  }
   
   public void setTitleMergeCellRange(String mergeRange) {
   	this.mergeCellRange = mergeRange;
   }
}
