<template>
	<div class="components-container"> 
		<div class="sms-list-wrap">
			<nav class="user-list-bar">
				<el-input style="width: 160px;" v-model="query.receiverName" placeholder="短信接收人"></el-input>

				<el-button type="primary"  @click="fetchSms">查询</el-button>
				<el-button type="primary" @click="reset">重置</el-button>
				<!--<el-button type="primary"  @click="handleCreate">新增</el-button>-->
			</nav>
			<section>
				 <el-table
					:data="tableData.data"
					stripe
          @row-click='toDetail'
					style="width: 100%">
					<el-table-column type="index"></el-table-column>
					<el-table-column prop="purpose" label="业务类型"></el-table-column>
					<!--<el-table-column prop="loginName" label="发件人"></el-table-column>-->
					<el-table-column prop="receiverName" label="收件人" ></el-table-column>
					<el-table-column prop="receiverPhone" label="手机号" ></el-table-column>
					<el-table-column prop="remark2" label="标题" ></el-table-column>
					<el-table-column label="发送时间" >
						<template slot-scope="scope">
							{{scope.row.sendTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}
						</template>
					</el-table-column>
				</el-table>
				<el-pagination
					@size-change="(size)=>{query.pageSize =size;this.fetchSms()}"
					@current-change="(current)=>{query.pageNo =current;this.fetchSms()}"
					:page-size="10"
					:page-sizes="[10, 20, 30, 40]"
					layout="total, sizes, prev, pager, next, jumper"
					:total="tableData.page.totalElements">
				</el-pagination>
			</section>
		</div>
		
	</div>
</template>

<script>
import request from '@/utils/request'
export default {
  data() {
    return {
      // 新增修改弹框
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新增'
      },
      roles: [],
      organizations: [],
      user: {
        loginName: '',
        name: '',
        password: '',
        email: '',
        mobile: '',
        organization: '',
        roles: []
      },
      userRule: {
        loginName: [{ required: true, message: '请填写登录名', trigger: 'blur' }],
        name: [{ required: true, message: '请填写用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请填写密码', trigger: 'blur' }],
        organizations: [{ required: true, message: '请填写组织', trigger: 'blur' }],
        roles: [{ required: true, message: '请填写角色', trigger: 'blur' }]
      },

      // 查询
      downloadLoading: false,
      query: {
        receiverName: '',
        pageSize: 10,
        pageNo: 1
      },
      sex: [{ value: '1', label: '男' }, { value: '2', label: '女' }],
      status: [{ value: '0', label: '停用' }, { value: '1', label: '启用' }],

      // 组织架构
      treeData: [],
      defaultProps: {
        children: 'child',
        label: 'name'
      },

      // 表格
      tableData: {
        data: [],
        page: {}
      },
      count: 0,
      // 角色
      role: [
        {
          name: '经理1',
          id: 6362555963081232384
        },
        {
          name: '查看',
          id: 6361849524041814016
        }
      ]
    }
  },
  filters: {
    statusTypeFilter(status) {
      const typeMap = {
        0: 'danger',
        1: 'success'
      }
      return typeMap[status]
    },
    statusFilter(status) {
      const statusMap = {
        0: '停用',
        1: '启用'
      }
      return statusMap[status]
    }
  },
  methods: {
  // 重置
    reset() {
      this.query.receiverName = ''
      this.fetchSms()
    },

    fetchSms() {
      request({
        url: '/api/sms/collection',
        method: 'get',
        params: this.query
      }).then(({ data: { data }}) => {
        this.tableData.data = data.data
        this.tableData.page = data.page
      })
    },
    toDetail(row) {
      this.$router.push({ path: '/permission/smsdetails', query: { id: row.id }})
    }
  },
  mounted() {
    this.fetchSms()
  }
}
</script>

<style>
.user-tree-header {
  padding: 10px;
  border-bottom: 1px solid #dddddd;
}
.sms-list-wrap {
  padding: 0px 20px;
}
.user-list-bar {
  padding-bottom: 10px;
  margin-bottom: 10px;
  border-bottom: 1px solid #dddddd;
}
</style>