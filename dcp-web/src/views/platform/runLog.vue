<!--平台操作日志界面-->
<template>
  <div>
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">平台操作日志</h2>
        </div>
        <div class="fr">
          <el-button class="more-search" @click="showMoreSearch">
            更多查询 <svg-icon icon-class="spread" :class="{'retract':moreSearch}"></svg-icon>
          </el-button>
        </div>
        <div class="search fr">
          <input type="text" style="padding-left: 15px; width: 200px; outline: none; border: 0; font-size: 12px; display: inline-block; height: 100%; float: left;" v-model="query.name" placeholder="请输入关键字"></input>
          <a href="javascript:;" @click="fetchOperLogTable()"><svg-icon icon-class="search"></svg-icon></a>
        </div>
      </div>
      <!-- <el-collapse-transition> -->
        <div class="clearfix more-search-box">
          <el-form ref="form" :model="searchForm">
            <div class="clearfix input-group">
              <el-form-item label="操作用户" style="width: 200px;" class="fl" label-width="80px">
                <el-input v-model="searchForm.userName" size="small"></el-input>
              </el-form-item>
              <el-form-item label="操作状态" style="width: 200px;" class="fl" label-width="80px">
                <el-select v-model="searchForm.status" clearable placeholder="" size="small">
                  <el-option :label="item.displayName" :value="item.code" v-for="(item,index) in statusCodeList" :key="index"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="创建时间" style="width: 320px;" class="fl" label-width="80px">
                <el-date-picker
                  v-model="searchForm.time"
                  type="daterange"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  size="mini">
                </el-date-picker>
              </el-form-item>
            </div>
            <div class="btn-group">
              <el-button type="primary" size="mini" @click="fetchOperLogTable('1')">查询</el-button>
              <el-button size="mini" style="margin-left: 10px"  @click.stop="resetQuery">重置</el-button>
            </div>
          </el-form>
        </div>
      <!-- </el-collapse-transition> -->
    </nav>
    <section class="components-container clearfix section-bod">
      <cr-loading v-show="loading"></cr-loading>
      <el-table
        v-show="!loading"
        ref="multipleTable"
        :data="tableData.data"
        tooltip-effect="light"
        style="width: 100%"
        size="mini"
        @cell-click = "accordDetails"
        :header-row-class-name="titleBgColor">
        <el-table-column type="index" label="序号" width="50">
        </el-table-column>
        <el-table-column prop="userName" show-overflow-tooltip label="操作用户">
        </el-table-column>
        <el-table-column prop="ip" show-overflow-tooltip label="访问IP">
        </el-table-column>
        <el-table-column prop="status" show-overflow-tooltip label="操作状态">
        </el-table-column>
        <el-table-column prop="api" show-overflow-tooltip label="操作接口">
        </el-table-column>
        <el-table-column show-overflow-tooltip label="返回信息">
          <template slot-scope="scope">
            <div style="font-size: 12px; color: #555">{{ scope.row.apiDesc | textLimit(6) }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="requestTime" show-overflow-tooltip label="访问时间(ms)">
        </el-table-column>
        <el-table-column prop="responseTime" show-overflow-tooltip label="响应时间(ms)">
        </el-table-column>
        <el-table-column label="创建时间" show-overflow-tooltip>
          <template slot-scope="scope">{{ scope.row.createTime }}</template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        @current-change="(current)=>{searchForm.pageNo = current;this.fetchOperLogTable('2')}"
        :page-size="10"
        :current-page="1"
        layout="prev, pager, next"
        :total="tableData.page"
        class="fr mt-15">
      </el-pagination>
    </section>
    <!-- 详情弹框 -->
    <!-- <el-dialog
      title="详情"
      :visible.sync="detailDialog"
      :showClose='closeBtn'
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
          <el-form-item label="操作用户:">
            <el-input v-model="detailMassage.userName" size="medium" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="访问IP:" class="mt-10">
            <el-input v-model="detailMassage.ip" size="medium" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="操作状态:" class="mt-10">
            <el-input v-model="detailMassage.status" size="medium" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="操作接口:" class="mt-10">
            <el-input v-model="detailMassage.api" size="medium" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="返回信息:" class="mt-10">
            <el-input type="textarea" v-model="detailMassage.apiDesc" :rows="5"></el-input>
          </el-form-item>
          <el-form-item label="访问时间:"  class="mt-10">
            <el-input v-model="detailMassage.requestTime" size="medium" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="响应时间:"  class="mt-10">
            <el-input v-model="detailMassage.responseTime" size="medium" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="创建时间:"  class="mt-10">
            <el-input v-model="detailMassage.createTime" size="medium" :disabled="true"></el-input>
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
        userName: undefined,
        status: undefined,
        time: undefined,
        pageNo: '1',
        pageSize: '10'
      },
      statusCodeList: [],
      // table数据
      tableData: {
        data: null,
        page: undefined
      },
      /* 详情数据 */
      detailDialog: false,
      detailMassage: {},
      closeBtn: false
    }
  },
  created() {
    this.fetchOperLogTable()
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
          type: 'sysauditLog_status_code'
        }
      }).then(({ data }) => {
        this.statusCodeList = data.data
      })
    },
    resetQuery(){
      this.searchForm = {
        userName: undefined,
        status: undefined,
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
    /****** 查询重置、获取状态码end*******/
    /******** 平台操作日志日志start **********/
    fetchOperLogTable(val){
      this.loading = true
      let paramData
      if(this.searchForm.time){
        paramData = {
          userName: this.searchForm.userName,
          status: this.searchForm.status,
          startTime: this.searchForm.time[0],
          endTime: this.searchForm.time[1],
          pageNo: this.searchForm.pageNo,
          pageSize: this.searchForm.pageSize
        }
      }else{
        paramData = {
          userName: this.searchForm.userName,
          status: this.searchForm.status,
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
          obj.userName = this.query.name
        }
      }else{// 模糊查询
        this.resetQuery()
        // 清空再赋值
        Object.keys(paramData).forEach(key => {
          obj[key] = paramData[key]
        })
        obj.userName = this.query.name
      }
      request({
        url: '/api/operations/sys_audit_log_query',
        method: 'get',
        params: paramData
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
    /******** 平台操作日志日志end **********/
    // el-table的背景边框改变函数
    titleBgColor({row, rowIndex}){
      return 'title-bg-color'
    },
        // 详情显示
    accordDetails(content){
      this.detailDialog = true;
      this.detailMassage = content;
    },
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