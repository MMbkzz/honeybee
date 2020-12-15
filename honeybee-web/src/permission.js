import router from './router'
import store from './store'
// import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css'// progress bar style

NProgress.configure({ showSpinner: false })// NProgress Configuration

import Cookies from 'js-cookie'

// 权限判断函数
function hasPermission(roles, permissionRoles) {
  // console.log('角色', roles)
  // console.log('权限', permissionRoles)
  if (roles === undefined) return false
  if (roles.indexOf('admin') >= 0) return true // admin permission passed directly
  if (!permissionRoles) return true
  return roles.some(role => permissionRoles.indexOf(role) >= 0)
}
// 动态配置路由的完整列表
const routerList = [
  '/permission',
  '/permission/users',
  '/permission/roles',
  '/permission/resources',
  '/permission/log',
  '/permission/serviceuser',
  // 'serversource',
  'datasource',
  'modelma',
  '/assetmanage/assetclassifi',
  '/assetmanage/assetheme',
  '/assetmanage/modelmana',
  '/assetmanage/servicevalidation',
  '/assetmanage/servicedetails',
  '/assetmanage/assetmanage',
  '/assetmanage/dataassetmap',
  '/assetmap', // （隐藏）
  '/servicemanage',
  '/operationmoni/accessstatic',
  '/operationmoni/statemonitor',
  '/operationmoni/logshow',
  '/operationmoni/clusteraudit',
  '/operationmoni/runlog',
  '/platform/cloudserver',
  '/platform/drivemane',
  '/platform/clusteropera',
  '/paramConfig/applyparam',
  '/paramConfig/systemparam',
  '/home',
  '/message'
]

// 获取存储中的路径
function getPath(arr, paths) {
  if (arr && arr.length) {
    for (let i = 0; i < arr.length; i++) {
      const temp = arr[i].attr1.split('/')
      paths.push(temp[temp.length - 1])
      if (arr[i].children.length) {
        getPath(arr[i].children, paths)
      }
    }
  }
}
// 设置导航守卫router.beforeEach()
router.beforeEach((to, from, next) => {
  NProgress.start() // 进度条开始
  // 有两种情况比较特殊：1、用户手动退出，2、刷新，3、新开窗口， 介于以上情况，用户信息存放在sessionStorage
  if (window.sessionStorage.getItem('userInfo')) {
    if (store.getters.userInfo.roles === undefined) {
      store.commit('SET_TOKEN', Cookies.get('token'))
      store.commit('SET_USERINFO', JSON.parse(window.sessionStorage.getItem('userInfo')))
    }
  }
  if (to.path === '/login') { // 登出
    if (store.getters.token) {
      next('/home')
    } else {
      next()
    }
  } else {
    // if (window.sessionStorage.getItem('routes')) {
    //   if (!store.getters.addRouters.length) {
    //     store.dispatch('addRoutes', JSON.parse(window.sessionStorage.getItem('routes'))).then(() => {
    //       router.addRoutes(JSON.parse(window.sessionStorage.getItem('routes')))
    //     })
    //   }
    // }
    // 判断是否具有进入权限，第一个参数为用户角色信息，第二个参数为跳转到的路由
    // 第一个参数 在登录接口设置为admin
    // 第二个参数 为登录后重定向页面路由
    if (hasPermission(store.getters.userInfo.roles, to.meta.roles)) {
      if (to.matched.length === 0) {
        if (routerList.indexOf(to.path) > -1) {
          var menu = JSON.parse(window.sessionStorage.getItem('userInfo')).menu
          var paths = []
          getPath(menu, paths)
          const toPath = to.path.split('/')[to.path.split('/').length - 1]
          if (paths.indexOf(toPath) < 0) {
            next('/401')
          }
        }
      }
      next()
    } else {
      // next({ path: '/401', replace: true, query: { noGoBack: true }})
      next('/login') // 否则全部重定向到登录页
      NProgress.done() // if current page is login will not trigger afterEach hook, so manually handle it
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 进度条结束
})
