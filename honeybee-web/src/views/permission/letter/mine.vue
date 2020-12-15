<template>
	<div class="components-container">
		<div class="mine-list">
		<el-table ref="multipleTable" :data="tableData.data" tooltip-effect="light" style="width: 100%" :row-style="handleIsRead" @row-click="handleSelectLetter">
			<el-table-column type="selection" width="55">
			</el-table-column>
          	<el-table-column :render-header="isReadRender" class-name="cursorPointer" width="120">
				<template slot-scope="scope">
					 <svg-icon v-if="scope.row.status =='2'" icon-class="letter" style="font-size:24px" />
					 <svg-icon v-else icon-class="letter_read" style="font-size:24px" />
				</template>
			</el-table-column>
			<el-table-column prop="sender_desc" class-name="cursorPointer" label="发件人" width="120">
			</el-table-column>
			<el-table-column prop="receiver_desc" class-name="cursorPointer" label="收件人" width="180">
			</el-table-column>
			<el-table-column prop="title" class-name="cursorPointer" label="主题" show-overflow-tooltip>
			</el-table-column>
			<el-table-column label="发送时间" class-name="cursorPointer" width="200">
				<!-- <template slot-scope="scope">{{ scope.row.post_time| parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</template> -->
				<template slot-scope="scope">{{ scope.row.post_time}}</template>
			</el-table-column>
			<el-table-column label="操作" class-name="cursorPointer">
				<template slot-scope="scope">
					<el-button type="danger" size="mini" @click.stop="deleteMsg(scope.row.msg_id)">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<el-pagination
			@size-change="(size)=>{queryParams.pageSize = size;fetchMyLetters()}"
			@current-change="(current)=>{queryParams.pageNo = current;fetchMyLetters()}"
			:page-size="10"
			:page-sizes="[10, 20, 30, 40]"
			layout="total, sizes, prev, pager, next, jumper"
			:total="tableData.page.totalElements">
		</el-pagination>
		</div>
	</div>
</template>

<script>
import request from '@/utils/request'
import { deleteMsg } from '@/api/letter'
export default {
  data() {
    return {
      tableData: {
        data: [],
        page: {}
      },
      multipleSelection: [],
      queryParams: {
        pageSize: '10',
        pageNo: '1'
      }
    }
  },
  methods: {
    handleSelectLetter(row, event, column) {
      this.$router.push({ path: '/permission/letter/details', query: { id: row.transferId }})
    },
    handleIsRead: ({ row }) => {
      // 1:已读，2：未读
      if (row.status == '2') {
        return { 'font-weight': 600 }
      }
    },
    isReadRender: (h) => {
      return <svg-icon icon-class='letters' style='font-size:24px' />
    },
    fetchMyLetters() {
      request({
        url: '/api/sitemsg/get_receive_msg',
        method: 'get',
        params: this.queryParams
      }).then(({ data }) => {
        if (data.code === 200) {
          this.tableData = data.data
        }
      })
    },
    deleteMsg(id) {
      deleteMsg(id).then(({ data }) => {
        if (data.code === 200) {
          this.$message.success('删除成功！')
          this.fetchMyLetters()
        } else {
          this.$message.error(data.message)
        }
      })
    }
  },
  mounted() {
    this.fetchMyLetters()
  }
}
</script>

<style lang="scss" scoped>
	.cursorPointer {
		cursor: pointer;
	}

	.components-container {
		overflow: hidden;
		height: 100%;
		.mine-list {
			overflow: auto;
			height: 100%;
			/deep/ .el-pagination {
				padding-bottom: 20px;
			}
		}
		
  	}
</style>