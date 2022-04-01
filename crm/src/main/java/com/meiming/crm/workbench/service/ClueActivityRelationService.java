package com.meiming.crm.workbench.service;

import com.meiming.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {

    //批量插入线索和市场活动关联对象
    int saveCeateClueActivityRelationByList(List<ClueActivityRelation> relationList);

    int deleteClueActivityRelationByCLueIdActivityId(ClueActivityRelation relation);


}
