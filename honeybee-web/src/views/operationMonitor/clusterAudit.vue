<!-- 服务审计日志界面 -->
<template>
  <div>
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">服务审计日志</h2>
        </div>
        <div class="fr">
          <el-button class="more-search" @click="showMoreSearch">
            更多查询 <svg-icon icon-class="spread" :class="{'retract':moreSearch}"></svg-icon>
          </el-button>
        </div>
        <div class="search fr">
          <input class="do-search-input" type="text" style="padding-left: 15px; width: 200px; outline: none; border: 0; font-size: 12px; display: inline-block; height: 100%; float: left;" v-model="query.name" placeholder="请输入关键字"></input>
          <a href="javascript:;" @click="fetchClusterAuditTable()"><svg-icon icon-class="search"></svg-icon></a>
        </div>
      </div>
      <!-- <el-collapse-transition> -->
        <div class="clearfix more-search-box">
          <el-form ref="form" :model="searchForm">
            <div class="clearfix input-group">
              <el-form-item label="任务名称" style="width: 226px;" class="fl" label-width="80px">
                <el-input v-model="searchForm.taskName" size="small"></el-input>
              </el-form-item>
              <el-form-item label="操作状态" style="width: 226px;" class="fl" label-width="80px">
                <el-select v-model="searchForm.statusCode" clearable placeholder="" size="small">
                  <el-option :label="item.displayName" :value="item.code" v-for="(item,index) in statusCodeList" :key="index"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="创建时间" style="width: 340px;" class="fl" label-width="80px">
                <el-date-picker
                  v-model="searchForm.time"
                  type="daterange"
                  range-separator="至"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  size="mini">
                </el-date-picker>
              </el-form-item>
            </div>
            <div class="btn-group">
              <el-button type="primary" size="mini" @click="fetchClusterAuditTable('1')">查询</el-button>
              <el-button size="mini" style="margin-left: 10px"  @click.stop="resetQuery">重置</el-button>
            </div>
          </el-form>
        </div>
      <!-- </el-collapse-transition> -->
    </nav>
    <section class="components-container clearfix section-bod" >
      <cr-loading v-show="loading"></cr-loading>
      <el-table
        v-show="!loading"
        ref="multipleTable"
        :data="tableData.data"
        tooltip-effect="light"
        style="width: 100%"
        size="small"
        @cell-click = "accordDetails"
        :header-row-class-name="titleBgColor">
        <el-table-column label="任务名称" prop="taskName" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="IP地址" prop="host" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="端口" prop="port" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="进程号" prop="thread" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="操作状态" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.statusCode |  auditStateTranst}}
          </template>
        </el-table-column>
        <!--<el-table-column label="日志类型" prop="statusCode" show-overflow-tooltip>
        </el-table-column>-->
        <el-table-column label="返回信息" show-overflow-tooltip>
          <template slot-scope="scope">
            <div style="font-size: 12px; color: #555">{{ scope.row.message | textLimit(6) }}</div>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" show-overflow-tooltip>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        @current-change="(current)=>{searchForm.pageNo = current;this.fetchClusterAuditTable('2')}"
        :page-size="10"
        :current-page="1"
        layout="prev, pager, next"
        :total="tableData.page"
        class="fr mt-15">
      </el-pagination>
    </section>
    <!-- 日志详情弹框 -->
    <!-- <el-dialog
      title="返回信息"
      :showClose='closeBtn'
      :visible.sync="detailDialog"
      width="60%">
      <div class="form-body">
        {{ detailMassage }}
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="detailDialog = false">关闭</el-button>
      </span>
    </el-dialog> -->
    <slide-box
      slideWidth="450px"
      :slideShow="detailDialog"
      @close="slideClose"
      title="详情"
      ref="slideBox">
      <div slot="slide-content">
        <el-form :model="detailMassage" label-width="70px" style="padding: 10px 30px;">
          <el-form-item label="任务名称:">
            <el-input v-model="detailMassage.taskName" size="medium" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="IP地址:" class="mt-10">
            <el-input v-model="detailMassage.host" size="medium" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="端口:" class="mt-10">
            <el-input v-model="detailMassage.port" size="medium" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="进程号:" class="mt-10">
            <el-input v-model="detailMassage.thread" size="medium" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="操作状态:" class="mt-10">
            <el-input v-model="detailMassage.statusCode" size="medium" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="返回信息:" class="mt-10">
            <el-input type="textarea" :rows="10" v-model="detailMassage.message" ></el-input>
          </el-form-item>
          <el-form-item label="创建时间:"  class="mt-10">
            <el-input v-model="detailMassage.createTime" :disabled="true"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="slide-footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="closeSlide">取消</el-button>
      </span>
    </slide-box>
  </div>
