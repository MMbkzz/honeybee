<!-- 数据服务监控界面 -->
<template>
  <div>
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">数据资产地图</h2>
        </div>
      </div>
    </nav>
    <div class="moni-time section-bod clearfix">
      <div class="fl">
        <ul class="time-title">
          <li>时间：</li>
        </ul>
      </div>
      <div class="fl">
        <ul class="time-list clearfix">
          <li class="fl">今天</li>
          <li class="fl">昨天</li>
          <li class="fl">最近7天</li>
          <li class="fl">最近30天</li>
        </ul>
      </div>
      <div class="fl" style="margin-left: 10px; margin-top: 4px;">
        <template>
          <el-checkbox v-model="comp">对比时间段</el-checkbox>
        </template>
      </div>
    </div>
    <div class="moni-line section-bod mt-15">
      <div class="line-des">
        <ul class="clearfix">
          <li class="fl">
            <dl>
              <dt>浏览量(PV)</dt>
              <dd>33,651</dd>
            </dl>
          </li>
          <li class="fl">
            <dl>
              <dt>访客量(UV)</dt>
              <dd>5,739</dd>
            </dl>
          </li>
          <li class="fl">
            <dl>
              <dt>平均访问时长</dt>
              <dd>5,739</dd>
            </dl>
          </li>
          <li class="fl">
            <dl>
              <dt>DB执行平均时长</dt>
              <dd>5,739</dd>
            </dl>
          </li>
        </ul>
      </div>
      <div class="line-content">
        <p class="text-center">访客数（UV）、浏览量（PV）</p>
        <div class="line-pic" id="linePic"></div>
      </div>
    </div>
    <div class="top-table mt-15 clearfix">
      <div class="top5-cont fl section-bod">
        <p>Top5访问时长</p>
        <table style="width: 100%">
          <thead>
            <tr>
              <th style="width: 10%; text-align: left">排名</th>
              <th style="width: 70%; text-align: left">网站</th>
              <th style="width: 10%; text-align: left">时长</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th style="text-align: left">1</th>
              <th style="text-align: left">百度</th>
              <th style="text-align: left">3920</th>
            </tr>
            <tr>
              <th style="text-align: left">2</th>
              <th style="text-align: left">Google</th>
              <th style="text-align: left">3920</th>
            </tr>
            <tr>
              <th style="text-align: left">3</th>
              <th style="text-align: left">神马搜索</th>
              <th style="text-align: left">3920</th>
            </tr>
            <tr>
              <th style="text-align: left">4</th>
              <th style="text-align: left">搜狗</th>
              <th style="text-align: left">3920</th>
            </tr>
            <tr>
              <th style="text-align: left">5</th>
              <th style="text-align: left">其他</th>
              <th style="text-align: left">3920</th>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="dbtop5-cont fl section-bod">
        <p>DB Top5访问时长</p>
        <table style="width: 100%">
          <thead>
            <tr>
              <th style="width: 10%; text-align: left">排名</th>
              <th style="width: 70%; text-align: left">网站</th>
              <th style="width: 10%; text-align: left">时长</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th style="text-align: left">1</th>
              <th style="text-align: left">百度</th>
              <th style="text-align: left">3920</th>
            </tr>
            <tr>
              <th style="text-align: left">2</th>
              <th style="text-align: left">Google</th>
              <th style="text-align: left">3920</th>
            </tr>
            <tr>
              <th style="text-align: left">3</th>
              <th style="text-align: left">神马搜索</th>
              <th style="text-align: left">3920</th>
            </tr>
            <tr>
              <th style="text-align: left">4</th>
              <th style="text-align: left">搜狗</th>
              <th style="text-align: left">3920</th>
            </tr>
            <tr>
              <th style="text-align: left">5</th>
              <th style="text-align: left">其他</th>
              <th style="text-align: left">3920</th>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="service-num section-bod mt-15">
      <p>服务数量</p>
      <div>
        <div class="pie-pic" id="piePic"></div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  data() {
    return {
      comp: true,
      initOptions:{
        series: [{
          type: 'liquidFill',
          data: [0.6, 0.5, 0.4, 0.3]
        }]
      }
    }
  },
  mounted(){
    // this.drawLine()
    // this.drawPie()
    this.initChart()
  },
  methods: {
    initChart () {
      let _this = this;
      if (_this.chart) {
        return
      }
      _this.chart = this.$echarts.init(document.getElementById("linePic"))
      this.setChart(_this.initOptions)
    },
    setChart (options) {
      this.chart.setOption(options)
    },
    drawLine() {
      let linePic = this.$echarts.init(document.getElementById('linePic'))
      linePic.setOption({
        //  series: [{
        //     type: 'liquidFill',
        //     data: [0.6, 0.5, 0.4, 0.3],
        //     radius: '80%'
        // }]
        tooltip : {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
                backgroundColor: '#6a7985'
            }
          }
        },
        legend: {
          bottom: '12%',
          orient: 'horizontal',
          data:['浏览量(PV)','访客数(UV)']
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
          data : [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23]
        },
        yAxis: {
          type : 'value'
        },
        series : [
          {
              name:'浏览量(PV)',
              type:'line',
              itemStyle:{normal:{color:'#56abf9'}},
              areaStyle: {normal: {color: '#e5f2fe'}},
              data:[6000, 2300, 1800, 1100, 1200, 1140, 1500, 1600,1530,2100, 2000, 1800, 1900,1890,2100,2002,0,0,0,0,0,0,0,0]
          },
          {
            name:'访客数(UV)',
            type:'line',
            itemStyle:{normal:{color:'#b1df9b'}},
            areaStyle: {normal: {color: '#d3ecdc'}},
            data:[1800, 900, 500, 400, 420, 330, 340, 330, 350,300,310,290, 270,200,180,150,0,0,0,0,0,0,0,0]
          }
        ] 
      })
    },
    drawPie() {
      let piePic = this.$echarts.init(document.getElementById('piePic'))
      piePic.setOption({
        tooltip : {
          trigger: 'item',
          formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: ['启用','暂停']
        },
        series : [
          {
            name: '访问来源',
            type: 'pie',
            radius : '55%',
            center: ['50%', '60%'],
            data:[
              {
                value: 4, 
                name:'启用',
                itemStyle:{normal:{color:'#6ec71e'}}
              },
              {
                value: 8, 
                name:'暂停',
                itemStyle:{normal:{color:'#56b8ff'}}
              }
            ]
          }
        ]
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.section-bod {
  border: 1px solid #e9e9e9; 
  background: #fff; 
  box-shadow: 0px 2px 2px #ddd;
}
.moni-time {
  font-size: 14px;
  padding: 11px 0 14px 14px;
  .time-title {
    height: 28px;
    line-height: 28px;
    display: inline-block;
    color: #999;
  }
  .time-list {
    height: 28px;
    line-height: 28px;
    display: inline-block;
    border: 1px solid rgba(240,241,242,1);
    color: #666;
    li {
      padding: 0 8px;
    }
  }
  .contrast {
    display: inline-block;
  }

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
        border-right: 1px solid rgba(225,225,225,1)
      }
      li:nth-child(3)>dl{
        border-right: 1px solid rgba(225,225,225,1)
      }
    }
  }
  .line-content{
    p {
      height: 36px;
      line-height: 36px;
      font-size: 14px;
      color: #666;
    }
    .line-pic{
      margin-left: 10%;
      width: 80%; 
      height: 300px
    }
  }
}
.top-table {
  width: 100%;
  .top5-cont {
    padding: 10px;
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
    padding: 10px;
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
.service-num {
  padding: 10px;
  p{
    height: 26px;
    line-height: 26px;
    font-size: 14px;
    font-weight: bold;
    color: #666;
  }
  .pie-pic{
    margin-left: 10%;
    width: 80%; 
    height: 300px
  }
}
</style>