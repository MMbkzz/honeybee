<template>
	<div>
    <!--<span class="title fl">平台用户管理</span>-->
    <nav class="components-nav">
      <div class="clearfix">
        <div class="fl">
          <h2 class="page-title">平台用户管理</h2>
        </div>
        <div class="search fr">
          <input class="do-search-input" type="text" style="padding-left: 15px; width: 200px; outline: none; border: 0; font-size: 12px; display: inline-block; height: 100%; float: left;" v-model="query.name" placeholder="请输入关键字">
          <a @click="fetchTableData()"><svg-icon icon-class="search"></svg-icon></a>
        </div>
        <div class="fr">
          <el-button class="add-btn" type="primary" @click="handleCreate" :disabled="!operatePermission"><i>+</i>新增</el-button>
        </div>
      </div>
    </nav>
    <section class="components-container section-bod clearfix">
      <!-- <el-table :data="tableData.data" stripe style="width: 100%" cell-class-name="nowrap">
        <el-table-column type="index" width="50" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="name" label="用户名" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="角色" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span v-for="(role ,index) in scope.row.roleList" :key="index">
              {{role.name}}
              {{index!=scope.row.roleList.length-1?',':''}}
            </span>
            <template v-for="(role ,index) in scope.row.roleList">{{role.name}}{{index!=scope.row.roleList.length-1?',':''}}</template>

          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="mobile" label="责任人" :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="状态" width="50" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            {{ scope.row.status | statusFilter }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300">
          <template slot-scope="scope">
            <a title="编辑" @click="handleUpdate(scope.row)"><svg-icon icon-class="editor" style="width: 20px; height: 20px"></svg-icon></a>
            <a title="删除" @click="deleteSource(scope.row.id)"><svg-icon icon-class="delete" style="width: 20px; height: 20px"></svg-icon></a>
            <a title="改密" @click="changePassword(scope.row.id)"><svg-icon icon-class="changepwd" style="width: 20px; height: 20px"></svg-icon></a>
            <a title="停用" v-if="scope.row.status == '1'" @click="handleUserStatus(scope.row.id, '0')"><svg-icon icon-class="stop" style="width: 22px; height: 22px"></svg-icon></a>
            <a title="启用" v-else @click="handleUserStatus(scope.row.id, '1')"><svg-icon icon-class="display" style="width: 20px; height: 20px"></svg-icon></a>

          </template>
        </el-table-column>
      </el-table> -->
      <cr-loading v-show="loading"></cr-loading>
      <cr-card
        :operatePermission="operatePermission"
        v-show="!loading"
        @edit="handleUpdate"
        @editPwd="changePassword"
        @delete="deleteUser"
        @handleSwitchClick="handleUserStatus"
        @browse="handleView"
        :cardData="tableData.data"
        :fieldsData="fieldsData">
      </cr-card>
      <el-pagination
        background
        @size-change="(size)=>{query.pageSize =size;this.fetchTableData()}"
        @current-change="(current)=>{query.pageNo =current;this.fetchTableData()}"
        :page-size="10"
        :current-page="1"
        layout="prev, pager, next"
        :total="tableData.page.totalElements"
        class="fr mt-15">
      </el-pagination>
    </section>

    <!--新增与修改-->
		<!-- <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="550px">

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取消</el-button>
				<el-button v-if="dialogStatus=='create'" type="primary" @click="userCreatSub(user)">创建</el-button>
				<el-button v-else type="primary" @click="userCreatSub(user)">确认</el-button>
			</div>
		</el-dialog> -->

    <!-- 滑框 -->
    <slide-box
      slideWidth="450px"
      :slideShow="dialogFormVisible"
      @close="slideClose"
      :title="textMap[dialogStatus]"
      ref="slideBox">
      {{ dialogStatus }}
      <div slot="slide-content">
        <el-form id="user-form" :rules="userRule" ref="usersForm" :model="user" label-width="80px" label-position="right" style="padding: 0 20px;">
          <el-form-item label="ldap账号" prop="ldapUser" class="mt-15">
            <el-input v-model="user.ldapUser" size="medium" :disabled="dialogStatus=='view'||dialogStatus=='update'"></el-input>
            <img v-if="dialogStatus == 'create' || dialogStatus == 'update'" src="../../assets/tdspic/synchronous.png" class="synchronous-btn" title="ldap用户同步" alt="ldap用户同步" @click.stop="userSync()">
          </el-form-item>
          <el-form-item label="用户账号" prop="loginName" class="mt-15">
            <el-input v-model="user.loginName" size="medium" :disabled="dialogStatus == 'view'"></el-input>
            <!-- <img v-if="dialogStatus == 'create'" src="../../assets/tdspic/synchronous.png" class="synchronous-btn" title="ldap用户同步" alt="ldap用户同步" @click.stop="userSync()"> -->
          </el-form-item>
          <el-form-item label="密码" prop="password" v-if="dialogStatus == 'create'" class="mt-15">
            <el-input type="password" v-model="user.password" size="medium"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="repassword" v-if="dialogStatus == 'create'" class="mt-15">
            <el-input type="password" v-model="user.repassword" size="medium"></el-input>
          </el-form-item>
          <el-form-item label="用户名" prop="name" class="mt-15">
            <el-input v-model="user.name" size="medium" :disabled="dialogStatus == 'view'"></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="sex" class="mt-15">
            <el-select v-model="user.sex" placeholder="性别" :disabled="dialogStatus == 'view'">
              <el-option v-for="item in sex" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="角色" prop="roles" class="mt-15">
            <el-select v-model="user.roles" placeholder="请选择" size="medium" :disabled="dialogStatus == 'view'">
              <el-option v-for="item in roles" :key="item.id" :label="item.status=='1'?item.name:item.name+'(已禁用)'" :value="item.id" :disabled="item.status=='0'">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="邮箱" prop="email" class="mt-15">
            <el-input v-model="user.email" size="medium" :disabled="dialogStatus == 'view'"></el-input>
          </el-form-item>
          <el-form-item label="联系电话" prop="mobile" class="mt-15">
            <el-input v-model="user.mobile" size="medium" :disabled="dialogStatus == 'view'"></el-input>
          </el-form-item>
          <el-form-item label="状态:" class="mt-15" style="color: #606266">
            <span v-if=" user.status == '1' ">有效</span>
            <span v-else-if=" user.status == '0' ">无效</span>
          </el-form-item>
        </el-form>
      </div>

      <span slot="slide-footer">
        <el-button size="mini" :disabled="submitBtnDisabled" v-if="dialogStatus=='create'" type="primary" @click="userCreatSub(user)">保存</el-button>
				<el-button size="mini" :disabled="submitBtnDisabled" v-else-if="dialogStatus=='update'" type="primary" @click="userCreatSub(user)">保存</el-button>
        <el-button size="mini" @click="closeSlide">取消</el-button>
      </span>
    </slide-box>

    <!--修改密码-->
	  <!-- 	<el-dialog
      title="修改密码"
      :visible.sync="dialogFormVisible2"
      width="500px">
			<el-form :rules="passwordRule" ref="editUserPwd" :model="newPassword" label-width="80px" label-position="right" style="padding: 10px 30px">
				<el-form-item label="原始密码" prop="password0" class="wd90 mb-16">
					<el-input type="password" v-model="newPassword.password0"></el-input>
				</el-form-item>
        <el-form-item label="新密码" prop="password1" class="wd90 mb-16">
					<el-input type="password" v-model="newPassword.password1"></el-input>
				</el-form-item>
				<el-form-item label="重复密码" prop="password2" class="wd90 mb-16">
					<el-input type="password" v-model="newPassword.password2"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible2 = false">取消</el-button>
				<el-button type="primary" @click="passwordChange()">确认</el-button>
			</div>
		</el-dialog> -->
	</div>
</template>

<script>
import request from '@/utils/request'

import { pattern } from '@/utils/validate'

import ScrollPane from '@/components/ScrollPane'
import CrCard from '@/components/CrCard'
import SlideBox from '@/components/SlideBox'

import { changePass, deleteUser } from '@/api/user'
import CrLoading from '@/components/CrLoading'
import { Message } from 'element-ui'

export default {
  components: {
    ScrollPane,
    CrCard,
    SlideBox,
    CrLoading
  },
  data() {
    return {
      operatePermission: false,
      submitBtnDisabled: false,
      loading: true,
      createStatus: '0',
      syncPwd: '',
      fieldsData:[
        {
          propTitle: '用户名',
          field: 'name'
        },
        {
          propTitle: '角色',
          field: 'roleListName'
        },
        {
          propTitle: '联系方式',
          field: 'mobile'
        },
        {
          propTitle: '邮箱',
          field: 'email'
        },
        {
          propTitle: '状态',
          field: 'status',
          switch: true
        }
      ],
      // 新增修改弹框
      dialogFormVisible: false,
      dialogFormVisible2: false,
      userId: '',
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新增',
        view: '详细信息'
      },
      roles: [],
      organizations: [],
      user: {
        ldapUser: '', // ldap用户
        name: '', // 用户账号
        password: '', // 密码
        repassword: '', // 确认密码
        loginName: '', // 用户名
        sex: '', // 性别
        email: '',
        mobile: '',
        roles: '',
        status: '1',
      },
      newPassword: {
        password0: '',
        password1: '',
        password2: ''
      },
      passwordRule: {
        password0: [
          { required: true, message: '请填写原始密码', trigger: 'blur' },
          { max: 12, message: '密码最多12位', trigger: 'blur' },
          { min: 6, message: '密码最少6位', trigger: 'blur' },
          { pattern: pattern.password, message: '密码由小写字母、大写字母、数字、特殊符号的两种及以上组成', trigger: 'blur' }
        ],
        password1: [
          { required: true, message: '请填写密码', trigger: 'blur' },
          { max: 12, message: '密码最多12位', trigger: 'blur' },
          { min: 6, message: '密码最少6位', trigger: 'blur' },
          { pattern: pattern.password, message: '密码由小写字母、大写字母、数字、特殊符号的两种及以上组成', trigger: 'blur' }
        ],
        password2: [
          { required: true, message: '请再填写确认密码', trigger: 'blur' },
          { validator: (rule, value, cb) => { if (value && value !== this.newPassword.password1) { cb(new Error('两次密码不一致')) } else { cb() } }, trigger: 'blur' }
        ]
      },
      userRule: {
        loginName: [
          { required: true, message: '请填写登录名', trigger: 'blur' },
          { max: 50, message: '用户名最多50位', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请填写密码', trigger: 'blur' },
          { max: 12, message: '密码最多12位', trigger: 'blur' },
          { min: 6, message: '密码最少6位', trigger: 'blur' },
          { pattern: pattern.password, message: '密码由小写字母、大写字母、数字、特殊符号的两种及以上组成', trigger: 'blur' }
        ],
        repassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { validator: (rule, value, cb) => { if (value && value !== this.user.password) { cb(new Error('两次密码不一致')) } else { cb() } }, trigger: 'blur' }
        ],
        name: [
          {
            required: true,
            message: '请填写用户名',
            trigger: 'blur'
          }
        ],
        email: [
          {
            type: 'email',
            message: '请输入正确的邮箱地址',
            trigger: 'blur'
          }
        ],
        mobile: [
          {
            pattern: pattern.phone,
            message: '请输入正确的手机号',
            trigger: 'blur'
          }
        ],

        organization: [
          {
            required: true,
            message: '请填写组织',
            trigger: 'blur'
          }
        ],
        roles: [
          {
            required: true,
            message: '请填写角色',
            trigger: 'blur'
          }
        ]
      },

      // 查询
      downloadLoading: false,
      query: {
        username: '',
        sex: '',
        status: '',
        organizationId: '',
        pageSize: '9',
        pageNo: '1'
      },
      sex: [
        {
          value: '1',
          label: '男'
        },
        {
          value: '2',
          label: '女'
        }
      ],
      status: [
        {
          value: '0',
          label: '停用'
        },
        {
          value: '1',
          label: '启用'
        }
      ],
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
    // 关闭滑框
    closeSlide(){
      this.$refs.slideBox.close()
    },
    slideClose(){
      this.dialogFormVisible = false
    },
    nodeEditStart(data) {
      // 记录修改前的值
      this.preName = data.name
      this.$set(data, 'showEdit', true)
      this.$nextTick(() => {
        this.$refs['innerInput' + data.id].focus()
      })
    },
    // 同步Idap用户信息
    userSync() {
      // console.log(this.user.ldapUser)
      const rex = /s/
      if (this.user.ldapUser === '' || this.user.ldapUser === undefined) {
        this.$message.error('请输入ldap账号！')
        return
      }
      request({
        url: '/api/auth/user/get_osp_user?loginName='+this.user.ldapUser,
        method: 'get',
        // params: {
        //   loginName: this.user.loginName
        // }
      }).then(({data}) => {
        if(data.code === 200){
          this.createStatus = '1'
          this.user.loginName = data.data.loginName
          this.user.name = data.data.name
          this.user.email = data.data.email
          this.user.mobile = data.data.mobile
          this.$message.success('同步成功！')
        } else {
          this.$message.error(data.message)
        }
      })
    },
    nodeEditExit(data) {
      // 值发生改变
      if (data.name != this.preName) {
        request({
          url: '/api/auth/organization/edit_organization',
          method: 'post',
          data: {
            id: data.id,
            name: data.name,
            parentId: data.parentId
          }
        }).then(({ data }) => {
          if (data.code === 200) {
            this.$message.success('修改成功')
            // 重新获取组织树数据
            this.fetchTree()
          }else if(data.code === 4010){
            this.$store.dispatch('LogOut').then(() => {
              location.reload()
            })
          } else {
            this.$message.error(data.message)
            this.fetchTree()
          }
        })
      }
      this.$set(data, 'showEdit', false)
    },
    nodeAdd(nodeData) {
      this.$prompt('请输入名称', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputValidator: val => {
          if (!val) {
            return '请输入信息'
          }
        }
      }).then(({ value }) => {
        request({
          url: '/api/auth/organization/add_organization',
          method: 'post',
          data: {
            name: value,
            parentId: nodeData.id
          }
        }).then(({ data }) => {
          if (data.code === 200) {
            this.$message.success('新增成功')
            // 重新获取组织树数据
            this.fetchTree()
          } else if(data.code === 4010){
            this.$store.dispatch('LogOut').then(() => {
              location.reload()
            })
          }else {
            this.$message.error(data.message)
          }
        })
      })
    },
    nodeDelete(node, data) {
      this.$confirm('此操作将删除该组织, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request({
          url: '/api/auth/organization/del_organization',
          method: 'post',
          data: {
            id: data.id
          }
        }).then(res => {
          if (res.data.code === 200) {
            this.$message.success('删除成功')
            this.fetchTree()
          }else if(data.code === 4010){
            this.$store.dispatch('LogOut').then(() => {
              location.reload()
            })
          } else {
            this.$message.error(res.data.message)
          }
          // 重新获取组织树数据

          //        const parent = node.parent;
          //        const children = parent.data.child || parent.data;
          //        const index = children.findIndex(d => d.id === data.id);
          //        children.splice(index, 1);
        })
      })
    },
    //回车搜索
    enterKeyEv(){
      const self = this
      $(document).on('keyup',function(ev){
        // console.log(ev.keyCode === 13, $('#test-select').is(':focus'))
        // console.log($('#test-select').val())
        if(ev.keyCode === 13&&$('.do-search-input').is(':focus')){
          self.fetchTableData()
        }
      });
    },
    /********** 获取所有用户table start ************/
    fetchTableData() {
      this.loading = true;
      this.tableData.data = []
      request({
        url: '/api/auth/user/get_users',
        method: 'get',
        params: {
          queryString: this.query.name,
          pageNo: this.query.pageNo,
          pageSize: this.query.pageSize
        }
      }).then(({ data }) => {
        const self = this
        setTimeout(function(){
          self.loading = false
          if(data.code == '200'){
            let tableData
            self.tableData.page = data.data.page
            tableData = JSON.parse(JSON.stringify(data.data.data))
            for(var i=0; i<tableData.length; i++){
              tableData[i].roleListName = []
              for(var j=0; j<tableData[i].roleList.length; j++){
                tableData[i].roleListName.push(
                  tableData[i].roleList[j].name
                )
              }
            }
            for(var k=0; k<tableData.length; k++){
              tableData[k].roleListName = tableData[k].roleListName.join(',')
            }
            self.tableData.data = tableData
          }else if(data.code === 4010){
            self.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
          }
        },300)

      })
    },
    //  点击分页总接口
    handleCurrentChange(val) {
      request({
        url: '/api/auth/user/get_users',
        method: 'get',
        params: {
          page_current: val,
          page_length: '10'
        }
      }).then(({ data }) => {
        if(data.code == 200){
          this.tableData = data.data.list
          this.count = parseInt(data.data.page.count)
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
           location.reload()
         })
        }

      })
    },
    /********** 获取所有用户table end ************/
    /********* 新增、编辑start *********/
      // 新增用户
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.createStatus = '0'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['usersForm'].clearValidate()
      })
    },
      // 编辑用户
    handleUpdate(row) {
      // this.user = this._.cloneDeep(row)
      if (this.$refs.usersForm) {
        this.$refs.usersForm.resetFields()
      }
      this.user.ldapUser = row.ldapUser
      this.user.loginName = row.loginName
      this.user.id = row.id
      this.user.name = row.name
      this.user.organization = row.organization
      this.user.email = row.email
      this.user.mobile = row.mobile
      this.user.sex = row.sex
      this.user.roles = row.roles
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
    },
      //  提交新增与编辑
    userCreatSub(userInfo) {
      this.$refs.usersForm.validate(valid => {
        if (valid) {
          this.submitBtnDisabled = true
          if (this.dialogStatus === 'create') {
            const param = this._.cloneDeep(userInfo)
            param.organization = userInfo.organization
            param.roles = userInfo.roles
            // if (this.createStatus === '1'){//表示同步新增的用户
            //   // param.password = this.syncPwd
            //   param.ldapUser = this.user.loginName
            // }
            delete param.repassword
            delete param.status
            delete param.id
            console.log(param)
            request({
              url: '/api/auth/user/add_user',
              method: 'post',
              data: param
            }).then(({ data }) => {
              if (data.code === 200) {
                this.fetchTableData()
                this.closeSlide()
                this.$message.success('新增成功')
              }else if(data.code === 4010){
                this.$store.dispatch('LogOut').then(() => {
                  location.reload()
                })
              } else {
                this.$message.error(data.message)
              }
              this.submitBtnDisabled = false
            })
          } else if (this.dialogStatus === 'update') {
            const param = this._.cloneDeep(userInfo)
            console.log(userInfo)
            param.roles = userInfo.roles
            // param.roles = this.roles.find(item => item.name === param.roles).id
            // console.log(param.roles)
            delete param.repassword
            delete param.password
            request({
              url: '/api/auth/user/edit_user',
              method: 'post',
              data: param
            }).then(({ data }) => {
              if (data.code === 200) {
                this.$message.success('编辑成功')
                this.fetchTableData()
                this.closeSlide()
              }else if(data.code === 4010){
                this.$store.dispatch('LogOut').then(() => {
                  location.reload()
                })
              } else {
                this.$message.error(data.message)
              }
              this.submitBtnDisabled = false
            })
          }
        }
      })
    },
      // 查看
    handleView(row){
      console.log(row)
      this.dialogStatus = 'view'
      this.dialogFormVisible = true
      if (this.$refs.usersForm) {
        this.$refs.usersForm.resetFields()
      }
      this.user.ldapUser = row.ldapUser
      this.user.loginName = row.loginName
      this.user.id = row.id
      this.user.name = row.name
      this.user.organization = row.organization
      this.user.email = row.email
      this.user.mobile = row.mobile
      this.user.sex = row.sex
      this.user.roles = row.roleList[0].name
    },
    /********* 新增、编辑start *********/

    /******** 删除、重置、修改密码start *********/
      // 重置
    reset() {
      this.query = Object.assign(this.query, {
        username: '',
        sex: '',
        status: ''
      })
      this.fetchTableData()
    },
      // 删除用户
    deleteUser(id) {
      this.$confirm('确定删除此用户？', '提示', {
        type: 'warning'
      }).then(() => {
        deleteUser(id.id).then(({ data }) => {
          if (data.code === 200) {
            this.$message.success('删除用户成功')
            this.fetchTableData()
          } else {
            this.$message.error(data.message)
          }
        })
      })
    },
      // 修改密码
    changePassword(id) {
      console.log('修改密码', id)
      this.dialogFormVisible2 = true
      // 重置表单
      if (this.$refs.editUserPwd) {
        this.$refs.editUserPwd.resetFields()
      }
      this.userId = id
    },
      // 提交修改密码
    passwordChange() {
      this.$refs.editUserPwd.validate(valid => {
        if (valid) {
          changePass(this.userId, this.newPassword.password1, this.newPassword.password0).then(({ data }) => {
            if (data.code === 200) {
              Message.success('修改密码成功')
              this.dialogFormVisible2 = false
            } else {
              Message.error(data.message)
            }
          })
        }
      })
    },
    resetTemp() {
      this.user = {
        name: '', // 用户账号
        password: '', // 密码
        repassword: '', // 确认密码
        loginName: '', // 用户名
        sex: '', // 性别
        email: '',
        mobile: '',
        roles: '',
        status: '1',
      }
    },
    /******** 删除、重置、修改密码end *********/
    /******** 启用、停用start ********/
      // 启用或停用
    handleUserStatus(val) {
      // 0 为禁用 1 为启用
      val.state = val.state?'1':'0'
      let data = {
        id: val.item.id,
        status: val.state
      }
      request({
        url: '/api/auth/user/start_stop_user',
        method: 'post',
        data: data
      }).then(({ data }) => {
        if (data.code == '200') {
          if(val.state == '1'){
            this.$message.success({ message: '启动成功' });
          }else{
            this.$message.success({ message: '禁用成功' });
          }
          this.fetchTableData()
        }else if(data.code === 4010){
          this.$store.dispatch('LogOut').then(() => {
            location.reload()
          })
        } else if (data.code == '400') {
          this.$message({
            type: 'error',
            message: data.message
          })
        }
      })
    },
    fetchRoles() {
      request({
        url: '/api/auth/role/get_page_roles',
        method: 'get',
        params: {
          pageNo: '1',
          pageSize: '99999'
        }
      }).then(({ data }) => {
        this.roles = data.data.data
      })
    },
    /******** 启用、停用end ********/

  },
  mounted() {
    this.$store.getters.userInfo.menu.forEach(item => {
      item.children.forEach(subItem => {
        if(subItem.name === '平台用户管理'){
          this.operatePermission = subItem.operationIds.indexOf('3') !== -1
          console.log(subItem.name+'页面操作权限',this.operatePermission)
        }
      })
    })
    this.fetchTableData()
    this.fetchRoles()
    this.enterKeyEv()
  },
  beforeDestroy() {
    $(document).off('keyup')
  }
}

