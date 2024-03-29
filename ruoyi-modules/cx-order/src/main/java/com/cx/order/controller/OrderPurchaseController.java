package com.cx.order.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.cx.order.domain.OrderPurchaseDetails;
import com.cx.order.dto.PreData;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.cx.order.domain.OrderPurchase;
import com.cx.order.service.IOrderPurchaseService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 进货订单Controller
 *
 * @author 刘志昂
 * @date 2023-11-20
 */
@RestController
@RequestMapping("/purchase")
public class OrderPurchaseController extends BaseController {
    @Autowired
    private IOrderPurchaseService orderPurchaseService;

    /**
     * 查询进货订单列表
     */
    @RequiresPermissions("order:purchase:list")
    @GetMapping("/list")
    public TableDataInfo list(OrderPurchase orderPurchase) {
        startPage();
        List<OrderPurchase> list = orderPurchaseService.selectOrderPurchaseList(orderPurchase);
        return getDataTable(list);
    }

    /**
     * 导出进货订单列表
     */
    @RequiresPermissions("order:purchase:export")
    @Log(title = "进货订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OrderPurchase orderPurchase) {
        List<OrderPurchase> list = orderPurchaseService.selectOrderPurchaseList(orderPurchase);
        ExcelUtil<OrderPurchase> util = new ExcelUtil<OrderPurchase>(OrderPurchase.class);
        util.exportExcel(response, list, "进货订单数据");
    }

    /**
     * 获取进货订单详细信息
     */
    @RequiresPermissions("order:purchase:query")
    @GetMapping(value = "/{poId}")
    public AjaxResult getInfo(@PathVariable("poId") Long poId) {
        return success(orderPurchaseService.selectOrderPurchaseByPoId(poId));
    }

    /**
     * 新增进货订单
     */
    @RequiresPermissions("order:purchase:add")
    @Log(title = "进货订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Map<String, Object> requestData) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OrderPurchase orderPurchase = mapper.convertValue(requestData.get("orderPurchase"), OrderPurchase.class);
        List<OrderPurchaseDetails> orderPurchaseDetails = mapper.convertValue(requestData.get("orderPurchaseDetails"), TypeFactory.defaultInstance().constructCollectionType(List.class, OrderPurchaseDetails.class));
        return toAjax(orderPurchaseService.insertOrderPurchase(orderPurchase, orderPurchaseDetails));
    }

    /**
     * 修改进货订单
     */
    @RequiresPermissions("order:purchase:edit")
    @Log(title = "进货订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Map<String, Object> requestData) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OrderPurchase orderPurchase = mapper.convertValue(requestData.get("orderPurchase"), OrderPurchase.class);
        List<OrderPurchaseDetails> orderPurchaseDetails = mapper.convertValue(requestData.get("orderPurchaseDetails"), TypeFactory.defaultInstance().constructCollectionType(List.class, OrderPurchaseDetails.class));
        List<OrderPurchaseDetails> updateDetails = mapper.convertValue(requestData.get("updateDetails"), TypeFactory.defaultInstance().constructCollectionType(List.class, OrderPurchaseDetails.class));

        return toAjax(orderPurchaseService.updateOrderPurchase(orderPurchase, orderPurchaseDetails, updateDetails));
    }

    /**
     * 删除进货订单
     */
    @RequiresPermissions("order:purchase:remove")
    @Log(title = "进货订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{poIds}")
    public AjaxResult remove(@PathVariable Long[] poIds) {
        return toAjax(orderPurchaseService.deleteOrderPurchaseByPoIds(poIds));
    }

    /**
     * 生成进货编码
     */
    @GetMapping("/getPoCode")
    public AjaxResult getPoCode() {
        return success(orderPurchaseService.getPoCode());
    }

//    @PostMapping("/preData")
//    public AjaxResult preData(@RequestBody PreData preData) {
//        //log.info("preData：{}",preData);
//        List<OrderPurchaseDetails> preList = new ArrayList<>();
//        if (preData != null) {
//            for (String s : preData.getGoodsCode()) {
//                OrderPurchaseDetails details = new OrderPurchaseDetails();
//                details.setPoCode(preData.getPoCode());
//                details.setgCode(s);
//                preList.add(details);
//            }
//        }
//        return success(preList);
//    }

    @GetMapping("/{status}/{poIds}")
    public AjaxResult updateAudit(@PathVariable Integer status, @PathVariable Long[] poIds) {
        String reviewer = String.valueOf(SecurityUtils.getUserId());
        return success(orderPurchaseService.updateAudit(status, poIds, reviewer));
    }

//    /**
//     * 新增进货明细
//     */
//    @RequiresPermissions("order:purchase:add")
//    @Log(title = "进货明细", businessType = BusinessType.INSERT)
//    @PostMapping("/add")
//    public AjaxResult add(@RequestBody List<OrderPurchaseDetails> orderPurchaseDetails)
//    {
//        //log.info("新增：{}",jhDetail);
//        int i = 0;
//        for (OrderPurchaseDetails detail : orderPurchaseDetails) {
//            i = jhDetailService.insertJhDetail(detail);
//        }
//        return toAjax(i);
//    }
}
