<!--服务驱动管理界面-->
<template>
  <div>
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">服务驱动管理</h2>
        </div>
        <div class="fr">
          <el-button class="more-search" @click="showMoreSearch">
            更多查询 <svg-icon icon-class="spread" :class="{'retract':moreSearch}"></svg-icon>
          </el-button>
        </div>
        <div class="search fr">
          <input class="do-search-input" type="text" style="padding-left: 15px; width: 200px; outline: none; border: 0; font-size: 12px; display: inline-block; height: 100%; float: left;" v-model="query.name" placeholder="请输入关键字"></input>
          <a href="javascript:;" @click="fetchDataDriveTable()"><svg-icon icon-class="search"></svg-icon></a>
        </div>
        <div class="fr">
          <el-button class="add-btn" type="primary" @click="handleCreate" :disabled="!operatePermission"><i>+</i>新增</el-button>
        </div>
      </div>
      <!-- <el-collapse-transition> -->
        <div class="clearfix more-search-box">
          <el-form ref="form" :model="searchForm">
            <div class="clearfix input-group">
              <el-form-item label="驱动名称" style="width: 226px;" class="fl" label-width="80px">
                <el-input v-model="searchForm.driverName" size="small"></el-input>
              </el-form-item>
              <el-form-item label="驱动版本" style="width: 226px;" class="fl" label-width="80px">
                <el-input v-model="searchForm.version" size="small"></el-input>
              </el-form-item>
              <el-form-item label="状态" style="width: 196px;" class="fl" label-width="50px">
                <el-select v-model="searchForm.statusCode" clearable placeholder="" size="small">
                  <el-option :label="item.displayName" :value="item.code" v-for="(item, index) in statusCodeList" :key="index"></el-option>
                </el-select>
              </el-form-item>
            </div>
            <div class="btn-group">
              <el-button type="primary" size="mini" @click="preciseQuery()">查询</el-button>
              <el-button size="mini" style="margin-left: 10px"  @click.stop="resetQuery">重置</el-button>
            </div>
          </el-form>
        </div>
      <!-- </el-collapse-transition> -->
    </nav>
    <section class="components-container clearfix section-bod">
      <!-- <el-table
        ref="multipleTable"
        :data="tableData.data"
        tooltip-effect="dark"
        style="width: 100%"
        size="small"
        :header-row-class-name="titleBgColor">
        <el-table-column prop="driverName" label="驱动名称" show-overflow-tooltip width="200">
        </el-table-column>
        <el-table-column prop="version" label="驱动版本" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="状态" show-overflow-tooltip>
          <template slot-scope="scope">{{ scope.row.statusCode | stateTransition}}</template>
        </el-table-column>
        <el-table-column prop="updateBy" show-overflow-tooltip label="更新用户">
        </el-table-column>
        <el-table-column label="更新时间">
          <template slot-scope="scope">{{ scope.row.updateTime }}</template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <a title="编辑" @click="handleUpdate(scope.row)"><svg-icon icon-class="editor" style="width: 20px; height: 20px"></svg-icon></a>
            <a title="查看" @click="handleView(scope.row)"><svg-icon icon-class="eye" style="width: 20px; height: 20px"></svg-icon></a>
            <a title="禁用" v-if="scope.row.statusCode == 'enabled'" @click="disableEnabledData(scope.row.id, 'disabled')"><svg-icon icon-class="stop" style="width: 22px; height: 22px"></svg-icon></a>
            <a title="启用" v-if="scope.row.statusCode == 'disabled'" @click="disableEnabledData(scope.row.id, 'enabled')"><svg-icon icon-class="display" style="width: 20px; height: 20px"></svg-icon></a>
            <a title="删除" @click="deleteSource(scope.row.id)"><svg-icon icon-class="delete" style="width: 20px; height: 20px"></svg-icon></a>
          </template>
        </el-table-column>
      </el-table> -->
      <cr-loading v-show="loading"></cr-loading>

      <list-card
        v-show="!loading"
        :operatePermission="operatePermission"
        :listData="tableData.data"
        :fieldsData="fieldsData"
        cardWidth="48%"
        cardImg="service01.png"
        @edit="handleUpdate"
        @delete="deleteSource"
        @browse="handleView"
        @handleSwitchClick="disableEnabledData"
      ></list-card>
      <el-pagination
        background
        @current-change="(current)=>{this.loading=true;this.tableData.data=[];searchForm.pageNo = current;this.fetchDataDriveTable('2')}"
        :page-size="parseInt(searchForm.pageSize)"
        :current-page="parseInt(searchForm.pageNo)"
        layout="prev, pager, next"
        :total="tableData.page"
        class="fr mt-10">
      </el-pagination>
    </section>
    <!--新增与修改-->
    <!-- <el-dialog
      :title="textMap[dialogStatus]"
      :visible.sync="addDialog"
      width="50%"
      @close='cancelSource(driveModelForm)'>
      <div>
        <el-form :model="driveModelForm" ref="driveModelForm" :rules="driveModelRule" label-width="110px" class="form-body">
          <el-collapse v-model="activeNames">
            <el-collapse-item title="基本属性" name="1">
              <el-form-item label="驱动名称：" prop="driverName" class="body-input mt-10" >
                <el-input v-model="driveModelForm.driverName" :disabled="dialogStatus == 'update' || dialogStatus == 'view'" @blur="checkName(driveModelForm.driverName)"></el-input>
              </el-form-item>
              <el-form-item label="驱动版本：" prop="version" class="body-input mt-20">
                <el-input v-model="driveModelForm.version" :disabled="dialogStatus == 'update' || dialogStatus == 'view'"></el-input>
              </el-form-item>
              <el-form-item label="jar上传：" class="mt-20" required v-show="dialogStatus == 'create' || dialogStatus == 'update' ">
                <input ref="pathClear"  type="file" @change="getFile($event)">
                <span style="color: #f56c6c; font-size: 12px;" v-show="fileRequired">请选择文件</span>
              </el-form-item>
              <el-form-item label="主类入口：" prop="driverClass" class="body-input mt-10">
                <el-input v-model="driveModelForm.driverClass" :disabled="dialogStatus == 'view'"></el-input>
              </el-form-item>
              <el-form-item label="状态：" prop="statusCode" class="body-input mt-10">
                <el-checkbox v-model="driveModelForm.statusCode" true-label="enabled" false-label="disabled" :disabled="true">启动</el-checkbox>
              </el-form-item>
              <el-form-item label="描述：" prop="driverDesc" class="body-input mt-10">
                <el-input type="textarea" v-model="driveModelForm.driverDesc" :disabled="dialogStatus == 'view'"></el-input>
              </el-form-item>
            </el-collapse-item>
            <el-collapse-item title="参数配置" name="2" class="mt-12">
              <template>
                <el-form label-position="top">
                  <div v-for="(item, index) in driveModelForm.paramConfigList" class="body-div">
                    <el-form-item :label="item.displayName+':'" style="width: 90%; display: inline-block;">
                      <el-input v-model="driveModelForm.paramConfigList[index].paramValue" disabled></el-input>
                    </el-form-item>
                    <a title="删除" class="fr" @click="deleteCustomParam(item, index)" v-show="dialogStatus == 'update' || dialogStatus == 'create'"><svg-icon icon-class="delete" style="width: 20px; height: 20px;margin-top: 50px;"></svg-icon></a>
                    <a title="编辑" class="fr" @click="editorCustomParam(item, index)" v-show="dialogStatus == 'update' || dialogStatus == 'create'"><svg-icon icon-class="editor" style="width: 20px; height: 20px; margin-top: 50px;"></svg-icon></a>
                  </div>
                </el-form>
              </template>
              <div class="text-r mt-12" style="border: none; margin-bottom:-20px;" v-show="dialogStatus == 'update' || dialogStatus == 'create'">
                <el-button type="success" size="mini" @click="handleCustomParam">添加参数</el-button>
              </div>
            </el-collapse-item>
          </el-collapse>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button v-if="dialogStatus=='create'" type="primary" size="mini" @click="creatSource(driveModelForm)">保存</el-button>
        <el-button v-else-if="dialogStatus=='update'" type="primary" size="mini" @click="editorSource(driveModelForm)">保存</el-button>
        <el-button size="mini" @click="cancelSource(driveModelForm)">取消</el-button>
      </span>
    </el-dialog> -->
    <!-- 二级弹框-自定义参数 -->
    <!-- <el-dialog
      title="自定义参数编辑"
      :visible.sync="editParam"
      width="40%"
      @close="cancelSecondSource(paramEditForm)">
      <el-form :model="paramEditForm" ref="paramEditForm" :rules="paramEditRule" label-width="124px" style="padding: 10px 30px;">
        <el-form-item label="参数名称" prop="paramName">
          <el-input v-model="paramEditForm.paramName"></el-input>
        </el-form-item>
        <el-form-item label="显示名称：" class="mt-15" prop="displayName">
          <el-input v-model="paramEditForm.displayName"></el-input>
        </el-form-item>
        <el-form-item label="默认值：" class="mt-15">
          <el-input v-model="paramEditForm.defaultValue"></el-input>
        </el-form-item>
        <el-form-item label="校验正则表达式：" class="mt-12">
          <el-input v-model="paramEditForm.regularExpress"></el-input>
        </el-form-item>
        <el-form-item label="是否必填：" class="mt-12">
          <el-checkbox v-model="paramEditForm.manda" true-label="y" false-label="n"></el-checkbox>
        </el-form-item>
        <el-form-item label="描述：" class="mt-12">
          <el-input v-model="paramEditForm.des"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button v-show="secondDialogStatus == 'create'" type="primary" @click="creatParam(paramEditForm)">创建</el-button>
        <el-button v-show="secondDialogStatus == 'update'" type="primary" @click="editorParam(paramEditForm)">修改</el-button>
        <el-button @click="editParam = false">取消</el-button>
      </span>
    </el-dialog> -->
    <slide-box
      slideWidth="600px"
      :slideShow="addDialog"
      @close="slideClose"
      :title="textMap[dialogStatus]"
      :subSlide="true"
      :subSlideShow="editParam"
      @subClose="slideSubClose"
      subTitle="自定义参数编辑"
      ref="slideBox">

      <el-form slot="slide-content" :model="driveModelForm" ref="driveModelForm" :rules="driveModelRule" label-width="94px" class="form-body">
        <el-collapse v-model="activeNames">
          <el-collapse-item title="基本属性" name="1">
            <el-form-item label="驱动名称：" prop="driverName" class="mt-10 wd90" >
              <el-input size="medium" v-model="driveModelForm.driverName" :disabled="dialogStatus == 'update' || dialogStatus == 'view'" @blur="checkName(driveModelForm.driverName)"></el-input>
            </el-form-item>
            <el-form-item label="驱动版本：" prop="version" class="mt-10 wd90">
              <el-input size="medium" v-model="driveModelForm.version" :disabled="dialogStatus == 'update' || dialogStatus == 'view'"></el-input>
            </el-form-item>
            <el-form-item label="jar上传：" class="mt-10" required v-show="dialogStatus == 'create' || dialogStatus == 'update'">
              <input style="float:left;margin-top:8px;width:200px;" ref="pathClear" type="file" @change="getFile($event)">
              <!-- <span style="color: #f56c6c; font-size: 12px; margin-left: 6px;float:left;" v-show="fileRequired">请选择文件</span> -->
              <cr-help-popver styleText="float:left;display:flex;margin-top:9px">
                <div slot="popver-content">
                  如需新建驱动、您可以到
                  <a href="./dsp-help/dsp-help.html#wow78" target="_blank">
                    <el-button type="text" style="padding:0;">驱动开发</el-button>
                  </a>
                  帮助开发新驱动
                </div>
              </cr-help-popver>
            </el-form-item>
            <!--<el-form-item label="驱动包路径：" prop="driverPath" class="mt-20">
              <el-input v-model="driveModelForm.driverPath" name="pic"></el-input>
            </el-form-item>-->
            <el-form-item label="主类入口：" prop="driverClass" class="mt-10 wd90">
              <el-input size="medium" v-model="driveModelForm.driverClass" :disabled="dialogStatus == 'view'"></el-input>
              <cr-help-popver styleText="position:absolute;right:-20px;top:12px;" popverWidth="400">
                <div slot="popver-content">
                  主类入口为驱动中实现ResourceSessionFactory接口的类，写法如<br/>com.stackstech.dcp.driver.Mysql.MysqlSessionFactory，您可以链接到
                  <a href="./dsp-help/dsp-help.html#wow78" target="_blank">
                    <el-button type="text" style="padding:0">驱动开发</el-button>
                  </a>
                  查看
                </div>
              </cr-help-popver>
            </el-form-item>
            <el-form-item label="状态：" prop="statusCode" class="mt-10" style="color: #606266">
              <span v-if=" driveModelForm.statusCode == 'enabled' ">启用</span>
              <span v-else-if=" driveModelForm.statusCode == 'disabled' ">禁用</span>
            </el-form-item>
            <el-form-item label="描述：" prop="driverDesc" class="mt-10 wd90">
              <el-input size="medium" type="textarea" v-model="driveModelForm.driverDesc" :disabled="dialogStatus == 'view'"></el-input>
            </el-form-item>
          </el-collapse-item>
          <el-collapse-item name="2">
            <template slot="title">
              参数配置
              <cr-help-popver styleText="position:relative;top:2px;">
                <div slot="popver-content">
                  请查看对应
                  <a href="./dsp-help/dsp-help.html#wow68" target="_blank">
                    <el-button type="text" style="padding:0">驱动</el-button>
                  </a>
                  的参数配置
                </div>
              </cr-help-popver>
            </template>
            <template>
              <el-form label-width="94px">
                <div v-for="(item, index) in driveModelForm.paramConfigList" class="mt-15" :key="index">
                  <el-form-item :label="item.displayName+':'" style="width: 90%; display: inline-block;">
                    <el-input :type="inputType(item.displayName)" size="medium" v-model="driveModelForm.paramConfigList[index].paramValue" disabled></el-input>
                  </el-form-item>
                  <a title="删除" class="fr" @click="deleteCustomParam(item, index)" v-show="dialogStatus == 'update' || dialogStatus == 'create'"><img src="../../assets/tdspic/delete.png" style="width: 20px; height: 20px; margin-top: 10px;" alt="删除"></a>
                  <a title="编辑" style="margin-right: 4px" class="fr" @click="editorCustomParam(item, index)" v-show="dialogStatus == 'update' || dialogStatus == 'create'"><img src="../../assets/tdspic/edit.png" style="width: 20px; height: 20px; margin-top: 10px;" alt="编辑"></a>
                </div>
              </el-form>
            </template>
            <div class="text-r mt-12" style="border: none; margin-bottom:-20px;" v-show="dialogStatus == 'update' || dialogStatus == 'create'">
              <el-button type="primary" size="mini" @click="handleCustomParam">添加参数</el-button>
            </div>
          </el-collapse-item>
        </el-collapse>
      </el-form>
      <span slot="slide-footer">
        <el-button :disabled="createdButtonDis"  v-if="dialogStatus=='create'" type="primary" size="mini" @click="creatSource(driveModelForm)">保存</el-button>
        <el-button :disabled="updateButtonDis" v-else-if="dialogStatus=='update'" type="primary" size="mini" @click="editorSource(driveModelForm)">保存</el-button>
        <el-button size="mini" @click="closeSlide">取消</el-button>
      </span>
      <!-- 二级弹框 -->
      <el-form slot="sub-slide-content" :model="paramEditForm" ref="paramEditForm" :rules="paramEditRule" label-width="124px" style="padding: 10px 60px 10px 10px;">
        <el-form-item label="参数名称" prop="paramName">
          <el-input size="medium" v-model="paramEditForm.paramName"></el-input>
        </el-form-item>
        <el-form-item label="显示名称：" class="mt-15" prop="displayName">
          <el-input size="medium" v-model="paramEditForm.displayName"></el-input>
        </el-form-item>
        <el-form-item label="默认值：" class="mt-15">
          <el-input size="medium" v-model="paramEditForm.defaultValue"></el-input>
        </el-form-item>
        <el-form-item label="校验正则表达式：" class="mt-12">
          <el-input size="medium" v-model="paramEditForm.regularExpress"></el-input>
        </el-form-item>
        <el-form-item label="是否必填：" class="mt-12">
          <el-checkbox v-model="paramEditForm.manda" true-label="y" false-label="n"></el-checkbox>
        </el-form-item>
        <el-form-item label="描述：" class="mt-12">
          <el-input size="medium" type="textarea" v-model="paramEditForm.des"></el-input>
          <!-- <cr-help-popver styleText="position:absolute;right:-20px;top:12px;">
            <div slot="popver-content">
              参数描述
            </div>
          </cr-help-popver> -->
        </el-form-item>
      </el-form>
      <span slot="sub-slide-footer" class="dialog-footer">
        <el-button size="mini" v-show="secondDialogStatus == 'create'" type="primary" @click="creatParam(paramEditForm)">保存</el-button>
        <el-button size="mini" v-show="secondDialogStatus == 'update'" type="primary" @click="editorParam(paramEditForm)">保存</el-button>
        <el-button size="mini" @click="closeSubSlide">取消</el-button>
      </span>
    </slide-box>
  </div>
