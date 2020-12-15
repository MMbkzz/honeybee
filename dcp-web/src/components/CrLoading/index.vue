<template>
  <div id="cr-loading">
    <div class='base'>
      <div class='cube'></div>
      <div class='cube'></div>
      <div class='cube'></div>
      <div class='cube'></div>
      <div class='cube'></div>
      <div class='cube'></div>
      <div class='cube'></div>
      <div class='cube'></div>
      <div class='cube'></div>
    </div>
    <span class="load-text">{{ loadingText }}</span>
  </div>

</template>

<script>
export default {
  name: 'CrLoading',
  data(){
    return {

    }
  },
  props: {
    loadingText: {
      type: String,
      default: '数据加载中'
    }
  },
  mounted() {
  },
  watch: {
  },
  computed: {

  },
  methods: {
  }
}
</script>
<style lang="scss" scoped>
#cr-loading{
  font-size: 12px;
  width: 100%;
  height: 450px;
  display: flex;
  flex-direction: row;
  position: relative;
  justify-content: center;
  align-items: center;
  .base {
    height: 6em;
    left: 50%;
    margin: -3em;
    position: absolute;
    top: 30%;
    width: 6em;
    transform: rotateX(45deg) rotateZ(45deg);
    transform-style: preserve-3d;
    .cube,
    .cube:after,
    .cube:before {
      content: '';
      float: left;
      height: 2em;
      position: absolute;
      left: 0;
      right: 0;
      width: 2em;
    }

    /* Top */
    .cube {
      background-color: #fcb945;
      position: relative;
      transform: translateZ(2em);
      transform-style: preserve-3d;
      transition: .25s;
      box-shadow: 12em 12em 1.5em rgba(0, 0, 0, 0.1);
      animation: anim 1s infinite;
    }
    .cube:after {
      background-color: #d3901e;
      transform: rotateX(-90deg) translateY(2em);
      transform-origin: 100% 100%;
    }
    .cube:before {
      background-color: #dfad58;
      transform: rotateY(90deg) translateX(2em);
      transform-origin: 100% 0;
    }
    .cube:nth-child(1) {
      animation-delay: 0.05s;
    }
    .cube:nth-child(2) {
      animation-delay: 0.1s;
    }
    .cube:nth-child(3) {
      animation-delay: 0.15s;
    }
    .cube:nth-child(4) {
      animation-delay: 0.2s;
    }
    .cube:nth-child(5) {
      animation-delay: 0.25s;
    }
    .cube:nth-child(6) {
      animation-delay: 0.3s;
    }
    .cube:nth-child(7) {
      animation-delay: 0.35s;
    }
    .cube:nth-child(8) {
      animation-delay: 0.4s;
    }
    .cube:nth-child(9) {
      animation-delay: 0.45s;
    }
  }
  .load-text{
    font-size: 12px;
    color: #999;
  }
  @keyframes anim {
    50% {
      transform: translateZ(0.5em);
    }
  }
}
</style>
