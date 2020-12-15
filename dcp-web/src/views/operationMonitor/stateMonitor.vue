<!--数据源监控界面-->
<template>
  <div>
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">数据源监控</h2>
          <div class="model-tab">
            <span :class="{'select-tab':sourceOrInstanceType==='service'}" @click="modelTab('service')">数据源</span>
            <span>|</span>
            <span :class="{'select-tab':sourceOrInstanceType==='instance'}" @click="modelTab('instance')">实例</span>
          </div>
        </div>
        <div class="fr">
          <el-button class="more-search" @click="showMoreSearch">
            更多查询 <svg-icon icon-class="spread" :class="{'retract':moreSearch}"></svg-icon>
          </el-button>
        </div>
        <div class="search fr">
          <input type="text" class="do-search-input" style="padding-left: 15px; width: 200px; outline: none; border: 0; font-size: 12px; display: inline-block; height: 100%; float: left;" v-model="query.name" placeholder="请输入关键字"></input>
          <a href="javascript:;" @click="doSearch()"><svg-icon icon-class="search"></svg-icon></a>
        </div>
      </div>
      <!-- <el-collapse-transition> -->
        <div class="clearfix more-search-box">
            <!-- 数据源名称 -->
          <el-form ref="form" :model="searchFormSource" v-if="sourceOrInstanceType == 'service'">
            <div class="clearfix input-group">
              <template>
                <el-form-item label="数据源名称" style="width: 236px;" class="fl" label-width="90px">
                  <el-input v-model="searchFormSource.serviceSourceName" size="small"></el-input>
                </el-form-item>
                <el-form-item label="状态" style="width: 206px;" class="fl" label-width="60px">
                  <el-select v-model="searchFormSource.statusCode" placeholder="" size="small" clearable>
                    <el-option :label="item.displayName" :value="item.code" v-for="(item, index) in statusCodeListSource" :key="index"></el-option>
                  </el-select>
                </el-form-item>
              </template>
            </div>
            <div class="btn-group">
              <el-button type="primary" size="mini" @click="fetchSourceMonitorTable('1')">查询</el-button>
              <el-button size="mini" style="margin-left: 10px"  @click.stop="resetQuerySource">重置</el-button>
            </div>
          </el-form>
            <!-- 实例 -->
          <el-form v-else :model="searchFormInstance">
            <div class="clearfix input-group">
              <template>
                <template>
                  <el-form-item label="实例名称" style="width: 236px;" class="fl" label-width="90px">
                    <el-input v-model="searchFormInstance.instanceName" size="small"></el-input>
                  </el-form-item>
                </template>
                <el-form-item label="状态" style="width: 206px;" class="fl" label-width="60px">
                  <el-select v-model="searchFormInstance.statusCode" placeholder="" size="small" clearable>
                    <el-option :label="item.displayName" :value="item.code" v-for="(item, index) in statusCodeListInstance" :key="index"></el-option>
                  </el-select>
                </el-form-item>
              </template>
            </div>
            <div class="btn-group">
              <el-button type="primary" size="mini" @click="fetchInstanceMonitorTable('1')">查询</el-button>
              <el-button size="mini" style="margin-left: 10px"  @click.stop="resetQueryInstance">重置</el-button>
            </div>
          </el-form>
        </div>
      <!-- </el-collapse-transition> -->
    </nav>
    <section class="components-container clearfix" key="sourceTable">
      <!-- <el-table ref="multipleTable"
        :data="sourceTable.data"
        tooltip-effect="dark"
        style="width: 100%"
        size="small"
        :header-row-class-name="titleBgColor">
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-table
              class="demo-table-expand"
              size="small"
              :data="scope.row.instances"
              :header-row-class-name="titleBgColor">
              <el-table-column prop="id" label="实例编号" show-overflow-tooltip width="100">
              </el-table-column>
              <el-table-column prop="name" label="实例名称" show-overflow-tooltip width="100">
              </el-table-column>
              <el-table-column prop="iphost" label="IP" show-overflow-tooltip width="100">
              </el-table-column>
              <el-table-column prop="port" show-overflow-tooltip label="端口号" width="100">
              </el-table-column>
              <el-table-column label="预期持有连接数">
                <template slot-scope="scope">
                  <div class="progressContainer">
                      <div class="progress-gary" :style="{width:scope.row.expectNumber+'%'}">
                        <b>{{scope.row.expectNumber}}</b>
                      </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="已持有连接数">
                <template slot-scope="scope">
                  <div class="progressContainer">
                    <div class="progress-orange" :style="{width:scope.row.ownNum/scope.row.expectedNum*100+'%'}">
                      <b>{{scope.row.ownNum}}</b>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="已使用有连接数">
                <template slot-scope="scope">
                  <div class="progressContainer">
                    <div class="progress-green" :style="{width:scope.row.used/scope.row.expectedNum*100+'%'}">
                      <b>{{scope.row.used}}</b>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="剩余连接数">
                <template slot-scope="scope">
                  <div class="progressContainer">
                    <div class="progress-blue" :style="{width:scope.row.remain/scope.row.expectedNum*100+'%'}">
                      <b>{{scope.row.remain}}</b>
                    </div>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </template>
        </el-table-column>
        <el-table-column label="数据源名称" prop="serviceSourceName" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="数据源状态" prop="statusCode" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="最大连接数">
          <template slot-scope="scope">
            <div class="progressContainer">
                <div class="progress-gary" :style="{width:scope.row.maxConnections+'%'}">
                  <b>{{scope.row.maxConnections}}</b>
                </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="已使用连接数">
          <template slot-scope="scope">
            <div class="progressContainer">
              <div class="progress-green" :style="{width:scope.row.hasLink/scope.row.maxLink*100+'%'}">
                <b>{{scope.row.hasLink}}</b>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="剩余连接数" prop="remainLink" show-overflow-tooltip>
          <template slot-scope="scope">
            <div class="progressContainer">
              <div class="progress-blue" :style="{width:scope.row.remainLink/scope.row.maxLink*100+'%'}">
                <b>{{scope.row.remainLink}}</b>
              </div>
            </div>
          </template>
        </el-table-column>
      </el-table> -->
      <cr-loading v-show="loading"></cr-loading>
      <list-card
        v-if="sourceOrInstanceType === 'service'"
        :listData="sourceTable.data"
        :fieldsData="sourcefieldsData"
        :header="false"
        :isDiy="true"
        :footer="true">
        <div slot="footer" slot-scope="props">
           <!-- 嵌入在底部的table写在这，数据取props.data是对应的card对象 -->
          <!-- {{ props.data }} -->
          <el-table max-height="300" class="diy-table" :data="props.data.instances" align="left" header-align="left" size="small">
            <el-table-column prop="id" label="实例编号">
            </el-table-column>
            <el-table-column prop="name" label="实例名称" show-overflow-tooltip width="100">
            </el-table-column>
            <el-table-column prop="host" label="IP" show-overflow-tooltip width="100">
            </el-table-column>
            <el-table-column prop="port" label="端口号">
            </el-table-column>
            <el-table-column label="状态">
              <template slot-scope="scope">
                {{ scope.row.statusCode | clusterStateTranst}}
              </template>
            </el-table-column>
            <el-table-column prop="expectNumber" label="预期持有连接数" width="140">
            </el-table-column>
            <el-table-column prop="heldNumber" label="已持有连接数">
            </el-table-column>
            <el-table-column　width="150" fixed="right" label="已使用　　　　剩余">
              <template slot-scope="scope">
                <div class="diy-wrap">
                  <div class="progress-bar used" :style="{width:calcu(scope.row.heldNumber,scope.row.usedNumber)}">{{ scope.row.usedNumber }}</div>
                  <div class="progress-bar surplus">{{ scope.row.heldNumber - scope.row.usedNumber }}</div>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </list-card>

      <list-card
        v-if="sourceOrInstanceType === 'instance'"
        :listData="instanceTable.data"
        :fieldsData="instancefieldsData"
        :header="false"
        :isDiy="true"
        :footer="true">
        <div slot="footer" slot-scope="props">
           <!-- 嵌入在底部的table写在这，数据取props.data是对应的card对象 -->
          <!-- {{ props.data }} -->
          <el-table max-height="300" class="diy-table" :data="props.data.serviceSources" align="left" header-align="left" size="small">
            <el-table-column prop="serviceSourceName" label="数据源名称">
            </el-table-column>
            <el-table-column label="数据源状态" show-overflow-tooltip>
              <template slot-scope="scope">
                {{ scope.row.statusCode | serviceUserStateTranst}}
              </template>
            </el-table-column>
            <el-table-column prop="maxConnections" label="持有连接数" show-overflow-tooltip>
            </el-table-column>
            <el-table-column　width="150" fixed="right" label="已使用　　　　剩余">
              <template slot-scope="scope">
                <div class="diy-wrap">
                  <div class="progress-bar used" :style="{width:calcu(scope.row.maxConnections,scope.row.usedNumber)}">{{ scope.row.usedNumber }}</div>
                  <div class="progress-bar surplus">{{ scope.row.maxConnections - scope.row.usedNumber }}</div>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </list-card>
      <div class="section-bod">
        <el-pagination
          v-if="sourceOrInstanceType === 'service'"
          background
          @current-change="sourcePageChange"
          :page-size="parseInt(searchFormSource.pageSize)"
          :current-page="parseInt(searchFormSource.pageNo)"
          layout="prev, pager, next"
          :total="sourceTable.page"
          class="fr mt-15">
        </el-pagination>
        <el-pagination
          v-if="sourceOrInstanceType === 'instance'"
          background
          @current-change="(current)=>{loading = true;instanceTable.data=[];searchFormInstance.pageNo = current;this.fetchInstanceMonitorTable('2')}"
          :page-size="parseInt(searchFormInstance.pageSize)"
          :current-page="parseInt(searchFormInstance.pageNo)"
          layout="prev, pager, next"
          :total="instanceTable.page"
          class="fr mt-15">
        </el-pagination>
      </div>
    </section>
  </div>
