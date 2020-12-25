<template>
  <el-menu class="navbar" mode="horizontal">
    <!--展开收起-->
    <hamburger class="hamburger-container" :toggleClick="toggleSideBar" :isActive="sidebar.opened"></hamburger>
    <!--<breadcrumb class="breadcrumb-container"></breadcrumb>-->
    <!--右边用户管理-->
    <div class="right-menu">
      <el-dropdown class="avatar-container right-menu-item" trigger="click">
        
        <div class="avatar-wrapper">
          <span class="avatar-header">
            <i><img src="../../../assets/tdspic/user.png" alt=""></i>
          </span>
         <span>{{ userName.name }}</span>
         <span class="triangle"></span>
        </div>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item>
            <svg-icon icon-class="key"></svg-icon>
            <span @click="() => {editorDialog = true;this.resetForm()}">修改密码</span>
          </el-dropdown-item>
          <el-dropdown-item divided>
            <svg-icon icon-class="quit"></svg-icon>
            <span @click="logout">{{$t('navbar.logOut')}}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <a class="fr mr-20" href="./dsp-help/dsp-help.html" target="_blank" style="display:flex;height:100%;flex-direction: column;justify-content: center;">
      <img src="../../../assets/tdspic/help2.png" title="帮助文档">
    </a>
    <div class="fr mr-30">
      <router-link to="/message?page=warning">
        <el-badge type="danger" :value="messageCount">
          <img src="../../../assets/tdspic/info.png" style="position:relative;top:2px;">
        </el-badge>
      </router-link>
    </div>
    

    <!-- 修改密码弹框 -->
    <el-dialog
      :visible.sync="editorDialog"
      width="30%"
      custom-class="modifyPwdForm"
      center
      :show-close='foo'>
      <div class="form-body">
        <el-form  :rules="psdRule" ref="psdForm" :model="pwdForm" label-width="100px" >
          <el-form-item label="原密码:" prop="oldPwd">
            <el-input v-model="pwdForm.oldPwd"></el-input>
          </el-form-item>
          <el-form-item label="新密码:" prop="newPwd" class="mt-15">
            <el-input type="password" v-model="pwdForm.newPwd"></el-input>
          </el-form-item>
          <el-form-item label="确认密码:" prop="comfirmPwd" class="mt-20">
            <el-input type="password" v-model="pwdForm.comfirmPwd"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" type="primary" @click="modifyPwd(pwdForm)">确定</el-button>
        <el-button size="mini" @click="editorDialog = false;">取消</el-button>
      </span>
    </el-dialog>

  </el-menu>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import ErrorLog from '@/components/ErrorLog'
import Screenfull from '@/components/Screenfull'
import LangSelect from '@/components/LangSelect'
import ThemePicker from '@/components/ThemePicker'

import request from '@/utils/request'
import { pattern } from '@/utils/validate'

