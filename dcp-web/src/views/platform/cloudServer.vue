<!--缓存管理界面-->
<template>
  <div>
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">缓存管理</h2>
        </div>
        <div class="fr">
          <el-button class="more-search" @click="showMoreSearch">
            更多查询 <svg-icon icon-class="spread" :class="{'retract':moreSearch}"></svg-icon>
          </el-button>
        </div>
        <div class="search fr">
          <input class="do-search-input" type="text" style="padding-left: 15px; width: 200px; outline: none; border: 0; font-size: 12px; display: inline-block; height: 100%; float: left;" v-model="query.name" placeholder="请输入关键字"></input>
          <a href="javascript:;" @click="fetchCacheManegeTable()"><svg-icon icon-class="search"></svg-icon></a>
        </div>
        <div class="fr mr-10">
          <el-button class="batch-del" type="primary" @click="AllSource">
            <span>批量删除</span>
          </el-button>
        </div>
      </div>
      <!-- <el-collapse-transition> -->
        <div class="clearfix more-search-box">
          <el-form ref="form" :model="searchForm">
            <div class="clearfix input-group">
              <el-form-item label="键" style="width: 206px;" class="fl" label-width="60px">
                <el-input v-model="searchForm.key" size="small"></el-input>
              </el-form-item>
            </div>
            <div class="btn-group">
              <el-button type="primary" size="mini" @click="fetchCacheManegeTable('1')">查询</el-button>
              <el-button size="mini" style="margin-left: 10px"  @click.stop="resetQuery">重置</el-button>
            </div>
          </el-form>
        </div>
      <!-- </el-collapse-transition> -->
    </nav>
    <section class="components-container clearfix section-bod">
      <cr-loading v-show="loading"></cr-loading>
      <el-table
        v-show="!loading"
        ref="multipleTable"
        :data="tableData.data"
        tooltip-effect="light"
        style="width: 100%"
        size="mini"
        @cell-click = "accordDetails"
        :header-row-class-name="titleBgColor"
        @selection-change="handleSelectionChange"
        >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="name" type="index" label="序号" width="80">
        </el-table-column>
        <el-table-column prop="key" label="键" show-overflow-tooltip>
        </el-table-column>
        <el-table-column show-overflow-tooltip label="值">
          <template slot-scope="scope">
            <div style="font-size: 12px; color: #555">{{ scope.row.value | textLimit(30) }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="expireTime" show-overflow-tooltip label="过期时间">
        </el-table-column>
        <el-table-column label="操作" show-overflow-tooltip width="80">
          <template slot-scope="scope">
            <!--<a title="清理"><svg-icon icon-class="clear" style="width: 18px; height: 18px; margin-bottom:1px;"></svg-icon></a>-->
            <a title="删除" :style="{'cursor':operatePermission?'pointer':'not-allowed','opacity':operatePermission?'1':'0.6'}" @click.stop="deleteSource(scope.row.key)"><img src="../../assets/tdspic/delete.png" alt=""></a>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        layout="prev, pager, next"
        @current-change="(current)=>{searchForm.pageNo = current;this.fetchServiceVisitorTable('2')}"
        :page-size="10"
        :current-page="1"
        :total="tableData.page"
        class="fr mt-15">
      </el-pagination>
    </section>
    <!-- 详情弹框 -->
    <!-- <el-dialog
      title="详情"
      :visible.sync="detailDialog"
      :showClose='closeBtn'
      width="60%">
      <div class="form-body">
        {{ detailMassage }}
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="detailDialog = false">关闭</el-button>
      </span>
    </el-dialog> -->
    <slide-box
      slideWidth="450px"
      :slideShow="detailDialog"
      @close="slideClose"
      title="详情"
      ref="slideBox">
      <div slot="slide-content">
        <el-form :model="detailMassage" label-width="70px" style="padding: 10px 30px;">
          <el-form-item label="键:">
            <el-input v-model="detailMassage.key" size="medium" :disabled="true"></el-input>
          </el-form-item>
          <el-form-item label="值:" class="mt-10">
            <el-input type="textarea" :rows="10" v-model="detailMassage.value" size="medium"></el-input>
          </el-form-item>
          <el-form-item label="过期时间:"  class="mt-10">
            <el-input v-model="detailMassage.expireTime" :disabled="true"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="slide-footer" class="dialog-footer">
        <el-button size="mini" @click="closeSlide">取消</el-button>
      </span>
    </slide-box>
  </div>
</template>

<script>
import request from '@/utils/request'
import SlideBox from '@/components/SlideBox'
import CrLoading from '@/components/CrLoading'
export default {
  components:{
    SlideBox,
    CrLoading
  },
  data() {
    return {
      operatePermission: false,
      loading: true,
      query: {
        name: ''
      },
      /* 更多查询 */
      moreSearch: false,
      searchForm: {
        pageNo: '1',
        pageSize: '10'
      },
      tableData:{
        data: null
      },
      /* 批量删除数据 */
      batchDelete: null,
      detailDialog: false,
      detailMassage: {} ,
      closeBtn: false
    }
  },
  created() {
    this.fetchCacheManegeTable()
  },
  mounted() {
    this.enterKeyEv()
    // this.$store.getters.userInfo.menu.forEach(item => {
    //   // console.log(item)
    //   item.children.forEach(subItem => {
    //     if(subItem.name === '缓存管理'){
    //       this.operatePermission = subItem.operationIds.indexOf('3') !== -1
    //       console.log(subItem.name+'页面操作权限',this.operatePermission)
    //     }
    //   })
    // })
  },
  beforeDestroy() {
    $(document).off('keyup')
  },
  methods: {
    // 关闭滑框
    closeSlide(){
      this.$refs.slideBox.close()
    },
    slideClose(){
      this.detailDialog = false
    },
    showMoreSearch(){
      const self = this;
      if(!this.moreSearch){

        $('.more-search-box').stop().animate({'height':$('.more-search-box .el-form').height()+'px','margin-top':'15px'},300)
      }else{

        $('.more-search-box').stop().animate({'height':'0','margin-top':'0'},300)
      }
      this.moreSearch = !this.moreSearch
    },
    /****** 查询重置、获取状态码start*******/
    resetQuery(){
      this.searchForm = {
        pageNo: '1',
        pageSize: '10'
      }
    },
    //回车搜索
    enterKeyEv(){
      const self = this
      $(document).on('keyup',function(ev){
        // console.log(ev.keyCode === 13, $('#test-select').is(':focus'))
        // console.log($('#test-select').val())
        if(ev.keyCode === 13&&$('.do-search-input').is(':focus')){
          self.fetchCacheManegeTable()
        }
      });
    },
    /****** 查询重置、获取状态码end*******/
    /******** 缓存管理界面table start ********/
    fetchCacheManegeTable(val){
      this.loading = true
      const obj = {}
      Object.keys(this.searchForm).forEach(key => {
        obj[key] = this.searchForm[key]
      })
      if(val==='1'){// 更多查询
        this.query.name = ''
        obj.pageNo = '1'
        this.searchForm.pageNo = '1'
      }else if(val==='2'){// 分页
        if(this.query.name === ''){ // 更多查询分页

        }else{// 模糊查询分页
          obj.key = this.query.name
        }
      }else{// 模糊查询
        this.resetQuery()
        // 清空再赋值
        Object.keys(this.searchForm).forEach(key => {
          obj[key] = this.searchForm[key]
        })
        obj.key = this.query.name
      }
      request({
        url: '/api/platform/cache/query_cache',
        method: 'get',
        params: this.searchForm
      }).then(({ data }) => {
        if(data.code === 200){
          const self = this
          setTimeout(function(){
            self.loading = false
            self.tableData.data = data.data.list
            self.tableData.page = parseInt(data.data.count)
          },300)
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{

        }
      })
    },
    /******** 缓存管理界面table end ********/
    // table选择
    handleSelectionChange(val) {
      this.batchDelete = val
    },
    // 批量删除
    AllSource(){
      let idData = [];
      // 先判断this.batchDelete是否为null 或为空
      if(this.batchDelete){
        this.batchDelete.forEach(function(currentValue, index, arr){
          idData.push(
            currentValue.key
          )
        })
        request({
          url: '/api/platform/cache/del_cache',
          method: 'post',
          data: idData
          }).then(({data}) => {
            if(data.code == 200){
              this.$message.success({
                message: '删除成功'
              });
              this.fetchCacheManegeTable();
            }else if(data.code === 4010){
              this.$store.dispatch('LogOut').then(() => {
                location.reload()
              })
            } else{
              this.$message.error({
                message: data.message
              })
            }
          })
      }
    },
    // 单个删除
    deleteSource(key) {
      if(this.operatePermission){
        let idData = [];
        idData.push(key);
        this.$confirm('确定删除此该缓存？', '提示', {
          type: 'warning'
        }).then(() => {
          request({
            url: '/api/platform/cache/del_cache',
            method: 'post',
            data: idData
            }).then(({data}) => {
              if(data.code == 200){
                this.$message.success({
                  message: '删除成功'
                });
                this.fetchCacheManegeTable();
              }else if(data.code === 4010){
                this.$store.dispatch('LogOut').then(() => {
                  location.reload()
                })
              } else{
                this.$message.error({
                  message: data.message
                })
              }
            })
          }
        ).catch(() => {
        })
      }
    },
    // 详情显示
    accordDetails(content){
      this.detailDialog = true;
      let paramsData = {
        key: content.key,
        expireTime: content.expireTime
      }
      request({
        url: '/api/platform/cache/get_cache',
        method: 'post',
        data: paramsData
      }).then(({ data }) => {
        if(data.code === 200){
          this.detailMassage = data.data;
          console.log('date', data)
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        }else{

        }
      })
    },
    // el-table的背景边框改变函数
    titleBgColor({row, rowIndex}){
      return 'title-bg-color'
    }
  },
  filters: {
    textLimit: function(val, num){
      if(val){
        let temp = val ;
        if(val.length>num){
            temp = val.substring(0, num)+"...";
        }
        return temp;
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.section-bod{
  background: transparent;
  border: none;
  box-shadow: none;
}
.components-nav {
  padding-bottom: 15px;
  background: #f1f1f1;
  line-height: 28px;
  .titile {
    color: #444;
    font-size: 23px;
  }
}
.form-body {
  padding: 6px 30px;
  height: 260px;
  overflow:auto;
}
</style>
<style>
.title-bg-color>th{
  background-color: rgba(246,247,250,1) !important;
}
</style>
