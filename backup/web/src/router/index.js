import Vue from 'vue'
import Router from 'vue-router'
const _import = require('./_import_' + process.env.NODE_ENV)
Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'
export const constantRouterMap = [
  { path: '/login', component: _import('login/index'), hidden: true },
  // { path: '/msssage', component: _import('message/index'), hidden: true },
  { path: '/authredirect', component: _import('login/authredirect'), hidden: true },
  { path: '/404', component: _import('errorPage/404'), hidden: true },
  { path: '/401', component: _import('errorPage/401'), hidden: true }
]

export const asyncRouterMap = [
  {
    path: '',
    component: Layout,
    redirect: 'sourcedetails',
    // name: 'servicemanage',
    meta: {
      title: 'dataServerMana'
    },
    children: [
      {
        path: 'sourcedetails/:id',
        props: true,
        // component: _import('dataServiceManage/details'),
        name: 'details',
        meta: { title: 'details' }
      }
    ],
    hidden: true
  },
  {
    path: '/sourcemanage',
    component: Layout, // 为什么是layout
    redirect: 'noredirect', // 没有默认指向
    meta: {
      title: 'sourcemanage',
      icon: 'sourcemanage'
    },
    children: [{ path: 'datasource', component: _import('dataSouceManage/sourceMane'), name: 'datasource', meta: { title: 'datasource' }}]
  },
  {
    path: '/assetmanage',
    component: Layout,
    redirect: 'noredirect',
    meta: {
      title: 'assetmanage',
      icon: ''
    },
    children: [
      { path: 'assetclassifi', component: _import('dataAssetManage/assetclassifi'), name: 'assetclassifi', meta: { title: 'assetclassifi' }},
      { path: 'assetheme', component: _import('dataAssetManage/assetheme'), name: 'assetheme', meta: { title: 'assetheme' }},
      { path: 'modelmana', component: _import('dataAssetManage/modelmana'), name: 'modelmana', meta: { title: 'modelmana' }}
    ]
  },
  {
    path: '',
    redirect: 'icon',
    component: Layout,
    children: [{
      path: 'icon',
      component: _import('svg-icons/index'),
      name: 'icons',
      meta: { title: 'icons', icon: 'icon', noCache: true }
    }]
  },
  { path: '*', redirect: '/404', hidden: true }
]

export default new Router({
  // mode: 'history', // 去掉#  增强搜索引擎 require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})
