<!--服务用户管理界面-->
<template>
  <div>
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">服务用户管理</h2>
        </div>
        <div class="fr">
          <el-button class="more-search" @click="showMoreSearch">
            更多查询 <svg-icon icon-class="spread" :class="{'retract':moreSearch}"></svg-icon>
          </el-button>
        </div>
        <div class="search fr">
          <input class="do-search-input" type="text" style="padding-left: 15px; width: 200px; outline: none; border: 0; font-size: 12px; display: inline-block; height: 100%; float: left;" v-model="query.name" placeholder="请输入关键字"></input>
          <a href="javascript:;" @click="fetchServiceUserTable()"><svg-icon icon-class="search"></svg-icon></a>
        </div>
        <div class="fr">
          <el-button class="add-btn" type="primary" @click="handleCreate" :disabled="!operatePermission"><i>+</i>新增</el-button>
        </div>
      </div>
      <!-- 精确查询 -->
      <!-- <el-collapse-transition> -->
        <div class="clearfix more-search-box">
          <el-form ref="form" :model="searchForm">
            <div class="clearfix input-group">
              <el-form-item label="APP名称" style="width: 226px;" class="fl" label-width="80px">
                <el-input v-model="searchForm.name" size="small"></el-input>
              </el-form-item>
              <el-form-item label="负责人" style="width: 236px;" class="fl" label-width="90px">
                <el-input v-model="searchForm.appOwner" size="small"></el-input>
              </el-form-item>
              <el-form-item label="状态" style="width: 206px;" class="fl" label-width="60px">
                <el-select v-model="searchForm.statusCode" placeholder="" clearable size="small">
                  <el-option :label="item.displayName" :value="item.code" v-for="(item, index) in statusCodeList" :key="index"></el-option>
                </el-select>
              </el-form-item>
            </div>
            <div class="btn-group">
              <el-button type="primary" size="mini" @click="fetchServiceUserTable('1')">查询</el-button>
              <el-button size="mini" style="margin-left: 10px" @click="resetQuery">重置</el-button>
            </div>
          </el-form>
        </div>
      <!-- </el-collapse-transition> -->
    </nav>
    <section class="components-container clearfix section-bod">
      <!-- <el-table
        key="table0"
        :data="tableData.data"
        tooltip-effect="dark"
        style="width: 100%"
        size="small"
        :header-row-class-name="titleBgColor">
        <el-table-column prop="name" label="APP名称" show-overflow-tooltip width="200">
        </el-table-column>
        <el-table-column prop="appOwner" label="负责人" show-overflow-tooltip>
        </el-table-column>
        <el-table-column prop="statusCode" label="状态" show-overflow-tooltip>
        </el-table-column>
        <el-table-column prop="appServices" label="授权服务" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-for="(item, index) in scope.row.appServices">{{ item.dataServiceName }}  </span>
          </template>
        </el-table-column>
        <el-table-column prop="updateBy" label="更新用户" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="更新时间" show-overflow-tooltip>
          <template slot-scope="scope">{{ scope.row.updateTime }}</template>
        </el-table-column>
        <el-table-column label="操作" show-overflow-tooltip width="150">
          <template slot-scope="scope">
            <a title="编辑" @click="handleUpdate(scope.row)"><svg-icon icon-class="editor" style="width: 20px; height: 20px"></svg-icon></a>
            <a title="删除" @click="deleteSerUser(scope.row.id)"><svg-icon icon-class="delete" style="width: 20px; height: 20px"></svg-icon></a>

            <a title="授权" v-if="scope.row.appServices.length >0" @click="handleAuthorization(scope.row, 'authorized')"><svg-icon icon-class="authorization" style="width: 20px; height: 20px"></svg-icon></a>
            <a title="授权" v-else @click="handleAuthorization(scope.row, 'nouthorize')"><svg-icon icon-class="authorization" style="width: 20px; height: 20px"></svg-icon></a>
          </template>
        </el-table-column>
      </el-table> -->
      <cr-loading v-show="loading"></cr-loading>
      <list-card
        v-show="!loading"
        :listData="tableData.data"
        :operatePermission="operatePermission"
        :fieldsData="fieldsData"
        :isOperation="true"
        :operationBtns="operationBtns"
        @handleOperate="handleOperate"
        @handleSwitchClick="changeServiceState"
        @edit="handleUpdate"
        @delete="deleteSerUser"
        @browse="handleView"
      ></list-card>
      <el-pagination
        @current-change="(current)=>{this.loading=true;this.tableData.data=[];searchForm.pageNo = current;this.fetchServiceUserTable('2')}"
        :page-size="parseInt(searchForm.pageSize)"
        :current-page="parseInt(searchForm.pageNo)"
        background
        layout="prev, pager, next"
        :total="tableData.page"
        class="fr mt-15">
      </el-pagination>
    </section>
    <!--新增与编辑-->
    <!-- <el-dialog
      :title="textMap[dialogStatus]"
      :visible.sync="addDialog"
      width="50%"
      @close='cancelSource(serviceUserForm)'>
      <el-form :model="serviceUserForm" ref="serviceUserForm" :rules="serviceUserRule" label-width="110px" style="padding: 10px 30px;">
        <el-form-item label="APP名称" prop="name" required>
          <el-input v-model="serviceUserForm.name" @blur="checkName(serviceUserForm.name)"  :disabled="dialogStatus == 'update' || dialogStatus == 'view'"></el-input>
        </el-form-item>
        <el-form-item label="负责人:" prop="code"  class="mt-15">
          <el-input v-model="serviceUserForm.appOwner" :disabled="dialogStatus == 'view'"></el-input>
        </el-form-item>
        <el-form-item label="描述:" prop="appDesc" class="mt-15">
          <el-input type="textarea" v-model="serviceUserForm.appDesc" :disabled="dialogStatus == 'view'"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createSerUser(serviceUserForm)">保存</el-button>
        <el-button v-else type="primary" @click="editorSerUser(serviceUserForm)">保存</el-button>
        <el-button @click="addDialog = false">取消</el-button>
      </span>
    </el-dialog> -->
    <!--授权服务-->
    <!-- <el-dialog
      title="授权服务"
      :visible.sync="authorizationDialog"
      width="60%">
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="creatAuthService">保存</el-button>
        <el-button @click="authorizationDialog = false">取消</el-button>
      </span>
    </el-dialog> -->

    <!-- 新增、编辑滑框 -->
    <slide-box
      slideWidth="450px"
      :slideShow="addDialog"
      @close="slideClose"
      :title="textMap[dialogStatus]"
      ref="slideBox1">
      <div class="form-body" slot="slide-content">
        <el-form :model="serviceUserForm" ref="serviceUserForm" :rules="serviceUserRule" label-width="110px">
          <el-form-item label="APP-ID：" v-if="dialogStatus == 'update'" >
            <el-input v-model="serviceUserForm.id" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="服务用户：" prop="name" required>
            <el-input v-model="serviceUserForm.name" @blur="checkName(serviceUserForm)"  :disabled="dialogStatus == 'view'"></el-input>
          </el-form-item>
          <el-form-item label="负责人:" prop="code"  class="mt-15">
            <!-- <el-input v-model="serviceUserForm.appOwner" :disabled="dialogStatus == 'view'"></el-input> -->
            <el-select v-model="serviceUserForm.appOwner" :disabled="dialogStatus == 'view'" style="width:100%">
              <el-option v-for="(item,index) in appUserOptions" :key="index" :label="item.label" :value="item.value"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="状态:" class="mt-15" style="color: #606266">
            <span v-if=" serviceUserForm.statusCode === 'enabled' ">启用</span>
            <span v-else-if=" serviceUserForm.statusCode === 'disabled' ">禁用</span>
          </el-form-item>
          <el-form-item label="描述:" prop="appDesc" class="mt-15">
            <el-input type="textarea" v-model="serviceUserForm.appDesc" :disabled="dialogStatus == 'view'"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="slide-footer">
        <el-button size="mini" :disabled="submitBtnDisabled" v-if="dialogStatus=='create'" type="primary" @click="createSerUser(serviceUserForm)">保存</el-button>
        <el-button size="mini" :disabled="submitBtnDisabled" v-if="dialogStatus=='update'" type="primary" @click="editorSerUser(serviceUserForm)">保存</el-button>
        <el-button size="mini" @click="closeSlide('1')">取消</el-button>
      </span>
    </slide-box>

    <!-- 查看滑框 -->
    <slide-box
      slideWidth="600px"
      @close="slideClose"
      :slideShow="viewDialog"
      :title="'用户详情'"
      ref="slideBox2">
      <div class="form-body" slot="slide-content">
        <el-collapse v-model="viewUserName">
          <!-- 基本信息 -->
          <el-collapse-item title="基本信息" name="1">
            <ul class="basic-infor">
              <li><span style="display: inline-block; width: 70px;color: #999; text-align: right; font-size: 12px; margin-right: 4px;">APP-ID:</span> <span>{{ viewBasicInfor.id }}</span></li>
              <li><span style="display: inline-block; width: 70px;color: #999; text-align: right; font-size: 12px; margin-right: 4px;">服务用户: </span><span>{{ viewBasicInfor.name }}</span></li>
              <li><span style="display: inline-block; width: 70px;color: #999; text-align: right; font-size: 12px; margin-right: 4px;">负责人: </span><span>{{ viewBasicInfor.appOwnerName }}</span></li>
              <li><span style="display: inline-block; width: 70px;color: #999; text-align: right; font-size: 12px; margin-right: 4px;">状态: </span><span v-if="viewBasicInfor.statusCode==='active'">启用</span><span v-else-if="viewBasicInfor.statusCode==='inactive'">禁用</span></li>
              <li><span style="display: inline-block; width: 70px;color: #999; text-align: right; font-size: 12px; margin-right: 4px;">描述: </span><span>{{ viewBasicInfor.appDesc }}</span></li>
            </ul>
          </el-collapse-item>
          <!-- 资产信息 -->
          <el-collapse-item title="资产服务" name="2">
            <div class="clearfix auth-dialog-body ba-fa">
              <el-form ref="assetAuthForm" :model="viewAssetAuthForm">
                <div class="wd35 fl">
                  <div class="auth-title">授权服务</div>
                  <ul class="auth-list">
                    <el-radio-group v-model="viewSingleChoose" >
                      <li v-for="(item,index) in viewAllService" :key="index">
                        <el-radio :label="item.id" @change="viewServiseChooseAuthChange(item.id)">
                          <el-tooltip :content="item.dataServiceName" placement="top" effect="light">
                            <span>{{ item.dataServiceName | textLimit(10)}}</span>
                          </el-tooltip>
                        </el-radio>
                      </li>
                    </el-radio-group>
                  </ul>
                </div>
                <div class="wd60 fr">
                  <span class="user-span">授权属性</span>
                  <template>
                    <el-table
                      :data="viewAttrList"
                      key="table1"
                      ref="serviceMultipleTable"
                      border
                      size="mini"
                      :header-row-class-name="backgroundColorBorderChange"
                      class="mt-8">
                      <el-table-column label="属性名" header-align="center" align="center" show-overflow-tooltip prop="fieldName">
                      </el-table-column>
                      <el-table-column label="描述" header-align="center" align="center" show-overflow-tooltip prop="fieldDesc">
                      </el-table-column>
                      <el-table-column label="授权" header-align="center" align="center">
                        <template slot-scope="scope">
                          <el-checkbox v-model="viewAttrList[scope.$index].checked" :checked="scope.row.checked" :disabled="true"></el-checkbox>
                        </template>
                      </el-table-column>
                    </el-table>
                  </template>
                </div>
              </el-form>
            </div>
          </el-collapse-item>
          <!-- 能力信息 -->
          <el-collapse-item title="能力服务" name="3">
            <div class="clearfix auth-dialog-body ba-fa">
              <div class="wd35 fl">
                <div class="auth-title">能力服务</div>
                <ul class="auth-list">
                  <el-radio-group>
                    <li v-for="(item,index) in viewAllAbilityService" :key="index">
                      <el-radio :label="item.id">
                        <el-tooltip :content="item.dataServiceName" placement="top" effect="light">
                          <span>{{ item.dataServiceName | textLimit(10)}}</span>
                        </el-tooltip>
                      </el-radio>
                    </li>
                  </el-radio-group>
                </ul>
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
      <span slot="slide-footer">
        <el-button size="mini" @click="closeSlide('2')">取消</el-button>
      </span>
    </slide-box>

    <!-- 授权滑框 -->
    <slide-box
      slideWidth="600px"
      :slideShow="authorizationDialog"
      @close="slideClose"
      title="授权服务"
      :subSlide="true"
      :subSlideShow="openAddServise"
      @subClose="slideSubClose"
      subTitle="添加服务"
      ref="slideBox3">
      <!-- 服务授权属性弹框 -->
      <div class="form-body" slot="slide-content">
        <el-collapse v-model="activeName">
          <!-- 基本信息 -->
          <el-collapse-item title="基本信息" name="1">
            <ul class="basic-infor">
              <li><span style="display: inline-block; width: 70px;color: #999; text-align: right; font-size: 12px; margin-right: 4px;">APP-ID:</span> <span>{{ authBasicInfor.id }}</span></li>
              <li><span style="display: inline-block; width: 70px;color: #999; text-align: right; font-size: 12px; margin-right: 4px;">服务用户:</span><span> {{ authBasicInfor.name }}</span></li>
              <li><span style="display: inline-block; width: 70px;color: #999; text-align: right; font-size: 12px; margin-right: 4px;">负责人:</span> <span>{{ authBasicInfor.appOwner }}</span></li>
              <li><span style="display: inline-block; width: 70px;color: #999; text-align: right; font-size: 12px; margin-right: 4px;">状态: </span><span>{{ authBasicInfor.statusCode | serviceUserStateTranst}}</span></li>
              <li><span style="display: inline-block; width: 70px;color: #999; text-align: right; font-size: 12px; margin-right: 4px;">描述：</span> <span>{{ authBasicInfor.appDesc }}</span></li>
            </ul>
          </el-collapse-item>
          <!-- 资产授权 -->
          <el-collapse-item title="资产服务" name="2">
            <div class="clearfix auth-dialog-body ba-fa">
              <el-form ref="assetAuthForm" :model="assetAuthForm">
                <div class="wd35 fl">
                  <div class="auth-title">授权服务</div>
                  <ul class="auth-list">
                    <el-radio-group v-model="authServiceSingleChoose" >
                      <li v-for="(item,index) in authAllService" :key="index">
                        <el-radio :label="item.id" @change="serviseChooseAuthChange(item.id)">
                          <el-tooltip :content="item.dataServiceName" placement="top" effect="light">
                            <span>{{ item.dataServiceName | textLimit(10)}}</span>
                          </el-tooltip>
                        </el-radio>
                      </li>
                    </el-radio-group>
                  </ul>
                  <el-button class="mt-12" size="mini" @click="openAddAuthServise">添加服务</el-button>
                </div>
                <div class="wd60 fr">
                  <span class="user-span">授权属性</span>
                  <template>
                    <el-table
                      :data="assetAuthFormAttrList"
                      key="table1"
                      ref="serviceMultipleTable"
                      border
                      size="mini"
                      :header-row-class-name="backgroundColorBorderChange"
                      class="mt-8">
                      <el-table-column label="属性名" header-align="center" align="center" show-overflow-tooltip prop="fieldName">
                      </el-table-column>
                      <el-table-column label="描述" header-align="center" align="center" show-overflow-tooltip prop="fieldDesc">
                      </el-table-column>
                      <el-table-column
                        header-align="center"
                        align="center"
                        :render-header="renderHeader" class-name="el-column-center">
                        <template slot-scope="scope" >
                          <el-checkbox v-model="assetAuthFormAttrList[scope.$index].checked" :checked="scope.row.checked" @change="attrListsave(assetAuthFormAttrList)"></el-checkbox>
                        </template>
                      </el-table-column>
                    </el-table>
                  </template>
                </div>
              </el-form>
            </div>
          </el-collapse-item>
          <!-- 能力授权 -->
          <el-collapse-item title="能力服务" name="3">
            <div class="clearfix massage-auth-dialog-body ba-fa">
              <div class="clearfix massage-second ba-fa">
                <template class="fl" >
                  <el-transfer
                    v-model="abilityUserAuth"
                    filterable
                    filter-placeholder="请输入服务名称"
                    :left-default-checked="[]"
                    :right-default-checked="[]"
                    :titles="['服务列表', '授权服务']"
                    :data="abilityServiseList">
                  </el-transfer>
                </template>
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>
        <!-- <template>
          <div class="massageAuthTab">
            <ul class="clearfix">
              <li class="fl" :class="{'active': massageTab == 'data'}" @click="authTabChoose('data')">资产服务</li>
              <li class="fl" :class="{'active': massageTab == 'ability'}" @click="authTabChoose('ability')">能力服务</li>
            </ul>
          </div>
          <div class="clearfix auth-dialog-body ba-fa" v-show="massageTab == 'data'">
            <el-form ref="assetAuthForm" :model="assetAuthForm">
              <div class="wd35 fl">
                <div class="auth-title">授权用户</div>
                <ul class="auth-list">
                  <el-radio-group v-model="authServiceSingleChoose" >
                    <li v-for="(item,index) in authAllService" :key="index">
                      <el-radio :label="item.id" @change="serviseChooseAuthChange(item.id)">
                        <el-tooltip :content="item.dataServiceName" placement="top" effect="light">
                          <span>{{ item.dataServiceName | textLimit(10)}}</span>
                        </el-tooltip>
                      </el-radio>
                    </li>
                  </el-radio-group>
                </ul>
                <el-button class="mt-12" size="mini" @click="openAddAuthServise">添加服务</el-button>
              </div>
              <div class="wd60 fr">
                <span class="user-span">授权属性</span>
                <template>
                  <el-table
                    :data="assetAuthForm.attrList"
                    key="table1"
                    ref="serviceMultipleTable"
                    border
                    size="mini"
                    :header-row-class-name="backgroundColorBorderChange"
                    class="mt-15">
                    <el-table-column label="属性名" header-align="center" align="center" show-overflow-tooltip prop="fieldName">
                    </el-table-column>
                    <el-table-column label="描述" header-align="center" align="center" show-overflow-tooltip prop="fieldDesc">
                    </el-table-column>
                    <el-table-column
                      label="授权" header-align="center" align="center">
                      <template slot-scope="scope">
                        <el-checkbox v-model="assetAuthForm.attrList[scope.$index].checked" :checked="scope.row.checked" @change="attrListsave(assetAuthForm.attrList)"></el-checkbox>
                      </template>
                    </el-table-column>
                  </el-table>
                </template>
              </div>
            </el-form>
          </div>
          <div class="clearfix massage-auth-dialog-body ba-fa" v-show="massageTab == 'ability'">
            <div class="clearfix massage-second ba-fa">
              <template class="fl" >
                <el-transfer
                  v-model="abilityUserAuth"
                  filterable
                  filter-placeholder="请输入服务名称"
                  :left-default-checked="[]"
                  :right-default-checked="[]"
                  :titles="['服务列表', '授权服务']"
                  :data="abilityServiseList">
                </el-transfer>
              </template>
            </div>
          </div>
        </template> -->
      </div>

      <span slot="slide-footer">
        <el-button :disabled="submitBtnDisabled" size="mini" type="primary" @click="creatAuthService">保存</el-button>
        <el-button size="mini" @click="closeSlide('3')">取消</el-button>
      </span>

      <!-- 添加服务弹框 -->
      <div class="form-body" slot="sub-slide-content">
        <template>
          <el-transfer
            class="mt-15"
            filterable
            v-model="userAssetAuth"
            filter-placeholder="请输入服务名称"
            @change="chooseService"
            @right-check-change="[]"
            :left-default-checked="[]"
            :right-default-checked="[]"
            :titles="['服务列表', '授权服务']"
            :data="serviceDataListCope">
          </el-transfer>
        </template>
      </div>
      <span slot="sub-slide-footer" class="dialog-footer">
        <el-button size="mini" type="primary" @click="createService()">确定</el-button>
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
export default {
  components: {
    ListCard,
    SlideBox,
    CrLoading
  },
  data() {
    return {
      operatePermission: false,
      submitBtnDisabled: false,
      loading: true,
      appUserOptions: [],
      query: {
        name: ''
      },
      operationBtns: [
        {
          text: '授权',
          type: 'default',
          isControl: true
        }
      ],
      fieldsData:[
        {
          propsTitle: 'APP-ID',
          field: 'id'
        },
        {
          propsTitle: '服务用户',
          field: 'name'
        },
        {
          propsTitle: '负责人',
          field: 'appOwnerName'
        },
        {
          propsTitle: '状态',
          field: 'statusCode',
          switch: true
        },
        {
          propsTitle: '授权服务',
          field: 'serviceName',
          toolTip: true
        }
      ],
      /* 更多查询 */
      moreSearch: false,
      searchForm: {
        name: undefined,
        appOwner: undefined,
        statusCode: undefined,
        pageNo: '1',
        pageSize: '5'
      },
      statusCodeList: [],
      tableData: {
        data: null,
        page: undefined
      },
      /******** 新增、编辑与查看start ********/
      addDialog: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新增',
        view: '详情'
      },
      serviceUserForm: {
        name: '',
        appDesc: '',
        appOwner: '',
        statusCode: 'enabled'
      },
      // 是否必填或填写规则
      serviceUserRule: {
        name: [{ required: true, message: '请输入APP名称'}]
      },
      // 查看
      viewDialog: false,
      viewUserName: ['1'], // 查看折叠框
      viewBasicInfor: {}, // 查看基本信息
      viewAssetAuthForm: {}, // 界面资产模型数据-查看
      viewSingleChoose: '', // 界面单选用户-查看
      viewAllService: [], //
      viewAttrList: [], // 所有属性
      viewAllAbilityService: [], // 能力服务
      /******** 新增、编辑与查看end ********/

      /********* 授权服务start *********/
      authorizationDialog: false,
      massageTab: 'data',
      authBasicInfor: {}, // 服务用户基本信息
      viewDialogType: '', // 用于判断授次数（初次还是不是初次）
        /* 资产服务 */
      assetAuthForm: {
        attrList: null
      },
      serviceDataList: [],  // 所有资产服务数据列表（授权第一列）
      serviceDataListCope: [], // 所有服务数据列表（授权第一列）
      userAssetAuth: [], // 所有右边服务
      authUsersSaveData: {
        id: '',  // 用户id
        appServices: []  // 用户授权的服务
      },
      middleAuthServise: {  // 中间过渡
        appServices: []
      },
      tempAppServices: [],// 暂存的授权服务
      tempAppMoveServices: [],// 中间过度
      curretService: '', // 存储当前选择的服务
      editorSttr: [],
      assetAuthFormAttrList: [], // 授权属性

      openAddServise: false,  // 打开二级弹框参数
      authServiceSingleChoose: '', // 选择服务进行授权属性
      authAllService: [],  // 所有选择授权的服务

        /* 能力服务 */
      secondTab: 'get',
      abilityServiseList: [], // 所有能力服务数据列表（授权第一列）
      abilityServiseListCope: [],
      abilityUserAuth: [],  // 能力授权用户
      userApiAuth: [], // api数据
      activeName: ['1'],  // 折叠框
       /********* 授权服务end *********/
      serviceAuthRule: {},
      nameRepeat: true,
      foo: false
    }
  },
  mounted() {
    this.$store.getters.userInfo.menu.forEach(item => {
      item.children.forEach(subItem => {
        if(subItem.name === '服务用户管理'){
          this.operatePermission = subItem.operationIds.indexOf('3') !== -1
          console.log(subItem.name+'页面操作权限',this.operatePermission)
        }
      })
    })
    this.fetchServiceUserTable()
    this.fetchAssetServiceList()
    this.fetchAbilityServiceList()
    this.fetchStatusCode()
    this.enterKeyEv()

  },
  beforeDestroy() {
    $(document).off('keyup')
  },
  methods: {
    /****** 全选start *******/

    /****** 全选end *******/
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
    closeSlide(val){
      this.$refs['slideBox'+val].close()
    },
    slideClose(){
      this.addDialog = false
      this.viewDialog = false
      this.authorizationDialog = false
      this.openAddServise = false
      this.appUserOptions = []
    },
    // 关闭二级弹框
    closeSubSlide(){
      this.$refs.slideBox3.subClose()
    },
    // 点击关闭二级弹框
    slideSubClose(){
      this.openAddServise = false
    },

    handleOperate(val){
      request({
        url: '/api/services/get_app',
        method: 'get',
        params: {
          id: val.operateObj.id
        }
      }).then(({ data }) => {
        if(val.operateName === '授权') {
          this.handleAuthorization(data.data,'authorized')
        }
      })
    },
    /****** 查询重置、获取状态码start*******/
    fetchStatusCode(){
      request({
        url: '/api/service/model/query_status',
        method: 'get',
        params: {
          type: 'app_status_code'
        }
      }).then(({ data }) => {
        this.statusCodeList = data.data
      })
    },
    resetQuery(){
      this.searchForm = {
        pageNo: '1',
        pageSize: '5'
      }
    },
    //负责人列表
    fetchAppUsers(){
      request({
        url: '/api/auth/user/get_role_user?roleCode=data_app',
        method: 'get'
      }).then(({data}) => {
        if(data.code === 200){
          // this.appUserOptions
          data.data.forEach(item => {
            const obj = {}
            obj.label = item.name
            obj.value = item.id
            this.appUserOptions.push(obj)
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
          self.fetchServiceUserTable()
        }
      });
    },
    /****** 查询重置、获取状态码end*******/
    /********* 获取服务用户table、服务列表start *********/
    fetchServiceUserTable(val){
      this.loading = true;
      this.tableData.data = []
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
        url: '/api/services/query_app',
        method: 'get',
        params: obj
      }).then(({ data }) => {
        const self = this
        setTimeout(function(){
          self.loading = false
          if(data.code === 200){
            self.tableData.page = parseInt(data.data.count)
            let tableData = JSON.parse(JSON.stringify(data.data.list))
            // for(var i=0; i<tableData.length; i++){
            //   tableData[i].dataServiceName = []
            //   for(var j=0; j<tableData[i].appServices.length; j++){
            //     if(tableData[i].appServices[j].dataServiceName){
            //       tableData[i].dataServiceName.push(tableData[i].appServices[j].dataServiceName)
            //     }
            //   }
            // }
            // for(var i=0; i<tableData.length; i++){
            //   tableData[i].dataServiceName = tableData[i].dataServiceName.join(',')
            // }
            self.tableData.data = tableData
          }else if(data.code === 4010){
            self.$store.dispatch('LogOut').then(() => {
              location.reload()
            })
          }else{
          }
        })
      })
    },
    // testApi(){
    //   request({
    //     url:'http://10.72.19.194:8081/api/services/get_app',
    //     method: 'get',
    //     param:{
    //       id: 'APP_000008'
    //     }
    //   }).then(res => {
    //     console.log(res)
    //   })
    // },
    // 资产服务列表
    fetchAssetServiceList(){
      request({
        url: '/api/services/query_auth_service',
        method: 'get',
        params:{
          typeCode: 'asset'
        }
      }).then(({ data }) => {
        if(data.code === 200){
          for(var i=0; i<data.data.length; i++){
            this.serviceDataList.push(
              {
                key: i, // 所有用户index， 用于之后的Arr[0]
                label: data.data[i].dataServiceName,
                id: data.data[i].id,
                name: data.data[i].dataServiceName,
              }
            )
          }
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
           location.reload()
         })
        }else{
          // this.$message.error(data.message)
        }
      })
    },
    // 能力服务列表
    fetchAbilityServiceList(){
      request({
        url: '/api/services/query_auth_service',
        method: 'get',
        params:{
          typeCode: 'ability'
        }
      }).then(({ data }) => {
        if(data.code === 200){
          for(var i=0; i<data.data.length; i++){
            this.abilityServiseList.push(
              {
                key: i,  // 所有用户index， 用于之后的Arr[0]
                label: data.data[i].dataServiceName,
                id: data.data[i].id,
                name: data.data[i].dataServiceName,
              }
            )
          }
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
           location.reload()
         })
        }else{
          // this.$message.error(data.message)
        }
      })
    },
    /********* 服务用户table、服务列表end *********/
    /********* 新增编辑查看start ********/
    // 新增弹框
    handleCreate() {
      this.dialogStatus = 'create'
      this.addDialog = true
      this.fetchAppUsers()
      this.reseatTemp()
    },
    // 新增确定函数
    createSerUser(form) {
      if(this.nameRepeat == true){
        this.$refs.serviceUserForm.validate((valid) => {
          if(valid){
            this.submitBtnDisabled = true
            request({
                url:'/api/services/add_app',
                method: 'post',
                data: form,
              }).then(({data}) => {
                if(data.code == 200){
                  this.$message.success('新增成功');
                  this.fetchServiceUserTable();
                  this.closeSlide('1')
                }else if(data.code === 4010){
                  this.$store.dispatch('LogOut').then(() => {
                    location.reload()
                  })
                } else{
                  this.$message.error(data.message)
                }
                this.submitBtnDisabled = false
              })
          }else{
            return false;
          }
        })
      }else{
        this.$message.error('该驱动名已经存在');
      }
    },
    // 编辑弹框
    handleUpdate(row){
      this.addDialog = true;
      this.dialogStatus = 'update'
      this.fetchAppUsers()
      // console.log(row)
      this.serviceUserForm = {
        id: row.id,
        name: row.name,
        appOwner: row.appOwner,
        appDesc: row.appDesc,
        id: row.id,
        statusCode: row.statusCode
      }
    },
    // 编辑确定函数
    editorSerUser(form) {
      this.$refs.serviceUserForm.validate((valid) => {
        if(valid){
          this.submitBtnDisabled = true
           request({
              url:'/api/services/edit_app',
              method: 'post',
              data: form,
            }).then(({data}) => {
              if(data.code == 200){
                this.$message.success('修改成功');
                this.closeSlide('1')
                this.fetchServiceUserTable();
              }else if(data.code === 4010){
                this.$store.dispatch('LogOut').then(() => {
                  location.reload()
                })
              } else{
                this.$message.error(data.message)
              }
              this.submitBtnDisabled = false
            })
        }else{
          return false;
        }
      })
    },
    // 查看
    handleView(row){
      this.viewDialog = true
      this.viewBasicInfor = row
      this.viewBasicInfor.statusCode = row.statusCode
      // console.log(row.appServices,row.abilityServices)
      // this.viewAllService = JSON.parse(JSON.stringify(row.appServices))
      // this.viewAllAbilityService = JSON.parse(JSON.stringify(row.abilityServices))
    },
    // 改变服务用户
    viewServiseChooseAuthChange(id){
      let curretId
      for(var i=0; i<this.viewAllService.length; i++){
        if(this.viewAllService[i].id == id){
          curretId = i;
          this.viewAttrList = this.viewAllService[i].modelParams
          // checked
          for(var j=0; j<this.viewAllService[i].dsFields.length; j++){
            for(var k=0; k<this.viewAttrList.length; k++){
              if(this.viewAllService[i].dsFields[j].fieldId == this.viewAttrList[k].id){
                this.viewAttrList[k].checked = true
              }
            }
          }
        }
      }
    },
    /********* 新增编辑查看end ********/
    // 查重
    checkName(form){
      console.log(form)
      if(form.name && form.name != '' ){
        let param = {
          'name': form.name,
          'userId': form.id
        }
        request({
          url: '/api/services/check_app',
          method: 'get',
          params: param
        }).then(({data}) => {
          if(data.code == 200){
            this.nameRepeat = true
          }else if(data.code === 4010){
            this.$store.dispatch('LogOut').then(() => {
              location.reload()
            })
          } else {
            this.nameRepeat = false
            this.$message.error(data.message)
          }
        })
      }
    },
    // 重置
    reseatTemp(){
      this.serviceUserForm = {
        name: '',
        appDesc: '',
        appOwner: '',
        statusCode: 'enabled'
      }
    },
    // 删除
    deleteSerUser(id){
      this.$confirm('确定删除此服务用户？', '提示', {
        type: 'warning'
      }).then(() => {
        request({
          url: '/api/services/del_app',
          method: 'post',
          data: {
            id: id
          }
          }).then(({data}) => {
            if(data.code == 200){
              this.$message.success({
                message: '删除成功'
              });
              this.fetchServiceUserTable();
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
    // 取消新增或编辑
    cancelSource(){
      this.addDialog = false
      this.$refs.serviceUserForm.resetFields()
    },
    // 状态改变
    changeServiceState(val){
      let stateParam
      val.state = val.state ? 'enabled':'disabled'
      stateParam = {
        id: val.item.id,
        statusCode: val.state
      }
      this.tableData.data.forEach((item, index)=>{
        if(item.id == val.item.id){
          item.statusCode = val.state
        }
      })
      request({
        url:'/api/services/edit_app',
        method: 'post',
        data: stateParam,
      }).then(({data}) => {
        if(data.code == 200){
          if(val.state === 'enabled'){
            this.$message.success({ message: '启动成功' });
          }else{
            this.$message.success({ message: '禁用成功' });
          }
          this.fetchServiceUserTable();
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        } else{
          this.$message.error(data.message)
        }
      })
    },
    /********* 授权start ********/
    // 服务tab栏选择
    authTabChoose(type){
      this.massageTab = type;
    },
      // 服务二级tab栏选择
    massgeAuthTabChoose(type){
      this.secondTab = type
    },
    // 授权弹框
    handleAuthorization(row, type) {
      this.authBasicInfor = JSON.parse(JSON.stringify(row))  // 服务用户基本信息
      request({
        url: '/api/auth/user/get_role_user?roleCode=data_app',
        method: 'get'
      }).then(({data}) => {
        // data.data.find(item => item.id === this.authBasicInfor.appOwner).name
        this.authBasicInfor.appOwner = data.data.find(item => item.id === this.authBasicInfor.appOwner).name
      })
      this.authorizationDialog = true
      this.viewDialogType = type
      /* 能力模型 */
      this.abilityUserAuth = []
      this.assetAuthFormAttrList = [] // 所有属性
      /* 资产模型 */
      this.userAssetAuth = []

      this.authServiceSingleChoose = ''
      this.assetAuthForm.attrList = null
      // 所有服务（穿梭框左边数据）--资产
      this.serviceDataListCope = JSON.parse(JSON.stringify(this.serviceDataList))
      // 所有服务（穿梭框左边数据）--能力
      this.abilityServiseListCope = JSON.parse(JSON.stringify(this.abilityServiseList))

      this.authUsersSaveData = {
        id:'',  // 用户id
        appServices: [],  // 用户授权的服务-资产
        modelParams: [],  // 所有属性
        abilityServices: []  // 用户授权的服务-能力
      }
      // 获取所有授权服务（一级弹框左边）--资产
      this.authAllService = []
      // 判断为一次授权还是再次授权--资产
      this.authUsersSaveData.id = row.id
      // 获取所有服务的对应属性（服务不同对应的所有属性不一样）--资产
      this.authUsersSaveData.modelParams = row.appServices
      // 获取所有授权的服务（二级弹框右边的值）--资产
      for(var i=0; i<row.appServices.length; i++){
        for(var j=0; j<this.serviceDataListCope.length; j++){
          if(row.appServices[i].id == this.serviceDataListCope[j].id){
            this.userAssetAuth.push(j)
            this.authAllService.push(
              row.appServices[i]
            )
          }
        }
      }
      // 获取保存该服务授权的所有属性
      for(var i=0; i<row.appServices.length; i++){
        this.authUsersSaveData.appServices.push(
          {
            id: row.appServices[i].id,
            dsFields: []
          }
        )
        if(row.appServices[i].dsFields){
          for(var j=0; j<row.appServices[i].dsFields.length; j++){

            for(var k=0; k<row.appServices[i].modelParams.length; k++){
              if(row.appServices[i].dsFields[j].fieldId == row.appServices[i].modelParams[k].id){
                this.authUsersSaveData.appServices[i].dsFields.push(
                  {
                    fieldName: row.appServices[i].modelParams[k].fieldName,
                    fieldId: row.appServices[i].modelParams[k].id
                  }
                )
              }

            }
          }
        }
      }

      // 获所有授权的服务(穿梭框右边)--能力
      for(var i=0; i<row.abilityServices.length; i++){
        for(var j=0; j<this.abilityServiseListCope.length; j++){
          if(row.abilityServices[i].id == this.abilityServiseListCope[j].id){
            this.abilityUserAuth.push(j)
          }
        }
      }

    },
        /* 资产服务授权 */
      // 增加服务弹框打开
    openAddAuthServise(){
      this.openAddServise = true
      $.extend(true,this.tempAppServices,this.authAllService)
      $.extend(true,this.tempAppMoveServices,this.authAllService)
    },
      // 添加授权服务-确定函数
    createService(){
      this.openAddServise = false
      this.closeSubSlide()
    },
      // 选择一个授权服务获取属性
    serviseChooseAuthChange(id){
      // this.assetAuthForm.attrList = []
      this.assetAuthFormAttrList = [];
      this.curretService = id  // 保存当前选择用户id
      for(var i=0; i<this.authUsersSaveData.modelParams.length; i++){
        if(id == this.authUsersSaveData.modelParams[i].id){
          // 获取对应服务的所有属性
          // this.assetAuthForm.attrList = this.authUsersSaveData.modelParams[i].modelParams
          this.assetAuthFormAttrList = JSON.parse(JSON.stringify(this.authUsersSaveData.modelParams[i].modelParams))
          // 如果授权了属性，则在该用户对应的服务的所有属性中打钩
          // 清除checked
          /* for(var j=0; j<this.authUsersSaveData.modelParams[i].modelParams.length; j++){
            this.authUsersSaveData.modelParams[i].modelParams[j].checked = false;
            this.authUsersSaveData.modelParams[i].modelParams[j].flag = 'flag' // 用于标记是否为第二次授权了的
          }
          for(var k=0; k<this.authUsersSaveData.modelParams[i].dsFields.length; k++){
            for(var j=0; j<this.authUsersSaveData.modelParams[i].modelParams.length; j++){
              if(this.authUsersSaveData.modelParams[i].dsFields[k].fieldId == this.authUsersSaveData.modelParams[i].modelParams[j].id){
                this.authUsersSaveData.modelParams[i].modelParams[j].checked = true;
              }
            }
          } */
          for(var j=0; j<this.assetAuthFormAttrList.length; j++){
            this.$set(this.assetAuthFormAttrList[j], 'checked', false)
            this.assetAuthFormAttrList[j].flag = 'flag' // 用于标记是否为第二次授权了的
          }

          for(var k=0; k<this.authUsersSaveData.modelParams[i].dsFields.length; k++){
            for(var j=0; j<this.assetAuthFormAttrList.length; j++){
              if(this.authUsersSaveData.modelParams[i].dsFields[k].fieldId == this.assetAuthFormAttrList[j].id){
                this.$set(this.assetAuthFormAttrList[j], 'checked', true)
              }
            }
          }

        }
      }

    },
    // 全选
    handleCheckAllChange(val){
      if(val){
        for(var i=0; i<this.assetAuthFormAttrList.length; i++){
          this.$set(this.assetAuthFormAttrList[i], 'checked', true)
        }
      }else {
        for(var i=0; i<this.assetAuthFormAttrList.length; i++){
          this.$set(this.assetAuthFormAttrList[i], 'checked', false)
        }
      }
          // 全选 改变最后传递的数据（与attrListsave里面的逻辑一样）
      // 根据id获取对应选择了的服务
      let idFlag
      for(var i=0; i<this.authAllService.length; i++){
        if(this.authAllService[i].id == this.curretService){
          idFlag = i;
          break;
        }
      }
      for(var i=0; i<this.authUsersSaveData.appServices.length; i++){
        if(this.authAllService[idFlag].id == this.authUsersSaveData.appServices[i].id){
          this.authUsersSaveData.appServices[i].dsFields = []
          for(var j=0; j<this.assetAuthFormAttrList.length; j++){
            if(this.assetAuthFormAttrList[j].checked == true){
              this.authUsersSaveData.appServices[i].dsFields.push(
                {
                  fieldId: this.assetAuthFormAttrList[j].id,
                  fieldName: this.assetAuthFormAttrList[j].fieldName
                }
              )
            }
          }
        }
      }
      // 在最开始的row里面改变数据  this.authUsersSaveData.modelParams
      for(var i=0; i<this.authUsersSaveData.modelParams.length; i++){
        if(this.authAllService[idFlag].id == this.authUsersSaveData.modelParams[i].id){
          this.authUsersSaveData.modelParams[i].dsFields = []
          for(var j=0; j<this.assetAuthFormAttrList.length; j++){
            if(this.assetAuthFormAttrList[j].checked == true){
              this.authUsersSaveData.modelParams[i].dsFields.push(
                {
                  fieldId: this.assetAuthFormAttrList[j].id,
                  fieldName: this.assetAuthFormAttrList[j].fieldName
                }
              )
            }
          }
        }
      }
    },
    renderHeader(h,{column}){
      return h('el-checkbox',
        {
          on: {
            change: this.handleCheckAllChange
          }
        }
      )
    },
      // 授权服务选择属性
    attrListsave(selection){
      // 根据id获取对应选择了的服务
      let idFlag
      for(var i=0; i<this.authAllService.length; i++){
        if(this.authAllService[i].id == this.curretService){
          idFlag = i;
          break;
        }
      }
      for(var i=0; i<this.authUsersSaveData.appServices.length; i++){
        if(this.authAllService[idFlag].id == this.authUsersSaveData.appServices[i].id){
          this.authUsersSaveData.appServices[i].dsFields = []
          for(var j=0; j<selection.length; j++){
            if(selection[j].checked == true){
              this.authUsersSaveData.appServices[i].dsFields.push(
                {
                  fieldId: selection[j].id,
                  fieldName: selection[j].fieldName
                }
              )
            }
          }
        }
      }
      // 在最开始的row里面改变数据  this.authUsersSaveData.modelParams
      for(var i=0; i<this.authUsersSaveData.modelParams.length; i++){
        if(this.authAllService[idFlag].id == this.authUsersSaveData.modelParams[i].id){
          this.authUsersSaveData.modelParams[i].dsFields = []
          for(var j=0; j<selection.length; j++){
            if(selection[j].checked == true){
              this.authUsersSaveData.modelParams[i].dsFields.push(
                {
                  fieldId: selection[j].id,
                  fieldName: selection[j].fieldName
                }
              )
            }
          }
        }
      }
      console.log('最后', this.authUsersSaveData)
    },
      // 选择授权服务
    chooseService(curretArr,directive,Arr){
      // console.log('原始',this.authAllService)
      // console.log('暂存',this.tempAppService)
      // 右移增加
      if(directive == 'right'){
        for(var i=0; i<Arr.length; i++){
          // 最后传递的数据
          this.authUsersSaveData.appServices.push(
            {
              id: this.serviceDataListCope[Arr[i]].id,  // 用户id
              name: this.serviceDataListCope[Arr[i]].name,
              dsFields: [], // 授权用户-属性
            }
          )
          // 所有授权的服务
          this.authAllService.push(
            {
              id: this.serviceDataListCope[Arr[i]].id,  // 用户id
              dataServiceName: this.serviceDataListCope[Arr[i]].name,
              dsFields: [], // 授权用户-属性
            }
          )
        }
        // 左移删除
      }else if(directive == 'left'){
        // 获取Arr[i]的id 然后载根据id删除
        let id
        for(var i=0; i<Arr.length; i++){
          id = this.serviceDataListCope[Arr[i]].id
          this.authUsersSaveData.appServices = this.authUsersSaveData.appServices.filter(function(item, index,arr){
            return item.id != id
          })
          this.authAllService = this.authAllService.filter(
            function(item, index,arr){
              return item.id != id
          })
        }
      }
      // console.log('原始',this.authUsersSaveData.appServices)
      // console.log('暂存',this.tempAppService)
    },
      // 授权保存
    creatAuthService(){
      let paramData = {
        id: this.authUsersSaveData.id,
        appServices: []
      }
      for(var i=0; i<this.authUsersSaveData.appServices.length; i++){
        // 新增appServices
        paramData.appServices.push(
          {
            id: this.authUsersSaveData.appServices[i].id,
            dsFields: []
          }
        )
      }
      for(var j=0; j<this.authUsersSaveData.appServices.length; j++){
        // 新增dsFields
        if(this.authUsersSaveData.appServices[j].id){
          for(var k=0; k<this.authUsersSaveData.appServices[j].dsFields.length;k++){
            // 存在flag说明 为第二次授权的
            if(this.authUsersSaveData.appServices[j].dsFields[k].flag){
              if(this.authUsersSaveData.appServices[j].dsFields[k].checked == true){
                paramData.appServices[j].dsFields.push(
                  {
                    fieldName: this.authUsersSaveData.appServices[j].dsFields[k].fieldName,
                    fieldId: this.authUsersSaveData.appServices[j].dsFields[k].fieldId,
                  }
                )
              }
            }else {
              paramData.appServices[j].dsFields.push(
                {
                  fieldName: this.authUsersSaveData.appServices[j].dsFields[k].fieldName,
                  fieldId: this.authUsersSaveData.appServices[j].dsFields[k].fieldId,
                }
              )
            }
          }
        }
      }
      if(this.abilityUserAuth.length>0){
        for(var i=0; i<this.abilityUserAuth.length; i++){
          paramData.appServices.push(
            {
              id:this.abilityServiseList[this.abilityUserAuth[i]].id
            }
          )
        }
      }
      this.submitBtnDisabled = true
      request({
        url:'/api/services/auth_app',
        method: 'post',
        data: paramData,
      }).then(({data}) => {
        if(data.code == 200){
          this.$message.success('授权成功');
          this.fetchServiceUserTable()
          this.closeSlide('3')
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        } else{
          this.$message.error(data.message)
        }
        this.submitBtnDisabled = false
      })
    },
    /********* 授权end ********/

    // el-table的背景边框改变函数
    backgroundColorBorderChange({row, rowIndex}){
      return 'table-header-border-bg'
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
  text-align: center;
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
.massageAuthTab {
  ul {
    border-bottom: 1px solid #f6f6f6;
    li {
      width: 70px;
      height: 40px;
      line-height: 40px;
      text-align: center;
      margin: 0 10px;
      cursor: pointer;
    }
    .active{
      border-bottom: 2px solid #5fb878;
    }
  }
}
.auth-dialog-body {
  padding: 14px 15px;
  .auth-title{
    font-size: 12px;
    color: #555;
    height: 30px;
    line-height: 20px;
    padding-left: 15px;
  }
  .auth-list {
    border: 1px solid #ebeef5;
    padding: 0 18px 20px 6px;
    li {
      height: 30px;
      line-height: 30px;
    }
  }
  .user-span{
    font-size: 12px;
    color: #555;
  }
}

.massage-auth-dialog-body {
  padding: 14px 20px;
  .massage-second{
    margin: 0 auto;
  }
}
.api-auth-dialog-body {
  padding: 14px 20px;
  margin: 0 auto;
}
.form-body {
  padding: 0 30px;
  overflow:auto;
  .basic-infor{
    li {
      height: 30px;
      line-height: 30px;
      font-size: 12px;
      color: #999;
      span{
        color: #555;
        font-size: 12px;
      }
    }
  }
}
</style>
<style lang="scss">
.table-header-border-bg>th{
  background-color: #f5f6fa !important;
}
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
          background: #fdb945;
        }
        &.active:hover {
          color: white;
        }
        &:hover {
          color: #fdb945;
        }
      }
    }
  }
}
</style>