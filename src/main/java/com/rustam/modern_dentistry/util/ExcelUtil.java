package com.rustam.modern_dentistry.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class ExcelUtil {
    private static final int MAX_COLUMN_WIDTH = 3000; // Maksimum sütun genişliyi (px ekvivalentində)

    public static <T> ByteArrayInputStream dataToExcel(List<T> dataList, Class<T> clazz) throws IOException {
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("Data list cannot be null or empty");
        }

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Field[] fields = dataList.get(0).getClass().getDeclaredFields();
            Sheet sheet = workbook.createSheet(clazz.getSimpleName()); // Sheet adı entity-nin adına uyğun olsun

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle bodyStyle = createBodyStyle(workbook);

            createHeaderRow(sheet, clazz, headerStyle);
            fillDataRows(sheet, dataList, bodyStyle);
            adjustColumnWidths(sheet, fields.length);

            workbook.write(byteArrayOutputStream);
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        }
    }

    private static <T> void createHeaderRow(Sheet sheet, Class<T> clazz, CellStyle style) {
        Row headerRow = sheet.createRow(0);
        Field[] fields = clazz.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(formatHeader(fields[i].getName())); // Field adını başlıq kimi yaz
            cell.setCellStyle(style);
        }
    }

    private static <T> void fillDataRows(Sheet sheet, List<T> dataList, CellStyle bodyStyle) {
        Field[] fields = dataList.get(0).getClass().getDeclaredFields();

        for (int rowIndex = 0; rowIndex < dataList.size(); rowIndex++) {
            Row row = sheet.createRow(rowIndex + 1);
            T item = dataList.get(rowIndex);

            for (int colIndex = 0; colIndex < fields.length; colIndex++) {
                fields[colIndex].setAccessible(true);
                try {
                    Object value = fields[colIndex].get(item);
                    Cell cell = row.createCell(colIndex);
                    cell.setCellValue(value != null ? value.toString() : ""); // Dəyəri qoy
                    cell.setCellStyle(bodyStyle); // Stil tətbiq et
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error accessing field value", e);
                }
            }
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
//        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
//        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private static CellStyle createBodyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private static void adjustColumnWidths(Sheet sheet, int size) {
        for (int i = 0; i < size; i++) {
            sheet.autoSizeColumn(i); // Avtomatik ölçü alır
            int currentWidth = sheet.getColumnWidth(i); // Mövcud genişlik
            if (currentWidth < MAX_COLUMN_WIDTH) {
                sheet.setColumnWidth(i, MAX_COLUMN_WIDTH);
            }
        }
    }

    private static String formatHeader(String header) {
        return header.replaceAll("([A-Z])", " $1").trim().replaceFirst(".",
                Character.toUpperCase(header.charAt(0)) + "");
    }
}
