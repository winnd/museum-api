package com.museum.api.common.orm.dao;

import com.museum.api.common.orm.model.Relic;
import com.museum.api.core.vo.RelicInfoVO;
import com.museum.api.core.vo.RelicSearchVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RelicDao {

    List<RelicInfoVO> getRelicBySearchVO(@Param("searchVO") RelicSearchVO relicSearchVO);

}
