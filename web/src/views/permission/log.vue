<template>
	<div class="components-container"> 
		<div class="sms-list-wrap">
			<nav class="user-list-bar">
				<el-input style="width: 160px;" v-model="query.name" placeholder="登陆用户名"></el-input>
				<el-button type="primary"  @click="_getLogs()">查询</el-button>
				<el-button type="primary" @click="reset">重置</el-button>
			</nav>
			<section>
				 <el-table
					:data="tableData.data"
					stripe
					style="width: 100%">
					<el-table-column type="index"></el-table-column>
					<el-table-column prop="loginName" label="用户名"></el-table-column>
					<el-table-column prop="loginIp" label="登陆ip"></el-table-column>
					<el-table-column prop="loginDate" label="登陆时间" ></el-table-column>
					<!-- <el-table-column label="发送时间" >
						<template slot-scope="scope">
							{{scope.row.sendTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}
						</template>
					</el-table-column> -->
				</el-table>
				<el-pagination
					@size-change="(size)=>{query.pageSize =size;_getLogs()}"
					@current-change="(current)=>{query.pageNo =current;_getLogs()}"
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
import { getLogs } from '@/api/log'
export default {
  data() {
    return {
      query: {
        name: '',
        pageSize: 10,
        pageNo: 1
      },
      // 表格
      tableData: {
        data: [],
        page: {}
      }
    }
  },
  methods: {

    // 重置
    reset() {
      this.query.name = ''
      this._getLogs()
    },
    _getLogs() {
      getLogs(this.query.pageNo, this.query.pageSize, this.query.name).then(({ data }) => {
        if (data.code === 200) {
          this.tableData.data = data.data.data
          this.tableData.page = data.data.page
        }
      })
    }
  },
  mounted() {
    this._getLogs()
  }
}
</script>
<style lang="scss" scoped>
    .listNone{
        list-style: none;
    }
    .listNone li{
        list-style: none;
    }
    .components-container {
        overflow: auto;
        height: 100%;
    }
</style>


