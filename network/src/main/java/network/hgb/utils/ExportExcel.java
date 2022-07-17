package network.hgb.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
/**
 * @author HUAWEI - Hinsane
 * @create 2022/6/4 15:24
 */

@Component
public class ExportExcel {
    public static boolean generateExcel(String sheetName, List<String> header, List<List<String>> body, int total, int price, ServletOutputStream out) throws Exception {
        try{
            // 新建excel报表
            HSSFWorkbook excel = new HSSFWorkbook();
            // 添加一个sheet
            HSSFSheet hssfSheet = excel.createSheet(sheetName);
            // 往excel表格创建一行，excel的行号是从0开始的
            // 设置表头
            HSSFRow firstRow = hssfSheet.createRow(0);
            //设置表头居中
            HSSFCellStyle hssfCellStyle = excel.createCellStyle();
            //居中样式
            hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            //修改字体样式...
            HSSFCellStyle hssfCellStyle1 = excel.createCellStyle();
            hssfCellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFFont font = excel.createFont();
            font.setFontName("宋体");
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setColor((short)10);
            hssfCellStyle1.setFont(font);
            //----header
            for (int columnNum = 0; columnNum < header.size(); columnNum++) {
                // 创建单元格
                HSSFCell hssfCell = firstRow.createCell(columnNum); //第1行创建列...
                // 设置单元格的值
                hssfCell.setCellValue(header.size() < columnNum ? "-" : header.get(columnNum));
                hssfCell.setCellStyle(hssfCellStyle1);//设置字体
            }

            // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
            for (int i = 0; i < 9; i++) {
                hssfSheet.setColumnWidth((short) i, (short) (28 * 200));
            }
            // 设置主体数据
            for (int rowNum = 0; rowNum < body.size(); rowNum++) {
                // 往excel表格创建一行，excel的行号是从0开始的
                HSSFRow hssfRow = hssfSheet.createRow(rowNum + 1);//现在从第二行开始写入数据
                List<String> data = body.get(rowNum);
                for (int columnNum = 0; columnNum < data.size(); columnNum++) {
                    // 创建单元格
                    HSSFCell hssfCell = hssfRow.createCell(columnNum);
                    // 设置单元格的值
                    hssfCell.setCellValue(data.size() < columnNum ? "-" : data.get(columnNum));
                    hssfCell.setCellStyle(hssfCellStyle);//设置居中显示
                }
            }
            HSSFRow LastfRow = hssfSheet.createRow(body.size() + 1);//现在从最后一行写入
            // 创建单元格
            HSSFCell hssfCell0 = LastfRow.createCell(0);
            HSSFCell hssfCell1 = LastfRow.createCell(1);
            //输入价格以及条数
            hssfCell0.setCellValue("订单总数为  " + total + " 单 ");
            hssfCell0.setCellStyle(hssfCellStyle1);//设置居中显示
            hssfCell1.setCellValue("订单总金额为  " + price  + " 元 ");
            hssfCell1.setCellStyle(hssfCellStyle1);//设置居中显示


            //将文件输出到客户端浏览器
            try {
                excel.write(out);
                out.flush();
                out.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("导出信息失败！");
        }
    }
}
