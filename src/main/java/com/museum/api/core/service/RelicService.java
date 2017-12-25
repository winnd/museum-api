package com.museum.api.core.service;


import com.github.pagehelper.PageHelper;
import com.museum.api.common.constant.Constants;
import com.museum.api.common.exception.InheaterSOAException;
import com.museum.api.common.exception.InheaterSOAExceptionCode;
import com.museum.api.common.exception.InheaterSOAExceptionType;
import com.museum.api.common.orm.dao.RelicDao;
import com.museum.api.common.orm.dao.RelicImagesDao;
import com.museum.api.common.orm.mapper.FileResourcesMapper;
import com.museum.api.common.orm.mapper.RelicImagesMapper;
import com.museum.api.common.orm.mapper.RelicMapper;
import com.museum.api.common.orm.model.*;
import com.museum.api.core.controller.RelicController;
import com.museum.api.core.vo.RelicInfoVO;
import com.museum.api.core.vo.RelicSearchVO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class RelicService {


    @Resource
    RelicMapper relicMapper;

    @Resource
    RelicImagesMapper relicImagesMapper;

    @Resource
    FileResourcesMapper fileResourcesMapper;

    @Resource
    RelicDao relicDao;

    @Resource
    RelicImagesDao relicImagesDao;

    @Resource
    FileService fileService;

    /**
     * 文件的存储位置
     */
    @Value("${fileUpload.fileRootDir}")
    public String fileRootDir;

    /**
     * 文件的访问链接
     */
    @Value("${fileUpload.fileUrl}")
    public String fileUrl;



    private static final Logger logger = Logger.getLogger(RelicService.class);

    /**
     * 添加一个展品(模板)
     * @return
     */
    @Transactional
    public int generateOneRelic(Integer userId){

        Relic relic = new Relic();

        Long currentTime = System.currentTimeMillis();

        relic.setCreateTime(currentTime);
        relic.setCreateBy(userId);
        relic.setUpdateTime(currentTime);
        relic.setUpdateBy(userId);

        if(relicMapper.insert(relic) != 1) {
            return 0;
        }
        else {
            return relic.getId();
        }

    }

    /**
     * 删除一个展品
     * @return
     */
    @Transactional
    public int removeOneRelic(Integer relicId) {

        Relic relic = relicMapper.selectByPrimaryKey(relicId);

        if(relic == null) {
            throw new InheaterSOAException(InheaterSOAExceptionCode.BUNIESS_EXCEPTION, InheaterSOAExceptionType.BUSINESS, "该藏品不存在");
        }

        List<Integer> fileIds = relicImagesDao.getFileIdsByRelicId(relicId);

        // 先删除file_resource中的文件信息和文件

        if(fileIds != null && fileIds.size() > 0) {

            for(Integer fileId : fileIds) {

                FileResources fileResources = fileService.getFileInfoById(fileId);

                if(fileResources != null && fileResources.getUrl() != null) {
                    String url = fileResources.getUrl();

                    String fileAddress = url.replaceFirst(fileUrl, fileRootDir);

                    logger.info("需要删除的文件为: " + fileAddress);

                    File file = new File(fileAddress);

                    if(file.exists()) {
                        file.delete();
                    }
                    else {
                        logger.info("删除失败，可能原因：文件不存在");
                    }

                    fileService.removeFileById(fileId);


                }
                else {
                    logger.info("删除失败，可能原因：数据库不存在文件记录");
                }
            }
        }



        // 删除relic_images中的数据

        RelicImagesExample relicImagesExample = new RelicImagesExample();

        relicImagesExample.createCriteria().andRelicIdEqualTo(relicId);

        relicImagesMapper.deleteByExample(relicImagesExample);


        return relicMapper.deleteByPrimaryKey(relicId);

    }

    /**
     * 更新藏品
     */
    public int updateRelic(Relic relic, Integer userId){
        if(relic == null) {
            throw new InheaterSOAException(InheaterSOAExceptionCode.BUNIESS_EXCEPTION, InheaterSOAExceptionType.BUSINESS, "传入参数为空");
        }

        if(relicMapper.selectByPrimaryKey(relic.getId()) == null) {
            throw new InheaterSOAException(InheaterSOAExceptionCode.BUNIESS_EXCEPTION, InheaterSOAExceptionType.BUSINESS, "藏品不存在");
        }

        Long currentTime = System.currentTimeMillis();

        relic.setUpdateBy(userId);
        relic.setUpdateTime(currentTime);

        return relicMapper.updateByPrimaryKeySelective(relic);
    }

    /**
     * 登记展品图片
     */
    @Transactional
    public void addRelicImage(Integer relicId, List<Integer> fileIds, Integer userId){

        Long currentTime = System.currentTimeMillis();

        if(relicMapper.selectByPrimaryKey(relicId) == null) {
            throw new InheaterSOAException(InheaterSOAExceptionCode.BUNIESS_EXCEPTION, InheaterSOAExceptionType.BUSINESS, "藏品不存在");
        }

        // 首先将数据库里的藏品对应的所有图片id取出来
        List<Integer> existsFileIds = relicImagesDao.getFileIdsByRelicId(relicId);

        if(fileIds != null) {

            for(Integer fileId : fileIds) {

                if(existsFileIds.contains(fileId.intValue())) {
                    continue;
                }

                if(fileResourcesMapper.selectByPrimaryKey(fileId) == null) {
                    throw new InheaterSOAException(InheaterSOAExceptionCode.BUNIESS_EXCEPTION, InheaterSOAExceptionType.BUSINESS, "文件不存在");
                }

                RelicImages relicImages = new RelicImages();

                relicImages.setFileId(fileId);
                relicImages.setRelicId(relicId);
                relicImages.setCreateBy(userId);
                relicImages.setCreateTime(currentTime);
                relicImages.setUpdateBy(userId);
                relicImages.setUpdateTime(currentTime);
                if(relicImagesMapper.insert(relicImages) == 0) {
                    throw new InheaterSOAException(InheaterSOAExceptionCode.BUNIESS_EXCEPTION, InheaterSOAExceptionType.BUSINESS, "插入失败");
                }
            }
        }




        return;

    }


    /**
     * 删除展品图片
     */
    @Transactional
    public int removeRelicImage(Integer relicId, Integer fileId){

        Long currentTime = System.currentTimeMillis();

        RelicImagesExample example = new RelicImagesExample();

        example.createCriteria().andRelicIdEqualTo(relicId).andFileIdEqualTo(fileId);

        return relicImagesMapper.deleteByExample(example);

    }

    /**
     * 根据需求获取产品
     */

    public List<RelicInfoVO> getRelicBySearchVO(RelicSearchVO relicSearchVO) {

        PageHelper.startPage(relicSearchVO.getPageNum(), relicSearchVO.getPageSize() == 0 ? Constants.PAGE_SIZE : relicSearchVO.getPageSize());

        return relicDao.getRelicBySearchVO(relicSearchVO);

    }

}
