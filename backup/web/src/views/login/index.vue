<template>
  <div class="login-container">
    <!-- <img class="big-logo" src="../../assets/tdspic/deep5.png" alt=""> -->
    <el-form class="login-form" autoComplete="on" :model="loginForm" :rules="loginRules" ref="loginForm" label-position="left">

      <div class="title-container">
        <!-- <img src="../../assets/tdspic/logo.png"> -->
        <span class="title">登&nbsp;&nbsp;录</span>
      </div>
      <el-form-item prop="username" class="username-container">
        <div class="el-input-con">
          <span class="svg-container">
            <img src="../../assets/tdspic/user.png" alt="">
          </span>
          <el-input name="username" type="text" v-model="loginForm.login_name" autoComplete="off" placeholder="用户名" />
        </div>
      </el-form-item>
      <div style="color: #f56c6c;margin-top: 4px;" v-if="usernameRequired">
        <span>请输入用户名</span>
      </div>
      <el-form-item prop="password" class="psd-container">
        <div class="el-input-con">
          <span class="svg-container">
            <img src="../../assets/tdspic/password.png" alt="">
          </span>
          <el-input name="password" :type="passwordType" @keyup.enter.native="handleLogin" v-model="loginForm.password" autoComplete="on" placeholder="密码" />
        </div>
      </el-form-item>
      <div style="color: #f56c6c;margin-top: 4px;" v-if="pwdRequired">
        <span>请输入密码</span>
      </div>
      <el-button class="login-botton" :loading="loading" @click.native.prevent="handleLogin">{{$t('login.logIn')}}</el-button>
    </el-form>
  </div>
</template>

<script>
/* eslint-disable */
import permission from '@/views/layout/Layout'
import users from '@/views/permission/user'
import roles from '@/views/permission/roles'
import log from '@/views/permission/log'
import resources from '@/views/permission/resource'
import serviceuser from '@/views/permission/serviceUser'
/* 数据源管理 */
import datasource from '@/views/dataSouceManage/sourceMane'
/* 数据资产管理 */
import modelma from '@/views/dataAssetManage/modelmana'

/* 资产管理 */
import assetclassifi from '@/views/dataAssetManage/assetclassifi'
import assetheme from '@/views/dataAssetManage/assetheme'
import modelmana from '@/views/dataAssetManage/modelmana'
import dataassetmap from '@/views/dataAssetMap/assetmap'
/* 资产地图管理 */
import assetmap from '@/views/dataAssetMap/assetmap'
/* 数据服务管理 */
import servicemanage from '@/views/dataServiceManage/serviceManage'
import servicedetails from '@/views/dataAssetManage/details'
import servicevalidation from '@/views/dataAssetManage/validation'
/* 运维监控 */
import accessstatic from '@/views/operationMonitor/accessStatic'
import statemonitor from '@/views/operationMonitor/stateMonitor'
import logshow from '@/views/operationMonitor/logShow'
import clusteraudit from '@/views/operationMonitor/clusterAudit'
import runlog from '@/views/platform/runLog' // 平台操作日志

/* 平台管理 */
import cloudserver from '@/views/platform/cloudServer'
import drivemane from '@/views/platform/driveMane'
import clusteropera from '@/views/platform/clusterOpera'
/* 参数配置 */
import applyparam from '@/views/paramConfig/applyParamConfig'
import systemparam from '@/views/paramConfig/systemParamConfig'
/* 首页 */
import home from '@/views/home/home'
/* 消息 */
import message from '@/views/message/index'


let component = {
  '/permission': permission,  // 安全管理
  users,
  roles,
  resources,
  log,
  serviceuser,
  // 数据源管理
  datasource,  // 数据源管理
  modelma, // 服务模型管理
  '/assetmanage': permission,  // 资产管理
  assetclassifi,
  assetheme,
  modelmana, // 服务模型管理
  dataassetmap,
  'servicedetails/:id': servicedetails,
  'servicevalidation/:id': servicevalidation,
  assetmap: assetmap,  // 资产地图（隐藏）
  '/serviceapi': permission,  // 数据服务管理
  servicemanage,
  '/operationmoni': permission, // 运维监控
  accessstatic,
  statemonitor,
  logshow,
  clusteraudit,
  runlog,
  '/platform': permission, // 平台管理
  cloudserver,
  drivemane,
  clusteropera,
  '/paramConfig': permission, // 参数配置
  applyparam,
  systemparam,
  // 主页
  home,
  // 消息
  message
}

import { isvalidUsername } from '@/utils/validate'
import SocialSign from './socialsignin'
import Cookies from 'js-cookie'
import request from '@/utils/request'
import mapGetters from 'vuex'
const Base64 = require('js-base64').Base64
import { Message } from 'element-ui'

