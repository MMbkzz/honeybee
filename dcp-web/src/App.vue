<template>
	<div id="app">
		<router-view></router-view>
	</div>
</template>

<script>
/* eslint-disable */
/* 安全管理 */
// import permission from'@/views/layout/Layout'
import permission from '@/views/permission/permission'
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
import runlog from '@/views/platform/runLog'
/* 平台管理 */
import cloudserver from '@/views/platform/cloudServer.vue'
import drivemane from '@/views/platform/driveMane'
import clusteropera from '@/views/platform/clusterOpera'
/* 参数配置 */
import applyparam from '@/views/paramConfig/applyParamConfig'
import systemparam from '@/views/paramConfig/systemParamConfig'
/* 主页 */
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
  datasource,  // 数据源管理
  modelma, // 服务模型管理
  '/assetmanage': permission,  // 资产管理
  assetclassifi,
  assetheme,
  modelmana, // 资产管理里面的服务模型管理
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
export default{
  name: 'APP',
  created() {
    const userInfo = JSON.parse(window.sessionStorage.getItem('userInfo'))
    if (userInfo && userInfo.menu) {
      let routes = []
      this.getRoutes(userInfo.menu, routes)
      this.$router.addRoutes(routes)
      this.$store.dispatch('addRoutes', routes)
    }
  },
  methods: {
    // 对请求获取的路由数据进行处理
    getRoutes(data, routes) {
      if (data && data.length) {
        for (let i = 0; i < data.length; i++) {
          routes.push({
            path: data[i].attr1,
            component: component[data[i].attr1],
            name: data[i].name,
            meta: {
              title: data[i].name,
              icon: data[i].attr2
            },
            hidden: data[i].attr4,
            // notShowChildren: data[i].attr5
          })
          // 只有一级菜单 路径、名字属性为空
          if(data[i].attr1 == ''){
            routes[i].redirect = data[i].children[0].attr1
            routes[i].component = component['/permission']
            routes[i].name = null;
          }
          if (data[i].children.length) {
            routes[i].children = []
            this.getRoutes(data[i].children, routes[i].children)
          }
        }
      }
    }
  }
}
</script>
