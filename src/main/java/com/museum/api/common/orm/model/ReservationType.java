package com.museum.api.common.orm.model;

public class ReservationType {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reservation_type.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reservation_type.reservation_type
     *
     * @mbggenerated
     */
    private String reservationType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reservation_type.create_by
     *
     * @mbggenerated
     */
    private Integer createBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reservation_type.create_time
     *
     * @mbggenerated
     */
    private Long createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reservation_type.update_by
     *
     * @mbggenerated
     */
    private Integer updateBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column reservation_type.update_time
     *
     * @mbggenerated
     */
    private Long updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reservation_type.id
     *
     * @return the value of reservation_type.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reservation_type.id
     *
     * @param id the value for reservation_type.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reservation_type.reservation_type
     *
     * @return the value of reservation_type.reservation_type
     *
     * @mbggenerated
     */
    public String getReservationType() {
        return reservationType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reservation_type.reservation_type
     *
     * @param reservationType the value for reservation_type.reservation_type
     *
     * @mbggenerated
     */
    public void setReservationType(String reservationType) {
        this.reservationType = reservationType == null ? null : reservationType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reservation_type.create_by
     *
     * @return the value of reservation_type.create_by
     *
     * @mbggenerated
     */
    public Integer getCreateBy() {
        return createBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reservation_type.create_by
     *
     * @param createBy the value for reservation_type.create_by
     *
     * @mbggenerated
     */
    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reservation_type.create_time
     *
     * @return the value of reservation_type.create_time
     *
     * @mbggenerated
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reservation_type.create_time
     *
     * @param createTime the value for reservation_type.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reservation_type.update_by
     *
     * @return the value of reservation_type.update_by
     *
     * @mbggenerated
     */
    public Integer getUpdateBy() {
        return updateBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reservation_type.update_by
     *
     * @param updateBy the value for reservation_type.update_by
     *
     * @mbggenerated
     */
    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column reservation_type.update_time
     *
     * @return the value of reservation_type.update_time
     *
     * @mbggenerated
     */
    public Long getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column reservation_type.update_time
     *
     * @param updateTime the value for reservation_type.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}