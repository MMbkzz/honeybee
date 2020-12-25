 <!-- 服务访问日志界面 -->
<template>
  <div>
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">服务访问日志</h2>
        </div>
        <div class="fr">
          <el-button class="more-search" @click="showMoreSearch">
            更多查询 <svg-icon icon-class="spread" :class="{'retract':moreSearch}"></svg-icon>
          </el-button>
        </div>
        <div class="search fr">
          <input class="do-search-input" type="text" style="padding-left: 15px; width: 200px; outline: none; border: 0; font-size: 12px; display: inline-block; height: 100%; float: left;" v-model="query.name" placeholder="请输入关键字"></input>
          <a href="javascript:;" @click="fetchServiceVisitorTable()"><svg-icon icon-class="search"></svg-icon></a>
        </div>
      </div>
      <!-- <el-collapse-transition> -->
        <div class="clearfix more-search-box">
          <el-form ref="form" :model="searchForm">
            <div class="clearfix input-group">
              <el-form-item label="服务ID" style="width: 226px;" class="fl" label-width="100px">
                <el-input v-model="searchForm.dataServiceId" size="small"></el-input>
              </el-form-item>
              <el-form-item label="APP_ID" style="width: 226px;" class="fl" label-width="100px">
                <el-input v-model="searchForm.appId" size="small"></el-input>
              </el-form-item>
              <el-form-item label="访问时长(s)" style="width: 226px;" class="fl" label-width="100px">
                <el-input v-model="searchForm.accessTimes" size="small"></el-input>
              </el-form-item>
              <el-form-item label="执行时长(s)" style="width: 226px;" class="fl" label-width="100px">
                <el-input v-model="searchForm.execTimes" size="small"></el-input>
              </el-form-item>
              <el-form-item label="创建时间" style="width: 340px;" class="fl" label-width="100px">
                <el-date-picker
                  v-model="searchForm.startTime"
                  type="daterange"
                  range-separator="至"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  size="mini">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="日志信息" style="width: 226px;" class="fl" label-width="100px">
                <el-input v-model="searchForm.message" size="small"></el-input>
              </el-form-item>
            </div>

            <div class="btn-group">
              <el-button type="primary" size="mini" @click="fetchServiceVisitorTable('1')">查询</el-button>
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
        size="small"
        @cell-click = "toDetail"
        :header-row-class-name="titleBgColor">
        <el-table-column label="服务ID" prop="dataServiceId" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="APP_ID" prop="appId" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="实例IP" prop="instanceHost" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="实例端口" prop="instancePort" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="访问耗时(s)" prop="accessTimes" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="执行耗时(s)" prop="execTimes" show-overflow-tooltip>
        </el-table-column>
        <!--<el-table-column label="日志类型" prop="logType" show-overflow-tooltip>
        </el-table-column>-->
        <el-table-column label="日志信息" width="200">
          <template slot-scope="scope">
            <!--<div @click="displayLog = !displayLog" v-show="displayLog" style="cursor: pointer">
              {{ scope.row.logMassge | textLimit(10)}}
            </div>
            <div @click="displayLog = !displayLog" v-show="!displayLog" style="cursor: pointer">
              {{ scope.row.logMassge }}
            </div>-->
            <div style="font-size: 12px; color: #555">{{ scope.row.message | textLimit(10) }}</div>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" show-overflow-tooltip>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        layout="prev, pager, next"
        @current-change="(current)=>{searchForm.pageNo = current;this.fetchServiceVisitorTable('2')}"
        :page-size="10"
        :current-page="1"
        :total="tableData.page"
        class="fr mt-15">
      </el-pagination>
    </section>
    <!-- 日志详情弹框 -->
    <!-- <el-dialog
      title="日志信息"
      :visible.sync="detailDialog"
      width="60%">
      <div class="form-body">

      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="detailDialog = false">关闭</el-button>
      </span>
    </el-dialog> -->
    <!-- 滑框 -->
    <slide-box slideWidth="450px" :slideShow="detailDialog" @close="slideClose" title="详情" ref="slideBox">
      <div slot="slide-content">
        <el-form :model="logDetailForm" :rules="logDetailRule" ref="logDetailForm" label-width="125px" style="padding: 10px 30px;">
          <el-form-item label="服务名称:" prop="dataServiceName">
            <el-input v-model="logDetailForm.dataServiceName" size="medium" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="访问用户:" prop="name" class="mt-10">
            <el-input v-model="logDetailForm.name" size="medium" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="请求参数:" prop="requestParams" class="mt-10">
            <el-input v-model="logDetailForm.requestParams" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="访问开始时间:" prop="accessStartTime" class="mt-10">
            <el-input v-model="logDetailForm.accessStartTime" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="访问结束时间:" prop="accessEndTime" class="mt-10">
            <el-input v-model="logDetailForm.accessEndTime" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="访问耗时(s):" prop="accessTimes" class="mt-10">
            <el-input v-model="logDetailForm.accessTimes" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="执行开始时间:" prop="dbStartTime" class="mt-10">
            <el-input v-model="logDetailForm.dbStartTime" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="执行结束时间:" prop="dbEndTime" class="mt-10">
          <el-input v-model="logDetailForm.dbEndTime" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="执行耗时(s):" prop="execTimes" class="mt-10">
            <el-input v-model="logDetailForm.execTimes" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="实例IP:" prop="instanceHost" class="mt-10">
            <el-input v-model="logDetailForm.instanceHost" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="实例端口:" prop="instancePort" class="mt-10">
            <el-input v-model="logDetailForm.instancePort" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="客户端IP:" prop="clientHost" class="mt-10">
            <el-input v-model="logDetailForm.clientHost" :disabled="true"></el-input>
          </el-form-item>
          <!--<el-form-item label="日志类型:" prop="logType" class="mt-10">
            <el-input v-model="logDetailForm.logType" ></el-input>
          </el-form-item>-->
          <el-form-item label="日志信息:" prop="message" class="mt-10">
            <el-input type="textarea" v-model="logDetailForm.message"></el-input>
          </el-form-item>
          <el-form-item label="返回行数:" prop="returnRow" class="mt-10">
            <el-input v-model="logDetailForm.returnRow" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="返回数据量(kb):" prop="returnSize" class="mt-10">
            <el-input v-model="logDetailForm.returnSize" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="创建时间:" prop="createTime" class="mt-10">
            <el-input v-model="logDetailForm.createTime" :disabled="true"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="slide-footer">
        <el-button type="primary" size="mini" @click="closeSlide">关闭</el-button>
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
        dataServiceId: undefined,
        appId: undefined,
        startTime: undefined,
        message: undefined,
        queryType: 'public',
        queryString: '',
        pageNo: '1',
        pageSize: '10'
      },
      tableData: {
        data: null,
        page: undefined
      },
      /* 日志信息显示隐藏 */
      displayLog: true,
      foo1: true,
      /* 日志信息详情 */
      detailDialog: false,
      detailMassage: '',
      logDetailForm: {},
      logDetailRule: {}
    }
  },
  created() {
    this.fetchServiceVisitorTable()
  },
  mounted() {
    this.enterKeyEv()
  },
  beforeDestroy() {
    $(document).off('keyup')
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
    },
    slideClose(){
      this.detailDialog = false
    },
    resetQuery(){
      this.searchForm = {
        dataServiceId: undefined,
        appId: undefined,
        startTime: undefined,
        message: undefined,
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
          self.fetchServiceVisitorTable()
        }
      });
    },
    /******** 获取服务访问日志start **********/
    fetchServiceVisitorTable(val){
      this.loading = true
      let paramData
      if(this.searchForm.startTime){
        paramData = {
          dataServiceId: this.searchForm.dataServiceId,
          appId: this.searchForm.appId,
          accessTimes: this.searchForm.accessTimes,
          execTimes: this.searchForm.execTimes,
          message: this.searchForm.message,
          startTime: this.searchForm.startTime[0],
          endTime: this.searchForm.startTime[1],
          pageNo: this.searchForm.pageNo,
          pageSize: this.searchForm.pageSize
        }
      }else{
        paramData = {
          dataServiceId: this.searchForm.dataServiceId,
          appId: this.searchForm.appId,
          accessTimes: this.searchForm.accessTimes,
          execTimes: this.searchForm.execTimes,
          message: this.searchForm.message,
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
        Object.keys(paramData).forEach(key => {
          obj[key] = paramData[key]
        })
        obj.queryString = this.query.name
        obj.queryType = 'public'
      }
      console.log(obj)
      request({
        url: '/api/services/query_access_log',
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
          this.$message.error(data.message)
        }
      })
    },
    /******** 获取服务访问日志end **********/
    // 日志信息详情
    toDetail(row, column, cell, event){
      this.detailDialog = true
      // console.log(row)
      // this.logDetailForm = row
      request({
        url:'/api/services/get_access_log',
        method: 'get',
        params: {
          id: row.id
        }
      }).then(({data}) => {
        this.logDetailForm = data.data
      })
    },
    // 日志详情
   /*  accordDetails(detail){
      this.detailMassage = detail
    }, */
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
.section-bod{
  background: transparent;
  border: none;
  box-shadow: none;
}
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
    overflow:auto;
  }
</style>
<style>
.title-bg-color>th{
  background-color: rgba(246,247,250,1) !important;
}
</style>