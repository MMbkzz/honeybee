<template>
  <!-- <div class="cr-switch"> -->
    <!-- <el-tooltip class="item" effect="light" content="您无权限进行此操作，请联系管理员" :disabled="operatePermission" placement="top"> -->
      <div class="switch-wrap" :style="{background:background,'cursor':operatePermission?'pointer':'not-allowed','opacity':operatePermission?'1':'0.6'}" @click.stop="">
        <div class="move-box" :style="{'margin-left':-pos_l+'px'}">
          <span class="on-text" :style="{'cursor':operatePermission?'pointer':'not-allowed'}" @click.stop="handleClick('close')">{{ onText }}</span>
          <span class="move-block" :style="{'cursor':operatePermission?'pointer':'not-allowed'}" @click.stop=""></span>
          <span class="off-text" :style="{'cursor':operatePermission?'pointer':'not-allowed'}" @click.stop="handleClick('start')">{{ offText }}</span>
        </div>
      </div>
    <!-- </el-tooltip> -->
  <!-- </div> -->
</template>

<script>
export default {
  name: 'crland-switch',
  data(){
    return {
      pos_l: 0,
      background: ''
    }
  },
  props: {
    operatePermission: {
      type: Boolean,
      default: true
    },
    status:{
      type: Boolean,
      default: true
    },
    objBind: {
      type: Object,
      default: function(){
        return {}
      }
    },
    onText: {
      type: String,
      default: '启用',
    },
    offText: {
      type: String,
      default: '禁用'
    }
  },
  mounted() {
    this.statusChange(this.status)
  },
  watch: {
    status(newValue,oldValue) {
      this.statusChange(newValue)
    }
  },
  computed: {

  },
  methods: {
    statusChange(state) {
      let b_w = 0;
      $('.move-box:first span').each(function(){
        b_w += $(this).outerWidth(true)
      })
      $('.move-box').width(b_w)
      this.pos_l =  this.status?0:($('.move-box span:first').outerWidth()-3);
      this.background = this.status?'#76C80E':'#ccc'
    },

    handleClick(type){
      if(!this.operatePermission){
        return;
      }
      let status = true;
      const obj = {};
      this.$confirm('确定更改该状态？', '提示', {
        type: 'warning'
      }).then(() => {
          if(type === 'close') {
            this.pos_l =  $('.move-box span:first').outerWidth()-3;
            this.background = '#ccc'
            status = false
          }else {
            this.pos_l =  0;
            this.background = '#76C80E';
            status = true
          }
          obj.state = status;
          obj.item = this.objBind;
          // this.$emit('beforeChange',obj)
          this.$emit('handleClick',obj)
        }
      ).catch(() => {
      })
    },
    close(){

    },
    start(){

    }
  }
}
</script>
<style lang="scss" scoped>
.switch-wrap{
  display: inline-block;
  width: 52px;
  padding: 2px;
  height: 23px;
  background: #75cf00;
  border-radius: 5px;
  overflow: hidden;
  transition: background 0.3s;
  position: relative;
  .move-box{
    position: absolute;
    left: 0;
    transition: margin 0.3s;
    span{
      color: white;
      font-size: 12px;
      line-height: 20px;
      display: inline-block;
      float: left;
      text-align: center;
      padding: 0 3px;
      cursor: pointer;
    }
    .move-block{
      background: white;
      border-radius: 5px;
      height: 19px;
      width: 19px;
      cursor: auto;
    }
  }

}
</style>
