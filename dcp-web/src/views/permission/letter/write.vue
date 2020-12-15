<template>
<div class="letter-send-wrap">
    <el-form  ref="dataForm" :model="formData" label-width="80px" label-position="right" style=''>
	    <el-form-item label="收件人" prop="receiverIds">
	      <el-select v-model="formData.receiverIds"  filterable  multiple placeholder="请选择" style="width: 100%;">
		    <el-option
		      v-for="item in options"
		      :key="item.id"
		      :label="item.loginName+'('+item.name+')'"
		      :value="item.id">
		    </el-option>
		  </el-select>
	    </el-form-item>
	    <el-form-item label="主题" prop="title">
	      <el-input v-model="formData.title"></el-input>
	    </el-form-item>
	    <el-form-item label="正文" prop="text">
	    	<tinymce :height=300 ref="editor" v-model="formData.text"></tinymce>
	    </el-form-item>
	</el-form>
	<div class="letter-send-operate">
		<el-button type="primary" @click="send" :loading="isSend">发送</el-button>
	</div>

</div>
</template>

<script>
	import Tinymce from '@/components/Tinymce'
	import request from '@/utils/request'
	import { Message } from 'element-ui'
export default {
  data() {
	    return {
	      formData: {},
	      options: [],
	      isSend: false
	    }
  },
  methods: {
	    findUser() {
	      request({
	        url: '/api/auth/user/get_users',
	        method: 'get',
	        params: {
	          pageNo: 1,
	          pageSize: 99999,
	          status: '1'
	        }
	      }).then(({ data }) => {
	        this.options = data.data.data
	      })
	    },
	    send() {
	      if (!this.formData.receiverIds.length) {
	        Message.error('请选择收件人！')
	        return false
	      }
	      if (!this.formData.title) {
	        Message.error('请填写主题！')
	        return false
	      }
	      if (!this.formData.text) {
	        Message.error('请填写信件内容！')
	        return false
	      }
	      const temp = this._.cloneDeep(this.formData)
	      temp.receiverIds = temp.receiverIds.join(',')
	      this.isSend = true
	      request({
	        url: '/api/sitemsg/send',
	        method: 'post',
	        data: temp
	      }).then((response) => {
	        if (response.data.code === 200) {
	          this.$message.success('发送成功')
	          this.formData.title = ''
	          this.formData.receiverIds = []
	          this.formData.text = ''
	          this.$refs.editor.setContent('')
	
	          // 跳转到已发送页面
	          this.$router.push('/permission/letter/send')
	        }
	        this.isSend = false
	      }).catch(() => {
	        this.isSend = false
	      })
    }
  },
  mounted() {
	    this.findUser()
  },
  components: { Tinymce }
}
</script>

<style>
.letter-send-operate {
	text-align: right;
}
</style>