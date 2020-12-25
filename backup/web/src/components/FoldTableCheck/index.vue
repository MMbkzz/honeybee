<template>
  <div class="fold-table-wrap" id="fold-table-wrap">
    <div class="table-header">
      <div class="table-line">
        <div class="item">资源</div>
        <div class="item">操作</div>
      </div>
    </div>
    <div class="table-content">
      <div :class="{'expand-line':true, 'expanded':allExpand}" v-for="(item,index) in data" :key="index">
        <div class="table-line" @click.stop="control(index)" v-if="item.resource.descr !== '首页' && item.resource.descr !=='消息中心'">
          <div class="item">
            <svg-icon icon-class="zhankai"></svg-icon>
            <svg-icon icon-class="shouqi"></svg-icon>
            <svg-icon icon-class="" v-if="shrink(index)" style="display:none;"></svg-icon>
            {{ item.resource.descr }}
          </div>
          <div class="item">
            <el-checkbox-group v-model="item.checks">
              <el-checkbox 
                v-for="(choice,checkIndex) of item.operations" 
                :disabled="item.resource.status == '0' || checkDisabled" 
                :label="choice.id" :key="checkIndex">
                {{ choice.name }}
              </el-checkbox>
            </el-checkbox-group>
          </div>
        </div>
        <div class="expand-wrap" :style="{'height':allExpand?expandHeight(index):'0'}" v-if="item.resource.descr !== '首页' && item.resource.descr !=='消息中心'">
          <div class="table-line" v-for="(subItem,subIndex) in item.children">
            <div class="item">{{ subItem.resource.descr }}</div>
            <div class="item">
              <el-checkbox-group v-model="subItem.checks">
                <el-checkbox 
                  v-for="(choice,subCheckIndex) of subItem.operations" 
                  :disabled="subItem.resource.status == '0' || checkDisabled" 
                  :label="choice.id" :key="subCheckIndex">
                  {{ choice.name }}
                </el-checkbox>
              </el-checkbox-group>
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
      checkDisabled: {
        type: Boolean,
        default: false
      },
      data: {
        type: Array,
        default: function(){
          return []
        }
      },
      allExpand: {
        type: Boolean,
        default: false
      }
    },
    mounted() {   
    },
    methods:{
      handleUpdate(val){
        this.$emit('edit',val)
      },
      handleDelete(val){
        this.$emit('delete',val)
      },
      shrink(val){
        if(this.data[val].expanded === undefined){
          this.data[val].expanded = !this.allExpand
        }
        return this.data[val].expanded
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
        expand_h = this.data[val].children.length * 30
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
    border: 1px solid #dee1e2;
    overflow: hidden;
    background: #fff;
    text-align: left;
    .table-header{
      height: 30px;
      line-height: 30px;
      width: 100%;
      background: #f0f0f0;
      .item{
        font-family: 'MicrosoftYaHei-Bold';
        color: #999;
        text-align: center;
      }
    }
    .table-line{
      width: 100%;
      display: flex;
      height: 30px;
      line-height: 30px;
      border-bottom: 1px solid #e4e3e0;
      flex-direction: row;
      justify-content: space-between;
      flex-wrap: wrap;
    }
    .item{
      padding:0 10px;
      border-right: 1px solid #e4e3e0;
      color: #555;
      font-size: 12px;
      font-family: "MicrosoftYaHei";
      .svg-icon{
        height: 18px;
        width: 18px;
        position: relative;
        top: 2px;
        cursor: pointer;
      }
      &:nth-child(1){
        flex-grow: 1;
        -webkit-user-select:none;
        -moz-user-select:none;
        -ms-user-select:none;
        user-select:none;
      }
      &:last-child{
        width: 150px;
        text-align: center;
        border-right: none;
      }
    }
    .expand-wrap{
      background: #f9f9f9;
      height: 0;
      transition: all .3s;
      overflow: hidden;
      .table-line{
        .item{
          line-height: 30px;
          &:first-child{
            padding-left: 60px;
          }
        }
      }
    }
    .expand-line{
      .svg-icon{
        margin-right: 5px;
      }
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


