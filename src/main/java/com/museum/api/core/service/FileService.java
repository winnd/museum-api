package com.museum.api.core.service;

import com.museum.api.common.orm.dao.FileResourcesDao;
import com.museum.api.common.orm.mapper.FileResourcesMapper;
import com.museum.api.common.orm.model.FileResources;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class FileService {

    @Resource
    FileResourcesMapper fileResourcesMapper;

    @Resource
    FileResourcesDao fileResourcesDao;

    @Transactional
    public FileResources addNewFile(FileResources fileResources){

        fileResourcesDao.insertFileAndGetId(fileResources);

        return fileResources;

    }

    public FileResources getFileInfoById(Integer fileId) {
        return fileResourcesMapper.selectByPrimaryKey(fileId);
    }

    public int removeFileById(Integer id) {
        return fileResourcesMapper.deleteByPrimaryKey(id);
    }

}
