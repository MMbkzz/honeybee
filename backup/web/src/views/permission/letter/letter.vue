<template>
<div class="components-container">
    <div class="sms-type">
        <router-link to="/permission/letter/write" >
            <div class="sms-type-item" :class="{'type-active':$route.path == '/permission/letter/write'}">
                <svg-icon icon-class="write"/>
                <span>写信</span>
            </div>
        </router-link>
        <router-link to="/permission/letter/mine" >
            <div class="sms-type-item" :class="{'type-active':$route.path == '/permission/letter/mine'}">
                <svg-icon icon-class="receive"/>
                <span>收件箱</span>
            </div>
        </router-link>
        <router-link to="/permission/letter/send">
            <div class="sms-type-item" :class="{'type-active':$route.path == '/permission/letter/send'}">
                <svg-icon icon-class="send"/>
                <span>已发送</span>
            </div>
        </router-link>
    </div>
    <div class="sms-list">
		<transition name="fade" mode="out-in">
	        <router-view></router-view>
	    </transition>
    </div>
</div>
</template>

<script>
export default {
  data() {
    return {

    }
  },
  methods: {

  }
}
</script>

<style>
.sms-type {
  position: absolute;
  width: 160px;
  top: 10px;
  bottom: 20px;
  border: 1px solid #ddd;
}
.sms-list {
	position: absolute;
	left: 180px;
	right: 10px;
	height: 100%;
}
.sms-type-item {
  height: 40px;
  line-height: 40px;
  padding-left: 40px;
  border-bottom: 1px solid #dddddd;
  cursor: pointer;
  color: #97a8be;
}
.type-active {
	color: rgb(64, 158, 255);
	background-color: #DDDDDD;
}
</style>