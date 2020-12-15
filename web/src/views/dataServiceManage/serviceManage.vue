<!-- 数据服务管理界面 -->
<template>
  <div>
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class='page-title'>数据服务管理</h2>
          <div class="model-tab">
            <span :class="{'select-tab':searchForm.typeCode==='asset'}" @click="modelTab('asset')">资产服务</span>
            <span>|</span>
            <span :style="{'cursor':isAdminOrAppUser()?'pointer':'not-allowed','color':isAdminOrAppUser()?'':'#bbb'}" :class="{'select-tab':searchForm.typeCode==='ability'}" @click="modelTab('ability')">能力服务</span>
          </div>
        </div>
        <div class="fr">
          <el-button class="more-search" @click="showMoreSearch">
            更多查询 <svg-icon icon-class="spread" :class="{'retract':moreSearch}"></svg-icon>
          </el-button>
        </div>
        <div class="search fr">
          <input class="do-search-input" type="text" style="padding-left: 15px; width: 200px; outline: none; border: 0; font-size: 12px; display: inline-block; height: 100%; float: left;" v-model="query.name" placeholder="请输入关键字"></input>
          <a href="javascript:;" @click="fetchDataServiceTable"><svg-icon icon-class="search"></svg-icon></a>
        </div>
      </div>
      <!-- 精确查询 -->
      <div class="clearfix more-search-box">
        <el-form ref="form" :model="searchForm">
          <div class="clearfix input-group">
            <!-- 资产服务 -->
            <template v-if=" this.searchForm.typeCode == 'asset' ">
              <el-form-item label="资产领域" style="width: 226px;" class="fl" label-width="80px">
                <el-select
                  v-model="searchForm.areaName"
                  size="small"
                  clearable
                  @change="queryAreaChange(searchForm.areaName)" >
                  <el-option v-for="(item, index) in areaList" :key="index" :label="item.areaName" :value="item.areaName"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="资产主题" style="width: 226px;" class="fl" label-width="80px">
                <el-select v-model="searchForm.topicName" size="small" clearable>
                  <el-option v-for="(item, index) in themeList" :key="index" :label="item.topicName" :value="item.topicName"></el-option>
                </el-select>
              </el-form-item>
            </template>
            <!-- 能力服务 -->
            <template v-else>
              <el-form-item label="大类" style="width: 226px;" class="fl" label-width="80px">
                <el-select
                  v-model="searchForm.areaName"
                  size="small"
                  clearable
                  @change="queryAbilityChange(searchForm.areaName)" >
                  <el-option v-for="(item, index) in abilityBigList" :key="index" :label="item.displayName" :value="item.displayName"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="子类" style="width: 226px;" class="fl" label-width="80px">
                <el-select v-model="searchForm.topicName" size="small" clearable>
                  <el-option v-for="(item, index) in abilitySubList" :key="index" :label="item.displayName" :value="item.displayName"></el-option>
                </el-select>
              </el-form-item>
            </template>

            <el-form-item label="数据模型名称" style="width: 256px;" class="fl" label-width="110px">
              <el-input v-model="searchForm.modelName" size="small"></el-input>
            </el-form-item>
            <el-form-item label="服务ID" style="width: 226px;" class="fl" label-width="80px">
              <el-input v-model="searchForm.dataServiceId" size="small"></el-input>
            </el-form-item>
            <el-form-item label="服务名称" style="width: 226px;" class="fl" label-width="80px">
              <el-input v-model="searchForm.dataServiceName" size="small"></el-input>
            </el-form-item>
            <el-form-item label="状态" style="width: 226px;" class="fl" label-width="80px">
              <el-select v-model="searchForm.statusCode" placeholder="" size="small" clearable>
                <el-option :label="item.displayName" :value="item.code" v-for="(item, index) in statusCodeList" :key="index"></el-option>
              </el-select>
            </el-form-item>
          </div>
          <div class="btn-group">
            <el-button type="primary" size="mini" @click="fetchDataServiceTable('1')">查询</el-button>
            <el-button size="mini" style="margin-left: 10px"  @click.stop="resetQuery">重置</el-button>
          </div>
        </el-form>
      </div>
    </nav>
    <section class="components-container clearfix section-bod">
      <!-- <el-table
        ref="multipleFooTable"
        :data="tableData.data"
        tooltip-effect="dark"
        style="width: 100%"
        size="small"
        :header-row-class-name="titleBgColor">
        <el-table-column prop="dataAssetArea.areaName" label="资产领域" show-overflow-tooltip>
        </el-table-column>
        <el-table-column prop="dataAssetTopic.topicName" label="资产主题" show-overflow-tooltip>
        </el-table-column>
        <el-table-column prop="serviceModel.modelName" label="服务模型名称" show-overflow-tooltip>
        </el-table-column>
        <el-table-column prop="dataServiceName" label="服务名称" show-overflow-tooltip>
        </el-table-column>
        <el-table-column prop="typeCode" label="服务类型" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.typeCode | modelTypeTransition }}
          </template>
        </el-table-column>
        <el-table-column label="服务状态">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.statusCode"
              active-color="#ffd700"
              inactive-color="#ddd"
              active-value="active"
              inactive-value="inactive"
              @change="changeServiceState(scope.row, scope.row.statusCode)">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="授权用户" show-overflow-tooltip>
            <template slot-scope="scope">
              <span v-for="(item, index) in scope.row.appUsers">{{ item.name }}  </span>
            </template>
        </el-table-column>
        <el-table-column label="更新时间" show-overflow-tooltip >
          <template slot-scope="scope">{{ scope.row.updateTime }}</template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <a title="查看" v-if="scope.row.appUsers.length >0" @click.stop="viewDetail(scope.row, 'authorized')"><svg-icon icon-class="eye" style="width: 19px; height: 19px"></svg-icon></a>
            <a title="查看" v-else @click.stop="viewDetail(scope.row, 'nouthorize')"><svg-icon icon-class="eye" style="width: 19px; height: 19px"></svg-icon></a>

            <a title="验证" v-if="scope.row.appUsers.length >0" @click.stop="validation(scope.row, 'authorized')"><svg-icon icon-class="valida02" style="width: 20px; height: 20px"></svg-icon></a>
            <a title="验证" v-else @click.stop="validation(scope.row, 'nouthorize')"><svg-icon icon-class="valida02" style="width: 20px; height: 20px"></svg-icon></a>

            <a title="授权" v-if="scope.row.appUsers.length >0" @click.stop="authoriza(scope.row, 'authorized')"><svg-icon icon-class="authorization" style="width: 20px; height: 20px"></svg-icon></a>
            <a title="授权" v-else @click.stop="authoriza(scope.row, 'nouthorize')"><svg-icon icon-class="authorization" style="width: 20px; height: 20px"></svg-icon></a>

            <a title="删除" @click.stop="deleteServiceUser(scope.row.id)"><svg-icon icon-class="delete" style="width: 20px; height: 20px"></svg-icon></a>
          </template>
        </el-table-column>
      </el-table> -->
      <cr-loading v-show="loading"></cr-loading>
      <list-card
        v-show="!loading"
        :listData="tableData.data"
        :isEdit="false"
        :isDel="false"
        :isOperation="true"
        :operatePermission="operatePermission"
        :operationBtns="operationBtns"
        :fieldsData="fieldsData"
        @delete="deleteServiceUser"
        @browse="viewDetail"
        @handleOperate="handleOperate"
        @handleSwitchClick="changeServiceState"
      ></list-card>
      <el-pagination
        background
        @current-change="(current)=>{this.loading=true;this.tableData.data=[];searchForm.pageNo = current;this.fetchDataServiceTable('2')}"
        :page-size="parseInt(searchForm.pageSize)"
        :current-page="parseInt(searchForm.pageNo)"
        layout="prev, pager, next"
        :total="tableData.page"
        class="fr mt-10">
      </el-pagination>
    </section>
    <!--查看弹框-->
    <!-- <el-dialog
      title="服务详情"
      :visible.sync="serviceDetailDialog"
      width="60%">
      <div class="form-body">
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="serviceDetailDialog = false" size="mini">关闭</el-button>
      </span>
    </el-dialog> -->
    <!--验证弹框-->
    <!-- <el-dialog
      title="验证"
      :visible.sync="validationDialog"
      width="60%">
      <div class="form-body">
        <div style="text-align: right; margin-bottom: 10px">
          <span style="display: inline-block; cursor: pointer; width: 66px; height: 28px; line-height: 28px;vertical-align: center;text-align: center;border: 1px solid #d7d7d7;border-radius: 5px;" @click="downloadSample(serviceValidForm, radioChooseValid)">样例下载</span>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="createValidation(serviceValidForm, radioChooseValid )">验证</el-button>
        <el-button @click="validationDialog = false" size="mini">关闭</el-button>
      </span>
    </el-dialog> -->
    <!--授权弹框-->
    <!-- <el-dialog
      title="授权"
      :visible.sync="authorizaDialog"
      width="60%"
      @close='cancelSource(serviceAuthForm)'>
      <div class="form-body">

      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="createAuthUser">保存</el-button>
        <el-button @click="authorizaDialog = false" size="mini">取消</el-button>
      </span>
    </el-dialog> -->

    <!-- 查看滑框 -->
    <slide-box
      slideWidth="600px"
      :slideShow="serviceDetailDialog"
      @close="slideClose"
      title="服务详情"
      ref="slideBox1">
      <div class="form-body" slot="slide-content">
        <el-collapse v-model="activeNames">
          <!-- 基本信息 -->
          <el-collapse-item title="基本信息" name="1">
            <ul class="basic-infor">
              <li>服务编号：<span>{{ serviceDetailForm.id }}</span></li>
              <li>服务名称：<span>{{ serviceDetailForm.dataServiceName }}</span></li>
              <li>服务类型：<span>{{ serviceDetailForm.typeCode | modelTypeTransition }}</span></li>
              <li>发布类型：<span>{{ serviceDetailForm.requestMethod }}</span></li>
              <li>操作类型：<span>{{ serviceDetailForm.operateType | operationType}}</span></li>
              <li><span style="display: inline-block; width: 62px;color: #999; text-align: right; font-size: 12px">状态：</span><span>{{ serviceDetailForm.statusCode | serviceStateTranst}}</span></li>
              <li>更新用户：<span>{{ serviceDetailForm.updateUser }}</span></li>
              <li>更新时间：<span>{{ serviceDetailForm.updateTime }}</span></li>
            </ul>
          </el-collapse-item>
          <!-- 授权信息 -->
          <el-collapse-item title="授权信息" name="2" v-if="viewDialogType == 'authorized'">
            <div class="clearfix view-dialog-body">
              <div class="mt-15">
                <div class="user-span">授权用户:</div>
                <ul class="auth-user-cont">
                  <ul class="clearfix">
                    <li class="fl" style="width:25%;color: #303133">APPID</li>
                    <li class="fl" style="width:25%;color: #303133">服务用户</li>
                    <li class="fl" style="width:25%;color: #303133">令牌</li>
                    <li class="fl" style="width:25%;color: #303133">操作</li>
                  </ul>
                  <el-radio-group v-model="radioChoose" @change="viewChooseUser" style="width: 100%">
                    <li v-for="(item,index) in serviceDetailForm.appUsers" :key="index" class="radio-list-user" :style="{'border-bottom':index===(serviceDetailForm.appUsers.length-1)?'none':'1px solid #ebeef5'}">
                      <el-radio :label="item.id" style="width: 70%">
                        <el-tooltip :content="item.appDs.appId" placement="top" effect="light">
                          <span class="app-id">{{ item.appDs.appId }}</span>
                        </el-tooltip>
                        <el-tooltip :content="item.name" placement="top" effect="light">
                          <span class="user-span-name">{{ item.name | textLimit(20)}}</span>
                        </el-tooltip>
                        <el-tooltip :content="item.appDs.token" placement="top" effect="light">
                          <span class="user-span-token">{{ item.appDs.token | textLimit(20) }}</span>
                        </el-tooltip>
                      </el-radio>
                      <span class="user-span-copy">
                        <a title="复制"
                          v-clipboard:copy="item.appDs.token"
                          v-clipboard:success="onCopy"
                          v-clipboard:error="onError">
                          <svg-icon icon-class="copy" style="width: 19px; height: 19px"></svg-icon>
                        </a>
                      </span>
                    </li>
                  </el-radio-group>
                </ul>
              </div>
              <!-- 能力模型目前没有授权属性 -->
              <div class="mt-15">
                <span class="user-span">{{ presentUserName }}授权属性：</span>
                <template>
                  <el-table
                    :data="viewAuthAttrList"
                    border
                    key="viewSttrTable"
                    size="mini"
                    :header-row-class-name="backgroundColorBorderChange"
                    class="mt-6">
                    <el-table-column label="属性名" header-align="center" align="center" prop="fieldName" show-overflow-tooltip>
                    </el-table-column>
                    <el-table-column label="描述" header-align="center" align="center" prop="fieldDesc" show-overflow-tooltip>
                    </el-table-column>
                  </el-table>
                </template>
              </div>
            </div>
          </el-collapse-item>
          <!--验证信息-->
          <el-collapse-item title="验证信息" name="3" v-if="serviceValidForm.appUsers.length !== 0">
            <div class="clearfix valid-dialog-body">
              <el-form ref="serviceAuthForm" :model="serviceValidForm" :rules="serviceAuthRule">
                <div class="mt-15">
                  <div class="user-span">授权用户</div>
                    <ul class="auth-user-cont">
                      <ul class="clearfix">
                        <li class="fl" style="width:30%; color: #303133">APPID</li>
                        <li class="fl" style="width:20%; color: #303133">服务用户</li>
                        <li class="fl" style="width:50%; color: #303133">令牌</li>
                      </ul>
                    <el-radio-group v-model="radioChooseValid" @change="validChooseUser" style="width: 100%">
                      <li v-for="(item,index) in serviceValidForm.appUsers" :key="index" class="radio-list-user" :style="{'border-bottom':index===(serviceValidForm.appUsers.length-1)?'none':'1px solid #ebeef5'}">

                        <el-radio :label="item.id" style="width: 100%">
                          <el-tooltip :content="item.appDs.appId" placement="top" effect="light">
                            <span class="app-id">{{ item.appDs.appId }}</span>
                          </el-tooltip>
                          <el-tooltip :content="item.name" placement="top" effect="light">
                            <span class="user-span-name">{{ item.name | textLimit(30)}}</span>
                          </el-tooltip>
                          <el-tooltip :content="item.appDs.token" placement="top" effect="light">
                            <span class="user-span-token">{{ item.appDs.token | textLimit(30) }}</span>
                          </el-tooltip>
                        </el-radio>
                      </li>
                    </el-radio-group>
                  </ul>
                </div>
                 <!-- 能力模型目前没有授权属性 -->
                <div class="mt-15">
                  <span class="user-span">{{ presentValidUserName }}授权属性</span>
                  <template>
                    <el-table
                      :data="validAuthAttrList"
                      border
                      ref="validSttrTable"
                      key="validSttrTable"
                      size="mini"
                      tooltip-effect="light"
                      @selection-change="handleSelectionChange"
                      :header-row-class-name="backgroundColorBorderChange"
                      class="mt-10">
                      <el-table-column label="属性名" header-align="center" align="center" prop="fieldName" show-overflow-tooltip>
                      </el-table-column>
                      <el-table-column label="描述" prop="fieldDesc" header-align="center" align="center" show-overflow-tooltip>
                      </el-table-column>
                      <!-- <el-table-column
                        type="selection"
                        align="center"
                        header-align="center"
                        class-name="el-column-center"
                        width="55"
                        :selectable="(row,index) => {return false}">
                      </el-table-column> -->
                    </el-table>
                  </template>
                </div>
                <!-- <div class="mt-15">
                  <span class="user-span">参数列表</span>
                  <el-table
                    :data="serviceValidForm.modelFilters"
                    border
                    size="mini"
                    :header-row-class-name="backgroundColorBorderChange"
                    class="mt-10">
                    <el-table-column label="参数名" header-align="center" align="center" show-overflow-tooltip>
                      <template slot-scope="scope">
                        <span style="color:red;" v-show="scope.row.isRequired == 'y'">*</span>
                        {{ scope.row.paramName }}
                      </template>
                    </el-table-column>
                    <el-table-column label="参数默认值" header-align="center" align="center" show-overflow-tooltip>
                      <template slot-scope="scope">
                        <el-input disabled v-model="scope.row.defaultValue" size="mini"></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column label="参数描述" header-align="center" align="center" show-overflow-tooltip>
                      <template slot-scope="scope">
                        <el-input v-model="scope.row.paramDesc" :disabled="true" size="mini"></el-input>
                      </template>
                    </el-table-column>
                  </el-table>
                </div> -->
              </el-form>
            </div>
          </el-collapse-item>
          <!-- 参数信息 -->
          <el-collapse-item title="参数信息" name="4">
            <template>
              <!-- <el-table
                :data="serviceDetailForm.modelFilters"
                border
                size="mini"
                :header-row-class-name="backgroundColorBorderChange"
                class="mt-15">
                <el-table-column label="参数名" header-align="center" align="center">
                  <template slot-scope="scope">
                    <span :class="{'must-before': scope.row.isRequired == 'y'}">{{ scope.row.paramName }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="数据类型" prop="paramType" header-align="center" align="center">
                </el-table-column>
                <el-table-column label="描述" prop="paramDesc" header-align="center" align="center">
                </el-table-column>
                <el-table-column label="参数默认值" prop="defaultValue" header-align="center" align="center">
                </el-table-column>
              </el-table> -->
              <el-table
                :data="serviceValidForm.modelFilters"
                border
                size="mini"
                :header-row-class-name="backgroundColorBorderChange"
                class="mt-10">
                <el-table-column label="参数名" header-align="center" align="center" show-overflow-tooltip>
                  <template slot-scope="scope">
                    <span style="color:red;" v-show="scope.row.isRequired == 'y'">*</span>
                    {{ scope.row.paramName }}
                  </template>
                </el-table-column>
                <el-table-column label="参数默认值" header-align="center" align="center" show-overflow-tooltip>
                  <template slot-scope="scope">
                    <!-- <cr-input-popver ref="crInputPopver1" :inputModel="scope.row.defaultValue" :disabled="true"></cr-input-popver> -->
                    <el-input v-if="scope.row.paramType === 'json'" type="textarea" disabled :autosize="{ minRows: 1, maxRows: 10}" v-model="scope.row.defaultValue" size="mini"></el-input>
                    <el-input v-else disabled v-model="scope.row.defaultValue" size="mini"></el-input>
                  </template>
                </el-table-column>
                <el-table-column label="参数描述" header-align="center" align="center" show-overflow-tooltip>
                  <template slot-scope="scope">
                    <el-input v-if="scope.row.paramType === 'json'" v-model="scope.row.paramDesc" :disabled="true" size="mini"></el-input>
                    <el-input v-else disabled v-model="scope.row.paramDesc" size="mini"></el-input>
                  </template>
                </el-table-column>
              </el-table>
            </template>
          </el-collapse-item>
        </el-collapse>
      </div>
      <span slot="slide-footer">
        <el-button size="mini" @click="closeSlide('1')">取消</el-button>
      </span>
    </slide-box>
    <!-- 验证滑框 -->
    <slide-box
      slideWidth="600px"
      :slideShow="validationDialog"
      @close="slideClose"
      title="验证"
      ref="slideBox2">
      <div class="form-body" slot="slide-content">
        <el-collapse v-model="activeNames">
          <!-- 基本信息 -->
          <el-collapse-item title="基本信息" name="1">
            <ul class="basic-infor">
              <li>服务编号：<span>{{ serviceValidForm.id }}</span></li>
              <li>服务名称：<span>{{ serviceValidForm.dataServiceName }}</span></li>
              <li>服务类型：<span>{{ serviceValidForm.typeCode | modelTypeTransition }}</span></li>
              <li>发布类型：<span>{{ serviceValidForm.requestMethod }}</span></li>
              <li>操作类型：<span>{{ serviceValidForm.operateType | operationType}}</span></li>
              <li><span style="display: inline-block; width: 62px;color: #999; text-align: right; font-size: 12px">状态：</span><span>{{ serviceValidForm.statusCode | serviceStateTranst }}</span></li>
              <li><span style="display: inline-block; width: 62px;color: #999; text-align: right; font-size: 12px">URL：</span><span>{{ serviceValidForm.url }}</span></li>
              <li>更新用户：<span>{{ serviceValidForm.updateUser }}</span></li>
              <li>更新时间：<span>{{ serviceValidForm.updateTime }}</span></li>
            </ul>
          </el-collapse-item>
          <!--验证信息-->
          <el-collapse-item title="验证信息" name="2" v-if="viewDialogType == 'authorized'">
            <div class="clearfix valid-dialog-body">
              <el-form ref="serviceAuthForm" :model="serviceValidForm" :rules="serviceAuthRule">
                <div class="mt-15">
                  <div class="user-span">授权用户</div>
                    <ul class="auth-user-cont">
                      <ul class="clearfix">
                        <li class="fl" style="width:30%; color: #303133">APPID</li>
                        <li class="fl" style="width:20%; color: #303133">服务用户</li>
                        <li class="fl" style="width:50%; color: #303133">令牌</li>
                      </ul>
                      <el-radio-group v-model="radioChooseValid" @change="validChooseUser" style="width: 100%">
                        <li v-for="(item,index) in serviceValidForm.appUsers" :key="index" class="radio-list-user" :style="{'border-bottom':index===(serviceValidForm.appUsers.length-1)?'none':'1px solid #ebeef5'}">

                          <el-radio :label="item.id" style="width: 100%">
                            <el-tooltip :content="item.appDs.appId" placement="top" effect="light">
                              <span class="app-id">{{ item.appDs.appId }}</span>
                            </el-tooltip>
                            <el-tooltip :content="item.name" placement="top" effect="light">
                              <span class="user-span-name">{{ item.name | textLimit(30)}}</span>
                            </el-tooltip>
                            <el-tooltip :content="item.appDs.token" placement="top" effect="light">
                              <span class="user-span-token">{{ item.appDs.token | textLimit(30) }}</span>
                            </el-tooltip>
                          </el-radio>
                        </li>
                      </el-radio-group>
                  </ul>
                </div>
                 <!-- 能力模型目前没有授权属性 -->
                <div class="mt-15" v-if="validAuthAttrList.length !== 0">
                  <span class="user-span">{{ presentValidUserName }}授权属性</span>
                  <template>
                    <el-table
                      :data="validAuthAttrList"
                      border
                      ref="validSttrTable"
                      key="validSttrTable"
                      size="mini"
                      tooltip-effect="light"
                      @selection-change="handleSelectionChange"
                      :header-row-class-name="backgroundColorBorderChange"
                      class="mt-10">
                      <el-table-column label="属性名" header-align="center" align="center" prop="fieldName" show-overflow-tooltip>
                      </el-table-column>
                      <el-table-column label="描述" prop="fieldDesc" header-align="center" align="center" show-overflow-tooltip>
                      </el-table-column>
                      <el-table-column
                        type="selection"
                        align="center"
                        header-align="center"
                        class-name="el-column-center"
                        width="55">
                      </el-table-column>
                    </el-table>
                  </template>
                </div>
                <div class="mt-15">
                  <span class="user-span">参数列表</span>
                  <!-- <el-tabs v-model="urlHeaderBodyChoose" type="card" v-if="searchForm.typeCode==='ability'&&serviceValidForm.requestMethod==='POST'&&serviceModel.expressionType==='url'">
                    <el-tab-pane label="head" name="first">
                      <el-table
                        :data="serviceValidForm.modelFilters"
                        border
                        size="mini"
                        :header-row-class-name="backgroundColorBorderChange"
                        class="mt-10">
                        <el-table-column label="参数名" header-align="center" align="center" show-overflow-tooltip>
                          <template slot-scope="scope">
                            <span style="color:red;" v-show="scope.row.isRequired == 'y'">*</span>
                            {{ scope.row.paramName }}
                          </template>
                        </el-table-column>
                        <el-table-column label="参数默认值" header-align="center" align="center" show-overflow-tooltip>
                          <template slot-scope="scope">
                            <el-input v-model="scope.row.defaultValue" size="mini"></el-input>
                          </template>
                        </el-table-column>
                      </el-table>
                    </el-tab-pane>
                    <el-tab-pane label="body" name="second">
                      <el-input type="textarea" class="mt-15" :rows="6" v-model="serviceValidForm.body"
                        placeholder="body"
                        size="medium"></el-input>
                    </el-tab-pane>
                  </el-tabs> -->
                  <el-table
                    :data="serviceValidForm.modelFilters"
                    border
                    size="mini"
                    :header-row-class-name="backgroundColorBorderChange"
                    class="mt-10">
                    <el-table-column label="参数名" header-align="center" align="center" show-overflow-tooltip>
                      <template slot-scope="scope">
                        <span style="color:red;" v-show="scope.row.isRequired == 'y'">*</span>
                        {{ scope.row.paramName }}
                      </template>
                    </el-table-column>
                    <el-table-column label="参数默认值" header-align="center" align="center" show-overflow-tooltip>
                      <template slot-scope="scope">
                        <!-- <cr-input-popver ref="crInputPopver2" :inputModel="scope.row.defaultValue" @ok="okFn($event,scope.row)" @blur="blurFn($event,scope.row)"></cr-input-popver> -->
                        <el-input v-if="scope.row.paramType === 'json'" type="textarea" :autosize="{ minRows: 1, maxRows: 10}" v-model="scope.row.defaultValue" size="mini"></el-input>
                        <el-input v-else v-model="scope.row.defaultValue" size="mini"></el-input>
                      </template>
                    </el-table-column>
                    <el-table-column label="参数描述" header-align="center" align="center" show-overflow-tooltip>
                      <template slot-scope="scope">
                        <el-input v-if="scope.row.paramType === 'json'" type="textarea" :autosize="{ minRows: 1, maxRows: 10}" v-model="scope.row.paramDesc" size="mini"></el-input>
                        <el-input v-else v-model="scope.row.paramDesc" size="mini"></el-input>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
              </el-form>
            </div>
          </el-collapse-item>
          <!--返回信息-->
          <el-collapse-item title="返回信息" name="3">
            <div class="clearfix mt-20">
              <div class="fl wd45">
                <span style="font-size: 12px; color: #555">参数样例</span>
                <cr-help-popver styleText="position:relative;top:3px;left:5px ">
                  <div slot="popver-content">
                    链接到
                    <a href="./dsp-help/dsp-help.html#wow30" target="_blank">
                      <el-button type="text" style="padding:0">参数样例</el-button>
                    </a>
                  </div>
                </cr-help-popver>
                <el-input class="mt-12 textarea-input" type="textarea" :autosize="{ minRows: 8, maxRows: 12}" v-model="paramJson"></el-input>
              </div>
              <div class="fr wd45">
                <span style="font-size: 12px; color: #555">结果样例</span>
                <cr-help-popver styleText="position:relative;top:3px;left:5px">
                  <div slot="popver-content">
                    链接到
                    <a href="./dsp-help/dsp-help.html#wow30" target="_blank">
                      <el-button type="text" style="padding:0">结果样例</el-button>
                    </a>
                  </div>
                </cr-help-popver>
                <el-input class="mt-12 textarea-input" type="textarea" :autosize="{ minRows: 8, maxRows: 12}" v-model="resultJson"></el-input>
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
      <span slot="slide-footer">
        <svg-icon icon-class="loading" style="width: 20px; height:20px;" v-if="validPic == 'loading'"></svg-icon>
        <svg-icon icon-class="checked" style="width: 20px; height:20px;" v-if="validPic == 'checked'"></svg-icon>
        <svg-icon icon-class="failure" style="width: 20px; height:20px;" v-if="validPic == 'failure'"></svg-icon>
        <el-tooltip class="item" :disabled="serviceValidForm.appUsers.length!==0" effect="light" content="未对用户授权" placement="top">
          <span>
            <el-button :disabled="validPic == 'loading'||serviceValidForm.appUsers.length===0" type="primary" size="mini" @click="createValidation(serviceValidForm, radioChooseValid, validAuthAttrList)">验证</el-button>
          </span>
        </el-tooltip>
        <el-button size="mini" @click="closeSlide('2')">取消</el-button>
        <div style="text-align: right;display: inline-block;margin-left: 6px;">
          <el-tooltip class="item" :disabled="serviceValidForm.appUsers.length!==0" effect="light" content="未对用户授权" placement="top">
            <span>
              <!-- serviceValidForm, radioChooseValid, validAuthAttrList -->
              <el-button size="mini" :disabled="serviceValidForm.appUsers.length===0" @click="getUrl(serviceValidForm, radioChooseValid, validAuthAttrList)">样例下载</el-button>
            </span>
          </el-tooltip>
        </div>
      </span>
    </slide-box>
    <!-- 授权滑框 -->
    <slide-box
      slideWidth="600px"
      :slideShow="authorizaDialog"
      @close="slideClose"
      title="授权"
      :subSlide="true"
      :subSlideShow="openAddUser"
      @subClose="slideSubClose"
      subTitle="添加用户"
      ref="slideBox3">
      <!-- 用户授权属性弹框 -->
      <div id="foo3" class="form-body" slot="slide-content">
        <el-collapse v-model="activeNames">
          <!-- 基本信息 -->
          <el-collapse-item title="基本信息" name="1">
            <ul class="basic-infor">
              <li>服务编号：<span>{{ serviceAuthForm.id }}</span></li>
              <li>服务名称：<span>{{ serviceAuthForm.dataServiceName }}</span></li>
              <li>服务类型：<span>{{ serviceAuthForm.typeCode | modelTypeTransition }}</span></li>
              <li>发布类型：<span>{{ serviceAuthForm.requestMethod }}</span></li>
              <li>操作类型：<span>{{ serviceAuthForm.operateType | operationType}}</span></li>
              <li><span style="display: inline-block; width: 62px;color: #999; text-align: right; font-size: 12px">状态：</span><span>{{ serviceAuthForm.statusCode | serviceStateTranst}}</span></li>
              <li>更新用户：<span>{{ serviceAuthForm.updateUser }}</span></li>
              <li>更新时间：<span>{{ serviceAuthForm.updateTime }}</span></li>
            </ul>
          </el-collapse-item>
          <!-- 授权信息 -->
          <el-collapse-item title="授权信息" name="2">
            <!-- 资产服务 -->
            <div class="clearfix auth-middle-body" v-if="searchForm.typeCode==='asset'">
              <div class="wd35 fl">
                <div class="auth-title">授权用户</div>
                <ul class="auth-list">
                  <el-radio-group v-model="radioChooseAuth" @change="radioChooseAuthChange">
                    <li v-for="(item,index) in authAllUser" :key="index" @click="radioChooseAuthChange(item.id)">
                      <el-radio :label="item.id">
                        <span>{{ item.name }}</span>
                      </el-radio>
                    </li>
                  </el-radio-group>
                </ul>
                <el-button class="mt-12" size="mini" @click="openAddAuthUser">添加用户</el-button>
              </div>
              <div class="wd60 fr">
                <span class="user-span">授权属性</span>
                <template>
                  <!-- <el-table
                    :data="serviceAuthForm.attrList"
                    border
                    ref="multipleTable"
                    key="AuthSttrTable"
                    size="mini"
                    :header-row-class-name="backgroundColorBorderChange"
                    class="mt-6">
                    <el-table-column label="属性名" header-align="center" align="center" show-overflow-tooltip prop="fieldName" >
                    </el-table-column>
                    <el-table-column label="描述" header-align="center" align="center" show-overflow-tooltip prop="fieldDesc">
                    </el-table-column>
                    <el-table-column
                      header-align="center" align="center" :render-header="renderHeader">
                      <template slot-scope="scope">
                        <el-checkbox v-model="serviceAuthForm.attrList[scope.$index].checked" @change="attrListsave(serviceAuthForm.attrList)"></el-checkbox>
                      </template>
                    </el-table-column>
                  </el-table> -->
                  <el-table
                    :data="serviceAuthFormAttrList"
                    border
                    ref="multipleTable"
                    key="AuthSttrTable"
                    size="mini"
                    :header-row-class-name="backgroundColorBorderChange"
                    class="mt-6">
                    <el-table-column label="属性名" header-align="center" align="center" show-overflow-tooltip prop="fieldName" >
                    </el-table-column>
                    <el-table-column label="描述" header-align="center" align="center" show-overflow-tooltip prop="fieldDesc">
                    </el-table-column>
                    <el-table-column
                      header-align="center" align="center" :render-header="renderHeader" class-name="el-column-center">
                      <template slot-scope="scope">
                        <el-checkbox v-model="serviceAuthFormAttrList[scope.$index].checked" @change="attrListsave(serviceAuthFormAttrList)"></el-checkbox>
                      </template>
                    </el-table-column>
                  </el-table>
                </template>
              </div>
            </div>
            <!-- 能力服务 -->
            <div class="mt-15" v-else>
              <el-transfer
                class="mt-15"
                v-model="dataAuth"
                filterable
                filter-placeholder="请输入用户名"
                @change="chooseUser"
                :left-default-checked="[]"
                :right-default-checked="[]"
                :titles="['用户列表', '授权用户']"
                :data="userDataListCope">
              </el-transfer>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
      <span slot="slide-footer">
        <el-button type="primary" :disabled="submitBtnDisabled" size="mini" @click="createAuthUser('3')">保存</el-button>
        <el-button size="mini" @click="closeSlide('3')">取消</el-button>
      </span>

      <!-- 添加用户弹框 -->
      <div class="form-body" slot="sub-slide-content">
        <template>
          <!-- :right-default-checked="userAuthRightChecked" -->
          <el-transfer
            class="mt-15"
            v-model="dataAuth"
            filterable
            filter-placeholder="请输入用户名"
            @change="chooseUser"
            :left-default-checked="[]"
            :right-default-checked="[]"
            :titles="['用户列表', '授权用户']"
            :data="userDataListCope">
          </el-transfer>
        </template>
      </div>
      <span slot="sub-slide-footer" class="dialog-footer">
        <el-button size="mini" type="primary" @click="createUser(dataAuth)">确定</el-button>
        <el-button size="mini" @click="closeSubSlide('3')">取消</el-button>
      </span>

    </slide-box>
  </div>
</template>

<script>
import request from '@/utils/request'
import ListCard from '@/components/ListCard'
import SlideBox from '@/components/SlideBox'
import CrLoading from '@/components/CrLoading'
import CrInputPopver from '@/components/CrInputPopver'
import CrHelpPopver from '@/components/CrHelpPopver'
export default {
  components:{
    ListCard,
    SlideBox,
    CrLoading,
    CrInputPopver,
    CrHelpPopver
  },
  data() {
    // 穿梭框数据
    return {
      operatePermission: true,
      submitBtnDisabled: false,
      validPic: 'start',
      loading: true,
      loadingTime: 0,
      foo: '',
      query: {
        name: ''
      },
      operationBtns: [
        {
          text: '验证',
          type: 'warning'
        },
        {
          text: '授权',
          type: 'default',
          isControl: true
          // disabled: !this.operationPer()
        }
      ],
      fieldsData: [],
      fieldsDataAsset:[
        {
          propsTitle: '资产领域',
          field: 'areaName'
        },
        {
          propsTitle: '资产主题',
          field: 'topicName'
        },
        {
          propsTitle: '数据模型名称',
          field: 'modelName',
          toolTip: true
        },
        {
          propsTitle: '服务名称',
          field: 'dataServiceName',
          toolTip: true
        },
        {
          propsTitle: '授权用户',
          field: 'appName',
          toolTip: true
        },{
          propsTitle: '操作类型',
          field: 'operateType'
        },
        {
          propsTitle: '服务状态',
          field: 'statusCode',
          switch: true
        }
      ],
      fieldsDataAbility: [
        {
          propsTitle: '大类',
          field: 'areaName'
        },
        {
          propsTitle: '子类',
          field: 'topicName'
        },
        {
          propsTitle: '数据模型名称',
          field: 'modelName'
        },
        {
          propsTitle: '服务名称',
          field: 'dataServiceName',
          toolTip: true
        },
        {
          propsTitle: '授权用户',
          field: 'appName',
          toolTip: true
        },
        {
          propsTitle: '操作类型',
          field: 'operateType'
        },
        {
          propsTitle: '服务状态',
          field: 'statusCode',
          switch: true
        }
      ],
      /* 表格搜索数据 */
      moreSearch: false,
      searchForm: {
        areaName: undefined,
        topicName: undefined,
        modelName: undefined,
        dataServiceName: undefined,
        typeCode: 'asset',
        statusCode: undefined,
        pageNo: '1',
        pageSize: '5',
        queryString: '',
        queryType: 'public'
      },
      statusCodeList: [],
      areaList: [], // 资产领域（下拉框内容）
      themeList: [], // 资产主题（下拉框内容）
      abilityBigList: [], // 能力大类（下拉框内容）
      abilitySubList: [], // 能力子类（下拉框内容）
      /******** table列表数据start ********/
      tableData: {
        data: null,
        page: undefined
      },
      activeNames: ['1'], // 折叠面板默认展开
      /******** table列表数据end ********/
      /******** 验证、授权、查看弹框start ********/
      /* 服务详情弹框 */
      serviceDetailDialog: false,
      viewDialogType: '',
      serviceDetailForm: {
        id: '', // 服务编号
        dataServiceName: '', // 服务名称
        typeCode: '',// 服务类型
        operateType: '',// 操作类型
        statusCode: '',// 状态
        updateTime: '',
        updateUser: '',
        // URL
        appUsers: [],
        modelParams: [] // 参数信息
      },
      viewAuthAttrList: [], // 对应用户对应属性信息
      radioChoose: '',
      presentUserName: '',

      /* 验证弹框 */
      validationDialog: false,
      serviceValidForm: {
        id: '', // 服务编号
        dataServiceName: '', // 服务名称
        typeCode: '',// 服务类型
        statusCode: '',// 状态,
        updateTime: '',
        updateUser: '',
        // URL
        appUsers: [],
        operateType: '',// 操作类型
        modelParams: [], // 参数信息
        body: ''// body模板
      },
      serviceModel: {},
      paramJson: '',  // 参数json
      resultJson: '',  // 结果json
      radioChooseValid: '',
      validAuthAttrList: [], // 对应用户对应属性信息
      baseUrl: '', // 样例下载
      presentValidUserName: '',
      validSelected: [], // 验证授权
      /* 授权用户弹框 */
        // 数据授权
      checkAll: false,
      authorizaDialog: false,
      authMassageType: 'asset', // 授权类型
      userDataList: [],  // 所有用户列表
      userDataListCope: [],
      dataAuth: [], // 数据服务-授权用户
      serviceAuthForm: {},  // 授权信息
      serviceAuthFormAttrList: [], // 授权属性
      authServiceSaveData: {
        id: '',  // 服务id
        appUsers: []  // 服务对应用户
      },

      openAddUser: false, // 打开添加用户弹框
      radioChooseAuth:'', // 要授属性的用户
      authAllUser: [], // 所有授权用户及信息
      authRightChecked: [], // 右边选择属性
      curretUser: '', // 存储当前选择的用户

        // api、消息授权
      massegaAuth: [], // 消息或者API授权

      /******** 验证、授权、查看弹框end ********/
      serviceAuthRule: {},
    }
  },
  methods: {
    okFn(val,row){
      row.defaultValue = val
      // val.oldValue = val.newValue
    },
    blurFn(val,row){
      row.defaultValue = val
    },
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
      this.serviceDetailDialog = false
      this.validationDialog = false
      this.authorizaDialog = false
      this.openAddUser = false
      this.activeNames = ['1']
      // console.log(this.$refs)
      if(this.$refs['crInputPopver1'] || this.$refs['crInputPopver2']){
        this.$refs['crInputPopver1'].inputModel2 = ''
        this.$refs['crInputPopver2'].inputModel2 = ''
      }
      this.paramJson = ''
      this.resultJson = ''
    },
    // 点击关闭二级弹框
    closeSubSlide(val){
      this.$refs.slideBox3.subClose()
    },
    // 关闭二级弹框
    slideSubClose(){
      this.openAddUser = false
    },

    handleOperate(val){
      // console.log(val)
      request({
        url: '/api/services/get_service',
        method: 'get',
        params: {
          id: val.operateObj.id
        }
      }).then(({ data }) => {
        // console.log(data.data)
        if(val.operateName === "验证"){
          // console.log(data.data.appUsers)
          if(data.data.appUsers.length > 0){
            this.validation(data.data, 'authorized');
          }else {
            this.validation(data.data, 'nouthorize');
          }
        }else if(val.operateName === "授权"){
          if(data.data.appUsers.length > 0){
            this.authoriza(data.data, 'authorized');
          }else {
            this.authoriza(data.data, 'nouthorize');
          }
        }
      })

    },
    //服务切换
    modelTab(type) {
      if (!this.isAdminOrAppUser() && type === 'ability'){
        this.$message.error('您无权限访问该数据！')
        return
      }
      this.searchForm.queryString = ''
      this.tableData.data = []
      this.loading = true
      this.searchForm.typeCode = type
      this.fetchDataServiceTable()
    },
    /****** 查询重置、获取状态码start*******/
    fetchStatusCode(){
      request({
        url: '/api/service/model/query_status',
        method: 'get',
        params: {
          type: 'service_status_code'
        }
      }).then(({ data }) => {
        this.statusCodeList = data.data
      })
    },
    resetQuery(){
      let typeCode = this.searchForm.typeCode
      this.searchForm = {
        areaName: undefined,
        topicName: undefined,
        modelName: undefined,
        dataServiceName: undefined,
        typeCode: typeCode,
        statusCode: undefined,
        pageNo: '1',
        pageSize: '5',
      }
    },
    /****** 查询重置、获取状态码end*******/
    /******** 精确查询下拉框内容start *********/
    queryAssetFirst(){
      request({
        url: '/api/dataasset/area/query_area',
        method: 'get',
      }).then(({ data }) => {
        if(data.code === 200){
          this.areaList = data.data.list
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{

        }
      })
    },
    queryAreaChange(name){
      this.searchForm.topicName = ''
      this.queryAssetSecond(name)
    },
    queryAssetSecond(name){
      let dataParam = {
        areaName: name
      }
      request({
        url: '/api/dataasset/topic/query_topic',
        method: 'get',
        params: dataParam
      }).then(({ data }) => {
        if(data.code === 200){
          this.themeList = data.data.list
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{

        }
      })
    },
    queryAbilityFirst(){
      request({
        url: '/api/service/model/query_code',
        method: 'get',
        params: {
          'parentCode': 'ability'
        }
      }).then(({ data }) => {
        if(data.code === 200){
          this.abilityBigList = JSON.parse(JSON.stringify(data.data))
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{

        }
      })
    },
    queryAbilityChange(name){
      this.searchForm.topicName = ''
      let code
      for(var i=0; i<this.abilityBigList.length; i++){
        if(this.abilityBigList[i].displayName == name){
          code = this.abilityBigList[i].code
        }
      }
      this.queryAbilitSecond(code)
    },
    queryAbilitSecond(code){
      request({
        url: '/api/service/model/query_code',
        method: 'get',
        params: {
          'parentCode': code
        }
      }).then(({ data }) => {
        if(data.code === 200){
          this.abilitySubList = JSON.parse(JSON.stringify(data.data))
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{
        }
      })
    },
    isAdminOrAppUser(){
      return this.$store.getters.userInfo.roles === 'administrator' || this.$store.getters.userInfo.roles === 'data_app'
    },
    /******** 精确查询下拉框内容end *********/
    /********* 获取table列表、用户列表（授权界面）start ********/
    // 获取数据服务table
    fetchDataServiceTable(val){
      this.loading = true;
      this.loadingTime = 500;
      if(val==='3'){
        this.loading = false;
        this.loadingTime = 0;
      }
      // this.tableData.data = []
      const obj = {}
      Object.keys(this.searchForm).forEach(key => {
        obj[key] = this.searchForm[key]
      })
      if(this.searchForm.typeCode == 'ability'){
        this.fieldsData = this.fieldsDataAbility
      }else if(this.searchForm.typeCode == 'asset'){
        this.fieldsData = this.fieldsDataAsset
      }
      if(val==='1'){// 更多查询
        this.query.name = ''
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
        url: '/api/services/query_service',
        method: 'get',
        params: obj
      }).then(({ data }) => {
        const self = this;
        setTimeout(function(){
          self.loading = false;
          if(data.code == 200){
            let tableData = JSON.parse(JSON.stringify(data.data.list))

            // for(var i=0; i<tableData.length; i++){
            //   tableData[i].appUsersName = []
            //   for(var j=0; j<tableData[i].appUsers.length; j++){
            //     tableData[i].appUsersName.push(
            //       tableData[i].appUsers[j].name
            //     )
            //   }
            // }
            // for(var i=0; i<tableData.length; i++){
            //   tableData[i].appUsersName = tableData[i].appUsersName.join(',')
            // }
            self.tableData.data = tableData
            self.tableData.page = parseInt(data.data.count)
          }else if(data.code === 4010){
            self.$store.dispatch('LogOut').then(() => {
              location.reload()
            })
          }else{
            // this.$message.error(data.message)
          }
        },self.loadingTime)

      })
    },
    // 获取用户列表（授权界面）
    fetchUserList(){
      request({
        url: '/api/services/query_auth_app',
        method: 'get',
      }).then(({ data }) => {
        if(data.code === 200){
          for(var i=0; i<data.data.length; i++){
            this.userDataList.push(
              {
                key: i, // 所有用户index， 用于之后的Arr[0]
                label: data.data[i].name,
                id: data.data[i].id,
                name: data.data[i].name,
                appOwner: data.data[i].appOwner,
              }
            )
            this.userDataListCope = JSON.parse(JSON.stringify(this.userDataList))
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
    /********* 获取table列表、用户列表（授权界面）end ********/
    /********* 查看、验证与授权start ********/
    // 查看详情
    viewDetail(row, type){
       request({
        url: '/api/services/get_service',
        method: 'get',
        params: {
          id: row.id
        }
      }).then(({data}) => {
        if(data.code === 200){
          const row = data.data
          this.viewDialogType = type
          this.serviceDetailDialog = true
          this.serviceDetailForm.id = row.id
          this.serviceDetailForm.dataServiceName = row.dataServiceName
          this.serviceDetailForm.typeCode = row.typeCode
          this.serviceDetailForm.statusCode = row.statusCode
          this.serviceDetailForm.requestMethod = row.requestMethod
          this.serviceDetailForm.operateType = row.operateType
          this.serviceDetailForm.updateTime = row.updateTime
          this.serviceDetailForm.updateUser = row.updateUser
          if(type == "authorized"){
            this.serviceDetailForm.appUsers = row.appUsers
            this.serviceDetailForm.modelFilters = row.modelFilters
            // 赋初值
            this.viewAuthAttrList = this.serviceDetailForm.appUsers[0].dsFields
            this.radioChoose = this.serviceDetailForm.appUsers[0].id
            this.presentUserName = this.serviceDetailForm.appUsers[0].name
          }
          this.serviceValidForm.id = row.id
          this.serviceValidForm.dataServiceName = row.dataServiceName
          this.serviceValidForm.typeCode = row.typeCode
          this.serviceValidForm.statusCode = row.statusCode
          this.serviceValidForm.requestMethod = row.requestMethod
          this.serviceValidForm.operateType = row.operateType
          this.serviceValidForm.updateTime = row.updateTime
          this.serviceValidForm.updateUser = row.updateUser
          if(this.searchForm.typeCode === 'ability'&&row.requestMethod === 'POST'){
            this.serviceModel = row.serviceModel
          }
          // if( type == 'authorized'){
            this.serviceValidForm.appUsers = row.appUsers
            for(var i=0; i<this.serviceValidForm.appUsers.length; i++){
              for(var j=0; j<this.serviceValidForm.appUsers[i].dsFields.length; j++){
                this.serviceValidForm.appUsers[i].dsFields[j].checked = true;
              }
            }
            this.serviceValidForm.modelFilters = JSON.parse(JSON.stringify(row.modelFilters))
            // 赋初值
            if(this.serviceValidForm.appUsers.length !== 0){
              this.validAuthAttrList = JSON.parse(JSON.stringify(this.serviceValidForm.appUsers[0].dsFields))
              this.radioChooseValid = this.serviceValidForm.appUsers[0].id
              this.presentValidUserName = this.serviceValidForm.appUsers[0].name
            }else{
              this.validAuthAttrList = []
              this.radioChooseValid = ''
              this.presentValidUserName =''
            }
            const self = this
            // setTimeout(function(){
            //   self.validAuthAttrList.forEach(row => {
            //     self.$refs.validSttrTable.toggleRowSelection(row)
            //   })
            // },0)
          // }
        }
      })
    },
    // 选择不同用户显示不同属性
    viewChooseUser(label){
      for(var i=0; i<this.serviceDetailForm.appUsers.length; i++){
        if(label == this.serviceDetailForm.appUsers[i].id){
          this.viewAuthAttrList = this.serviceDetailForm.appUsers[i].dsFields
          this.presentUserName = this.serviceDetailForm.appUsers[i].name
        }
      }
    },

    // 验证详情
    validation(row, type){
      this.serviceValidForm.appUsers = []
      this.validPic = 'start'
      this.viewDialogType = type
      this.validationDialog = true
      this.serviceValidForm.id = row.id
      this.serviceValidForm.dataFormat = row.dataFormat
      this.serviceValidForm.dataServiceName = row.dataServiceName
      this.serviceValidForm.typeCode = row.typeCode
      this.serviceValidForm.url = row.url
      this.serviceValidForm.statusCode = row.statusCode
      this.serviceValidForm.requestMethod = row.requestMethod
      this.serviceValidForm.operateType = row.operateType
      if(this.searchForm.typeCode === 'ability'&&row.requestMethod === 'POST'){
        this.serviceModel = row.serviceModel
      }
      this.validSelected = [];
      if( type == 'authorized'){
        // console.log(row.appUsers)
        this.serviceValidForm.appUsers = row.appUsers
        for(var i=0; i<this.serviceValidForm.appUsers.length; i++){
          for(var j=0; j<this.serviceValidForm.appUsers[i].dsFields.length; j++){
            this.serviceValidForm.appUsers[i].dsFields[j].checked = true;
          }
        }
        this.serviceValidForm.modelFilters = JSON.parse(JSON.stringify(row.modelFilters))
        // 赋初值
        this.validAuthAttrList = JSON.parse(JSON.stringify(this.serviceValidForm.appUsers[0].dsFields))
        // console.log(this.validAuthAttrList.length)
        this.radioChooseValid = this.serviceValidForm.appUsers[0].id
        this.presentValidUserName = this.serviceValidForm.appUsers[0].name
        const self = this
        setTimeout(function(){
          self.validAuthAttrList.forEach(row => {
            self.$refs.validSttrTable.toggleRowSelection(row)
          })
        },0)

      }else{
        console.log(this.serviceValidForm.appUsers)
        if(row.appUsers.length === 0){
          this.validAuthAttrList = []
        }else{
          this.validAuthAttrList = JSON.parse(JSON.stringify(this.serviceValidForm.appUsers[0].dsFields))
        }
      }
      // this.getUrl()
    },
    // 选择不同用户显示不同属性
    validChooseUser(label){
      this.validSelected = [];
      for(var i=0; i<this.serviceValidForm.appUsers.length; i++){
        if(label == this.serviceValidForm.appUsers[i].id){
          this.validAuthAttrList = JSON.parse(JSON.stringify(this.serviceValidForm.appUsers[i].dsFields))
          this.presentValidUserName = this.serviceValidForm.appUsers[i].name
        }
      }
      const self = this
      setTimeout(function(){
        self.validAuthAttrList.forEach(row => {
          self.$refs.validSttrTable.toggleRowSelection(row)
        })
      },0)
    },
    // 全选事件 render函数
    handleSelectionChange(val){
      this.validSelected = val
    },
    // 点击验证事件
    createValidation(form, id, attr){
      this.activeNames.push('3')
      let isRequiredValid = true
      let params
      params = form.appUsers.filter(function(item, index,arr){
        return item.id == id
      })
      form.dataFormat = form.dataFormat === '' ? undefined : form.dataFormat
      let paramData = {
        dataServiceId: form.id,
        token: params[0].appDs.token,
        appId: params[0].id,
        dataFormat: form.dataFormat,
        data: {
          fields: [],
          params: []
        }
      }
      // 属性列表
      for(var i=0; i<this.validSelected.length; i++){
        paramData.data.fields.push(
          {
            fieldName: this.validSelected[i].fieldName
          }
        )
      }
      // 参数列表
      for(var j=0; j<form.modelFilters.length; j++){
        if(form.modelFilters[j].isRequired == 'y'){
          if(form.modelFilters[j].defaultValue === ''){
            isRequiredValid = false
            this.$message.error('请填写必填项')
            break;
          }else{
            isRequiredValid = true
          }
        }
        if(form.modelFilters[j].defaultValue != ''){
            if(form.modelFilters[j].paramType === 'json'){
              try {
                // console.log(form.modelFilters[j].defaultValue)
                const obj=JSON.parse(form.modelFilters[j].defaultValue);
                paramData.data.params.push(
                  {
                    name: form.modelFilters[j].paramName,
                    value: obj
                  }
                )
              } catch(e) {
                this.$message.error('JSON格式错误！');
                return false;
              }
            }else{
              paramData.data.params.push(
                {
                  name: form.modelFilters[j].paramName,
                  value: form.modelFilters[j].defaultValue
                }
              )
            }
        }

      }
      if(this.serviceValidForm.requestMethod==='POST'&&this.serviceModel.expressionType==='url'){
        // console.log(this.serviceValidForm)
        // try {
        //   const obj=JSON.parse(this.serviceValidForm.body);
        //   if(typeof obj == 'object' && obj ){
        //     paramData.body = obj
        //   }
        // } catch(e) {
        //   this.$message.error('body模板JSON格式错误！');
        //   return false;
        // }
      }
      const paramData2 = {}
      $.extend(true,paramData2,paramData)
      if(form.requestMethod === 'POST'){
        delete paramData2.token
        delete paramData2.appId
      }
      // console.log(paramData2)
      if(form.requestMethod === 'GET'){
        // if(paramData2.data.params.length !== 0){
          let paramStr = ''
          paramStr += this.serviceValidForm.url+'?token='+paramData2.token+'&appid='+paramData2.appId+'&dataServiceId='+paramData2.dataServiceId
          paramData2.data.params.forEach((item,index) => {
            paramStr += '&' + item.name + '=' + item.value
            // if(index !== paramData2.data.params.length-1){
            //   paramStr += '&'
            // }
          })
          // paramData2.data.params = paramStr
          this.paramJson = paramStr
        // }else{
        //   let paramStr = ''
        //   console.log(paramData2)
        //   console.log(this.serviceValidForm)
        // }
      }else{
        // console.log(form)
        this.paramJson = JSON.stringify(paramData2,null,2)
      }
      // console.log('paramData2:', paramData2)
      // console.log('paramData:', paramData)

      if(isRequiredValid){
        this.validPic = 'loading'
        request({
          url:'/api/services/verify_service',
          method: 'post',
          data: paramData,
        }).then(({data}) => {
          console.log(data)
          if(data.code == 200){
            // const obj = JSON.parse(data.data)
            // obj.code = '200'
            this.resultJson = data.data
            this.$message.success('验证完成');
            this.validPic = 'checked'
          }else if(data.code === 4010){
            this.$store.dispatch('LogOut').then(() => {
              location.reload()
            })
          } else {
            this.validPic = 'failure'
            this.$message.error(data.message)
          }
        })
      }
    },
    downLoad(options) {
      var config = $.extend(true, { method: 'post' }, options);
      var $iframe = $('<iframe id="down-file-iframe" />');
      var $form = $('<form target="down-file-iframe" method="' + config.method + '" />');
      $form.attr('action', config.url);
      // console.log("<input type='hidden' name='version' value='" + JSON.stringify(config.data) + "'/>")
      $form.append("<input type='hidden' name='version' value='" + JSON.stringify(config.data) + "'/>");
      $iframe.append($form);
      $(document.body).append($iframe);
      $form[0].submit();
      $iframe.remove();
    },
    // 样例下载form, id, attr
    getUrl(form, id, attr){
      // this.baseUrl = process.env.BASE_API + '/api/services/download_service' + '?id='+this.serviceValidForm.id
      // if(val === '1'){
      //   window.location.href = this.baseUrl
      // }
      let isRequiredValid = true
      let params
      params = form.appUsers.filter(function(item, index,arr){
        return item.id == id
      })
      form.dataFormat = form.dataFormat === '' ? undefined : form.dataFormat
      let paramData = {
        dataServiceId: form.id,
        token: params[0].appDs.token,
        appId: params[0].id,
        dataFormat: form.dataFormat,
        data: {
          fields: [],
          params: []
        }
      }
      // console.log(this.validAuthAttrList)
      // 属性列表
      for(var i=0; i<this.validAuthAttrList.length; i++){
        paramData.data.fields.push(
          {
            fieldName: this.validAuthAttrList[i].fieldName,
            fieldId: this.validAuthAttrList[i].fieldId
          }
        )
      }
      // 参数列表
      for(var j=0; j<form.modelFilters.length; j++){
        // if(form.modelFilters[j].isRequired == 'y'){
        //   if(form.modelFilters[j].defaultValue === ''){
        //     isRequiredValid = false
        //     this.$message.error('请填写必填项')
        //     break;
        //   }else{
        //     isRequiredValid = true
        //   }
        // }
        // if(form.modelFilters[j].defaultValue != ''){
          // console.log(form.modelFilters[j].defaultValue!=='')
          // if(form.modelFilters[j].defaultValue!==''){
            if(form.modelFilters[j].paramType === 'json'){
              try {
                let value = ''
                console.log(form.modelFilters[j].defaultValue)

                  const obj=JSON.parse(form.modelFilters[j].defaultValue);
                  value = obj
                  value = JSON.stringify(obj)
                  paramData.data.params.push(
                    {
                      name: form.modelFilters[j].paramName,
                      value: value,
                      required: form.modelFilters[j].isRequired,
                      type: form.modelFilters[j].paramType,
                      desc: form.modelFilters[j].paramDesc
                    }
                  )

                // console.log(form.modelFilters[j].defaultValue)
              } catch(e) {
                this.$message.error('JSON格式错误！');
                return false;
              }
            }else{
              paramData.data.params.push(
                {
                  name: form.modelFilters[j].paramName,
                  value: form.modelFilters[j].defaultValue,
                  required: form.modelFilters[j].isRequired,
                  type: form.modelFilters[j].paramType,
                  desc: form.modelFilters[j].paramDesc
                }
              )
            }
          // }

        // }

      }
      if(isRequiredValid){
        const Base64 = require('js-base64').Base64
        // console.log(process.env.BASE_API)
        this.downLoad({
          url: process.env.BASE_API+'/api/services/download_service',
          data: paramData
        })
        // request({
        //   url: '/api/services/download_service',
        //   method: 'post',
        //   params: {
        //     version: Base64.encode(JSON.stringify(paramData))
        //   }
        // }).then(res => {
        //   console.log(res)
        // })
      }
    },
    //回车搜索
    enterKeyEv(){
      const self = this
      $(document).on('keyup',function(ev){
        // console.log(ev.keyCode === 13, $('#test-select').is(':focus'))
        // console.log($('#test-select').val())
        if(ev.keyCode === 13&&$('.do-search-input').is(':focus')){
          self.fetchDataServiceTable()
        }
      });
    },

    // 授权弹框
    authoriza(row, type){
      this.viewDialogType = type
      this.authorizaDialog = true
      this.dataAuth = []
      this.serviceAuthFormAttrList = []
      this.radioChooseAuth = ''
      this.authServiceSaveData = {
        id: '',
        appUsers: []
      }
      // 获取所有用户（穿梭框左边数据）
      this.userDataListCope = JSON.parse(JSON.stringify(this.userDataList))
      // 保存数据服务的id （前端另存）
      this.authServiceSaveData.id = row.id
      // 授权保存信息(在界面显示)
      this.serviceAuthForm = row // 获取基本信息
      this.serviceAuthForm.attrList = [] // 获取所有属性

      // 获取所有授权用户 (一级弹框左边)
      this.authAllUser = JSON.parse(JSON.stringify(this.serviceAuthForm.appUsers))

      // 获取所有授权用户（二级弹框右侧）
      if(type == 'nouthorize'){
      } else if(type == 'authorized'){
        for(var i=0; i<row.appUsers.length; i++){
          for(var j=0; j<this.userDataListCope.length; j++){
            if(row.appUsers[i].id == this.userDataListCope[j].id){
              this.dataAuth.push(j)
            }
          }
        }
      }

    },
    // 选择一个授权用户获取属性
    radioChooseAuthChange(label){
      this.curretUser = label
      this.serviceAuthForm.attrList = []
      // console.log(this.serviceAuthForm.modelParams)
      this.serviceAuthForm.modelParams.forEach(item => {
        const obj ={}
        Object.keys(item).forEach(key => {
          obj[key] = item[key]
        })
         this.serviceAuthForm.attrList.push(obj)
      })
      // console.log(this.serviceAuthForm.attrList)
      this.serviceAuthFormAttrList = JSON.parse(JSON.stringify(this.serviceAuthForm.modelParams))
      this.checkAll = true
      this.$set(this,'checkAll',true)
      for(var j=0; j<this.authAllUser.length; j++){
        if(this.authAllUser[j].id === label){
          for(var i=0; i<this.serviceAuthFormAttrList.length; i++){
            // this.serviceAuthFormAttrList[i].checked = false
            this.$set(this.serviceAuthFormAttrList[i], 'checked', false)
            for(var k=0; k<this.authAllUser[j].dsFields.length; k++){
              if(this.authAllUser[j].dsFields[k].fieldId ===  this.serviceAuthFormAttrList[i].id && this.authAllUser[j].dsFields[k].fieldName ===  this.serviceAuthFormAttrList[i].fieldName){
                // this.serviceAuthFormAttrList[i].checked = true
                this.$set(this.serviceAuthFormAttrList[i], 'checked', true)
              }
            }
          }
        }
      }

    },
    // 全选
    handleCheckAllChange(val){
      this.checkAll = val;
      if(val){
        for(var i=0; i<this.serviceAuthForm.attrList.length; i++){
          this.$set(this.serviceAuthFormAttrList[i], 'checked', true)
        }
      }else {
        for(var i=0; i<this.serviceAuthFormAttrList.length; i++){
          this.$set(this.serviceAuthFormAttrList[i], 'checked', false)
        }
      }
      // 全选 改变最后传递的数据（与attrListsave里面的逻辑一样）
      let flag
      for(var j=0; j<this.authAllUser.length; j++){
        if(this.authAllUser[j].id == this.curretUser){
          flag = j;
          break;
        }
      }
      this.authAllUser[flag].dsFields = []
      for(var i=0; i<this.serviceAuthFormAttrList.length; i++){
        if(this.serviceAuthFormAttrList[i].checked == true){
          this.authAllUser[flag].dsFields.push(
            {
              fieldName: this.serviceAuthFormAttrList[i].fieldName,
              fieldId: this.serviceAuthFormAttrList[i].id
            }
          )
        }
      }
    },
    // render函数
    renderHeader(h,{column}){
      return h('el-checkbox',
        {
          attrs: {
            id: 'foo'
          },
          on: {
            change: this.handleCheckAllChange,
          }
        }
      )
    },
    // 属性授权后
    attrListsave(selection){
      let flag
      for(var j=0; j<this.authAllUser.length; j++){
        if(this.authAllUser[j].id == this.curretUser){
          flag = j;
          break;
        }
      }
      this.authAllUser[flag].dsFields = []
      for(var i=0; i<selection.length; i++){
        if(selection[i].checked == true){
          this.authAllUser[flag].dsFields.push(
            {
              fieldName: selection[i].fieldName,
              fieldId: selection[i].id
            }
          )
        }
      }
    },
    // 授权保存
    createAuthUser(val){
      this.submitBtnDisabled = true
      let paramData = {
        id: this.authServiceSaveData.id,
        appUsers: []
      }
      // 得到用户id
      this.authAllUser.forEach( cur => {
        paramData.appUsers.push(
          {
            id: cur.id,
            dsFields: []
          }
        )
      })
      // 得到用户字段
      this.authAllUser.forEach( (cur,index, arr) => {
        cur.dsFields.forEach( item => {
          paramData.appUsers[index].dsFields.push(
            {
              fieldName: item.fieldName,
              fieldId: item.fieldId
            }
          )
        })
      })
      request({
        url:'/api/services/auth_service',
        method: 'post',
        data: paramData,
      }).then(({data}) => {
        if(data.code == 200){
          this.$message.success('授权成功');
          this.closeSlide(val)
          this.fetchDataServiceTable('3')
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

    // 增加授权用户弹框
    openAddAuthUser(){
      this.openAddUser = true
    },
    // 选择授权用户
    chooseUser(curretArr,directive,Arr){
      // 右移增加
      if(directive == 'right'){
        for(var i=0; i<Arr.length; i++){
          this.authAllUser.push(
            {
              id: this.userDataListCope[Arr[i]].id,  // 用户id
              name: this.userDataListCope[Arr[i]].name,
              dsFields: []
            }
          )

        }
      }else {
        for(var i=0; i<Arr.length; i++){
            // 获取Arr[i]的id 然后载根据id删除
          let id = this.userDataListCope[Arr[i]].id
          this.authAllUser = this.authAllUser.filter(function(item, index,arr){
            return item.id != id
          })
        }
      }

    },
    // 添加授权用户-确定函数
    createUser(){
      this.openAddUser = false
      this.closeSubSlide()
    },

    /********* 查看、验证与授权end ********/

    /******** 改变服务状态start *********/
    // 状态改变
    changeServiceState(val){
      let state;
      let paramData;
      if(val.state == false){
        state = 'inactive'
      }else{
        state = 'active'
      }
      paramData = {
        id: val.item.id,
        statusCode: state
      }
      this.tableData.data.forEach((item, index)=>{
        if(item.id == val.item.id){
          item.statusCode = state
        }
      })
      request({
        url: '/api/services/change_service_status',
        method: 'get',
        params: paramData
      }).then(({ data }) => {
        if(data.code == 200){
          if(val.state){
            this.$message.success({ message: '启动成功' });
          }else{
            this.$message.success({ message: '禁用成功' });
          }
          // this.fetchDataServiceTable('3')
          this.modifyData(data.data)
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{
          this.modifyData(data.data)
        }
      })
    },
    // 单条数据修改保存
    modifyData(val){
      const _index = this.tableData.data.indexOf(this.tableData.data.find(item => item.id === val.id))
      this.tableData.data[_index] = val
    },
    // 删除服务用户
    deleteServiceUser(val) {
      this.$confirm('确定删除此数据服务？', '提示', {
        type: 'warning'
      }).then(() => {
        request({
          url: '/api/services/del_service',
          method: 'post',
          data: {
            id: val
          }
          }).then(({data}) => {
            if(data.code == 200){
              this.$message.success({
                message: '删除成功'
              });
              this.fetchDataServiceTable();
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
    // 取消弹框
    cancelSource(){
      // this.authorizaDialog = false
      // this.$refs.serviceAuthForm.resetFields()
      // this.$refs.serviceAuthForm.resetFields()
      // this.$refs.serviceAuthForm.resetFields()
      // this.$refs.serviceAuthForm.resetFields()
    },
    /******** 改变服务状态end *********/
    // 复制粘贴
    onCopy: function (e) {
      // alert('You just copied: ' + e.text)
    },
    onError: function (e) {
      // alert('Failed to copy texts')
    },
    // el-table的背景边框改变函数
    titleBgColor({row, rowIndex}){
      return 'title-bg-color'
    },
    // el-table的背景边框改变函数
    backgroundColorBorderChange({row, rowIndex}){
      return 'table-header-border-bg'
    },
  },
  watch: {
    serviceAuthFormAttrList(curVal, oldVal){
      //
    },
    // immediate: true,
    deep: true
  },
  created() {
    this.fetchDataServiceTable()
    this.fetchUserList()
    this.fetchStatusCode()
    this.queryAssetFirst()
    this.queryAbilityFirst()
  },
  mounted() {
    this.enterKeyEv()
    this.$store.getters.userInfo.menu.forEach(item => {
      item.children.forEach(subItem => {
        if(subItem.name === '数据服务管理'){
          this.operatePermission = subItem.operationIds.indexOf('3') !== -1
        }
      })
    })
  },
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
  .model-tab{
    float: left;
    margin-left: 130px;
    span{
      font-size: 16px;
      cursor: pointer;
      color: #555;
      border-bottom: 2px solid transparent;
      display: inline-block;
      height: 100%;
      &:nth-child(2){
        margin: 0 55px;
        cursor: auto;
      }
    }
    .select-tab{
      color: #4562fc;
      border-color: #4562fc;
    }
  }
  .titile {
    color: #444;
    font-size: 23px;
  }
}
.span-click{
  display: inline-block;
  width: 100%;
  cursor: pointer
}
.section-attr {
  padding: 2px 20px;
  p {
    color: #999;
    height: 28px;
    line-height: 28px;
  }
}
.textr{
  text-align: right;
}
.downloadSample {
  display: inline-block;
  cursor: pointer;
  width: 66px;
  height: 23px;
  line-height: 23px;
  vertical-align: center;
  text-align: center;
  border: 1px solid #d7d7d7;
  border-radius: 5px;
  font-size: 12px;
  color: #606266;
}
.form-body {
  padding: 0px 30px;
  overflow:auto;
  .basic-infor {
    li {
      height: 30px;
      line-height: 30px;
      font-size: 12px;
      color: #999999;
      span{
        font-size: 12px;
        color: #555;
      }
    }
  }
  .view-dialog-body {
    .user-span{
      font-size: 12px;
      color: #999;
    }
    .auth-user-cont{
      border: 1px solid #ebeef5;
      margin-top: 6px;
      ul {
        background-color: rgba(246,247,250,1);
        height: 30px;
        line-height: 30px;
        li {
          font-size: 12px;
          color: #7f8c8d;
          text-align: center
        }
      }
      .radio-list-user{
        width: 100%;
        height: 30px;
        line-height: 30px;
        padding-left: 20px;
        .app-id{
          display: inline-block;
          width: 25%;
        }
        .user-span-name {
          display: inline-block;
          color: #555;
          width: 38%;
          text-align: center;
        }
        .user-span-token {
          display: inline-block;
          color: #555;
          width: 50%;
        }
        .user-span-copy{
          display: inline-block;
          width: 25%;
          float: right;
          text-align: center;
        }
      }
    }
  }
  .valid-dialog-body{
    .user-span{
      font-size: 12px;
      color: #999;
    }
    .auth-user-cont{
      border: 1px solid #ebeef5;
      margin-top: 6px;
      ul {
        background-color: rgba(246,247,250,1);
        height: 30px;
        line-height: 30px;
        li {
          font-size: 12px;
          color: #7f8c8d;
          text-align: center
        }
      }
      .radio-list-user{
        width: 100%;
        height: 30px;
        line-height: 30px;
        padding-left: 25px;
        .app-id{
          display: inline-block;
          width: 27%;
        }
        .user-span-name {
          width: 20%;
          color: #555;
          display: inline-block;
        }
        .user-span-token {
          display: inline-block;
          color:#555;
          width: 50%;
        }
      }
    }
  }
}
.api-dialog-body {
  padding: 14px 15px;
  .data-valid {
    width: 30%;
    .auth-title{
      height: 30px;
      line-height: 30px;
      padding-left: 15px;
      background: #f5f7fa;
    }
    div:nth-child(2){
      padding: 15px 18px;
    }
    ul {
      padding: 0px 18px;
      li {
        height: 30px;
        line-height: 30px;
      }
    }
  }
}
.auth-middle-body {
  padding: 14px 15px;
  .auth-title {
    font-size: 12px;
    color: #999;
    height: 30px;
    line-height: 30px;
  }
  .auth-list {
    border: 1px solid #ebeef5;
    padding: 6px 18px 20px 18px;
    li {
      height: 30px;
      line-height: 30px;
    }
  }
  .user-span{
    font-size: 12px;
    color: #999;
  }
}
.must-before {
  position: relative;
}
.must-before:before {
  content: "*";
  color: #F56461;
  position: absolute;
  left: -10px;
  top: -2px;
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