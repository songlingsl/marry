<template>

  <div class="app-container">

    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">

      <el-form-item label="预约号牌" prop="carNumber">

        <el-input

          v-model="queryParams.carNumber"

          placeholder="请输入车牌"

          clearable

          size="small"

          @keyup.enter.native="handleQuery"

        />

      </el-form-item>
      <el-form-item label="预约码" prop="subscribeCode">

        <el-input

          v-model="queryParams.subscribeCode"

          placeholder="请输入预约码"

          clearable

          size="small"
          style="width: 150px"
          @keyup.enter.native="handleQuery"

        />

      </el-form-item>

      <el-form-item label="预约日期">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>


      <el-form-item label="预约人" prop="subscribeName">

        <el-input

          v-model="queryParams.subscribeName"

          placeholder="请输入预约者姓名"

          clearable

          size="small"

          @keyup.enter.native="handleQuery"

        />

      </el-form-item>


      <el-form-item label="预约状态" prop="subscribeStatus">
        <el-select
          v-model="queryParams.subscribeStatus"
          placeholder="请选择当前状态"
          clearable
          size="small"
          style="width: 150px"
        >
          <el-option
            v-for="dict in   statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>


      <el-form-item label="12123" prop="importFlag">
        <el-select
          v-model="queryParams.importFlag"
          placeholder="请选择当前状态"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="dict in  importOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>


      <el-form-item label="录入人" prop="sysNickName">

        <el-input

          v-model="queryParams.sysNickName"

          placeholder="请输入录入人"

          clearable

          size="small"

          @keyup.enter.native="handleQuery"

        />

      </el-form-item>


      <el-form-item  >

        <el-input type="hidden"

                   style="width: 240px"

        />

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



        >新增
        </el-button>

      </el-col>

      <el-col :span="1.5">

        <el-button

          type="success"

          icon="el-icon-edit"

          size="mini"

          :disabled="single"

          @click="handleUpdate"



        >修改
        </el-button>

      </el-col>

      <el-col :span="1.5">

        <el-button

          type="danger"

          icon="el-icon-delete"

          size="mini"

          :disabled="multiple"

          @click="handleDelete"



        >删除
        </el-button>

      </el-col>


      <el-col :span="1.5">
        <el-button
          type="info"
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"

        >12123导入</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"

        >导出</el-button>
      </el-col>


    </el-row>


    <el-table v-loading="loading" :data="subscribeList" @selection-change="handleSelectionChange">

      <el-table-column type="selection" width="20" align="center"/>


      <el-table-column label="预约号牌" align="center" prop="carNumber"/>
      <el-table-column label="预约码" align="center" prop="subscribeCode"/>
      <el-table-column label="预约日期" align="center" prop="subscribeTime" width="100">

        <template slot-scope="scope">

          <span>{{ parseTime(scope.row.subscribeTime, '{y}-{m}-{d}') }}</span>

        </template>

      </el-table-column>

      <el-table-column label="预约时段" align="center" prop="subscribeTimePhase" width="100"/>
      <el-table-column label="预约人" align="center" prop="subscribeName" width="80"/>
      <el-table-column label="电话" align="center" prop="subscribePhone" width="110"/>

<!--      <el-table-column label="当前状态" align="center" prop="subscribeStatus"/>-->
      <el-table-column prop="status" label="预约状态" :formatter="statusFormat" align="center" width="80"></el-table-column>

      <el-table-column label="录入人" align="center" prop="sysNickName" width="100"/>

      <el-table-column label="录入时间" align="center" prop="createTime" width="100">

        <template slot-scope="scope">

          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>

        </template>

      </el-table-column>
<!--      <el-table-column prop="importFlag" label="12123" :formatter="importFormat" width="80" align="center"></el-table-column>-->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">

        <template slot-scope="scope">

          <el-button

            size="mini"

            type="text"

            icon="el-icon-edit"

            @click="handleUpdate(scope.row)"



          >修改
          </el-button>

          <el-button

            size="mini"

            type="text"

            icon="el-icon-delete"

            @click="handleDelete(scope.row)"


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


    <!-- 添加或修改【检测预约信息】对话框 -->

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>

      <el-form ref="form" :model="form" :rules="rules" label-width="100px">




        <el-form-item label="车牌" prop="carNumber">

          <el-input v-model="form.carNumber" placeholder="请输入车牌"/>

        </el-form-item>

        <el-form-item   label="预约时间" prop="subscribeTime">

          <el-date-picker clearable size="small" style="width: 200px"

                          v-model="form.subscribeTime"

                          type="date"

                          value-format="yyyy-MM-dd"

                          placeholder="选择预约时间"
                          @change="subscribeSum"

          >

          </el-date-picker>
          <el-col  v-show="showTodaySubscribeSum" >该日已电话预约{{todaySubscribeSum}}人</el-col>


        </el-form-item>

