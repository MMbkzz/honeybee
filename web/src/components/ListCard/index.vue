<template>
  <div class="list-wrap">

    <div class="list-card" :style="{width:cardWidth,height:header?'125px':'auto'}" v-for="(item,index) in listData" :key="index" @click="handleView(item)">
      <div class="card-header" v-if="header">
        <!-- <div class="update-user-time">
          <span class="update-user">更新用户：{{ item.updateUser }}</span>
          <span class="update-time">更新时间：{{ item.updateTime }}</span>
        </div> -->
        <div class="operate-btn">
          <!-- <el-tooltip class="item" v-if="isEdit" effect="light" :content="operatePermission?'编辑':'您无权限进行此操作，请联系管理员'" placement="top"> -->
            <a title="编辑" v-if="isEdit" @click.stop="handleUpdate(item)" :style="{'cursor':operatePermission?'pointer':'not-allowed','opacity':operatePermission?'1':'0.6'}"><img src="../../assets/tdspic/edit.png" alt=""></a>
          <!-- </el-tooltip> -->
          <!-- <el-tooltip class="item" v-if="isDel" effect="light" :content="operatePermission?'删除':'您无权限进行此操作，请联系管理员'" placement="top"> -->
            <a title="删除" v-if="isDel" @click.stop="handleDelete(item.id)" :style="{'cursor':operatePermission?'pointer':'not-allowed','opacity':operatePermission?'1':'0.6'}"><img src="../../assets/tdspic/delete.png" alt=""></a>
          <!-- </el-tooltip> -->
        </div>
      </div>

      <div class="card-content">
        <!-- <div class="card-col" v-for="(subItem,subIndex) in fieldsData" v-show="!subItem.show==='no'" :key="subIndex"> -->
        <div class="card-col" v-for="(subItem,subIndex) in fieldsData" :key="subIndex">
          <div class="card-icon-wrap" v-if="subIndex === 0 && cardImg !== ''">
            <img :src="require('../../assets/tdspic/'+cardImg)" alt="">
          </div>
          <div class="card-icon-wrap" v-if="subIndex === 0 && (item.picPath !== undefined && item.picPath !== '')">
            <img :src="require('../../assets/tdspic/'+item.picPath)" alt="">
          </div>
          <div class="card-col-desc">
            <h3>{{ subItem.propsTitle }}</h3>
            <crland-switch @beforeChange="beforeSwitchChange" :operatePermission="operatePermission" v-if="subItem.switch" :objBind="item" :status="statusParse(index,subItem.field)" @handleClick="handleClick"></crland-switch>
            <!-- <el-tooltip v-if="subItem.toolTip" class="item" effect="dark" :content="evalParse(index,subItem.field)" placement="top">
              <span>
                  {{ evalParse(index,subItem.field) }}
                <img v-if="evalParse(index,subItem.field)==='已发布'" src="../../assets/tdspic/success.png">
                <img v-if="evalParse(index,subItem.field)==='待发布'" src="../../assets/tdspic/wait.png">
                <img v-if="evalParse(index,subItem.field)==='重发布'" src="../../assets/tdspic/reply.png">
              </span>
            </el-tooltip> -->
            <tool-tip-str v-if="!subItem.switch" :content="evalParse(index,subItem.field)+''" >
              <i slot="strWrap">
                {{ evalParse(index,subItem.field) }}
                <img v-if="evalParse(index,subItem.field)==='已发布'" src="../../assets/tdspic/success.png">
                <img v-if="evalParse(index,subItem.field)==='待发布'" src="../../assets/tdspic/wait.png">
                <img v-if="evalParse(index,subItem.field)==='重发布'" src="../../assets/tdspic/reply.png">
              </i>
            </tool-tip-str>
            <!-- <span v-if="!subItem.switch&&!subItem.toolTip">
              {{ evalParse(index,subItem.field) }}
              <img v-if="evalParse(index,subItem.field)==='已发布'" src="../../assets/tdspic/success.png">
              <img v-if="evalParse(index,subItem.field)==='待发布'" src="../../assets/tdspic/wait.png">
              <img v-if="evalParse(index,subItem.field)==='重发布'" src="../../assets/tdspic/reply.png">
            </span> -->
          </div>
        </div>

        <div class="card-col operation-col" style="justify-content: start;" v-if="isOperation">
          <div class="card-col-desc">
            <h3>操作</h3>
            <div class="operation-btn-wrap">
              <el-button v-for="(btn,btnIndex) in operationBtns" :key="btnIndex" :disabled="btn.isControl?!operatePermission:false" :type="btn.type" size="mini" @click.stop="handleOperate(btn,item)">{{ btn.text }}</el-button>
            </div>
          </div>
        </div>

        <div class="card-col diy-col" v-if="isDiy">
          <div class="card-col-desc">
            <h3 class="clearfix"><span>已使用</span><span>剩余</span></h3>
            <div class="diy-wrap">
              <div class="progress-bar used" :style="{width:calcu(item.maxConnections, item.usedNumber)}">{{ item.usedNumber }}</div>
              <div class="progress-bar surplus">{{ item.remainNumber }}</div>
            </div>
          </div>
        </div>
      </div>

      <div class="card-footer" v-if="footer">
        <!-- <el-collapse-transition> -->
          <div class="footer-box">
            <div class="footer-content">
              <slot name="footer" :data="item"></slot>
            </div>
          </div>
        <!-- </el-collapse-transition> -->
        <div class="show-hide"><span @click.stop="test(index)"><svg-icon icon-class="spread"></svg-icon></span></div>
      </div>
    </div>
  </div>
