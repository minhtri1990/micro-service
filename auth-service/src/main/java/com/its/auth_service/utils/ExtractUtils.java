package com.its.auth_service.utils;

import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Worksheet;
import com.google.gson.Gson;
import com.its.auth_service.model.dto.UserExtractDto;
import com.its.auth_service.model.dto.UserFullDto;
import com.its.module.utils.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.modelmapper.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class ExtractUtils {
    private static final String ACCOUNT_EXCEL_TEMPLATE = "accounts.xlsx";
    public static ByteArrayOutputStream accountExcel(List<UserFullDto> users) throws IOException, URISyntaxException {
        final int timeRowIdx = 1;
        final int templateRowIdx = 3;
        final int numCol = 7;
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
        for(int r=0; r<users.size(); r++) {
            Row newRow = sheet.createRow(currentRowIdx++);
            UserExtractDto user = new UserExtractDto(users.get(r));
            Map<String, Object> mapUser = gson.fromJson(gson.toJson(user), new TypeToken<Map<String, Object>>(){}.getType());
            for(int c=0; c<numCol; c++) {
                Cell newCell = newRow.createCell(c);
                newCell.setCellStyle(templateRow.getCell(c).getCellStyle());
                String templateValue = templateRow.getCell(c).getStringCellValue();
                if(INDEX_PATTERN.equals(templateValue)) {
                    newCell.setCellValue(String.valueOf(r+1));
                } else if(mapUser.containsKey(templateValue)){
                    Object value = mapUser.get(templateValue);
                    if(value instanceof String) newCell.setCellValue((String)value);
                    else if(value instanceof Integer) newCell.setCellValue((Integer)value);
                }
            }
        }
        sheet.shiftRows(templateRowIdx+1,templateRowIdx + users.size()+1,-1);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return bos;
    }

    public static ByteArrayOutputStream accountPdf(List<UserFullDto> userFullDtos) throws Exception {
        byte[] bytes = accountExcel(userFullDtos).toByteArray();
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
