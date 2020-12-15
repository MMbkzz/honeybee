<template>
<div class="">
	<div class="letter-details-header">
		<div class="letter-details-header-title">{{letterInfo.title||letterInfo.msg_title}}</div>
		<div class="letter-details-header-item">发件人&nbsp;:&nbsp;{{letterInfo.sender_desc}}</div>
		<div class="letter-details-header-item">时间&nbsp;:&nbsp;{{ letterInfo.post_time}}</div>
		<div class="letter-details-header-item">收件人&nbsp;:&nbsp;{{letterInfo.receiver||letterInfo.receiver_desc}}</div>
	</div>
	<div class="letter-details-container">
		<div v-html="letterInfo.text">
			
		</div>
	</div>
</div>
</template>

<script>
	import request from '@/utils/request'
	import { getSendDetail, getReceiveDetail } from '@/api/letter'
export default {
  data() {
	    return {
	      letterInfo: {}
	    }
  },
  methods: {
	    setLetterRead(id) {
	      request({
	        url: '/api/sitemsg/set_already_read',
	        method: 'get',
	        params: { msgId: id }
	      }).then(({ data }) => {
	        console.log(data)
	      })
    }
  },
  created() {
	    if (this.$route.query.from === 'send') {
	      getSendDetail(this.$route.query.id).then(({ data }) => {
	        if (data.code === 200) {
	          this.letterInfo = data.data
	        }
	      })
	    } else {
	      getReceiveDetail(this.$route.query.id).then(({ data }) => {
	        if (data.code === 200) {
	          this.letterInfo = data.data
	          this.setLetterRead(this.letterInfo.msg_id)
	        }
	      })
	    }
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
}
</style>