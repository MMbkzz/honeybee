<template>
    <div>
      <!--<span class="title fl">平台角色管理</span>-->
      <nav class="components-nav">
        <div class="clearfix">
          <div class="fl">
            <h2 class="page-title">平台角色管理</h2>
          </div>
          <div class="search fr">
            <input class="do-search-input" type="text" style="padding-left: 15px; width: 200px; outline: none; border: 0; font-size: 12px; display: inline-block; height: 100%; float: left;" v-model="query.name" placeholder="请输入关键字"></input>
            <a @click="fetchRoles()"><svg-icon icon-class="search"></svg-icon></a>
          </div>
          <div class="fr">
            <el-button class="add-btn" type="primary" @click="handleCreate" :disabled="!operatePermission"><i>+</i>新增</el-button>
          </div>
        </div>
        <!--<el-input style="width: 160px;" v-model="query.name" placeholder="角色名、编码、描述"></el-input>
        <el-select v-model="query.state" placeholder="状态" style="width: 160px">
        <el-option
          v-for="item in status"
          :key="item.id"
          :label="item.label"
          :value="item.id">
        </el-option>
        </el-select>
          <el-button type="primary" @click="fetchRoles()">查询</el-button>
          <el-button type="primary" @click="reset">重置</el-button>
          <el-button type="primary" @click="handleCreate">新增</el-button>-->
      </nav>
      <section class="components-container clearfix section-bod">
        <!-- <el-table
            :data="tableData.data"
            style="width: 100%">
            <el-table-column type="index">
            </el-table-column>
            <el-table-column label="角色编码" prop="code">
              </el-table-column>
            <el-table-column label="角色名" prop="name">
            </el-table-column>
            <el-table-column label="角色描述" prop="descr">
            </el-table-column>
            <el-table-column label="状态" >
                <template slot-scope="scope">
                    {{scope.row.status | statusFormat }}
                </template>
            </el-table-column>
            <el-table-column label="操作" width="250">
            <template slot-scope="scope">
              <a title="编辑" @click="handleUpdate(scope.row)" v-if="scope.row.status === '1'"><svg-icon icon-class="editor" style="width: 20px; height: 20px"></svg-icon></a>
              <a title="删除" @click="deleteRole(scope.row.id)"><svg-icon icon-class="delete" style="width: 20px; height: 20px"></svg-icon></a>
              <a title="停用" v-if="scope.row.status == '1'" @click="handleRoleStatus(scope.row.id, '0')"><svg-icon icon-class="stop" style="width: 22px; height: 22px"></svg-icon></a>
              <a title="启用" v-else @click="handleRoleStatus(scope.row.id, '1')"><svg-icon icon-class="display" style="width: 20px; height: 20px"></svg-icon></a>
            </template>
            </el-table-column>
        </el-table> -->
        <cr-loading v-show="loading"></cr-loading>
        <cr-card
          v-show="!loading"
          :operatePermission="operatePermission"
          @edit="handleUpdate"
          @delete="deleteRole"
          @browse="handleView"
          @handleSwitchClick="handleRoleStatus"
          :cardData="tableData.data"
          :fieldsData="fieldsData">
        </cr-card>
        <el-pagination
          background
          @size-change="(size)=>{query.page_length =size;fetchRoles()}"
          @current-change="(current)=>{query.page_current =current;fetchRoles();}"
          :page-size="query.page_length"
          :current-page="query.page_current"
          layout="prev, pager, next"
          :total="tableData.page.totalElements"
          class="fr mt-10">
        </el-pagination>
      </section>
      <!--编辑与新增界面-->
      <!-- <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="800px">
        <el-form :rules="rolesRule" :model="roleInfo" ref="dataForm" label-width="80px" label-position="right" style='width: 85%; margin-left:50px;'>
            <el-form-item label="角色编码" prop="code" class="mb-16">
                <el-input v-model="roleInfo.code"></el-input>
            </el-form-item>
            <el-form-item label="角色名" prop="name" class="mb-16">
                <el-input v-model="roleInfo.name"></el-input>
            </el-form-item>
            <el-form-item label="角色描述" prop="descr" class="mb-16">
                <el-input v-model="roleInfo.descr"></el-input>
            </el-form-item>
            <el-form-item label="角色状态" prop="status" v-if="dialogStatus==='update'" class="mb-16">
              <el-select v-model="roleInfo.status"  placeholder="请选择" style="width: 100%;" >
                <el-option
                  v-for="item in status"
                  :key="item.id"
                  :label="item.label"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="角色权限">
              <el-tree
                :data="organizationTreeData"
                :expand-on-click-node='false'
                default-expand-all
                node-key="id"
                ref="tree"
                :default-checked-keys="roleInfo.permissions"
                :props="defaultProps">
                <div class="custom-tree-node" slot-scope="{ node, data }">
                  <span>{{ data.resource.name }}</span>
                  <span class="custom-tree-node-option">
                    <el-checkbox-group v-model="data.checks">
                      <el-checkbox v-for="(choice,index) of data.operations" :disabled="data.resource.status == '0'" :label="choice.id" :key="index">{{choice.name}}</el-checkbox>
                    </el-checkbox-group>
                  </span>
                </div>
              </el-tree>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取消</el-button>
            <el-button v-if="dialogStatus=='create'" type="primary" @click="roleCreatSub()" >创建</el-button>
            <el-button v-else type="primary"  @click="roleCreatSub()" >确认</el-button>
        </div>
      </el-dialog> -->


      <!-- 滑框 -->
      <slide-box slideWidth="600px" :slideShow="dialogFormVisible" @close="slideClose" :title="textMap[dialogStatus]" ref="slideBox">
        <div slot="slide-content">
          <el-form :rules="rolesRule" :model="roleInfo" ref="dataForm" label-width="80px" label-position="right" style='width: 85%; margin-left:50px;'>
              <el-form-item label="角色编码" prop="code" class="mb-16">
                  <el-input v-model="roleInfo.code" :disabled="dialogStatus == 'view'"></el-input>
              </el-form-item>
              <el-form-item label="角色名" prop="name" class="mb-16">
                  <el-input v-model="roleInfo.name" :disabled="dialogStatus == 'view'"></el-input>
              </el-form-item>
              <el-form-item label="角色描述" prop="descr" class="mb-16">
                  <el-input v-model="roleInfo.descr" :disabled="dialogStatus == 'view'"></el-input>
              </el-form-item>
              <el-form-item label="角色状态" prop="status" v-if="dialogStatus==='update' || dialogStatus == 'view'" class="mb-16">
                <el-select v-model="roleInfo.status"  placeholder="请选择" style="width: 100%;" :disabled="dialogStatus == 'view'">
                  <el-option
                    v-for="item in status"
                    :key="item.id"
                    :label="item.label"
                    :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="角色权限">
                <fold-table-check :data="organizationTreeData" :checkDisabled="dialogStatus === 'view'" :allExpand="true"></fold-table-check>
              </el-form-item>
          </el-form>
        </div>
        <span slot="slide-footer">
          <el-button size="mini" v-if="dialogStatus=='create'" type="primary" @click="roleCreatSub()" >保存</el-button>
          <el-button size="mini" v-else-if="dialogStatus=='update'" type="primary"  @click="roleCreatSub()" >保存</el-button>
          <el-button size="mini" @click="closeSlide">取消</el-button>
        </span>
      </slide-box>
    </div>
