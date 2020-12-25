import {
  logout
} from '@/api/login'
import Cookies from 'js-cookie'

const user = {
  state: {
    token: '',
    userInfo: {},
    messageCount: 0,
    ldapUrl: ''
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_USERINFO: (state, info) => {
      state.userInfo = info
    },
    SET_MESSAGECOUNT: (state, messageCount) => {
      state.messageCount = messageCount
    }
  },

  actions: {
    // 登记用户信息：设置token,设置用户信息
    RegisterUserInfo({
      commit
    }, data) {
      commit('SET_USERINFO', data)
      commit('SET_TOKEN', data.token)
      // Cookies.set('userInfo', JSON.stringify(data))
      window.sessionStorage.setItem('userInfo', JSON.stringify(data))
      Cookies.set('token', data.token)
      // 判断单点登录和普通登录渠道
    },

    // 用户名登录
    LoginByUsername({
      commit
    }, userInfo) {
      const username = userInfo.login_name.trim()
      return new Promise((resolve, reject) => {
        loginByUsername(username, userInfo.password).then(response => {
          const data = response.data.data
          // state.token = token
          // commit('SET_TOKEN', data.token)
          commit('SET_TOKEN', data.token)
          // return Cookies.set(TokenKey, token)
          // setToken(response.data.token)
          setToken(data.token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    LogOut({
      commit,
      state
    }) {
      return new Promise((resolve, reject) => {
        logout(state.token).then(() => {
          commit('SET_USERINFO', {})
          commit('SET_TOKEN', '')
          window.sessionStorage.removeItem('routes')
          window.sessionStorage.removeItem('userInfo')
          Cookies.remove('token', {
            path: ''
          })
          if (Cookies.get('useSso')) {
            Cookies.remove('useSso')
            window.location.href = '' // 生产
          } else {
            window.location.reload()
          }
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    }

  }
}

export default user
