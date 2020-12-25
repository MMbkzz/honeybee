<template>
<div class="">
	<div class="letter-details-header">
		<div class="letter-details-header-title">{{sms.remark2}}</div>
		<div class="letter-details-header-item">发送时间&nbsp;:&nbsp;{{ sms.sendTime}}</div>
		<div class="letter-details-header-item">接收人姓名&nbsp;:&nbsp;{{sms.receiverName}}</div>
		<div class="letter-details-header-item">接收人手机&nbsp;:&nbsp;{{sms.receiverPhone}}</div>
	</div>
	<div class="letter-details-container">
		<div>
			<!-- <p v-for="(item) of 5" :key="item" v-if='sms["remark"+item]'>{{sms["remark"+item]}}</p> -->
			<p>{{sms.remark1}}</p>
		</div>
	</div>
</div>
</template>

<script>
import { getSmsDetail } from '@/api/sms'
export default {
  data() {
    return {
      sms: {
        purpose: '',
        sendTime: '',
        receiverName: '',
        receiverPhone: ''
      }
    }
  },
  created() {
    getSmsDetail(this.$route.query.id).then(({ data }) => {
      if (data.code === 200) {
        this.sms = data.data.data[0]
      }
    })
  }
}
</script>

<style>
.letter-details-header {
	background-color: #eff5fb;
	height: 120px;
	padding: 10px;
	/* margin-bottom: 10px; */
}
.letter-details-header-title {
	font-weight: bold;
	font-size: 14px;
	margin-bottom: 10px;
}
.letter-details-header-item {
	color: #798699;
	margin-bottom: 5px;
}
.letter-details-container {
	/* border: 1px solid #DDDDDD;
	width: 100%;
	min-height: 500px;
	padding: 0px 15px; */
	padding: 10px;
}
</style>