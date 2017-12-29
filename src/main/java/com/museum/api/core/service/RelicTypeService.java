package com.museum.api.core.service;

import javax.annotation.Resource;

import com.museum.api.common.orm.mapper.RelicTypeMapper;
import com.museum.api.common.orm.model.RelicType;
import com.museum.api.common.orm.model.RelicTypeExample;
import org.springframework.stereotype.Service;

import com.museum.api.common.exception.InheaterSOAException;
import com.museum.api.common.exception.InheaterSOAExceptionCode;
import com.museum.api.common.exception.InheaterSOAExceptionType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RelicTypeService {

	@Resource
	RelicTypeMapper relicTypeMapper;

    /**
     * 添加一个分类
     * @param relicType
     * @return
     */
	@Transactional
	public int addOneRelicType(RelicType relicType, Integer userId) {
		
		RelicTypeExample example = new RelicTypeExample();
		
		example.createCriteria().andRelicTypeEqualTo(relicType.getRelicType());
		
		if(!relicTypeMapper.selectByExample(example).isEmpty()) {
			throw new InheaterSOAException(InheaterSOAExceptionCode.BUNIESS_EXCEPTION, InheaterSOAExceptionType.BUSINESS,"该分类已存在");
		}
		
		Long currentTime = System.currentTimeMillis();

		relicType.setCreateBy(userId);
		relicType.setUpdateBy(userId);
		relicType.setCreateTime(currentTime);
		relicType.setUpdateTime(currentTime);
		
		return relicTypeMapper.insert(relicType);
		
	}


    /**
     * 删除一个分类
     * @param relicTypeId
     * @return
     */
	@Transactional
    public int removeOneRelicType(Integer relicTypeId) {

	    RelicTypeExample example = new RelicTypeExample();

	    example.createCriteria().andIdEqualTo(relicTypeId);

        if(relicTypeMapper.selectByExample(example).isEmpty()) {
            throw new InheaterSOAException(InheaterSOAExceptionCode.BUNIESS_EXCEPTION, InheaterSOAExceptionType.BUSINESS,"该分类不存在");
        }

        return relicTypeMapper.deleteByPrimaryKey(relicTypeId);

    }

	/**
	 * 编辑一个分类
	 * @param relicType
	 * @return
	 */
	@Transactional
	public int updateOneRelicType(RelicType relicType, Integer userId) {

		Long currentTime = System.currentTimeMillis();

		relicType.setUpdateBy(userId);
		relicType.setUpdateTime(currentTime);

		return relicTypeMapper.updateByPrimaryKeySelective(relicType);

	}



	/**
	 * 获取所有分类
	 */

	public List<RelicType> getAllRelicType() {

		RelicTypeExample example = new RelicTypeExample();

		example.setOrderByClause("update_time desc");

		example.createCriteria();

		return relicTypeMapper.selectByExample(example);

	}

}