</template>

<script>
import { stateTransition } from '@/filters'
import { screenResize } from '@/utils'
import CrlandSwitch from '@/components/CrlandSwitch'
import ToolTipStr from '@/components/ToolTipStr'

export default {
  name: 'list-card',
  components: {
    CrlandSwitch,
    ToolTipStr,
  },
  data(){
    return {
      usedNum: 5,
      footerShow: false,
      showIndex: -1,
      showStatus: 'show',
      loading: true
    }
  },
  props: {
    loadingText: {
      type: String
    },
    operatePermission: {
      type: Boolean,
      default: true
    },
    listData: {
      type: Array,
      default: function(){
        return []
      }
    },
    fieldsData: {
      type: Array,
      default: function(){
        return []
      }
    },
    isOperation:{
      type: Boolean,
      default: false
    },
    isDiy:{
      type: Boolean,
      default: false
    },
    operationBtns:{
      type: Array,
      default: function () {
        return []
      }
    },
    cardImg: {
      type: String,
      default: ''
    },
    cardWidth: {
      type: String,
      default: '100%'
    },
    header: {
      type: Boolean,
      default: true
    },
    footer: {
      type: Boolean,
      default: false
    },
    isEdit: {
      type: Boolean,
      default: true
    },
    isDel: {
      type: Boolean,
      default: true
    }
  },
  created() {
    // console.log(this.listData)
    // debugger
  },
  mounted() {
    this.$emit('mounted')
  },
  computed: {
  },
  methods: {
    beforeSwitchChange(val){
      this.$emit('beforeSwitchChange',val)
    },
    test(index){
      this.showIndex = index
      const self = this
      // console.log($('.list-card:eq('+index+')').find('.svg-icon').hasClass('retract'))
      const footerBox = $(self.$el).find('.footer-box:eq('+ index +')')
      // console.log($(self.$el).find('.list-card .retract').parents('.list-card').index())
      const showingBoxIndex = $(self.$el).find('.list-card .retract').parents('.list-card').index()
      const showingBox = showingBoxIndex === -1 ? null : $(self.$el).find('.footer-box:eq(' + showingBoxIndex + ')')
      const footer_h = $(self.$el).find('.footer-box:eq('+ index +')').find('.footer-content').height()
      if(!$(self.$el).find('.list-card:eq('+index+')').find('.svg-icon').hasClass('retract')){
        this.showStatus = 'show'
        if(showingBox !== null){
          showingBox.parents('.list-card').find('.svg-icon').removeClass('retract')
          showingBox.stop().animate({'height':0},300)
        }
        footerBox.stop().animate({'height':footer_h + 'px'},300)
      }else {
        footerBox.stop().animate({'height':0},300,function(){
          self.showStatus = ''
        })
      }
      $(self.$el).find('.list-card:eq('+index+')').find('.svg-icon').toggleClass('retract')
    },
    calcu(max,used) { //计算比例
      max = max === 0?1:max
      const scaleNum = used / max * 100 + '%'
      return scaleNum
    },
    handleOperate(operateBtn,operateObj){
      if(operateBtn.isControl){
        if(this.operatePermission){
          const obj = {
            operateName:operateBtn.text,
            operateObj
          }
          this.$emit('handleOperate',obj)
        }
      }else{
        const obj = {
          operateName:operateBtn.text,
          operateObj
        }
        this.$emit('handleOperate',obj)
      }
    },
    handleUpdate(obj){
      if(this.operatePermission){
        this.$emit('edit',obj)
      }
    },
    handleDelete(id){
      if(this.operatePermission){
        this.$emit('delete',id)
      }
    },
    handleView(obj){
      if(obj.appUsers){
        if(obj.appUsers.length > 0){
          this.$emit('browse',obj, 'authorized')
        }else {
          this.$emit('browse',obj, 'nouthorize')
        }
      }else {
        this.$emit('browse',obj)
      }
    },
    handleClick(val){
      this.$emit('handleSwitchClick',val);
    },
    tooltipShow(str){
      let isLong = false
      const oSpan = $('<span style="display:none"></span>')
      oSpan.text(str)
      $('body').append(oSpan)
      // isLong = oSpan.width()>
      return false
    },
    evalParse(index,field){
      let str='this.listData['+index+'].'+field
      str = eval(str)
      if(field==='statusCode'){
        if(str === 'initialized'){
          str = '待发布'
        }else if(str === 'published'){
          str = '已发布'
        }else if(str === 'rebalance'){
          str = '重发布'
        }else if(str === 'enabled'){
          str = '启动'
        }else if(str === 'disabled'){
          str = '禁用'
        }else if(str === 'unknown'){
          str = '未知'
        }else if(str === 'normal'){
          str = '正常'
        }else if(str === 'stopping'){
          str = '待停止'
        }else if(str === 'stopped'){
          str = '停止'
        }
      }else if(field==='operateType'){
        if(str === 'write'){
          str = '写'
        }else if(str === 'read'){
          str = '读'
        }
      }
      if(str instanceof Array){
        str = str.join(',')
      }
      return str
    },
    statusParse(index,field){
      let str='this.listData['+index+'].'+field
      let status;
      str = eval(str)
      if(str === '1' || str==='active' || str==='enabled'){
        status = true;
      }else if(str === '0'|| str==='inactive' || str==='disabled'){
        status = false
      }
      return status
    }
  }
}
</script>
<style lang="scss" scoped>
.list-wrap{
  width: 100%;
  text-align: left;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: space-between;
  .list-card{
    margin-bottom: 20px;
    width: 100%;
    background: white;
    box-shadow: 0px 2px 1px 0px
      rgba(0, 0, 0, 0.08);
    border-radius: 5px;
    overflow: hidden;
    height: 125px;
    transition: all 0.3s;
    cursor: pointer;
    border: 1px solid transparent;
    &:hover{
      border-color: #9eacf0;
    }
    .card-header{
      height: 35px;
      line-height: 35px;
      padding: 0 20px;
      background: #FCFCFC;
      border-bottom: 1px solid #f0f0f0;
      .update-user-time{
        height: 35px;
        float: left;
        span{
          font-size: 12px;
          display: inline-block;
          color: #9c9c9c;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          &:nth-child(2){
            width: 180px;
          }
        }
      }
      .operate-btn{
        float: right;
        a {
          display: flex;
          float: left;
          margin-left: 10px;
          height: 35px;
          flex-direction: column;
          justify-content: center;
          img{
            vertical-align: text-top;
          }
        }
      }
    }
    .card-content{
      width: 100%;
      height: 90px;
      box-sizing: border-box;
      padding: 20px;
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      font-size: 14px;
      color: #656565;
      .card-col{
        padding-left: 20px;
        border-left: 1px solid #efefef;
        width: 100%;
        overflow: hidden;
        &:first-child{
          border: none;
          padding-left: 0;
        }
        .card-icon-wrap{
          width: 50px;
          height: 50px;
          float: left;
          overflow: hidden;
          display: flex;
          flex-direction: row;
          justify-content: center;
          align-items: center;
          border: 2px solid #666;
          border-radius: 50%;
          margin-right: 10px;
        }
        .card-col-desc{
          height: 100%;
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          h3{
            font-size: 12px;
            color: #999;
            span{
              font-size: 12px;
              color: #999;
              &:nth-child(2){
                float: right;
                margin-right: 10px;
              }
            }
          }
          span{
            display: inline-block;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            color: #555;
            line-height: 17px;
            img{
              width: 16px;
              float: none;
              position: relative;
              top: 2px;
            }
          }

        }
      }
      .operation-col{
        min-width: 160px;
        .operation-btn-wrap{
          .el-button{
            margin-right: 10px;
            padding: 5px 12px;
            height: 23px;
          }
          .el-button:last-child{
            margin-right: 0;
          }
        }
      }
      .diy-col{
        min-width: 150px;
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
            }
            &.surplus{
              right: 0;
              text-align: right;
              font-size: 12px;
              padding-right: 10px;
            }
          }
        }
      }
    }
    .card-footer{
      width: 100%;
      .footer-box{
        height: 0;
        overflow: hidden;
      }
      .show-hide{
        width: 100%;
        text-align: center;
        height: 20px;
        border-top: 1px solid #F0F0F0;
        span{
          color: white;
          background: #999;
          display: inline-block;
          width: 50px;
          height: 25px;
          line-height: 18px;
          font-size: 11px;
          position: relative;
          top: 2px;
          border-radius: 25px 25px 0 0;
        }
        .retract{
          transform: rotate(180deg)
        }
      }
      .footer-content{
        width: 100%;
        border-top: 1px solid rgb(240, 240, 240);
        color: rgb(153, 153, 153);
        text-align: center;
        font-size: 14px;
      }
    }
  }
}
</style>
