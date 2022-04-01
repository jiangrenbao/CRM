package com.meiming.crm.workbench.mapper;

import com.meiming.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Wed Jun 03 17:19:22 CST 2020
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Wed Jun 03 17:19:22 CST 2020
     */
    int insert(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Wed Jun 03 17:19:22 CST 2020
     */
    int insertSelective(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Wed Jun 03 17:19:22 CST 2020
     */
    ClueRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Wed Jun 03 17:19:22 CST 2020
     */
    int updateByPrimaryKeySelective(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Wed Jun 03 17:19:22 CST 2020
     */
    int updateByPrimaryKey(ClueRemark record);

    /**
    * 根据clueId查询该线索下所有备注明细信息
    */
    List<ClueRemark> selectClueRemarkForDetailByClueId(String clueId);

    /**
    * @根据clueId查询该线索下所有的备注
    */
    List<ClueRemark> selectClueRemarkByClueId(String clueId);

    /**
    * 根据clueId删除该线索下所有的备注
    */
    int deleteClueRemarkByClueId(String clueId);
}