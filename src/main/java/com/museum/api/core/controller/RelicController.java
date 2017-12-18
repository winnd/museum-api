package com.museum.api.core.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.museum.api.common.annotation.Authorization;
import com.museum.api.common.constant.Constants;
import com.museum.api.common.controller.BaseController;
import com.museum.api.common.exception.InheaterSOAException;
import com.museum.api.common.orm.model.FileResources;
import com.museum.api.common.orm.model.Relic;
import com.museum.api.common.vo.BaseModel;
import com.museum.api.core.service.FileService;
import com.museum.api.core.service.RelicService;
import com.museum.api.core.vo.ImageInfo;
import com.museum.api.core.vo.RelicInfoVO;
import com.museum.api.core.vo.RelicSearchVO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("relic")
public class RelicController extends BaseController{

    private static final Logger logger = Logger.getLogger(RelicController.class);


    /**
     * 文件的存储位置
     */
    @Value("#{configProperties['fileUpload.fileRootDir']}")
    public String fileRootDir;

    /**
     * 文件的访问链接
     */
    @Value("#{configProperties['fileUpload.fileUrl']}")
    public String fileUrl;

    @Resource
    RelicService relicService;

    @Resource
    FileService fileService;

    /**
     * 添加一个展品(模板)
     * @return
     */
    @Authorization(authCode = 3)
    @RequestMapping(value = "/management/template", method = RequestMethod.GET)
    public @ResponseBody BaseModel<HashMap<String, Integer>> generateOneRelic() {

        BaseModel<HashMap<String, Integer>> result = new BaseModel<>();

        try{
            Integer relicId = relicService.generateOneRelic();

            if(relicId == 0) {
                result.setMessage("操作失败");
                result.setStatus(Constants.FAIL_BUSINESS_ERROR);
            }
            else {
                HashMap<String, Integer> resultHashMap = new HashMap();
                resultHashMap.put("id", relicId);
                result.setData(resultHashMap);
            }

        }
        catch (InheaterSOAException e) {
            result.setMessage(e.getMessage());
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setMessage("捕捉到异常");
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }

        return result;

    }

    /**
     * 更新藏品信息
     */
    @Authorization(authCode = 3)
    @RequestMapping(value = "management", method = RequestMethod.PUT)
    public @ResponseBody BaseModel<String> updateRelic() {
        BaseModel<String> result = new BaseModel<>();

        JSONObject json = this.convertRequestBody();

        Relic relic = JSONObject.toJavaObject(json, Relic.class);

        try{
            if(relicService.updateRelic(relic) == 0) {
                result.setMessage("业务错误");
                result.setStatus(Constants.FAIL_BUSINESS_ERROR);
            }
        }
        catch (InheaterSOAException e) {
            result.setMessage(e.getMessage());
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setMessage("业务错误");
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }

        return result;

    }

    /**
     * 根据ID删除展品
     * @return
     */
    @Authorization(authCode = 2)
    @RequestMapping(value = "/management/id/{relicId}", method = RequestMethod.DELETE)
    public @ResponseBody BaseModel<String> removeOneRelic(@PathVariable Integer relicId) {

        BaseModel<String> result = new BaseModel<>();

        try{

            if(relicService.removeOneRelic(relicId) != 1) {
                result.setMessage("操作失败");
                result.setStatus(Constants.FAIL_BUSINESS_ERROR);
            }
        }
        catch (InheaterSOAException e) {
            result.setMessage(e.getMessage());
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setMessage("捕捉到异常");
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }

        return result;

    }

    /**
     * 根据需求获取产品
     */
    @RequestMapping(value = "/resources", method = RequestMethod.POST)
    public @ResponseBody BaseModel<List<RelicInfoVO>> getRelicBySearchVO() {
        BaseModel<List<RelicInfoVO>> result = new BaseModel<>();

        JSONObject json = this.convertRequestBody();

        RelicSearchVO relicSearchVO = JSONObject.toJavaObject(json, RelicSearchVO.class);

        try{
            List<RelicInfoVO> relics = relicService.getRelicBySearchVO(relicSearchVO);

            for (RelicInfoVO relic : relics) {
                String imagesString = relic.getImagesString();

                if(imagesString != null && imagesString.trim().length() > 0) {

                    ArrayList<ImageInfo> imageInfos = new ArrayList<>();

                    String[] eachImage = imagesString.split(",");

                    for (int i = 0; i < eachImage.length; i++) {
                        String[] info = eachImage[i].split("::");

                        ImageInfo imageInfo = new ImageInfo();

                        imageInfo.setId(Integer.parseInt(info[0]));
                        imageInfo.setUrl(info[1]);

                        imageInfos.add(imageInfo);

                    }

                    relic.setImages(imageInfos);
                    relic.setImagesString("");


                }

            }

            result.setData(relics);
            result.setPage(new PageInfo<RelicInfoVO>(relics));
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setMessage("系统错误");
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }

        return result;
    }



    /**
     * 登记展品图片
     */
    @Authorization(authCode = 3)
    @RequestMapping(value = "management/image", method = RequestMethod.POST)
    public @ResponseBody BaseModel<String> addRelicImage(){

        BaseModel<String> result = new BaseModel();

        JSONObject json = this.convertRequestBody();

        Integer relicId = json.getInteger("relicId");

        String imageIds = json.getString("imageIds");

        try {

            List<Integer> ids =  JSON.parseArray(imageIds, Integer.class);

            relicService.addRelicImage(relicId, ids);
        }
        catch (InheaterSOAException e) {
            result.setMessage(e.getMessage());
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }

        return result;
    }


    /**
     * 删除展品图片
     */
    @Authorization(authCode = 3)
    @RequestMapping(value = "management/image", method = RequestMethod.DELETE)
    public @ResponseBody BaseModel<String> removeRelicImage(){

        BaseModel<String> result = new BaseModel();

        JSONObject json = this.convertRequestBody();

        Integer relicId = json.getInteger("relicId");

        Integer fileId = json.getInteger("imageId");

        try {
            if(relicService.removeRelicImage(relicId, fileId) == 1) {
                FileResources fileResources = fileService.getFileInfoById(fileId);

                if(fileResources != null) {
                    String url = fileResources.getUrl();

                    String fileAddress = url.replaceFirst(fileUrl, fileRootDir);

                    logger.info("需要删除的文件为: " + fileAddress);

                    File file = new File(fileAddress);

                    if(file.exists()) {
                        file.delete();
                    }
                    else {
                        result.setStatus(Constants.FAIL_BUSINESS_ERROR);
                        result.setMessage("删除失败，可能原因：文件不存在");
                    }

                    fileService.removeFileById(fileId);


                }
                else {
                    result.setStatus(Constants.FAIL_BUSINESS_ERROR);
                    result.setMessage("删除失败，可能原因：数据库不存在文件记录");
                }
            }
            else {
                result.setStatus(Constants.FAIL_BUSINESS_ERROR);
                result.setMessage("删除失败，可能原因：数据库不存在相应的记录");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }

        return result;
    }

}
