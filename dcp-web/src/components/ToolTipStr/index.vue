<template>
    <el-tooltip popper-class="table-tooltip" :disabled="!show" class="tooltip-wrap" :effect="effect" :content="content" :placement="placement">
      <span><slot name="strWrap"></slot></span>
    </el-tooltip>
</template>

<script>

const version = require('element-ui/package.json').version // element-ui version from node_modules
const ORIGINAL_THEME = '#409EFF' // default color

export default {
  data() {
    return {
      show: false
    }
  },
  watch: {
  },
  props: {
    effect: {
      type: String,
      default: 'light'
    },
    content: {
      type: String,
      default: '提示'
    },
    placement: {
      type: String,
      default: 'top-start'
    }
  },
  mounted() {
    const self = this;
    this.show = $(self.$el).find('i').width()+20>$(self.$el).width()
  },
  methods: {
  }
}
</script>

<style>
</style>
