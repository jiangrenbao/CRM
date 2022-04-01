package com.crm.poi;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;

public class ParseExcelTest {

    public static void main(String[] args) throws Exception{
        //模拟将excel上传到服务器
        InputStream is=new FileInputStream("d:\\student.xls");
        //获取工作簿
        HSSFWorkbook wb=new HSSFWorkbook(is);
        //取第一张表
        HSSFSheet sheet=wb.getSheetAt(0);

        //定义行和列
        HSSFRow row=null;
        HSSFCell cell=null;

        //外层对行进行循环
        for(int i=0;i<=sheet.getLastRowNum();i++){
            row=sheet.getRow(i);

            //内层对列循环
            for(int j=0;j<row.getLastCellNum();j++){
                cell=row.getCell(j); //取到对应的单元格
                System.out.print(getCellValue(cell)+" ");
            }
            System.out.println();
        }


    }

    //将单元格中的类型做相应转换
    public static String getCellValue(HSSFCell cell){

        String ret="";

        switch (cell.getCellType()){
            case HSSFCell.CELL_TYPE_STRING:
                ret=cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                ret=cell.getBooleanCellValue()+"";
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                ret=cell.getNumericCellValue()+"";
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                ret=cell.getCellFormula()+"";
                break;
            default:
                ret="";
        }

        return ret;
    }



}
