<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="主表编码" prop="isId">
        <el-input
          v-model="queryParams.isId"
          placeholder="请输入盘点主表编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="盘点单号" prop="isCode">
        <el-input
          v-model="queryParams.isCode"
          placeholder="请输入盘点单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="仓库" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入仓库编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="primary"-->
      <!--          plain-->
      <!--          icon="el-icon-plus"-->
      <!--          size="mini"-->
      <!--          @click="handleAdd"-->
      <!--          v-hasPermi="['cx-kcinventory:inventoryDetail:add']"-->
      <!--          disabled="disabled"-->
      <!--        >新增-->
      <!--        </el-button>-->
      <!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['cx-kcinventory:inventoryDetail:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['cx-kcinventory:inventoryDetail:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['cx-kcinventory:inventoryDetail:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="inventoryDetailList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <!--      <el-table-column label="盘点明细ID" align="center" prop="isdId"/>-->
      <!--      <el-table-column label="盘点主表id" align="center" prop="isId"/>-->
      <el-table-column label="盘点单号" align="center" prop="isCode"/>
      <el-table-column label="仓库" align="center" prop="orderId">
        <template slot-scope="scope">
          <span v-if="scope.row.orderId === 1">玲露仓库</span>
          <span v-else-if="scope.row.orderId === 2">鸿衡仓库</span>
          <span v-else-if="scope.row.orderId === 3">易腾仓库</span>
          <span v-else-if="scope.row.orderId === 4">懒财仓库</span>
          <span v-else>未知状态</span>
        </template>
      </el-table-column>
      <el-table-column label="货品信息" align="center" prop="goods_name"/>
      <el-table-column label="盘点数量" align="center" prop="countQuantity"/>
      <el-table-column label="盘点状态" align="center" prop="isStatus">
        <template slot-scope="scope">
          <span v-if="scope.row.isStatus === 0">无盈亏</span>
          <span v-else-if="scope.row.isStatus === 1">盘盈</span>
          <span v-else-if="scope.row.isStatus === 2">盘亏</span>
          <span v-else>未知状态</span>
        </template>
      </el-table-column>
      <el-table-column label="盈亏数量" align="center" prop="profitLossQuantity"/>
      <el-table-column label="盘点金额" align="center" prop="countAmount"/>
      <el-table-column label="入库单价" align="center" prop="iuPrice"/>
      <!--      <el-table-column label="货品code" align="center" prop="g_code"/>-->
      <el-table-column label="备注" align="center" prop="remark">
        <template slot-scope="scope">
          {{ scope.row.remark === null || scope.row.remark == " " ? '暂无备注' : scope.row.remark }}
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['cx-kcinventory:inventoryDetail:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['cx-kcinventory:inventoryDetail:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改盘点明细对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="主表编码" prop="isId">
          <el-input v-model="form.isId" placeholder="请输入盘点主表编码" :disabled="isDisable" style="width: 350px"/>
        </el-form-item>
        <el-form-item label="盘点单号" prop="isCode">
          <el-input v-model="form.isCode" placeholder="请输入盘点单号" :disabled="isDisable" style="width: 350px"/>
        </el-form-item>
        <el-form-item label="仓库" prop="orderId">
          <el-select v-model="form.orderId" placeholder="请选择仓库" filterable style="width: 350px">
            <el-option
              v-for="warehouse in WareHouse"
              :key="warehouse.w_id"
              :label="warehouse.w_name"
              :value="warehouse.w_id"
            />
          </el-select>
        </el-form-item>
        <!--        <el-form-item label="规格型号" prop="specCode">-->
        <!--          <el-input v-model="form.specCode" placeholder="请输入规格型号"/>-->
        <!--        </el-form-item>-->
        <el-form-item label="盘点数量" prop="countQuantity">
          <el-input-number v-model="form.countQuantity" placeholder="请输入盘点数量" style="width: 350px"/>
        </el-form-item>
        <el-form-item label="盈亏数量" prop="profitLossQuantity">
          <el-input-number v-model="form.profitLossQuantity" placeholder="请输入盈亏数量" style="width: 350px"/>
        </el-form-item>
        <el-form-item label="盘点金额" prop="countAmount">
          <el-input-number v-model="form.countAmount" placeholder="请输入盘点金额" style="width: 350px"/>
        </el-form-item>
        <el-form-item label="入库单价" prop="iuPrice">
          <el-input-number v-model="form.iuPrice" placeholder="请输入入库单价" style="width: 350px"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" style="width: 350px"/>
        </el-form-item>
        <el-form-item label="货品" prop="gCode">
          <template slot-scope="scope">
            <el-select v-model="form.gCode" placeholder="请选择货品" style="width: 350px">
              <el-option
                v-for="good in allHpGoods"
                :key="good.g_code"
                :label="good.g_name"
                :value="good.g_code"
              ></el-option>
            </el-select>
          </template>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addInventoryDetail,
  delInventoryDetail,
  getInventoryDetail,
  listInventoryDetail,
  updateInventoryDetail
} from "@/api/inventoryDetail/inventoryDetail";
import dict from "@/utils/dict";
import {findAllHpGoods, findWareHouse, genIsCode} from "@/api/kcinventory/kcinventory";

export default {
  name: "InventoryDetail",
  data() {
    return {
      isDisable: false,
      //货品信息
      allHpGoods: [],
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 盘点明细表格数据
      inventoryDetailList: [],
      //仓库
      WareHouse: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangeCreateTime: [],
      // 备注时间范围
      daterangeUpdateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        isId: null,
        isCode: null,
        orderId: null,
        isStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        isId: [
          {required: true, message: "盘点主表id不能为空", trigger: "blur"}
        ],
        isCode: [
          {required: true, message: "盘点单号不能为空", trigger: "blur"}
        ],
        orderId: [
          {required: true, message: "相关订单ID不能为空", trigger: "blur"}
        ],
        specCode: [
          {required: true, message: "规格型号不能为空", trigger: "blur"}
        ],
        countQuantity: [
          {required: true, message: "盘点数量不能为空", trigger: "blur"}
        ],
        profitLossQuantity: [
          {required: true, message: "盈亏数量不能为空", trigger: "blur"}
        ],
        isStatus: [
          {required: true, message: "盘点状态 0:无盈亏 1:盘盈 2:盘亏不能为空", trigger: "change"}
        ],
        countAmount: [
          {required: true, message: "盘点金额不能为空", trigger: "blur"}
        ],
        iuPrice: [
          {required: true, message: "入库单价不能为空", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  mounted() {
    this.findAllHpGoods();
    this.findWareHouse();
  },
  methods: {
    dict,
    /** 查询盘点明细列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      if (null != this.daterangeUpdateTime && '' != this.daterangeUpdateTime) {
        this.queryParams.params["beginUpdateTime"] = this.daterangeUpdateTime[0];
        this.queryParams.params["endUpdateTime"] = this.daterangeUpdateTime[1];
      }
      listInventoryDetail(this.queryParams).then(response => {
        this.inventoryDetailList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        isdId: null,
        isId: null,
        isCode: null,
        orderId: null,
        specCode: null,
        unit: null,
        countQuantity: null,
        profitLossQuantity: null,
        isStatus: null,
        countAmount: null,
        iuPrice: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        isDelte: null,
        gCode: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeCreateTime = [];
      this.daterangeUpdateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.isdId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      genIsCode().then(response => {
        console.info(response);
      });
      this.reset();
      this.open = true;
      this.title = "添加盘点明细";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const isdId = row.isdId || this.ids
      getInventoryDetail(isdId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改盘点明细";
        if (this.title === "修改盘点明细") {
          this.isDisable = true;
        }
      });
    },
    // 获取仓库信息
    findWareHouse() {
      findWareHouse().then(response => {
        //console.info(response);
        this.WareHouse = response.data;
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.isdId != null) {
            updateInventoryDetail(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInventoryDetail(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const isdIds = row.isdId || this.ids;
      this.$modal.confirm('是否确认删除盘点明细编号为"' + isdIds + '"的数据项？').then(function () {
        return delInventoryDetail(isdIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('cx-kcinventory/inventoryDetail/export', {
        ...this.queryParams
      }, `inventoryDetail_${new Date().getTime()}.xlsx`)
    },
    // 获取货品信息
    findAllHpGoods() {
      findAllHpGoods().then(response => {
        //console.info(response);
        this.allHpGoods = response.data;
      })
    },
  }
};
</script>