export default {
  components: { SocialSign },
  name: 'login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!isvalidUsername(value)) {
        callback(new Error('Please enter the correct user name'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('The password can not be less than 6 digits'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        login_name: '',
        password: ''
      },
      loginRules: {
        login_name: [{ required: true, validator: validateUsername }],
        password: [{ required: true }]
      },
      usernameRequired: false,
      pwdRequired: false,
      passwordType: 'password',
      loading: false,
      showDialog: false
    }
  },
  computed: {
    // ...mapGetters([
    //   'ldapUrl'
    // ])
  },
  mounted() {
    const strArr = window.location.href.split('?')
    // console.log(strArr[1])
    if(strArr[1]){
      try {
        // console.log(decodeURIComponent(strArr[1].split('=')[1]))
        const obj = JSON.parse(Base64.decode(decodeURIComponent(strArr[1].split('=')[1])))
        console.log(obj)
        if(obj.returnCode === '200'){
          $('#app').css('display', 'none')
          request({
            url: '/api/auth/get_ldap_user?ldapUser='+obj.loginName,
            method: 'get'
          }).then(res => {
            const self = this
            if (res.data.code === 200) {
              // 解析异步挂载路由所需的数据
              // $('#app').css('display', 'none')
              const routes = []
              Cookies.set('useSso', obj.useSso)
              // debugger
              // 获取路由权限
              this.getRoutes(res.data.data.menu, routes)
              // data.data.roles = 'admin'
              // 设置token,设置用户信息
              // console.log(data.data)
              this.$store.dispatch('RegisterUserInfo', res.data.data)
              this.$store.dispatch('addRoutes',routes).then(()=> {
                this.$router.addRoutes(this.$store.getters.addRouters)
                this.$router.push({ path: 'home' })
              })
            } else {
              this.$message.error('登录失败！')
            }
          })
        } else {
          $('#app').css('display', 'none')
          this.$message.error(obj.returnMessage)
        }
      } catch(e) {
        // console.log('格式错误')
      }
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
    },
    // 对请求获取的路由数据进行处理
    getRoutes(data, routes) {
      // debugger
      if (data&&data.length) {
        for (let i = 0; i < data.length; i++) {
          routes.push({
            path: data[i].attr1,
            component:component[data[i].attr1],
            name: data[i].name,
            meta: {
              title: data[i].name,
              icon: data[i].attr2
            },
            hidden: data[i].attr4, // 该状态为有路由但没有菜单
            menuId: data[i].id
          })
          // 只有一级菜单 路径、名字属性为空
          if(data[i].attr1 == ''){
            routes[i].redirect = data[i].children[0].attr1
            routes[i].component = component['/permission']
            routes[i].name = null;
          }
          // 递归计算出mune菜单
          if (data[i].children.length) {
            routes[i].children = []
            this.getRoutes(data[i].children, routes[i].children)
          }
        }
      }
    },
    // 登录函数
    handleLogin() {
      // console.log('loginForm',this.loginForm.password)

      this.usernameRequired = this.loginForm.login_name =='' ? true : false
      this.pwdRequired = this.loginForm.password =='' ? true : false
      if(this.loginForm.login_name != '' && this.loginForm.password != ''){
        this.$refs.loginForm.validate(valid => {
          if (valid) {
            this.loading = true
            request({
              url: 'api/auth/login',
              method: 'post',
              // params: this.loginForm,
              // data: this.loginForm,
              headers: { 'Authorization': 'Basic ' + Base64.encode(this.loginForm.login_name + ':' + this.loginForm.password) }
            }).then(({ data }) => {
              // debugger
              this.loading = false
              if (data.code === 200) {
                // 解析异步挂载路由所需的数据
                const routes = []
                // debugger
                // 获取路由权限
                this.getRoutes(data.data.menu, routes)
                // data.data.roles = 'admin'
                // 设置token,设置用户信息
                console.log(data.data)
                this.$store.dispatch('RegisterUserInfo', data.data)
                this.$store.dispatch('addRoutes',routes).then(()=> {
                  this.$router.addRoutes(this.$store.getters.addRouters)
                  this.$router.push({ path: 'home' })
                })
              } else {
                Message({
                  message: data.message,
                  type: 'error',
                  duration: 5 * 1000
                })
              }
            }).catch(() => {
              this.loading = false
            })
          }
        })
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
$bg:#2d3a4b;
$light_gray:#eee;

.login-container {
  .el-input-con{
    width: 320px;
  }
  .el-input {
    display: inline-block;
    height: 47px;
    width: 288px;
    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      color: $light_gray;
      height: 47px;
      &:-webkit-autofill {
        -webkit-box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: #fff !important;
      }
    }
  }
  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
    overflow: hidden;
  }
}
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  background: url('../../assets/tdspic/bg.png') no-repeat;
  background-size: cover;
  position: absolute;
  height: 100%;
  width: 100%;
  .big-logo{
    position: absolute;
    top: 50%;
    left: 50%;
    margin-top: -53px;
    margin-left: -450px;
  }
  .login-form {
    position: absolute;
    left: 50%;
    top: 50%;
    width: 380px;
    height: 380px;
    background: rgba(0,0,0,.6);
    padding: 45px;
    border-radius: 5px;
    //margin-left: 250px;
    transform: translate(-50%, -50%)
  }
  .svg-container {
    display: inline-block;
    color: white;
    width: 32px;
    height: 47px;
    line-height: 52px;
    text-align: center;
    float: left;
    margin-left: -32px;
    transition: all 0.2s;
    background: rgba(255, 255, 255, 0.1);
    &_login {
      font-size: 20px;
    }
  }
  .title-container {
    text-align: left;
    margin-bottom: 45px;
    img {
      vertical-align:middle;
    }
    .title {
      font-size: 26px;
      font-weight: 400;
      color: $light_gray;
      font-family: 'SourceHanSansCN-Bold';
    }
  }
  .username-container {
    overflow: hidden;
    background-color: rgba(255,255,255,0.2)!important;
    // margin-bottom: 22px;
    &:focus-within{
      border-color: #4562fc;
      .svg-container{
        margin-left: 0;
        background: #4562fc;
      }
    }
  }
  .psd-container {
    background-color: rgba(255,255,255,0.2)!important;
    margin-top: 22px;
    &:focus-within{
      border-color: #4562fc;
      .svg-container{
        margin-left: 0;
        background: #4562fc;
      }
    }
  }
  .login-botton {
    margin-top: 22px;
    background: #4562fc;
    transition: all 0.5s;
    width: 100%;
    height: 45px;
    border-radius: 5px;
    border: none;
    color: white;
    font-size: 15px;
    &:hover{
      box-shadow: 0 0 10px 5px inset rgba(0, 0, 0, 0.3);
    }
  }
}
</style>
