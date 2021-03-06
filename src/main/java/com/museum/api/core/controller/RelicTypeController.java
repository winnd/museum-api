package com.museum.api.core.controller;

import javax.annotation.Resource;

import com.museum.api.common.annotation.Authorization;
import com.museum.api.common.annotation.CurrentUser;
import com.museum.api.common.orm.model.RelicType;
import com.museum.api.common.orm.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.museum.api.common.constant.Constants;
import com.museum.api.common.controller.BaseController;
import com.museum.api.common.exception.InheaterSOAException;
import com.museum.api.common.vo.BaseModel;
import com.museum.api.core.service.RelicTypeService;

import java.util.List;


@Controller
@RequestMapping("relic-type")
public class RelicTypeController extends BaseController{
	
	@Resource
	RelicTypeService relicTypeService;

    /**
     * 增加一个分类
     */
	@Authorization(authCode = 4)
	@RequestMapping(value = "/management", method = RequestMethod.POST)
	public @ResponseBody BaseModel<String> addOneRelicType(@CurrentUser User user) {
		BaseModel<String> result = new BaseModel<>();
		
		JSONObject json = this.convertRequestBody();

		RelicType relicType = JSONObject.toJavaObject(json, RelicType.class);
		
		if("".equals(relicType.getRelicType().trim())) {
			result.setMessage("分类名不能为空");
			result.setStatus(Constants.FAIL_INVALID_DATA);
			return result;
		}
		
		try {
			relicTypeService.addOneRelicType(relicType, user.getId());
		}
		catch(InheaterSOAException e) {
			result.setMessage(e.getMessage());
			result.setStatus(Constants.FAIL_BUSINESS_ERROR);
		}
		
		return result;
		
	}

    /**
     * 删除一个分类
     */
	@Authorization(authCode = 5)
	@RequestMapping(value = "/management/{relicTypeId}", method = RequestMethod.DELETE)
    public @ResponseBody BaseModel<String> removeOneRelicType(@PathVariable Integer relicTypeId) {

	    BaseModel<String> result = new BaseModel<>();

        try{
            relicTypeService.removeOneRelicType(relicTypeId);
        }
        catch (InheaterSOAException e) {
            result.setMessage(e.getMessage());
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }

        return result;

    }

	/**
	 * 编辑一个分类
	 */
	@Authorization(authCode = 6)
	@RequestMapping(value = "/management", method = RequestMethod.PUT)
	public @ResponseBody BaseModel<String> updateOneRelicType(@CurrentUser User user) {

		BaseModel<String> result = new BaseModel<>();

		JSONObject json = this.convertRequestBody();

		RelicType relicType = JSONObject.toJavaObject(json, RelicType.class);

		try{
			relicTypeService.updateOneRelicType(relicType, user.getId());
		}
		catch (InheaterSOAException e) {
			result.setMessage(e.getMessage());
			result.setStatus(Constants.FAIL_BUSINESS_ERROR);
		}

		return result;

	}


	/**
	 * 获取所有分类
	 */
	@RequestMapping(value = "/resources", method = RequestMethod.GET)
	public @ResponseBody BaseModel<List<RelicType>> getAllRelicType() {

		BaseModel<List<RelicType>> result = new BaseModel<>();

		try{
			List<RelicType> relicTypes = relicTypeService.getAllRelicType();

			result.setData(relicTypes);
		}
		catch (Exception e) {
			result.setMessage(e.getMessage());
			result.setStatus(Constants.FAIL_BUSINESS_ERROR);
		}

		return result;

	}

}
