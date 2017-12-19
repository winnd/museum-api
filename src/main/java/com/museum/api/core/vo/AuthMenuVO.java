package com.museum.api.core.vo;

import com.museum.api.common.orm.model.Function;
import com.museum.api.common.orm.model.Menu;

import java.util.List;

public class AuthMenuVO {

    private Integer id;

    private String name;

    private Integer parentId;

    private List<AuthMenuVO> menus;

    private List<Function> functions;

    public AuthMenuVO(){};

    public AuthMenuVO(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.parentId = menu.getParentId();
    }

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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    public List<AuthMenuVO> getMenus() {
        return menus;
    }

    public void setMenus(List<AuthMenuVO> menus) {
        this.menus = menus;
    }

    public void convertMenu(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.parentId = menu.getParentId();
    }
}
