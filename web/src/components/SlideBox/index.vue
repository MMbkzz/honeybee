<template>
    <div class="slide-wrap" :style="{display:slideShow?'block':'none'}" @click="close">

      <div class="slide-main" :style="{width:slideWidth}" @click.stop="">
        <h2 class="slide-title">
          <i class="close-icon el-icon el-icon-arrow-right" @click="close"></i><span class="title">{{ title }}</span>
        </h2>
        <div class="slide-content">
          <slot name="slide-content"></slot>
        </div>
        <div class="slide-footer">
          <slot name="slide-footer"></slot>
        </div>
      </div>

      <div v-if="subSlide" class="slide-main sub-slide-main" :style="{width:slideWidth}" @click.stop="">
        <h2 class="slide-title">
          <i class="close-icon sub-close-icon" @click="subClose"><svg-icon icon-class="return"></svg-icon></i>
          <span class="title">{{ subTitle }}</span>
        </h2>
        <div class="slide-content">
          <slot name="sub-slide-content"></slot>
        </div>
        <div class="slide-footer">
          <slot name="sub-slide-footer"></slot>
        </div>
      </div>
    </div>
</template>
<script>
export default {
  name: 'SlideBox',
  components: {},
  data() {
    return {
    }
  },
  created() {
      // this.slideIn()
  },
  computed: {
  },
  props:{
      slideShow: {
          type: Boolean,
          default: false
      },
      subSlideShow: {
          type: Boolean,
          default: false
      },
      slideWidth: {
          type: String,
          default: '0'
      },
      subSlide: {
        type: Boolean,
        default: false
      },
      title: {
        type: String,
        default: ''
      },
      subTitle: {
        type: String,
        default: ''
      }
  },
  watch:{
    slideShow(val){
      if(val){
        this.slideIn()
      }
    },
    subSlideShow(val){
      if(val){
        this.subSlideIn()
      }else{
      }
    }
  },
  methods: {
      slideIn(){
        const el = this.$el
        // console.log($(el))
        $(el).find('.slide-main').css({'margin-right':'-'+this.slideWidth,'opacity':0});
        $(el).animate({'opacity':1},300)
        $(el).find('.slide-main:eq(0)').animate({'margin-right':0,'opacity':1},300)
      },
      close(){
        const self = this
        const el = this.$el
        $(el).animate({'opacity':0},300)
        $(el).find('.slide-main').animate({'margin-right':'-'+this.slideWidth},300,function(){
          self.$emit('close')
        })
      },
      subSlideIn(){
        // console.log(111111)
        const el = this.$el
        $(el).find('.slide-main:eq(1)').animate({'margin-right':0,'opacity':1},300)
      },
      subClose(){
        const self = this
        const el = this.$el
        $(el).find('.slide-main:eq(1)').animate({'margin-right':'-'+this.slideWidth,'opacity':0},300,function(){
          self.$emit('subClose')
        })
      }
  }
}
</script>
<style lang="scss" scoped>
.slide-wrap{
    position: fixed;
    left: 0;
    top: 0;
    z-index: 1999;
    width: 100%;
    height: 100%;
    opacity: 0;
    background: rgba(0,0,0,.5);
    text-align: left;
    .slide-main{
      position: absolute;
      right: 0;
      background: white;
      height: 100%;
      z-index: 1;
      &.sub-slide-main{
        z-index: 2;
      }
      .slide-title{
        width: 100%;
        height: 50px;
        line-height: 50px;
        font-size: 16px;
        border-bottom: 1px solid #eeeeee;
        background: #fcfcfc;
        display: flex;
        flex-direction: row;
        align-items: center;
        .close-icon{
          font-style: normal;
          text-align: center;
          line-height: 45px;
          cursor: pointer;
          font-size: 25px;
          color: white;
          display: inline-block;
          height: 100%;
          width: 30px;
          background: #4562fc;
          font-family: consolas;
          margin-right: 15px;
          &.sub-close-icon{
            border-right:1px solid #eee;
            background: transparent;
            font-size:18px;
            .svg-icon{
              position: relative;
              top: 3px;
            }
          }
        }
      }
      .slide-content{
        overflow: hidden;
        overflow-y: auto;
        position: absolute;
        width: 100%;
        top: 50px;
        bottom: 55px;
        &::-webkit-scrollbar{
          width: 5px;
          height: 1px;
        }
        &::-webkit-scrollbar-thumb {/*滚动条里面小方块*/
          border-radius: 5px;
          background: #555;
        }
        &::-webkit-scrollbar-track {/*滚动条里面轨道*/
        }
      }
      .slide-footer{
        height: 55px;
        width: 100%;
        position: absolute;
        bottom: 0;
        background: #fcfcfc;
        border-top: 1px solid #eeeeee;
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        .el-button{
          &.el-button--primary{
            background: #4562fc;
            border: 1px solid #4562fc;
          }
        }
      }
    }
}
</style>
<style lang="scss">
.el-checkbox__inner{
          z-index: 0;
        }
</style>
