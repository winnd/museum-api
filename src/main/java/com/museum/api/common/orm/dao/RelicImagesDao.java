package com.museum.api.common.orm.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RelicImagesDao {

    List<Integer> getFileIdsByRelicId(@Param("relicId") Integer relicId);

}
