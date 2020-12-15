<!--数据源管理界面-->
<template>
  <div>
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">数据源管理</h2>
        </div>
        <div class="fr">
          <el-button class="more-search" @click="showMoreSearch">
            更多查询 <svg-icon icon-class="spread" :class="{'retract':moreSearch}"></svg-icon>
          </el-button>
        </div>
        <div class="search fr">
          <input class="do-search-input" type="text" style="padding-left: 15px; width: 200px; outline: none; border: 0; font-size: 12px; display: inline-block; height: 100%; float: left;" v-model="query.name" placeholder="请输入关键字"></input>
          <a href="javascript:;" @click="fetchDataSourceTable"><svg-icon icon-class="search"></svg-icon></a>
        </div>
        <div class="fr">
          <!-- <el-tooltip class="item" :disabled="operatePermission" effect="light" content="您无权限进行此操作，请联系管理员" placement="top">
            <span> -->
              <el-button class="add-btn" type="primary" @click="handleCreate" :disabled="!operatePermission"><i>+</i>新增</el-button>
            <!-- </span>
          </el-tooltip> -->
        </div>
      </div>
      <!-- 精确查询 -->
      <div class="clearfix more-search-box">
        <el-form ref="form" :model="searchForm">
          <div class="clearfix input-group">
            <el-form-item label="驱动名称" style="width: 226px;" class="fl" label-width="80px">
              <el-input v-model="searchForm.driverName" size="small"></el-input>
            </el-form-item>
            <el-form-item label="数据源名称" style="width: 236px;" class="fl" label-width="90px">
              <el-input v-model="searchForm.serviceSourceName" size="small"></el-input>
            </el-form-item>
            <el-form-item label="状态" style="width: 196px;" class="fl" label-width="50px">
              <el-select v-model="searchForm.statusCode" clearable placeholder="" size="small">
                <el-option :label="item.displayName" :value="item.code" v-for="(item, index) in statusCodeList" :key="index"></el-option>
              </el-select>
            </el-form-item>
          </div>
          <div class="btn-group">
            <el-button type="primary" size="mini" @click="preciseQuery()">查询</el-button>
            <el-button size="mini" style="margin-left: 10px" @click="resetQuery">重置</el-button>
          </div>
        </el-form>
      </div>
    </nav>
    <section class="components-container section-bod clearfix">
      <!-- <el-table
        ref="multipleTable"
        :data="tableData.data"
        tooltip-effect="dark"
        style="width: 100%"
        size="small"
        :header-row-class-name="titleBgColor">
        <el-table-column prop="serviceDriver.driverName" label="驱动名称" show-overflow-tooltip>
        </el-table-column>
        <el-table-column prop="serviceDriver.version" label="驱动版本" show-overflow-tooltip width="100">
        </el-table-column>
        <el-table-column prop="serviceSourceName" label="数据源名称" show-overflow-tooltip width="200">
        </el-table-column>
        <el-table-column label="状态" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.statusCode | stateTransition }}
          </template>
        </el-table-column>
        <el-table-column prop="maxConnections" show-overflow-tooltip label="最大连接数" >
        </el-table-column>
        <el-table-column prop="updateBy" label="更新用户" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="更新时间" show-overflow-tooltip>
          <template slot-scope="scope">{{ scope.row.updateTime }}</template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template slot-scope="scope">
            <a title="编辑" @click="handleUpdate(scope.row)"><svg-icon icon-class="editor" style="width: 20px; height: 20px"></svg-icon></a>
            <a title="查看" @click="handleView(scope.row)"><svg-icon icon-class="eye" style="width: 20px; height: 20px" ></svg-icon></a>
            <a title="删除" @click="deleteSource(scope.row.id)"><svg-icon icon-class="delete" style="width: 20px; height: 20px"></svg-icon></a>
          </template>
        </el-table-column>
      </el-table> -->
      <cr-loading v-show="loading"></cr-loading>
      <list-card
        v-show="!loading"
        :listData="tableData.data"
        :fieldsData="fieldsData"
        :operatePermission="operatePermission"
        @edit="handleUpdate"
        @delete="deleteSource"
        @browse="handleView"
        @handleSwitchClick="changeServiceState"
        cardImg="origin.png"
      ></list-card>
      <el-pagination
        background
        @current-change="(current)=>{loading = true;tableData.data=[];searchForm.pageNo = current;this.fetchDataSourceTable('2')}"
        :page-size="parseInt(searchForm.pageSize)"
        :current-page="parseInt(searchForm.pageNo)"
        layout="prev, pager, next"
        :total="tableData.page"
        class="mt-10">
      </el-pagination>

    </section>
    <!--新增与修改--><!--基本属性与参数配置-->
    <!-- <el-dialog
      :title="textMap[dialogStatus]"
      :visible.sync="addDialog"
      width="50%"
      @close='cancelSource(sourceForm)'>
      <div>
        <el-form :model="sourceForm" ref="sourceForm" :rules="sourceRule" label-width="100px" >
          <div class="form-body">
            <el-collapse v-model="activeNames">
              <el-collapse-item title="基本属性" name="1">
                <el-form-item label="驱动名称:" prop="driverId" class="mt-10">
                  <el-select v-model="sourceForm.driverId" placeholder="请选择驱动名称" @change="driveTypeChange(sourceForm.driverId)" size="medium" :disabled="dialogStatus == 'view'">
                    <el-option v-for="(item, index) in driveType" :key="index" :label="item.driverName" :value="item.id"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="驱动版本:" class="mt-15">
                  <el-input v-model="version" :disabled="true" size="medium" ></el-input>
                </el-form-item>
                <el-form-item label="数据源名称:" prop="serviceSourceName" class="mt-10">
                  <el-input v-model="sourceForm.serviceSourceName" size="medium" @blur="checkName(sourceForm.serviceSourceName)" :disabled="dialogStatus == 'view'"></el-input>
                </el-form-item>
                <el-form-item label="最大连接数:" prop="maxConnections" class="mt-15">
                  <el-input type="age" v-model.number="sourceForm.maxConnections" size="medium" :disabled="dialogStatus == 'view'"></el-input>
                </el-form-item>
                <el-form-item label="查询超时(s):" prop="queryTimeout" class="mt-15">
                  <el-input type="age" v-model.number="sourceForm.queryTimeout" size="medium" :disabled="dialogStatus == 'view'"></el-input>
                </el-form-item>
                <el-form-item label="连接超时(s):" prop="connTimeout" class="mt-15">
                  <el-input type="age" v-model.number="sourceForm.connTimeout" size="medium" :disabled="dialogStatus == 'view'"></el-input>
                </el-form-item>
                <el-form-item label="状态:" class="mt-15">
                  <el-checkbox v-model="sourceForm.statusCode" true-label="published" false-label="initialized" :disabled="true">发布</el-checkbox>
                </el-form-item>
                <el-form-item label="描述:" class="mt-10">
                  <el-input type="textarea" v-model="sourceForm.serviceSourceDesc" :disabled="dialogStatus == 'view'"></el-input>
                </el-form-item>
              </el-collapse-item>
              <el-collapse-item title="配置参数" name="2" class="mt-12">
                <el-form label-position="top">
                  <el-form-item v-for="(item, index) in sourceForm.paramConfigList" :key='index' :label="item.displayName+':'" class="mt-15" :required = "item.isRequired == 'y'">
                    <el-input v-model="sourceForm.paramConfigList[index].paramValue" size="medium" :disabled="dialogStatus == 'view'"></el-input>
                  </el-form-item>
                </el-form>
              </el-collapse-item>
            </el-collapse>
            <div class="mt-15">
              <span>连接状态：</span>
              <svg-icon icon-class="loading" style="width: 20px; height:20px;" v-if="testPic == 'loading'"></svg-icon>
              <svg-icon icon-class="checked" style="width: 20px; height:20px;" v-if="testPic == 'checked'"></svg-icon>
              <svg-icon icon-class="failure" style="width: 20px; height:20px;" v-if="testPic == 'failure'"></svg-icon>
              <el-button size="mini" :disabled="testPic == 'loading'" @click="test(sourceForm)">测试连接</el-button>
            </div>
          </div>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button v-if="dialogStatus=='create'" type="primary" size="mini" @click="creatSource(sourceForm)">保存</el-button>
        <el-button v-else-if="dialogStatus=='update'" type="primary" size="mini" @click="editorSource(sourceForm)">保存</el-button>
        <el-button size="mini" @click="addDialog = false">取消</el-button>
      </span>
    </el-dialog> -->
    <slide-box
      slideWidth="450px"
      :slideShow="addDialog"
      @close="slideClose"
      :title="textMap[dialogStatus]"
      :subSlide="true"
      :subSlideShow="editParam"
      @subClose="slideSubClose"
      subTitle="自定义参数编辑"
      ref="slideBox">
      <el-form :model="sourceForm" ref="sourceForm" :rules="sourceRule" label-width="100px" slot="slide-content" >
        <div class="form-body">
          <!--基本属性与参数配置-->
          <el-collapse v-model="activeNames">
            <el-collapse-item title="基本属性" name="1">
              <el-form-item label="驱动名称:" prop="driverId" class="mt-10">
                <el-select v-model="sourceForm.driverId" filterable placeholder="请选择驱动名称" @change="driveTypeChange(sourceForm.driverId)" size="medium" :disabled="dialogStatus == 'view' || dialogStatus == 'update'">
                  <el-option v-for="(item, index) in driveType" :key="index" :label="item.driverName" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="驱动版本:" class="mt-15">
                <el-input v-model="version" :disabled="true" size="medium" ></el-input>
              </el-form-item>
              <el-form-item label="数据源名称:" prop="serviceSourceName" class="mt-10">
                <el-input v-model="sourceForm.serviceSourceName" size="medium" @blur="checkName(sourceForm.serviceSourceName)" :disabled="dialogStatus == 'view' || dialogStatus == 'update'"></el-input>
              </el-form-item>
              <el-form-item label="最大连接数:" prop="maxConnections" class="mt-15">
                <el-input type="age" v-model.number="sourceForm.maxConnections" size="medium" :disabled="dialogStatus == 'view'"></el-input>
                <cr-help-popver styleText="position:absolute;right:-20px;top:12px;" popverWidth="300">
                  <div slot="popver-content">
                    连接数会均匀分配到实例上，链接数数量根据实际情况配置，建议40-80，查看均匀分配
                    <a href="./dsp-help/dsp-help.html#wow34" target="_blank">
                      <el-button type="text" style="padding:0;">详情</el-button>
                    </a>
                  </div>
                </cr-help-popver>
              </el-form-item>
              <!-- <el-form-item label="查询超时(s):" prop="queryTimeout" class="mt-15">
                <el-input type="age" v-model.number="sourceForm.queryTimeout" size="medium" :disabled="dialogStatus == 'view'"></el-input>
              </el-form-item>
              <el-form-item label="连接超时(s):" prop="connTimeout" class="mt-15">
                <el-input type="age" v-model.number="sourceForm.connTimeout" size="medium" :disabled="dialogStatus == 'view'"></el-input>
              </el-form-item> -->
              <el-form-item label="状态:" class="mt-15" style="color: #606266">
                <span v-if=" sourceForm.statusCode == 'enabled' ">启用</span>
                <span v-else-if=" sourceForm.statusCode == 'disabled' ">禁用</span>
                <!-- <el-checkbox v-model="sourceForm.statusCode" true-label="published" false-label="initialized" :disabled="true">发布</el-checkbox> -->
              </el-form-item>
              <el-form-item label="阶段:" class="mt-15" style="color: #606266">
                <span v-if=" sourceForm.stageCode == 'unpublished' " style="float:left">待发布</span>
                <span v-else-if=" sourceForm.stageCode == 'publishing' " style="float:left">发布中</span>
                <span v-else-if=" sourceForm.stageCode == 'published' " style="float:left">已发布</span>
                <span v-else-if=" sourceForm.stageCode == 'waiting' " style="float:left">待下线</span>
                <span v-else-if=" sourceForm.stageCode == 'stopping' " style="float:left">下线中</span>
                <span v-else-if=" sourceForm.stageCode == 'offline' " style="float:left">已下线</span>
                <cr-help-popver styleText="float:left;display:flex;margin:10px 10px">
                  <div slot="popver-content">
                    默认直接发布，阶段
                    <a href="./dsp-help/dsp-help.html#wow10" target="_blank">
                      <el-button type="text" style="padding:0;">详情</el-button>
                    </a>
                  </div>
                </cr-help-popver>
              </el-form-item>
              <el-form-item label="描述:" class="mt-10">
                <el-input type="textarea" v-model="sourceForm.serviceSourceDesc" :disabled="dialogStatus == 'view'"></el-input>
              </el-form-item>
            </el-collapse-item>
            <el-collapse-item title="配置参数" name="2">
              <template slot="title">
                配置参数
                <cr-help-popver styleText="position:relative;top:2px;">
                  <div slot="popver-content">
                    请查看对应
                    <a href="./dsp-help/dsp-help.html#wow68" target="_blank">
                      <el-button type="text" style="padding:0">数据源</el-button>
                    </a>
                    的参数配置
                  </div>
                </cr-help-popver>
              </template>
              <!-- <el-form :model="sourceParamForm" label-width="100px" :rules="driveParamsRule"> -->
                <el-form-item
                  v-for="(item, index) in sourceForm.paramConfigList"
                  :key='index'
                  :label="item.displayName+':'"
                  class="mt-15"
                  :required="item.isRequired == 'y'"
                  :prop="item.isRequired == 'y'?('requiredParam'+index):null">
                  <el-popover
                    placement="top"
                    width="300"
                    trigger="hover"
                    :disabled="item.paramDesc === ''">
                    <p>{{ item.paramDesc }}</p>
                    <el-input slot="reference" :type="inputType(item.displayName)" v-model="sourceForm['requiredParam'+index]" size="medium" :disabled="dialogStatus == 'view'"></el-input>
                  </el-popover>
                </el-form-item>
                <el-form-item v-if="dialogStatus == 'update' || dialogStatus == 'create'">
                  <el-button type="primary" size="mini" @click="addParamConfig" style="float:right">添加参数</el-button>
                </el-form-item>
              <!-- </el-form> -->
            </el-collapse-item>
          </el-collapse>
          <div class="mt-15">
            <span>连接状态：</span>
            <svg-icon icon-class="loading" style="width: 20px; height:20px;" v-if="testPic == 'loading'"></svg-icon>
            <svg-icon icon-class="checked" style="width: 20px; height:20px;" v-if="testPic == 'checked'"></svg-icon>
            <svg-icon icon-class="failure" style="width: 20px; height:20px;" v-if="testPic == 'failure'"></svg-icon>
            <el-button size="mini" :disabled="testPic == 'loading'" @click="test(sourceForm)">测试连接</el-button>
            <!--<span class="linktest" @click="test(sourceForm)">测试连接</span>-->
          </div>
        </div>
      </el-form>
      <!-- 二级弹框 -->
      <el-form slot="sub-slide-content" :model="paramEditForm" ref="paramEditForm" :rules="paramEditRule" label-width="124px" style="padding: 10px 30px;">
        <el-form-item label="参数名称：" prop="paramName">
          <el-input size="medium" v-model="paramEditForm.paramName"></el-input>
        </el-form-item>
        <el-form-item label="显示名称：" class="mt-15" prop="displayName">
          <el-input size="medium" v-model="paramEditForm.displayName"></el-input>
        </el-form-item>
        <el-form-item label="值：" class="mt-15">
          <el-input size="medium" v-model="paramEditForm.defaultValue"></el-input>
        </el-form-item>
        <!-- <el-form-item label="校验正则表达式：" class="mt-12">
          <el-input size="medium" v-model="paramEditForm.regularExpress"></el-input>
        </el-form-item>
        <el-form-item label="是否必填：" class="mt-12">
          <el-checkbox v-model="paramEditForm.manda" true-label="y" false-label="n"></el-checkbox>
        </el-form-item>
        <el-form-item label="描述：" class="mt-12">
          <el-input size="medium" v-model="paramEditForm.des"></el-input>
        </el-form-item> -->
      </el-form>
      <span slot="slide-footer">
        <el-button v-if="dialogStatus=='create'" :disabled="submitBtnDisabled" type="primary" size="mini" @click="creatSource(sourceForm)">保存</el-button>
        <el-button v-else-if="dialogStatus=='update'" :disabled="submitBtnDisabled" type="primary" size="mini" @click="editorSource(sourceForm)">保存</el-button>
        <el-button size="mini" @click="closeSlide">取消</el-button>
      </span>
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
import CrLoading from '@/components/CrLoading'
import CrHelpPopver from '@/components/CrHelpPopver'
import mapGetters from 'vuex'
export default {
  components: {
    ListCard,
    SlideBox,
    CrLoading,
    CrHelpPopver
  },
  data() {
    return {
      operatePermission: true,
      submitBtnDisabled: false,//防止重复提交
      loading: true,
      loadingTime: 300,
      /******** 表格搜索数据start ********/
      query: {
        name: ''
      },
      // 更多查询
      moreSearch: false,
      searchForm: {
        driveName: null,
        serviceSourceName: null,
        statusCode: null,
        pageNo: '1',
        pageSize: '5',
        queryType: 'public',
        queryString: ''
      },
      statusCodeList: [],
      /******** 表格搜索数据end ********/

      /******** table列表数据start ********/
      tableData: {
        data:[],
        page: null
      },
      fieldsData:[
        {
          propsTitle: '数据源名称',
          field: 'serviceSourceName'
        },
        {
          propsTitle: '驱动名称',
          field: 'driverName',
          fontFamily: 'SourceHanSansCN-Bold'
        },
        {
          propsTitle: '驱动版本',
          field: 'driverVersion'
        },

        {
          propsTitle: '状态',
          field: 'statusCode',
          switch: true
        },
        {
          propsTitle: '最大连接数',
          field: 'maxConnections'
        }
      ],
      sourceRule: {
        driverId: [{ required: true, message: '请输入驱动名称' }],
        serviceSourceName: [{ required: true, message: '请输入数据源名称' }],
        maxConnections: [
          { required: true, message: '请输入最大连接数'},
          { type: 'number', message: '最大连接数为数字值'}
        ]
        // queryTimeout: [
        //   { required: true, message: '请输入查询时间'},
        //   { type: 'number', message: '查询时间为数字值'}
        // ],
        // connTimeout: [
        //   { required: true, message: '请输入连接时间'},
        //   { type: 'number', message: '连接时间为数字值'}
        // ]
      },
      /******** table列表数据end ********/

      /******** 新增弹框数据start ********/
      addDialog: false, // 新增弹框
      editParam: false, // 二级滑框
      activeNames: ['1'], // 折叠面板默认展开
      version: '',  // 版本号
      sourceForm: {
        driverId: '',  // 驱动名称
        serviceSourceName: '', // 数据源名称
        serviceSourceDesc: '',  // 数据源描述
        statusCode: 'enabled', // 数据源状态
        stageCode: 'unpublished', // 数据源阶段
        maxConnections: null, // 最大连接数
        queryTimeout: '', // 查询超时时间
        connTimeout: '',  // 连接超时时间
        paramConfigList: [] // 配置参数
      },
      secondDialogStatus: '',
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
      sourceParamForm: {},
      driveParamsRule: {},
      driveType:[],  // 驱动类型
        // 编辑与新增的分别
      textMap: {
        update: '编辑',
        create: '新增',
        view: '详细信息'
      },
      dialogStatus: '',
      testPic: '',  // 测试图标
      nameRepeat: true,
      /******** 新增弹框数据end ********/
      moreSearchHeight: 0,
    }
  },
  // computed:{
  //   ...mapGetters([
  //     'userInfo'
  //   ])
  // },
  mounted(){
    this.fetchDataSourceTable()
    this.getDriveList()
    this.fetchStatusCode()
    this.enterKeyEv()
    this.$store.getters.userInfo.menu.forEach(item => {
      item.children.forEach(subItem => {
        if(subItem.name === '数据源管理'){
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
      this.$refs.slideBox.close()
      this.cancelSource()
    },
    slideClose(){
      this.addDialog = false
      this.editParam = false
      // console.log(this.sourceRule)
      this.sourceForm.paramConfigList.forEach((item,index) => {
        delete this.sourceForm['requiredParam'+index]
        delete this.sourceRule['requiredParam'+index]
      })
      this.cancelSource()
    },
    closeSubSlide(){
      this.cancelSecondSource()
      this.$refs.slideBox.subClose()
    },
    slideSubClose(){
      this.cancelSecondSource()
      this.editParam = false
    },
    // 取消二级弹框
    cancelSecondSource(){
      this.$refs.paramEditForm.resetFields()
    },
    /****** 查询重置、获取状态码start*******/
    fetchStatusCode(){
      request({
        url: '/api/service/model/query_status',
        method: 'get',
        params: {
          type: 'source_status_code'
        }
      }).then(({ data }) => {
        this.statusCodeList = data.data
      })
    },
    resetQuery(){
      this.searchForm = {
        driveName: null,
        serviceSourceName: null,
        statusCode: null,
        pageNo: '1',
        pageSize: '5',
      }
    },
    /****** 查询重置、获取状态码end*******/
    /******** 数据驱动start *********/
    // 获取数据源列表
    fetchDataSourceTable(val) {
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
        url: '/api/servicesource/query_servicesource',
        method: 'get',
        params: obj
      }).then(({ data }) => {
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
          }
        },300)
      })
    },
    // 获取数据驱动列表
    getDriveList() {
      request({
        url: '/api/platform/servicedriver/query_servicedriver',
        method: 'get',
        params: {
          statusCode: 'enabled'
        }
      }).then(({data}) => {
        if(data.code === 200){
          this.driveType = data.data.list
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
           location.reload()
         })
        }

      })
    },
    //回车搜索
    enterKeyEv(){
      const self = this
      $(document).on('keyup',function(ev){
        // console.log(ev.keyCode === 13, $('#test-select').is(':focus'))
        // console.log($('#test-select').val())
        if(ev.keyCode === 13&&$('.do-search-input').is(':focus')){
          self.fetchDataSourceTable()
        }
      });
    },
    // 添加参数
    addParamConfig(){
      this.resetSecondTemp()
      this.editParam = true;
    },
    // 编辑自定义参数-创建-确定函数
    creatParam(data){
      this.$refs.paramEditForm.validate(valid => {
        if(valid){
          // this.deleteIndex += 1;
          this.sourceForm.paramConfigList.push({
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
      this.sourceForm.paramConfigList.push(row)
      this.editParam = false;
      this.closeSubSlide()
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
    /******** 数据驱动end *********/

    /******** 新增编辑start *********/
    // 新增弹框
    handleCreate() {
      this.resetTemp()
      this.cancelSource()
      this.dialogStatus = 'create'
      this.secondDialogStatus = 'create'
      this.addDialog = true
    },
    // 驱动类型选择
    driveTypeChange(drive) {
      this.sourceForm.paramConfigList.forEach((item,index) => {
        delete this.sourceRule['requiredParam'+index]
      })
      if(this.activeNames.indexOf(this.activeNames.find(item => item === '2')) === -1){
        this.activeNames.push('2')
      }
      request({
        url: '/api/platform/servicedriver/get_servicedriver',
        method: 'get',
        params: {
          id: drive
        }
      }).then(({data}) => {
        console.log(data)
        this.version = data.data.version
        this.sourceForm.paramConfigList = data.data.paramConfigList
        this.sourceForm.paramConfigList.forEach((item,index) => {
          this.sourceForm['requiredParam'+index] = this.sourceForm.paramConfigList[index].paramValue
          if(item.isRequired === 'y'){
            this.sourceRule['requiredParam'+index] = [{required: true, message: '请填写必填项', trigger: 'blur'}]
          }
        })
      })
    },
    // 新增确定
    creatSource(source) {

      if(this.nameRepeat == true){
        this.$refs.sourceForm.validate(valid => {
          if(valid){
            if(this.testPic !== 'checked'){
              this.$message.error('请先完成正确测试！')
              return
            }
            const paramSource = {}
            $.extend(true,paramSource,source)
            paramSource.paramConfigList.forEach((item,index) => {
              item.paramValue = paramSource['requiredParam'+index]
              delete paramSource['requiredParam'+index]
            })
            this.submitBtnDisabled = true //禁用按钮，避免重复提交
            request({
              url:'/api/servicesource/add_servicesource',
              method: 'post',
              data: paramSource,
            }).then(({data}) => {
              if(data.code == 200){
                this.$message.success('新增成功');
                this.fetchDataSourceTable();
                this.closeSlide()
              }else if(data.code === 4010){
                this.$store.dispatch('LogOut').then(() => {
                  location.reload()
                })
              }else{
                this.$message.error(data.message)
              }
              this.submitBtnDisabled = false
            })

          }
        })
      }else {
        this.$message.error('该驱动名已经存在');
      }
    },
    // 测试连接函数
    test(form){
      if(this.nameRepeat == true){
        this.$refs.sourceForm.validate(valid => {
          if(valid){
            this.testPic = 'loading'
            let message ='提示'
            console.log(form)
            form.paramConfigList.forEach((item,index) => {
              item.paramValue = form['requiredParam'+index]
            })
            let paramData = {
              driverId: form.driverId,
              paramConfigList: form.paramConfigList
            }
            request({
              url: '/api/servicesource/get_servicesource_connnection',
              method: 'post',
              data: paramData
            }).then(({data}) => {
              if(data.code === 200){
                this.testPic = 'checked'
                this.$message.success(data.data)
              }else if(data.code === 4010){
                this.$store.dispatch('LogOut').then(() => {
                  location.reload()
                })
              }else{
                message = data.message
                this.$message.error(message)
                // this.$alert(message , '失败原因', {
                //   type: 'info',
                //   confirmButtonText: '取消',
                //   callback: action => {}
                // })
                // this.$message.error(data.message)
                this.testPic = 'failure'
              }
            })
          }
        })
      }else {
        this.$message.error('该驱动名已经存在');
      }

    },
    // 编辑弹框
    handleUpdate(row){
      this.testPic = ''
      this.addDialog = true
      this.dialogStatus = 'update'
      this.secondDialogStatus = 'update'
      request({
        url: '/api/servicesource/get_servicesource',
        method: 'get',
        params: {
          id: row.id
        }
      }).then(({data}) => {
        const row = data.data
        this.sourceForm = {
          id: row.id,
          driverId: row.driverId, // 驱动id
          serviceSourceName: row.serviceSourceName,
          serviceSourceDesc: row.serviceSourceDesc,
          statusCode: row.statusCode,
          stageCode: row.stageCode,
          maxConnections: row.maxConnections,
          queryTimeout: row.queryTimeout,
          connTimeout: row.connTimeout,
          paramConfigList: row.paramConfigList
        }
        this.sourceForm.paramConfigList.forEach((item,index) => {
          this.sourceForm['requiredParam'+index] = this.sourceForm.paramConfigList[index].paramValue
          if(item.isRequired === 'y'){
            this.sourceRule['requiredParam'+index] = [{required: true, message: '请填写必填项', trigger: 'blur'}]
          }
        })
        // 驱动版本赋初值
        for(var i=0; i<this.driveType.length; i++){
          if(row.driverId == this.driveType[i].id){
            this.version = this.driveType[i].version
          }
        }
      })

    },
    // 编辑确定函数
    editorSource(source) {

      this.$refs.sourceForm.validate(valid => {
        if(valid){
          if(this.testPic !== 'checked'){
            this.$message.error('请先完成正确测试！')
            return
          }
          const paramSource = {}
          $.extend(true,paramSource,source)
          paramSource.paramConfigList.forEach((item,index) => {
            item.paramValue = paramSource['requiredParam'+index]
            delete paramSource['requiredParam'+index]
          })
          this.submitBtnDisabled = true //禁用按钮，避免重复提交
          request({
            url:'/api/servicesource/edit_servicesource',
            method: 'post',
            data: paramSource,
          }).then(({data}) => {

            if(data.code == 200){
              this.$message.success('更新成功');
              this.closeSlide()
              this.fetchDataSourceTable();
            } else if(data.code === 4010){
              this.$store.dispatch('LogOut').then(() => {
                location.reload()
              })
            }else{
              this.$message.error(data.message)
            }
            this.submitBtnDisabled = false
          })
        }
      })
    },
    /******** 新增编辑end *********/
    // 查看
    handleView(row){
      this.dialogStatus = 'view'
      this.addDialog = true
      request({
        url: '/api/servicesource/get_servicesource',
        method: 'get',
        params: {
          id: row.id
        }
      }).then(({data}) => {
        const row = data.data
        this.sourceForm = {
          id: row.id,
          driverId: row.driverId, // 驱动id
          serviceSourceName: row.serviceSourceName,
          serviceSourceDesc: row.serviceSourceDesc,
          statusCode: row.statusCode,
          stageCode: row.stageCode,
          maxConnections: row.maxConnections,
          queryTimeout: row.queryTimeout,
          connTimeout: row.connTimeout,
          paramConfigList: row.paramConfigList
        }
        this.sourceForm.paramConfigList.forEach((item,index) => {
          this.sourceForm['requiredParam'+index] = this.sourceForm.paramConfigList[index].paramValue
          // if(item.isRequired === 'y'){
          //   this.sourceRule['requiredParam'+index] = [{required: true, message: '请填写必填项', trigger: 'blur'}]
          // }
        })
        // 驱动版本赋初值
        for(var i=0; i<this.driveType.length; i++){
          if(row.driverId == this.driveType[i].id){
            this.version = this.driveType[i].version
          }
        }
      })
    },
    /******** 删除、查询、状态改变与查重start *********/
    // 单个删除
    deleteSource(id) {
      let idData = {
        id: id
      };
      this.$confirm('确定删除此数据源？', '提示', {
        type: 'warning'
      }).then(() => {
        request({
          url: '/api/servicesource/del_servicesource',
          method: 'post',
          data: idData
          }).then(({data}) => {

            if(data.code == 200){
              this.$message.success({
                message: '删除成功'
              });
              this.fetchDataSourceTable();
            } else if(data.code === 4010){
              this.$store.dispatch('LogOut').then(() => {
                location.reload()
              })
            }else{
              this.$message.error({
                message: data.message
              })
            }

          })
        }
      ).catch(() => {
      })
    },
    // 取消新增或编辑
    cancelSource(){
      this.$refs.sourceForm.resetFields()
    },
    // 精确查询
    preciseQuery(){
      this.fetchDataSourceTable('1')
    },
     // 驱动名称查重
    checkName(name){
      if(name && name != '' ){
        let param = {
          'serviceSourceName': name
        }
        request({
          url: '/api/servicesource/check_servicesource',
          method: 'get',
          params: param
        }).then(({data}) => {

          if(data.code == 200){
            this.nameRepeat = true
          } else if(data.code === 4010){
            this.$store.dispatch('LogOut').then(() => {
              location.reload()
            })
          }else {
            this.nameRepeat = false
            this.$message.error(data.message)
          }

        })
      }
    },
    // 状态改变
    changeServiceState(val){
      let statuParams
      if(val.state){
        statuParams = {
          id: val.item.id,
          statusCode: 'enabled',
          stageCode: 'unpublished'
        }
        this.tableData.data.forEach((item, index)=>{
          if(item.id == val.item.id){
            item.statusCode = 'enabled'
          }
        })
      }else {
        statuParams = {
          id: val.item.id,
          statusCode: 'disabled',
          stageCode: 'waiting'
        }
        this.tableData.data.forEach((item, index)=>{
          if(item.id == val.item.id){
            item.statusCode = 'disabled'
          }
        })
      }
      request({
        url: '/api/servicesource/change_servicesource',
        method: 'post',
        data: statuParams
      }).then(({data}) => {
        if(data.code == 200){
          if(val.state){
            this.$message.success({ message: '启动成功' });
          }else{
            this.$message.success({ message: '禁用成功' });
          }
          this.fetchDataSourceTable('3')
        } else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else {
          this.$message.error(data.message)
          this.fetchDataSourceTable()
        }

      })

    },
    /******** 删除、查询、状态改变与查重end *********/
    // 重置弹框内容
    resetTemp() {
      this.sourceForm = {
        driverId: '',
        serviceSourceName: '',
        serviceSourceDesc: '',
        statusCode: 'enabled', // 数据源状态
        stageCode: 'unpublished', // 数据源阶段
        maxConnections: null,
        queryTimeout: '',
        connTimeout: '',
        paramConfigList: []
      },
      this.version = ''
      this.testPic = ''
    },
    // 重置二级弹框
    resetCustomParam() {
      this.paramEditForm = {
        name: '',
        value: '',
        displayName: '',
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
    padding: 6px 30px;
    overflow:auto;
  }
  .form-header {
    padding: 0 30px 12px 12px;
    width: 100%
  }
  .linktest {
    border: 1px solid #e5e5e5;
    border-radius: 4px;
    padding: 4px 8px;
    cursor: pointer;
  }
  .param-input {
    width: 80%;
    height: 40px;
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
