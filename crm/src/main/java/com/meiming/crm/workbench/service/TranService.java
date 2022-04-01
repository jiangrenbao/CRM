package com.meiming.crm.workbench.service;

import com.meiming.crm.workbench.domain.FunnelVO;
import com.meiming.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {

    int saveCreateTran(Map<String, Object> map);

    Tran queryTranForDetailById(String id);

    List<FunnelVO> queryCountOfTranGroupByStage();

}
