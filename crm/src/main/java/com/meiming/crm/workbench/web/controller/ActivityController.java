package com.meiming.crm.workbench.web.controller;

import com.meiming.crm.commons.contants.Contants;
import com.meiming.crm.commons.domain.ReturnObject;
import com.meiming.crm.commons.utils.DateUtils;
import com.meiming.crm.commons.utils.UUIDUtils;
import com.meiming.crm.settings.domain.User;
import com.meiming.crm.settings.service.UserService;
import com.meiming.crm.workbench.domain.Activity;
import com.meiming.crm.workbench.service.ActivityService;
import javafx.scene.control.TableColumn;
import jdk.nashorn.internal.ir.ReturnNode;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@Controller
public class ActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    //跳转市场活动首页
    @RequestMapping("/workbench/activity/index.do")
    public String index(Model model) {
        List<User> userList = userService.queryAllUsers();
        model.addAttribute("userList", userList);
        return "workbench/activity/index";
    }

    //添加
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    public @ResponseBody
    Object saveCreateActivity(Activity activity, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //封装参数
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateBy(user.getId());
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));

        ReturnObject returnObject = new ReturnObject();

        int ret = activityService.saveCreateActivity(activity);

        if (ret > 0) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("保存失败");
        }


        return returnObject;

    }

    //查询分页和条件
    @RequestMapping("/workbench/activity/queryActivityForPageByCondition.do")
    public @ResponseBody
    Object queryActivityForPageByCondition(int pageNo, int pageSize, String name,
                                           String owner, String startDate, String endDate) {
        Map<String, Object> map = new HashMap<>();
        map.put("beginNo", (pageNo - 1) * pageSize); //pageNo第一页 limit 0,10, limit 10,10
        map.put("pageSize", pageSize);
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        List<Activity> activityList = activityService.queryActivityForPagesByCondition(map);
        Long totalRow = activityService.queryCountOfActivityByCondition(map);

        //封装返回对象
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("activityList", activityList); //分页的市场活动对象集合
        retMap.put("totalRow", totalRow); //总记录数

        return retMap;
    }

    //跳到市场活动的编辑页面
    @RequestMapping("/workbench/activity/editActivity.do")
    public @ResponseBody
    Object editActivity(String id) {
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }

    //跳到市场活动的编辑页面
    @RequestMapping("/workbench/activity/saveEditActivity.do")
    public @ResponseBody
    Object saveEditActivity(Activity activity, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activity.setEditBy(user.getId());
        activity.setEditTime(DateUtils.formatDateTime(new Date()));
        ReturnObject returnObject = new ReturnObject();

        int ret = activityService.saveEditActivity(activity);

        if (ret > 0) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("更新失败");
        }


        return returnObject;

    }


    //跳到市场活动的编辑页面
    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    public @ResponseBody
    Object deleteActivityByIds(String[] id) {

        ReturnObject returnObject = new ReturnObject();

        int ret = activityService.deleteActivityByIds(id);

        if (ret > 0) {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } else {
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("删除失败");
        }


        return returnObject;

    }

    //批量导出
    @RequestMapping("/workbench/activity/exportAllActivity.do")
    public void exportAllActivity(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //将数据库市场活动全部取出
        List<Activity> activityList = activityService.queryAllActivityForDetail();

        //创建excel
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");

        cell = row.createCell(1);
        cell.setCellValue("所有者");


        cell = row.createCell(2);
        cell.setCellValue("名称");

        cell = row.createCell(3);
        cell.setCellValue("成本");

        //格式对象
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        if (activityList != null) {
            //对集合迭代取出每一个市场活动对象
            Activity activity = null;
            for (int i = 0; i < activityList.size(); i++) {
                activity = activityList.get(i);

                row = sheet.createRow(i + 1); //+1,是为从第2行开始
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());

                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());

                cell = row.createCell(2);
                cell.setCellValue(activity.getName());

                cell = row.createCell(3);
                cell.setCellValue(activity.getCost());
            }
        }

        //服务器返回对象给客户端
        //设置响应类型为流类型
        response.setContentType("application/octet-stream;charset=UTF-8");

        String fileName = URLEncoder.encode("市场活动", "UTF-8");
        System.out.println(fileName);


        response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");

        OutputStream os = response.getOutputStream();
        wb.write(os);
        wb.close();
        os.close();

    }


    @RequestMapping("/workbench/activity/exportActivitySelective.do")
    public void exportActivitySelective(String[] id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //调用service层方法，查询市场活动
        List<Activity> activityList = activityService.queryActivityForDetailByIds(id);
        //根据查询结果，生成excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建者");
        cell = row.createCell(8);
        cell.setCellValue("创建时间");
        cell = row.createCell(9);
        cell.setCellValue("修改者");
        cell = row.createCell(10);
        cell.setCellValue("修改时间");

        //创建HSSFCellStyle对象，对应样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        //5.遍历activityList，显示数据行
        if (activityList != null) {
            Activity activity = null;
            for (int i = 0; i < activityList.size(); i++) {
                activity = activityList.get(i);//获取每一条数据

                row = sheet.createRow(i + 1);//创建一行

                cell = row.createCell(0);//column：列的编号,从0开始，0表示第一列，1表示第二列，....
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditBy());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditTime());
            }
        }

        //返回响应信息
        //1.设置响应类型
        response.setContentType("application/octet-stream;charset=UTF-8");
        //根据HTTP协议的规定，浏览器每次向服务器发送请求，都会把浏览器信息以请求头的形式发送到服务器
        String browser = request.getHeader("User-Agent");

        //不同的浏览器接收响应头采用的编码格式不一样：
        //IE采用 urlencoded
        ////火狐采用 ISO8859-1
        String fileName = URLEncoder.encode("市场活动列表", "UTF-8");
        if (browser.contains("firefox")) {
            //火狐采用 ISO8859-1
            fileName = new String("市场活动列表".getBytes("UTF-8"), "ISO8859-1");
        }

        //默认情况下，浏览器接收到响应信息之后，直接在显示窗口中打开；
        //可以设置响应头信息，使浏览器接收到响应信息之后，在下载窗口打开
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
        //2.获取输出流
        OutputStream os2 = response.getOutputStream();
        //3.把excel文件通过os2输出到客户端
        wb.write(os2);
        //4.关闭资源
        os2.flush();
        wb.close();
    }


    @RequestMapping("/workbench/activity/fileUpload.do")
    public @ResponseBody
    Object fileUpload(String username, MultipartFile myfile) throws Exception {
        System.out.println("username=" + username);
        String fileName = myfile.getName();
        String orginalfileName = myfile.getOriginalFilename();
        File file = new File("d:\\testDir", orginalfileName);

        myfile.transferTo(file);

        return null;
    }


    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(String username, MultipartFile activityFile, HttpSession session) throws Exception {
        System.out.println("ok");
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        Map<String, Object> retMap = new HashMap<>();
        //将excel文件里的行全出来,每一行做为市场活动对象,放市场活动对象的集合
        List<Activity> activityList = new ArrayList<>();
        //将上传文件excel转is
        InputStream is = activityFile.getInputStream();
        //获取工作簿
        HSSFWorkbook wb = new HSSFWorkbook(is);
        //取第一张表
        HSSFSheet sheet = wb.getSheetAt(0);
        //定义行和列
        HSSFRow row = null;
        HSSFCell cell = null;
        Activity activity = null;
        // 最后一行的行号(从0开始)
        int rownum = sheet.getLastRowNum();
        //外层 行
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i); //跳过标题
            activity = new Activity();
            activity.setId(UUIDUtils.getUUID());
            activity.setOwner(user.getId());
            activity.setCreateBy(user.getId());
            activity.setCreateTime(DateUtils.formatDateTime(new Date()));
            // 得到最后一列的序号(从0开始)
            row.getLastCellNum();
            //内层 列
            for (int j = 0; j < row.getLastCellNum(); j++) {
                cell = row.getCell(j);
                String cellValue = getCellValue(cell);
                if (j == 0) { //name
                    activity.setName(cellValue);
                }
                if (j == 1) { //starttime
                    activity.setStartDate(cellValue);
                }
                if (j == 2) { //endtime
                    activity.setEndDate(cellValue);
                }
                if (j == 3) { //cost
                    activity.setCost(cellValue);
                }
                if (j == 4) { //desc
                    activity.setDescription(cellValue);
                }


            }

            activityList.add(activity);

        }


        int ret = activityService.saveCreateActivityByList(activityList);
        retMap.put("code", Contants.RETURN_OBJECT_CODE_SUCCESS);
        retMap.put("count", ret);

        return retMap;
    }

    //将单元格中的类型做相应转换
    public static String getCellValue(HSSFCell cell) {

        String ret = "";

        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                ret = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                ret = cell.getBooleanCellValue() + "";
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                ret = cell.getNumericCellValue() + "";
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                ret = cell.getCellFormula() + "";
                break;
            default:
                ret = "";
        }

        return ret;
    }


}