</template>

<script>
import request from '@/utils/request'
import ListCard from '@/components/ListCard'
import SlideBox from '@/components/SlideBox'
import CrHelpPopver from '@/components/CrHelpPopver'

import CrLoading from '@/components/CrLoading'

export default {
  components: {
    ListCard,
    SlideBox,
    CrLoading,
    CrHelpPopver
  },
  data() {
    return {
      operatePermission: false,
      submitBtnDisabled: false,
      loading: true,
      /* 查询 */
      query: {
        name: ''
      },  // 全局
      searchForm: {
        driverName: null,
        version: null,
        statusCode: null,
        pageNo: '1',
        pageSize: '10',
      },
      fieldsData:[
        {
          propsTitle: '驱动名称',
          field: 'driverName',
          fontFamily: 'SourceHanSansCN-Bold',
          toolTip: true,
        },
        {
          propsTitle: '驱动版本',
          field: 'version',
          fontFamily: 'SourceHanSansCN-Bold'
        },
        {
          propsTitle: '状态',
          field: 'statusCode',
          switch: true
        }
      ],
      moreSearch: false,
      statusCodeList: [], // 查询状态码
      /* 服务驱动表格 */
      tableData: {
        data: [],
        page: null
      },
      /* 表格必填项 */
      driveModelRule: {
        driverName: [{ required: true, message: '请输入驱动名称' }],
        version: [{ required: true, message: '请输入版本号' }],
        driverPath: [{ required: true, message: '请输入驱动包路径' }],
        driverClass: [{ required: true, message: '请输入主类入口' }]
      },
      fileRequired: false,
      multipleSelection: [], // 全选
      /* 弹框配置数据 */
      addDialog: false, // 新增弹框
      activeNames: ['1'], // 折叠面板默认展开
      file: null,
      driveModelForm:{
        driverName: '',
        version: '',
        driverPath: '',
        driverClass: '',
        statusCode: 'enabled',
        driverDesc: '',
        paramConfigList: []
      },
      /* 编辑与新增的分别 */
      textMap: {
        update: '编辑',
        create: '新增',
        view: '详细信息'
      },
      dialogStatus: '',
      /* 二级弹框（参数编辑） */
      editParam: false,
      deleteIndex: '1',
      paramEditForm: {
        paramName: '',
        displayName: '',
        defaultValue:'',
        regularExpress: '',
        manda: 'n',
        des: ''
      },
      paramEditRule: {
        paramName: [{ required: true, message: '请输入参数名称', trigger: 'blur' }],
        displayName: [{ required: true, message: '请输入显示名称', trigger: 'blur' }]
      },
      secondDialogStatus: '',
      // 驱动名称查重
      cheackName: true,
      /* 新增编辑按钮禁用 */
      createdButtonDis: false,
      updateButtonDis: false
    }
  },
  created() {
    this.fetchDataDriveTable();
    this.fetchStatusCode();
  },
  mounted() {
    this.enterKeyEv()
    this.$store.getters.userInfo.menu.forEach(item => {
      item.children.forEach(subItem => {
        if(subItem.name === '服务驱动管理'){
          this.operatePermission = subItem.operationIds.indexOf('3') !== -1
          console.log(subItem.name+'页面操作权限',this.operatePermission)
        }
      })
    })
  },
  beforeDestroy() {
    $(document).off('keyup')
  },
  methods:{
    showMoreSearch(){
      const self = this;
      if(!this.moreSearch){

        $('.more-search-box').stop().animate({'height':$('.more-search-box .el-form').height()+'px','margin-top':'15px'},300)
      }else{

        $('.more-search-box').stop().animate({'height':'0','margin-top':'0'},300)
      }
      this.moreSearch = !this.moreSearch
    },
    // 关闭滑框
    closeSlide(){
      this.cancelSource()
      this.$refs.slideBox.close()
    },
    slideClose(){
      this.cancelSource()
      this.addDialog = false
      this.editParam = false
    },

    closeSubSlide(){
      this.cancelSecondSource()
      this.$refs.slideBox.subClose()
    },
    slideSubClose(){
      this.cancelSecondSource()
      this.editParam = false
    },
    /****** 查询重置、获取状态码start*******/
    fetchStatusCode(){
      request({
        url: '/api/service/model/query_status',
        method: 'get',
        params: {
          type: 'driver_status_code'
        }
      }).then(({ data }) => {
        this.statusCodeList = data.data
      })
    },
    resetQuery(){
      this.searchForm = {
        driverName: null,
        version: null,
        statusCode: null,
        pageNo: '1',
        pageSize: '10',
      }
    },
    //回车搜索
    enterKeyEv(){
      const self = this
      $(document).on('keyup',function(ev){
        // console.log(ev.keyCode === 13, $('#test-select').is(':focus'))
        // console.log($('#test-select').val())
        if(ev.keyCode === 13&&$('.do-search-input').is(':focus')){
          self.fetchDataDriveTable()
        }
      });
    },
    /****** 查询重置、获取状态码end*******/
    /******* 数据驱动列表获取start ********/
    fetchDataDriveTable(val) {
      this.loading = true;
      const obj = {}
      Object.keys(this.searchForm).forEach(key => {
        obj[key] = this.searchForm[key]
      })
      if(val==='1'){// 更多查询
        this.query.name = ''
        obj.pageNo = '1'
        this.searchForm.pageNo = '1'
        delete obj.queryString
        delete obj.queryType
      }else if(val==='2'){// 分页
        if(this.query.name === ''){ // 更多查询分页
          delete obj.queryString
          delete obj.queryType
        }else{// 模糊查询分页
          obj.queryString = this.query.name
          obj.queryType = 'public'
        }
      }else{// 模糊查询
        this.resetQuery()
        // 清空再赋值
        Object.keys(this.searchForm).forEach(key => {
          obj[key] = this.searchForm[key]
        })
        obj.queryString = this.query.name
        obj.queryType = 'public'
      }
      request({
        url: '/api/platform/servicedriver/query_servicedriver',
        method: 'get',
        params: obj
      }).then(({ data }) => {
        console.log(data)
        const self = this
        setTimeout(function(){
          self.loading = false
          if(data.code === 200){
            self.tableData.data = data.data.list
            self.tableData.page = parseInt(data.data.count)
          }else if(data.code === 4010){
            self.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
          }else{
            // this.$message.error(data.message)
          }
        },300)
      })
    },
    /******* 数据驱动列表获取end ********/

    /******* 新增start ********/
    // 新增弹框
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.addDialog = true
      if(this.$refs.pathClear){
        this.$refs.pathClear.value = ''
      }
    },
        /******* 二级弹框start ********/
      // 编辑自定义参数-创建-弹框
    handleCustomParam() {
      this.secondDialogStatus = 'create'
      this.resetSecondTemp()
      this.editParam = true;
    },
     // 编辑自定义参数-创建-确定函数
    creatParam(data){
      this.$refs.paramEditForm.validate(valid => {
        if(valid){
          this.deleteIndex += 1;
          this.driveModelForm.paramConfigList.push({
            paramName: data.paramName,
            paramValue: data.defaultValue,
            displayName: data.displayName,
            paramDesc: data.des,
            checkRegexp: data.regularExpress,
            isRequired: data.manda
          })
          this.editParam = false;
          this.closeSubSlide()
        }
      })
    },
      // 编辑自定义参数-编辑-弹框
    editorCustomParam(row, index){
      this.secondDialogStatus = 'update'
      this.editParam = true;
      this.paramEditForm = {
        objectId: row.objectId,
        id: row.id,
        seqNum: index,
        paramName: row.paramName,
        displayName: row.displayName,
        defaultValue: row.paramValue,
        regularExpress: row.checkRegexp,
        manda: row.isRequired,
        des: row.paramDesc
      }
    },
      // 编辑自定义参数-编辑-确定函数
    editorParam(data, index){
      let row = {
        objectId: data.objectId,
        id: data.id,
        paramName: data.paramName,
        paramValue: data.defaultValue,
        displayName: data.displayName,
        paramDesc: data.des,
        checkRegexp: data.regularExpress,
        isRequired: data.manda
      }
      this.driveModelForm.paramConfigList.splice(data.seqNum, 1, row)
      this.editParam = false;
      this.closeSubSlide()
    },
      // 编辑自定义参数-编辑添加
    editorAddCustomParam(){
      this.secondDialogStatus = 'update'
      this.editParam = true
    },
      // 删除-编辑自定义参数确定函数
    deleteCustomParam(row,index){
      let idData = {
        id: row.id
      }
      this.driveModelForm.paramConfigList.splice(index, 1)
      // 现不用调接口
      /* if(row.id){
        request({
          url: '/api/param/delete_param',
          method: 'post',
          data: idData
          }).then(({data}) => {
            if(data.code == 200){
              this.driveModelForm.paramConfigList.splice(index, 1)
              this.fetchDataDriveTable();
            } else{
              this.$message.error({
                message: data.message
              })
            }
          })
      }else{
        this.driveModelForm.paramConfigList.splice(index, 1)
      } */
    },
      /******* 二级弹框end ********/
    // file文件改变
    getFile(event){
      this.file = event.target.files[0];
    },
    // 新增确定
    creatSource(source) {
      event.preventDefault()
      if(this.cheackName == true){
        if(this.file){
          this.fileRequired = false;
          this.$refs.driveModelForm.validate(valid => {
            if(valid){
              let paramData = new FormData();
              paramData.append('file', this.file);
              paramData.append('driverName', source.driverName);
              paramData.append('version', source.version);
              paramData.append('driverDesc', source.driverDesc);
              paramData.append('driverPath', source.driverPath);
              paramData.append('driverClass', source.driverClass);
              paramData.append('statusCode', source.statusCode);
              for(var i=0; i<source.paramConfigList.length; i++){
                paramData.append('paramConfigList[' + i + "].paramName",source.paramConfigList[i].paramName)
                paramData.append('paramConfigList[' + i + "].paramValue",source.paramConfigList[i].paramValue)
                paramData.append('paramConfigList[' + i + "].paramDesc",source.paramConfigList[i].paramDesc)
                paramData.append('paramConfigList[' + i + "].displayName",source.paramConfigList[i].displayName)
                paramData.append('paramConfigList[' + i + "].checkRegexp",source.paramConfigList[i].checkRegexp)
                paramData.append('paramConfigList[' + i + "].isRequired",source.paramConfigList[i].isRequired)
              }
              this.createdButtonDis = true
              request({
                url: '/api/platform/servicedriver/add_servicedriver',
                method: 'post',
                data: paramData,
                headers: {
                  'Content-Type': 'multipart/form-data'
                }
              }).then(({data}) => {
                if(data.code == 200){
                  this.$message.success('新增成功');
                  this.createdButtonDis = false
                  this.closeSlide()
                  this.fetchDataDriveTable();
                }else if(data.code === 4010){
                  this.$store.dispatch('LogOut').then(() => {
                    location.reload()
                  })
                } else{
                  this.createdButtonDis = false
                  /* this.$confirm(data.message, '提示', {
                    type: 'warning'
                  }) */
                  this.$message.error(data.message)
                }
              })
            }
          })
        }else {
          this.fileRequired = true;
        }
      }else {
        this.$message.error('该驱动名已经存在');
      }
    },
    /********* 新增end *******/

    /******* 编辑start ********/
    // 编辑弹框
    handleUpdate(row){
      this.addDialog = true;
      this.dialogStatus = 'update';
      this.file = '';
      this.resetTemp();
      // 重新发送请求 （因为存在前端的增删改，会影响paramConfigList）
      request({
        url: '/api/platform/servicedriver/get_servicedriver',
        method: 'get',
        params: {
          id: row.id
        }
      }).then(({ data }) => {
        if(data.code === 200){
          this.driveModelForm = data.data
          this.file = null
          if(this.$refs.pathClear){
            this.$refs.pathClear.value = ''
          }
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{
          this.updateButtonDis = false
          this.$message.error(data.message)
        }
      })
    },
    // 编辑确定
    editorSource(row) {
      this.$refs.driveModelForm.validate(valid => {
        if(valid){
          let paramData = new FormData();
          paramData.append('file', this.file);
          paramData.append('id', row.id);
          paramData.append('driverName', row.driverName);
          paramData.append('version', row.version);
          paramData.append('driverDesc', row.driverDesc);
          paramData.append('driverPath', row.driverPath);
          paramData.append('driverClass', row.driverClass);
          paramData.append('statusCode', row.statusCode);
          for(var i=0; i<row.paramConfigList.length; i++){
            paramData.append('paramConfigList[' + i + "].paramName",row.paramConfigList[i].paramName)
            paramData.append('paramConfigList[' + i + "].paramValue",row.paramConfigList[i].paramValue)
            paramData.append('paramConfigList[' + i + "].paramDesc",row.paramConfigList[i].paramDesc)
            paramData.append('paramConfigList[' + i + "].displayName",row.paramConfigList[i].displayName)
            paramData.append('paramConfigList[' + i + "].checkRegexp",row.paramConfigList[i].checkRegexp)
            paramData.append('paramConfigList[' + i + "].isRequired",row.paramConfigList[i].isRequired)
          }
          this.updateButtonDis = true
          request({
            url: '/api/platform/servicedriver/edit_servicedriver',
            method: 'post',
            data: paramData,
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          }).then(({data}) => {
            if(data.code == 200){
              this.updateButtonDis = false
              this.closeSlide()
              this.$message.success('编辑成功');
              this.fetchDataDriveTable();
            }else if(data.code === 4010){
              this.$store.dispatch('LogOut').then(() => {
                location.reload()
              })
            } else{
              this.updateButtonDis = false
              this.$message.error(data.message)
            }
          })
        }
      })
    },
    /********* 编辑end *******/
    // 取消新增或编辑
    cancelSource(){
      this.$refs.driveModelForm.resetFields()
    },
    // 取消二级弹框
    cancelSecondSource(){
      this.$refs.paramEditForm.resetFields()
    },
    /********* 删除、查看与查重 start *******/
    // 单个删除资源
    deleteSource(row) {
      let idData = [];
      idData.push(row);
      this.$confirm('确定删除此驱动？', '提示', {
        type: 'warning'
      }).then(() => {
        request({
          url: '/api/platform/servicedriver/del_servicedriver',
          method: 'post',
          data: idData
          }).then(({data}) => {
            if(data.code == 200){
              this.$message.success({
                message: '删除成功'
              });
              this.fetchDataDriveTable();
            }else if(data.code === 4010){
              this.$store.dispatch('LogOut').then(() => {
                location.reload()
              })
            } else{
              this.$message.error({
                message: data.message
              })
            }
          })
        }
      ).catch(() => {
      })
    },
    // 查看
    handleView(row){
      this.addDialog = true;
      this.dialogStatus = 'view';
      this.resetTemp();
      // 重新发送请求 （因为存在前端的增删改，会影响paramConfigList）
      request({
        url: '/api/platform/servicedriver/get_servicedriver',
        method: 'get',
        params: {
          id: row.id
        }
      }).then(({ data }) => {
        if(data.code === 200){
          this.driveModelForm = data.data
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
           location.reload()
         })
        }else{
          // this.$message.error(data.message)
        }
      })
    },
    // 驱动名称查重
    checkName(name){
      if(name && name != '' ){
        let param = {
          'driverName': name
        }
        request({
          url: '/api/platform/servicedriver/check_servicedriver',
          method: 'get',
          params: param
        }).then(({data}) => {
          if(data.code == 200){
            this.cheackName = true
          }else if(data.code === 4010){
            this.$store.dispatch('LogOut').then(() => {
              location.reload()
            })
          } else {
            this.cheackName = false
            this.$message.error(data.message)
          }
        })
      }
    },
     /********* 删除、查看与查重 end *******/
    // 启用与禁用 状态改变
    disableEnabledData(val){
      const oldState = !val.state;
      val.state = val.state?'enabled':'disabled'
      let param = {
        id: val.item.id,
        statusCode: val.state
      }
      this.tableData.data.forEach((item, index) => {
        if(item.id == val.item.id){
          item.statusCode = val.state
        }
      })
      request({
        url: '/api/platform/servicedriver/change_servicedriver_status',
        method: 'get',
        params: param
      }).then(({data}) => {
        if(data.code == 200){
            if(val.state === 'enabled'){
              this.$message.success({ message: '启动成功' });
            }else{
              this.$message.success({ message: '禁用成功' });
            }
            this.fetchDataDriveTable();
          }else if(data.code === 4010){
            this.$store.dispatch('LogOut').then(() => {
              location.reload()
            })
          } else {
            this.$message.error(data.message)
          }
      })
    },
    // 精确查询
    preciseQuery(){
      this.fetchDataDriveTable('1')
    },
    // 重置弹框内容
    resetTemp() {
      this.driveModelForm = {
        driverName: '',
        version: '',
        driverPath: '',
        driverClass: '',
        statusCode: 'enabled',
        driverDesc: '',
        paramConfigList: []
      }
      this.fileRequired = false;
      this.createdButtonDis = false;
      this.updateButtonDis = false;
    },
    // 重置二级弹框
    resetSecondTemp(){
      this.paramEditForm = {
        paramName: '',
        displayName: '',
        defaultValue:'',
        regularExpress: '',
        manda: 'n',
        des: ''
      }
    },
    // 密码为输入框的为密码
    inputType(val){
      if(val == '密码'){
        return 'password'
      }else{
        return 'text'
      }
    },
    // el-table的背景边框改变函数
    titleBgColor({row, rowIndex}){
      return 'title-bg-color'
    }
  },
  filters: {
    stateTransition: function(value){
      if(value){
        if(value == 'enabled'){
          return '启用'
        }else if(value == 'disabled'){
          return '禁用'
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.section-bod{
  background: transparent;
  border: none;
  box-shadow: none;
}
  .components-nav {
    padding-bottom: 15px;
    background: #f1f1f1;
    line-height: 28px;
    .titile {
      color: #444;
      font-size: 23px;
    }
  }
  .editor-content{
    margin-top: 20px;
  }
  .form-body {
    padding: 14px 30px;
    overflow:auto;
  }
  .form-header {
    padding: 0 30px 12px 12px;
    width: 100%
  }
</style>
<style lang="scss">
  .title-bg-color>th{
    background-color: rgba(246,247,250,1) !important;
  }
</style>
<style lang="scss">
.section-bod{
  text-align: center;
  .el-table{
    text-align: left;
  }
  .el-pagination.is-background{
    display: inline-block;
    float: none;
    .btn-prev,.btn-next{
      border: none;
    }
    .el-pager{
      .number{
        border: none;
        border-radius: 50%;
        &.active{
          background: #4562fc;
        }
        &.active:hover {
          color: white;
        }
        &:hover {
          color: #4562fc;
        }
      }
    }
  }
}
</style>