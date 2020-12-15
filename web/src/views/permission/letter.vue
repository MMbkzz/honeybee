<template>
<div class="components-container">
    <div class="sms-type">
        <router-link :to="666">
            <div class="sms-type-item">
                <svg-icon icon-class="write" style="color:green;font-size:24px" />
                <span>写信</span>
            </div>
        </router-link>
        <router-link>
            <div class="sms-type-item">
                <svg-icon icon-class="get" style="color:green;font-size:24px" />
                <span>写信</span>
            </div>
        </router-link>
    </div>
    <div class="sms-list">

    </div>
</div>
</template>

<script>
export default {}
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
}
.sms-type-item {
  text-align: center;
  font-size: 18px;
  height: 40px;
  line-height: 40px;
  border-bottom: 1px solid #dddddd;
  cursor: pointer;
}
</style>