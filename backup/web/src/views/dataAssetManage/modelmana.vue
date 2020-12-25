<!--服务模型管理界面-->
<template>
  <div>
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">服务模型管理</h2>
          <div class="model-tab">
            <span :class="{'select-tab':searchForm.typeCode==='asset'}" @click="modelTab('asset')">资产模型</span>
            <span>|</span>
            <span :style="{'cursor':isAdmin()?'pointer':'not-allowed','color':isAdmin()?'':'#bbb'}" :class="{'select-tab':searchForm.typeCode==='ability'}" @click="modelTab('ability')">能力模型</span>
          </div>
        </div>
        <div class="fr">
          <el-button class="more-search" @click="showMoreSearch">
            更多查询 <svg-icon icon-class="spread" :class="{'retract':moreSearch}"></svg-icon>
          </el-button>
        </div>
        <div class="search fr">
          <input class="do-search-input" type="text" style="padding-left: 15px; width: 200px; outline: none; border: 0; font-size: 12px; display: inline-block; height: 100%; float: left;" v-model="query.name" placeholder="请输入关键字"></input>
          <a href="javascript:;" @click="fetchAssetFieldTable"><svg-icon icon-class="search"></svg-icon></a>
        </div>
        <div class="fr">
          <el-button class="oper-btn" @click="batchExport" type="primary" style="margin-right:10px;">批量导出</el-button>
          <input id="modelFile" type="file" style="display:none" @change="importSourceFile">
          <el-button class="oper-btn" @click="batchImport" type="primary">批量导入</el-button>
          <el-button class="add-btn" type="primary" @click="handleCreate" :disabled="!operatePermission"><i>+</i>新增</el-button>
        </div>
      </div>
      <!-- 精确查询 -->
      <div class="clearfix more-search-box">
        <el-form ref="form" :model="searchForm">
          <div class="clearfix input-group">
            <!-- 资产模型 -->
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
            <!-- 能力模型 -->
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

            <el-form-item label="数据源名称" style="width: 236px;" class="fl" label-width="90px">
              <el-input v-model="searchForm.serviceSourceName" size="small"></el-input>
            </el-form-item>
            <el-form-item label="服务模型名称" style="width: 256px;" class="fl" label-width="110px">
              <el-input v-model="searchForm.modelName" size="small"></el-input>
            </el-form-item>
            <el-form-item label="发布类型" style="width: 226px;" class="fl" label-width="80px">
              <el-select v-model="searchForm.requestMethod" clearable placeholder="" size="small">
                <el-option label="POST" value="POST"></el-option>
                <el-option label="GET" value="GET"></el-option>
              </el-select>
            </el-form-item>
          </div>
          <div class="btn-group">
            <el-button type="primary" size="mini" @click="fetchAssetFieldTable('1')">查询</el-button>
            <el-button size="mini" style="margin-left: 10px" @click="resetQuery">重置</el-button>
          </div>
        </el-form>
      </div>
    </nav>
    <section class="components-container clearfix section-bod">
      <cr-loading v-show="loading"></cr-loading>
      <list-card
        :listData="tableData.data"
        :fieldsData="fieldsData"
        :operatePermission="operatePermission"
        @edit="handleUpdate"
        @delete="deleteSource"
        @browse="handleView"
      ></list-card>
      <el-pagination
        background
        layout="prev, pager, next"
        @current-change="(current)=>{this.loading=true;this.tableData.data=[];searchForm.pageNo = current;this.fetchAssetFieldTable('2')}"
        :page-size="parseInt(searchForm.pageSize)"
        :current-page="parseInt(searchForm.pageNo)"
        :total="tableData.page"
        class="fr mt-10">
      </el-pagination>

    </section>

    <!--模型新增修改滑框 -->
    <slide-box
      slideWidth="800px"
      :slideShow="addDialog"
      @close="slideClose"
      :title="textMap[dialogStatus]"
      :subSlide="true"
      :subSlideShow="testDialog"
      @subClose="closeSubSlide"
      subTitle="测试"
      ref="slideBox">
      <!-- 一级弹框 -->
      <div slot="slide-content">
        <el-form :model="serviceModelForm" :rules="serviceModelRule" ref="serviceModelForm"  class="add-dialog-form" label-width="90px">
          <div class="form-body">
            <el-collapse v-model="activeNames">

              <!-- 基本信息 -->
              <el-collapse-item title="基本信息" name="1">
                <div class="form-header clearfix">
                  <!-- 模型类型 -->
                  <el-form-item label="模型类型:" prop="typeCode" class="mt-12">
                    <el-select
                      v-model="serviceModelForm.typeCode"
                      size="medium"
                      placeholder="请选择模型类型"
                      @change="modelTypeChange(serviceModelForm.typeCode)"
                      :disabled="dialogStatus == 'update'|| dialogStatus == 'view'">
                      <el-option
                        v-for="(item, index) in primaryModelType"
                        :key="index"
                        :label="item.displayName"
                        :value="item.code"></el-option>
                    </el-select>
                    <cr-help-popver styleText="position:absolute;right:-20px;top:12px;">
                      <div slot="popver-content">
                        系统数据源数据（数据库）对外提供查询访问默认用资产模型，其他根据选择对应的能力模型，链接到
                        <a href="./dsp-help/dsp-help.html#wow13" target="_blank">
                          <el-button type="text" style="padding:0">模型类型</el-button>
                        </a>
                        帮助
                      </div>
                    </cr-help-popver>
                  </el-form-item>

                  <div class="clearfix mt-12">
                    <!--能力模型 另分子类-->
                    <template v-if="modelTypeChoose == 'ability'">
                      <el-form-item
                        label="大类"
                        prop="classifi"
                        :rules="[
                          { required: true, message: '请选择大类' }
                        ]">
                        <el-select
                          v-model="serviceModelForm.classifi"
                          size="medium"
                          filterable
                          placeholder="请选择"
                          @change="abilitySubTyleChange(serviceModelForm.classifi)"
                          :disabled="dialogStatus == 'update' || dialogStatus == 'view' ">
                          <el-option
                            v-for="(item, index) in abilityModelBigTyle"
                            :key="index"
                            :label="item.displayName"
                            :value="item.code">
                          </el-option>
                        </el-select>
                      </el-form-item>
                      <el-form-item
                        label="子类"
                        prop="type"
                        :rules="[
                          { required: true, message: '请选择子类' }
                        ]" class="mt-15">
                        <el-select
                          v-model="serviceModelForm.type"
                          size="medium"
                          filterable
                          placeholder="请选择"
                          @change="abilityBigSubTyleChange(serviceModelForm.type)"
                          :disabled="dialogStatus == 'update' || dialogStatus == 'view' ">
                          <el-option
                            v-for="(item, index) in abilityModelBigSubType"
                            :key="index"
                            :label="item.displayName"
                            :value="item.code">
                          </el-option>
                        </el-select>
                      </el-form-item>
                    </template>
                    <!--资产模型-->
                    <template v-else>
                      <el-form-item
                        label="资产领域"
                        prop="classifi"
                        :rules="[
                          { required: true, message: '请选择资产领域' }
                        ]" >
                        <el-select v-model="serviceModelForm.classifi"
                          placeholder="请选择"
                          filterable
                          size="medium"
                          @change="getAssetTheme(serviceModelForm.classifi)"
                          :disabled="dialogStatus == 'update' || dialogStatus == 'view' ">
                          <el-option v-for="(item, index) in assetFieldList" :label="item.areaName" :value="item.areaName" :key="index"></el-option>
                        </el-select>
                      </el-form-item>
                      <el-form-item
                        label="资产主题"
                        prop="theme"
                        :rules="[
                          { required: true, message: '请选择资产主题' }
                        ]" class="mt-15">
                        <el-select v-model="serviceModelForm.theme"
                          size="medium"
                          placeholder="请选择"
                          filterable
                          :disabled="dialogStatus == 'update' || dialogStatus == 'view' ">
                          <el-option v-for="(item, index) in assetThemeList" :label="item.topicName" :value="item.id" :key="index"></el-option>
                        </el-select>
                      </el-form-item>
                      <el-form-item
                        label="父模型名称"
                        prop="parentId"
                        class="mt-15">
                        <el-select v-model="serviceModelForm.parentId"
                          size="medium"
                          placeholder="请选择"
                          filterable
                          :disabled="dialogStatus == 'view'">
                          <el-option label="" value=""></el-option>
                          <el-option v-for="(item, index) in parentModelList" :label="item.modelName" :value="item.id" :key="index"></el-option>
                        </el-select>
                      </el-form-item>
                    </template>
                  </div>
                  <!--公共部分-->
                  <el-form-item label="模型名称:" prop="modelName" class="mt-12">
                    <el-input v-model="serviceModelForm.modelName"
                      size="medium"
                      @blur="checkName(serviceModelForm.modelName)"
                      :disabled="dialogStatus == 'update' || dialogStatus == 'view'">
                    </el-input>
                  </el-form-item>
                  <el-form-item label="描述:" class="mt-20">
                    <el-input type="textarea" v-model="serviceModelForm.des" :disabled="dialogStatus == 'view'"></el-input>
                  </el-form-item>
                  <el-form-item label="数据源:" prop="dataSource" class="mt-12" >
                    <el-select v-model="serviceModelForm.dataSource"
                      size="medium"
                      filterable
                      placeholder="请选择"
                      @change="getServiceTable(serviceModelForm.dataSource)"
                      :disabled="dialogStatus == 'view'">
                      <el-option v-for="(item, index) in dataSourceList" :label="item.serviceSourceName" :value="item.id" :key="index"></el-option>
                    </el-select>
                  </el-form-item>
                  <!-- <el-form-item label="是否发布:" prop="release" class="mt-12 ml-12" label-width="80px">
                    <el-radio-group
                      v-model="serviceModelForm.release"
                      :disabled="dialogStatus == 'view'"
                      @change="(statu) =>{this.releaseStute = statu}">
                      <el-radio label="published" style="margin-right: 40px;">是</el-radio>
                      <el-radio label="initialized">否</el-radio>
                    </el-radio-group>
                  </el-form-item> -->
                  <el-form-item label="发布类型:" prop="releaseType" class="mt-12 ml-12" label-width="80px" v-if="releaseStute == 'published'">
                    <el-radio-group style="float:left;margin-top:10px" v-model="serviceModelForm.requestMethod" :disabled="dialogStatus == 'update'|| dialogStatus == 'view'">
                      <el-radio label="POST" style="margin-right: 30px;">POST</el-radio>
                      <el-radio label="GET">GET</el-radio>
                    </el-radio-group>
                    <cr-help-popver styleText="float:left;display:flex;margin:10px 0 0 40px">
                      <div slot="popver-content">
                        默认选择post发布类型，一旦选择，不可更改。
                      </div>
                    </cr-help-popver>
                  </el-form-item>
                  <!--变动部分-->
                  <!-- 资产模型 -->
                  <div v-if="modelTypeChoose === 'asset'">
                    <div class="clearfix">
                      <el-form-item label="缓存时间(s):" prop="time" class="mt-12" >
                        <el-input size="medium"  v-model="serviceModelForm.time" :disabled="dialogStatus == 'view'"></el-input>
                        <cr-help-popver styleText="position:absolute;right:-20px;top:12px;">
                          <div slot="popver-content">
                            缓存时间为0表示不缓存。服务模型条件查询固定或者服务模型频繁访问建议设置缓存时间，例如：600秒。
                          </div>
                        </cr-help-popver>
                      </el-form-item>
                      <el-form-item label="表达式类型:" prop="type" class="mt-12">
                        <el-select v-model="serviceModelForm.type"
                          size="medium"
                          placeholder="请选择"
                          @change="dataModelTypeChange(serviceModelForm.type)"
                          :disabled="dialogStatus === 'update' || dialogStatus == 'view'">
                          <el-option label="数据源表" value="table"></el-option>
                          <el-option label="查询表达式" value="sql"></el-option>
                        </el-select>
                      </el-form-item>
                    </div>
                    <!--查询表达式-->
                    <template v-if="dataModelTypeChoose == 'sql'">
                      <el-form-item label="查询表达式:">
                        <el-input rows="4" type="textarea"
                          v-model="serviceModelForm.parsingExpress"
                          :disabled="dialogStatus == 'view' || (!serviceModelForm.fieldLoad&&dialogStatus!=='create')"
                          class="mt-12"></el-input>
                        <img v-if="dialogStatus == 'update' && !serviceModelForm.fieldLoad" src="../../assets/tdspic/edit.png" class="sql-load-btn" title="" alt="" @click.stop="testSqlLoad()">
                        <el-button
                          v-if="dialogStatus === 'create' || serviceModelForm.fieldLoad"
                          size="mini"
                          type="primary"
                          style="margin-right: 20px;"
                          @click="parsingSql(serviceModelForm.parsingExpress)"
                          v-show="dialogStatus === 'create' || dialogStatus === 'update'">解析</el-button>
                        <span v-if="dialogStatus == 'update'">注：更改sql表达式后请对相应服务重新授权</span>
                        <el-popover
                          placement="bottom"
                          title="查询表达式说明："
                          width="500"
                          trigger="hover">
                          <div>
                            <p>1.查询表达式仅支持关系型数据库，如：ORACLE、MYSQL、SQL SERVER、GREENPLUM等；</p>
                            <p>2.表达式样例如下，留意参数间的空格。<br/>
                            select header_id,project_number,tenant_number,premise_number,contact_person,<br/>bill_number,balance_period,
                            email,bill_date,bill_type,description,bill_title,vat_flag <br/>
                            from jep.tnnt_fin_bill_headers where 1 = 1 <br/>
                            and project_number = ${ project_number} <br/>
                            <#if bill_date_from??><br/>
                            and bill_date > = ${ bill_date_from} <br/>
                            </#if><br/>
                            <#if bill_date_to??><br/>
                            and bill_date < = ${ bill_date_to} <br/>
                            </#if></p>
                          </div>
                          <el-button slot="reference" style="padding: 0;border: none;float: right;margin-top: 8px;">
                            <img src="../../assets/tdspic/help.png" alt="帮助">
                          </el-button>
                        </el-popover>
                      </el-form-item>
                    </template>
                    <!--表-->
                    <template v-else-if="dataModelTypeChoose == 'table'">
                      <div class="clearfix">
                        <el-form-item label="数据源表:" prop="tableName" class="mt-12">
                          <el-popover height="200" placement="bottom-start" trigger="manual" v-model="popoverVisible">
                            <ul class="service-table-list">
                              <li v-for="item in serviceTableList" :key="item"><span @click.stop="offTest(item)">{{ item }}</span></li>
                              <span v-if="serviceTableList.length === 0" style="text-center">无结果</span>
                            </ul>
                            <el-input
                              slot="reference"
                              id="test-select"
                              v-model="serviceModelForm.tableName"
                              size="medium"
                              placeholder="请输入关键字，按回车键搜索数据源表"
                              @blur="popoverVisible=false"
                              :disabled="(!serviceModelForm.fieldLoad && dialogStatus == 'update') || dialogStatus == 'view'">
                            </el-input>
                          </el-popover>
                          <img v-if="dialogStatus == 'update'" src="../../assets/tdspic/edit.png" class="load-btn" title="" alt="" @click.stop="testLoad()">
                          <br/><span v-if="dialogStatus == 'update'">注：更改数据源表后请对相应服务重新授权</span>
                        </el-form-item>
                      </div>
                    </template>
                  </div>
                  <!-- 能力模型 -->
                  <template>
                    <!-- 能力模型第一类 -->
                    <div v-if="abilityModelSubChoose === 'topic'">
                      <el-form-item label="topicName:" prop="expression" class="fl mt-12 wd100">
                        <el-input
                          v-model="serviceModelForm.expression"
                          size="medium"
                          :disabled="dialogStatus == 'update' || dialogStatus == 'view'"></el-input>
                      </el-form-item>
                    </div>
                    <!-- 能力模型第二类 -->
                    <div v-else-if="abilityModelSubChoose === 'url'" class="clearfix mb-12">
                      <el-form-item label="url:" prop="expression" class="mt-12">
                        <el-input v-model="serviceModelForm.expression"
                          placeholder="URL"
                          size="medium"
                          :disabled="dialogStatus == 'update' || dialogStatus == 'view'"></el-input>
                      </el-form-item>
                      <!-- <el-form-item label="body:" prop="expression" class="mt-12"  v-if="serviceModelForm.requestMethod === 'POST'">
                        <el-input type="textarea" :rows="6" v-model="serviceModelForm.bodyPattern"
                          placeholder="body"
                          size="medium"
                          :disabled="dialogStatus == 'update' || dialogStatus == 'view'"></el-input>
                        <el-button type="primary" size="mini" @click="parseTemplate(serviceModelForm.bodyPattern)">解析</el-button>
                      </el-form-item> -->
                    </div>
                  </template>
                </div>
              </el-collapse-item>

              <!-- 属性列表 -->
              <el-collapse-item name="2" v-if="modelTypeChoose === 'asset'">
                <template slot="title">
                  属性列表
                  <cr-help-popver styleText="position:relative;top:2px;">
                    <div slot="popver-content">
                      查看属性列表
                      <a href="./dsp-help/dsp-help.html#wow13" target="_blank">
                        <el-button type="text" style="padding:0">规则</el-button>
                      </a>
                    </div>
                  </cr-help-popver>
                </template>
                <!--资产模型-->
                <template v-if="modelTypeChoose === 'asset'">
                    <!--查询表达式-->
                  <template v-if="dataModelTypeChoose === 'sql'">
                    <div class="sttrTable clearfix mt-12">
                      <el-table
                        :data="serviceModelForm.dataModelAttrTable"
                        border
                        :header-row-class-name="backgroundColorBorderChange"
                        size="mini"
                        key="checkSttrTable"
                        class="fl">
                        <el-table-column label="属性名" header-align="center" align="center" prop="fieldName" show-overflow-tooltip>
                          <!-- 先不用 -->
                          <!-- <template slot-scope="scope">
                            <el-input v-model="scope.row.fieldName" placeholder="" size="mini" :disabled="dataModelParamTableFoo == true"></el-input>
                          </template> -->
                        </el-table-column>
                        <el-table-column label="类型" header-align="center">
                          <template slot-scope="scope">
                            <el-input v-model="scope.row.dataType" placeholder="" size="mini" ></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="描述" header-align="center">
                          <template slot-scope="scope">
                            <el-input v-model="scope.row.fieldDesc" placeholder="" size="mini" ></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="表达式" header-align="center" align="center" prop="expression">
                          <!-- 先不用 -->
                          <!-- <template slot-scope="scope">
                            <el-input v-model="scope.row.expression" placeholder="" size="mini" :disabled="dataModelParamTableFoo == true"></el-input>
                          </template> -->
                        </el-table-column>
                        <!-- 先不用 -->
                        <!-- <el-table-column label="操作" width="50">
                          <template slot-scope="scope">
                            <a title="删除" @click="deleteDataQueryAttr(scope.row, 'sttr')" v-show="scope.row.hidden == 'y'">
                              <svg-icon icon-class="delete" style="width: 20px; height: 20px"></svg-icon>
                            </a>
                          </template>
                        </el-table-column> -->
                      </el-table>
                    </div>
                    <!--<div class="text-r mt-12">
                      <el-button type="success" size="mini" @click="addDataQueryAttr('sttr')">添加属性</el-button>
                    </div>-->
                  </template>
                    <!--表-->
                  <template v-else-if="dataModelTypeChoose === 'table' || dataModelTypeChoose === 'hiveThird'">
                    <div class="clearfix mt-12" >
                      <el-table
                        :data="serviceModelForm.dataModelTableAttrTable"
                        border
                        size="mini"
                        key="tableSttrTable"
                        :header-row-class-name="backgroundColorBorderChange"
                        class="fl">
                        <el-table-column label="属性名" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-input v-model="scope.row.fieldName" placeholder="" size="mini" :disabled="scope.row.isDerived != 'y' "></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="类型" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-input v-model="scope.row.dataType" placeholder="" size="mini" :disabled="scope.row.isDerived != 'y' "></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="描述" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-input v-model="scope.row.fieldDesc" placeholder="" size="mini" :disabled="scope.row.isDerived != 'y' "></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="表达式" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-input v-model="scope.row.expression" placeholder="" size="mini" :disabled="scope.row.isDerived != 'y' "></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="操作" width="50">
                          <template slot-scope="scope">
                            <a title="删除" @click="deleteDataTable(scope.row, scope.$index,'sttr')" v-show="dialogStatus != 'view' && expressionTypeDeleteDis == 'table'">
                              <!-- <svg-icon icon-class="delete" style="width: 20px; height: 20px"></svg-icon> -->
                              <img src="../../assets/tdspic/delete.png" style="width: 20px; height: 20px" alt="">
                            </a>
                          </template>
                        </el-table-column>
                      </el-table>
                    </div>
                    <div class="text-r mt-12" v-show="dialogStatus == 'create' || dialogStatus == 'update'">
                      <el-button type="primary" size="mini" @click="addDataTableAttr('sttr')" >添加属性</el-button>
                    </div>
                  </template>
                </template>
                <template v-else-if="modelTypeChoose === 'ability'">
                  <!--能力模型一种 （用户填写）-->
                  <template v-if="abilityModelSubChoose === 'url'">
                    <div class="clearfix mt-12" >
                      <el-table
                        :data="serviceModelForm.abilityFirstModelAttrTable"
                        border
                        size="mini"
                        key="tableSttrTable"
                        :header-row-class-name="backgroundColorBorderChange"
                        class="fl">
                        <el-table-column label="属性名" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-input v-model="scope.row.fieldName" placeholder="" size="mini"></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="类型" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-input v-model="scope.row.dataType" placeholder="" size="mini"></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="描述" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-input v-model="scope.row.fieldDesc" placeholder="" size="mini"></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="表达式" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-input v-model="scope.row.expression" placeholder="" size="mini"></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="操作" width="50">
                          <template slot-scope="scope">
                            <a title="删除" @click="deleteAbilityData(scope.row, scope.$index,'sttr')"  v-show="dialogStatus == 'create' || dialogStatus == 'update'">
                              <!-- <svg-icon icon-class="delete" style="width: 20px; height: 20px"></svg-icon> -->
                              <img src="../../assets/tdspic/delete.png" style="width: 20px; height: 20px" alt="">
                            </a>
                          </template>
                        </el-table-column>
                      </el-table>
                    </div>
                    <div class="text-r mt-12" v-show="dialogStatus == 'create' || dialogStatus == 'update'">
                      <el-button type="primary" size="mini" @click="addAbilityTableAttr('sttr')" >添加属性</el-button>
                    </div>
                  </template>
                  <!--能力模型第二种模型 -->
                  <template v-else-if="abilityModelSubChoose === 'topic'">
                    <div class="clearfix mt-12" >
                      <el-table
                        :data="serviceModelForm.abilityFirstModelAttrTable"
                        border
                        size="mini"
                        key="tableSttrTable"
                        :header-row-class-name="backgroundColorBorderChange"
                        class="fl">
                        <el-table-column label="属性名" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-input v-model="scope.row.fieldName" placeholder="" size="mini"></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="类型" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-input v-model="scope.row.dataType" placeholder="" size="mini"></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="描述" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-input v-model="scope.row.fieldDesc" placeholder="" size="mini"></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="表达式" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-input v-model="scope.row.expression" placeholder="" size="mini"></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="操作" width="50">
                          <template slot-scope="scope">
                            <a title="删除" @click="deleteAbilityData(scope.row, scope.$index,'sttr')"  v-show="dialogStatus == 'create' || dialogStatus == 'update'">
                              <img src="../../assets/tdspic/delete.png" style="width: 20px; height: 20px" alt="">
                            </a>
                          </template>
                        </el-table-column>
                      </el-table>
                    </div>
                    <div class="text-r mt-12"  v-show="dialogStatus == 'create' || dialogStatus == 'update'">
                      <el-button type="primary" size="mini" @click="addAbilityTableAttr('sttr')" >添加属性</el-button>
                    </div>
                  </template>
                </template>
              </el-collapse-item>
              <!-- 参数列表 -->
              <el-collapse-item name="3">
                <template slot="title">
                  参数列表
                  <cr-help-popver styleText="position:relative;top:2px;">
                    <div slot="popver-content">
                      查看参数列表
                      <a href="./dsp-help/dsp-help.html#wow13" target="_blank">
                        <el-button type="text" style="padding:0">规则</el-button>
                      </a>
                    </div>
                  </cr-help-popver>
                </template>
                <!--资产模型-->
                <template v-if="modelTypeChoose === 'asset'">
                  <!--查询表达式-->
                  <template v-if="dataModelTypeChoose == 'sql'">
                    <div class="paramTable clearfix mt-12">
                      <el-table
                        :data="serviceModelForm.dataModelParamTable"
                        border
                        :header-row-class-name="backgroundColorBorderChange"
                        key="checkParamTable"
                        size="mini"
                        class="fl sql-sttr-table">

                        <el-table-column
                          label="参数名"
                          header-align="center"
                          align="center"
                          prop="paramName"
                          :render-header = "headerStar"
                          show-overflow-tooltip>
                          <template slot-scope="scope">
                            <el-input placeholder="" size="mini" v-model="scope.row.paramName" :disabled="true"></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column
                          label="参数类型"
                          :render-header = "headerStar"
                          header-align="center"
                          align="center">
                          <template slot-scope="scope">
                            <el-select size="mini" placeholder="" v-model="scope.row.paramType" :disabled="dialogStatus == 'view'">
                              <el-option label="string" value="string"></el-option>
                              <el-option label="number" value="number"></el-option>
                              <el-option label="date" value="date"></el-option>
                              <el-option label="list" value="list"></el-option>
                            </el-select>
                            <!-- <el-input placeholder="" size="mini" v-model="scope.row.paramType" :disabled="dialogStatus == 'view'"></el-input> -->
                          </template>
                        </el-table-column>
                        <el-table-column
                          label="属性名"
                          header-align="center"
                          :render-header = "headerStar"
                          align="center"
                          prop="fieldName"
                          show-overflow-tooltip>
                          <template slot-scope="scope">
                            <el-input placeholder="" size="mini" v-model="scope.row.fieldName" :disabled="dialogStatus == 'view'"></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="描述" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-input placeholder="" size="mini" v-model="scope.row.paramDesc" :disabled="dialogStatus == 'view'"></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="默认值" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-input placeholder="" size="mini" v-model="scope.row.defaultValue" :disabled="dialogStatus == 'view'"></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column width="80" label="是否必填" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-checkbox v-model="scope.row.isRequired" true-label='y' false-label='n' size="mini" :disabled="dataModelParamTableFoo == true"></el-checkbox>
                          </template>
                        </el-table-column>
                        <!-- 先不用 -->
                        <!-- <el-table-column label="操作" width="50">
                          <template slot-scope="scope">
                            <a title="删除" @click="deleteDataQueryAttr(scope.row, 'param')" v-show="scope.row.hidden == 'y'">
                              <svg-icon icon-class="delete" style="width: 20px; height: 20px"></svg-icon>
                            </a>
                          </template>
                        </el-table-column> -->
                      </el-table>
                    </div>
                    <!-- 先不用 -->
                    <!--<div class="text-r mt-12">
                      <el-button type="success" size="mini" @click="addDataQueryAttr('param')">添加参数</el-button>
                    </div>-->
                  </template>
                    <!--表-->
                  <template v-else-if="dataModelTypeChoose === 'table' || dataModelTypeChoose === 'hiveThird'">
                    <div class="clearfix mt-12">
                      <el-table
                        :data="serviceModelForm.dataModelTableParamTable"
                        border
                        key="tableParamTable"
                        size="mini"
                        :header-row-class-name="backgroundColorBorderChange"
                        class="fl">

                        <el-table-column label="参数名"  :render-header = "headerStar" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-input placeholder="" size="mini" v-model="scope.row.paramName" :disabled="dialogStatus == 'view'"></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="参数类型" :render-header = "headerStar" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-select size="mini" placeholder="" v-model="scope.row.paramType" :disabled="dialogStatus == 'view'">
                              <el-option label="string" value="string"></el-option>
                              <el-option label="number" value="number"></el-option>
                              <el-option label="date" value="date"></el-option>
                              <el-option label="list" value="list"></el-option>
                            </el-select>
                            <!-- <el-input placeholder="" size="mini" v-model="scope.row.paramType" :disabled="dialogStatus == 'view'"></el-input> -->
                          </template>
                        </el-table-column>
                        <el-table-column
                          label="属性名"
                          :render-header = "headerStar"
                          header-align="center"
                          align="center">
                          <template slot-scope="scope">
                            <el-select v-model="scope.row.fieldName" placeholder="" size="mini"
                              @change="changeParamName(scope.row.fieldName, scope.$index)"
                              :disabled="dialogStatus == 'view'" filterable>
                              <!--<el-option v-for="(item, index) in serviceModelForm.dataModelTableAttrTable" :label="item.columnName" :value="item.columnName" :key="index"></el-option>-->
                              <el-option v-for="(item, index) in fieldsAttrList" :label="item" :value="item" :key="index"></el-option>
                            </el-select>
                          </template>
                        </el-table-column>
                        <el-table-column label="描述" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-input placeholder="" size="mini" v-model="scope.row.paramDesc" :disabled="dialogStatus == 'view'"></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column label="操作符" :render-header = "headerStar" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-select v-model="scope.row.operateType" placeholder="" size="mini" :disabled="dialogStatus == 'view'">
                              <el-option label="大于" value=">"></el-option>
                              <el-option label="小于" value="<"></el-option>
                              <el-option label="大于等于" value=">="></el-option>
                              <el-option label="小于等于" value="<="></el-option>
                              <el-option label="等于" value="="></el-option>
                              <el-option label="不等于" value="!="></el-option>
                            </el-select>
                          </template>
                        </el-table-column>
                        <el-table-column label="默认值" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-input placeholder="" size="mini" v-model="scope.row.defaultValue" :disabled="dialogStatus == 'view'"></el-input>
                          </template>
                        </el-table-column>
                        <el-table-column width="80" label="是否必填" header-align="center" align="center">
                          <template slot-scope="scope">
                            <el-checkbox v-model="scope.row.isRequired" true-label='y' false-label='n' size="mini" :disabled="dialogStatus == 'view'"></el-checkbox>
                          </template>
                        </el-table-column>
                        <el-table-column label="操作" width="50">
                          <template slot-scope="scope">
                            <a title="删除" @click="deleteDataTable(scope.row, scope.$index,'param')" v-show="dialogStatus == 'create' || dialogStatus == 'update'">
                              <img src="../../assets/tdspic/delete.png" style="width: 20px; height: 20px" alt="">
                            </a>
                          </template>
                        </el-table-column>
                      </el-table>
                    </div>
                    <div class="text-r mt-12" v-show="dialogStatus == 'create' || dialogStatus == 'update'">
                      <el-button type="primary" size="mini" @click="addDataTableAttr('param')">添加参数</el-button>
                    </div>
                  </template>
                </template>
                <!-- 能力模型 -->
                <template v-else-if="modelTypeChoose === 'ability'">
                  <!--能力模型第二种模型-->
                  <template v-if="abilityModelSubChoose === 'url'">
                    <!-- <el-tabs @tab-click="headBodyTab" v-model="urlHeaderBodyChoose" type="card" v-if="serviceModelForm.requestMethod === 'POST'">
                      <el-tab-pane label="head" name="first">
                        <el-table
                          :data="serviceModelForm.abilityFirstModelParamTable"
                          border
                          :header-row-class-name="backgroundColorBorderChange"
                          size="mini"
                          key="apiTable"
                          class="mt-12">
                          <el-table-column label="类型" :render-header = "headerStar" header-align="center" align="center">
                            <template slot-scope="scope">
                              <el-select placeholder="" size="mini" v-model="scope.row.fieldName" :disabled="dialogStatus == 'update'||dialogStatus == 'view'||dialogStatus == 'create'">
                                <el-option label="head" value="head"></el-option>
                                <el-option label="body" value="body"></el-option>
                              </el-select>
                            </template>
                          </el-table-column>
                          <el-table-column label="参数名" :render-header = "headerStar" header-align="center" align="center">
                            <template slot-scope="scope">
                              <el-input placeholder="" size="mini" v-model="scope.row.paramName" :disabled="dialogStatus == 'view'"></el-input>
                            </template>
                          </el-table-column>
                          <el-table-column label="参数类型" :render-header = "headerStar" header-align="center" align="center">
                            <template slot-scope="scope">
                              <el-select size="mini" placeholder="" v-model="scope.row.paramType" :disabled="dialogStatus == 'view'">
                                <el-option label="string" value="string"></el-option>
                                <el-option label="number" value="number"></el-option>
                                <el-option label="date" value="date"></el-option>
                              </el-select>
                            </template>
                          </el-table-column>
                          <el-table-column label="描述" header-align="center" align="center">
                            <template slot-scope="scope">
                              <el-input placeholder="" size="mini" v-model="scope.row.paramDesc" :disabled="dialogStatus == 'view'"></el-input>
                            </template>
                          </el-table-column>
                          <el-table-column label="默认值" header-align="center" align="center">
                            <template slot-scope="scope">
                              <el-input placeholder="" size="mini" v-model="scope.row.defaultValue" :disabled="dialogStatus == 'view'"></el-input>
                            </template>
                          </el-table-column>
                          <el-table-column width="80" label="是否必填" header-align="center" align="center">
                            <template slot-scope="scope">
                              <el-checkbox v-model="scope.row.isRequired" true-label='y' false-label='n' size="mini" :disabled="dialogStatus == 'view'"></el-checkbox>
                            </template>
                          </el-table-column>
                          <el-table-column label="操作" width="50">
                            <template slot-scope="scope">
                              <a title="删除" @click="deleteAbilityData(scope.row, scope.$index,'param')"  v-show="dialogStatus == 'create' || dialogStatus == 'update'">
                                <img src="../../assets/tdspic/delete.png" style="width: 20px; height: 20px" alt="">
                              </a>
                            </template>
                          </el-table-column>
                        </el-table>
                      </el-tab-pane>
                      <el-tab-pane label="body" name="second">
                        <el-table
                          :data="serviceModelForm.abilityFirstModelParamBodyTable"
                          border
                          :header-row-class-name="backgroundColorBorderChange"
                          size="mini"
                          key="apiTable"
                          class="mt-12">
                          <el-table-column label="类型" :render-header = "headerStar" header-align="center" align="center">
                            <template slot-scope="scope">
                              <el-select placeholder="" size="mini" v-model="scope.row.fieldName" :disabled="dialogStatus == 'update'||dialogStatus == 'view'||dialogStatus == 'create'">
                                <el-option label="body" value="body"></el-option>
                              </el-select>
                            </template>
                          </el-table-column>
                          <el-table-column label="参数名" :render-header = "headerStar" header-align="center" align="center">
                            <template slot-scope="scope">
                              <el-input placeholder="" size="mini" v-model="scope.row.paramName" :disabled="dialogStatus == 'view'"></el-input>
                            </template>
                          </el-table-column>
                          <el-table-column label="参数类型" :render-header = "headerStar" header-align="center" align="center">
                            <template slot-scope="scope">
                              <el-select size="mini" placeholder="" v-model="scope.row.paramType" :disabled="dialogStatus == 'view'">
                                <el-option label="string" value="string"></el-option>
                                <el-option label="number" value="number"></el-option>
                                <el-option label="date" value="date"></el-option>
                              </el-select>
                            </template>
                          </el-table-column>
                          <el-table-column label="描述" header-align="center" align="center">
                            <template slot-scope="scope">
                              <el-input placeholder="" size="mini" v-model="scope.row.paramDesc" :disabled="dialogStatus == 'view'"></el-input>
                            </template>
                          </el-table-column>
                          <el-table-column label="默认值" header-align="center" align="center">
                            <template slot-scope="scope">
                              <el-input placeholder="" size="mini" v-model="scope.row.defaultValue" :disabled="dialogStatus == 'view'"></el-input>
                            </template>
                          </el-table-column>
                          <el-table-column width="80" label="是否必填" header-align="center" align="center">
                            <template slot-scope="scope">
                              <el-checkbox v-model="scope.row.isRequired" true-label='y' false-label='n' size="mini" :disabled="dialogStatus == 'view'"></el-checkbox>
                            </template>
                          </el-table-column>
                          <el-table-column label="操作" width="50">
                            <template slot-scope="scope">
                              <a title="删除" @click="deleteAbilityData(scope.row, scope.$index,'param')"  v-show="dialogStatus == 'create' || dialogStatus == 'update'">
                                <img src="../../assets/tdspic/delete.png" style="width: 20px; height: 20px" alt="">
                              </a>
                            </template>
                          </el-table-column>
                        </el-table>
                      </el-tab-pane>
                    </el-tabs> -->
                    <el-table
                      :data="serviceModelForm.abilityFirstModelParamTable"
                      border
                      :header-row-class-name="backgroundColorBorderChange"
                      size="mini"
                      key="apiTable"
                      class="mt-12">
                      <!-- v-else-if="serviceModelForm.requestMethod === 'GET'" -->

                      <el-table-column label="参数名" :render-header = "headerStar" header-align="center" align="center">
                        <template slot-scope="scope">
                          <el-input placeholder="" size="mini" v-model="scope.row.paramName" :disabled="dialogStatus == 'view'"></el-input>
                        </template>
                      </el-table-column>
                      <el-table-column label="参数类型" :render-header = "headerStar" header-align="center" align="center">
                        <template slot-scope="scope">
                          <el-select size="mini" placeholder="" v-model="scope.row.paramType" :disabled="dialogStatus == 'view'">
                            <el-option label="string" value="string"></el-option>
                            <el-option label="number" value="number"></el-option>
                            <el-option label="date" value="date"></el-option>
                            <el-option label="json" value="json"></el-option>
                          </el-select>
                        </template>
                      </el-table-column>
                      <el-table-column label="参数位置" :render-header = "headerStar" header-align="center" align="center">
                        <template slot-scope="scope">
                          <el-select placeholder="" size="mini" v-model="scope.row.fieldName" :disabled="dialogStatus == 'view'">
                            <el-option label="head" value="head"></el-option>
                            <el-option label="body" value="body"></el-option>
                          </el-select>
                        </template>
                      </el-table-column>
                      <el-table-column label="描述" header-align="center" align="center">
                        <template slot-scope="scope">
                          <el-input placeholder="" size="mini" v-model="scope.row.paramDesc" :disabled="dialogStatus == 'view'"></el-input>
                        </template>
                      </el-table-column>
                      <el-table-column label="默认值" header-align="center" align="center">
                        <template slot-scope="scope">
                          <el-input placeholder="" size="mini" v-model="scope.row.defaultValue" :disabled="dialogStatus == 'view'"></el-input>
                        </template>
                      </el-table-column>
                      <el-table-column width="80" label="是否必填" header-align="center" align="center">
                        <template slot-scope="scope">
                          <el-checkbox v-model="scope.row.isRequired" true-label='y' false-label='n' size="mini" :disabled="dialogStatus == 'view'"></el-checkbox>
                        </template>
                      </el-table-column>
                      <el-table-column label="操作" width="50" header-align="center">
                        <template slot-scope="scope">
                          <a title="删除" @click="deleteAbilityData(scope.row, scope.$index,'param')"  v-show="dialogStatus == 'create' || dialogStatus == 'update'">
                            <!-- <svg-icon icon-class="delete" style="width: 20px; height: 20px"></svg-icon> -->
                            <img src="../../assets/tdspic/delete.png" style="width: 20px; height: 20px" alt="">
                          </a>
                        </template>
                      </el-table-column>
                    </el-table>
                    <el-form-item size="mini" class="mt-12" label-width="10px" style="text-align: right">
                      <el-button type="primary" @click="addAbilityTableAttr('param', 'url')"  v-show="(dialogStatus == 'create' || dialogStatus == 'update')">添加参数</el-button>
                    </el-form-item>
                  </template>
                    <!--能力模型第一种模型 （消息队列）-->
                  <template v-else-if="abilityModelSubChoose === 'topic'">
                    <el-table
                      :data="serviceModelForm.abilityFirstModelParamTable"
                      border
                      :header-row-class-name="backgroundColorBorderChange"
                      size="mini"
                      key="apiTable"
                      class="mt-12">

                      <el-table-column label="参数名" :render-header = "headerStar" header-align="center" align="center">
                        <template slot-scope="scope">
                          <el-input placeholder="" size="mini" v-model="scope.row.paramName" :disabled="dialogStatus == 'view'"></el-input>
                        </template>
                      </el-table-column>
                      <el-table-column label="参数类型" :render-header = "headerStar" header-align="center" align="center">
                        <template slot-scope="scope">
                          <el-select size="mini" placeholder="" v-model="scope.row.paramType" :disabled="dialogStatus == 'view'">
                            <el-option label="string" value="string"></el-option>
                            <el-option label="number" value="number"></el-option>
                            <el-option label="date" value="date"></el-option>
                          </el-select>
                          <!-- <el-input placeholder="" size="mini" v-model="scope.row.paramType" :disabled="dialogStatus == 'view'"></el-input> -->
                        </template>
                      </el-table-column>
                      <el-table-column label="参数位置" :render-header = "headerStar" header-align="center" align="center">
                        <template slot-scope="scope">
                          <el-input placeholder="" size="mini" v-model="scope.row.fieldName" :disabled="dialogStatus == 'view'"></el-input>
                        </template>
                      </el-table-column>
                      <el-table-column label="描述" header-align="center" align="center">
                        <template slot-scope="scope">
                          <el-input placeholder="" size="mini" v-model="scope.row.paramDesc" :disabled="dialogStatus == 'view'"></el-input>
                        </template>
                      </el-table-column>
                      <el-table-column label="操作符" :render-header = "headerStar" header-align="center" align="center">
                        <template slot-scope="scope">
                          <el-select v-model="scope.row.operateType" placeholder="" size="mini" :disabled="dialogStatus == 'view'">
                            <el-option label="大于" value=">"></el-option>
                            <el-option label="小于" value="<"></el-option>
                            <el-option label="大于等于" value=">="></el-option>
                            <el-option label="小于等于" value="<="></el-option>
                            <el-option label="等于" value="="></el-option>
                            <el-option label="不等于" value="!="></el-option>
                          </el-select>
                        </template>
                      </el-table-column>
                      <el-table-column label="默认值" header-align="center" align="center">
                        <template slot-scope="scope">
                          <el-input placeholder="" size="mini" v-model="scope.row.defaultValue" :disabled="dialogStatus == 'view'"></el-input>
                        </template>
                      </el-table-column>
                      <el-table-column width="80" label="是否必填" header-align="center" align="center">
                        <template slot-scope="scope">
                          <el-checkbox v-model="scope.row.isRequired" true-label='y' false-label='n' size="mini" :disabled="dialogStatus == 'view'"></el-checkbox>
                        </template>
                      </el-table-column>
                      <el-table-column label="操作" width="50">
                        <template slot-scope="scope">
                          <a title="删除" @click="deleteAbilityData(scope.row, scope.$index,'param')"  v-show="dialogStatus == 'create' || dialogStatus == 'update'">
                            <!-- <svg-icon icon-class="delete" style="width: 20px; height: 20px"></svg-icon> -->
                            <img src="../../assets/tdspic/delete.png" style="width: 20px; height: 20px" alt="">
                          </a>
                        </template>
                      </el-table-column>
                    </el-table>
                    <el-form-item size="mini" class="mt-12" label-width="10px" style="text-align: right">
                      <el-button type="primary" @click="addAbilityTableAttr('param', 'topic')"  v-show="dialogStatus == 'create' || dialogStatus == 'update'">添加参数</el-button>
                    </el-form-item>
                  </template>
                </template>
              </el-collapse-item>
            </el-collapse>
          </div>
        </el-form>
      </div>
      <span slot="slide-footer">
        <svg-icon icon-class="loading" style="width: 20px; height:20px;" v-if="testPic == 'loading'"></svg-icon>
        <svg-icon icon-class="checked" style="width: 20px; height:20px;" v-if="testPic == 'checked'"></svg-icon>
        <svg-icon icon-class="failure" style="width: 20px; height:20px;" v-if="testPic == 'failure'"></svg-icon>
        <el-button :disabled="testPic == 'loading'" v-if="dialogStatus=='create' || dialogStatus=='update'" type="primary" size="mini" @click="testSource(serviceModelForm.type)">测试</el-button>
        <!-- <el-button :disabled="submitButtonDis" v-if="dialogStatus!='create'" type="primary" size="mini" @click="exportSource()">导出</el-button>
        <el-button :disabled="submitButtonDis" v-if="dialogStatus=='create'" type="primary" size="mini" @click="importSource()">导入</el-button> -->
        <el-button :disabled="submitButtonDis" v-if="dialogStatus=='create'" type="primary" size="mini" @click="creatSource(serviceModelForm, serviceModelForm.type )">保存</el-button>
        <el-button :disabled="submitButtonDis" v-if="dialogStatus=='update'" type="primary" size="mini" @click="editorSource(serviceModelForm, serviceModelForm.type)">保存</el-button>
        <el-button size="mini" @click="closeSlide">取消</el-button>
      </span>

      <!-- 二级测试弹框 -->
      <div slot="sub-slide-content">
        <el-form ref="serviceTestForm" :model="serviceTestForm" label-width="140px" class="test-body">
          <el-form-item v-for="(item, index) in serviceTestForm.testForm"
            :label="item.paramName+':'"
            class="mt-12"
            :key="index"
            :prop="'testForm.'+index+ '.defaultValue'"
            :rules="serviceTestRule[index]">
            <el-input v-model="item.defaultValue" v-if="item.paramType == 'number' "></el-input>
            <el-input v-model="item.defaultValue" v-else></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="sub-slide-footer">
        <svg-icon icon-class="loading" style="width: 20px; height:20px;" v-if="testPic == 'loading'"></svg-icon>
        <svg-icon icon-class="checked" style="width: 20px; height:20px;" v-if="testPic == 'checked'"></svg-icon>
        <svg-icon icon-class="failure" style="width: 20px; height:20px;" v-if="testPic == 'failure'"></svg-icon>
        <el-button :disabled="testPic == 'loading'"  type="primary" size="mini"  @click="creatTest(serviceTestForm)" v-loading.fullscreen.lock="fullscreenLoading">测试</el-button>
        <el-button size="mini" @click="slideSubClose()">返回</el-button>
      </span>
    </slide-box>
    <export-import
      :batchExportDialog="batchExportDialog"
      @close="slideClose"
      @handleModel="modelJsonFile"
    ></export-import>
  </div>
