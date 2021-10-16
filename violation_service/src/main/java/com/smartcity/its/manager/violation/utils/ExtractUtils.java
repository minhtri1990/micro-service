package com.smartcity.its.manager.violation.utils;

import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Worksheet;
import com.google.gson.Gson;
import com.its.module.utils.StringUtils;
import com.smartcity.its.manager.violation.model.dto.ViolationDto;
import com.smartcity.its.manager.violation.model.dto.ViolationExtractDto;
import org.apache.poi.ss.usermodel.*;
import org.modelmapper.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ExtractUtils {
    private static final String ACCOUNT_EXCEL_TEMPLATE = "violations.xlsx";
    public static ByteArrayOutputStream violationExcel(List<ViolationDto> violations) throws IOException {
        final int timeRowIdx = 1;
        final int templateRowIdx = 3;
        final int numCol = 6;
        final String INDEX_PATTERN = "index";
        InputStream is = ExtractUtils.class.getClassLoader().getResourceAsStream(ACCOUNT_EXCEL_TEMPLATE);
        Workbook workbook = WorkbookFactory.create(is);
        is.close();

        Sheet sheet = workbook.getSheetAt(0);
        //Time
        Cell timeCell = sheet.getRow(timeRowIdx).getCell(0);
        String timeTemplate = timeCell.getStringCellValue();
        timeCell.setCellValue(timeTemplate.replace("time", StringUtils.convertTimestamp(System.currentTimeMillis())));

        final Gson gson = new Gson();
        final Row templateRow = sheet.getRow(templateRowIdx);
        int currentRowIdx = templateRowIdx+1;
        for(int r=0; r<violations.size(); r++) {
            Row newRow = sheet.createRow(currentRowIdx++);
            ViolationExtractDto violation = new ViolationExtractDto(violations.get(r));
            Map<String, Object> mapViolation = gson.fromJson(gson.toJson(violation), new TypeToken<Map<String, Object>>(){}.getType());
            for(int c=0; c<numCol; c++) {
                Cell newCell = newRow.createCell(c);
                newCell.setCellStyle(templateRow.getCell(c).getCellStyle());
                String templateValue = templateRow.getCell(c).getStringCellValue();
                if(INDEX_PATTERN.equals(templateValue)) {
                    newCell.setCellValue(String.valueOf(r+1));
                } else if(mapViolation.containsKey(templateValue)){
                    Object value = mapViolation.get(templateValue);
                    if(value instanceof String) newCell.setCellValue((String)value);
                    else if(value instanceof Integer) newCell.setCellValue((Integer)value);
                }
            }
        }
        sheet.shiftRows(templateRowIdx+1,templateRowIdx + violations.size()+1,-1);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return bos;
    }

    public static ByteArrayOutputStream violationPdf(List<ViolationDto> violations) throws Exception {
        byte[] bytes = violationExcel(violations).toByteArray();
        com.aspose.cells.Workbook workbook = new com.aspose.cells.Workbook(new ByteArrayInputStream(bytes));
        PdfSaveOptions options = new PdfSaveOptions();
        options.setAllColumnsInOnePagePerSheet(true);
        Worksheet worksheet = workbook.getWorksheets().get(0);
        worksheet.autoFitRows();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.save(bos, options);
        return bos;
    }
}
