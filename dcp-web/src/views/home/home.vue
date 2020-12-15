<!--首页界面-->
<template>
  <div class="home-container">
    <!-- <div style="width: 890px;margin: 0 auto; padding-top: 20px;"> -->
      <!-- <div style="z-index: 11">
        <span class="home-title"><span class="little-title">指标</span>概览</span>
      </div>
      <div style="z-index: 11" class="clearfix indicators">
        <div class="fl indica-box mr-10">
          <span>资产领域数量:</span>
          <div id="fieldPic" style="width: 125px; height: 90px;"></div>
        </div>
        <div class="fl indica-box mr-10">
          <span>资产主题数量:</span>
          <div id="themePic" style="width: 125px; height: 90px;"></div>
        </div>
        <div class="fl indica-box mr-10">
          <span>数据模型数量:</span>
          <div id="modelPic" style="width: 125px; height: 90px;"></div>
        </div>
        <div class="fl indica-box mr-10">
          <span>用户数量:</span>
          <div id="userPic" style="width: 125px; height: 90px;"></div>
        </div>
        <div class="fl indica-box">
          <span>累计访问数量:</span>
          <div id="visiterPic" style="width: 125px; height: 90px;"></div>
        </div>
      </div>
      <div style="z-index: 11" class="mt-20">
        <span class="home-title"><span class="little-title">数据</span>服务平台<span> ( Data Service Platform )</span></span>
        <div class="platform clearfix">
          <div class="fl" style="width: 60%; margin-top: 40px;">
            <ul>
              <li>
                <img src="../../assets/tdspic/cluster-icon.png">
                <span>集群化：支持集群动态扩展</span>
              </li>
              <li>
                <img src="../../assets/tdspic/component-icon.png">
                <span>服务化：restful,统一入口，统一规格</span>
              </li>
              <li>
                <img src="../../assets/tdspic/service-icon.png">
                <span>组件化：可插拨式数据驱动扩展</span>
              </li>
              <li>
                <img src="../../assets/tdspic/preform.png">
                <span>高性能：负载匀衡，高效缓存</span>
              </li>
            </ul>
            <div>
              构件企业数据资产地图，实现数据资产授权与监管；借助资产服务化打通数据发布与消费集成障碍，减少系统
              间数据冗余存储，提升数据利用效率，充分发挥企业数据价值。
            </div>
          </div>
          <div class="fl" style="width: 40%">
            <img src="../../assets/tdspic/service.png">
          </div>
        </div>
      </div> -->

    <!-- </div> -->
    <!-- <div class="notice">
      <ul class="notice-tab">
        <li v-for="(item,index) in notices" :key="index" :class="{'active': index===0}"></li>
      </ul>
      <div class="notice-wrap">
        <ul class="notice-content">
          <li v-for="(item,index) in notices" :key="index"><a href="javascript:;" @click="linkToMessage">{{ item.context }}</a></li>
        </ul>
      </div>
    </div> -->
    <div class="clearfix" style="padding: 15px 0">
      <div class="fl">
        <h2 class="page-section-title">指标概览</h2>
      </div>
    </div>
    <div class="target-view clearfix">
      <!-- 资产服务数量 -->
      <div class="service source-service">
        <div class="view-card source-card">
          <div class="card-title">
            <span>资产服务数量</span>
            <h3>{{ platformCount.assetCount }}</h3>
          </div>
          <div class="card-body">
            <div v-for="(item,index) in sourceData"  :key="index"
              :class="{'card-item':true,'last-item':index === sourceData.length-1}"
              :style="{'width':(item.areaCount/platformCount.assetCount)*100 + '%'}" >
              <tool-tip-str :content="item.areaName" placement="top">
                <i slot="strWrap">
                  {{ item.areaName }}
                </i>
              </tool-tip-str>
              <span :style="{'background-color':item.bgColor}"></span>
              <span>{{ item.areaCount }}</span>
            </div>
          </div>
        </div>
        <div class="right-card animate-card clearfix">
          <div class="view-card">
            <span style="line-height:30px;font-size:12px;color:#999;width:100%;display:inline-block;text-align:left;padding:0 10px;">实例数量</span>
            <span style="line-height:60px;font-family:'SourceHanSansCN-Bold';font-size:24px;text-align:center;color:#555;width:100%;display:inline-block;">{{ platformCount.insCount }}</span>
            <!-- <div id="fieldPic" style="width:120px;height:60px;"></div>  -->
          </div>
          <div class="view-card">
            <span style="line-height:30px;font-size:12px;color:#999;width:100%;display:inline-block;text-align:left;padding:0 10px;">数据源数量</span>
            <span style="line-height:60px;font-family:'SourceHanSansCN-Bold';font-size:24px;text-align:center;color:#555;width:100%;display:inline-block;">{{ platformCount.sourceCount }}</span>
            <!-- <div id="themePic" style="width:120px;height:60px;"></div>  -->
          </div>
          <div class="view-card">
            <span style="line-height:30px;font-size:12px;color:#999;width:100%;display:inline-block;text-align:left;padding:0 10px;">服务用户数量</span>
            <span style="line-height:60px;font-family:'SourceHanSansCN-Bold';font-size:24px;text-align:center;color:#555;width:100%;display:inline-block;">{{ platformCount.appCount }}</span>
            <!-- <div id="modelPic" style="width:120px;height:60px;"></div> -->
          </div>
        </div>
      </div>
      <!-- 能力服务数量 -->
      <div class="service ability-service">
        <div class="view-card source-card">
          <div class="card-title">
            <span>能力服务数量</span>
            <h3>{{ platformCount.abilityCount }}</h3>
          </div>
          <div class="card-body">
            <div v-for="(item,index) in abilityData"
              :key="index"
              :class="{'card-item':true,'last-item':index === abilityData.length-1}"
              :style="{'width':(item.areaCount/platformCount.abilityCount)*100 + '%'}">
              <tool-tip-str :content="item.areaName" placement="top">
                <i slot="strWrap">
                  {{ item.areaName }}
                </i>
              </tool-tip-str>
              <span :style="{'background-color':item.bgColor}"></span>
              <span>{{ item.areaCount }}</span>
            </div>
          </div>
        </div>
        <div class="right-card clearfix">
          <div class="view-card">
            <h3>连接数量</h3>
            <div class="card-body">
              <div class="card-col">
                <span>最大连接数</span>
                <span>{{ platformCount.maxCount }}</span>
              </div>
              <div class="card-col">
                <span>已用连接数</span>
                <span>{{ platformCount.usedCount }}</span>
              </div>
              <div class="card-col">
                <span>剩余连接数</span>
                <span>{{ platformCount.remainCount }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="clearfix" style="padding: 15px 0">
      <div class="fl">
        <h2 class="page-section-title">数据服务平台</h2>
      </div>
    </div>
    <div class="data-service">
      <div class="view-card big-card">
        <div class="desc-wrap">
          <div class="desc-item">
            <!-- menuid 20 -->
            <a href="javascript:;" @click="routerPush('服务运维管理','/platform/clusteropera')">
              <i style="background:#FDC057"><img src="../../assets/tdspic/jiqun.png" alt=""></i>
            </a>
            <!-- <router-link to="/platform/clusteropera">

            </router-link> -->
            <div class="item-txt">
              <h4>集群化</h4>
              <span>支持集群动态扩展</span>
            </div>
          </div>
          <div class="desc-item">
            <!-- menuid 12 -->
            <!-- <router-link to="/servicemanage"> -->
            <a href="javascript:;" @click="routerPush('数据服务管理','/servicemanage')">
              <i style="background:#BAEB75"><img src="../../assets/tdspic/fuwuhua.png" alt=""></i>
            </a>
            <!-- </router-link> -->
            <div class="item-txt">
              <h4>服务化</h4>
              <span>restful，统一入口，统一规格</span>
            </div>
          </div>
          <div class="desc-item">
            <!-- menuid 19 -->
            <!-- <router-link to="/platform/drivemane"> -->
            <a href="javascript:;" @click="routerPush('服务驱动管理','/platform/drivemane')">
              <i style="background:#F96969"><img src="../../assets/tdspic/zujianhua.png" alt=""></i>
            </a>

            <!-- </router-link> -->
            <div class="item-txt">
              <h4>组件化</h4>
              <span>可插拔式数据驱动扩展</span>
            </div>
          </div>
          <div class="desc-item">
            <!-- menuid 19 -->
            <!-- <router-link to="/platform/cloudserver"> -->
            <a href="javascript:;" @click="routerPush('缓存管理','/platform/cloudserver')">
              <i style="background:#7ADFD5"><img src="../../assets/tdspic/gaoxingneng.png" alt=""></i>
            </a>
            <!-- </router-link> -->
            <div class="item-txt">
              <h4>高性能</h4>
              <span>负载匀衡，高效缓存</span>
            </div>
          </div>
        </div>
        <p>构建企业数据资产地图，实现数据资产授权与监管；借助资产服务化打通数据发布与消费集成障碍，减少系统间数据冗余存储，提升数据利用效率，充分发挥企业数据价值。</p>
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'

import ToolTipStr from '@/components/ToolTipStr'
export default {
  components:{
    ToolTipStr
  },
  data() {
    return {
      bg: '',
      actNotice: 0,
      notices: [
      ],
      sourceData: [
        {
          title: '招采',
          count: 5,
          bgColor: '#4562fc'
        },
        {
          title: '工程',
          count: 11,
          bgColor: '#FFF846'
        },
        {
          title: '商业',
          count: 23,
          bgColor: '#B3E966'
        },
        {
          title: '住宅',
          count: 15,
          bgColor: '#4EF0DF'
        },
        {
          title: '写字楼',
          count: 34,
          bgColor: '#7FB3EF'
        },
        {
          title: '数据共享',
          count: 26,
          bgColor: '#D97FF0'
        },
        {
          title: '技术平台',
          count: 30,
          bgColor: '#F85959'
        }
      ],
      abilityData: [
        {
          title: '组件',
          count: 65,
          bgColor: '#4562fc'
        },
        {
          title: '解决方案',
          count: 41,
          bgColor: '#FFF846'
        },
        {
          title: '应用接口',
          count: 22,
          bgColor: '#B3E966'
        }
      ],
      platformCount: {
        assetCount: '', // 资产服务数量
        abilityCount: '', // 能力服务数量
        insCount: '', // 实例数量
        sourceCount: '', // 数据源数量
        appCount: '', // 用户服务数量
        maxCount: '', // 最大连接数
        usedCount: '', // 已用连接数
        remainCount: '', // 剩余连接数
      },
      color: ['#4562fc','#FFF846','#B3E966','#4EF0DF','#7FB3EF','#D97FF0','#F85959'],
      colorAsset: ['#4562fc','#FFF846','#B3E966']
    }
  },
  mounted() {
    $('#app').css('display', 'block')
    this.fetchNoticeData()
    this.fetchPlatformCount()
    this.noticeAutoPlay()
    this.animateCard()
    // console.log(this.$store.state.user)
    // this.drawField()
    // this.drawTheme()
    // this.drawModel()
    // this.drawUser()
    // this.drawVisiter()
  },
  methods: {
    // 获取消息内容
    fetchNoticeData(){
      request({
        url: '/api/platform/message/query_notice',
        method: 'get'
      }).then(({data}) => {
        this.notices = data.data.list
      })
    },
    routerPush(menuName, path){
      const routes = JSON.parse(window.sessionStorage.getItem('routes'))
      let count = 0
      routes.forEach(item => {
        item.children.forEach(subItem => {
          if(subItem.name === menuName){
            count = 1
          }
        })
      })
      if(count === 1){
        this.$router.push({path: path})
      }else{
        this.$message.error('您无权限访问此页面')
      }
    },
    linkToMessage(){
      this.$router.push({ path: '/message' })
    },
    animateCard(){
      clearInterval(window.timer2)
      let i = 0;
      window.timer2 = setInterval(function(){
        $('.animate-card .view-card').css('background-position',i+'px 0');
        i--;
      },50)
    },
    noticeAutoPlay(){
      const self = this;
      clearInterval(window.timer)
      this.timer()
      $('.notice').hover(function(){
        clearInterval(window.timer)
      },function(){
        self.timer()
      })
    },
    timer(){
      const self = this
      window.timer = setInterval(function(){
        self.actNotice++;
        self.actNotice = self.actNotice === self.notices.length ? 0 : self.actNotice;
        $('.notice-tab li:eq('+self.actNotice+')').addClass('active').siblings().removeClass('active')
        $('.notice-content').animate({top:'-40px'},300,function(){
          const oLi = $('.notice-content li').first().clone()
          $('.notice-content li').first().remove()
          $('.notice-content').append(oLi).css('top', '0')
        })
      },3000)
    },
    fetchPlatformCount(){
      request({
        url: '/api/platform/home/query_index',
        method: 'get',
      }).then(({ data }) => {
        this.platformCount = {
          assetCount: data.data.assetCount,
          abilityCount: data.data.abilityCount,
          insCount: data.data.insCount,
          sourceCount: data.data.sourceCount,
          appCount: data.data.appCount,
          maxCount: data.data.maxCount,
          usedCount: data.data.usedCount,
          remainCount: data.data.remainCount
        }
        this.abilityData = data.data.abilityList
        this.sourceData = data.data.assetList
        for(var i=0; i<this.abilityData.length; i++){
          this.abilityData[i].bgColor = this.colorAsset[i]
        }
        for(var i=0; i<this.sourceData.length; i++){
          this.sourceData[i].bgColor = this.color[i]
        }
      })
    },
    drawField() {
      let fieldPic = this.$echarts.init(document.getElementById('fieldPic'))
      fieldPic.setOption({
        series: [{
            type: 'liquidFill',
            name: 'Liquid Fill',
            radius: '80%',
            data: [{
              value: '0.5',
              direction: 'left',
              itemStyle: {
                normal: {
                  color: '#6acdfd'
                }
              }
            }],
            outline: {
              show: true,
              itemStyle: {
                borderColor: '#6acdfd',
                borderWidth: 2,
              }
            },
            label: {
              formatter: function(param) {
                return param.value*100;
              },
              fontSize: 16,
              fontWeight: 'bold',
              align: 'center',
            },
        }]
      })
    },
    drawTheme() {
      let themePic = this.$echarts.init(document.getElementById('themePic'))
      themePic.setOption({
        series: [{
            type: 'liquidFill',
            name: 'Liquid Fill',
            radius: '80%',
            data: [{
              value: '0.5',
              direction: 'left',
              itemStyle: {
                normal: {
                  color: '#4cdf63'
                }
              }
            }],
            outline: {
              show: true,
              itemStyle: {
                borderColor: '#4cdf63',
                borderWidth: 2,
              }
            },
            label: {
              formatter: function(param) {
                return param.value*100;
              },
              fontSize: 16,
              fontWeight: 'bold',
              align: 'center',
            },
        }]
      })
    },
    drawModel() {
      let modelPic = this.$echarts.init(document.getElementById('modelPic'))
      modelPic.setOption({
        series: [{
            type: 'liquidFill',
            name: 'Liquid Fill',
            radius: '80%',
            data: [{
              value: '0.5',
              direction: 'left',
              itemStyle: {
                normal: {
                  color: '#76a0ff'
                }
              }
            }],
            outline: {
              show: true,
              itemStyle: {
                borderColor: '#76a0ff',
                borderWidth: 2,
              }
            },
            label: {
              formatter: function(param) {
                return param.value*100;
              },
              fontSize: 16,
              fontWeight: 'bold',
              align: 'center',
            },
        }]
      })
    },
    drawUser() {
      let userPic = this.$echarts.init(document.getElementById('userPic'))
      userPic.setOption({
        series: [{
            type: 'liquidFill',
            name: 'Liquid Fill',
            radius: '80%',
            data: [{
              value: '0.5',
              direction: 'left',
              itemStyle: {
                normal: {
                  color: '#ff6d4b'
                }
              }
            }],
            outline: {
              show: true,
              itemStyle: {
                borderColor: '#ff6d4b',
                borderWidth: 2,
              }
            },
            label: {
              formatter: function(param) {
                return param.value*100;
              },
              fontSize: 16,
              fontWeight: 'bold',
              align: 'center',
            },
        }]
      })
    },
    drawVisiter() {
      let visiterPic = this.$echarts.init(document.getElementById('visiterPic'))
      visiterPic.setOption({
        series: [{
            type: 'liquidFill',
            name: 'Liquid Fill',
            radius: '80%',
            data: [{
              value: '0.5',
              direction: 'left',
              itemStyle: {
                normal: {
                  color: '#42e9e1'
                }
              }
            }],
            outline: {
              show: true,
              itemStyle: {
                borderColor: '#42e9e1',
                borderWidth: 2,
              }
            },
            label: {
              formatter: function(param) {
                return param.value*100;
              },
              fontSize: 16,
              fontWeight: 'bold',
              align: 'center',
            },
        }]
      })
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.text-box{
  width: 180px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  height: 40px;
  line-height: 40px;
  margin-bottom: 40px;
}
.home-container{
  // background: url('../../assets/tdspic/homegj.jpg') no-repeat;
  // background: #368ae4;
  background-size: 100%;
  width: 100%;
  top: 50px;
  bottom: 0;
  right: 0;
  .view-card{
    background-color: #ffffff;
    box-shadow: 0px 2px 1px 0px
      rgba(0, 0, 0, 0.08);
    border-radius: 5px;
    overflow: hidden;
    color: #999;
    span{
      font-size: 12px;
    }
  }
  .page-section-title{
    font-size: 18px;
    color: #999;
    border-left: solid 3px #9c9c9c;
    padding-left: 10px;
    float: left;
  }
  .notice{
    height: 40px;
    line-height: 40px;
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    border: 1px solid #4562fc;
    background: rgba(252,185,69,.1);
    .notice-tab{
      padding: 0 10px;
      li{
        display: inline-block;
        width: 12px;
        height: 12px;
        border-radius: 50%;
        margin: 0 2px;
        background:  rgba(252,185,69,.5);
        &.active{
          background: #f2a31c;
        }
      }
    }
    .notice-wrap{
      flex-grow: 1;
      overflow: hidden;
      color: #f2a31c;
      position: relative;
      .notice-content{
        position: absolute;
        li{
          a{
            &:hover{
              text-decoration: underline;
            }
          }
        }
      }
    }
  }
  .target-view{
    .service{
      height: 90px;
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      margin-bottom: 20px;
      .source-card{
        height: 90px;
        flex-grow: 1;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        .card-title{
          height: 100%;
          width: 128px;
          display: flex;
          flex-direction: row;
          justify-content: center;
          align-items: center;
          flex-wrap: wrap;
          text-align: center;
          border-right: 1px solid #eeeeee;
          background: #FDFDFD;
          padding: 20px 0;
          span{
            width: 100%;
          }
          h3{
            font-size: 30px;
            font-weight: bold;
            color: rgb(44, 58, 121);
          }
        }
        .card-body{
          flex-grow: 1;
          display: flex;
          flex-direction: row;
          justify-content: center;
          align-items: center;
          height: 100%;
          padding: 20px 15px;
          .card-item{
            height: 100%;
            display: flex;
            min-width: 20px;
            flex-direction: column;
            justify-content: space-between;
            align-items: center;
            span{
              display: inline-block;
              width: 100%;
              text-align: center;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }
            span:nth-child(1){
              border-right: 1px solid #eee;
              flex-grow: 0.5;
              i{
                font-size: 12px;
              }
            }
            span:nth-child(2){
              height: 10px;
            }
            span:nth-child(3){
              font-size: 14px;
              color: #555;
              font-weight: bold;
              flex-grow: 0.5;
              display: flex;
              flex-direction: column;
              justify-content: center;
            }
            &:nth-child(1){
              span:nth-child(2){
                border-radius: 5px 0 0 5px;
              }
            }
            &.last-item{
              span:nth-child(2){
                height: 10px;
                border-radius: 0 5px 5px 0;
              }
              span:nth-child(1){
                border-right: none;
              }
            }
          }
        }
      }
      .right-card{
        .view-card{
          width: 120px;
          height: 90px;
          margin-left: 15px;
          float: left;
          text-align: center;
        }
        &.animate-card{
          .view-card{
            &:nth-child(1){
              background: url('../../assets/tdspic/1.png')
            }
            &:nth-child(2){
              background: url('../../assets/tdspic/2.png')
            }
            &:nth-child(3){
              background: url('../../assets/tdspic/3.png')
            }
          }
        }
      }
    }
    .ability-service{
      .right-card{
        .view-card{
          width: 390px;
          height: 90px;
          margin-left: 15px;
          float: left;
          h3{
            height: 35px;
            width: 100%;
            line-height: 35px;
            border-bottom: 1px solid #eee;
            background: #fdfdfd;
            text-align: center;
            font-size: 12px;
          }
          .card-body{
            display: flex;
            flex-direction: row;
            justify-content: space-between;
            align-items: center;
            height: 55px;
            .card-col{
              height: 35px;
              width: 100%;
              border-right: 1px solid #eee;
              display: flex;
              flex-direction: column;
              justify-content: space-between;
              align-items: center;
              font-size: 12px;
              &:last-child{
                border-right: none;
              }
              span{
                color: #999;
              }
              span:nth-child(2){
                font-size: 16px;
                color: #555;
                font-weight: bold;
              }
            }
          }
        }
      }
    }

  }
  .data-service{
    padding-bottom: 30px;
    .big-card{
      width: 100%;
      height: 400px;
      // background: #ffffff url('../../assets/tdspic/big-card-bg.png') no-repeat right top;
      display: flex;
      flex-direction: column;
      justify-content: center;
      padding-left: 30px;
      .desc-wrap{
        width: 500px;
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        justify-content: space-between;
        .desc-item{
          width: 240px;
          height: 55px;
          border-radius: 5px;
          overflow: hidden;
          margin-bottom: 16px;
          i{
            display: flex;
            height: 100%;
            width: 55px;
            float: left;
            flex-direction: row;
            justify-content: center;
            align-items: center;
          }
          .item-txt{
            width: 184px;
            height: 100%;
            border: 1px solid #e9e9e9;
            border-left: none;
            border-radius: 0 5px 5px 0;
            padding-left: 10px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            h4{
              color: #555;
              font-size: 14px;
              margin-bottom: 5px;
            }
            span{
              color: #999;
              font-size: 12px;
            }
          }
        }
      }
      p{
        width: 500px;
        color: #555;
        font-size: 14px;
        line-height: 28px;
      }
    }
  }
}
.indicators {
  margin-top: 10px;
  span{
    color: #fff;
    font-size: 16px;
  }
  .indica-box {
    padding: 10px 20px;
    width: 170px;
    height: 135px;
    border: 1px solid rgba(251, 255, 52, 0.3);
    border-radius: 8px;
  }
}
.platform {
  margin-top: 10px;
  padding-left: 30px;
  width: 890px;
  border: 1px solid rgba(251, 255, 52, 0.3);
  border-radius: 8px;
  color: #ffffff;
  font-size: 16px;
  ul {
    margin-bottom: 20px;
    li {
      color: rgba(255,255,255,0.9);
      font-size: 16px;
      height: 25px;
      line-height: 25px;
      img {
        vertical-align: middle;
        margin-right: 6px;
      }
    }
  }
}
.home-title{
  display: inline-block;
  color: #fff;
  font-size: 22px;
  .little-title {
    padding-bottom: 10px;
    display: inline-block;
    font-size: 22px;
    border-bottom: 2px solid #42e9e1;
  }
}
</style>
