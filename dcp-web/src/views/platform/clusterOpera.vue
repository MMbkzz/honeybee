<!--服务运维管理界面-->
<template>
  <div>
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">服务运维管理</h2>
        </div>
        <div class="fr">
          <el-button class="more-search" @click="showMoreSearch">
            更多查询 <svg-icon icon-class="spread" :class="{'retract':moreSearch}"></svg-icon>
          </el-button>
        </div>
        <div class="search fr">
          <input class="do-search-input" type="text" style="padding-left: 15px; width: 200px; outline: none; border: 0; font-size: 12px; display: inline-block; height: 100%; float: left;" v-model="query.name" placeholder="请输入关键字"></input>
          <a href="javascript:;" @click="fetchClusterOperTable()"><svg-icon icon-class="search"></svg-icon></a>
        </div>
        <div class="fr">
          <el-button class="add-btn" type="primary" @click="handleCreate" :disabled="!operatePermission"><i>+</i>新增</el-button>
        </div>
      </div>
      <!--精确查询-->
      <!-- <el-collapse-transition> -->
        <div class="clearfix more-search-box">
          <el-form ref="form" :model="searchForm">
            <div class="clearfix input-group">
              <el-form-item label="实例名称" style="width: 226px;" class="fl" label-width="80px">
                <el-input v-model="searchForm.name" size="small"></el-input>
              </el-form-item>
              <el-form-item label="状态" style="width: 206px;" class="fl" label-width="60px">
                <el-select v-model="searchForm.statusCode" placeholder="" size="small" clearable>
                  <el-option label="未知" value="unknown"></el-option>
                  <el-option label="正常" value="normal"></el-option>
                  <el-option label="待停止" value="stopping"></el-option>
                  <el-option label="停止" value="stopped"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="主机用户" style="width: 226px;" class="fl" label-width="80px">
                <el-input v-model="searchForm.hostUser" size="small"></el-input>
              </el-form-item>
            </div>
            <div class="btn-group">
              <el-button type="primary" size="mini" @click="fetchClusterOperTable('1')">查询</el-button>
              <el-button size="mini" style="margin-left: 10px" @click.stop="resetQuery" >重置</el-button>
            </div>
          </el-form>
        </div>
      <!-- </el-collapse-transition> -->
    </nav>
    <section class="components-container clearfix section-bod" style="min-width:1050px;">
      <cr-loading v-show="loading"></cr-loading>
      <div class="opera-wrap" v-show="!loading">
        <div class="opera-card"
          v-for="(item,index) in tableData.data"
          :key="index"
          @click="handleView(item)">
          <div class="card-header">
            <span class="title">实例名称：{{item.name}}</span>
            <div class="handle-box">
              <a title="编辑" @click.stop="handleUpdate(item)" :style="{'cursor':operatePermission?'pointer':'not-allowed','opacity':operatePermission?'1':'0.6'}"><img src="../../assets/tdspic/edit.png" alt=""></a>
              <a title="删除" @click.stop="handleDelete(item.id)" :style="{'cursor':operatePermission?'pointer':'not-allowed','opacity':operatePermission?'1':'0.6'}"><img src="../../assets/tdspic/delete.png" alt=""></a>
              <a title="重置" @click.stop="handleReset(item.id)" :style="{'cursor':operatePermission?'pointer':'not-allowed','opacity':operatePermission?'1':'0.6'}"><img style="width: 20px; height: 20px;" src="../../assets/tdspic/reply.png" alt=""></a>
              <crland-switch :operatePermission="operatePermission" @beforeChange="beforeSwitchChange" :objBind="item" :status="item.oper" :offText="'停用'" @handleClick="switchChange"></crland-switch>
            </div>
          </div>
          <div class="card-step">
            <div
              v-for="(stepItem,index) in stepArray"
              :key="index"
              :class="{'step':true,'active':stepItem.step_name === item.stageCode,'over':index<stepArray.indexOf(stepArray.find(obj => obj.step_name === item.stageCode))}">
              <span class="step-num">{{ index+1 }}</span><br/>
              <span class="step-desc">{{ stepItem.step_cn_name }}</span><br/>
              <span class="act" v-if="stepItem.step_name === item.stageCode">▲</span>
              <span :class="{'step-line':true,'step-line-over':index<stepArray.indexOf(stepArray.find(obj => obj.step_name === item.stageCode))}" v-if="index<stepArray.length-1">----></span>
            </div>
          </div>
          <div class="card-footer">
            <div class="fl">
              <span>主机IP：{{item.host}}</span>
              <span>端口号：{{item.port}}</span>
            </div>
            <div class="fr">
              <span>状态：<i :style="{'color':stateColor[item.statusCode]}">{{item.statusCode | clusterStateTranst}}</i></span>
            </div>
          </div>
        </div>
      </div>

      <el-pagination
        background
        layout="prev, pager, next"
        @current-change="(current)=>{this.loading=true;this.tableData.data=[];searchForm.pageNo = current;this.fetchClusterOperTable('2')}"
        :page-size="parseInt(searchForm.pageSize)"
				:current-page="parseInt(searchForm.pageNo)"
        :total="tableData.page"
        class="fr mt-10">
      </el-pagination>
    </section>
    <!--新增与修改-->
    <!-- <el-dialog
      :title="textMap[dialogStatus]"
      :visible.sync="addDialog"
      width="50%"
      @close='cancelSource(clusterOperForm)'>
      <div class="form-body">

      </div>
      <span slot="footer" class="dialog-footer">
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createClusterOper(clusterOperForm)">保存</el-button>
        <el-button v-else type="primary" @click="editorClusterOper(clusterOperForm)">保存</el-button>
        <el-button @click="addDialog = false;">取消</el-button>
      </span>
    </el-dialog> -->
    <slide-box
      slideWidth="450px"
      :slideShow="addDialog"
      @close="slideClose"
      :title="textMap[dialogStatus]"
      ref="slideBox">
      <div class="form-body" slot="slide-content">
        <el-form :model="clusterOperForm" :rules="clusterOperRules" ref="clusterOperForm" label-width="100px">
          <el-form-item label="实例名称:" prop="name" required>
            <el-input v-model="clusterOperForm.name" size="medium" :disabled="dialogStatus == 'view'"></el-input>
          </el-form-item>
          <el-form-item label="主机IP:" prop="host" required class="mt-15">
            <el-input v-model="clusterOperForm.host" size="medium" :disabled="dialogStatus == 'view'"></el-input>
          </el-form-item>
          <el-form-item label="主机用户:" prop="hostUser" required class="mt-15">
            <el-input v-model="clusterOperForm.hostUser" size="medium" :disabled="dialogStatus == 'view'"></el-input>
          </el-form-item>
          <el-form-item label="主机密码:" prop="hostPassword" required class="mt-15">
            <el-input type="password" v-model="clusterOperForm.hostPassword" size="medium" :disabled="dialogStatus == 'view'"></el-input>
          </el-form-item>
          <el-form-item label="端口:" prop="port" required class="mt-15">
            <el-input v-model="clusterOperForm.port" size="medium" :disabled="dialogStatus == 'view'"></el-input>
          </el-form-item>
          <el-form-item label="存放路径:" prop="instancePath" required class="mt-15">
            <el-input v-model="clusterOperForm.instancePath" size="medium" :disabled="dialogStatus == 'view'"></el-input>
          </el-form-item>
          <el-form-item label="状态:" prop="statusCode" class="mt-15">
            <span v-if="clusterOperForm.statusCode == 'unknown'" style="color: #606266">未知</span>
            <span v-else-if="clusterOperForm.statusCode == 'normal'"  style="color: #606266">正常</span>
            <span v-else-if="clusterOperForm.statusCode == 'stopping'"  style="color: #606266">待停止</span>
            <span v-else-if="clusterOperForm.statusCode == 'stopped'"  style="color: #606266">停止</span>
          </el-form-item>
          <el-form-item label="阶段:" prop="stageCode">
            <span v-for="(item, index) in stepArray" v-if="clusterOperForm.stageCode == item.step_name" style="color: #606266" :key="index">{{ item.step_cn_name}}</span>
            <!-- <el-checkbox v-model="clusterOperForm.stageCode" true-label="unactivated" :disabled="true">未激活</el-checkbox> -->
          </el-form-item>
        </el-form>
      </div>
      <span slot="slide-footer">
        <el-button size="mini" :disabled="submitBtnDisabled" v-if="dialogStatus=='create'" type="primary" @click="createClusterOper(clusterOperForm)">保存</el-button>
        <el-button size="mini" :disabled="submitBtnDisabled" v-else type="primary" @click="editorClusterOper(clusterOperForm)">保存</el-button>
        <el-button size="mini" @click="closeSlide">取消</el-button>
      </span>
    </slide-box>
  </div>
