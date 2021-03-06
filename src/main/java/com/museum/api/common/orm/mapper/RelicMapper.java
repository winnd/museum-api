package com.museum.api.common.orm.mapper;

import com.museum.api.common.orm.model.Relic;
import com.museum.api.common.orm.model.RelicExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RelicMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relic
     *
     * @mbggenerated
     */
    int countByExample(RelicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relic
     *
     * @mbggenerated
     */
    int deleteByExample(RelicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relic
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relic
     *
     * @mbggenerated
     */
    int insert(Relic record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relic
     *
     * @mbggenerated
     */
    int insertSelective(Relic record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relic
     *
     * @mbggenerated
     */
    List<Relic> selectByExampleWithBLOBs(RelicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relic
     *
     * @mbggenerated
     */
    List<Relic> selectByExample(RelicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relic
     *
     * @mbggenerated
     */
    Relic selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relic
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") Relic record, @Param("example") RelicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relic
     *
     * @mbggenerated
     */
    int updateByExampleWithBLOBs(@Param("record") Relic record, @Param("example") RelicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relic
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") Relic record, @Param("example") RelicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relic
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Relic record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relic
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(Relic record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table relic
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Relic record);
}