</template>

<script>
import request from '@/utils/request'
import SlideBox from '@/components/SlideBox'
import CrLoading from '@/components/CrLoading'
export default {
  components:{
    SlideBox,
    CrLoading
  },
  data() {
    return {
      loading: true,
      query: {
        name: ''
      },
      /* 更多查询 */
      moreSearch: false,
      searchForm: {
        taskName: undefined,
        statusCode: undefined,
        time: undefined,
        pageNo: '1',
        pageSize: '10'
      },
      tableData: {
        data: null,
        page: undefined
      },
      statusCodeList: [],
      /* 返回信息详情 */
      detailDialog: false,
      detailMassage: {},
      closeBtn: false
    }
  },
  created() {
    this.fetchClusterAuditTable()
    this.fetchStatusCode()
  },
  mounted() {
    this.enterKeyEv()
  },
  beforeDestroy() {
    $(document).off('keyup')
  },
  methods: {
    doSearch(val){
      console.log(val)
    },
    // 关闭滑框
    closeSlide(){
      this.$refs.slideBox.close()
    },
    slideClose(){
      this.detailDialog = false
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
    /****** 查询重置、获取状态码start*******/
    fetchStatusCode(){
      request({
        url: '/api/service/model/query_status',
        method: 'get',
        params: {
          type: 'auditLog_status_code'
        }
      }).then(({ data }) => {
        this.statusCodeList = data.data
      })
    },
    resetQuery(){
      this.searchForm = {
        taskName: undefined,
        statusCode: undefined,
        time: undefined,
        pageNo: '1',
        pageSize: '10'
      }
    },
    //回车搜索
    enterKeyEv(){
      const self = this
      $(document).on('keyup',function(ev){
        // console.log(ev.keyCode === 13, $('#test-select').is(':focus'))
        // console.log($('#test-select').val())
        if(ev.keyCode === 13&&$('.do-search-input').is(':focus')){
          self.fetchClusterAuditTable()
        }
      });
    },
    /******** 服务审计日志日志start **********/
    fetchClusterAuditTable(val){
      this.loading = true
      let paramData
      if(this.searchForm.time){
        paramData = {
          taskName: this.searchForm.taskName,
          statusCode: this.searchForm.statusCode,
          startTime: this.searchForm.time[0],
          endTime: this.searchForm.time[1],
          pageNo: this.searchForm.pageNo,
          pageSize: this.searchForm.pageSize
        }
      }else{
        paramData = {
          taskName: this.searchForm.taskName,
          statusCode: this.searchForm.statusCode,
          pageNo: this.searchForm.pageNo,
          pageSize: this.searchForm.pageSize
        }
      }
      const obj = {}
      Object.keys(paramData).forEach(key => {
        obj[key] = paramData[key]
      })
      if(val==='1'){// 更多查询
        this.query.name = ''
        this.searchForm.pageNo = '1'
        obj.pageNo = '1'
      }else if(val==='2'){// 分页
        if(this.query.name === ''){ // 更多查询分页
        }else{// 模糊查询分页
          obj.taskName = this.query.name
        }
      }else{// 模糊查询
        this.resetQuery()
        // 清空再赋值
        Object.keys(paramData).forEach(key => {
          obj[key] = paramData[key]
        })
        obj.taskName = this.query.name
      }
      // console.log(obj)
      request({
        url: '/api/operations/operation_query',
        method: 'get',
        params: obj
      }).then(({ data }) => {
        if(data.code === 200){
          const self = this
          setTimeout(function(){
            self.loading = false
            self.tableData.data = data.data.list
            self.tableData.page = parseInt(data.data.count)
          },300)
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
           location.reload()
         })
        }else{

        }
      })
    },
    /******** 服务审计日志日志end **********/
    // 日志详情
    accordDetails(detail){
      this.detailDialog = true
      this.detailMassage = detail
    },
    // el-table的背景边框改变函数
    titleBgColor({row, rowIndex}){
      return 'title-bg-color'
    }
  },
  filters: {
    textLimit: function(val, num){
      if(val){
        let temp = val ;
        if(val.length>num){
            temp = val.substring(0, num)+"...";
        }
        return temp;
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  .components-nav {
    padding-bottom: 15px;
    background: #f1f1f1;
    .titile {
      color: #444;
      font-size: 23px;
    }
  }
  .form-body {
    padding: 6px 30px;
    height: 260px;
    overflow:auto;
  }
</style>
<style>
.title-bg-color>th{
  background-color: rgba(246,247,250,1) !important;
}
</style>
