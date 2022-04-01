package com.meiming.crm.workbench.service;

import com.meiming.crm.workbench.domain.Clue;

import java.util.Map;

public interface ClueService {
    //保存线索
    int saveCreateClue(Clue clue);


    //根据id去查询线索详情
    Clue queryClueForDetailById(String id);

    //处理线索转换
    void saveConvert(Map<String, Object> map);


}
