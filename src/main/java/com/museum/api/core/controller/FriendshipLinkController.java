package com.museum.api.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.museum.api.common.annotation.Authorization;
import com.museum.api.common.annotation.CurrentUser;
import com.museum.api.common.constant.Constants;
import com.museum.api.common.controller.BaseController;
import com.museum.api.common.orm.model.FriendshipLink;
import com.museum.api.common.orm.model.User;
import com.museum.api.common.vo.BaseModel;
import com.museum.api.core.service.FriendshipLinkService;
import com.museum.api.core.vo.FriendshipLinkSearchVO;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("friendship-link")
public class FriendshipLinkController extends BaseController {

    @Resource
    FriendshipLinkService friendshipLinkService;

    /**
     * 新增友情链接
     */
    @Authorization(authCode = 10)
    @RequestMapping(value = "/management", method = RequestMethod.POST)
    @ResponseBody
    public BaseModel<String> addFriendshipLink(@CurrentUser User user){

        BaseModel<String> result = new BaseModel<>();

        JSONObject json = this.convertRequestBody();

        String name = json.getString("name");

        String url = json.getString("url");

        String image = json.getString("image");

        try{

            Assert.hasText(name, "链接名不能为空");

            Assert.hasText(url, "链接地址不能为空");

            if( friendshipLinkService.addFriendshipLink(name, url, image, user.getId()) != 1){

                result.setMessage("数据库错误");
                result.setStatus(Constants.FAIL_BUSINESS_ERROR);

            }

        }

        catch (IllegalArgumentException e) {
            result.setMessage(e.getMessage());
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setMessage("出现异常");
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }

        return result;

    }

    /**
     * 删除友情链接
     */
    @Authorization(authCode = 11)
    @RequestMapping(value = "/management/id/{friendshipLinkId}", method = RequestMethod.DELETE)
    @ResponseBody
    public BaseModel<String> removeFriendshipLink(@PathVariable Integer friendshipLinkId) {

        BaseModel<String> result = new BaseModel<>();

        try {

            Assert.notNull(friendshipLinkId, "友情链接id不能为空");

            if (friendshipLinkService.removeFriendshipLink(friendshipLinkId) != 1) {
                result.setMessage("数据库错误");
                result.setStatus(Constants.FAIL_BUSINESS_ERROR);
            }

        }

        catch (IllegalArgumentException e) {
            result.setMessage(e.getMessage());
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }

        catch (Exception e) {
            e.printStackTrace();
            result.setMessage("出现异常");
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }

        return result;

    }



    /**
     * 更新友情链接
     */
    @Authorization(authCode = 12)
    @RequestMapping(value = "/management", method = RequestMethod.PUT)
    @ResponseBody
    public BaseModel<String> updateFriendshipLink(@CurrentUser User user){

        BaseModel<String> result = new BaseModel<>();

        JSONObject json = this.convertRequestBody();

        FriendshipLink friendshipLink = JSONObject.toJavaObject(json, FriendshipLink.class);

        try {

            Assert.notNull(friendshipLink.getId(), "友情链接id不能为空");

            if (friendshipLinkService.updateFriendshipLink(friendshipLink, user.getId()) != 1) {
                result.setMessage("数据库错误");
                result.setStatus(Constants.FAIL_BUSINESS_ERROR);
            }

        }

        catch (IllegalArgumentException e) {
            result.setMessage(e.getMessage());
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }

        catch (Exception e) {
            e.printStackTrace();
            result.setMessage("出现异常");
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }

        return result;
    }

    /**
     * 获取友情链接
     */
    @RequestMapping(value = "/resources", method = RequestMethod.POST)
    @ResponseBody
    public BaseModel<List<FriendshipLink>> getFriendshipLink() {

        BaseModel<List<FriendshipLink>> result = new BaseModel<>();

        JSONObject json = this.convertRequestBody();

        FriendshipLinkSearchVO searchVO = JSONObject.toJavaObject(json, FriendshipLinkSearchVO.class);

        try {

            List<FriendshipLink> friendshipLinks = friendshipLinkService.getFriendshipLink(searchVO);

            result.setData(friendshipLinks);
            result.setPage(new PageInfo<FriendshipLink>(friendshipLinks));

        }


        catch (Exception e) {
            e.printStackTrace();
            result.setMessage("出现异常");
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }

        return result;


    }

}
