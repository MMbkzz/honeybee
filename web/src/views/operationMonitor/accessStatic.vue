<!-- 数据服务监控界面 -->
<template>
  <div style="margin-bottom:30px;">
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">数据服务监控</h2>
        </div>
      </div>
    </nav>
    <div class="moni-time section-bod-title clearfix">
      <div class="fl">
        <ul class="time-title">
          <li>时间：</li>
        </ul>
      </div>
      <div class="fl">
        <ul class="time-list clearfix">
          <li class="fl" v-for="(item, index) in dataPicker" :class="{'active':item.id == num}" @click="tabChange(item)" :key="index">{{ item.name }}</li>
        </ul>
      </div>
      <div class="fl" style="margin-left: 10px;">
        <el-date-picker
          v-model="date"
          size="middle"
          type="date"
          placeholder="选择日期"
          @change="dateChange"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd">
        </el-date-picker>
      </div>
      <div class="fr" style="margin-right:30px;width:300px;">
        <span style="display:inline-block;height:100%;line-height:28px;color:#666">服务ID:</span>
        <el-input class="do-search-input" style="float:right;width:250px;" v-model="search.dataServiceId" size="small"></el-input>
      </div>
      <!-- <div class="fl" style="margin-left: 10px; margin-top: 4px;">
        <template>
          <el-checkbox v-model="comp" @change="addComDate">对比时间段</el-checkbox>
        </template>
      </div>
      <div class="fl" style="margin-left: 10px;" v-if="displayCom">
        <el-date-picker
          v-model="comDate"
          size="middle"
          type="date"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          placeholder="选择日期">
        </el-date-picker>
      </div> -->
    </div>
    <cr-loading v-show="loading"></cr-loading>
    <div class="statistics-wrap clearfix" :style="{'height':loading?0:'auto','visibility':loading?'hidden':'visible'}">

      <!-- 访问量、访客量、平均时长、平均执行时长 -->
      <div class="moni-line section-bod mt-20">
        <div class="line-des">
          <ul class="clearfix">
            <li class="fl">
              <dl>
                <dt>访问量</dt>
                <dd>{{ totalNum.accessServiceCount.toLocaleString() }}</dd>
              </dl>
            </li>
            <li class="fl">
              <dl>
                <dt>访客量</dt>
                <dd>{{ totalNum.accessAppCount }}</dd>
              </dl>
            </li>
            <li class="fl">
              <dl>
                <dt>平均访问时长（s）</dt>
                <dd>{{ totalNum.accessAvgTime }}</dd>
              </dl>
            </li>
            <li class="fl">
              <dl>
                <dt>平均执行时长（s）</dt>
                <dd>{{ totalNum.executeAvgTime }}</dd>
              </dl>
            </li>
          </ul>
        </div>
        <div class="line-content clearfix">
          <div class="fl access-status">
            <div class="num-pie" id="numPiePic"></div>
          </div>
          <div class="fr access-amount">
            <!-- <p class="text-center" style="text-align:center;">访问量、访客量、平均访问时长（s）、平均执行时长（s）</p> -->
            <div style="margin-top:30px" class="line-pic" id="linePic"></div>
          </div>
        </div>
      </div>
      <!-- 服务访问量排名、 用户访问量排名 -->
      <div class="visit-pic clearfix">
        <div class="fl servise-visit section-bod">
          <p>
            服务访问量排名
            <span :class="{'right-move':true,'not-allow-move':serviceVisitPageNum === serviceVisitMaxPage}" @click="serviceVisitRankPageChange('0')"><i class="el-icon-arrow-right"></i></span>
            <span :class="{'left-move':true,'not-allow-move':serviceVisitPageNum === 1}" @click="serviceVisitRankPageChange('1')"><i class="el-icon-arrow-left"></i></span>
          </p>
          <div class="servise-bar" id="serviseBar"></div>
        </div>
        <div class="fl user-visit section-bod">
          <p>
            用户访问量排名
            <span :class="{'right-move':true,'not-allow-move':userVisitPageNum === userVisitMaxPage}" @click="userVisitRankPageChange('0')"><i class="el-icon-arrow-right"></i></span>
            <span :class="{'left-move':true,'not-allow-move':userVisitPageNum === 1}" @click="userVisitRankPageChange('1')"><i class="el-icon-arrow-left"></i></span>
          </p>
          <div class="user-bar" id="userBar"></div>
        </div>
      </div>
      <!-- Top5访问时长、Top5执行时长 -->
      <div class="top-table mt-20 clearfix">
        <div class="top5-cont fl section-bod">
          <p>Top5访问时长</p>
          <table style="width: 100%">
            <thead>
              <tr>
                <th style="width: 10%; text-align: left; font-size:13px">排名</th>
                <th style="width: 25%; text-align: left; font-size:13px">服务ID</th>
                <th style="width: 45%; text-align: left; font-size:13px">访问时间</th>
                <th style="width: 10%; text-align: left; font-size:13px">时长(s)</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in top5AccessTime.slice(0,5)" :key="index">
                <th style="text-align: left; font-size:13px">{{ index+1 }}</th>
                <th style="text-align: left; font-size:13px">{{ item.serviceName }}</th>
                <th style="text-align: left; font-size:13px">{{ item.accessTime }}</th>
                <th style="text-align: left; font-size:13px">{{ item.serviceTime === undefined?item.serviceTime:item.serviceTime.toLocaleString() }}<!--.toLocaleString()--></th>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="dbtop5-cont fl section-bod">
          <p>Top5执行时长</p>
          <table style="width: 100%">
            <thead>
              <tr>
                <th style="width: 10%; text-align: left; font-size:13px">排名</th>
                <th style="width: 25%; text-align: left; font-size:13px">服务ID</th>
                <th style="width: 45%; text-align: left; font-size:13px">执行时间</th>
                <th style="width: 10%; text-align: left; font-size:13px">时长(s)</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in dbTop5AccessTime.slice(0,5)" :key="index">
                <th style="text-align: left; font-size:13px">{{ index+1 }}</th>
                <th style="text-align: left; font-size:13px">{{ item.serviceName }}</th>
                <th style="text-align: left; font-size:13px">{{ item.accessTime }}</th>
                <th style="text-align: left; font-size:13px">{{ item.executeTime === undefined?item.executeTime:item.executeTime.toLocaleString() }}<!--.toLocaleString()--></th>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <!-- 服务平均访问时长排名、服务DB平均访问时长 -->
      <div class="service-num clearfix mt-20">
        <div class="visiter-cont fl section-bod">
          <p>
            服务平均访问时长排名
            <span :class="{'right-move':true,'not-allow-move':serviceAvgVisitPageNum === serviceAvgVisitMaxPage}" @click="serviceAvgVisitRankPageChange('0')"><i class="el-icon-arrow-right"></i></span>
            <span :class="{'left-move':true,'not-allow-move':serviceAvgVisitPageNum === 1}" @click="serviceAvgVisitRankPageChange('1')"><i class="el-icon-arrow-left"></i></span>
          </p>
          <table style="width: 100%">
            <thead>
              <tr>
                <th style="width: 10%; text-align: left; font-size:13px">排名</th>
                <th style="width: 70%; text-align: left; font-size:13px">服务ID</th>
                <th style="width: 10%; text-align: left; font-size:13px">时长(s)</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in serviceVisitTime.slice(0,5)" :key="index">
                <th style="text-align: left; font-size:13px">{{ (serviceAvgVisitPageNum-1)*5+index+1 }}</th>
                <th style="text-align: left; font-size:13px">{{ item.serviceName }}</th>
                <th style="text-align: left; font-size:13px">{{ item.serviceAvg === undefined?item.serviceAvg:item.serviceAvg.toLocaleString() }}<!--.toLocaleString()--></th>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="dbvisiter-cont fl section-bod">
          <p>
            服务平均执行时长排名
            <span :class="{'right-move':true,'not-allow-move':serviceAvgExecutePageNum === serviceAvgExecuteMaxPage}" @click="serviceAvgExecuteRankPageChange('0')"><i class="el-icon-arrow-right"></i></span>
            <span :class="{'left-move':true,'not-allow-move':serviceAvgExecutePageNum === 1}" @click="serviceAvgExecuteRankPageChange('1')"><i class="el-icon-arrow-left"></i></span>
          </p>
          <table style="width: 100%">
            <thead>
              <tr>
                <th style="width: 10%; text-align: left; font-size:13px">排名</th>
                <th style="width: 70%; text-align: left; font-size:13px">服务ID</th>
                <th style="width: 10%; text-align: left; font-size:13px">时长(s)</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in serviceDbVisitTime.slice(0,5)" :key="index">
                <th style="text-align: left; font-size:13px">{{ (serviceAvgExecutePageNum-1)*5+index+1 }}</th>
                <th style="text-align: left; font-size:13px">{{ item.serviceName }}</th>
                <th style="text-align: left; font-size:13px">{{ item.executeAvg === undefined?item.executeAvg:item.executeAvg.toLocaleString() }}<!--.toLocaleString()--></th>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import request from '@/utils/request'