</template>

<script>
import request from '@/utils/request'
import CrlandSwitch from '@/components/CrlandSwitch'
import SlideBox from '@/components/SlideBox'
import { screenResize } from '@/utils'
import CrLoading from '@/components/CrLoading'
// '1.unactivated 未激活、2.activated 已激活、3.initialized 已初始化、4.registered 已注册、5.online 已上线、6.unregister 取消注册、7.dealloc  释放资源、8.stopped 停止'
export default {
  components:{
    CrlandSwitch,
    SlideBox,
    CrLoading
  },
  data() {
    return {
      operatePermission: false,
      submitBtnDisabled: false,
      loading: true,
      testStr: 'activated',
      stepArray:[
        {step_name:'unactivated',step_cn_name:'未激活'},
        {step_name:'activated',step_cn_name:'已激活'},
        {step_name:'initialized',step_cn_name:'已初始化'},
        {step_name:'registered',step_cn_name:'已注册'},
        {step_name:'online',step_cn_name:'已上线'},
        {step_name:'unregister',step_cn_name:'取消注册'},
        {step_name:'dealloc',step_cn_name:'释放资源'},
        {step_name:'stopped',step_cn_name:'停止'}
      ],
      stateColor:{
        unknown:'#7EB3E9',
        normal: '#76C80E',
        stopping: '#4562fc',
        stopped: '#999'
      },
      query: {
        name: ''
      },
      /* 更多查询 */
      moreSearch: false,
      searchForm: {
        pageNo: '1',
        pageSize: '6',
      },
      // table数据
      tableData:{
        data: null,
      },
      /* 编辑与新增的分别 */
      addDialog: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新增',
        view: '查看'
      },
      // 新增表单
      clusterOperForm: {
        statusCode: 'unknown',
        stageCode: 'unactivated'
      },
      // 是否必填或填写规则
      clusterOperRules: {
        name: [{ required: true, message: '请输入实例名称'}],
        host: [{ required: true, message: '主机IP'}],
        hostUser: [{ required: true, message: '主机用户'}],
        hostPassword: [{ required: true, message: '主机密码'}],
        port: [{ required: true, message: '请输入端口'}],
        instancePath: [{ required: true, message: '请输入存放路径'}]
      }
    }
  },
  created(){
    this.fetchClusterOperTable()
  },
  mounted() {
    this.enterKeyEv()
    this.$store.getters.userInfo.menu.forEach(item => {
      // console.log(item)
      item.children.forEach(subItem => {
        if(subItem.name === '服务运维管理'){
          this.operatePermission = subItem.operationIds.indexOf('3') !== -1
          console.log(subItem.name+'页面操作权限',this.operatePermission)
        }
      })
    })
  },
  beforeDestroy() {
    $(document).off('keyup')
  },
  computed:{
    myWidth() {
      const obj = screenResize(520,220)
      const newWidth = obj.newWidth
      return newWidth-10 + 'px'
    },
    totalNum() { // 页面能存放的个数
      const total = screenResize(420,215).total
      return total
    }
  },
  methods: {
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
      this.cancelSource()
    },
    resetQuery(){
      this.searchForm = {
        name: undefined,
        statusCode: undefined,
        hostUser: undefined,
        pageNo: '1',
        pageSize: '6',
      }
    },
    //回车搜索
    enterKeyEv(){
      const self = this
      $(document).on('keyup',function(ev){
        // console.log(ev.keyCode === 13, $('#test-select').is(':focus'))
        // console.log($('#test-select').val())
        if(ev.keyCode === 13&&$('.do-search-input').is(':focus')){
          self.fetchClusterOperTable()
        }
      });
    },
    /******** 获取服务运维管理table start ********/
    fetchClusterOperTable(val){
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
        url: '/api/platform/operation/query_cluster_operation',
        method: 'get',
        params: obj
      }).then(({ data }) => {
        const self = this
        setTimeout(function(){
          self.loading = false
          if(data.code === 200){
            self.tableData.page = parseInt(data.data.count)
            self.tableData.data = JSON.parse(JSON.stringify(data.data.list))
            for(var i=0; i<self.tableData.data.length; i++){
              // if(self.tableData.data[i].statusCode == 'unknown' || self.tableData.data[i].statusCode == 'normal'){
              //   self.tableData.data[i].oper = true
              // }else
              if(self.tableData.data[i].statusCode == 'stopped'){
                self.tableData.data[i].oper = false
              }else {
                self.tableData.data[i].oper = true
              }

            }
            console.log(self.tableData.data)
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
    /******** 获取服务运维管理table end ********/
    /********* 新增Start ********/
    // 新增弹框
    handleCreate(form) {
      this.cancelSource()
      this.addDialog = true;
      this.dialogStatus = 'create';
      this.resetTemp()
    },
    // 新增确定函数
    createClusterOper(form) {
      this.$refs.clusterOperForm.validate((valid) => {
        if(valid){
          this.submitBtnDisabled = true
          request({
            url:'/api/platform/operation/add_cluster_operation',
            method: 'post',
            data: form,
          }).then(({data}) => {
            if(data.code == 200){
              this.$message.success('新增成功');
              this.closeSlide()
              this.fetchClusterOperTable();
            }else if(data.code === 4010){
              this.$store.dispatch('LogOut').then(() => {
                location.reload()
              })
            } else{
              this.$message.error(data.message)
            }
          })
          this.submitBtnDisabled = false;
        }else{
          return false;
        }
      })
    },
    // 取消新增
    cancelSource(){
      this.$refs.clusterOperForm.resetFields()
    },
    // 重置弹框内容
    resetTemp() {
      this.clusterOperForm = {
        statusCode: 'unknown',
        stageCode: 'unactivated'
      }
    },
    /********* 新增end ********/
    /******** 删除、编辑、查看start **********/
    // 编辑弹框
    handleUpdate(form){
      if(this.operatePermission){
        this.addDialog = true;
        this.dialogStatus = 'update';
        this.clusterOperForm = JSON.parse(JSON.stringify(form))
      }
    },
    // 编辑确定函数
    editorClusterOper(form){
      let source = {
        id: form.id,
        name: form.name,
        host: form.host,
        hostUser: form.hostUser,
        hostPassword: form.hostPassword,
        port: form.port,
        instancePath: form.instancePath,
        statusCode: form.statusCode,
        stageCode: form.stageCode
      }
      this.$refs.clusterOperForm.validate(valid => {
        if(valid){
          this.submitBtnDisabled = true
          request({
            url:'/api/platform/operation/update_cluster_operation',
            method: 'post',
            data: source,
          }).then(({data}) => {
            if(data.code == 200){
              this.$message.success('更新成功');
              this.closeSlide()
              this.fetchClusterOperTable();
            }else if(data.code === 4010){
              this.$store.dispatch('LogOut').then(() => {
                location.reload()
              })
            } else{
              this.$message.error(data.message)
            }
          })
          this.submitBtnDisabled = false;
        }
      })
    },
    // 查看
    handleView(form){
      this.addDialog = true;
      this.dialogStatus = 'view';
      this.clusterOperForm = JSON.parse(JSON.stringify(form))
    },
    // 删除
    handleDelete(id){
      if(this.operatePermission){
        this.$confirm('确定删除此集群？', '提示', {
          type: 'warning'
        }).then(() => {
          request({
            url: '/api/platform/operation/del_cluster_operation',
            method: 'post',
            data: {
              id: id
            }
            }).then(({data}) => {
              if(data.code == 200){
                this.$message.success({
                  message: '删除成功'
                });
                this.fetchClusterOperTable();
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
      }
    },
    // 状态重置
    handleReset(id){
      if(this.operatePermission){
        let paramData = {
          id: id,
          statusCode: 'stopped',
          stageCode: 'stopped'
        }
        this.$confirm('确定更改该状态？', '提示', {
          type: 'warning'
        }).then(() => {
            request({
              url:'/api/platform/operation/change__cluster_operation',
              method: 'post',
              data: paramData,
            }).then(({data}) => {
              if(data.code == 200){
                this.$message.success('更新成功');
                this.fetchClusterOperTable();
              }else if(data.code === 4010){
                this.$store.dispatch('LogOut').then(() => {
                  location.reload()
                })
              } else {
                this.$message.error(data.message)
              }
            })
          }
        ).catch(() => {
        })
      }
    },
    //开关改变状态之前调用
    beforeSwitchChange(val){
    },
    /******** 删除、编辑、查看end **********/
    // 状态改变
    switchChange(row){
      let paramData = {
        id: row.item.id,
      }
      if(row.item.oper == false){
        paramData.statusCode = 'unknown'
        paramData.stageCode = 'unactivated'
      }else {
        paramData.statusCode = 'stopping'
      }
      request({
        url:'/api/platform/operation/change__cluster_operation',
        method: 'post',
        data: paramData,
      }).then(({data}) => {
        if(data.code == 200){
          console.log(row.item.oper)
          if(!row.item.oper){
            this.$message.success({ message: '启动成功' });
          }else{
            this.$message.success({ message: '禁用成功' });
          }
          this.fetchClusterOperTable();
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        } else {
          this.loading = true;
          this.tableData.data = []
          this.fetchClusterOperTable();
          this.$message.error(data.message)
        }
      })
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
.opera-wrap{
  display:flex;
  flex-direction:row;
  justify-content: space-between;
  flex-wrap: wrap;
}
.opera-card{
  min-width: 520px;
  width: 48%;
  height: 200px;
  background-color: #ffffff;
	box-shadow: 0px 4px 3px 0px
		rgba(0, 0, 0, 0.08);
  border-radius: 5px;
  overflow: hidden;
  text-align: center;
  margin-bottom: 20px;
  border: 1px solid transparent;
  transition: all 0.5s;
  cursor: pointer;
  &:hover{
    border-color: #4562fc;
  }
  .card-header{
    padding: 0 15px;
    height: 40px;
    line-height: 40px;
    font-size: 12px;
    background: #fcfcfc;
    border-bottom: 1px solid #eee;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    .title{
      color: #555;
      font-size: 12px;
    }
    .handle-box{
      display: flex;
      flex-direction: row;
      width: 130px;
      justify-content: space-between;
      align-items: center;
      height: 100%;
      a{
        display: flex;
      }
    }
  }
  .card-step{
    text-align: center;
    display:flex;
    height: 120px;
    box-sizing: border-box;
    padding-top:30px;
    flex-direction: row;
    justify-content: center;
    border-bottom: 1px solid #eee;
    .step{
      font-size: 12px;
      color: white;
      display: inline-block;
      width: 65px;
      position: relative;
      user-select: none;
      span{
        display: inline-block;
        margin: 5px 0;
        user-select: none;
        font-size: 12px;
        &:nth-child(3){
          margin: 0;
        }
      }
      .step-num{
        background: #ccc;
        height: 22px;
        width: 22px;
        line-height: 22px;
        border-radius: 50%;
      }
      .step-desc{
        color: #a1a1a1
      }
      .step-line{
        position: absolute;
        font-family: consolas;
        color: #a1a1a1;
        width: 60px;
        top: 4px;
        left: 35px;
        &.step-line-over{
          color: #4562fc;
        }
      }
      .act{
        color: #4562fc;
      }
      &.over{
        .step-num{
          background: #4562fc;
          opacity: 0.5;
        }
        .step-desc{
          color: #f59f0a;
          opacity: 0.5;
        }
      }
      &.active{
        .step-num{
          background: #4562fc;
        }
        .step-desc{
          color: #f59f0a;
        }
      }
    }
  }
  .card-footer{
    height: 40px;
    line-height: 40px;
    color: #999;
    padding: 0 15px;
    span{
      font-size: 12px;
      i{
        font-size: 12px;
      }
    }
  }
}
.components-nav {
  padding-bottom: 15px;
  background: #f1f1f1;
  .titile {
    color: #444;
    font-size: 23px;
  }
}
.form-body{
  padding: 6px 30px;
  overflow:auto;
}
.state-div {
  color: #fff;
  font-size: 14px;
  width: 40px;
  height: 20px;
  text-align: center;
  border-radius: 6px;
}
.state-orange {
  background-color: #f1be1a;
}
.state-gray {
  background-color: #b4b8bb;
}
.state-green {
  background-color: #b5d561;
}
.state-red {
  background-color: #fb6c6e;
}
.circle {
 // display: inline-block;
  cursor: pointer;
  width: 15px;
  height: 15px;
  border-radius: 50%;
}
.triangle {
  // display: inline-block;
  width: 0;
  height: 0;
  border-left: 6px solid #999;
  border-top: 4px solid transparent;
  border-bottom: 4px solid transparent;
  margin: 4px 12px 2px 12px;
}
.circle-gray {
  background: #999;
}
.circle-green {
  background-color: #b5d561;
}
.circle-red {
  background-color: #de4e42;
}
.circle-orange {
  background-color: #f1be1a;
}
</style>
<style>
  .title-bg-color>th{
    background-color: rgba(246,247,250,1) !important;
  }
</style>
