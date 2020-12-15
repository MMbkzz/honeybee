<template>
	<div class="card-wrap">
		<el-row class="cards" :gutter="25">
			<el-col :span="8" v-for="(item,index) in cardData" :key="index">
				<div class="card-main" @click="handleView(item)">
					<div class="card-header">
						<div class="title">
							<!-- <el-tooltip class="item" effect="dark" :content="item[showFields.name]" placement="top"> -->
								<span>{{ cardTitle }}：{{ item[showFields.name] }}</span>
							<!-- </el-tooltip> -->
						</div>
						<div class="operate-btn">
							<a title="编辑" @click.stop="handleUpdate(item)"><img src="../../assets/tdspic/edit.png" alt=""></a>
							<a title="删除" @click.stop="handleDelete(item)"><img src="../../assets/tdspic/delete.png" alt=""></a>
						</div>
					</div>
					<div class="card-des">
						<div class="card-des-content">
							<p>{{ item[showFields.desc] }}</p>
						</div>
						<div class="card-footer">
							<span>更新用户：{{ item.updateUser }}</span>
							<span>更新时间：{{ item.updateTime }}</span>
						</div>
					</div>
				</div>
			</el-col>
		</el-row>
		<div style="width:100%;float:left;">
			<el-pagination
				background
				@current-change="currentPageChange"
				:page-size="parseInt(searchForm.pageSize)"
				:current-page="parseInt(searchForm.pageNo)"
				layout="prev, pager, next"
				:total="pageNum">
			</el-pagination>
		</div>
	</div>
</template>
<script>
import { screenResize,windowResize } from '@/utils'
export default {
  name: 'TextCard',
  components: {},
  data() {
    return {
    }
  },
  created() {
      this.$store.commit('SET_COUNT_NUM', 9)
  },
  mounted() {
	//   boxResize(this.$el,'card-main',380, 215)
  },
  computed: {
      myWidth() {
          const obj = screenResize(420,150)
          const newWidth = obj.newWidth
          return newWidth + 'px'
      },
      total() { // 页面能存放的个数
          const total = screenResize(420,150).total
          return total
      }
  },
  props:{
      cardData:{
          type: Array,
          default: []
      },
      searchForm: {
          type: Object,
          default: {
            areaName: null,
            pageNo: '1',
            pageSize: '9'
          }
      },
      showFields: {
          //展示的字段
          type: Object,
          default: {
            name: '名称的字段名',
            desc: '描述的字段名'
          }
      },
      pageNum: {
          type: Number,
          default: 0
      },
      cardTitle: {
          type: String,
          default: ''
      }
  },
  methods: {
      currentPageChange(current){
          this.$emit('current-change', current)
      },
      handleAdd(){
          this.$emit('add')
      },
      handleView(obj){
          this.$emit('browse',obj)
      },
      handleDelete(obj){
          this.$emit('delete',obj)
      },
      handleUpdate(obj){
          this.$emit('edit',obj)
	  },
	  test() {
	  }
  }
}
</script>
<style lang="scss" scoped>
.card-wrap{
    text-align: center;
    .cards{
        .el-pagination{
            display: inline-block;
            float: initial;
		}
    }
}
.card-main{
	text-align: left;
	height: 130px;
	width: 100%;
	position: relative;
	float: left;
	transition: all 0.5s;
	margin-bottom: 20px;
	border: 1px solid transparent;
    box-shadow: 0px 2px 1px 0px
      rgba(0, 0, 0, 0.08);
	border-radius: 5px;
	overflow: hidden;
	cursor: pointer;
	&:hover{
		border-color: #4562fc;
	}
	.card-header{
		width:100%;
		height: 40px;
		line-height: 40px;
		color: #999;
		background: #fcfcfc;
		border-bottom: 1px solid #eee;
		padding: 0 15px;
		font-size: 12px;
		.title{
			float: left;
			width: 200px;
			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;
			span{
				font-size: 12px;
				color: #999;
			}
		}
		.operate-btn{
			float: right;
			a {
				img{
					vertical-align: text-top;
				}
			}
		}
	}
	.card-des{
		width:100%;
		float:left;
		height:90px;
		position: relative;
		background: white;
		box-shadow: 0px 4px 3px 0px rgba(0, 0, 0, 0.08);
		.card-des-content{
			height: 50px;
			padding: 0 15px;
			overflow: hidden;
			overflow-y: auto;
			p{
				line-height: 24px;
				font-size: 14px;
				color: #555;
			}
		}
		.card-footer{
			height: 40px;
			position: absolute;
			bottom: 0;
			padding: 0 15px;
			border-top: 1px solid #eee;
			width: 100%;
			line-height: 40px;
			span{
				font-size: 12px;
				color: #999;
			}
			span:nth-child(2){
				float: right;
			}
		}
	}
}
.card-create{
	width: 350px;
	height: 175px;
	position: relative;
	float: left;
	border-radius: 5px;
	margin-bottom: 20px;
	border: dashed 2px #818181;
	text-align: center;
	cursor: pointer;
	&:hover{
		.add-pic{
			img{
				transform-origin: 50% 50%;
				transform: rotate(90deg);
			}
		}
	}
	.add-pic{
		padding-top: 47px;
		img{
			transition: transform 0.5s;
		}
	}
	.add-text{
		margin-top: 15px;
		font-size: 20px;
		font-weight: bold;
		color: #999999;
	}
}
</style>
<style lang="scss">
.card-wrap{
  text-align: center;
  .el-pagination.is-background{
    display: inline-block;
    float: none;
    .btn-prev,.btn-next{
      border: none;
    }
    .el-pager{
      .number{
        border: none;
        border-radius: 50%;
        &.active{
          background: #4562fc;
        }
        &.active:hover {
          color: white;
        }
        &:hover {
          color: #4562fc;
        }
      }
    }
  }
}
</style>
