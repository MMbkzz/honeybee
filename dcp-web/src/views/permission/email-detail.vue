<template>
<div>
	<div class="letter-details-header">
		<div class="letter-details-header-title">{{email.title}}</div>
		<div class="letter-details-header-item">发件人&nbsp;:&nbsp;{{email.fromUserNumber}}</div>
		<div class="letter-details-header-item">时间&nbsp;:&nbsp;{{ email.sendTime}}</div>
		<div class="letter-details-header-item">收件人&nbsp;:&nbsp;{{email.toUserNumber}}</div>
	</div>
	<div class="letter-details-container">
		<div v-html="email.contents">
			
		</div>
	</div>
</div>
</template>

<script>
import { getEmailDetail } from '@/api/email'
export default {
  data() {
    return {
      email: {
        title: '',
        fromUserNumber: '',
        sendTime: '',
        toUserNumber: '',
        contents: ''
      }
    }
  },
  created() {
    getEmailDetail(this.$route.query.id).then(({ data }) => {
      if (data.code === 200) {
        if (data.data.length) {
          this.email = data.data[0]
        }
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