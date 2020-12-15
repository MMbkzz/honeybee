<template>
	<div class="components-container"> 
		<div class="sms-list-wrap">
			<nav class="user-list-bar">
				<el-input style="width: 200px;" v-model="query.sendTo" placeholder="邮件标题、收件人、发件人"></el-input>

				<el-button type="primary"  @click="fetchEmail">查询</el-button>
				<el-button type="primary" @click="reset">重置</el-button>
				<!--<el-button type="primary"  @click="handleCreate">新增</el-button>-->
			</nav>
			<section>
				 <el-table
					:data="tableData.data"
					stripe
          @row-click="toDetail"
					style="width: 100%">
					<el-table-column type="index"></el-table-column>
					<el-table-column prop="title" label="标题"></el-table-column>
					<el-table-column prop="fromUserNumber" label="发件人"></el-table-column>
					<el-table-column prop="toUserNumber" label="收件人" ></el-table-column>
					<el-table-column label="发送时间" >
						<template slot-scope="scope">
							<!-- {{scope.row.sendTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }} -->
							{{scope.row.sendTime}}
						</template>
					</el-table-column>
				</el-table>
				<el-pagination
					@size-change="(size)=>{query.pageSize =size;this.fetchEmail()}"
					@current-change="(current)=>{query.pageNo =current;this.fetchEmail()}"
					:page-size="10"
					:current-page="1"
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
        query: {
          sendTo: '',
          pageSize: 10,
          pageNo: 1
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
        this.query.sendTo = ''
        this.fetchEmail()
      },

      fetchEmail() {
        request({
          url: '/api/email/send/selectEmail',
          method: 'get',
          params: this.query
        }).then(({ data: { data }}) => {
          this.tableData.data = data.data
          this.tableData.page = data.page
        })
      },
      toDetail(row) {
        this.$router.push({ path: '/permission/emaildetails', query: {
          id: row.id
        }})
      }
    },
    mounted() {
      this.fetchEmail()
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