</script>
<style lang="scss" scoped>
#user-form{
  .el-input,.el-select{
    width: 90%;
  }
  .synchronous-btn{
    width: 26px;
    height: 26px;
    position: absolute;
    top: 50%;
    margin-top: -13px;
    right: 0;
    cursor: pointer;
  }
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
.user-tree-wrap {
  width: 200px;
  height: 100%;
  border: 1px solid #dddddd;
  // overflow-y: scroll;
  // overflow-y: auto;
  // overflow: auto;
  position: relative;
  overflow: hidden;
  .tree-container {
    // height: 100%;
    position: absolute;
    overflow: auto;
    left:0;
    right: 0;
    top: 40px;
    bottom: 0;
  }
}

.user-tree-header {
  padding: 10px;
  border-bottom: 1px solid #dddddd;
}

.user-list-bar {
  padding-bottom: 10px;
  margin-bottom: 10px;
  border-bottom: 1px solid #dddddd;
}

.el-tree-node__content {
  height: 36px !important;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
  &:hover {
    .custom-tree-node-option {
      opacity: 1;
    }
  }
}

.custom-tree-node-option {
  opacity: 0;
  transition: opacity 0.5s ease-in-out;
}
// 修改样式

.components-container {
  overflow: hidden;
  /deep/ .el-pagination {
    padding-bottom: 20px;
  }

  /deep/ .el-tree {
    // overflow: auto;
  }
  /deep/ .el-tree-node>.el-tree-node__children {
    overflow: visible;
  }
  /deep/ .el-tree-node__content {
    min-width: 198px;
  }
}

.resource-tree-inner-input {
  width: 150px;
}
.w525{
    width: 525px;
}
.mb-16{
    margin-bottom: 16px;
}
</style>