</template>

<script>
import { isValidateDateTime,isValidateNumber } from '@/utils/validate'
import request from '@/utils/request'
import ListCard from '@/components/ListCard'
import SlideBox from '@/components/SlideBox'
import CrLoading from '@/components/CrLoading'
import CrHelpPopver from '@/components/CrHelpPopver'
import { mapGetters } from 'vuex'
import { funDownload } from '@/utils/index'
import ExportImport from '@/components/ExportImport'
export default {
  components: {
    ListCard,
    SlideBox,
    CrLoading,
    CrHelpPopver,
    ExportImport
  },
  data() {
    return {
      operatePermission: true,
      batchExportDialog: false,
      popoverVisible: false,
      tempRow: null,
      tempFormData: null,
      isImportModel: false,
      testPic:'start',
      loading: true,
      visible2:false,
      /******** 表格搜索数据 ********/
      query: {
        name: ''
      },
        // 精确查询
      moreSearch: false,
      searchForm: {
        areaName: null,
        topicName: null,
        serviceSourceName: null,
        modelName: null,
        typeCode: 'asset',
        statusCode: null,
        requestMethod: null,
        pageNo: '1',
        queryType: 'public',
        queryString: '',
        pageSize: '5'
      },
      urlHeaderBodyChoose: 'first',// url参数列表切换
      statusCodeList: [], // 查询状态码
      areaList: [], // 资产领域（下拉框内容）
      themeList: [], // 资产主题（下拉框内容）
      abilityBigList: [], // 能力大类（下拉框内容）
      abilitySubList: [], // 能力子类（下拉框内容）

      fieldsData: [],
      fieldsDataAseet:[
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
          propsTitle: '数据源名称',
          field: 'serviceSourceName',
          show: true
        },
        {
          propsTitle: '发布类型',
          field: 'requestMethod'
        }
      ],
      fieldsDataAbility:[
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
          propsTitle: '数据源名称',
          field: 'serviceSourceName',
          show: true
        },
        {
          propsTitle: '发布类型',
          field: 'requestMethod'
        }
      ],

      /******** table列表数据start ********/
      tableData: {
        data: [],
        page: null
      },
      /******** table列表数据end ********/
      /********* 编辑与新增的分别 *********/
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新增',
        view: '查看'
      },
        // 新增修改弹框
      /********* 新增与编辑数据 *********/
      primaryModelType: [],
        /* 资产模型 */
      addDialog: false,
      fieldsAttrList: [], // 排完序的参数列表属性选值
      activeNames: ['1'], // 折叠面板默认展开
      serviceModelForm: {
        modelName: undefined, // 模型名称
        typeCode: undefined, // 模型类型
        // driveType: undefined,  // 驱动类型
        classifi: undefined, // 分类
        theme: undefined, // 主题
        des: undefined,  // 描述
        dataSource: undefined,  // 数据源
        tableName: undefined, // 数据源表
        parentId: undefined, // 父模型ID
        parsingExpress: undefined, // 解析表达式
        release: 'published',  // 是否发布
        requestMethod: 'POST', // 发布类型（如果不发布，则没有类型）
        topicName: undefined, // topicName
        time: undefined, // 缓存时间
        type: undefined, // 类型
        apiSource: undefined, // API数据源
        url: undefined, // URL
        // bodyPattern: undefined,//能力模型body模板
        // 资产模型-表-属性列表信息
        dataModelTableAttrTable: [],
        // 资产模型-表格-参数列表信息
        dataModelTableParamTable: [],
        // 资产模型-查询表达式-属性列表信息
        dataModelAttrTable: [],
        // 资产模型-查询表达式-参数列表信息
        dataModelParamTable: [],
        // 能力模型-第一种模型-属性列表
        abilityFirstModelAttrTable: [],
        // 能力模型-第一种模型-参数列表
        abilityFirstModelParamTable: [],
        // 能力模型-第二种模型-body参数列表
        abilityFirstModelParamBodyTable: [],
        // 能力模型-第二种模型-属性列表
        abilitySecondModelAttrTable: [],
        // 能力模型-第二种模型-参数列表
        abilitySecondModelParamTable: [],

      },
      assetFieldList: [],  // 资产领域数据
      assetThemeList: [],  // 资产主题数据
      parentModelList: [], // 父模型数据
      dataSourceList: [],  // 数据源数据
      serviceTableList: [], // 数据源表
      serviceTableListCope: [], // 数据源表搜索用
      modelTypeChoose: '', // 一级模型类型
      dataModelTypeChoose: '', // 数据模型子类型
      abilityModelSubChoose: '', // 能力模型子类型

      releaseStute: 'published',
      assetParamNameList: [],  // 属性列表数据
      serviceModelRule: {
        typeCode: [{ required: true, message: '请选择模型类型' }],
        modelName: [{ required: true, message: '请输入模型名称' }],
        dataSource: [{ required: true, message: '请选择数据源' }]
      },  // 验证
      nameRepeat: true,
      expressionTypeDeleteDis: 'table',
        /* 能力模型 */
      abilityModelBigTyle: [],
      abilityModelBigSubType: [],
      abilityBigSubId: [],
      /********* 服务授权数据（消息、api、数据） *********/
      /********* 测试数据 *********/
        // 测试弹框,
      testDialog:false,
      fullscreenLoading: false, // 测试等待
      testState: '',
      dataModelParamTableFoo: true, // 查询表达式参数属性不能修改
        // 测试数据
      serviceTestForm: {
        testForm: [],
      },
      serviceTestRule: [

      ],
      submitButtonDis: false
    }
  },
  mounted() {
    this.fetchAssetFieldTable()
    this.getAbilityModelSubValue()
    // 查询参数
    this.queryAssetFirst()
    this.queryAbilityFirst()
    // this.fetchStatusCode()
    this.test()
    this.$store.getters.userInfo.menu.forEach(item => {
      item.children.forEach(subItem => {
        if(subItem.name === '服务模型管理'){
          this.operatePermission = subItem.operationIds.indexOf('3') !== -1
          console.log(subItem.name+'页面操作权限',this.operatePermission)
        }
      })
    })
    // console.log(this.$store.state.userInfo)
  },
  watch: {
    dataModelTableAttrTable:{
      handler(newVal, oldVal){
        // console.log(newVal)
        // const tempFields = []
        // newVal.forEach(item => {
        //   tempFields.push(item.fieldName)
        // })
        // this.fieldsAttrList = tempFields.sort()
      },
      deep: true
    }
  },
  computed: {
    dataModelTableAttrTable() {
      return this.serviceModelForm.dataModelTableAttrTable
    }
    // ...mapGetters([
    //   'userInfo'
    // ])
  },
  beforeDestroy() {
    $(document).off('keyup')
  },
  methods: {
    testSqlLoad () {
      this.serviceModelForm.fieldLoad = true
    },
    testLoad () {
      this.serviceModelForm.fieldLoad = true
    },
    headBodyTab(val){
      console.log(this.urlHeaderBodyChoose)
    },
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
    },
    slideClose(){
      this.cancelSource()
      this.abilityModelSubChoose = ''
      this.batchExportDialog = false
      this.addDialog = false
      this.popoverVisible = false
      this.isImportModel = false
      $('#modelFile').val('')
    },
    // 关闭二级弹框
    closeSubSlide(){
      this.testDialog = false
    },
    // 点击关闭二级弹框
    slideSubClose(){
      this.$refs.slideBox.subClose()
    },
    /****** 查询重置、获取状态码start*******/
    fetchStatusCode(){
      request({
        url: '/api/service/model/query_status',
        method: 'get',
        params: {
          type: 'model_status_code'
        }
      }).then(({ data }) => {
        this.statusCodeList = data.data
      })
    },
    resetQuery(){
      let typeCode = this.searchForm.typeCode
      this.searchForm = {
        areaName: null,
        topicName: null,
        serviceSourceName: null,
        modelName: null,
        typeCode: typeCode,
        statusCode: null,
        pageNo: '1',
        pageSize: '5'
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
    /******** 精确查询下拉框内容end *********/
    /********  *********/
    //模型切换
    modelTab(typeCode) {
      if (!this.isAdmin() && typeCode === 'ability'){
        this.$message.error('您无权限访问该数据！')
        return
      }
      this.searchForm.queryString = ''
      this.searchForm.typeCode = typeCode
      this.fetchAssetFieldTable()
    },
    /******** 模型管理start  ********/
    fetchAssetFieldTable(val) {
      this.loading = true;
      this.tableData.data = []
      const obj = {}
      Object.keys(this.searchForm).forEach(key => {
        obj[key] = this.searchForm[key]
      })
      if(val==='1'){// 模糊查询
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
      }else{// 更多查询
        this.resetQuery()
        // 清空再赋值
        Object.keys(this.searchForm).forEach(key => {
          obj[key] = this.searchForm[key]
        })
        obj.queryString = this.query.name
        obj.queryType = 'public'
      }
      if(this.searchForm.typeCode == 'ability'){
        this.fieldsData = this.fieldsDataAbility
      }else if(this.searchForm.typeCode == 'asset'){
        this.fieldsData = this.fieldsDataAseet
      }
      request({
        url: '/api/service/model/query_model',
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
          }else{
            // this.$message.error(data.message)
          }
        },300)

      })
    },
    /******** 模型管理end  ********/
    /******** 新增start *********/
    // 新增弹框
    handleCreate() {
      this.resetTemp()
      this.cancelSource()
      this.testPic = 'start'
      this.dialogStatus = 'create'
      this.addDialog = true
      this.expressionTypeDeleteDis = 'table'
      this.abilityModelSubChoose = '';
      this.getDataSource()
    },
    // 一级模型类型改变
    modelTypeChange(type){
      this.modelTypeChoose = type;
      this.serviceModelForm.classifi = '';
      this.serviceModelForm.theme = '';
      this.serviceModelForm.dataSource = '';
      this.serviceModelForm.type = '';
      this.serviceModelForm.modelName = '';
      this.abilityModelSubChoose = '';
      if(type == 'asset'){
        this.getAssetField()
        this.getParentModel()
      }else{
        this.getAbilityBig(type)
      }
    },
    // 资产模型二级类型选择（表、查询表达式）
    dataModelTypeChange(type){
      this.dataModelTypeChoose = type;
      this.serviceModelForm.tableName = '';
      setTimeout(function(){
        const e = $.Event('keyup')
        e.keyCode = 13
        $('#test-select').focus()
        $(document).trigger(e)
      })

    },
    // 二级模型类型改变
    abilitySubTyleChange(name){
      this.serviceModelForm.theme = '';
      this.serviceModelForm.type = '';
      this.abilityModelSubChoose = '';
      this.getAbilitySub(name)
    },
    // 三级模型类型改变
    abilityBigSubTyleChange(name){
      console.log('能力模型二类',name)
      if(name == 'MQ'){
        this.abilityModelSubChoose = 'topic'
      }else {
        this.abilityModelSubChoose = 'url'
      }
    },
    isAdmin(){
      return this.$store.getters.userInfo.roles === 'administrator'
    },
      /******** 获取资产领域、资产主题列表(获取能力子类)、数据源与数据源表start ********/
    // 获取一级模型类别（区分资产模型模型与能力模型）
    getAbilityModelSubValue(){
      request({
        url: '/api/service/model/query_code',
        method: 'get',
      }).then(({ data }) => {
        if(data.code === 200){
          this.primaryModelType = data.data
          console.log(this.$store.getters.userInfo)
          if(this.$store.getters.userInfo.roles !== 'administrator'){
            const _index = this.primaryModelType.indexOf(this.primaryModelType.find(item => item.displayName === '能力模型'))
            this.primaryModelType.splice(_index,1)
          }
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{

        }
      })
    },
    // 获取所有数据源table(资产模型与能力模型都有数据源)
    getDataSource() {
      request({
        url: '/api/servicesource/query_servicesource_all',
        method: 'get',
        params: {
          statusCode: 'enabled'
        }
      }).then(({ data }) => {
        if(data.code === 200){
          this.dataSourceList = data.data
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{

        }
      })
    },

    // 获取资产模型大类
    getAssetField() {
      request({
        url: '/api/dataasset/area/query_area',
        method: 'get',
      }).then(({ data }) => {
        if(data.code === 200){
          this.assetFieldList = data.data.list
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{

        }
      })
    },
    // 获取父模型
    getParentModel() {
      request({
        url: '/api/service/model/get_parent_model',
        method: 'get'
      }).then(({ data }) => {
        this.parentModelList = data.data
        console.log(this.parentModelList)
        // this.statusCodeList = data.data
      })
    },
    // 获取资产模型
    getAssetTheme(name) {
      this.assetThemeList = [];
      this.serviceModelForm.theme = '';
      let dataParam = {
        areaName: name
      }
      request({
        url: '/api/dataasset/topic/query_topic',
        method: 'get',
        params: dataParam
      }).then(({ data }) => {
        if(data.code === 200){
          this.assetThemeList = data.data.list
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{

        }
      })
    },
    // 获取所有数据源表
    getServiceTable(id){
      this.serviceModelForm.tableName = ''
      this.dataModelTypeChoose = this.dataModelTypeChoose !== '' ? this.dataModelTypeChoose:'hiveThird'
      setTimeout(function(){
        const e = $.Event('keyup')
        e.keyCode = 13
        if(this.serviceModelForm) {
          $('#test-select').focus()
        }
        $(document).trigger(e)
      })
      // let dataParam = {
      //   id:id
      // }
      // request({
      //   url: '/api/servicesource/get_servicesource_tables',
      //   method: 'get',
      //   params: dataParam
      // }).then(({ data }) => {
      //   if(data.code === 200){
      //     this.serviceTableList = data.data
      //     this.serviceTableListCope = JSON.parse(JSON.stringify(this.serviceTableList))
      //   }else if(data.code === 4010){
      //     this.$store.dispatch('LogOut').then(() => {
      //       location.reload()
      //     })
      //   }else{

      //   }
      // })
    },
    // 获取数据源表的参数 与 参数名列表
    tableNameChange(id, table) {
      this.serviceModelForm.dataModelTableParamTable = []
      let dataParam = {
        id:id,
        tableName: table
      }
      request({
        url: '/api/servicesource/get_servicesource_fields',
        method: 'get',
        params: dataParam
      }).then(({ data }) => {
        if(data.code === 200){
          // console.log(data.data)
          const tempFields = []
          data.data.forEach(item => {
            tempFields.push(item.fieldName)
          })
          this.fieldsAttrList = tempFields.sort()
          this.serviceModelForm.dataModelTableAttrTable = data.data
          this.assetParamNameList = data.data
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{

        }
      })
    },

    // 获取能力模型大类函数
    getAbilityBig(code){
      request({
        url: '/api/service/model/query_code',
        method: 'get',
        params: {
          'parentCode': code
        }
      }).then(({ data }) => {
        if(data.code === 200){
          this.abilityModelBigTyle = data.data
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{

        }
      })
    },
    // 获取能力模型子类函数
    getAbilitySub(code){
      request({
        url: '/api/service/model/query_code',
        method: 'get',
        params: {
          'parentCode': code
        }
      }).then(({ data }) => {
        if(data.code === 200){
          this.abilityModelBigSubType = data.data
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{

        }
      })
    },
      /******** 获取资产领域、资产主题列表、数据源与数据源表end ********/
      /******* 属性列表、参数列表获取start ********/
    // 参数类型获取
    changeParamName(name,index){
      // console.log(this.assetParamNameList)
      for(var i=0; i<this.assetParamNameList.length; i++){
        if(this.assetParamNameList[i].fieldName == name){
          // this.serviceModelForm.dataModelTableParamTable[index].paramType = this.assetParamNameList[i].dataType
          this.serviceModelForm.dataModelTableParamTable[index].paramDesc = this.assetParamNameList[i].fieldDesc
        }
      }
    },
      /******* 属性列表、参数列表获取end ********/
          /* 资产模型start */
    // 新增资产模型-表-属性（参数）列表函数
    addDataTableAttr(type) {
      if(type == 'sttr'){
        this.serviceModelForm.dataModelTableAttrTable.push(
          {
            fieldName: '', // 属性名
            dataType: '', // 类型
            fieldDesc: '', // 描述
            expression: '', // 表达式
            isDerived: 'y' // 派生字段
          }
        )
      }else {
        this.serviceModelForm.dataModelTableParamTable.push(
          {
            fieldName: '', // 属性名
            paramName:'', // 参数名
            paramType: '',
            paramDesc: undefined,
            defaultValue: '',
            operateType: '',
            isDerived: 'y', // 派生字段
            isRequired: 'y',
          }
        )
      }
      this.assetParamNameList = this.serviceModelForm.dataModelTableAttrTable
    },
      // 删除资产模型-表-属性（参数）列表函数
    deleteDataTable(row, index, type){
      let idData
      if(type == 'sttr'){
        // 编辑-删除需要调接口
        if(row.id){
          idData = {
            id: row.id
          }
          this.$confirm('确定删除此模型属性？', '提示', {
            type: 'warning'
          }).then(() => {
            request({
              url: '/api/service/model/del_model_field',
              method: 'post',
              data: idData
              }).then(({data}) => {
                if(data.code == 200){
                  this.$message.success({
                    message: '删除成功'
                  });
                  this.serviceModelForm.dataModelTableAttrTable.splice(index, 1)
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
        }else{
          this.serviceModelForm.dataModelTableAttrTable.splice(index, 1)
        }
      }else {
        if(row.id){
          idData = {
            id: row.id
          }
          this.$confirm('确定删除此模型参数？', '提示', {
            type: 'warning'
          }).then(() => {
            request({
              url: '/api/service/model/del_model_param',
              method: 'post',
              data: idData
              }).then(({data}) => {
                if(data.code == 200){
                  this.$message.success({
                    message: '删除成功'
                  });
                  this.serviceModelForm.dataModelTableParamTable.splice(index, 1)
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
        }else{
          this.serviceModelForm.dataModelTableParamTable.splice(index, 1)
        }
      }
    },
      // 新增资产模型-查询表达式-属性(参数)列表函数 (先不用)
    addDataQueryAttr(type){
      if(type == 'sttr'){
        this.serviceModelForm.dataModelAttrTable.push(
          {
            name: '',
            des: '',
            express: '',
            hidden: 'y',
          }
        )
      }else{
        this.serviceModelForm.dataModelParamTable.push(
          {
            name: 'P1',
            des: '',
            type: '',
            mandatory: true,
            hidden: 'y',
          }
        )
      }
    },
      // 删除资产模型-查询表达式-属性（参数）列表函数(先不用)
    deleteDataQueryAttr(row, type){
      if(type == 'sttr'){
        this.serviceModelForm.dataModelAttrTable = this.serviceModelForm.dataModelAttrTable.filter((ele, index, array) => {
          return ele.index != row.index;
        })
      }else{
        this.serviceModelForm.dataModelParamTable = this.serviceModelForm.dataModelParamTable.filter((ele, index, array) => {
          return ele.index != row.index;
        })
      }
    },
        /* 资产模型end */

        /* 能力模型start */
      // 新增能力模型-第一种模型-属性（参数）列表函数
    addAbilityTableAttr(type, ability){
      if(type == "sttr"){
        this.serviceModelForm.abilityFirstModelAttrTable.push(
          {
            fieldName: '', // 属性名称
            dataType: '', // 类型
            fieldDesc: '', // 描述
            expression: '' // 表达式
          }
        )
      }else if(type == 'param'){
        if(ability == 'url'){
          let fieldName = ''
          // fieldName = this.urlHeaderBodyChoose === 'first'?'head':'body'
          this.serviceModelForm.abilityFirstModelParamTable.push(
            {
              fieldName: fieldName, // 属性名称
              paramName: '', // 参数名称
              paramType: '',
              paramDesc: '',
              defaultValue: '',
              isRequired: 'y'
            }
          )
        }else if(ability == 'topic'){
          this.serviceModelForm.abilityFirstModelParamTable.push(
            {
              fieldName: '', // 属性名称
              paramName: '', // 参数名称
              paramType: '',
              paramDesc: '',
              defaultValue: '',
              operateType: '',
              isRequired: 'y'
            }
          )
        }
      }
    },
      // 删除能力模型-第一种-属性（参数）列表函数
    deleteAbilityData(row, index, type){
      let idData
      if(type == 'sttr'){
        // 编辑-删除需要调接口
        if(row.id){
          idData = {
            id: row.id
          }
          this.$confirm('确定删除此模型属性？', '提示', {
            type: 'warning'
          }).then(() => {
            request({
              url: '/api/service/model/del_model_field',
              method: 'post',
              data: idData
              }).then(({data}) => {
                if(data.code == 200){
                  this.$message.success({
                    message: '删除成功'
                  });
                  this.serviceModelForm.abilityFirstModelAttrTable.splice(index, 1)
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
        }else{
          this.serviceModelForm.abilityFirstModelAttrTable.splice(index, 1)
        }
      }else if(type == 'param'){
        if(row.id){
          idData = {
            id: row.id
          }
          this.$confirm('确定删除此模型参数？', '提示', {
            type: 'warning'
          }).then(() => {
            request({
              url: '/api/service/model/del_model_param',
              method: 'post',
              data: idData
              }).then(({data}) => {
                if(data.code == 200){
                  this.$message.success({
                    message: '删除成功'
                  });
                  this.serviceModelForm.abilityFirstModelParamTable.splice(index, 1)
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
        }else{
          this.serviceModelForm.abilityFirstModelParamTable.splice(index, 1)
        }
      }
    },
        /* 能力模型end */

      // 新增确定函数
    creatSource(form, type) {
      let paramData = {}
      // 资产模型的第三种
      // console.log(form, this.isImportModel)
      if(this.modelTypeChoose === 'asset' && this.dataModelTypeChoose == 'hiveThird' ){
        paramData = {
          modelName: form.modelName,  // 模型名称
          parentId: form.parentId === ''?undefined:form.parentId,
          modelDesc: form.des,  // 模型描述
          typeCode: form.typeCode,  // 模型类型
          topicId: this.isImportModel?form.topicId:form.theme, // 资产主题id
          serviceSourceId: form.dataSource, // 数据源id
          statusCode: form.release, // 是否发布
          requestMethod: form.requestMethod, // 发布类型
          expressionType: form.type, // 资产类型
          expression: form.tableName, // 数据源表名
          cacheDuration: form.time, // 缓存时间
          serviceModelFields: [], // 属性列表
          serviceModelParams: []  // 参数列表
        }
        // 属性
        for(var i=0; i<form.dataModelTableAttrTable.length; i++){
          paramData.serviceModelFields.push(
            {
              fieldName: form.dataModelTableAttrTable[i].fieldName, // 属性名
              dataType: form.dataModelTableAttrTable[i].dataType, // 类型
              fieldDesc: form.dataModelTableAttrTable[i].fieldDesc, // 描述
              expression: form.dataModelTableAttrTable[i].expression, // 表达式
              isDerived: form.dataModelTableAttrTable[i].isDerived, // 是否为新增
            }
          )
        }
        // 参数
        for(var i=0; i<form.dataModelTableParamTable.length; i++){
          paramData.serviceModelParams.push(
            {
              fieldName: form.dataModelTableParamTable[i].fieldName, // 属性名
              paramName: form.dataModelTableParamTable[i].paramName, // 参数名
              paramType: form.dataModelTableParamTable[i].paramType, // 类型
              operateType: form.dataModelTableParamTable[i].operateType, // 操作符
              paramDesc: form.dataModelTableParamTable[i].paramDesc, // 描述
              defaultValue: form.dataModelTableParamTable[i].defaultValue, // 默认值
              isRequired: form.dataModelTableParamTable[i].isRequired, // 是否必填
            }
          )
        }
      }else{
        // 其他模型
        if(type == 'table'){
          paramData = {
            parentId: form.parentId === ''?undefined:form.parentId,
            modelName: form.modelName,  // 模型名称
            modelDesc: form.des,  // 模型描述
            typeCode: form.typeCode,  // 模型类型
            topicId: this.isImportModel?form.topicId:form.theme, // 资产主题id
            serviceSourceId: form.dataSource, // 数据源id
            statusCode: form.release, // 是否发布
            requestMethod: form.requestMethod, // 发布类型
            expressionType: form.type, // 资产类型
            expression: form.tableName, // 数据源表名
            cacheDuration: form.time, // 缓存时间
            serviceModelFields: [], // 属性列表
            serviceModelParams: []  // 参数列表
          }
          for(var i=0; i<form.dataModelTableAttrTable.length; i++){
            paramData.serviceModelFields.push(
              {
                fieldName: form.dataModelTableAttrTable[i].fieldName, // 属性名
                dataType: form.dataModelTableAttrTable[i].dataType, // 类型
                fieldDesc: form.dataModelTableAttrTable[i].fieldDesc, // 描述
                expression: form.dataModelTableAttrTable[i].expression, // 表达式
                isDerived: form.dataModelTableAttrTable[i].isDerived, // 是否为新增
              }
            )
          }
          for(var i=0; i<form.dataModelTableParamTable.length; i++){
            paramData.serviceModelParams.push(
              {
                fieldName: form.dataModelTableParamTable[i].fieldName, // 属性名
                paramName: form.dataModelTableParamTable[i].paramName, // 参数名
                paramType: form.dataModelTableParamTable[i].paramType, // 类型
                operateType: form.dataModelTableParamTable[i].operateType, // 操作符
                paramDesc: form.dataModelTableParamTable[i].paramDesc, // 描述
                defaultValue: form.dataModelTableParamTable[i].defaultValue, // 默认值
                isRequired: form.dataModelTableParamTable[i].isRequired, // 是否必填
              }
            )
          }
        }else if(type == 'sql'){
          paramData = {
            parentId: form.parentId === ''?undefined:form.parentId,
            modelName: form.modelName,  // 模型名称
            modelDesc: form.des,  // 模型描述
            typeCode: form.typeCode,  // 模型类型
            topicId: this.isImportModel?form.topicId:form.theme, // 资产主题id
            serviceSourceId: form.dataSource, // 数据源id
            statusCode: form.release, // 是否发布
            requestMethod: form.requestMethod, // 发布类型
            expressionType: form.type, // 资产类型
            expression: form.parsingExpress, // sql表达式
            cacheDuration: form.time, // 缓存时间
            serviceModelFields: [], // 属性列表
            serviceModelParams: []  // 参数列表
          }
          for(var i=0; i<form.dataModelAttrTable.length; i++){
            paramData.serviceModelFields.push(
              {
                fieldName: form.dataModelAttrTable[i].fieldName, // 属性名
                dataType: form.dataModelAttrTable[i].dataType, // 类型
                fieldDesc: form.dataModelAttrTable[i].fieldDesc, // 描述
                expression: form.dataModelAttrTable[i].expression, // 表达式
                isDerived: form.dataModelAttrTable[i].isDerived, // 是否为新增
              }
            )
          }
          for(var i=0; i<form.dataModelParamTable.length; i++){
            paramData.serviceModelParams.push(
              {
                fieldName: form.dataModelParamTable[i].fieldName, // 属性名
                paramName: form.dataModelParamTable[i].paramName, // 参数性名
                paramType: form.dataModelParamTable[i].paramType, // 类型
                // operateType: form.dataModelParamTable[i].operateType, // 操作符（界面先不显示）
                paramDesc: form.dataModelParamTable[i].paramDesc, // 描述
                defaultValue: form.dataModelParamTable[i].defaultValue, // 默认值
                isRequired: form.dataModelParamTable[i].isRequired, // 是否必填
              }
            )
          }
          // 能力模型
        }else if(type == 'MQ'){
          let topicId
          for(var i=0; i<this.abilityModelBigSubType.length; i++){
            if(this.abilityModelBigSubType[i].code == form.type){
              topicId = this.abilityModelBigSubType[i].id
            }
          }
          paramData = {
            parentId: form.parentId === ''?undefined:form.parentId,
            modelName: form.modelName,  // 模型名称
            modelDesc: form.des,  // 模型描述
            typeCode: form.typeCode,  // 模型类型
            topicId: topicId, // 资产主题id
            serviceSourceId: form.dataSource, // 数据源id
            statusCode: form.release, // 是否发布
            requestMethod: form.requestMethod, // 发布类型
            expressionType: 'topic', // 资产类型
            expression: form.expression, // topicName
            serviceModelFields: [], // 属性列表
            serviceModelParams: []  // 参数列表
          }
          for(var i=0; i<form.abilityFirstModelAttrTable.length; i++){
            paramData.serviceModelFields.push(
              {
                fieldName: form.abilityFirstModelAttrTable[i].fieldName, // 属性名
                dataType: form.abilityFirstModelAttrTable[i].dataType, // 类型
                fieldDesc: form.abilityFirstModelAttrTable[i].fieldDesc, // 描述
                expression: form.abilityFirstModelAttrTable[i].expression, // 表达式
                isDerived: form.abilityFirstModelAttrTable[i].isDerived, // 是否为新增
              }
            )
          }
          for(var i=0; i<form.abilityFirstModelParamTable.length; i++){
            paramData.serviceModelParams.push(
              {
                fieldName: form.abilityFirstModelParamTable[i].fieldName, // 属性名
                paramName: form.abilityFirstModelParamTable[i].paramName, // 参数名
                paramType: form.abilityFirstModelParamTable[i].paramType, // 类型
                operateType: form.abilityFirstModelParamTable[i].operateType, // 操作符
                paramDesc: form.abilityFirstModelParamTable[i].paramDesc, // 描述
                defaultValue: form.abilityFirstModelParamTable[i].defaultValue, // 默认值
                isRequired: form.abilityFirstModelParamTable[i].isRequired, // 是否必填
              }
            )
          }
        }else {
          // 能力模型第二种
          let topicId
          for(var i=0; i<this.abilityModelBigSubType.length; i++){
            if(this.abilityModelBigSubType[i].code == form.type){
              topicId = this.abilityModelBigSubType[i].id
            }
          }
          paramData = {
            modelName: form.modelName,  // 模型名称
            modelDesc: form.des,  // 模型描述
            typeCode: form.typeCode,  // 模型类型
            topicId: this.isImportModel?form.topicId:topicId, // 资产主题id
            serviceSourceId: form.dataSource, // 数据源id
            statusCode: form.release, // 发布状态
            requestMethod: form.requestMethod, // 请求类型
            expressionType: 'url', // 资产类型
            expression: form.expression, // URL
            // bodyPattern: JSON.stringify(JSON.parse(form.bodyPattern)), //body模板
            serviceModelFields: [], // 属性列表
            serviceModelParams: [],  // 参数列表
          }
          for(var i=0; i<form.abilityFirstModelAttrTable.length; i++){
            paramData.serviceModelParams.push(
              {
                fieldName: form.abilityFirstModelAttrTable[i].fieldName, // 属性名
                dataType: form.abilityFirstModelAttrTable[i].dataType, // 类型
                fieldDesc: form.abilityFirstModelAttrTable[i].fieldDesc, // 描述
                expression: form.abilityFirstModelAttrTable[i].expression, // 表达式
                isDerived: form.abilityFirstModelAttrTable[i].hidden, // 是否为新增
              }
            )
          }
          for(var i=0; i<form.abilityFirstModelParamTable.length; i++){
            paramData.serviceModelParams.push(
              {
                fieldName: form.abilityFirstModelParamTable[i].fieldName, // 属性名
                paramName: form.abilityFirstModelParamTable[i].paramName, // 参数名
                paramType: form.abilityFirstModelParamTable[i].paramType, // 类型
                operateType: form.abilityFirstModelParamTable[i].operateType, // 操作符
                paramDesc: form.abilityFirstModelParamTable[i].paramDesc, // 描述
                defaultValue: form.abilityFirstModelParamTable[i].defaultValue, // 默认值
                isRequired: form.abilityFirstModelParamTable[i].isRequired, // 是否必填
              }
            )
          }
          // for(var i=0; i<form.abilityFirstModelParamBodyTable.length; i++){
          //   paramData.serviceModelParams.push(
          //     {
          //       fieldName: form.abilityFirstModelParamBodyTable[i].fieldName, // 属性名
          //       paramName: form.abilityFirstModelParamBodyTable[i].paramName, // 参数名
          //       paramType: form.abilityFirstModelParamBodyTable[i].paramType, // 类型
          //       operateType: form.abilityFirstModelParamBodyTable[i].operateType, // 操作符
          //       paramDesc: form.abilityFirstModelParamBodyTable[i].paramDesc, // 描述
          //       defaultValue: form.abilityFirstModelParamBodyTable[i].defaultValue, // 默认值
          //       isRequired: form.abilityFirstModelParamBodyTable[i].isRequired, // 是否必填
          //     }
          //   )
          // }
        }
      }
        // 判断必填项
      let fieldName = true // 属性名
      let paramName = true // 参数名
      let paramType = true // 参数类型
      let defaultValue = true // 默认值
      let operateType = true // 操作符
      for(var j=0; j<paramData.serviceModelParams.length; j++){
        // 判断属性名是否填写
        if(paramData.serviceModelParams[j].fieldName === undefined || paramData.serviceModelParams[j].fieldName == ''){
          // console.log(paramData.serviceModelParams[j].operateType)
          // if(paramData.serviceModelParams[j].operateType === undefined){
            this.$message.error('请填写属性名')
          // }else{
          //   this.$message.error('请填写类型')
          // }
          fieldName = false
          break;
        }else {
          //
        }
        // 判断参数名是否必填
        if(paramData.serviceModelParams[j].paramName === undefined || paramData.serviceModelParams[j].paramName == ''){
          this.$message.error('请填写参数名')
          paramName = false
          break;
        }else {
          //
        }
        // 判断参数类型是否填写
        if(paramData.serviceModelParams[j].paramType === undefined || paramData.serviceModelParams[j].paramType == ''){
          this.$message.error('请填写参数类型')
          paramType = false
          break;
        }else {
          //
        }
        // 判断操作符是否填写
        if(paramData.serviceModelParams[j].operateType != undefined){
          if(paramData.serviceModelParams[j].operateType === undefined || paramData.serviceModelParams[j].operateType == ''){
            this.$message.error('请填写操作符')
            operateType = false
            break;
          }else {
            //
          }
        }
      }
      if(fieldName && paramName && paramType && defaultValue && operateType){
        this.$refs.serviceModelForm.validate(valid => {
          if(valid){
            // console.log(paramData)
            if(this.testPic !== 'checked'){
              this.$message.error('请先完成正确测试！')
              return
            }
            this.submitButtonDis = true
            request({
              url:'/api/service/model/add_model',
              method: 'post',
              data: paramData,
            }).then(({data}) => {
              if(data.code == 200){
                this.$message.success('新增成功');
                this.submitButtonDis = false
                this.closeSlide()
                this.fetchAssetFieldTable()
              } else if(data.code === 4010){
                this.$store.dispatch('LogOut').then(() => {
                  location.reload()
                })
              }else{
                this.$message.error(data.message)
                // this.$message.error(data.message)
                this.submitButtonDis = false
              }
            })
          }
        })
      }
    },

    /******** 新增end *********/
      // 名字查重
    checkName(name){
      if(name && name != '' ){
        let param = {
          'modelName': name
        }
        request({
          url: '/api/service/model/check_model',
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
      // 取消
    remove(){
      this.resetTemp()
      this.addDialog = false;
    },
    // 取消新增或编辑
    cancelSource(){
      this.$refs.serviceModelForm.resetFields()
    },
    cancelSubSource(){
      this.$refs.serviceTestForm.resetFields()
    },
    /******** 测试、解析start *********/
      // 测试弹框
    testSource(type) {

      this.$refs.serviceModelForm.validate(valid => {
        if(valid){
          this.serviceTestRule = []
          this.popoverVisible = false
          const validateDateTime = (rule, value, callback) => {
            if (!isValidateDateTime(value)) {
              callback(new Error('请输入正确时间格式 yyyy-mm-dd 或 yyyy-mm-dd HH(24):mm:ss'))
            } else {
              callback()
            }
          }
          const validateNumber = (rule, value, callback) => {
            if (!isValidateNumber(value)) {
              callback(new Error('请输入数字'))
            } else {
              callback()
            }
          }
          this.cancelSubSource()
          this.resetTest()
          let requiredBounced = false;
          // 有参数裂列表  赋值参数列表到测试
          // 资产模型
          if(type == 'table'|| type =='' || type == 'sql'){
            if(this.serviceModelForm.dataModelTableParamTable.length>0 || this.serviceModelForm.dataModelParamTable.length>0 ){
              if(type == 'table'|| type ==''){
                // 表和redis
                for(var i=0;i<this.serviceModelForm.dataModelTableParamTable.length; i++){
                  this.serviceTestForm.testForm.push(
                    {
                      fieldName: this.serviceModelForm.dataModelTableParamTable[i].fieldName,
                      paramName: this.serviceModelForm.dataModelTableParamTable[i].paramName,
                      paramType: this.serviceModelForm.dataModelTableParamTable[i].paramType,
                      defaultValue: this.serviceModelForm.dataModelTableParamTable[i].defaultValue,
                      operateType: this.serviceModelForm.dataModelTableParamTable[i].operateType,
                      isRequired: this.serviceModelForm.dataModelTableParamTable[i].isRequired
                    }
                  )
                }
                this.serviceTestForm.modelFields = this.serviceModelForm.dataModelTableAttrTable
              }else if(type == 'sql'){
                for(var i=0;i<this.serviceModelForm.dataModelParamTable.length; i++){
                  this.serviceTestForm.testForm.push(
                    {
                      fieldName: this.serviceModelForm.dataModelParamTable[i].fieldName,
                      paramName: this.serviceModelForm.dataModelParamTable[i].paramName,
                      paramType: this.serviceModelForm.dataModelParamTable[i].paramType,
                      defaultValue: this.serviceModelForm.dataModelParamTable[i].defaultValue,
                      // operateType: this.serviceModelForm.dataModelParamTable[i].operateType,
                      isRequired: this.serviceModelForm.dataModelParamTable[i].isRequired
                    }
                  )
                }
                this.serviceTestForm.modelFields = this.serviceModelForm.dataModelAttrTable
              }
              requiredBounced = true
            }else{
              // 没有参数列表  直接测试（不要弹框）
              requiredBounced = false
              let paramData
              if(type == 'table'|| type==''){
                paramData = {
                  id: this.serviceModelForm.dataSource,
                  tableName: this.serviceModelForm.tableName,
                  modelFields: []
                };
                for(var i=0; i<this.serviceModelForm.dataModelTableAttrTable.length; i++){
                  paramData.modelFields.push(
                    {
                      fieldName: this.serviceModelForm.dataModelTableAttrTable[i].fieldName,
                      isDerived: this.serviceModelForm.dataModelTableAttrTable[i].isDerived,
                      expression: this.serviceModelForm.dataModelTableAttrTable[i].expression
                    }
                  )
                }
              }else if(type == 'sql'){
                paramData = {
                  id: this.serviceModelForm.dataSource,
                  expression: this.serviceModelForm.parsingExpress,
                };
              }
              this.testPic = 'loading'
              request({
                url:'/api/servicesource/execute_servicesource_sql',
                method: 'post',
                data: paramData,
              }).then(({data}) => {
                if(data.code == 200){
                  this.$message.success('测试成功');
                  this.testPic = 'checked'
                } else if(data.code === 4010){
                  this.$store.dispatch('LogOut').then(() => {
                    location.reload()
                  })
                }else{
                  this.testPic = 'failure'
                  this.$message.error(data.message)
                }
              })
            }
          }else{
            // 能力模型
            if(this.serviceModelForm.abilityFirstModelParamTable.length>0){
              // 有参数裂列表  赋值参数列表到测试
              for(var i=0;i<this.serviceModelForm.abilityFirstModelParamTable.length; i++){
                this.serviceTestForm.testForm.push(
                  {
                    paramName: this.serviceModelForm.abilityFirstModelParamTable[i].paramName,
                    fieldName: this.serviceModelForm.abilityFirstModelParamTable[i].fieldName,
                    paramType: this.serviceModelForm.abilityFirstModelParamTable[i].paramType,
                    defaultValue: this.serviceModelForm.abilityFirstModelParamTable[i].defaultValue,
                    operateType: this.serviceModelForm.abilityFirstModelParamTable[i].operateType,
                    isRequired: this.serviceModelForm.abilityFirstModelParamTable[i].isRequired,
                  }
                )
              }
              this.serviceTestForm.modelFields = this.serviceModelForm.abilityFirstModelAttrTable
              requiredBounced = true
            } else {
              requiredBounced = false
              // 没有参数列表 传属性列表  直接测试（不要弹框）
              let paramData = {
                id: this.serviceModelForm.dataSource,
                expression: this.serviceModelForm.expression,
                modelFields: []
              }
              for(var i=0;i<this.serviceModelForm.abilityFirstModelAttrTable.length; i++){
                paramData.modelFields.push(
                  {
                    fieldName: this.serviceModelForm.abilityFirstModelAttrTable[i].fieldName,
                    // isDerived: this.serviceModelForm.abilityFirstModelAttrTable[i].isDerived,
                    expression: this.serviceModelForm.abilityFirstModelAttrTable[i].expression
                  }
                )
              }
              this.testPic = 'loading'
              request({
                url:'/api/servicesource/execute_servicesource_sql',
                method: 'post',
                data: paramData,
              }).then(({data}) => {
                if(data.code == 200){
                  this.$message.success('测试成功');
                  this.testPic = 'checked'
                } else if(data.code === 4010){
                  this.$store.dispatch('LogOut').then(() => {
                    location.reload()
                  })
                }else{
                  this.$message.error(data.message)
                  this.testPic = 'failure'
                }
              })
            }
          }
          // 测试
          if(requiredBounced){
            // 判断必填项
            let fieldName = true // 属性名
            let paramName = true // 参数名
            let paramType = true // 参数类型
            let defaultValue = true // 默认值
            let operateType = true // 操作符
            for(var j=0; j<this.serviceTestForm.testForm.length; j++){
              // 判断属性名是否填写
              if(this.serviceTestForm.testForm[j].fieldName === undefined || this.serviceTestForm.testForm[j].fieldName == ''){
                this.$message.error('请填写属性名')
                fieldName = false
                break;
              }else {
                //
              }
              // 判断参数名是否必填
              if(this.serviceTestForm.testForm[j].paramName === undefined || this.serviceTestForm.testForm[j].paramName == ''){
                this.$message.error('请填写参数名')
                paramName = false
                break;
              }else {
                //
              }
              // 判断参数类型是否填写
              if(this.serviceTestForm.testForm[j].paramType === undefined || this.serviceTestForm.testForm[j].paramType == ''){
                this.$message.error('请填写参数类型')
                paramType = false
                break;
              }else {
                //
              }
              // 判断操作符是否填写

              if(this.serviceTestForm.testForm[j].operateType != undefined){
                if(this.serviceTestForm.testForm[j].operateType === undefined || this.serviceTestForm.testForm[j].operateType == ''){
                  if(this.serviceModelForm.expressionType !== 'url'){
                    this.$message.error('请填写操作符')
                    operateType = false
                    break;
                  }
                }else {
                  //
                }
              }

            }
            if(fieldName && paramName && paramType && defaultValue && operateType){
              this.testDialog = true
            }
          }
          let required = { required: true, message: '请输入必填项'}
          let num = { validator:validateNumber }
          let date = { validator: validateDateTime }
          for(var i=0; i<this.serviceTestForm.testForm.length; i++){
            this.serviceTestRule[i] = []
            if(this.serviceTestForm.testForm[i].isRequired == 'y'){
              this.serviceTestRule[i].push(required)
            }
            if(this.serviceTestForm.testForm[i].paramType == 'number'){
              this.serviceTestRule[i].push(num)
            }
            if(this.serviceTestForm.testForm[i].paramType == 'date'){
              this.serviceTestRule[i].push(date)
            }
          }
          // console.log('testRule',this.serviceTestRule)
        }
      })
    },
    // 解析body
    // parseTemplate(val){
    //   try {
    //         const obj=JSON.parse(val);
    //         if(typeof obj == 'object' && obj ){
    //           request({
    //             url: '/api/service/model/get_body_pattern',
    //             method: 'post',
    //             data: obj
    //           }).then(({data}) => {
    //             if(data.code === 200){
    //               if(this.activeNames.indexOf(this.activeNames.find(item => item==='3')) === -1){
    //                 this.activeNames.push('3')
    //                 this.urlHeaderBodyChoose = 'second'
    //               }
    //               this.serviceModelForm.abilityFirstModelParamBodyTable = data.data
    //               this.$message.success('解析成功')
    //             }else{
    //               this.$message.error(data.message)
    //             }
    //           })
    //           return true;
    //         }

    //   } catch(e) {
    //     this.$message.error('JSON格式错误！');
    //     return false;
    //   }
    // },
      // 确定测试(有测试弹框)
    creatTest(form) {
      this.testState = '';
      this.testPic = 'start'
      // sql传id、expression、modelParam
      // table 传id、tableName、modelParam、modelFields
      // 能力模型第一种  传id（既数据源id）、url、modelParam、modelFields
      // 能力模型第二种  传id（既数据源id）、topicName、modelParam、modelFields
      let paramData
      if(this.serviceModelForm.type === 'sql'){
        paramData = {
          id: this.serviceModelForm.dataSource,
          expression: this.serviceModelForm.parsingExpress,
          modelParams: [],
        };
        // 参数
        for(var i=0; i<form.testForm.length; i++){
          paramData.modelParams.push(
            {
              fieldName: form.testForm[i].fieldName,
              paramType: form.testForm[i].paramType,
              paramName: form.testForm[i].paramName,
              defaultValue: form.testForm[i].defaultValue,
              operateType: form.testForm[i].operateType,
              isRequired: form.testForm[i].isRequired
            }
          )
        }
      }else if(this.serviceModelForm.type === 'table' || this.serviceModelForm.type === ''){
        paramData = {
          id: this.serviceModelForm.dataSource,
          tableName: this.serviceModelForm.tableName,
          modelParams: [], // 参数列表
          modelFields: []  // 属性列表
        };
        // 参数
        for(var i=0; i<form.testForm.length; i++){
          paramData.modelParams.push(
            {
              fieldName: form.testForm[i].fieldName,
              paramType: form.testForm[i].paramType,
              paramName: form.testForm[i].paramName,
              defaultValue: form.testForm[i].defaultValue,
              operateType: form.testForm[i].operateType,
              isRequired: form.testForm[i].isRequired
            }
          )
        }
        // 属性
        for(var j=0; j<form.modelFields.length; j++){
          paramData.modelFields.push(
            {
              fieldName: form.modelFields[j].fieldName,
              isDerived: form.modelFields[j].isDerived,
              expression: form.modelFields[j].expression
            }
          )
        }
      }else{
        if(this.serviceModelForm.type == 'MQ' || this.serviceModelForm.type == '消息列队'){
          paramData = {
            id: this.serviceModelForm.dataSource,
            topicName: this.serviceModelForm.expression,
            modelParams: [], // 参数列表
            modelFields: []  // 属性列表
          };
        }else{
          paramData = {
            id: this.serviceModelForm.dataSource,
            url: this.serviceModelForm.expression,
            modelParams: [], // 参数列表
            modelFields: []  // 属性列表
          };
        }
        // 参数
        for(var i=0; i<form.testForm.length; i++){
          paramData.modelParams.push(
            {
              fieldName: form.testForm[i].fieldName,
              paramType: form.testForm[i].paramType,
              paramName: form.testForm[i].paramName,
              defaultValue: form.testForm[i].defaultValue,
              operateType: form.testForm[i].operateType,
              isRequired: form.testForm[i].isRequired
            }
          )
        }
        // 属性
        for(var j=0; j<form.modelFields.length; j++){
          paramData.modelFields.push(
            {
              fieldName: form.modelFields[j].fieldName,
              // isDerived: form.modelFields[j].isDerived,
              expression: form.modelFields[j].expression
            }
          )
        }
      }
      this.$refs.serviceTestForm.validate(valid => {
        if(valid){
          this.testPic = 'loading'
          request({
            url:'/api/servicesource/execute_servicesource_sql',
            method: 'post',
            data: paramData,
          }).then(({data}) => {
            if(data.code == 200){
              this.$message.success('测试成功');
              this.testPic = 'checked'
            } else if(data.code === 4010){
              this.$store.dispatch('LogOut').then(() => {
                location.reload()
              })
            }else{
              this.$message.error(data.message)
              this.testPic = 'failure'
              this.fullscreenLoading = false;
            }
            this.fullscreenLoading = false;
          })
        }
      })
    },
      // 解析函数
    parsingSql(express){
      let paramData = {
        sql: express
      }
      request({
        url:'/api/servicesource/parse_servicesource_sql',
        method: 'post',
        data: paramData,
      }).then(({data}) => {
        if(data.code == 200){
          // console.log(data)
          this.$message.success('解析成功');
          const newFieldList = []
          const newParamList = []
          data.data.fieldList.forEach((item,index) => {
            const obj = this.serviceModelForm.dataModelAttrTable.find(subItem => subItem.fieldName === item.fieldName)
            if(obj){
              $.extend(obj,item)
              newFieldList.push(obj)
            }else{
              newFieldList.push(item)
            }
          })
          data.data.paramList.forEach((item,index) => {
            const obj = this.serviceModelForm.dataModelParamTable.find(subItem => subItem.paramName === item.paramName)
            // console.log(obj,item)
            if(obj){
              $.extend(obj,item)
              newParamList.push(obj)
            }else{
              newParamList.push(item)
            }
          })
          // console.log(this.serviceModelForm.dataModelAttrTable, newFieldList)
          this.serviceModelForm.dataModelAttrTable = newFieldList
          this.serviceModelForm.dataModelParamTable = newParamList
          this.serviceModelForm.dataModelParamTable.forEach(item => {
            item.fieldName = ''
          })
        } else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{
          this.$message.error(data.message)
        }
      })
    },
    /******** 测试、解析end *********/

    /******** 编辑与删除start *********/
    // 编辑弹框
    handleUpdate(row) {
      // console.log(row)
      this.tempRow = row
      this.testPic = 'start'
      this.expressionTypeDeleteDis = row.expressionType
      this.cancelSource()
      this.resetTemp()
      this.getDataSource() // 获取所有数据源
      this.getAssetTheme(row.areaName)  // 获取所有主题
      this.getServiceTable(row.serviceSourceId)  // 获取所有数据源表
      this.assetParamNameList = row.serviceModelFields  // 获取该模型的表字段
      this.getAbilityBig('ability')// 获取所有的大类
      this.dialogStatus = 'update'
      this.addDialog = true
      this.modelTypeChoose = row.typeCode;
      if(row.expressionType){
        this.dataModelTypeChoose = row.expressionType;
        this.abilityModelSubChoose = row.expressionType;
      }else{
        this.dataModelTypeChoose = 'hiveThird'
      }
      // 重新发送请求 （因为存在前端的增删改）
      request({
        url: '/api/service/model/get_model',
        method: 'get',
        params: {
          id: row.id
        }
      }).then(({ data }) => {
        if(data.code === 200){
          this.tempFormData = data.data
          // console.log(data.data)
          const tempFields = []
          data.data.serviceModelFields.forEach(item => {
            tempFields.push(item.fieldName)
          })
          this.fieldsAttrList = tempFields.sort()
          if(row.typeCode == "asset"){
            // 资产模型
            this.getParentModel()
            this.serviceModelForm = {
              id:data.data.id,
              fieldLoad:data.data.fieldLoad,// 用户更改模型时，是否进行了换表操作
              parentId: data.data.parentId,
              topicId:data.data.topicId,
              modelName: data.data.modelName, // 模型名称
              typeCode: data.data.typeCode, // 模型类型
              classifi: data.data.dataAssetArea.areaName, // 分类
              theme: data.data.dataAssetTopic.topicName, // 主题
              des: data.data.modelDesc, // 描述
              dataSource: data.data.serviceSourceId,  // 数据源
              tableName: data.data.expression, // 数据源表
              parsingExpress: data.data.expression, // 解析表达式
              release: data.data.statusCode,  // 是否发布
              requestMethod: data.data.requestMethod, // 发布类型
              time: data.data.cacheDuration, // 缓存时间
              type: data.data.expressionType, // 类型
              dataModelTableParamTable: data.data.serviceModelParams,  // 资产-表-参数
              dataModelParamTable: data.data.serviceModelParams, // 资产-查询-参数
              dataModelTableAttrTable: [], // 资产-表-属性
              dataModelAttrTable: [] // 资产-查询-属性
            }
            // 给资产-查询-属性 赋值
            for(var i=0; i<data.data.serviceModelFields.length; i++){
              this.serviceModelForm.dataModelAttrTable.push(
                {
                  id: data.data.serviceModelFields[i].id,
                  fieldName: data.data.serviceModelFields[i].fieldName, // 属性名
                  fieldDesc: data.data.serviceModelFields[i].fieldDesc, // 描述
                  expression: data.data.serviceModelFields[i].expression, // 表达式
                  isDerived: data.data.serviceModelFields[i].isDerived// 是否派生
                }
              )
            }
            // 给资产-表-属性 赋值
            for(var j=0; j<data.data.serviceModelFields.length; j++){
              this.serviceModelForm.dataModelTableAttrTable.push(
                {
                  id: data.data.serviceModelFields[j].id,
                  fieldName: data.data.serviceModelFields[j].fieldName, // 属性名
                  expression: data.data.serviceModelFields[j].expression, // 表达式
                  dataType: data.data.serviceModelFields[j].dataType, // 类型
                  fieldDesc:  data.data.serviceModelFields[j].fieldDesc, // 描述
                  isDerived: data.data.serviceModelFields[j].isDerived // 是否派生
                }
              )
            }

          } else if(row.typeCode == "ability"){
            // console.log(data.data.bodyPattern)
            this.serviceModelForm = {
              id:data.data.id,
              topicId:data.data.topicId,
              modelName: data.data.modelName, // 模型名称
              typeCode: data.data.typeCode, // 模型类型
              classifi: data.data.dataAssetArea.areaName, // 大类
              type: data.data.dataAssetTopic.topicName, // 子类
              dataSource: data.data.serviceSourceId,  // 数据源
              des: data.data.modelDesc, // 描述
              expressionType: data.data.expressionType,
              // bodyPattern: data.data.bodyPattern, //body模板
              release: data.data.statusCode,  // 是否发布
              requestMethod: data.data.requestMethod,// 发布类型
              expression: data.data.expression,// topicName
              abilityFirstModelAttrTable: [],
              abilityFirstModelParamTable: []
            }
              // 属性 赋值
            for(var i=0; i<data.data.serviceModelFields.length; i++){
              this.serviceModelForm.abilityFirstModelAttrTable.push(
                {
                  id: data.data.serviceModelFields[i].id,
                  dataType: data.data.serviceModelFields[i].dataType, // 类型
                  fieldName: data.data.serviceModelFields[i].fieldName, // 属性名
                  fieldDesc: data.data.serviceModelFields[i].fieldDesc, // 描述
                  expression: data.data.serviceModelFields[i].expression, // 表达式
                }
              )
            }
            // 参数 赋值
            for(var j=0; j<data.data.serviceModelParams.length; j++){
              if(data.data.expressionType == 'url'){
                // if(data.data.requestMethod === 'POST'){
                //   if(data.data.serviceModelParams[j].fieldName === 'head'){
                //     this.serviceModelForm.abilityFirstModelParamTable.push(
                //       {
                //         id: data.data.serviceModelParams[j].id,
                //         fieldName: data.data.serviceModelParams[j].fieldName,
                //         paramName: data.data.serviceModelParams[j].paramName,
                //         paramType: data.data.serviceModelParams[j].paramType,
                //         paramDesc: data.data.serviceModelParams[j].paramDesc,
                //         operateType: data.data.serviceModelParams[j].operateType,
                //         defaultValue: data.data.serviceModelParams[j].defaultValue,
                //         isRequired: data.data.serviceModelParams[j].isRequired,
                //       }
                //     )
                //   }else if(data.data.serviceModelParams[j].fieldName === 'body'){
                //     this.serviceModelForm.abilityFirstModelParamBodyTable = []
                //     this.serviceModelForm.abilityFirstModelParamBodyTable.push(
                //       {
                //         id: data.data.serviceModelParams[j].id,
                //         fieldName: data.data.serviceModelParams[j].fieldName,
                //         paramName: data.data.serviceModelParams[j].paramName,
                //         paramType: data.data.serviceModelParams[j].paramType,
                //         paramDesc: data.data.serviceModelParams[j].paramDesc,
                //         operateType: data.data.serviceModelParams[j].operateType,
                //         defaultValue: data.data.serviceModelParams[j].defaultValue,
                //         isRequired: data.data.serviceModelParams[j].isRequired,
                //       }
                //     )
                //   }
                // }else {
                  this.serviceModelForm.abilityFirstModelParamTable.push(
                    {
                      id: data.data.serviceModelParams[j].id,
                      fieldName: data.data.serviceModelParams[j].fieldName,
                      paramName: data.data.serviceModelParams[j].paramName,
                      paramType: data.data.serviceModelParams[j].paramType,
                      paramDesc: data.data.serviceModelParams[j].paramDesc,
                      operateType: data.data.serviceModelParams[j].operateType,
                      defaultValue: data.data.serviceModelParams[j].defaultValue,
                      isRequired: data.data.serviceModelParams[j].isRequired,
                    }
                  )
                // }
              }else if(data.data.expressionType == 'topic'){
                this.serviceModelForm.abilityFirstModelParamTable.push(
                  {
                    id: data.data.serviceModelParams[j].id,
                    fieldName: data.data.serviceModelParams[j].fieldName,
                    paramName: data.data.serviceModelParams[j].paramName,
                    paramType: data.data.serviceModelParams[j].paramType,
                    paramDesc: data.data.serviceModelParams[j].paramDesc,
                    operateType: data.data.serviceModelParams[j].operateType,
                    defaultValue: data.data.serviceModelParams[j].defaultValue,
                    isRequired: data.data.serviceModelParams[j].isRequired,
                  }
                )
              }
            }
          }
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{

        }
      })

    },
    // 编辑确定函数
    editorSource(form, type) {
      let paramData = {}
      if(type == 'table' || type == ''){
        // 能力模型table与第三种
        paramData = {
          id: form.id,
          fieldLoad: form.fieldLoad,
          parentId: form.parentId===''?undefined:form.parentId, // 父模型
          modelName: form.modelName,  // 模型名称
          modelDesc: form.des,  // 模型描述
          typeCode: form.typeCode,  // 模型类型
          topicId: form.topicId, // 资产主题id
          serviceSourceId: form.dataSource, // 数据源id
          statusCode: form.release, // 发布状态
          expressionType: form.type, // 资产类型
          expression: form.tableName, // 数据源表名
          cacheDuration: form.time, // 缓存时间
          serviceModelFields: [], // 属性列表
          serviceModelParams: []  // 参数列表
        }
        for(var i=0; i<form.dataModelTableAttrTable.length; i++){
          paramData.serviceModelFields.push(
            {
              id: form.fieldLoad ? undefined : form.dataModelTableAttrTable[i].id,
              fieldName: form.dataModelTableAttrTable[i].fieldName, // 属性名
              dataType: form.dataModelTableAttrTable[i].dataType, // 类型
              fieldDesc: form.dataModelTableAttrTable[i].fieldDesc, // 描述
              expression: form.dataModelTableAttrTable[i].expression, // 表达式
              isDerived: form.dataModelTableAttrTable[i].isDerived, // 是否为新增
            }
          )
        }
        for(var i=0; i<form.dataModelTableParamTable.length; i++){
          paramData.serviceModelParams.push(
            {
              id: form.fieldLoad ? undefined : form.dataModelTableParamTable[i].id,
              fieldName: form.dataModelTableParamTable[i].fieldName,// 属性名
              paramName: form.dataModelTableParamTable[i].paramName, // 参数名
              paramType: form.dataModelTableParamTable[i].paramType, // 类型
              operateType: form.dataModelTableParamTable[i].operateType, // 操作符
              paramDesc: form.dataModelTableParamTable[i].paramDesc, // 描述
              defaultValue: form.dataModelTableParamTable[i].defaultValue, // 默认值
              isRequired: form.dataModelTableParamTable[i].isRequired, // 是否必填
            }
          )
        }
      }else if(type == 'sql'){
        paramData = {
          id:form.id, // id
          fieldLoad: form.fieldLoad,
          parentId: form.parentId===''?undefined:form.parentId,
          modelName: form.modelName,  // 模型名称
          modelDesc: form.des,  // 模型描述
          typeCode: form.typeCode,  // 模型类型
          topicId: form.topicId, // 资产主题id
          serviceSourceId: form.dataSource, // 数据源id
          statusCode: form.release, // 发布状态
          expressionType: form.type, // 资产类型
          expression: form.parsingExpress, // sql表达式
          cacheDuration: form.time, // 缓存时间
          serviceModelFields: [], // 属性列表
          serviceModelParams: []  // 参数列表
        }
        for(var i=0; i<form.dataModelAttrTable.length; i++){
          paramData.serviceModelFields.push(
            {
              id: form.fieldLoad ? undefined : form.dataModelAttrTable[i].id,
              fieldName: form.dataModelAttrTable[i].fieldName, // 属性名
              fieldDesc: form.dataModelAttrTable[i].fieldDesc, // 描述
              expression: form.dataModelAttrTable[i].expression, // 表达式
              isDerived: form.dataModelAttrTable[i].isDerived, // 是否为新增
            }
          )
        }
        for(var i=0; i<form.dataModelParamTable.length; i++){
          paramData.serviceModelParams.push(
            {
              id: form.fieldLoad ? undefined : form.dataModelParamTable[i].id,
              fieldName: form.dataModelParamTable[i].fieldName, // 属性名
              paramName: form.dataModelParamTable[i].paramName, // 参数名
              paramType: form.dataModelParamTable[i].paramType, // 类型
              // operateType: form.dataModelParamTable[i].operateType, // 操作符
              paramDesc: form.dataModelParamTable[i].paramDesc, // 描述
              defaultValue: form.dataModelParamTable[i].defaultValue, // 默认值
              isRequired: form.dataModelParamTable[i].isRequired, // 是否必填
            }
          )
        }
      }else if(type == 'MQ'){
        paramData = {
          id: form.id,
          parentId: form.parentId===''?undefined:form.parentId,
          modelName: form.modelName,  // 模型名称
          modelDesc: form.des,  // 模型描述
          typeCode: form.typeCode,  // 模型类型
          topicId: form.topicId, // 资产主题id
          statusCode: form.release, // 发布状态
          expressionType: 'topic', // 资产类型
          expression: form.expression, // topicName
          serviceModelFields: [], // 属性列表
          serviceModelParams: []  // 参数列表
        }
        for(var i=0; i<form.abilityFirstModelAttrTable.length; i++){
          paramData.serviceModelFields.push(
            {
              fieldName: form.abilityFirstModelAttrTable[i].fieldName, // 属性名
              fieldDesc: form.abilityFirstModelAttrTable[i].fieldDesc, // 描述
              dataType: form.abilityFirstModelAttrTable[i].dataType, // 类型
              expression: form.abilityFirstModelAttrTable[i].expression, // 表达式
              isDerived: form.abilityFirstModelAttrTable[i].isDerived, // 是否为新增
              id:form.abilityFirstModelAttrTable[i].id
            }
          )
        }
        for(var i=0; i<form.abilityFirstModelParamTable.length; i++){
          paramData.serviceModelParams.push(
            {
              fieldName: form.abilityFirstModelParamTable[i].fieldName, // 属性名
              paramName: form.abilityFirstModelParamTable[i].paramName, // 参数名
              paramType: form.abilityFirstModelParamTable[i].paramType, // 类型
              operateType: form.abilityFirstModelParamTable[i].operateType, // 操作符
              paramDesc: form.abilityFirstModelParamTable[i].paramDesc, // 描述
              defaultValue: form.abilityFirstModelParamTable[i].defaultValue, // 默认值
              isRequired: form.abilityFirstModelParamTable[i].isRequired, // 是否必填
              id: form.abilityFirstModelParamTable[i].id
            }
          )
        }
      }else{
        paramData = {
          id:form.id, //
          parentId: form.parentId===''?undefined:form.parentId,
          modelName: form.modelName,  // 模型名称
          modelDesc: form.des,  // 模型描述
          typeCode: form.typeCode,  // 模型类型
          topicId: form.topicId, // 资产主题id
          statusCode: form.release, // 发布状态
          expressionType: 'url', // 资产类型
          requestMethod: form.requestMethod, // 请求类型
          expression: form.expression, // URL
          serviceModelFields: [], // 属性列表
          serviceModelParams: []  // 参数列表
        }
        for(var i=0; i<form.abilityFirstModelAttrTable.length; i++){
          paramData.serviceModelFields.push(
            {
              fieldName: form.abilityFirstModelAttrTable[i].fieldName, // 属性名
              dataType: form.abilityFirstModelAttrTable[i].dataType, // 类型
              fieldDesc: form.abilityFirstModelAttrTable[i].fieldDesc, // 描述
              expression: form.abilityFirstModelAttrTable[i].expression, // 表达式
              isDerived: form.abilityFirstModelAttrTable[i].isDerived, // 是否为新增
              id:form.abilityFirstModelAttrTable[i].id
            }
          )
        }
        for(var i=0; i<form.abilityFirstModelParamTable.length; i++){
          paramData.serviceModelParams.push(
            {
              fieldName: form.abilityFirstModelParamTable[i].fieldName, // 属性名
              paramName: form.abilityFirstModelParamTable[i].paramName, // 参数名
              paramType: form.abilityFirstModelParamTable[i].paramType, // 类型
              operateType: form.abilityFirstModelParamTable[i].operateType, // 操作符
              paramDesc: form.abilityFirstModelParamTable[i].paramDesc, // 描述
              defaultValue: form.abilityFirstModelParamTable[i].defaultValue, // 默认值
              isRequired: form.abilityFirstModelParamTable[i].isRequired, // 是否必填
              id: form.abilityFirstModelParamTable[i].id
            }
          )
        }
      }
        // 判断必填项
      let fieldName = true // 属性名
      let paramName = true // 参数名
      let paramType = true // 参数类型
      let defaultValue = true // 默认值
      let operateType = true // 操作符
      for(var j=0; j<paramData.serviceModelParams.length; j++){
        // 判断属性名是否填写
        if(paramData.serviceModelParams[j].fieldName === undefined || paramData.serviceModelParams[j].fieldName == ''){
          if(paramData.serviceModelParams[j].operateType != undefined){
            this.$message.error('请填写属性名')
          }else {
            this.$message.error('类型')
          }
          fieldName = false
          break;
        }else {
          //
        }
        // 判断参数名是否必填
        if(paramData.serviceModelParams[j].paramName === undefined || paramData.serviceModelParams[j].paramName == ''){
          this.$message.error('请填写参数名')
          paramName = false
          break;
        }else {
          //
        }
        // 判断参数类型是否填写
        if(paramData.serviceModelParams[j].paramType === undefined || paramData.serviceModelParams[j].paramType == ''){
          this.$message.error('请填写参数类型')
          paramType = false
          break;
        }else {
          //
        }
        // 判断操作符是否填写
        if(paramData.serviceModelParams[j].operateType != undefined){
          if(paramData.serviceModelParams[j].operateType === undefined || paramData.serviceModelParams[j].operateType == ''){
            if(this.serviceModelForm.expressionType !== 'url'){
              this.$message.error('请填写操作符')
              operateType = false
              break;
            }
          }else {
            //
          }
        }

      }
      if(fieldName && paramName && paramType && defaultValue && operateType){
        this.$refs.serviceModelForm.validate(valid => {
          if(valid){
            if(this.testPic !== 'checked'){
              this.$message.error('请先完成正确测试！')
              return
            }
            this.submitButtonDis = true
            request({
              url:'/api/service/model/edit_model',
              method: 'post',
              data: paramData,
            }).then(({data}) => {
              if(data.code == 200){
                this.$message.success('修改成功');
                this.submitButtonDis = false
                this.closeSlide()
                this.fetchAssetFieldTable()
              } else if(data.code === 4010){
                this.$store.dispatch('LogOut').then(() => {
                  location.reload()
                })
              }else{
                this.$message.error(data.message)
                this.submitButtonDis = false
                // this.$message.error(data.message)
              }
            })
          }
        })
      }
    },
    // 导出模型
    exportSource() {
      let formJson = JSON.stringify({
        formData: this.tempFormData,
        row: this.tempRow
      })
      funDownload(formJson, 'model.json')
    },
    batchExport(){
      this.batchExportDialog = true
    },
    batchImport(){
      $('#modelFile').trigger('click')
      console.log('批量导入')
    },
    // 导入模型
    importSource() {
      $('#modelFile').trigger('click')
    },
    importSourceFile(e) {
      const self = this
      let reader = new FileReader()
      reader.readAsText(e.target.files[0])
      reader.onload = function () {
        self.isImportModel = true
        //当读取完成后回调这个函数,然后此时文件的内容存储到了result中,直接操作即可
        let result = JSON.parse(this.result)
        console.log(result)
        // const paramModels = []
        // result.forEach(item => {
        //   paramModels.push(self.submitModels(item.row, item.modelData))
        // })
        // console.log(paramModels)
        request({
          url: '/api/service/model/add_models',
          method: 'post',
          data: result
        }).then(({ data }) => {
          // console.log(data)
          if(data.code===200){
            self.$message.success('导入成功')
            self.fetchAssetFieldTable()
          }
        })
      }
    },
    modelJsonFile(val){
      const models = []
      val.forEach(item => {
        models.push(this.submitModels(item))
      })
      // console.log(models)
      let modelJson = JSON.stringify(models)
      funDownload(modelJson, 'model.json')
    },
    // 批量导入整理模型对象
    submitModels(val){
      const self = this
      self.modelTypeChoose = val.row.typeCode;
      if(val.row.expressionType){
        self.dataModelTypeChoose = val.row.expressionType;
        self.abilityModelSubChoose = val.row.expressionType;
      }else{
        self.dataModelTypeChoose = 'hiveThird'
      }
      let paramData = {}
      // 资产模型的第三种
      // console.log(form, this.isImportModel)
      if(this.modelTypeChoose === 'asset' && this.dataModelTypeChoose == 'hiveThird' ){
        paramData = {
          modelName: val.modelData.modelName,  // 模型名称
          parentId: val.modelData.parentId,
          modelDesc: val.modelData.modelDesc,  // 模型描述
          typeCode: val.modelData.typeCode,  // 模型类型
          topicId: val.modelData.topicId, // 资产主题id
          serviceSourceId: val.modelData.serviceSourceId, // 数据源id
          statusCode: val.modelData.statusCode, // 是否发布
          requestMethod: val.modelData.requestMethod, // 发布类型
          expressionType: val.modelData.expressionType, // 资产类型
          expression: val.modelData.expression, // 数据源表名
          cacheDuration: val.modelData.cacheDuration, // 缓存时间
          serviceModelFields: [], // 属性列表
          serviceModelParams: []  // 参数列表
        }
        // 属性
        for(var i=0; i<val.modelData.serviceModelFields.length; i++){
          paramData.serviceModelFields.push(
            {
              fieldName: val.modelData.serviceModelFields[i].fieldName, // 属性名
              dataType: val.modelData.serviceModelFields[i].dataType, // 类型
              fieldDesc: val.modelData.serviceModelFields[i].fieldDesc, // 描述
              expression: val.modelData.serviceModelFields[i].expression, // 表达式
              isDerived: val.modelData.serviceModelFields[i].isDerived, // 是否为新增
            }
          )
        }
        // 参数
        for(var i=0; i<val.modelData.serviceModelParams.length; i++){
          paramData.serviceModelParams.push(
            {
              fieldName: val.modelData.serviceModelParams[i].fieldName, // 属性名
              paramName: val.modelData.serviceModelParams[i].paramName, // 参数名
              paramType: val.modelData.serviceModelParams[i].paramType, // 类型
              operateType: val.modelData.serviceModelParams[i].operateType, // 操作符
              paramDesc: val.modelData.serviceModelParams[i].paramDesc, // 描述
              defaultValue: val.modelData.serviceModelParams[i].defaultValue, // 默认值
              isRequired: val.modelData.serviceModelParams[i].isRequired, // 是否必填
            }
          )
        }
      }else{
        // 其他模型
        if(val.row.expressionType == 'table'){
          paramData = {
            parentId: val.modelData.parentId,
            modelName: val.modelData.modelName,  // 模型名称
            modelDesc: val.modelData.modelDesc,  // 模型描述
            typeCode: val.modelData.typeCode,  // 模型类型
            topicId: val.modelData.topicId, // 资产主题id
            serviceSourceId: val.modelData.serviceSourceId, // 数据源id
            statusCode: val.modelData.statusCode, // 是否发布
            requestMethod: val.modelData.requestMethod, // 发布类型
            expressionType: val.modelData.expressionType, // 资产类型
            expression: val.modelData.expression, // 数据源表名
            cacheDuration: val.modelData.cacheDuration, // 缓存时间
            serviceModelFields: [], // 属性列表
            serviceModelParams: []  // 参数列表
          }
          for(var i=0; i<val.modelData.serviceModelFields.length; i++){
            paramData.serviceModelFields.push(
              {
                fieldName: val.modelData.serviceModelFields[i].fieldName, // 属性名
                dataType: val.modelData.serviceModelFields[i].dataType, // 类型
                fieldDesc: val.modelData.serviceModelFields[i].fieldDesc, // 描述
                expression: val.modelData.serviceModelFields[i].expression, // 表达式
                isDerived: val.modelData.serviceModelFields[i].isDerived, // 是否为新增
              }
            )
          }
          for(var i=0; i<val.modelData.serviceModelParams.length; i++){
            paramData.serviceModelParams.push(
              {
                fieldName: val.modelData.serviceModelParams[i].fieldName, // 属性名
                paramName: val.modelData.serviceModelParams[i].paramName, // 参数名
                paramType: val.modelData.serviceModelParams[i].paramType, // 类型
                operateType: val.modelData.serviceModelParams[i].operateType, // 操作符
                paramDesc: val.modelData.serviceModelParams[i].paramDesc, // 描述
                defaultValue: val.modelData.serviceModelParams[i].defaultValue, // 默认值
                isRequired: val.modelData.serviceModelParams[i].isRequired, // 是否必填
              }
            )
          }
        }else if(val.row.expressionType == 'sql'){
          paramData = {
            parentId: val.modelData.parentId,
            modelName: val.modelData.modelName,  // 模型名称
            modelDesc: val.modelData.modelDesc,  // 模型描述
            typeCode: val.modelData.typeCode,  // 模型类型
            topicId: val.modelData.topicId, // 资产主题id
            serviceSourceId: val.modelData.serviceSourceId, // 数据源id
            statusCode: val.modelData.statusCode, // 是否发布
            requestMethod: val.modelData.requestMethod, // 发布类型
            expressionType: val.modelData.expressionType, // 资产类型
            expression: val.modelData.expression, // sql表达式
            cacheDuration: val.modelData.cacheDuration, // 缓存时间
            serviceModelFields: [], // 属性列表
            serviceModelParams: []  // 参数列表
          }
          for(var i=0; i<val.modelData.serviceModelFields.length; i++){
            paramData.serviceModelFields.push(
              {
                fieldName: val.modelData.serviceModelFields[i].fieldName, // 属性名
                dataType: val.modelData.serviceModelFields[i].dataType, // 类型
                fieldDesc: val.modelData.serviceModelFields[i].fieldDesc, // 描述
                expression: val.modelData.serviceModelFields[i].expression, // 表达式
                isDerived: val.modelData.serviceModelFields[i].isDerived, // 是否为新增
              }
            )
          }
          for(var i=0; i<val.modelData.serviceModelParams.length; i++){
            paramData.serviceModelParams.push(
              {
                fieldName: val.modelData.serviceModelParams[i].fieldName, // 属性名
                paramName: val.modelData.serviceModelParams[i].paramName, // 参数性名
                paramType: val.modelData.serviceModelParams[i].paramType, // 类型
                // operateType: form.dataModelParamTable[i].operateType, // 操作符（界面先不显示）
                paramDesc: val.modelData.serviceModelParams[i].paramDesc, // 描述
                defaultValue: val.modelData.serviceModelParams[i].defaultValue, // 默认值
                isRequired: val.modelData.serviceModelParams[i].isRequired, // 是否必填
              }
            )
          }
          // 能力模型
        }else if(val.row.expressionType == 'MQ'){
          let topicId
          for(var i=0; i<this.abilityModelBigSubType.length; i++){
            if(this.abilityModelBigSubType[i].code == form.type){
              topicId = this.abilityModelBigSubType[i].id
            }
          }
          paramData = {
            parentId: val.modelData.parentId,
            modelName: val.modelData.modelName,  // 模型名称
            modelDesc: val.modelData.modelDesc,  // 模型描述
            typeCode: val.modelData.typeCode,  // 模型类型
            topicId: val.modelData.topicId, // 资产主题id
            serviceSourceId: val.modelData.serviceSourceId, // 数据源id
            statusCode: val.modelData.statusCode, // 是否发布
            requestMethod: val.modelData.requestMethod, // 发布类型
            expressionType: val.modelData.expressionType, // 资产类型
            expression: val.modelData.expression, // topicName
            serviceModelFields: [], // 属性列表
            serviceModelParams: []  // 参数列表
          }
          for(var i=0; i<val.modelData.serviceModelFields.length; i++){
            paramData.serviceModelFields.push(
              {
                fieldName: val.modelData.serviceModelFields[i].fieldName, // 属性名
                dataType: val.modelData.serviceModelFields[i].dataType, // 类型
                fieldDesc: val.modelData.serviceModelFields[i].fieldDesc, // 描述
                expression: val.modelData.serviceModelFields[i].expression, // 表达式
                isDerived: val.modelData.serviceModelFields[i].isDerived, // 是否为新增
              }
            )
          }
          for(var i=0; i<val.modelData.serviceModelParams.length; i++){
            paramData.serviceModelParams.push(
              {
                fieldName: val.modelData.serviceModelParams[i].fieldName, // 属性名
                paramName: val.modelData.serviceModelParams[i].paramName, // 参数名
                paramType: val.modelData.serviceModelParams[i].paramType, // 类型
                operateType: val.modelData.serviceModelParams[i].operateType, // 操作符
                paramDesc: val.modelData.serviceModelParams[i].paramDesc, // 描述
                defaultValue: val.modelData.serviceModelParams[i].defaultValue, // 默认值
                isRequired: val.modelData.serviceModelParams[i].isRequired, // 是否必填
              }
            )
          }
        }else {
          // 能力模型第二种
          let topicId
          for(var i=0; i<this.abilityModelBigSubType.length; i++){
            if(this.abilityModelBigSubType[i].code == form.type){
              topicId = this.abilityModelBigSubType[i].id
            }
          }
          paramData = {
            modelName: val.modelData.modelName,  // 模型名称
            modelDesc: val.modelData.modelDesc,  // 模型描述
            typeCode: val.modelData.typeCode,  // 模型类型
            topicId: val.modelData.topicId, // 资产主题id
            serviceSourceId: val.modelData.serviceSourceId, // 数据源id
            statusCode: val.modelData.statusCode, // 发布状态
            requestMethod: val.modelData.requestMethod, // 请求类型
            expressionType: val.modelData.expressionType, // 资产类型
            expression: val.modelData.expression, // URL
            // bodyPattern: JSON.stringify(JSON.parse(form.bodyPattern)), //body模板
            serviceModelFields: [], // 属性列表
            serviceModelParams: [],  // 参数列表
          }
          for(var i=0; i<val.modelData.serviceModelFields.length; i++){
            paramData.serviceModelFields.push(
              {
                fieldName: val.modelData.serviceModelFields[i].fieldName, // 属性名
                dataType: val.modelData.serviceModelFields[i].dataType, // 类型
                fieldDesc: val.modelData.serviceModelFields[i].fieldDesc, // 描述
                expression: val.modelData.serviceModelFields[i].expression, // 表达式
                isDerived: val.modelData.serviceModelFields[i].hidden, // 是否为新增
              }
            )
          }
          for(var i=0; i<val.modelData.serviceModelParams.length; i++){
            paramData.serviceModelParams.push(
              {
                fieldName: val.modelData.serviceModelParams[i].fieldName, // 属性名
                paramName: val.modelData.serviceModelParams[i].paramName, // 参数名
                paramType: val.modelData.serviceModelParams[i].paramType, // 类型
                operateType: val.modelData.serviceModelParams[i].operateType, // 操作符
                paramDesc: val.modelData.serviceModelParams[i].paramDesc, // 描述
                defaultValue: val.modelData.serviceModelParams[i].defaultValue, // 默认值
                isRequired: val.modelData.serviceModelParams[i].isRequired, // 是否必填
              }
            )
          }
          // for(var i=0; i<form.abilityFirstModelParamBodyTable.length; i++){
          //   paramData.serviceModelParams.push(
          //     {
          //       fieldName: form.abilityFirstModelParamBodyTable[i].fieldName, // 属性名
          //       paramName: form.abilityFirstModelParamBodyTable[i].paramName, // 参数名
          //       paramType: form.abilityFirstModelParamBodyTable[i].paramType, // 类型
          //       operateType: form.abilityFirstModelParamBodyTable[i].operateType, // 操作符
          //       paramDesc: form.abilityFirstModelParamBodyTable[i].paramDesc, // 描述
          //       defaultValue: form.abilityFirstModelParamBodyTable[i].defaultValue, // 默认值
          //       isRequired: form.abilityFirstModelParamBodyTable[i].isRequired, // 是否必填
          //     }
          //   )
          // }
        }
      }
      return paramData
    },
    // 删除
    deleteSource(id) {
      let idData = {
        id: id
      };
      this.$confirm('确定删除此服务模型？', '提示', {
        type: 'warning'
      }).then(() => {
        request({
          url: '/api/service/model/del_model',
          method: 'post',
          data: idData
          }).then(({data}) => {
            if(data.code == 200){
              this.$message.success({
                message: '删除成功'
              });
              this.fetchAssetFieldTable()
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
    // 查看
    handleView(row) {
      this.tempRow = row
      this.resetTemp()
      this.testPic = 'start'
      this.dialogStatus = 'view'
      this.addDialog = true
      this.modelTypeChoose = row.typeCode;
      if(row.expressionType){
        this.dataModelTypeChoose = row.expressionType;
        this.abilityModelSubChoose = row.expressionType;
      }else{
        this.dataModelTypeChoose = 'hiveThird'
      }
      // 重新发送请求 （因为存在前端的增删改）
      request({
        url: '/api/service/model/get_model',
        method: 'get',
        params: {
          id: row.id
        }
      }).then(({ data }) => {
        if(data.code === 200){
          this.tempFormData = data.data
          if(row.typeCode == "asset"){
            // 资产模型
            this.serviceModelForm = {
              id:data.data.id,
              parentId: data.data.parentId,
              modelName: data.data.modelName, // 模型名称
              typeCode: data.data.typeCode, // 模型类型
              classifi: data.data.dataAssetArea.areaName, // 分类
              theme: data.data.dataAssetTopic.topicName, // 主题
              des: data.data.modelDesc, // 描述
              dataSource: data.data.serviceSource.serviceSourceName,  // 数据源
              tableName: data.data.expression, // 数据源表
              parsingExpress: data.data.expression, // 解析表达式
              release: data.data.statusCode,  // 是否发布
              requestMethod: data.data.requestMethod, // 发布类型
              time: data.data.cacheDuration, // 缓存时间
              type: data.data.expressionType, // 类型
              dataModelTableParamTable: data.data.serviceModelParams,  // 资产-表-参数
              dataModelParamTable: data.data.serviceModelParams, // 资产-查询-参数
              dataModelTableAttrTable: [], // 资产-表-属性
              dataModelAttrTable: [] // 资产-查询-属性
            }
            // 给资产-查询-属性 赋值
            for(var i=0; i<data.data.serviceModelFields.length; i++){
              this.serviceModelForm.dataModelAttrTable.push(
                {
                  id: data.data.serviceModelFields[i].id,
                  fieldName: data.data.serviceModelFields[i].fieldName, // 属性名
                  fieldDesc: data.data.serviceModelFields[i].fieldDesc, // 描述
                  expression: data.data.serviceModelFields[i].expression, // 表达式
                  isDerived: data.data.serviceModelFields[i].isDerived// 是否派生
                }
              )
            }
            // 给资产-表-属性 赋值
            for(var j=0; j<data.data.serviceModelFields.length; j++){
              this.serviceModelForm.dataModelTableAttrTable.push(
                {
                  id: data.data.serviceModelFields[j].id,
                  fieldName: data.data.serviceModelFields[j].fieldName, // 属性名
                  expression: data.data.serviceModelFields[j].expression, // 表达式
                  dataType: data.data.serviceModelFields[j].dataType, // 类型
                  fieldDesc:  data.data.serviceModelFields[j].fieldDesc, // 描述
                  isDerived: data.data.serviceModelFields[j].isDerived // 是否派生
                }
              )
            }

          } else if(row.typeCode == "ability"){
            this.serviceModelForm = {
              id:data.data.id,
              topicId:data.data.topicId,
              modelName: data.data.modelName, // 模型名称
              typeCode: data.data.typeCode, // 模型类型
              classifi: data.data.dataAssetArea.areaName, // 大类
              type: data.data.dataAssetTopic.topicName, // 子类
              // bodyPattern: data.data.bodyPattern, //body模板
              dataSource: data.data.serviceSource.serviceSourceName,  // 数据源
              des: data.data.modelDesc, // 描述
              release: data.data.statusCode,  // 是否发布
              requestMethod: data.data.requestMethod,// 发布类型
              expression: data.data.expression,// topicName
              abilityFirstModelAttrTable: [],
              abilityFirstModelParamTable: []
            }
              // 属性 赋值
            for(var i=0; i<data.data.serviceModelFields.length; i++){
              this.serviceModelForm.abilityFirstModelAttrTable.push(
                {
                  id: data.data.serviceModelFields[i].id,
                  dataType: data.data.serviceModelFields[i].dataType, // 类型
                  fieldName: data.data.serviceModelFields[i].fieldName, // 属性名
                  fieldDesc: data.data.serviceModelFields[i].fieldDesc, // 描述
                  expression: data.data.serviceModelFields[i].expression, // 表达式
                }
              )
            }
            // 参数 赋值
            for(var j=0; j<data.data.serviceModelParams.length; j++){
              // if(data.data.requestMethod === 'POST'){
              //   if(data.data.serviceModelParams[j].fieldName === 'head'){
              //     this.serviceModelForm.abilityFirstModelParamTable.push(
              //       {
              //         id: data.data.serviceModelParams[j].id,
              //         fieldName: data.data.serviceModelParams[j].fieldName,
              //         paramName: data.data.serviceModelParams[j].paramName,
              //         paramType: data.data.serviceModelParams[j].paramType,
              //         paramDesc: data.data.serviceModelParams[j].paramDesc,
              //         operateType: data.data.serviceModelParams[j].operateType,
              //         defaultValue: data.data.serviceModelParams[j].defaultValue,
              //         isRequired: data.data.serviceModelParams[j].isRequired,
              //       }
              //     )
              //   }else if(data.data.serviceModelParams[j].fieldName === 'body'){
              //     this.serviceModelForm.abilityFirstModelParamBodyTable = []
              //     this.serviceModelForm.abilityFirstModelParamBodyTable.push(
              //       {
              //         id: data.data.serviceModelParams[j].id,
              //         fieldName: data.data.serviceModelParams[j].fieldName,
              //         paramName: data.data.serviceModelParams[j].paramName,
              //         paramType: data.data.serviceModelParams[j].paramType,
              //         paramDesc: data.data.serviceModelParams[j].paramDesc,
              //         operateType: data.data.serviceModelParams[j].operateType,
              //         defaultValue: data.data.serviceModelParams[j].defaultValue,
              //         isRequired: data.data.serviceModelParams[j].isRequired,
              //       }
              //     )
              //   }
              // }else {
                this.serviceModelForm.abilityFirstModelParamTable.push(
                  {
                    id: data.data.serviceModelParams[j].id,
                    fieldName: data.data.serviceModelParams[j].fieldName,
                    paramName: data.data.serviceModelParams[j].paramName,
                    paramType: data.data.serviceModelParams[j].paramType,
                    paramDesc: data.data.serviceModelParams[j].paramDesc,
                    operateType: data.data.serviceModelParams[j].operateType,
                    defaultValue: data.data.serviceModelParams[j].defaultValue,
                    isRequired: data.data.serviceModelParams[j].isRequired,
                  }
                )
              // }
            }
          }
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{
          this.$message.error(data.message)
        }
      })

    },
    /******** 编辑与删除end *********/
    /******** 授权end *********/
    // 重置
    resetTemp(){
      this.serviceModelForm = {
        modelName: undefined, // 模型名称
        typeCode: undefined, // 模型类型
        // driveType: undefined,  // 驱动类型
        classifi: undefined, // 分类
        theme: undefined, // 主题
        des: undefined, // 描述
        dataSource: undefined,  // 数据源
        tableName: undefined, // 数据源表
        parsingExpress: undefined, // 解析表达式
        release: 'published',  // 是否发布
        requestMethod: 'POST', // 发布类型
        topicName: undefined, // topicName
        time: undefined, // 缓存时间
        type: undefined, // 类型
        apiSource: undefined, // API数据源
        url: undefined, // URL
        dataModelTableAttrTable: [], // 资产-表-属性
        dataModelTableParamTable: [],  // 资产-表-参数
        dataModelAttrTable: [], // 资产-查询-属性
        dataModelParamTable: [],  // 资产-查询-参数
        // 能力模型-第一种模型-属性(参数)列表
        abilityFirstModelAttrTable: [],
        abilityFirstModelParamTable: [],
        // 能力模型-第二种模型-属性（参数）列表
        abilitySecondModelAttrTable: [],
        abilitySecondModelParamTable: []
      }
      this.modelTypeChoose = ''; // 模型类型
      this.dataModelTypeChoose = '';
      this.releaseStute = 'published'; // 发布类型
      this.assetFieldList = [];  // 资产领域数据
      this.assetThemeList = [];  // 资产主题数据
      this.dataSourceList = [];  // 数据源数据
      this.serviceTableList = []; // 数据源表
      this.serviceTableListCope = []; // 复制品数据源表
      this.submitButtonDis = false;
    },
    // 重置测试弹框
    resetTest() {
      this.serviceTestForm.testForm = []
    },
    // 表头加星
    headerStar(h,{column}){
      return h('span',[
          h(
            'span', {
              attrs: {
              class: 'headRed'
            }},'*'),
          h('span', {
            attrs: {
              class: 'headTitle'
            }
          },column.label)
        ]
      )
    },
    // 过滤数据源
    filterServiceSource(val){
      console.log(val)
      console.log('dataSource',this.serviceModelForm.dataSource)
      let dataParam = {
        id: this.serviceModelForm.dataSource,
        name: val,
      }
      request({
        url: '/api/servicesource/get_servicesource_tables',
        method: 'get',
        params: dataParam
      }).then(({ data }) => {
        if(data.code === 200){
          this.serviceTableList = data.data
          console.log('list', data.data)
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{

        }
      })
    },
    offTest(val){
      console.log(val)
      this.popoverVisible = false
      this.serviceModelForm.tableName = val
      this.tableNameChange(this.serviceModelForm.dataSource, val)
    },
    test(){
      const self = this
      $(document).on('keyup',function(ev){
        // console.log(ev.keyCode === 13, $('#test-select').is(':focus'))
        // console.log($('#test-select').val())
        if(ev.keyCode === 13&&$('#test-select').is(':focus')){
          // 更改选项调用接口
          let dataParam = {
            id: self.serviceModelForm.dataSource,
            name: $('#test-select').val(),
          }
          request({
            url: '/api/servicesource/get_servicesource_tables',
            method: 'get',
            params: dataParam
          }).then(({ data }) => {
            self.popoverVisible = true
            if(data.code === 200){
              self.serviceTableList = data.data
            }else if(data.code === 4010){
              self.$store.dispatch('LogOut').then(() => {
                location.reload()
              })
            }else{

            }
          })
        }
        if(ev.keyCode === 13&&$('.do-search-input').is(':focus')){
          self.fetchAssetFieldTable()
        }
      });
    },
    /* filterServiceSource(val){
      console.log('val', val)
      if(val){
        // console.log('val', val)
        let pattern = new RegExp("^"+val)
        this.serviceTableList =  this.serviceTableList.filter((item) => {
          if (this.regularCheckHeader(pattern, item)) {
            return true
          }
        })
      }else{
        this.serviceTableList = this.serviceTableListCope
      }
    }, */
    // 正则检验函数
    regularCheckHeader(pa,st){
      return pa.test(st)
    },
    // el-table的背景边框改变函数
    backgroundColorBorderChange({row, rowIndex}){
      return 'table-header-border-bg'
    },
    // el-table的背景边框改变函数
    titleBgColor({row, rowIndex}){
      return 'title-bg-color'
    }
  },
  filters: {
    stateTransition: function(value){
      if(value){
        if(value == 'initialized'){
          return '待发布'
        }else if(value == 'published'){
          return '已发布'
        }
      }
    },
    modelTransition: function(value){
      if(value){
        if(value == 'asset'){
          return '资产模型'
        }else if(value == 'topic'){
          return '消息模型'
        }else if (value == 'url'){
          return 'api模型'
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
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
.add-dialog-form {
  .form-header {
    padding: 0 16px;
  }
}
.table-header {
  background-color: #ddd;
}
.form-body {
  padding: 6px 30px;
  overflow:auto;
  .load-btn{
    width: 18px;
    height: 18px;
    position: absolute;
    top: 50%;
    margin-top: -30px;
    right: -22px;
    cursor: pointer;
  }
  .sql-load-btn{
    width: 18px;
    height: 18px;
    position: relative;
    margin-right: 20px;
    top: 4px;
    cursor: pointer;
  }
}
.api-dialog-body {
  padding: 14px 15px;
}
.massage-dialog-body {
  padding-top: 30px;
}
.data-auth {
  border: 1px solid #f5f7fa;
  width: 30%;
  .auth-title{
    height: 40px;
    line-height: 40px;
    padding-left: 15px;
    background: #f5f7fa;
  }
  div:nth-child(2){
    padding: 15px 12px;
  }
  .has-search {
    padding: 30px 18px;
    li {
      height: 30px;
      line-height: 30px;
    }
  }
  .searching-list {
    z-index: 100;
    position: absolute;
    top: 162px;
    left: 32px;
    background: #fff;
    padding: 10px 2px;
    border: 1px solid #e9e9e9;
    box-shadow: 0px 2px 2px #ddd;
    li {
      width: 160px;
      height: 30px;
      line-height: 30px;
      padding-left: 6px;
      border-bottom: 1px solid #e9e9e9;
      &:hover{
        background: #f5f6f7;
      }
    }
  }
}
.service-table-list{
  width: 100%;
  max-height: 200px;
  overflow: hidden;
  overflow-y: auto;
  li{
    cursor: pointer;
    &:hover{
      color: #4562fc
    }
  }
}
.pos-tl {
  position: relative;
  top: -46px;
  left: 10px;
}
.search-in{
  width: 200px;
  margin-right: 6px;
  margin-top: 6px;
}
.user-span {
  font-size: 14px;
  color: #a1a6a7;
}
.test-body {
  padding: 6px 24px;
}
.test-foot {
  padding: 4px 60px 10px;
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
.headRed {
  color: red;
  background: red;
}
</style>
<style>
.table-header-border-bg > th{
  background-color: #f5f6fa !important;
}
.table-header-border-bg > th >.cell{
  font-size: 14px;
  color: #303133;
  font-weight: normal;
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
