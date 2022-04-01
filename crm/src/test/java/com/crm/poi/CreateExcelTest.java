package com.crm.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class CreateExcelTest {

    public static void main(String[] args) throws Exception{
        //1.创建一个工作薄 XSSFWorkbook
        HSSFWorkbook wb=new HSSFWorkbook();
        //2.创建表
        HSSFSheet sheet=wb.createSheet("学生列表");
        //3.创建第一行(行,列,从0开始)
        HSSFRow row=sheet.createRow(0);
        //4.创建单元格
        HSSFCell cell=row.createCell(0);
        //5.为单元格设置仠
        cell.setCellValue("学号");
        //第二单元格
        cell=row.createCell(1);
        cell.setCellValue("姓名");
        //第三单元格
        cell=row.createCell(2);
        cell.setCellValue("年龄");

        //样式对象
        HSSFCellStyle style=wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        long begin=System.currentTimeMillis();
        //写数据
        for(int i=1;i<=60000;i++){ //i=1,从第2行
            row=sheet.createRow(i);
            //第一列
            cell=row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue(i);

            cell=row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue("tom"+i);

            cell=row.createCell(2);
            cell.setCellStyle(style);
            cell.setCellValue(20+i);

        }
        long end=System.currentTimeMillis();

        //定义一个输出流
        OutputStream os=new FileOutputStream("d:\\student.xls");
        wb.write(os); //输出excel
        os.close();
        wb.close(); //wb null gc回收
        System.out.println(end-begin);
        System.out.println("-------------end-------------------");

    }
}
