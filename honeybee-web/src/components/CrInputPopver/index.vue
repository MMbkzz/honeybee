<template>
  <div class="input-popver">
    <el-popover
      v-if="!disabled"
      placement="top"
      width="300"
      trigger="manual"
      v-model="visible">
      <el-input class="popver-textarea" type="textarea" v-model="textModel" @focus="visible = true" resize="none" rows="10"></el-input>
      <el-input v-model="inputModel" style="display:none"></el-input>
      <div style="text-align: right; margin-top: 15px;">
        <el-button size="mini" @click.stop="cancle">取消</el-button>
        <el-button type="primary" size="mini" @click.stop="okFn">确定</el-button>
      </div>
      <el-input slot="reference" v-model="inputModel2" @focus="visible = true" @blur="blurFn" size="mini"></el-input>
    </el-popover>
    <el-popover
      v-else
      placement="top"
      width="300"
      trigger="hover">
      <el-input class="popver-textarea" type="textarea" disabled v-model="inputModel" resize="none" rows="10"></el-input>
      <el-input slot="reference" v-model="inputModel" disabled size="mini"></el-input>
    </el-popover>
  </div>
</template>

<script>
export default {
  name: 'cr-input-popver',
  data(){
    return {
      textModel: '',
      inputModel2: '',
      visible: false
    }
  },
  props: {
    inputModel: {
      type: String,
      default: ''
    },
    disabled:{
      type: Boolean,
      default: false
    }
  },
  mounted() {
    // console.log(this.inputModel)
    this.inputModel2 = this.inputModel
  },
  methods: {
    cancle(){
      this.visible = false
    },
    okFn(){
      this.inputModel2 = this.textModel
      this.$emit('ok',this.textModel)
      // this.inputModel = this.textModel
      this.visible = false
    },
    blurFn(){
      this.textModel = this.inputModel2
      this.$emit('blur',this.inputModel2)
      this.visible = false
    }
  }
}
</script>
<style lang="scss" scoped>
.input-popver{
  .popver-textarea{

  }
}
</style>



