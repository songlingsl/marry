<template>

  <div class="app-container">

    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">

      <el-form-item label="车牌" prop="carNumber">

        <el-input

          v-model="queryParams.carNumber"

          placeholder="请输入车牌"

          clearable

          size="small"

          @keyup.enter.native="handleQuery"

        />

      </el-form-item>



      <el-form-item label="有效期" prop="validTime">

        <el-date-picker clearable size="small" style="width: 200px"

                        v-model="queryParams.validTime"

                        type="date"

                        value-format="yyyy-MM-dd"

                        placeholder="选择有效期">

        </el-date-picker>

      </el-form-item>

      <el-form-item>

        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>

        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>

      </el-form-item>

    </el-form>



    <el-row :gutter="10" class="mb8">

      <el-col :span="1.5">

        <el-button

          type="primary"

          icon="el-icon-plus"

          size="mini"

          @click="handleAdd"

          v-hasPermi="['system:number:add']"

        >新增</el-button>

      </el-col>

      <el-col :span="1.5">

        <el-button

          type="success"

          icon="el-icon-edit"

          size="mini"

          :disabled="single"

          @click="handleUpdate"

          v-hasPermi="['system:number:edit']"

        >修改</el-button>

      </el-col>

      <el-col :span="1.5">

        <el-button

          type="danger"

          icon="el-icon-delete"

          size="mini"

          :disabled="multiple"

          @click="handleDelete"

          v-hasPermi="['system:number:remove']"

        >删除</el-button>

      </el-col>

      <el-col :span="1.5">

        <el-button

          type="warning"

          icon="el-icon-download"

          size="mini"

          @click="handleImport"

          v-hasPermi="['system:number:export']"

        >导入</el-button>

      </el-col>

    </el-row>



    <el-table v-loading="loading" :data="numberList" @selection-change="handleSelectionChange">

      <el-table-column type="selection" width="55" align="center" />

<!--      <el-table-column label="有效期" align="center" prop="carId" />-->

      <el-table-column label="车牌" align="center" prop="carNumber" />



      <el-table-column label="有效期" align="center" prop="validTime" width="180">

        <template slot-scope="scope">

          <span>{{ parseTime(scope.row.validTime, '{y}-{m}-{d}') }}</span>

        </template>

      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">

        <template slot-scope="scope">

          <el-button

            size="mini"

            type="text"

            icon="el-icon-edit"

            @click="handleUpdate(scope.row)"

            v-hasPermi="['system:number:edit']"

          >修改</el-button>

          <el-button

            size="mini"

            type="text"

            icon="el-icon-delete"

            @click="handleDelete(scope.row)"

            v-hasPermi="['system:number:remove']"

          >删除</el-button>

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



    <!-- 添加或修改【请填写功能名称】对话框 -->

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>

      <el-form ref="form" :model="form" :rules="rules" label-width="80px">

        <el-form-item label="车牌" prop="carNumber">

          <el-input v-model="form.carNumber" placeholder="请输入车牌" />

        </el-form-item>



        <el-form-item label="有效期" prop="validTime">

          <el-date-picker clearable size="small" style="width: 200px"

                          v-model="form.validTime"

                          type="date"

                          value-format="yyyy-MM-dd"

                          placeholder="选择有效期">

          </el-date-picker>

        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">

        <el-button type="primary" @click="submitForm">确 定</el-button>

        <el-button @click="cancel">取 消</el-button>

      </div>

    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
<!--        <div class="el-upload__tip" slot="tip">-->
<!--          <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据-->
<!--          <el-link type="info" style="font-size:12px" @click="importTemplate">下载模板</el-link>-->
<!--        </div>-->
        <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“xls”或“xlsx”格式文件！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>



<script>

  import { listNumber, getNumber, delNumber, addNumber, updateNumber, exportNumber } from "@/api/car/car";
  import { getToken } from "@/utils/auth";


  export default {

    name: "Number",

    data() {

      return {

        // 遮罩层

        loading: true,

        // 选中数组

        ids: [],

        // 非单个禁用

        single: true,

        // 非多个禁用

        multiple: true,

        // 总条数

        total: 0,

        // 【请填写功能名称】表格数据

        numberList: [],

        // 弹出层标题

        title: "",

        // 是否显示弹出层

        open: false,

        // 查询参数

        queryParams: {

          pageNum: 1,

          pageSize: 10,

          carNumber: undefined,

          importTime: undefined,

          validTime: undefined

        },
        upload: {
          // 是否显示弹出层（用户导入）
          open: false,
          // 弹出层标题（用户导入）
          title: "",
          // 是否禁用上传
          isUploading: false,
          // 是否更新已经存在的用户数据
          updateSupport: 0,
          // 设置上传的请求头部
          headers: { Authorization: "Bearer " + getToken() },
          // 上传的地址
          url: process.env.VUE_APP_BASE_API + "/car/number/importData"
        },
        // 表单参数

        form: {},

        // 表单校验

        rules: {

          carNumber: [

            { required: true, message: "车牌不能为空", trigger: "blur" }

          ],

        }

      };

    },

    created() {

      this.getList();

    },

    methods: {


      handleImport() {
        this.upload.title = "车辆导入";
        this.upload.open = true;
      },

      /** 查询【请填写功能名称】列表 */

      getList() {

        this.loading = true;

        listNumber(this.queryParams).then(response => {
          console.log("得到的值",response)
          this.numberList = response.rows;

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

          carId: undefined,

          carNumber: undefined,

          importTime: undefined,

          validTime: undefined

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

        this.resetForm("queryForm");

        this.handleQuery();

      },

      // 多选框选中数据

      handleSelectionChange(selection) {

        this.ids = selection.map(item => item.carId)

        this.single = selection.length!=1

        this.multiple = !selection.length

      },

      /** 新增按钮操作 */

      handleAdd() {

        this.reset();

        this.open = true;

        this.title = "添加【请填写功能名称】";

      },

      /** 修改按钮操作 */

      handleUpdate(row) {

        this.reset();

        const carId = row.carId || this.ids

        getNumber(carId).then(response => {

          this.form = response.data;

          this.open = true;

          this.title = "修改【请填写功能名称】";

        });

      },

      /** 提交按钮 */

      submitForm: function() {

        this.$refs["form"].validate(valid => {

          if (valid) {

            if (this.form.carId != undefined) {

              updateNumber(this.form).then(response => {

                if (response.code === 200) {

                  this.msgSuccess("修改成功");

                  this.open = false;

                  this.getList();

                }

              });

            } else {

              addNumber(this.form).then(response => {

                if (response.code === 200) {

                  this.msgSuccess("新增成功");

                  this.open = false;

                  this.getList();

                }

              });

            }

          }

        });

      },

      /** 删除按钮操作 */

      handleDelete(row) {

        const carIds = row.carId || this.ids;

        this.$confirm('是否确认删除【请填写功能名称】编号为"' + carIds + '"的数据项?', "警告", {

          confirmButtonText: "确定",

          cancelButtonText: "取消",

          type: "warning"

        }).then(function() {

          return delNumber(carIds);

        }).then(() => {

          this.getList();

          this.msgSuccess("删除成功");

        }).catch(function() {});

      },

      /** 导出按钮操作 */

      submitFileForm() {
        this.$refs.upload.submit();
      },

      // 文件上传中处理
      handleFileUploadProgress(event, file, fileList) {
        this.upload.isUploading = true;
      },
      // 文件上传成功处理
      handleFileSuccess(response, file, fileList) {
        this.upload.open = false;
        this.upload.isUploading = false;
        this.$refs.upload.clearFiles();
        this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
        this.getList();
      },

    }

  };

</script>