export default {
  components: {
    Breadcrumb,
    Hamburger,
    ErrorLog,
    Screenfull,
    LangSelect,
    ThemePicker
  },
  data() {
    return {
      // msgCount: 1,
      collections: [],
      userName: {},
      
      editorDialog: false,
      foo: false,
      pwdForm: {
        oldPwd: '',
        newPwd: '',
        comfirmPwd: ''
      },
      psdRule: {
        oldPwd: [
          { required: true, message: '请输入原密码'},
        ],
        newPwd: [
          { required: true, message: '请输入新密码' },
          { max: 12, message: '密码最多12位' },
          { min: 6, message: '密码最少6位' },
          { pattern: pattern.password, message: '密码由小写字母、大写字母、数字、特殊符号的两种及以上组成' }
        ],
        comfirmPwd: [
          { required: true, message: '请确认密码'},
          { validator: (rule, value, cb) => { if (value && value !== this.pwdForm.newPwd) { cb(new Error('两次密码不一致')) } else { cb() } }}
        ]
      }
      
    }
  },
  props: {
    // msgCount: {
    //   type: Number,
    //   default: 1
    // }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'collectChanged',
      'userInfo',
      'messageCount'
    ])
  },
  watch: {
    collectChanged: function(newValue, old) {
      // 目前没有用到
    }
  },
  created(){
    this.getUser()
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('toggleSideBar')
    },
    logout() {
      console.log('退出登录')
      this.$store.dispatch('LogOut').then(() => {
        // location.reload()  // In order to re-instantiate the vue-router object to avoid bugs
      })
    },
    getUser() {
      this.userName = JSON.parse(window.sessionStorage.getItem('userInfo'))
    },
    // 修改密码
    modifyPwd(form){
      let params = {
        password : form.newPwd,
        oldPassword : form.oldPwd
      }
      console.log('padForm1', params)
      this.$refs.psdForm.validate(valid => {
        if(valid){
          request({
            url: '/api/auth/user/edit_pwd',
            method: 'post',
            data: params
          }).then(({data}) => {
            if(data.code == 200){
              console.log(data)
            }else if(data.code === 4010){
              this.$store.dispatch('LogOut').then(() => {
                location.reload()
              })
            }else{
              this.$message.error(data.message)
            }
          })
        }
      })
      
    },
    resetForm(){
      this.pwdForm = {
        oldPwd: '',
        comfirmPwd: '',
        newPwd: '',
      }
    }
  },
  mounted() {
    const self = this
    request({
      url: '/api/platform/message/query_warn',
      method: 'get'
    }).then(res => {
      // console.log(res.data.data)
      self.$store.commit('SET_MESSAGECOUNT',res.data.data)
    })
    setInterval(function(){
			
			// this.msgCount++;
      request({
        url: '/api/platform/message/query_warn',
        method: 'get'
      }).then(res => {
				// console.log(res.data.data)
				self.$store.commit('SET_MESSAGECOUNT',res.data.data)
      })
      
    },30000)
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  line-height: 50px;
  border-radius: 0px !important;
  .hamburger-container {
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 25px;
    display: flex;
    flex-direction: column;
    justify-content: center;
  }
  .breadcrumb-container{
    float: left;
  }
  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }
  .right-menu {
    float: right;
    height: 100%;
    margin-right: 15px;
    &:focus{
     outline: none;
    }
    .right-menu-item {
      display: inline-block;
      margin: 0 8px;
      
    }
    .screenfull {
      height: 20px;
    }
   .international{
      vertical-align: top;
      svg-icon.international-icon {
        font-size: 30px;
      }
    }
    .theme-switch {
      vertical-align: 15px;
    }
    .avatar-container {
      height: 50px;
      margin-right: 10px;
      .avatar-wrapper {
        cursor: pointer;
        position: relative;
        .avatar-header{
          display: flex;
          width: 30px;
          height: 50px;
          float: left;
          margin-right: 10px;
          flex-direction: column;
          justify-content: center;
          i{
            display: inline-block;
            width: 28px;
            height: 28px;
            border-radius: 50%;
            background: #999;
            text-align: center;
            img{
              position: relative;
              top: -7px;
            }
          }
        }
        .el-icon-caret-bottom {
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
.collect-wrap {
	max-width: 500px;
}
.collect-item {
	margin: 20px;
	float: left;
	width: 60px;
	height: 50px;
	font-size: 14px;
	color: #97a8be;
	text-align: center;
}
.collect-item-svg {
	width: 24px !important;
	height: 24px !important;
	// margin-bottom: 5px;
	cursor: pointer;
}
.my-delete-icon {
  position: absolute;
}
.triangle {
  display: inline-block;
  width: 0; 
  height: 0; 
  border-left: 6px solid transparent; 
  border-right: 6px solid transparent;
  border-top: 6px solid #999; 
  margin: -2px 10px 2px 8px;
}
</style>
