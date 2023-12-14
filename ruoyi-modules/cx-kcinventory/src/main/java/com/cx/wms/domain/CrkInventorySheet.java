package com.cx.wms.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

/**
 * 库存盘点对象 crk_inventory_sheet
 *
 * @author xrc
 * @date 2023-12-14
 */
public class CrkInventorySheet extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 盘点ID
     */
    private Long isId;

    /**
     * 仓库表_仓库ID
     */
    @Excel(name = "仓库表_仓库ID")
    private Long warWId;

    /**
     * 盘点单号
     */
    @Excel(name = "盘点单号")
    private String isCode;

    /**
     * 盘点结果  0:无盈亏 1:盘盈 2:盘亏
     */
    @Excel(name = "盘点结果  0:无盈亏 1:盘盈 2:盘亏")
    private Integer isResult;

    /**
     * 仓库 ID
     */
    @Excel(name = "仓库 ID")
    private Long wId;

    /**
     * 来自数据字典（盘点类型）
     */
    @Excel(name = "来自数据字典", readConverterExp = "盘=点类型")
    private String isType;

    /**
     * 盘点开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "盘点开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date isStartTime;

    /**
     * 盘点结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "盘点结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date isEndTime;

    /**
     * 出库状态 0:已出库 1:未出库
     */
    @Excel(name = "出库状态 0:已出库 1:未出库")
    private Integer outStatus;

    /**
     * 入库状态 0:已入库 1:未入库
     */
    @Excel(name = "入库状态 0:已入库 1:未入库")
    private Integer inStatus;

    /**
     * 关联用户表（盘点经办人）
     */
    @Excel(name = "关联用户表", readConverterExp = "盘=点经办人")
    private Long isManager;

    /**
     * 0：存在；1：已删除，不存在
     */
    @Excel(name = "0：存在；1：已删除，不存在")
    private Integer isDelte;

    /**
     * 盘点明细信息
     */
    private List<CrkIsDetails> crkIsDetailsList;

    public void setIsId(Long isId) {
        this.isId = isId;
    }

    public Long getIsId() {
        return isId;
    }

    public void setWarWId(Long warWId) {
        this.warWId = warWId;
    }

    public Long getWarWId() {
        return warWId;
    }

    public void setIsCode(String isCode) {
        this.isCode = isCode;
    }

    public String getIsCode() {
        return isCode;
    }

    public void setIsResult(Integer isResult) {
        this.isResult = isResult;
    }

    public Integer getIsResult() {
        return isResult;
    }

    public void setwId(Long wId) {
        this.wId = wId;
    }

    public Long getwId() {
        return wId;
    }

    public void setIsType(String isType) {
        this.isType = isType;
    }

    public String getIsType() {
        return isType;
    }

    public void setIsStartTime(Date isStartTime) {
        this.isStartTime = isStartTime;
    }

    public Date getIsStartTime() {
        return isStartTime;
    }

    public void setIsEndTime(Date isEndTime) {
        this.isEndTime = isEndTime;
    }

    public Date getIsEndTime() {
        return isEndTime;
    }

    public void setOutStatus(Integer outStatus) {
        this.outStatus = outStatus;
    }

    public Integer getOutStatus() {
        return outStatus;
    }

    public void setInStatus(Integer inStatus) {
        this.inStatus = inStatus;
    }

    public Integer getInStatus() {
        return inStatus;
    }

    public void setIsManager(Long isManager) {
        this.isManager = isManager;
    }

    public Long getIsManager() {
        return isManager;
    }

    public void setIsDelte(Integer isDelte) {
        this.isDelte = isDelte;
    }

    public Integer getIsDelte() {
        return isDelte;
    }

    public List<CrkIsDetails> getCrkIsDetailsList() {
        return crkIsDetailsList;
    }

    public void setCrkIsDetailsList(List<CrkIsDetails> crkIsDetailsList) {
        this.crkIsDetailsList = crkIsDetailsList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("isId", getIsId())
                .append("warWId", getWarWId())
                .append("isCode", getIsCode())
                .append("isResult", getIsResult())
                .append("wId", getwId())
                .append("isType", getIsType())
                .append("isStartTime", getIsStartTime())
                .append("isEndTime", getIsEndTime())
                .append("outStatus", getOutStatus())
                .append("inStatus", getInStatus())
                .append("remark", getRemark())
                .append("isManager", getIsManager())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("isDelte", getIsDelte())
                .append("crkIsDetailsList", getCrkIsDetailsList())
                .toString();
    }
}