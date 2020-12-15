import Vue from 'vue'

import 'normalize.css/normalize.css'// A modern alternative to CSS resets

import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './styles/element-variables.scss'
import 'babel-polyfill'

import '@/styles/index.scss' // global css

import App from './App'
import router from './router'
import store from './store'

import i18n from './lang' // Internationalization
import './icons' // icon
import './errorLog'// error log
import './permission' // permission control
import locale from 'element-ui/lib/locale/lang/zh-CN'
import $ from 'jquery'

import * as filters from './filters' // global filters

import lodash from 'lodash'
import VueLodash from 'vue-lodash'
import VueClipboard from 'vue-clipboard2'
import echarts from 'echarts'
import 'echarts-liquidfill'

Vue.use(VueLodash, lodash)
Vue.use(Element, { locale }, $)

Vue.use(Element, {
  size: 'medium', // set element-ui default size
  i18n: (key, value) => i18n.t(key, value)
})

Vue.use(VueClipboard)
// register global utility filters.
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false
Vue.prototype.$echarts = echarts

new Vue({
  el: '#app',
  router,
  store,
  i18n,
  template: '<App/>',
  components: { App }
})
