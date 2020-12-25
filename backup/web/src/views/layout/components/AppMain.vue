<template>
  <section class="app-main">
    <div class="app-main-wrap">
    <transition name="fade" mode="out-in">
      <keep-alive :include="cachedViews">
        <router-view></router-view>
      </keep-alive>
    </transition>
    </div>
  </section>
</template>

<script>
export default {
  name: "AppMain",
  computed: {
    cachedViews() {
      return this.$store.state.tagsView.cachedViews;
    }
    // key() {
    //   return this.$route.name !== undefined ? this.$route.name + +new Date() : this.$route + +new Date()
    // }
  }
};
</script>
<style>
.app-main {
  position: absolute;
  background-color: #f1f1f1;
  bottom: 0px;
  top: 50px;
  left: 0px;
  right: 0px;
  padding: 15px;
}
.app-main-wrap {
  background-color: #f1f1f1;
  height: 100%;
  position: relative; 
}
</style>