<!--        <el-form-item    label="今电话预约" prop="4"   v-show="showTodaySubscribeSum"   >-->

<!--          <el-col>{{todaySubscribeSum}}人</el-col>-->
<!--        </el-form-item>-->


        <el-form-item label="预约时段">
          <el-select v-model="form.subscribeTimePhase" placeholder="请选择">
            <el-option
              v-for="dict in timePhases"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"

            ></el-option>
          </el-select>
        </el-form-item>


        <el-form-item label="姓名" prop="subscribeName">

          <el-input     v-model="form.subscribeName" placeholder="请输入预约者姓名"/>

        </el-form-item>

        <el-form-item label="电话" prop="subscribePhone">

          <el-input v-model="form.subscribePhone" placeholder="请输入预约者电话"/>

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
                <div class="el-upload__tip" slot="tip">
<!--                  <el-link type="info" style="font-size:12px" @click="importTemplate">下载模板</el-link>-->
                  <a href="https://songling.oss-cn-beijing.aliyuncs.com/car/subscribe_template.xlsx">下载模板</a>
                </div>
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

  import {listSubscribe, getSubscribe, delSubscribe, addSubscribe, updateSubscribe,importTemplate,exportSubscribe,getTodaySubscribeSum} from "@/api/carsubscribe/subscribe";
  import { getToken } from "@/utils/auth";

  export default {

    name: "Subscribe",

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

        // 【检测预约信息】表格数据

        subscribeList: [],

        // 弹出层标题

        title: "",

        // 是否显示弹出层

        open: false,

        // 查询参数

        queryParams: {

          pageNum: 1,

          pageSize: 10,

          carNumber: undefined,

          subscribeTime: undefined,

          subscribeCode: undefined,

          subscribePhone: undefined,

          subscribeName: undefined,

          subscribeStatus: undefined,

          subscribeTimePhase: undefined,
          importFlag: undefined,
          sysNickName:undefined

        },

        upload: {
          open: false,
          title: "",
          // 是否禁用上传
          isUploading: false,
          // 是否更新已经存在的用户数据
          updateSupport: 0,
          // 设置上传的请求头部
          headers: { Authorization: "Bearer " + getToken() },
          // 上传的地址
          url: process.env.VUE_APP_BASE_API + "/car/carSubscribe/importData"
        },
        // 日期范围
        dateRange: [],
        // 表单参数
        statusOptions: [{dictValue:0,dictLabel:'正常'},{dictValue:1,dictLabel:'取消'}],

        importOptions: [{dictValue:0,dictLabel:'否'},{dictValue:1,dictLabel:'是'}],
        timePhases:[{dictValue:'08:00--09:00',dictLabel:'08:00--09:00'},{dictValue:'09:00--10:00',dictLabel:'09:00--10:00'},{dictValue:'10:00--11:00',dictLabel:'10:00--11:00'},{dictValue:'13:00--14:00',dictLabel:'13:00--14:00'},{dictValue:'14:00--15:00',dictLabel:'14:00--15:00'},{dictValue:'15:00--16:00',dictLabel:'15:00--16:00'},{dictValue:'16:00--17:00',dictLabel:'16:00--17:00'}],
        form: {},
        showTodaySubscribeSum:false,
        todaySubscribeSum:0,
        // 表单校验

        rules: {
          carNumber: [

            { required: true, message: "车牌不能为空", trigger: "blur" },

            {pattern:/^([京津晋冀蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼渝川贵云藏陕甘青宁新][ABCDEFGHJKLMNPQRSTUVWXY][1-9DF][1-9ABCDEFGHJKLMNPQRSTUVWXYZ]\d{3}[1-9DF]|[京津晋冀蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼渝川贵云藏陕甘青宁新][ABCDEFGHJKLMNPQRSTUVWXY][\dABCDEFGHJKLNMxPQRSTUVWXYZ警]{5,6})$/,
              message: '常规格式：京QZ68W0'},


          ],
          subscribeTime: [

            {required: true, message: "预约時間不能为空", trigger: "blur"}

          ],
          subscribeName: [

            {required: true, message: "姓名不能为空", trigger: "blur"}

          ]


        }

      };

    },

    created() {

      this.getList();

    },

    methods: {

      subscribeSum(e){
        console.log("值",e)
        this.showTodaySubscribeSum = true;
        getTodaySubscribeSum(e).then(response => {//今天所有电话预约数量
          console.log("获取中", response)

          this.todaySubscribeSum =response.msg;

        });
      },
      getList() {

        this.loading = true;
        listSubscribe(this.addDateRange(this.queryParams, this.dateRange)).then(response => {

          this.subscribeList = response.rows;

          this.total = response.total;

          this.loading = false;

        });

      },

      // 取消按钮

      cancel() {

        this.open = false;
        this.showTodaySubscribeSum=false
        this.reset();

      },
      statusFormat(row, column) {
        if (row.menuType == "F") {
          return "";
        }
        return this.selectDictLabel(this.statusOptions, row.subscribeStatus);
      },

      importFormat(row, column) {
        if (row.menuType == "F") {
          return "";
        }
        return this.selectDictLabel(this.importOptions, row.importFlag);
      },
      // 表单重置

      reset() {

        this.form = {

          subscribeId: undefined,

          carNumber: '京',

          createTime: undefined,

          subscribeTime: undefined,

          subscribeCode: undefined,


          subscribePhone: undefined,

          subscribeName: undefined,

          subscribeStatus: "0",

          subscribeTimePhase: '16:00--17:00',

        };

        this.resetForm("form");

      },


      handleQuery() {

        this.queryParams.pageNum = 1;

        this.getList();

      },

      /** 重置按钮操作 */

      resetQuery() {
        this.dateRange = [];
        this.resetForm("queryForm");

        this.handleQuery();

      },

      // 多选框选中数据

      handleSelectionChange(selection) {

        this.ids = selection.map(item => item.subscribeId)

        this.single = selection.length != 1

        this.multiple = !selection.length

      },

      /** 新增按钮操作 */

      handleAdd() {

        this.reset();

        this.open = true;
        this.showTodaySubscribeSum=false
        this.title = "添加【检测预约信息】";

      },



      /** 修改按钮操作 */

      handleUpdate(row) {

        this.reset();
        this.showTodaySubscribeSum=false
        const subscribeId = row.subscribeId || this.ids

        getSubscribe(subscribeId).then(response => {
          console.log("获取中", response.data)
          this.form = response.data;

          this.open = true;
          this.showTodaySubscribeSum = false;
          this.title = "修改【检测预约信息】";

        });

      },

      /** 提交按钮 */

      submitForm: function () {

        this.$refs["form"].validate(valid => {

          if (valid) {

            if (this.form.subscribeId != undefined) {

              updateSubscribe(this.form).then(response => {

                if (response.code === 200) {

                  this.msgSuccess("修改成功");

                  this.open = false;
                  this.showTodaySubscribeSum = false;
                  this.getList();

                }

              });

            } else {

              addSubscribe(this.form).then(response => {

                if (response.code === 200) {

                  this.msgSuccess("新增成功");

                  this.open = false;
                  this.showTodaySubscribeSum = false;
                  this.getList();

                }

              });

            }

          }

        });

      },

      /** 删除按钮操作 */

      handleDelete(row) {

        const subscribeIds = row.subscribeId || this.ids;
        console.log("要删除的", subscribeIds)
        this.$confirm('是否确认删除【检测预约信息】?', "警告", {
          //this.$confirm('是否确认删除【检测预约信息】编号为"' + subscribeIds + '"的数据项?', "警告", {

          confirmButtonText: "确定",

          cancelButtonText: "取消",

          type: "warning"

        }).then(function () {

          return delSubscribe(subscribeIds);

        }).then(() => {

          this.getList();

          this.msgSuccess("删除成功");

        }).catch(function () {
        });

      },

      handleExport(){
        const queryParams = this.queryParams;
        this.$confirm('是否确认导出预约数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportSubscribe(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
      },
      handleImport() {
        this.upload.title = "12123数据导入";
        this.upload.open = true;
      },
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
      /** 下载模板操作 */
      importTemplate() {
        importTemplate().then(response => {
          this.download(response.msg);
        });
      },

    }

  };

</script>
