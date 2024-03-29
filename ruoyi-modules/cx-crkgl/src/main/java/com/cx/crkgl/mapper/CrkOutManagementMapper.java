package com.cx.crkgl.mapper;

import java.util.List;

import com.cx.crkgl.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 出库管理Mapper接口
 * 
 * @author hfh
 * @date 2023-12-20
 */
@Mapper
public interface CrkOutManagementMapper 
{
    /**
     * 查询出库管理
     * 
     * @param outId 出库管理主键
     * @return 出库管理
     */
    public CrkOutManagement selectCrkOutManagementByOutId(Long outId);

    /**
     * 查询出库管理列表
     * 
     * @param crkOutManagement 出库管理
     * @return 出库管理集合
     */
    public List<CrkOutManagement> selectCrkOutManagementList(CrkOutManagement crkOutManagement);

    /**
     * 获取最新的出库编码
     * @return
     */
    public CrkOutManagement getCrkOutManagementBySlCode();

    /**
     * 新增出库管理
     * 
     * @param crkOutManagement 出库管理
     * @return 结果
     */
    public int insertCrkOutManagement(CrkOutManagement crkOutManagement);

    /**
     * 修改出库管理
     * 
     * @param crkOutManagement 出库管理
     * @return 结果
     */
    public int updateCrkOutManagement(CrkOutManagement crkOutManagement);

    /**
     * 删除出库管理
     * 
     * @param outId 出库管理主键
     * @return 结果
     */
    public int deleteCrkOutManagementByOutId(Long outId);

    /**
     * 批量删除出库管理
     * 
     * @param outIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCrkOutManagementByOutIds(Long[] outIds);

    /**
     * 批量删除出库明细

     * 
     * @param outIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCrkOutDetailsByOutIds(Long[] outIds);
    
    /**
     * 批量新增出库明细

     * 
     * @param crkOutDetailsList 出库明细
列表
     * @return 结果
     */
    public int batchCrkOutDetails(List<CrkOutDetails> crkOutDetailsList);
    

    /**
     * 通过出库管理主键删除出库明细
信息
     * 
     * @param outId 出库管理ID
     * @return 结果
     */
    public int deleteCrkOutDetailsByOutId(Long outId);

    /**
     * 查询销售明细
     *
     */
    public List<OrderSalesDetails> selectSalesDetails(OrderSalesDetails orderSalesDetails);

    /**
     * 查询当前库存数量
     *
     * @param
     */
    public long currentInventory(@Param("slId")Long slId, @Param("gId")Long gId);

    /**
     * 批量出库,减少库存
     *
     * @param
     * @return
     */
    public int InventoryOutbound(CrkOutDetails crkOutDetails);
    public int InventoryOutbound1(CrkOutDetails crkOutDetails);
//删除出库，增加计划数量
    public int InventoryOutbound2(CrkOutDetails crkOutDetails);
    /**
     * 查询货品id
     *
     * @param outId 出库管理
     */
    public List<CrkOutDetails> selectCrkOutDetails(Long[] outId);

    public List<CrkOutDetails> selectCrkOutDetail(List<String> outIds);

    public List<CrkOutDetails> selectCrkOutDetails2(Long outId);
    /**
     * 查询出库管理列表
     *
     * @param outCodes 入库管理
     * @return 入库管理集合
     */
    public List<CrkOutManagement> selectCrkOutManagementList1(List<String> outCodes);

    public List<CrkOutManagement> selectCrkOutManagementList2(List<String> outId);

    /**
     * 批量审核出库
     *
     * @param
     * @return 结果
     */
    public int AuditOutbounds(@Param("crkOutManagements") CrkOutManagement crkOutManagements , @Param("isApproved") boolean isApproved);

    /**
     * 批量审核出库,减少库存
     *
     * @param
     * @return 结果
     */
    public int reduceReviews(CrkOutDetails crkOutDetails);

    /*
     * 修改销售状态
     *
     */
    public int saleStatus(String[] sCode);

    /*
     * 修改销售状态,部分出库
     *
     */
    public int saleStatus1(String[] sCode);

    public int saleStatus2(String[] sCode);

    /**
     * 批量撤销出库
     *
     * @param outIds 需要删除的数据主键集合
     * @return 结果
     */
    public int WithdrawalStorages(List<String> outIds);
    /**
     * 批量撤销出库,增加库存
     *
     * @param
     * @return 结果
     */
    public int productReviews(CrkOutDetails crkOutDetails);

}
