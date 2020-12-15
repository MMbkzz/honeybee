
<!--消息中心界面-->
<template>
  <div class="message-container">
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">消息中心</h2>
          <div class="model-tab">
            <span :class="{'select-tab':searchForm.messageLevel==='notice'}" @click="modelTab('notice')">公告消息</span>
            <span>|</span>
            <span :class="{'select-tab':searchForm.messageLevel==='warning'}" @click="modelTab('warning')">预警消息</span>
          </div>
        </div>
        <div class="search fr">
          <input class="do-search-input" v-model="searchForm.queryString" type="text" style="padding-left: 15px; width: 200px; outline: none; border: 0; font-size: 12px; display: inline-block; height: 100%; float: left;" placeholder="请输入关键字"></input>
          <a href="javascript:;" @click="fetchMessageData()"><svg-icon icon-class="search"></svg-icon></a>
        </div>
        <div class="fr mr-10">
          <el-button class="batch-del" type="primary" @click.stop="batchDelete()">
            <span>批量删除</span>
          </el-button>
        </div>
      </div>
    </nav>
    <section class="section-box section-bod">
      <cr-loading v-show="loading"></cr-loading>
      <el-table
        v-show="!loading"
        id="message-table"
        ref="messageTable"
        :data="tableData.data"
        tooltip-effect="light"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        @expand-change="handleExpandChange">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
        <el-table-column
          label="标题内容">
          <template slot-scope="scope">
            <span :class="{'message-title':true,'read':scope.row.statusCode==='read'}">{{scope.row.title}}<i></i></span>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="接收日期">
        </el-table-column>
        <el-table-column type="expand">
          <template slot-scope="scope">
            <div>{{scope.row.context}}</div>
          </template>
        </el-table-column>

      </el-table>
      <el-pagination
        background
        layout="prev, pager, next"
        @current-change="(current)=>{searchForm.pageNo = current;this.fetchMessageData()}"
        :page-size="10"
        :current-page="1"
        :total="tableData.page"
        class="fr mt-15">
      </el-pagination>
      <!-- <cr-input-popver :inputModel="inputModel" @ok="okFn"></cr-input-popver> -->
    </section>
  </div>
</template>

<script>
import request from '@/utils/request'
import CrLoading from '@/components/CrLoading'
import CrInputPopver from '@/components/CrInputPopver'
export default {
  components: {
    CrLoading,
    CrInputPopver
  },
	data() {
    return {
      loading: true,
      inputModel: '',
      tableData:{
        data: null
      },
      searchForm:{
        messageLevel: 'notice',
        queryString: '',
        pageNo: '1',
        pageSize: '10'
      },
      multipleSelection: []
    }
  },
  created() {
    // console.log(window.location.href.split('=').length>1)
    if(window.location.href.split('=').length>1){
      this.searchForm.messageLevel = window.location.href.split('=')[1]
    }
  },
  mounted() {
    this.fetchMessageData()
    this.enterKeyEv()
  },
  beforeDestroy() {
    $(document).off('keyup')
  },
  methods: {
    okFn(val){
      this.inputModel = val
    },
    handleExpandChange(row,expandedRows){
      if(row.statusCode === 'read'){
        return
      }
      // row.statusCode = 'read'
      request({
        url: '/api/platform/message/edit_message',
        method: 'post',
        data: {
          id: row.id,
          objectId: row.objectId,
          statusCode: 'read'
        }
      }).then(({data}) => {
        if(data.code === 200){
          row.statusCode = 'read'
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 批量删除
    batchDelete(){
      if(this.multipleSelection.length === 0){
        this.$message.warning('未选中任何条目！')
        return
      }
      const paramData = []
      this.multipleSelection.forEach(item => {
        const obj = {}
        obj.id = item.id
        obj.objectId = item.objectId
        paramData.push(obj)
      })
      this.$confirm('确定删除选中的'+paramData.length+'条消息吗？', '提示', {
        type: 'warning'
      }).then(() => {
          request({
            url: '/api/platform/message/del_message',
            method: 'post',
            data: paramData
          }).then(({data}) => {
            if(data.code === 200){
              this.$message.success('删除成功！')
              this.fetchMessageData()
            }else{
              this.$message.error(data.message)
            }
          })
        }).catch(() => {
          console.log('取消')
        })
    },
    //回车搜索
    enterKeyEv(){
      const self = this
      $(document).on('keyup',function(ev){
        // console.log(ev.keyCode === 13, $('#test-select').is(':focus'))
        // console.log($('#test-select').val())
        if(ev.keyCode === 13&&$('.do-search-input').is(':focus')){
          self.fetchMessageData()
        }
      });
    },
    // 获取消息列表
    fetchMessageData(){
      this.loading = true
      request({
        url: '/api/platform/message/query_message',
        method: 'get',
        params: this.searchForm
      }).then(({data}) => {
        this.loading = false
        this.tableData.data = data.data.list
        this.tableData.page = data.data.count
      })
    },
    //消息类型切换
    modelTab(type) {
      this.searchForm.messageLevel = type
      this.searchForm.queryString = ''
      this.fetchMessageData()
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.components-nav{
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
}

#message-table{
  .el-table__expanded-cell{
    background: #EBEBEB;
  }
}
</style>
