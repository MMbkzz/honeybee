<template>
  <el-row :gutter="25" class="card-wrap clearfix">
    <el-col :span="8"  v-for="(item,index) in cardData" :key="index">
      <div class="card" @click="handleView(item)">
        <ul class="card-info">
          <li :class="{'card-header':subIndex===0}" v-for="(subItem,subIndex) in fieldsData" :key="subIndex">
            <div v-if="subIndex===0" class="role-code">
              <span>{{subItem.propTitle}}：{{evalParse(index,subItem.field)}}</span>
            </div>
            <div v-if="subIndex===0" class="operate-btn">
              <a title="编辑" @click.stop="handleUpdate(item)" :style="{'cursor':operatePermission?'pointer':'not-allowed','opacity':operatePermission?'1':'0.6'}"><img src="../../assets/tdspic/edit.png" alt=""></a>
              <a title="删除" @click.stop="handleDelete(item)" :style="{'cursor':operatePermission?'pointer':'not-allowed','opacity':operatePermission?'1':'0.6'}"><img src="../../assets/tdspic/delete.png" alt=""></a>
              <!-- <a title="改密" @click.stop="updatePwd(item)"><svg-icon icon-class="changepwd" style="width: 20px; height: 20px"></svg-icon></a> -->
            </div>
            <span v-if="subIndex!==0">{{subItem.propTitle}}</span>
            <crland-switch :operatePermission="operatePermission" v-if="subItem.switch" :objBind="item" :status="statusParse(index,subItem.field)" @handleClick="handleClick"></crland-switch>
            <span v-if="subIndex!==0&&!subItem.switch">{{evalParse(index,subItem.field)}}</span>
          </li>
        </ul>
      </div>
    </el-col>
    <!-- <div class="card last-card" v-for="(item,index) in hideCards" :style="{'margin-right':index === (hideCards.length-1)?'0':'25px','flex-grow':cardFlexGrow}"></div> -->
  </el-row>
</template>


<script>
import { windowResize } from '@/utils'
import CrlandSwitch from '@/components/CrlandSwitch'
import ToolTipStr from '@/components/ToolTipStr'
export default {
  name: 'CrCard',
  components: {
    CrlandSwitch
  },
  data() {
    return {
      hideCards: [], //补充的透明盒子
      crossNum: 0,
      newWidth: 0,
      total: 0
    }
  },
  created() {
      // this.slideIn()
    // this.$store.commit('SET_COUNT_NUM', this.total)
  },
  mounted() {
    const newSize = windowResize('.card-wrap',350,215,25)
    // console.log(newSize)
    this.crossNum = newSize.crossNum
    this.total = newSize.total
    this.newWidth = newSize.newWidth
    console.log(this.hideCards)
    $('.card-wrap').resize(function(e){
      console.log(e)
    })
  },
  computed: {
    // myWidth() {
    //     const obj = windowResize('.card-wrap',350,215,25)
    //     const newWidth = obj.newWidth - 40
    //     return newWidth + 'px'
    // },
    // myHeight() {
    //     const obj = screenResize(350,215)
    //     const newHeight = obj.newHeight - 40
    //     return newHeight + 'px'
    // },
    // total() { // 页面能存放的个数
    //     const total = screenResize(350,215).total
    //     return total
    // }
  },
  props:{
    cardData: {
      type: Array,
      default: function(){
        return []
      }
    },
    fieldsData: {
      type: Array,
      default: function () {
        return []
      }
    },
    operatePermission: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    handleUpdate(obj){
      if(this.operatePermission){
        this.$emit('edit',obj)
      }
    },
    handleDelete(obj){
      if(this.operatePermission){
        this.$emit('delete',obj)
      }
    },
    handleView(obj){
      this.$emit('browse',obj)
    },
    // 启用禁用开关
    handleClick(val){
      if(this.operatePermission){
        this.$emit('handleSwitchClick',val);
      }
    },
    // 修改密码
    updatePwd(val){
      this.$emit('editPwd',val)
    },
    evalParse(index,field){
      let str='this.cardData['+index+'].'+field
      str = eval(str)
      return str
    },
    statusParse(index,field){
      let str='this.cardData['+index+'].'+field
      let status;
      str = eval(str)
      if(str === '1' || str==='active'){
        status = true;
      }else if(str === '0'|| str==='inactive'){
        status = false
      }
      return status
    }
  }
}
</script>
<style lang="scss" scoped>
.card-wrap{
  // display: flex;
  // flex-direction: row;
  // justify-content: space-between;
  // flex-wrap: wrap;
  .card{
    margin-bottom: 20px;
    float: left;
    width: 100%;
    box-shadow: 0px 2px 1px 0px
      rgba(0, 0, 0, 0.08);
    border-radius: 5px;
    overflow: hidden;
    border: 1px solid transparent;
    cursor: pointer;
    transition: border-color 0.3s;
    &.last-card{
      height: 0;
      visibility: hidden;
      margin: 0;
    }
    &:hover{
      border-color: #4562fc;
    }
    .card-info{
      background: #fff;
      .card-header{
        height: 40px;
        line-height: 40px;
        background: #fcfcfc;
        padding: 0 15px;
        .role-code{
          font-size: 12px;
          color: #999;
          float: left;
        }
        .operate-btn{
          float: right;
          a {
            img{
              vertical-align: text-top;
            }
          }
        }
      }
      li{
        border-top: 1px solid #eee;
        width: 100%;
        height: 40px;
        line-height: 40px;
        padding: 0 15px;
        font-size: 12px;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        span{
          &:nth-of-type(1){
            color: #999;
            font-size: 12px;
          }
          &:nth-of-type(2){
            font-size: 12px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            width: 80%;
            text-align: right;
            color: #555;
          }
        }
      }
    }
  }
}
</style>