import CrLoading from '@/components/CrLoading'
export default {
  components: {
    CrLoading
  },
  data() {
    return {
      loading: true,
      options: [],
      value8: '',
      /* 时间选择 */
      dataPicker: [
        {
          id: '1',
          name: '今天',
          value: '0'
        },{
          id: '2',
          name: '昨天',
          value: '1'
        },{
          id: '3',
          name: '最近7天',
          value: '7'
        },{
          id: '4',
          name: '最近30天',
          value: '30'
        }
      ],
      search: {
        queryDate: undefined,
        dataServiceId: '',
        dateIndex: '0'
      },
      serviceVisitPageNum: 1,//服务访问量排名当前分页数
      serviceVisitMaxPage: 1,//服务访问量最大分页数
      userVisitPageNum: 1,//用户访问量排名当前分页数
      userVisitMaxPage: 1,//用户访问量最大分页数
      serviceAvgVisitPageNum: 1,//服务平均访问时长排名当前分页数
      serviceAvgVisitMaxPage: 1,//服务平均访问时长最大分页数
      serviceAvgExecutePageNum: 1,//服务平均执行时长排名当前分页数
      serviceAvgExecuteMaxPage: 1,//服务平均执行时长最大分页数
      num: '1',
      /* 总量（浏览量(PV)、访客量(UV)、平均访问时长与DB执行平均时长） */
      totalNum: {
        accessServiceCount: '', //浏览量(PV)
        accessAppCount: '', // 访问量
        accessAvgTime: '', // 平均访问时长
        executeAvgTime: '' // DB执行平均时长
      },
      /* 访问成功、失败量 */
      accessResult: [],
      /* 服务、用户访问量排名 */
      serviceRank: [], // 服务访问
      userRank: [], // 用户访问
      top5AccessTime: [], // top5访问时长
      dbTop5AccessTime: [], // dbTop5访问时长
      serviceVisitTime: [], // 服务平均访问时长排名
      serviceDbVisitTime: [], // 服务DB平均访问时长
      lineData: [],
      comp: false,
      date: '',
      comDate: '', // 对比时间段
      displayCom: false,
    }
  },
  mounted(){
    this.fetchServiceMonitor()
    this.enterKeyEv()
  },
  beforeDestroy() {
    $(document).off('keyup')
  },
  methods: {
    serviceVisitRankPageChange(val){
      if(val === '0'){
        this.serviceVisitPageNum++
        if(this.serviceVisitPageNum>this.serviceVisitMaxPage){
          this.serviceVisitPageNum = this.serviceVisitMaxPage
          return;
        }
      }else if(val === '1'){
        this.serviceVisitPageNum--
        if(this.serviceVisitPageNum < 1){
          this.serviceVisitPageNum = 1
          return;
        }
      }
      this.fetchRankData(this.serviceVisitPageNum,7,'accessCount')
    },
    userVisitRankPageChange(val){
      if(val === '0'){
        this.userVisitPageNum++
        if(this.userVisitPageNum>this.userVisitMaxPage){
          this.userVisitPageNum = this.userVisitMaxPage
          return;
        }
      }else if(val === '1'){
        this.userVisitPageNum--
        if(this.userVisitPageNum < 1){
          this.userVisitPageNum = 1
          return;
        }
      }
      this.fetchRankData(this.userVisitPageNum,7,'appCount')
    },
    serviceAvgVisitRankPageChange(val){
      if(val === '0'){
        this.serviceAvgVisitPageNum++
        if(this.serviceAvgVisitPageNum>this.serviceAvgVisitMaxPage){
          this.serviceAvgVisitPageNum = this.serviceAvgVisitMaxPage
          return;
        }
      }else if(val === '1'){
        this.serviceAvgVisitPageNum--
        if(this.serviceAvgVisitPageNum < 1){
          this.serviceAvgVisitPageNum = 1
          return;
        }
      }
      this.fetchRankData(this.serviceAvgVisitPageNum,5,'accessAvg')
    },
    serviceAvgExecuteRankPageChange(val){
      if(val === '0'){
        this.serviceAvgExecutePageNum++
        if(this.serviceAvgExecutePageNum>this.serviceAvgExecuteMaxPage){
          this.serviceAvgExecutePageNum = this.serviceAvgExecuteMaxPage
          return;
        }
      }else if(val === '1'){
        this.serviceAvgExecutePageNum--
        if(this.serviceAvgExecutePageNum < 1){
          this.serviceAvgExecutePageNum = 1
          return;
        }
      }
      this.fetchRankData(this.serviceAvgExecutePageNum,5,'executeAvg')
    },
    //回车搜索
    enterKeyEv(){
      const self = this
      $(document).on('keyup',function(ev){
        if(ev.keyCode === 13&&$('.do-search-input .el-input__inner').is(':focus')){
          self.fetchServiceMonitor()
        }
      });
    },
    fetchRankData(currentPage,pageSize,accessType){
      const paramData = {}
      $.extend(true,paramData,this.search)
      paramData.pageNo = currentPage
      paramData.pageSize = pageSize
      paramData.accessType = accessType
      request({
        url: '/api/operations//query_page_monitor',
        method: 'get',
        params: paramData
      }).then(({data}) => {
        if(data.code === 200){
          switch(accessType){
            case 'accessCount':
              this.serviceRank = data.data.list
              if(data.data.count === 0){
                this.serviceVisitMaxPage = 1
              }else{
                this.serviceVisitMaxPage = Math.ceil(data.data.count/pageSize)
              }
              this.drawServiceBar()
              break;
            case 'appCount':
              this.userRank = data.data.list
              if(data.data.count === 0){
                this.userVisitMaxPage = 1
              }else{
                this.userVisitMaxPage = Math.ceil(data.data.count/pageSize)
              }
              this.drawUserBar()
              break;
            case 'accessAvg':
              if(data.data.count === 0){
                this.serviceAvgVisitMaxPage = 1
              }else{
                this.serviceAvgVisitMaxPage = Math.ceil(data.data.count/pageSize)
              }
              this.serviceVisitTime = data.data.list
              break;
            case 'executeAvg':
              if(data.data.count === 0){
                this.serviceAvgExecuteMaxPage = 1
              }else{
                this.serviceAvgExecuteMaxPage = Math.ceil(data.data.count/pageSize)
              }
              this.serviceDbVisitTime = data.data.list
              break;
            default:
              break;
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
    fetchServiceMonitor(){
      let total
      this.loading =  true
      this.serviceVisitPageNum = 1
      this.userVisitPageNum = 1
      this.serviceAvgVisitPageNum = 1
      this.serviceAvgExecutePageNum = 1
      request({
        url: '/api/operations/query_service_monitor',
        methods: 'get',
        params: this.search
      }).then(({data}) => {
        if(data.code === 200){
          this.loading = false
          console.log(data.data)
          this.totalNum = {
            accessServiceCount: data.data.accessServiceCount,
            accessAppCount: data.data.accessAppCount,
            accessAvgTime: data.data.accessAvgTime,
            executeAvgTime: data.data.executeAvgTime
          }
          // 线性图数据
          if(this.search.dateIndex==='0'||this.search.dateIndex === '1'){
            this.lineData =  data.data.accessHourApi
          }else{
            this.lineData = data.data.accessDayApi
          }
          total = data.data.accessOkCount + data.data.accessErrorCount
          // 环形图数据
          this.accessResult[0] = {
            value: data.data.accessOkCount,
            name:'访问成功数',
            itemStyle:{normal:{color:'#11c98d'}},
            label: {  // 圆圈中间数字
              normal: {
                  show: true,
                  position: 'center',
                  formatter: function(data) {
                    return '访问次数' +'\n'+'\n'+ total.toLocaleString()
                  },
                  textStyle: {
                    fontSize: 12,
                    color: '#999'
                  }
              },
            },
            labelLine: {
              show: false
            }
          }
          this.accessResult[1] = {
            value: data.data.accessErrorCount,
            name:'访问失败数',
            itemStyle:{normal:{color:'#feb846'}},
            label: {
              normal: {
                show: false,
              },
            },
            labelLine: {
              show: false
            }
          }
          // 横柱状图
          // this.serviceRank = data.data.serviceCountRank
          // this.userRank = data.data.appCountRank
          // 访问时长
          this.top5AccessTime = data.data.serviceTimeRank
          this.dbTop5AccessTime = data.data.executeTimeRank
          // 服务访问时长
          // this.serviceVisitTime = data.data.serviceAvgRank
          // this.serviceDbVisitTime = data.data.executeAvgRank

          this.drawLine()
          this.drawNumPie()
          this.drawServiceBar()
          this.drawUserBar()
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{
          // this.$message.error(data.message)
        }
      })
      this.fetchRankData(1,7,'accessCount')
      this.fetchRankData(1,7,'appCount')
      this.fetchRankData(1,5,'accessAvg')
      this.fetchRankData(1,5,'executeAvg')
    },
    // 数据线形图处理
    dateLineFn(arr){
      const dateLineData = {}
      dateLineData.accessCountArr = []
      dateLineData.appCountArr = []
      dateLineData.accessAvgArr = []
      dateLineData.executeAvgArr = []
      for(let i=0;i<arr.length;i++){
        let a = 0
        let b = 0
        let c = 0
        let d = 0
        let obj = {}
        if(this.search.dateIndex === '0' || this.search.dateIndex === '1'){
          obj = this.lineData.find(item => arr[i] === parseInt(item.recordTime))
        }else{
          obj = this.lineData.find(item => arr[i] === parseInt(item.day))
        }
        if(obj !== undefined){
          a = obj.accessCount
          b = parseInt(obj.appCount)
          c = obj.accessAvg
          d = obj.executeAvg
        }
        dateLineData.accessCountArr.push(a)
        dateLineData.appCountArr.push(b)
        dateLineData.accessAvgArr.push(c)
        dateLineData.executeAvgArr.push(d)
      }
      return dateLineData
    },
    drawLine() {
      let linePic = this.$echarts.init(document.getElementById('linePic'))
      let xAxisData = []
      let data = {}
      let _dateIndex = this.search.dateIndex
      const xAxisData1 = [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23] //今天，昨天横坐标
      const xAxisData2 = [0,1,2,3,4,5,6,7]//最近7天横坐标
      const xAxisData3 = [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30]//最近30天横坐标
      console.log(this.lineData)
      if(_dateIndex === '0' || _dateIndex === '1'){
        xAxisData = xAxisData1
      }else if(_dateIndex === '7'){
        xAxisData = xAxisData2
      }else if(_dateIndex === '30'){
        xAxisData = xAxisData3
      }
      data = this.dateLineFn(xAxisData)
      // for(let i=0;i<xAxisData.length;i++){
      //   let a = 0
      //   let b = 0
      //   let c = 0
      //   let d = 0
      //   const obj = this.lineData.find(item => xAxisData[i] === parseInt(item.recordTime))
      //   if(obj !== undefined){
      //     a = obj.accessCount
      //     b = parseInt(obj.appCount)
      //     c = obj.accessAvg
      //     d = obj.executeAvg
      //   }
      //   accessCountArr.push(a)
      //   appCountArr.push(b)
      //   accessAvgArr.push(c)
      //   executeAvgArr.push(d)
      // }
      linePic.setOption({
        tooltip : {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#6a7985'
            }
          },
          textStyle:{
            align:'left'
          }
        },
        legend: {
          bottom: '12%',
          orient: 'horizontal',
          data:['访问量','访客量','平均访问时长(s)','平均执行时长(s)']
        },
        grid: {
          left: '2%',
          right: '2%',
          top: '10%',
          bottom: '30%',
          containLabel: true
        },
        xAxis:{
          type : 'category',
          boundaryGap : false,
          data : xAxisData//横坐标数据
        },
        yAxis:[
          {
            type : 'value',
            axisLine: {
              show: false
            },
            axisTick: {
              show: false
            }
          },{
            type: 'value',
            scale: true,
            name: '时长（s）',
            axisLine: {
              show: false
            },
            axisTick: {
              show: false
            }
          }
        ],
        series : [
          {
            name:'访问量',
            type:'line',
            itemStyle:{normal:{color:'#66b4f6'}},
            yAxisIndex: 0,
            data: data.accessCountArr
          },
          {
            name:'访客量',
            type:'line',
            itemStyle:{normal:{color:'#8acd3f'}},
            yAxisIndex: 0,
            data: data.appCountArr
          },
          {
            name:'平均访问时长(s)',
            type:'line',
            itemStyle:{normal:{color:'#f3867f'}},
            yAxisIndex: 1,
            data: data.accessAvgArr
          },
          {
            name:'平均执行时长(s)',
            type:'line',
            yAxisIndex: 1,
            itemStyle:{normal:{color:'#fca263'}},
            data: data.executeAvgArr
          }
        ]
      })
    },
    drawNumPie() {
      let numPiePic = this.$echarts.init(document.getElementById('numPiePic'))
      numPiePic.setOption({
        tooltip: {
          trigger: 'item',
          formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        legend: {
          bottom: 10,
          left: 'center',
          data: ['访问成功数', '访问失败数']
        },
        hoverAnimation: false,
        series: [
          {
            name:'访问次数',
            type:'pie',
            radius: ['45%', '70%'],
            avoidLabelOverlap: false,

            labelLine: {
              normal: {
                  show: false
              }
            },
            data: this.accessResult
          }
        ]
      })
    },
    // 服务访问量排名 （左一）
    drawServiceBar(){
      let serviseBar = this.$echarts.init(document.getElementById('serviseBar'))
      let baifenbi = []
      let grayBar = []
      let name = []
      let percent = []
      for(var i=0; i<this.serviceRank.length; i++)  {
        baifenbi.push(this.serviceRank[i].serviceCount.toFixed(2)+'%')
        percent.push((this.serviceRank[i].serviceCount/100).toFixed(4))
        // grayBar.push(1)
        if(this.serviceRank[i].serviceName){
          name.push(this.serviceRank[i].serviceName)
        }else{
          name.push('')
        }
      }
      const newName = []
      const newBaifenbi = []
      const newPercent = []
      // console.log(serviceRank.length)
      let len = this.serviceRank.length>7?6:this.serviceRank.length-1;
      for(let j=len;j>=0;j--){
        // if(percent[j] !== '0.0000'){
          newName.push(name[j])
          grayBar.push(1)
          newBaifenbi.push(baifenbi[j])
          newPercent.push(percent[j])
        // }
      }
      serviseBar.setOption({
        // tooltip : {
        //   trigger: 'item',
        //   axisPointer: {
        //     label: {
        //       backgroundColor: '#6a7985'
        //     }
        //   }
        // },
        color: ['#feb846'], //进度条颜色
        grid: {
          left: 100,
        },
        xAxis: [
          {
            show: false,
          },
          // 顶部加一个X轴,刻度是百分比
          {
            type: 'category',
            axisLine: {
              show: false
            },
            axisTick: {
              show: false, //隐藏Y轴刻度
            },
            boundaryGap: false,
            data: ['0', '50%', '100%']
          }
        ],
        yAxis: {
          type: 'category',
          data: newName,
          axisTick: {
            show: false, //隐藏Y轴刻度
          },
          axisLine: {
            show: false, //隐藏Y轴线段
          }
        },
        series: [
          {
            show: true,
            type: 'bar',
            barGap: '-100%',
            barWidth: 14, //统计条宽度
            itemStyle: {
                normal: {
                    barBorderRadius: 20,
                    color: '#fafafa'
                },
            },
            z:1,
            data: grayBar,
          },
          {
            show: true,
            type: 'bar',
            barGap: '-100%',
            barWidth: 14, //统计条宽度
            itemStyle: {
                normal: {
                  barBorderRadius: 20, //统计条弧度
                },
            },
            max: 1,
            label: {
              normal: {
                show: true,
                textStyle: {
                  color: '#666', //百分比颜色
                },
                align: 'left',
                position: 'inside',
                //百分比格式
                formatter: function(data) {
                  return newBaifenbi[data.dataIndex];
                },
              }
            },
            labelLine: {
              show: false,
            },
            z:2,
            data: newPercent,
          }
        ]
      })
    },
    // 用户访问量排名 (右一)
    drawUserBar(){
      let userBar = this.$echarts.init(document.getElementById('userBar'))
      let baifenbi = []
      let grayBar = []
      let percent = []
      let name = []
      for(var i=0; i<this.userRank.length; i++)  {
        baifenbi.push(this.userRank[i].appCount.toFixed(2)+'%')
        percent.push((this.userRank[i].appCount/100).toFixed(4))
        // grayBar.push(1)
        if(this.userRank[i].appName){
          name.push(this.userRank[i].appName)
        }else{
          name.push('')
        }
      }

      const newName = []
      const newBaifenbi = []
      const newPercent = []
      let len = this.userRank.length>7?6:this.userRank.length-1;
      for(let j=len;j>=0;j--){
        // if(percent[j] !== '0.0000'){
          newName.push(name[j])
          grayBar.push(1)
          newBaifenbi.push(baifenbi[j])
          newPercent.push(percent[j])
        // }
      }
      userBar.setOption({
        color: ['#feb846'], //进度条颜色
        grid: {
          left: 100,
        },
        xAxis: [
          {
            show: false,
          },
          // 顶部加一个X轴,刻度是百分比
          {
            type: 'category',
            axisLine: {
              show: false
            },
            axisTick: {
              show: false, //隐藏Y轴刻度
            },
            boundaryGap: false,
            data: ['0', '50%', '100%']
          }
        ],
        yAxis: {
          type: 'category',
          data: newName,
          axisTick: {
              show: false, //隐藏Y轴刻度
          },
          axisLine: {
              show: false, //隐藏Y轴线段
          }
        },
        series: [
          {
            show: true,
            type: 'bar',
            barGap: '-100%',
            barWidth: 12, //统计条宽度
            itemStyle: {
              normal: {
                barBorderRadius: 20,
                color: '#fafafa'
              },
            },
            z:1,
            data: grayBar,
          },
          {
            show: true,
            type: 'bar',
            barGap: '-100%',
            barWidth: 12, //统计条宽度
            itemStyle: {
              normal: {
                barBorderRadius: 20, //统计条弧度
              },
            },
            max: 1,
            label: {
              normal: {
                show: true,
                textStyle: {
                    color: '#666', //百分比颜色
                },
                align: 'left',
                position: 'inside',
                //百分比格式
                formatter: function(data) {
                    return newBaifenbi[data.dataIndex];
                },
              }
            },
            labelLine: {
              show: false,
            },
            z:2,
            data: newPercent,
          }
        ]
      })
    },
    // 选择对比时间
    addComDate(flag){
      if(flag){
        this.displayCom = true
      }else{
        this.displayCom = false
      }
    },
    // 日期模糊选择tab页切换
    tabChange(val){
      this.num = val.id
      this.search.dateIndex = val.value
      this.fetchServiceMonitor()
    },
    // 日期精准选择
    dateChange(val){
      this.search.queryDate = val
      this.fetchServiceMonitor()
    }
  }
}
</script>

<style lang="scss" scoped>
  .section-bod-title{
    background-color: #ffffff;
    box-shadow: 0px 4px 3px 0px
    rgba(0, 0, 0, 0.08);
    border-radius: 5px;
  }
  .section-bod {
    min-height: 230px;
    background-color: #ffffff;
    box-shadow: 0px 4px 3px 0px
    rgba(0, 0, 0, 0.08);
    border-radius: 5px;
    p{
      text-align: left;
    }
  }
  .moni-time {
    font-size: 14px;
    padding: 11px 0 14px 14px;
    .time-title {
      height: 30px;
      line-height: 30px;
      display: inline-block;
      color: #999;
    }
    .time-list {
      height: 30px;
      line-height: 30px;
      display: inline-block;
      border: 1px solid rgba(240,241,242,1);
      color: #666;
      li {
        padding: 0 8px;
        cursor: pointer;
      }
    }
    .contrast {
      display: inline-block;
    }

  }
  .statistics-wrap{

  }
  .moni-line {
    .line-des {
      width: 100%;
      border-bottom: 1px solid rgba(225,225,225,1);
      padding: 12px;
      font-size: 14px;
      ul {
        width: 100%;
        li {
          padding-left: 12px;
          width: 25%;
          color: #999;
          dl{
            text-align: left;
            dt{
              font-size: 12px;
              color: #666;
            }
            dd{
              margin-top: 10px;
              margin-left: 0;
              font-size: 20px;
              color: #666;
            }
          }
        }
        li:nth-child(1)>dl{
          border-right: 1px solid rgba(225,225,225,1)
        }
        li:nth-child(2)>dl{
          text-align: left;
          border-right: 1px solid rgba(225,225,225,1)
        }
        li:nth-child(3)>dl{
          border-right: 1px solid rgba(225,225,225,1)
        }
      }
    }
    .line-content{
      padding-top: 4px;
      p {
        height: 36px;
        line-height: 36px;
        font-size: 14px;
        color: #666;
      }
      .access-status {
        width: 30%;
        .num-pie{
          margin: 0 auto;
          width: 80%;
          height: 300px
        }
      }
      .access-amount {
        width: 70%;
        .line-pic{
          margin: 0 auto;
          width: 80%;
          height: 300px
        }
      }
    }
  }
  .visit-pic {
    width: 100%;
    margin-top: 20px;
    .servise-visit {
      padding: 15px;
      width: 49%;
      margin-right: 2%;
      .servise-bar {
        margin: 0 auto;
        width: 100%;
        height: 300px;
      }
    }
    .user-visit {
      padding: 15px;
      width: 49%;
      .user-bar {
        margin: 0 auto;
        width: 100%;
        height: 300px;
      }
    }
    p{
      font-size: 14px;
      font-weight: bold;
      letter-spacing: 0px;
      color: #666;
    }
  }
  .top-table {
    width: 100%;
    .top5-cont {
      padding: 15px;
      width: 49%;
      margin-right: 2%;
      p {
        height: 26px;
        line-height: 26px;
        font-size: 14px;
        font-weight: bold;
        color: #666;
      }
    }
    .dbtop5-cont {
      padding: 15px;
      width: 49%;
      p {
        height: 26px;
        line-height: 26px;
        font-size: 14px;
        font-weight: bold;
        color: #666;
      }
    }
  }
  .service-num,.visit-pic {
    p{
      height: 26px;
      line-height: 26px;
      font-size: 14px;
      font-weight: bold;
      color: #666;
      text-align: left;
      span{
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        i{
          font-size: 10px;
        }
      }
    }
    .visiter-cont {
      padding: 15px;
      width: 49%;
      margin-right: 2%;
      p {
        height: 26px;
        line-height: 26px;
        font-size: 14px;
        font-weight: bold;
        color: #666;
      }
    }
    .dbvisiter-cont {
      padding: 15px;
      width: 49%;
      p {
        height: 26px;
        line-height: 26px;
        font-size: 14px;
        font-weight: bold;
        color: #666;
      }
    }
    .pie-pic{
      margin-left: 10%;
      width: 80%;
      height: 300px
    }
  }
  .active{
    background:#fcdd51;
    color: #fff
  }
  .left-move {
    display: inline-block;
    float: right;
    margin-right: 20px;
    width: 16px;
    height: 16px;
    line-height: 13px;
    padding-left: 2px;
    border: 1px solid #030303;
    color: #030303;
    border-radius: 50%;
    cursor: pointer;
  }
  .right-move{
    display: inline-block;
    float: right;
    width: 16px;
    height: 16px;
    line-height: 13px;
    border: 1px solid #030303;
    padding-left: 2px;
    color: #030303;
    border-radius: 50%;
    cursor: pointer;
  }
  .not-allow-move{
    border: 1px solid #b3b3b3;
    color: #b3b3b3;
    cursor: not-allowed;
  }
</style>