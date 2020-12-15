<template>
  <el-popover
    placement="bottom"
    :width="popverWidth"
    trigger="hover"
    popper-class="cr-help-popver">
    <div class="popver-content">
      <slot name="popver-content"></slot>
    </div>
    <el-button slot="reference" class="help-btn" :style="styleText">
      <img src="../../assets/tdspic/help.png">
    </el-button>
  </el-popover>
</template>

<script>
  export default {
    name: 'cr-help-popver',
    data(){
      return {
      }
    },
    props: {
      popverWidth:{
        type: String,
        default: 'auto'
      },
      styleText:{
        type: String,
        default: ''
      }
    },
    mounted() {
    },
    methods: {
    }
  }
</script>

<style lang="scss" scoped>
.help-btn{
  padding: 0;
  border: none;
}
.popver-content{
  div,a,p{
    font-size: 12px;
  }
}
</style>


