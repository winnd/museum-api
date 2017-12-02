package com.museum.api.core.vo;

import com.museum.api.common.vo.BaseSearchVO;
import com.museum.api.common.vo.PageModel;

public class RelicSearchVO extends BaseSearchVO{

    private Integer relicId;

    private Integer relicType;

    private String name;

    public Integer getRelicId() {
        return relicId;
    }

    public void setRelicId(Integer relicId) {
        this.relicId = relicId;
    }

    public Integer getRelicType() {
        return relicType;
    }

    public void setRelicType(Integer relicType) {
        this.relicType = relicType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
