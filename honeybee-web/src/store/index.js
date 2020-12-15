import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import errorLog from './modules/errorLog'
import permission from './modules/permission'
import tagsView from './modules/tagsView'
import user from './modules/user'
import getters from './getters'
import cssComputed from './modules/cssComputed'
// import createLogger from 'vuex/dist/logger'
// const debug = process.env.NODE_ENV !== 'production'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    errorLog,
    permission,
    tagsView,
    user,
    cssComputed
  },
  getters,
  // strict: debug,
  // plugins: debug ? [createLogger()] : []
})

export default store