</template>

<script>
import request from '@/utils/request'
import CrCard from '@/components/CrCard'
import SlideBox from '@/components/SlideBox'
import FoldTableCheck from '@/components/FoldTableCheck'
import CrLoading from '@/components/CrLoading'
import { checkCode, checkName, getPermissionTree, deleteRole } from '@/api/roles.js'
export default {
  components:{
    CrCard,
    SlideBox,
    FoldTableCheck,
    CrLoading
  },
  data() {
    return {
      operatePermission: false,
      loading: true,
      tableData: {
        data: [],
        page: {}
      },
      fieldsData:[
        {
          propTitle: '角色编码',
          field: 'code'
        },
        {
          propTitle: '角色名',
          field: 'name'
        },
        {
          propTitle: '角色描述',
          field: 'descr'
        },
        {
          propTitle: '状态',
          field: 'status',
          switch: true
        }
      ],
      organizationTreeData: [],
      checkedTreeNodes: [],
      count: 0,
      dialogFormVisible: false,
      dialogStatus: '',
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      textMap: {
        update: '编辑',
        create: '新增',
        view: '详细信息'
      },
      roleInfo: {
        code: '',
        name: '',
        descr: '',
        status: '1',
        permissionVos: []
      },
      rolesRule: {
        code: [
          { required: true, message: '请输入角色编码', trigger: 'blur' },
          { max: 20, message: '角色编码不超过20位', trigger: 'blur' },
          { pattern: /^[0-9a-zA-Z_]{1,20}$/, message: '角色编码由数字，字母或下划线组成。', trigger: 'blur' },
          { validator: (rule, val, cb) => {
            if (this.roleInfo.id) {
              checkCode(val, this.roleInfo.id).then(({ data }) => {
                if (data.code === 200) {
                  if (data.data) {
                    cb(new Error('角色编码已存在，请重新输入'))
                  } else {
                    cb()
                  }
                }
              })
            } else {
              checkCode(val).then(({ data }) => {
                if (data.code === 200) {
                  if (data.data) {
                    cb(new Error('角色编码已存在，请重新输入'))
                  } else {
                    cb()
                  }
                }
              })
            }
          }, trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请填写角色名', trigger: 'blur' },
          { validator: (rule, val, cb) => {
            if (this.roleInfo.id) {
              checkName(val, this.roleInfo.id).then(({ data }) => {
                if (data.code === 200) {
                  if (data.data) {
                    cb(new Error('角色名已存在，请重新输入'))
                  } else {
                    cb()
                  }
                }
              })
            } else {
              checkName(val).then(({ data }) => {
                if (data.code === 200) {
                  if (data.data) {
                    cb(new Error('角色名已存在，请重新输入'))
                  } else {
                    cb()
                  }
                }
              })
            }
          }, trigger: 'blur' }
        ],
        descr: [{ required: true, message: '请填写角色描述', trigger: 'blur' }],
        status: [{ required: true, message: '请选择是否启用', trigger: 'blur' }]
      },
      permis: [],
      query: {
        name: '',
        state: '',
        page_current: 1,
        page_length: 10
      },
      status: [
        { id: '0', label: '停用' },
        { id: '1', label: '启用' }
      ],
      operationIds: []
    }
  },
  mounted() {
    this.$store.getters.userInfo.menu.forEach(item => {
      item.children.forEach(subItem => {
        if(subItem.name === '平台角色管理'){
          this.operatePermission = subItem.operationIds.indexOf('3') !== -1
          console.log(subItem.name+'页面操作权限',this.operatePermission)
        }
      })
    })
    this.enterKeyEv()
  },
  methods: {
    // 关闭滑框
    closeSlide(){
      this.$refs.slideBox.close()
    },
    slideClose(){
      this.dialogFormVisible = false
    },

    // 重置
    reset() {
      this.query.name = ''
      this.query.state = ''
      this.query.page_current = 1
      this.fetchRoles()
    },
    beforeDestroy() {
      $(document).off('keyup')
    },
    //回车搜索
    enterKeyEv(){
      const self = this
      $(document).on('keyup',function(ev){
        // console.log(ev.keyCode === 13, $('#test-select').is(':focus'))
        // console.log($('#test-select').val())
        if(ev.keyCode === 13&&$('.do-search-input').is(':focus')){
          self.fetchRoles()
        }
      });
    },
    /********* 获取角色数据start **********/
      // 获取所有角色列表
    fetchRoles() {

      this.loading = true;

      request({
        url: '/api/auth/role/get_page_roles',
        method: 'get',
        params: {
          queryString: this.query.name,
          pageNo: this.query.page_current,
          pageSize: this.query.page_length
        }
      }).then(({ data }) => {
        const self = this
        setTimeout(function(){
          self.loading = false
          if(data.code == 200){
            self.tableData.data = data.data.data
            self.tableData.page = data.data.page
          }else if(data.code === 4010){
            self.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
          }else {

          }
        },300)

      })
    },
    /********* 获取角色数据end **********/
    /******** 新增、编辑start *********/
      // 新增角色
    handleCreate() {

      this.setFixedPage()
      // console.log(this.organizationTreeData)
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.operationIds = []
      this.clearChecks(this.organizationTreeData)
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
      // 编辑角色
    handleUpdate(row) {

      this.setFixedPage()
      this.roleInfo = this._.cloneDeep(row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.operationIds = []
      this.clearChecks(this.organizationTreeData)
      for (let i = 0; i < row.permissionVos.length; i++) {
        this.setOperationIds(this.organizationTreeData, row.permissionVos[i])
      }
    },
      // 提交新增与编辑
    roleCreatSub() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          this.getOperationIds(this.organizationTreeData)
          if (this.dialogStatus === 'create') {
            this.roleInfo.status = '1'
            const permissionsArr = [
              {resourceId:21, ops: '1'},
              {resourceId:22, ops: '1'},
              {resourceId:32, ops: '1'},
              {resourceId:33, ops: '1'}
            ]
            const param = this._.cloneDeep(this.roleInfo)
            // param.permissions = this.$refs.tree.getCheckedNodes().map((data) => data.id).join(',')
            param.permissionVos = this.operationIds.map(item => {
              item.ops = item.ops.join(',')
              return item
            })
            permissionsArr.forEach(item => {
              param.permissionVos.push(item)
            })
            // console.log(param)
            request({
              url: '/api/auth/role/add_role_permission',
              method: 'post',
              data: param
            }).then(({ data }) => {
              if (data.code === 200) {
                this.dialogFormVisible = false
                // this.fetchRoles()
                this.reset()
                this.$message.success('新增角色成功')
              } else if(data.code === 4010){
                this.$store.dispatch('LogOut').then(() => {
                  location.reload()
                })
              }else {
                this.$message.error(data.message)
              }
            })
          } else if (this.dialogStatus === 'update') {
            // console.log(this.roleInfo)
            const param = {
              'id': this.roleInfo.id,
              'name': this.roleInfo.name,
              'etag': this.roleInfo.etag,
              'descr': this.roleInfo.descr,
              'status': this.roleInfo.status,
              'code': this.roleInfo.code
            }
            param.permissionVos = this.operationIds.map(item => {
              item.ops = item.ops.join(',')
              return item
            })
            const permissionsArr = [
              {resourceId:21, ops: '1'},
              {resourceId:22, ops: '1'},
              {resourceId:32, ops: '1'},
              {resourceId:33, ops: '1'}
            ]
            permissionsArr.forEach(item => {
              param.permissionVos.push(item)
            })
            request({
              url: '/api/auth/role/edit_role_permission',
              method: 'post',
              data: param
            }).then(({ data }) => {
              if (data.code === 200) {
                this.dialogFormVisible = false
                // this.fetchRoles()
                this.reset()
                this.$message.success('编辑成功')
              }else if(data.code === 4010){
                this.$store.dispatch('LogOut').then(() => {
                  location.reload()
                })
              } else {
                this.$message.error(data.message)
              }
            })
          }
        }
      })
    },
     // 查看
    handleView(row){
      // console.log(this.organizationTreeData)
      this.roleInfo = this._.cloneDeep(row)
      this.dialogStatus = 'view'
      this.dialogFormVisible = true
      this.operationIds = []
      this.clearChecks(this.organizationTreeData)
      for (let i = 0; i < row.permissionVos.length; i++) {
        this.setOperationIds(this.organizationTreeData, row.permissionVos[i])
      }
    },
    /******** 新增、编辑end *********/
    // 删除角色
    deleteRole(val) {
      this.$confirm('确定删除此角色？', '提示', {
        type: 'warning'
      }).then(() => {
        deleteRole(val.id).then(({ data }) => {
          if (data.code === 200) {
            this.$message.success('删除成功')
            this.reset()
          } else {
            this.$message.error(data.message)
          }
        })
      })
    },
    resetTemp() {
      this.roleInfo = {
        'name': '',
        'descr': '',
        'status': '',
        'permissionVos': []
      }
    },
    // 启用停用
    handleRoleStatus(val) {
      val.state = val.state?'1':'0'
        request({
          url: '/api/auth/role/start_stop_role',
          method: 'post',
          data: {
            id: val.item.id,
            status: val.state
          }
        }).then(({ data }) => {
          if (data.code === 200) {
            if(val.state === '1'){
            this.$message.success({ message: '启动成功' });
          }else{
            this.$message.success({ message: '禁用成功' });
          }
            // this.fetchRoles()
            this.reset()
          }else if(data.code === 4010){
            this.$store.dispatch('LogOut').then(() => {
              location.reload()
            })
          } else {
            this.$message({
              type: 'error',
              message: data.message
            })
          }
        })
    },
    // 点击分页总接口
    // handleCurrentChange(val) {
    //   console.log(`每页 ${val} 条`)
    // },
    // handleSizeChange(val) {
    //   console.log(`每页 ${val} 条`)
    // },
    /*********** 权限处理函数start ***********/
    fetchTree() {
      request({
        url: '/api/auth/category/get_categorys',
        method: 'get'
      }).then(({ data }) => {
        // console.log(data)
        if(data.code == 200){
          this.organizationTreeData = data.data
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
           location.reload()
         })
        }
      })
    },
    // 清空选择
    clearChecks(arr) {
      for (let i = 0; i < arr.length; i++) {
        arr[i].checks = []
        if (arr[i].children.length) {
          this.clearChecks(arr[i].children)
        }
      }
    },
    // 设置数组
    setArr(arr) {
      for (let i = 0; i < arr.length; i++) {
        arr[i].checks = []
        if (arr[i].children.length) {
          this.setArr(arr[i].children)
        }
      }
    },
    // 取操作id
    getOperationIds(arr) {
      for (let i = 0; i < arr.length; i++) {
        if (arr[i].checks.length) {
          this.operationIds.push({ resourceId: arr[i].resource.id, ops: [...arr[i].checks] })
        }
        if (arr[i].children.length) {
          this.getOperationIds(arr[i].children)
        }
      }
    },
    // 设置编辑时候的操作id
    setOperationIds(arr, obj) {
      for (let j = 0; j < arr.length; j++) {
        if (arr[j].resource.id === obj.resourceId) {
          arr[j].checks = obj.ops.split(',')
          break
        } else if (arr[j].children.length) {
          this.setOperationIds(arr[j].children, obj)
        }
      }
    },
    // 默认首页，消息中心是固定访问页面
    setFixedPage(){
      this.organizationTreeData.forEach(item => {
        // console.log(item.resource.descr)
        if(item.resource.descr === '首页' || item.resource.descr ==='消息中心'){
          // console.log(item)
          item.checks = ['1']
          item.children.forEach(subItem => {
            subItem.checks = ['1']
          })
        }
      })
    }
    /*********** 权限处理函数end ***********/
  },
  created() {
    this.fetchRoles()
    // this.fetchTree()
    getPermissionTree().then(({ data }) => {
      // console.log(data)
      if (data.code === 200) {
        var temp = data.data
        this.setArr(temp)
        this.organizationTreeData = temp
        this.setFixedPage()
        // console.log(this.organizationTreeData)
      }
    })
  }
}
</script>

<style lang="scss" scoped>

.components-nav {
  padding-bottom: 15px;
  background: #f1f1f1;
  .titile {
    color: #444;
    font-size: 23px;
  }
}
  .listNone{
      list-style: none;
  }
  .listNone li{
      list-style: none;
  }
  .components-container {
      height: 100%;
  }
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
  .w525{
    width: 525px;
}
.mb-16{
    margin-bottom: 16px;
}
.tree-header{

  overflow: hidden;
  line-height: 30px;
  border-bottom: 1px solid #ccc;
  padding: 0 10px;
  &>div{
    font-size: 12px;
    color: #555;
  }
  div:nth-of-type(1) {
    float: left;
  }
  div:nth-of-type(2) {
    float: right;
    width: 50px;
    text-align: right;
  }
}
</style>