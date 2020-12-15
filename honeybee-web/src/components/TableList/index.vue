<template>
  <div class="table-wrap">
    <div class="table-header">
      <div class="header-col" v-for="(fieldItem,title_index) in fieldsData" :key="title_index">
        <h3>{{ fieldItem.propsTitle }}</h3>
      </div>
      <div class="header-col" v-if="isOperation">
        <h3>操作</h3>
      </div>
    </div>
    <div class="list-card" v-for="(item,index) in listData" :key="index" @click="handleView(item)">
      <div class="card-header">
        <div class="update-user-time">
          <span class="update-user">更新用户：{{ item.updateUser }}</span>
          <span class="update-time">更新时间：{{ item.updateTime }}</span>
        </div>
        <div class="operate-btn">
          <a v-if="isEdit" title="编辑" @click.stop="handleUpdate(item)"><img src="../../assets/tdspic/edit.png" alt=""></a>
          <a title="删除" @click.stop="handleDelete(item)"><img src="../../assets/tdspic/delete.png" alt=""></a>
        </div>
      </div>
      <div class="list-content">
        <div class="list-col" v-for="(subItem,subIndex) in fieldsData" :key="subIndex">
          <crland-switch v-if="subItem.switch" :objBind="item" :status="statusParse(index,subItem.field)" @handleClick="handleClick"></crland-switch>
          <!-- <el-tooltip :disabled="contentOverflow(subIndex)" effect="dark" :content="evalParse(index,subItem.field)" placement="top">

          </el-tooltip> -->
          <span v-else>{{ evalParse(index,subItem.field) }}
            <img v-if="evalParse(index,subItem.field)==='已发布'" src="../../assets/tdspic/success.png">
            <img v-if="evalParse(index,subItem.field)==='待发布'" src="../../assets/tdspic/wait.png">
          </span>
        </div>
        <div class="list-col operation-col" style="justify-content: start;" v-if="isOperation">
          <slot name="operation"></slot>
          <el-button v-for="(btn,btnIndex) in operationBtns" :key="btnIndex" :type="btn.type" size="mini" @click.stop="handleOperate(btn.text,item)">{{ btn.text }}</el-button>
        </div>
        <!-- <div class="list-col">
          <h3>驱动名称</h3>
          <span>mysql_01</span>
        </div>
        <div class="list-col">
          <h3>驱动版本</h3>
          <span>1.0</span>
        </div>
        <div class="list-col">
          <h3>状态</h3>
          <span>已发布</span>
        </div>
        <div class="list-col">
          <h3>最大连接数</h3>
          <span>12</span>
        </div> -->
      </div>
    </div>
  </div>
</template>

<script>
import { stateTransition } from '@/filters'
import CrlandSwitch from '@/components/CrlandSwitch'

export default {
  name: 'table-list',
  components: {
    CrlandSwitch
  },
  props: {
    listData: {
      type: Array,
      default: function () {
        return []
      }
    },
    fieldsData: {
      type: Array,
      default: function () {
        return []
      }
    },
    isOperation:{
      type: Boolean,
      default: false
    },
    operationBtns:{
      type: Array,
      default: function () {
        return []
      }
    },
    isEdit: {
      type: Boolean,
      default: true
    }
  },
  mounted() {
    console.log(this.listData)
    // this.listData.forEach(item => {
    //   console.log(item.statusCode)
    // })
  },
  created() {

  },
  computed: {
  },
  methods: {
    contentOverflow(index){
      // console.log($('.header-col:eq('+index+') h3').width(),$('.list-col:eq('+index+') span').width())
    },
    handleOperate(operateName,operateObj){
      const obj = {
        operateName,
        operateObj
      }
      this.$emit('handleOperate',obj)
    },
    handleClick(val){
      this.$emit('handleSwitchClick',val);
    },
    handleUpdate(obj){
      this.$emit('edit',obj)
    },
    handleDelete(obj){
      this.$emit('delete',obj)
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
    evalParse(index,field){
      let str='this.listData['+index+'].'+field
      str = eval(str)
      if(field==='statusCode'){
        if(str === 'initialized' || str === 'inactive'){
          str = '待发布'
        }else if(str === 'published' || str === 'active'){
          str = '已发布'
        }
      }
      return str
    },
    statusParse(index,field){
      let str='this.listData['+index+'].'+field
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
.table-wrap{
  width: 100%;
  text-align: left;
  .table-header {
    width: 100%;
    background: white;
    box-shadow: 0px 4px 3px 0px
      rgba(0, 0, 0, 0.08);
    border-radius: 5px;
    overflow: hidden;
    height: 50px;
    padding: 0 20px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    .header-col{
      padding-left: 20px;
      line-height: 50px;
      width: 24%;
      &:first-child{
        border: none;
        padding-left: 0;
      }
      h3{
        font-size: 14px;
        color: #999;
      }
    }
  }
  .list-card{
    margin-top: 20px;
    width: 100%;
    background: white;
    box-shadow: 0px 4px 3px 0px
      rgba(0, 0, 0, 0.08);
    border-radius: 5px;
    overflow: hidden;
    height: 88px;
    transition: all 0.3s;
    cursor: pointer;
    border: 1px solid transparent;
    &:hover{
      border-color: #4562fc;
    }
    .card-header{
      padding: 0 20px;
      border-bottom: 1px solid #F0F0F0;
      height: 35px;
      line-height: 35px;
      background: #FCFCFC;
      .update-user-time{
        float: left;
        span{
          font-size: 12px;
          display: inline-block;
          width: 200px;
          color: #999;
        }
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
    .list-content{
      width: 100%;
      box-sizing: border-box;
      padding: 0 20px;
      height: 54px;
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      font-size: 14px;
      color: #656565;
      .list-col{
        position: relative;
        padding-left: 20px;
        border-left: 1px solid #F0F0F0;
        line-height: 53px;
        width: 24%;
        display: flex;
        flex-direction: column;
        justify-content: center;
        span{
          color: #333;
          font-size: 14px;
          overflow: hidden;
          text-overflow:ellipsis;
          position: absolute;
          height: 100%;
          right: 10px;
          left: 20px;
          white-space: nowrap;
          img{
            width: 16px;
            float: none;
            position: relative;
            top: 2px;
            }
        }
        &:first-child{
          border: none;
          padding-left: 0;
        }
        h3{
          font-size: 12px;
          color: #999;
        }
        .el-button--mini{
          width: 50px;
          white-space: nowrap;
          span{
            font-size: 12px;
          }
        }
      }
      .operation-col{
        flex-direction: row;
        align-items:center;
      }
    }
  }
}
</style>
