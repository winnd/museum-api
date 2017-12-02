package com.museum.api.common.orm.dao;

import com.museum.api.common.orm.model.FileResources;

public interface FileResourcesDao {

    Integer insertFileAndGetId(FileResources fileResources);

}
