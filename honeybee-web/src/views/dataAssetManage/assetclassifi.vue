<!--资产领域管理界面-->
<template>
  <div>
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">资产领域管理</h2>
        </div>
        <!-- <div class="search fr">
          <input type="text" style="padding-left: 15px; width: 200px; outline: none; border: 0; font-size: 12px; display: inline-block; height: 100%; float: left;" v-model="query.name" placeholder="请输入关键字"></input>
          <svg-icon icon-class="search"></svg-icon>
        </div> -->
        <!-- <div class="search fr">
          <input type="text" style="padding-left: 15px; width: 200px; outline: none; border: 0; font-size: 12px; display: inline-block; height: 100%; float: left;" v-model="query.name" placeholder="请输入关键字"></input>
          <svg-icon icon-class="search"></svg-icon>
        </div> -->
        <div class="fr">
          <el-button class="add-btn" type="primary" @click="handleCreate" :disabled="!operatePermission"><i>+</i>新增</el-button>
        </div>
      </div>
      <!-- 精确查询 -->
      <!-- <div class="clearfix mt-15" v-show="moreSearch == true">
        <el-form ref="form" :model="searchForm">
          <el-form-item label="资产领域" style="width: 226px;" class="fl" label-width="80px">
            <el-input v-model="searchForm.areaName" size="small"></el-input>
          </el-form-item>
          <el-button type="primary" class="fl" size="mini" style="margin: 6px 0 0 10px" @click="fetchAssetFieldTable">查询</el-button>
        </el-form>
      </div> -->
    </nav>
    <section class="components-container clearfix section-bod">
      <!-- <el-table
        ref="multipleTable"
        :data="tableData.data"
        tooltip-effect="dark"
        style="width: 100%"
        size="small"
        :header-row-class-name="titleBgColor">
        <el-table-column prop="areaName" label="资产领域名称" show-overflow-tooltip width="200">
        </el-table-column>
        <el-table-column prop="areaDesc" label="描述" show-overflow-tooltip width="200">
        </el-table-column>
        <el-table-column prop="updateBy" label="更新用户" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="更新时间" show-overflow-tooltip>
          <template slot-scope="scope">{{ scope.row.updateTime }}</template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <a title="编辑" @click="handleUpdate(scope.row)"><svg-icon icon-class="editor" style="width: 20px; height: 20px"></svg-icon></a>
            <a title="查看" @click="handleView(scope.row)"><svg-icon icon-class="eye" style="width: 20px; height: 20px"></svg-icon></a>
            <a title="删除" @click="deleteSource(scope.row.id)"><svg-icon icon-class="delete" style="width: 20px; height: 20px"></svg-icon></a>
          </template>
        </el-table-column>
      </el-table> -->
      <!-- <list-card
        :listData="tableData.data"
        :fieldsData="fieldsData"
        cardWidth="32%"
        cardImg="2"
        @edit="handleUpdate"
        @delete="deleteSource"
        @browse="handleView"
      ></list-card>
      <el-pagination
        background
        @current-change="(current)=>{}"
        :page-size="parseInt(searchForm.pageSize)"
        :current-page="parseInt(searchForm.pageNo)"
        layout="prev, pager, next"
        :total="tableData.page">
      </el-pagination> -->
      <!-- <text-card
        :cardData="tableData.data"
        :searchForm="searchForm"
        :pageNum="tableData.page"
        :showFields="showFields"
        @browse="handleView"
        @edit="handleUpdate"
        @delete="deleteSource"
        cardTitle="资产领域名称"
        @current-change="currentChange"
        ref="card">
      </text-card> -->
      <cr-loading v-show="loading"></cr-loading>
      <list-card
        :operatePermission="operatePermission"
        v-show="!loading"
        :listData="tableData.data"
        :fieldsData="fieldsData"
        cardWidth="48%"
        @edit="handleUpdate"
        @delete="deleteSource"
        @browse="handleView"
      ></list-card>
      <el-pagination
        background
        @current-change="(current)=>{this.loading=true;this.tableData.data=[];searchForm.pageNo = current;this.fetchAssetFieldTable()}"
        :page-size="parseInt(searchForm.pageSize)"
        :current-page="parseInt(searchForm.pageNo)"
        layout="prev, pager, next"
        :total="tableData.page"
        class="fr mt-10">
      </el-pagination>
    </section>
    <!--<div class="bg">
    </div>-->
    <!--新增与编辑-->
    <!-- <el-dialog
      :title="textMap[dialogStatus]"
      :visible.sync="addDialog"
      width="50%"
      @close='cancelSource(fieldForm)'>
      <el-form :model="fieldForm" ref="fieldForm" :rules="fieldRule" label-width="120px" style="padding: 10px 30px;">
        <el-form-item label="资产领域名称:" prop="areaName" required >
          <el-input v-model="fieldForm.areaName" :disabled="dialogStatus == 'view'"></el-input>
        </el-form-item>
        <el-form-item label="描述:" prop="areaDesc" class="mt-15">
          <el-input type="textarea" v-model="fieldForm.areaDesc" :disabled="dialogStatus == 'view'"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button v-if="dialogStatus=='create'" type="primary" size="mini" @click="creatField(fieldForm)">保存</el-button>
        <el-button v-else-if="dialogStatus=='update'" type="primary" size="mini" @click="editorField(fieldForm)">保存</el-button>
        <el-button size="mini" @click="cancelSource(fieldForm)">取消</el-button>
      </span>
    </el-dialog> -->
    <slide-box
      slideWidth="450px"
      :slideShow="addDialog"
      @close="slideClose"
      :title="textMap[dialogStatus]"
      ref="slideBox">
      <el-form slot="slide-content" :model="fieldForm" ref="fieldForm" :rules="fieldRule" label-width="120px" style="padding: 10px 30px;">
        <el-form-item label="资产领域名称:" prop="areaName" required >
          <el-input size="medium" v-model="fieldForm.areaName" :disabled="dialogStatus == 'view'"></el-input>
        </el-form-item>
        <el-form-item label="资产领域图标:">
          <el-select size="medium"  v-model="fieldForm.picPath" @change="imgChange(fieldForm.picPath)" :disabled="dialogStatus == 'view'">
            <el-option v-for="(item, index) in imgTitle" :label="item.name" :key="index" :value="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item style="padding-top: 8px;">
          <img v-if="imgSrc !== ''" :src="require('../../assets/tdspic/'+imgSrc)" alt="">
        </el-form-item>
        <el-form-item label="描述:" prop="areaDesc">
          <el-input size="medium" type="textarea" v-model="fieldForm.areaDesc" :disabled="dialogStatus == 'view'"></el-input>
        </el-form-item>
      </el-form>
      <span slot="slide-footer">
        <el-button v-if="dialogStatus=='create'" :disabled="submitBtnDisabled" type="primary" size="mini" @click="creatField(fieldForm)">保存</el-button>
        <el-button v-else-if="dialogStatus=='update'" :disabled="submitBtnDisabled" type="primary" size="mini" @click="editorField(fieldForm)">保存</el-button>
        <el-button size="mini" @click="closeSlide">取消</el-button>
      </span>
    </slide-box>
  </div>
