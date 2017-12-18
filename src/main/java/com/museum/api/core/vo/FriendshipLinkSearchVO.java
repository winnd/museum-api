package com.museum.api.core.vo;

import com.museum.api.common.vo.BaseSearchVO;

public class FriendshipLinkSearchVO extends BaseSearchVO {

    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