</template>

<script>
import request from '@/utils/request'
import ListCard from '@/components/ListCard'
import CrLoading from '@/components/CrLoading'
export default {
  components:{
    ListCard,
    CrLoading
  },
  data() {
    return {
      queryInfo: null,
      loading: true,
      query: {
        name: ''
      },
      sourcefieldsData:[
        {
          propsTitle: '数据源名称',
          field: 'serviceSourceName'
        },
        {
          propsTitle: '数据源状态',
          field: 'statusCode'
        },
        {
          propsTitle: '最大连接数',
          field: 'maxConnections'
        }
      ],
      instancefieldsData:[
        {
          propsTitle: '实例编号',
          field: 'id'
        },
        {
          propsTitle: '实例名称',
          field: 'name'
        },
        {
          propsTitle: 'IP',
          field: 'host'
        },
        {
          propsTitle: '端口号',
          field: 'port'
        },
        {
          propsTitle: '状态',
          field: 'statusCode'
        },
        {
          propsTitle: '预期持有连接数',
          field: 'expectNumber'
        },
        {
          propsTitle: '已持有连接数',
          field: 'heldNumber'
        }
      ],
      /* 更多查询 */
      moreSearch: false,
      searchFormSource: {
        serviceSourceName: undefined,
        statusCode: undefined,
        pageNo: '1',
        pageSize: '5',
        queryString: '',
        queryType: 'public'
      },
      searchFormInstance: {
        instanceName: undefined,
        statusCode: undefined,
        pageNo: '1',
        pageSize: '5',
        queryString: '',
        queryType: 'public'
      },

      statusCodeListSource: [], // 数据源状态
      statusCodeListInstance: [], // 实例状态
      /****** 数据源或者实例类别start ******/
      sourceOrInstanceType: 'service',
      /****** 数据源或者实例类别end ******/
      // 数据源数据
      sourceTable: {
        data: null,
        page: undefined
      },
      // 实例数据
      instanceTable: {
        data: null,
        page: undefined
      },
      // 二级菜单
      list: [
        {
          title: '数据源A',
          content: ['实例1','实例2','实例3']
        },{
          title: '数据源B',
          content: ['实例A','实例B']
        },{
          title: '数据源C',
          content: ['实例A','实例B']
        },{
          title: '数据源D',
          content: ['实例A','实例B']
        }
      ],
      showIndex: '1',
      secondIndex: '-1',
      menuTable: '1',
      progress: 50, // 进度条
    }
  },
  created() {
    this.fetchSourceMonitorTable()
    // this.fetchInstanceMonitorTable()
    this.fetchStatusCodeSource()
    this.fetchStatusCodeInstance()
  },
  mounted() {
    this.enterKeyEv()
  },
  beforeDestroy() {
    clearInterval(window.timer)
    $(document).off('keyup')
  },
  methods: {
    sourcePageChange(current){
      this.loading = true
      this.sourceTable.data=[]
      this.searchFormSource.pageNo = current
      this.fetchSourceMonitorTable('2')
    },
    doSearch(){
      if(this.sourceOrInstanceType === 'service'){
        this.fetchSourceMonitorTable()
      }else if(this.sourceOrInstanceType === 'instance'){
        this.fetchInstanceMonitorTable()
      }
    },
    calcu(max,used) { //计算比例
      max = max === 0?1:max
      const scaleNum = used / max * 100 + '%'
      return scaleNum
    },
    showMoreSearch(){
      const self = this;
      this.resetQuerySource()
      this.resetQueryInstance()
      if(!this.moreSearch){
        $('.more-search-box').stop().animate({'height':$('.more-search-box .el-form').height()+'px','margin-top':'15px'},300)
      }else{
        $('.more-search-box').stop().animate({'height':'0','margin-top':'0'},300)
      }
      this.moreSearch = !this.moreSearch
    },
    setSourceTimer(){
      const self = this
      // clearInterval(window.timer)
      window.timer = setInterval(function(){
        request({
          url: '/api/operations/query_source_monitor',
          method: 'get',
          params: self.queryInfo
        }).then(({ data }) => {
          if(window.timer === undefined){
            return
          }
          if(data.code === 200){
            self.sourceTable.page = parseInt(data.data.count)
            self.sourceTable.data = data.data.list
          }else if(data.code === 4010){
            self.$store.dispatch('LogOut').then(() => {
              location.reload()
            })
          }else{

          }
        })
      },3000)
    },
    setInstanceTimer(){
      const self = this
      // clearInterval(window.timer)
      window.timer = setInterval(function(){
        request({
          url: '/api/operations/query_instance_monitor',
          method: 'get',
          params: self.queryInfo
        }).then(({ data }) => {
          if(window.timer === undefined){
            return
          }
          if(data.code === 200){
            self.instanceTable.page = parseInt(data.data.count)
            self.instanceTable.data = data.data.list
          }else if(data.code === 4010){
            self.$store.dispatch('LogOut').then(() => {
              location.reload()
            })
          }else{

          }
        })
      },3000)
    },
    //切换
    modelTab(typeCode) {
      this.sourceOrInstanceType = typeCode
      this.query.name = ''
      if(typeCode === 'service'){
        this.fetchSourceMonitorTable()
      }else if(typeCode === 'instance'){
        this.fetchInstanceMonitorTable()
      }
      $('.list-card .svg-icon').removeClass('retract');
      $('.list-card .footer-box').height(0);
      // this.fetchAssetFieldTable()
    },

    /****** 查询重置、获取状态码start*******/
      // 数据源
    fetchStatusCodeSource(){
      request({
        url: '/api/service/model/query_status',
        method: 'get',
        params: {
          type: 'source_status_code'
        }
      }).then(({ data }) => {
        // console.log(1111111)
        this.statusCodeListSource = data.data
      })
    },
    resetQuerySource(){
      this.searchFormSource = {
        serviceSourceName: undefined,
        statusCode: undefined,
        pageNo: '1',
        pageSize: '5'
      }
    },
      // 实例
    fetchStatusCodeInstance(){
      request({
        url: '/api/service/model/query_status',
        method: 'get',
        params: {
          type: 'instance_status_code'
        }
      }).then(({ data }) => {
        this.statusCodeListInstance = data.data
      })
    },
    resetQueryInstance(){
      this.searchFormInstance = {
        instanceName: undefined,
        statusCode: undefined,
        pageNo: '1',
        pageSize: '5'
      }
    },
    enterKeyEv(){
      const self = this
      $(document).on('keyup',function(ev){
        // console.log(ev.keyCode === 13, $('#test-select').is(':focus'))
        // console.log($('.do-search-input').val( ))
        if(ev.keyCode === 13&&$('.do-search-input').is(':focus')){
          self.doSearch()
        }
      });
    },
    /****** 查询重置、获取状态码end*******/
    /******** 获取数据源监控start ********/
    fetchSourceMonitorTable(val){
      if(window.timer){
        clearInterval(window.timer)
      }
      this.loading = true;
      this.sourceTable.data = []
      const obj = {}
      Object.keys(this.searchFormSource).forEach(key => {
        obj[key] = this.searchFormSource[key]
      })
      if(val==='1'){// 更多查询
        this.query.name = ''
        this.searchFormSource.pageNo = '1'
      }else if(val==='2'){// 分页
        if(this.query.name === ''){ // 更多查询分页
        }else{// 模糊查询分页
          obj.serviceSourceName = this.query.name
        }
      }else{// 模糊查询
        this.resetQuerySource()
        // 清空再赋值
        Object.keys(this.searchFormSource).forEach(key => {
          obj[key] = this.searchFormSource[key]
        })
        obj.serviceSourceName = this.query.name
      }
      this.queryInfo = obj
      request({
        url: '/api/operations/query_source_monitor',
        method: 'get',
        params: obj
      }).then(({ data }) => {
        const self = this
        setTimeout(function(){
          self.loading = false
          if(data.code === 200){
            self.sourceTable.page = parseInt(data.data.count)
            self.sourceTable.data = data.data.list
            self.setSourceTimer()
          }else if(data.code === 4010){
            self.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
          }else{

          }
        },300)
      })
    },
    /******** 获取实例监控start ********/
    fetchInstanceMonitorTable(val){
      if(window.timer){
        clearInterval(window.timer)
      }
      this.loading = true;
      this.instanceTable.data = []
      const obj = {}
      Object.keys(this.searchFormInstance).forEach(key => {
        obj[key] = this.searchFormInstance[key]
      })
      if(val==='1'){// 模糊查询
        this.query.name = ''
        this.searchFormInstance.pageNo = '1'
      }else if(val==='2'){// 分页
        if(this.query.name === ''){ // 更多查询分页
        }else{// 模糊查询分页
          obj.instanceName = this.query.name
        }
      }else{// 更多查询
        this.resetQueryInstance()
        // 清空再赋值
        Object.keys(this.searchFormInstance).forEach(key => {
          obj[key] = this.searchFormInstance[key]
        })
        obj.instanceName = this.query.name
      }
      this.queryInfo = obj
      request({
        url: '/api/operations/query_instance_monitor',
        method: 'get',
        params: obj
      }).then(({ data }) => {
        const self = this
        setTimeout(function(){
          self.loading = false
          if(data.code === 200){
            self.instanceTable.page = parseInt(data.data.count)
            self.instanceTable.data = data.data.list
            self.setInstanceTimer()
          }else if(data.code === 4010){
            self.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
          }else{

          }
        },300)

      })
    },

    /******** 获取数据源监控end ********/
     // 一级菜单点击
    show(i) {
      this.showIndex = i;
      this.secondIndex = '-1';
      this.menuTable = '1';
    },
      // 二级菜单点击
    secondActive(i){
      // this.showIndex = '-1';
      this.secondIndex = i;
      this.menuTable = '2';
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
.components-container{
  .diy-table{
    .diy-wrap{
      width: 100%;
      height: 16px;
      line-height: 16px;
      border-radius: 5px;
      position: relative;
      overflow: hidden;
      background: #76C80E;
      .progress-bar{
        color: white;
        position: absolute;
        height: 100%;
        &.used{
          background: #eca219;
          left: 0;
          text-align: left;
          padding-left: 10px;
          border-radius: 5px;
          font-size: 12px;
          width: 40%;
        }
        &.surplus{
          right: 0;
          text-align: right;
          font-size: 12px;
          padding-right: 10px;
          width: 60%;
        }
      }
    }
  }
}
.source-moni {
  height: 100%;
  margin-top: -15px;
  margin-left: -15px;
  .secondMenu {
    position: absolute;
    top: 0;
    bottom: 0;
    height: 100%;
    width: 180px;
    background: #e1e4e9;
    padding-top: 25px;
    .menu-title {
      height: 40px;
      line-height: 40px;
      color: rgba(50,63,79,1);
      font-size: 14px;
      padding-left: 20px;
      cursor: pointer;
    }
    .menu-title:hover{
      background: rgba(241, 241, 241, 1)
    }
    .menu-title-active{
      background: rgba(241, 241, 241, 1)
    }
    .menu-content {
      padding-bottom: 20px;
      display: none;
      transition: display 2s;
      div{
        cursor: pointer;
        height: 35px;
        line-height: 35px;
        padding-left: 60px;
        color: rgba(46,63,79,1);
        font-size: 14px;
      }
      div:hover{
        background: rgba(241, 241, 241, 1)
      }
      .div-active{
        background: rgba(241, 241, 241, 1)
      }
    }
    .active {
      display: block;
    }
  }
  .source-content {
    padding-left: 30px;
    margin-left: 180px;
    .components-nav {
      height: 75px;
      padding-top: 20px;
      background: #f1f1f1;
      .titile {
        color: #444;
        font-size: 23px;
      }
    }
  }
}
.triangle-up {
  display: inline-block;
  width: 0;
  height: 0;
  border-top: 5px solid rgba(50,63,79,1);
  border-left: 4px solid transparent;
  border-right: 4px solid transparent;
  margin-bottom: 2px;
  margin-right: 10px;
}
.triangle-down {
  display: inline-block;
  width: 0;
  height: 0;
  border-bottom: 5px solid rgba(50,63,79,1);
  border-left: 4px solid transparent;
  border-right: 4px solid transparent;
  margin-bottom: 2px;
  margin-right: 10px;
}
.fade-enter-active, .fade-leave-active {
  transition: opacity 1.5s;
}
.fade-enter, .fade-leave-to {
  opacity: 0;
}
.components-nav {
  padding-bottom: 15px;
  background: #f1f1f1;

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

.progressContainer{
  height: 20px;
  width: 70px;
  border-radius: 5px;
  background-color: rgba(203,207,216,1);
  margin-left: 2%;
}
.progress-orange {
  background-color: rgba(255,149,1,1);
  border-radius: 5px;
  height:20px;
  line-height: 20px;
}
.progress-gary {
  // background-color: rgba(203,207,216,1);
  border-radius: 5px;
  height:20px;
  line-height: 20px;
}
.progress-green {
  background-color: rgba(118,200,14,1);
  border-radius: 5px;
  height:20px;
  line-height: 20px;
}
.progress-blue {
  background-color: rgba(65, 138, 215,1);
  border-radius: 5px;
  height:20px;
  line-height: 20px;
}
b{
  color:#fff;
  font-weight: 100;
  font-size: 12px;
  position:absolute;
  left: 15%;
}
</style>
<style>
.title-bg-color>th{
  background-color: rgba(246,247,250,1) !important;
}
</style>
