<!--资产主题管理界面-->
<template>
  <div>
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">资产主题管理</h2>
        </div>
        <!-- <div class="search fr">
          <input type="text" style="padding-left: 15px; width: 200px; outline: none; border: 0; font-size: 12px; display: inline-block; height: 100%; float: left;" v-model="query.name" placeholder="请输入关键字"></input>
          <svg-icon icon-class="search"></svg-icon>
        </div> -->
        <div class="fr">
          <el-button class="add-btn" type="primary" @click="handleCreate" :disabled="!operatePermission"><i>+</i>新增</el-button>
        </div>
      </div>
      <div class="area-type">
        <!-- <a href="javascript:;">
          <svg-icon icon-class="zhaocai" style="width: 22px; height: 22px"></svg-icon>
          <span>主题1</span>
        </a>
        <a href="javascript:;">
          <svg-icon icon-class="engineering" style="width: 22px; height: 22px"></svg-icon>
          <span>主题2</span>
        </a>
        <a href="javascript:;">
          <svg-icon icon-class="business" style="width: 22px; height: 22px;transform: scale(1.5);"></svg-icon>
          <span>主题3</span>
        </a>
        <a href="javascript:;">
          <svg-icon icon-class="house" style="width: 22px; height: 22px"></svg-icon>
          <span>主题4</span>
        </a>
        <a href="javascript:;">
          <svg-icon icon-class="officeBuilding" style="width: 22px; height: 22px"></svg-icon>
          <span>主题5</span>
        </a> -->
        <a href="javascript:;">
          <svg-icon icon-class="dataSharing" style="width: 22px; height: 22px"></svg-icon>
          <span>数据共享</span>
        </a>
        <a href="javascript:;">
          <svg-icon icon-class="tecPlatForm" style="width: 22px; height: 22px"></svg-icon>
          <span>技术平台</span>
        </a>
        <a href="javascript:;">
          <svg-icon icon-class="otherArea" style="width: 22px; height: 22px"></svg-icon>
          <span>其他领域</span>
        </a>
      </div>
      <!-- 精确查询 -->
      <div class="clearfix mt-15" v-show="moreSearch == true">
        <el-form ref="form" :model="searchForm">
          <el-form-item label="资产领域" style="width: 226px;" class="fl" label-width="80px">
            <el-input v-model="searchForm.areaName" size="small"></el-input>
          </el-form-item>
          <el-form-item label="资产主题" style="width: 226px;" class="fl" label-width="80px">
            <el-input v-model="searchForm.topicName" size="small"></el-input>
          </el-form-item>
          <el-button type="primary" size="mini" class="fl" style="margin: 6px 0 0 10px" @click="fetchAssetThemeTable">查询</el-button>
        </el-form>
      </div>
    </nav>
    <section class="components-container clearfix section-bod">
      <!-- <el-table
        ref="multipleTable"
        :data="tableData.data"
        tooltip-effect="dark"
        style="width: 100%"
        size="small"
        :header-row-class-name="titleBgColor">
        <el-table-column prop="dataAssetArea.areaName" label="资产领域" show-overflow-tooltip>
        </el-table-column>
        <el-table-column prop="topicName" label="资产主题名称" show-overflow-tooltip width="200">
        </el-table-column>
        <el-table-column prop="topicDesc" label="描述" show-overflow-tooltip>
        </el-table-column>
        <el-table-column prop="updateBy" label="更新用户" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="更新时间" show-overflow-tooltip>
          <template slot-scope="scope">{{ scope.row.updateTime }}</template>
        </el-table-column>
        <el-table-column label="操作" show-overflow-tooltip width="150">
          <template slot-scope="scope">
            <a title="编辑" @click="handleUpdate(scope.row)"><svg-icon icon-class="editor" style="width: 20px; height: 20px"></svg-icon></a>
            <a title="查看" @click="handleView(scope.row)"><svg-icon icon-class="eye" style="width: 20px; height: 20px"></svg-icon></a>
            <a title="删除" @click="deleteSource(scope.row.id)"><svg-icon icon-class="delete" style="width: 20px; height: 20px"></svg-icon></a>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        @current-change="(current)=>{searchForm.pageNo = current;this.fetchAssetThemeTable()}"
        :page-size="10"
        :current-page="1"
        layout="prev, pager, next"
        :total="tableData.page"
        class="fr mt-15">
      </el-pagination> -->
      <!-- <text-card
        :cardData="tableData.data"
        :searchForm="searchForm"
        :pageNum="tableData.page"
        :showFields="showFields"
        @add="handleCreate"
        @browse="handleView"
        @edit="handleUpdate"
        @delete="deleteSource"
        @current-change="currentChange"
        cardTitle="资产主题名称"
        ref="card">
      </text-card> -->
      <cr-loading v-show="loading"></cr-loading>
      <list-card
        v-show="!loading"
        :listData="tableData.data"
        :fieldsData="fieldsData"
        :operatePermission="operatePermission"
        cardWidth="48%"
        @edit="handleUpdate"
        @delete="deleteSource"
        @browse="handleView"
      ></list-card>
      <el-pagination
        background
        @current-change="(current)=>{this.loading=true;this.tableData.data=[];searchForm.pageNo = current;this.fetchAssetThemeTable()}"
        :page-size="parseInt(searchForm.pageSize)"
        :current-page="parseInt(searchForm.pageNo)"
        layout="prev, pager, next"
        :total="tableData.page"
        class="fr mt-10">
      </el-pagination>
    </section>
    <!--新增与编辑-->
    <!-- <el-dialog
      :title="textMap[dialogStatus]"
      :visible.sync="addDialog"
      width="50%"
      @close='cancelSource(themeForm)'>
      <el-form :model="themeForm" ref="themeForm" :rules="themeRule" label-width="120px" style="padding: 10px 30px;">
        <el-form-item label="资产领域">
          <el-select v-model="themeForm.areaId" :disabled="dialogStatus == 'view'">
            <el-option v-for="(item, index) in fieldName" :key="index" :label="item.areaName" :value="item.id" ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="资产主题名称:" prop="topicName"  class="mt-15" required>
          <el-input v-model="themeForm.topicName" :disabled="dialogStatus == 'view'"></el-input>
        </el-form-item>
        <el-form-item label="描述:" class="mt-15">
          <el-input type="textarea" v-model="themeForm.topicDesc" :disabled="dialogStatus == 'view'"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button v-if="dialogStatus=='create'" type="primary" size="mini" @click="creatTheme(themeForm)">保存</el-button>
        <el-button v-else-if="dialogStatus=='update'" type="primary" size="mini" @click="editorTheme(themeForm)">保存</el-button>
        <el-button size="mini" @click="cancelSource(themeForm)">取消</el-button>
      </span>
    </el-dialog> -->
    <slide-box
      slideWidth="450px"
      :slideShow="addDialog"
      @close="slideClose"
      :title="textMap[dialogStatus]"
      ref="slideBox">
      <el-form slot="slide-content" :model="themeForm" ref="themeForm" :rules="themeRule" label-width="120px" style="padding: 10px 30px;">
        <el-form-item label="资产领域" prop="areaId">
          <el-select size="medium" v-model="themeForm.areaId" :disabled="dialogStatus == 'view'">
            <el-option v-for="(item, index) in fieldName" :key="index" :label="item.areaName" :value="item.id" ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="资产主题名称:" prop="topicName"  class="mt-15" required>
          <el-input size="medium" v-model="themeForm.topicName" :disabled="dialogStatus == 'view'"></el-input>
        </el-form-item>
        <el-form-item label="主题负责人:" prop="topicName"  class="mt-15" required>
          <el-select v-model="themeForm.owner" :disabled="dialogStatus == 'view'" style="width:100%">
            <el-option v-for="(item,index) in appUserOptions" :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="描述:" class="mt-15">
          <el-input size="medium" type="textarea" v-model="themeForm.topicDesc" :disabled="dialogStatus == 'view'"></el-input>
        </el-form-item>
      </el-form>
      <span slot="slide-footer">
        <el-button v-if="dialogStatus=='create'" :disabled="submitBtnDisabled" type="primary" size="mini" @click="creatTheme(themeForm)">保存</el-button>
        <el-button v-else-if="dialogStatus=='update'" :disabled="submitBtnDisabled" type="primary" size="mini" @click="editorTheme(themeForm)">保存</el-button>
        <el-button size="mini" @click="closeSlide">取消</el-button>
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
  components:{
    ListCard,
    SlideBox,
    CrLoading
  },
  data() {
    return {
      operatePermission: true,
      submitBtnDisabled: false,
      loading: true,
      appUserOptions: [],
      /******** 表格搜索数据start ********/
      query: {
        name: ''
      },
      showFields: {
        name: 'topicName',
        desc: 'topicDesc'
      },
      fieldsData:[
        {
          propsTitle: '资产主题名称',
          field: 'topicName'
        },
        {
          propsTitle: '描述',
          field: 'topicDesc'
        }
      ],
       // 更多查询
      moreSearch: false,
      searchForm: {
        areaName: null,
        topicName: null,
        pageNo: '1',
        pageSize: '10'
      },
      /******** 表格搜索数据end ********/

      /******** table列表数据start ********/
      tableData: {
        data: [],
        page: null
      },
      themeRule: {
        areaId: [{ required: true, message: '请选择资产领域'}],
        topicName: [{ required: true, message: '请输入资产主题名称'}]
      },
      fieldName: [],  // 资产领域名称
      /******** table列表数据end ********/
      /******** 新增编辑start ********/
      /* 新增与编辑 */
      addDialog: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新增',
        view: '详细信息'
      },
        // 新增表单
      themeForm: {
        areaId: '',
        topicName: '',
        topicDesc: ''
      }
      /******** 新增编辑end ********/
    }
  },
  mounted() {
    this.initAreaClick()
    this.getFieldName()
    this.fetchAssetThemeTable()
    // this.fetchAppUsers()
    this.$store.getters.userInfo.menu.forEach(item => {
      item.children.forEach(subItem => {
        if(subItem.name === '资产主题管理'){
          this.operatePermission = subItem.operationIds.indexOf('3') !== -1
          console.log(subItem.name+'页面操作权限',this.operatePermission)
        }
      })
    })
  },
  methods: {
    initAreaClick(){
      const self = this;
      $('.area-type a').click(function(){
        // console.log($(this))
        if($(this).hasClass('active')){
          self.searchForm.areaName = '';
        }else{
          self.searchForm.areaName = $(this).find('span').text();
        }
        $(this).toggleClass('active').siblings().removeClass('active');
        self.fetchAssetThemeTable()
      })
    },
    // 关闭滑框
    closeSlide(){
      this.$refs.slideBox.close()
    },
    slideClose(){
      this.appUserOptions = []
      this.addDialog = false

    },
    currentChange(current) {
      this.searchForm.pageNo = current;
      this.fetchAssetThemeTable()
    },
    /******** 资产主题start *********/
    // 获取资产主题管理列表
    fetchAssetThemeTable() {
      // this.searchForm.pageSize = this.$store.state.cssComputed.countNum +''
      request({
        url: '/api/dataasset/topic/query_topic',
        method: 'get',
        params: this.searchForm
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
    // 获取资产领域
    getFieldName(){
      request({
        url: '/api/dataasset/area/query_area',
        method: 'get',
      }).then(({ data }) => {
        if(data.code === 200){
          this.fieldName = data.data.list;
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
           location.reload()
         })
        }else{

        }
      })
    },
    /******** 资产主题start *********/

    /******** 新增、编辑与查看start *********/
    // 新增弹框
    handleCreate() {
      this.fetchAppUsers()
      this.resetTemp()
      this.cancelSource()
      this.dialogStatus = 'create'
      this.addDialog = true
    },
    // 新增确定函数
    creatTheme(form) {
      this.$refs.themeForm.validate((valid) => {
        if(valid){
          this.submitBtnDisabled = true;
          this.loading = true;
          request({
            url:'/api/dataasset/topic/add_topic',
            method: 'post',
            data: form,
          }).then(({data}) => {
            if(data.code == 200){
              this.$message.success('新增成功');
              this.closeSlide()
              this.fetchAssetThemeTable();
            }else if(data.code === 4010){
              this.$store.dispatch('LogOut').then(() => {
                location.reload()
              })
            }else{
              this.$message.error(data.message)
            }
          })
          this.submitBtnDisabled = false
        }else{
          return false;
        }
      })
    },
    // 编辑弹框
    handleUpdate(row){
      this.fetchAppUsers()
      // console.log(row)
      this.addDialog = true;
      this.dialogStatus = 'update';
      // console.log(this.appUserOptions,row)
      this.themeForm = {
        id: row.id,
        areaId: row.areaId,
        topicName: row.topicName,
        topicDesc: row.topicDesc,
        owner: row.owner
      }
    },
    // 编辑确定函数
    editorTheme(row) {
      this.$refs.themeForm.validate(valid => {
        if(valid){
          // console.log(this.appUserOptions.find(item => item.label === row.owner))
          // row.owner = this.appUserOptions.find(item => item.label === row.owner).value
          this.submitBtnDisabled = true;
          this.loading = true;
          request({
            url:'/api/dataasset/topic/edit_topic',
            method: 'post',
            data: row,
          }).then(({data}) => {
            if(data.code == 200){
              this.$message.success('更新成功');
              this.closeSlide()
              this.fetchAssetThemeTable();
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
    },
    // 查看
    handleView(row){
      this.fetchAppUsers()
      // console.log(this.appUserOptions.find(item => item.value === row.owner).label)
      this.dialogStatus = 'view'
      this.addDialog = true
      this.themeForm = {
        areaId: row.areaId,
        topicName: row.topicName,
        topicDesc: row.topicDesc,
        owner: row.owner
      }
    },
    /******** 新增、编辑与查看end *********/

    /******** 删除、其他start *********/
    // 重置
    resetTemp(){

      // this.appUserOptions = []
      this.themeForm = {
        areaId: '',
        owner: '',
        topicName: '',
        topicDesc: ''
      }
    },
    //app负责人列表
    fetchAppUsers(){
      request({
        url: '/api/auth/user/get_role_user?roleCode=data_engineer',
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
    // 取消新增或编辑
    cancelSource(){
      this.$refs.themeForm.resetFields()
    },
    // 删除
    deleteSource(item) {
      let idData = [];
      idData.push(item);
      this.$confirm('确定删除此主题？', '提示', {
        type: 'warning'
      }).then(() => {
        request({
          url: '/api/dataasset/topic/del_topic',
          method: 'post',
          data: idData
          }).then(({data}) => {
            if(data.code == 200){
              this.$message.success({
                message: '删除成功'
              });
              this.fetchAssetThemeTable();
            }else if(data.code === 4010){
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
    /******** 删除、其他end *********/
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
  .area-type{
    width: 100%;
    line-height: 45px;
    margin-top: 20px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    a {
      text-align: center;
      border: 1px solid #999;
      border-radius: 5px;
      color: #999;
      display:inline-block;
      height: 40px;
      width: 140px;
      margin-right: 10px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      &:last-child{
        margin-right: 0;
      }
      span{
        position: relative;
        top: -3px;
        font-size: 16px;
      }
    }
    .active{
      border-color: #4562fc;
      background: #fff;
      color: #4562fc
    }
  }
  .titile {
    color: #444;
    font-size: 23px;
  }
}
</style>