<template>
  <div class="fold-table-wrap" id="fold-table-wrap">
    <div class="table-header">
      <div class="table-line">
        <div class="item"></div>
        <div class="item">资源名称</div>
        <div class="item">状态</div>
        <div class="item">操作</div>
      </div>
    </div>
    <div class="table-content">
      <div :class="{'expand-line':true, 'expanded':allExpand}" v-for="(item,index) in data" :key="index" v-show="item.descr!=='首页' && item.descr!=='消息中心'">
        <div class="table-line" @click.stop="control(index)">
          <div class="item">
            <svg-icon icon-class="zhankai"></svg-icon>
            <svg-icon icon-class="shouqi"></svg-icon>
            <svg-icon icon-class="" v-if="shrink(index)" style="display:none;"></svg-icon>
          </div>
          <div class="item">{{ item.descr }}</div>
          <div class="item">
            <span v-if="item.descr==='首页' || item.descr==='消息中心'">启动</span>
            <crland-switch v-else  :operatePermission="operatePermission" :objBind="item" :status="statusParse(item.status)" :offText="'停用'" @handleClick="switchChange"></crland-switch>
          </div>
          <div class="item">
            <a><img :style="{'cursor':operatePermission?'pointer':'not-allowed','opacity':operatePermission?'1':'0.6'}" @click.stop="handleUpdate(item)" src="../../assets/tdspic/edit.png" alt="编辑"></a>
            <a><img :style="{'cursor':operatePermission?'pointer':'not-allowed','opacity':operatePermission?'1':'0.6'}" @click.stop="handleDelete(item)"src="../../assets/tdspic/delete.png" alt="删除"></a>
          </div>
        </div>
        <div class="expand-wrap" :style="{'height':allExpand?expandHeight(index):'0'}">
          <div class="table-line" v-for="(subItem,subIndex) in item.children">
            <div class="item"></div>
            <div class="item">{{ subItem.descr }}</div>
            <div class="item">
              <span v-if="subItem.descr==='首页' || subItem.descr==='消息中心'">启动</span>
              <crland-switch v-else :operatePermission="operatePermission" :objBind="subItem" :status="statusParse(subItem.status)" :offText="'停用'" @handleClick="switchChange"></crland-switch>
            </div>
            <div class="item">
              <a><img :style="{'cursor':operatePermission?'pointer':'not-allowed','opacity':operatePermission?'1':'0.6'}" @click.stop="handleUpdate(subItem)" src="../../assets/tdspic/edit.png" alt="编辑"></a>
              <a><img :style="{'cursor':operatePermission?'pointer':'not-allowed','opacity':operatePermission?'1':'0.6'}" @click.stop="handleDelete(subItem)" src="../../assets/tdspic/delete.png" alt="删除"></a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>


<script>
import CrlandSwitch from '@/components/CrlandSwitch'
  export default {
    components:{
      CrlandSwitch
    },
    data() {
      return {
      expandIndex: []
      }
    },
    props: {
      data: {
        type: Array,
        default: function(){
          return []
        }
      },
      allExpand: {
        type: Boolean,
        default: false
      },
      operatePermission: {
        type: Boolean,
        default: false
      }
    },
    mounted() {
    },
    methods:{
      handleUpdate(val){
        if(this.operatePermission){
          this.$emit('edit',val)
        }
      },
      handleDelete(val){
        if(this.operatePermission){
          this.$emit('delete',val)
        }
      },
      shrink(val){
        if(this.data[val].expanded === undefined){
          this.data[val].expanded = !this.allExpand
        }
        return this.data[val].expanded
      },
      abc(val){
      },
      switchChange(val){
        this.$emit('switchChange',val)
      },
      // 状态解析
      statusParse(filed){
        if(filed){
          if(filed == '1'){
            return true
          }else {
            return false
          }
        }
      },
      control(index){
        let expand_h = 0;
        if(this.data[index].expanded){
          $('.expand-wrap:eq('+index+') .table-line').each(function() {
            expand_h += $(this).outerHeight()
          })
        }
        $('.expand-wrap:eq('+index+')').height(expand_h)
        $('.expand-line:eq('+index+')').toggleClass('expanded')
        this.data[index].expanded = !this.data[index].expanded
      },
      expandHeight(val){
        let expand_h = 0;
        expand_h = this.data[val].children.length * 54.2
        // $('.expand-wrap:eq('+val+') .table-line').each(function() {
        //   expand_h += $(this).outerHeight()
        // })
        return expand_h + 'px';
      }
    }
  }
</script>
<style lang="scss" scoped>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
  #fold-table-wrap{
    border-radius: 5px;
    box-shadow: 0px 4px 3px 0px	rgba(0, 0, 0, 0.08);
    overflow: hidden;
    background: #fff;
    text-align: left;
    .table-header{
      height: 54px;
      line-height: 54px;
      width: 100%;
      background: #fcfcfc;
      .item{
        font-family: 'SourceHanSansCN-Normal';
        color: #999;
        border-right: none;
      }
    }
    .table-line{
      width: 100%;
      display: flex;
      line-height: 54px;
      border-bottom: 1px solid #e4e3e0;
      flex-direction: row;
      justify-content: space-between;
      flex-wrap: wrap;
    }
    .item{
      padding:0 15px;
      border-right: 1px solid #e4e3e0;
      color: #555;
      -webkit-user-select:none;
      -moz-user-select:none;
      -ms-user-select:none;
      user-select:none;
      .svg-icon{
        height: 18px;
        width: 18px;
        position: relative;
        top: 3px;
        cursor: pointer;
      }
      .switch-wrap{
        position: relative;
        top: 8px;
      }
      img{
        position: relative;
        top: 6px;
      }
      &:nth-child(1){
        width: 50px;
        text-align: center;
      }
      &:nth-child(2),&:nth-child(3){
        width: 100px;
        flex-grow: 0.5;
      }
      &:last-child{
        width: 150px;
        border-right: none;
      }
    }
    .expand-wrap{
      background: #f2f2f2;
      height: 0;
      transition: all .3s;
      overflow: hidden;
      .table-line{
        .item{
          color: #999;
          font-size: 12px;
        }
      }
    }
    .expand-line{
      .svg-icon:nth-child(1){
        display: inline-block;
      }
      .svg-icon:nth-child(2){
        display: none;
      }
      &.expanded{
        .svg-icon:nth-child(1){
        display: none;
        }
        .svg-icon:nth-child(2){
          display: inline-block;
        }
      }
    }
  }
</style>
