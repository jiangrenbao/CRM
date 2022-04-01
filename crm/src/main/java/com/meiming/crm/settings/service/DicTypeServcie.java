package com.meiming.crm.settings.service;

import com.meiming.crm.settings.domain.DicType;

import java.util.List;

public interface DicTypeServcie {

    List<DicType> queryAllDicTypes(); //过度注释

    DicType queryDicTypeByCode(String coce);

    int saveCreateDicType(DicType dicType);

    int deleteDicTypeByCodes(String[] codes);

    int saveEditDicType(DicType dicType);
}