</template>

<script>
import request from '@/utils/request'
import ListCard from '@/components/ListCard'
import SlideBox from '@/components/SlideBox'
import TextCard from '@/components/TextCard'
import CrLoading from '@/components/CrLoading'
export default {
  components:{
    ListCard,
    SlideBox,
    TextCard,
    CrLoading
  },
  data() {
    return {
      operatePermission: true,
      submitBtnDisabled: false,
      loading: true,
      loadingTime: 200,
      /******** 表格搜索数据start ********/
      query: {
        name: ''
      },
      showFields:{
        name: 'areaName',
        desc: 'areaDesc'
      },
      fieldsData:[
        {
          propsTitle: '资产领域名称',
          field: 'areaName'
        },
        {
          propsTitle: '描述',
          field: 'areaDesc'
        }
      ],
      // 更多查询
      moreSearch: false,
      searchForm: {
        areaName: null,
        pageNo: '1',
        pageSize: '10'
      },
      /******** 表格搜索数据end ********/

      /******** table列表数据start ********/
      tableData: {
        data: [],
        page: null
      },
      /******** table列表数据end ********/

      /******** 新增编辑start ********/
      /* 编辑与新增的分别 */
      addDialog: false,
      dialogStatus: '',
      imgTitle: [
        {
          name: 'business.png',
          id: '1'
        },{
          name: 'dataSharing.png',
          id: '1'
        },{
          name: 'house.png',
          id: '1'
        },{
          name: 'officeBuilding.png',
          id: '1'
        },{
          name: 'otherArea.png',
          id: '1'
        },{
          name: 'tecPlatForm.png',
          id: '1'
        },{
          name: 'zhaocai.png',
          id: '1'
        },{
          name: 'engineering.png',
          id: '1'
        }
      ],
      imgSrc: '',
      textMap: {
        update: '编辑',
        create: '新增',
        view: '查看'
      },
      // 表格必填项
      fieldRule: {
        areaName: [{ required: true, message: '请输入资产领域名称' }],
      },
        // 新增表单
      fieldForm: {
        areaName: '',
        areaDesc: ''
      },
      /******** 新增编辑end ********/
    }
  },
  mounted() {
    this.fetchAssetFieldTable()
    this.$store.getters.userInfo.menu.forEach(item => {
      item.children.forEach(subItem => {
        if(subItem.name === '资产领域管理'){
          this.operatePermission = subItem.operationIds.indexOf('3') !== -1
          console.log(subItem.name+'页面操作权限',this.operatePermission)
        }
      })
    })
  },
  methods: {
    // 关闭滑框
    closeSlide(){
      this.$refs.slideBox.close()
    },
    slideClose(){
      this.addDialog = false
    },
    currentChange(current) {
      this.searchForm.pageNo = current;
      this.fetchAssetFieldTable()
    },
    /******** 资产领域start *********/
    // 获取资产领域管理列表
    fetchAssetFieldTable(val) {
      console.log(val)
      if(val === '3'){
        this.loading = false
        this.loadingTime = 0
      }
      // this.searchForm.pageSize = this.$store.state.cssComputed.countNum + ''
      request({
        url: '/api/dataasset/area/query_area',
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
        },this.loadingTime)
      })
    },
    /******** 资产领域start *********/
    imgChange(val){
      this.imgSrc = val
    },
    /******** 新增、编辑与查看start *********/
    // 新增弹框
    handleCreate() {
      this.resetTemp()
      this.cancelSource()
      this.dialogStatus = 'create'
      this.addDialog = true
    },
    // 新增确定函数
    creatField(form) {

      this.$refs.fieldForm.validate((valid) => {
        if(valid){
          this.submitBtnDisabled = true;
          this.loading = true;
          request({
            url:'/api/dataasset/area/add_area',
            method: 'post',
            data: form,
          }).then(({data}) => {
            if(data.code == 200){
              this.$message.success('新增成功');
              this.closeSlide()
              this.fetchAssetFieldTable();
            }else if(data.code === 4010){
              this.$store.dispatch('LogOut').then(() => {
                location.reload()
              })
            }else{
              this.$message.error(data.message)
            }
            this.submitBtnDisabled = false
          })
        }else{
          return false
        }
      })
    },
    // 编辑弹框
    handleUpdate(row){
      this.addDialog = true;
      this.dialogStatus = 'update';
      this.fieldForm = {
        id: row.id,
        areaName: row.areaName,
        areaDesc: row.areaDesc,
        picPath: row.picPath
      }
      this.imgSrc = row.picPath
    },
    // 编辑确定函数
    editorField(row) {
      this.$refs.fieldForm.validate(valid => {
        if(valid){
          this.submitBtnDisabled = true;
          this.loading = true;
          request({
            url:'/api/dataasset/area/edit_area',
            method: 'post',
            data: row,
          }).then(({data}) => {
            if(data.code == 200){
              this.$message.success('更新成功');
              this.closeSlide()
              this.fetchAssetFieldTable('3');
            }else if(data.code === 4010){
              this.$store.dispatch('LogOut').then(() => {
                location.reload()
              })
            } else{
              this.$message.error(data.message)
            }
          })
        }
      })
    },
    // 查看
    handleView(row){
      this.dialogStatus = 'view'
      this.addDialog = true
      this.fieldForm = {
        areaName: row.areaName,
        areaDesc: row.areaDesc,
        picPath: row.picPath
      }
      this.imgSrc = row.picPath
    },
    /******** 新增、编辑与查看end *********/
    /******** 删除、其他start *********/
    // 重置
    resetTemp(){
      this.fieldForm = {
        areaName: '',
        areaDesc: ''
      }
    },
    // 取消新增或编辑
    cancelSource(){
      this.$refs.fieldForm.resetFields()
    },
    // 删除
    deleteSource(val) {
      console.log('val', val)
      let idData = [];
      idData.push(val);
      this.$confirm('确定删除此数据源？', '提示', {
        type: 'warning'
      }).then(() => {
        request({
          url: '/api/dataasset/area/del_area',
          method: 'post',
          data: idData
          }).then(({data}) => {
            if(data.code == 200){
              this.$message.success({
                message: '删除成功'
              });
              this.fetchAssetFieldTable();
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
        console.log(11111)
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
.bg{
  width: 200px;
  height: 200px;
  background: url('../../assets/tdspic/homegj.jpg') no-repeat;
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
</style>
