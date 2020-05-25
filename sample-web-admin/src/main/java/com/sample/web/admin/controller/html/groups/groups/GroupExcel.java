package com.sample.web.admin.controller.html.groups.groups;

import static org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined.DARK_GREEN;
import static org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined.WHITE;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

import lombok.val;
import org.apache.poi.ss.usermodel.*;

import com.sample.domain.dto.group.Group;
import com.sample.web.base.view.ExcelView;

public class GroupExcel implements  ExcelView.Callback {

    @Override
    public void buildExcelWorkbook(Map<String, Object> model, Collection<?> data, Workbook workbook) {

        // シートを作成する
        Sheet sheet = workbook.createSheet("グループ");
        sheet.setDefaultColumnWidth(30);

        // フォント
        Font font = workbook.createFont();
        font.setFontName("メイリオ");
        font.setBold(true);
        font.setColor(WHITE.getIndex());

        // ヘッダーのスタイル
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(DARK_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(font);

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("グループ名");
        header.getCell(0).setCellStyle(style);
        header.createCell(1).setCellValue("略称");
        header.getCell(1).setCellStyle(style);
        header.createCell(2).setCellValue("メールアドレス");
        header.getCell(2).setCellStyle(style);

        // 明細
        @SuppressWarnings("unchecked")
        val groups = (List<Group>) data;

        int count = 1;
        for (Group group : groups) {
            Row groupRow = sheet.createRow(count++);
            groupRow.createCell(0).setCellValue(group.getFullName());
            groupRow.createCell(1).setCellValue(group.getShortName());
            groupRow.createCell(2).setCellValue(group.getEmail());
        }
    }
}
