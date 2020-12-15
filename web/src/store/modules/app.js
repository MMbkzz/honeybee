import Cookies from 'js-cookie'

const app = {
  state: {
    sidebar: {
      opened: !+Cookies.get('sidebarStatus')
    },
    language: Cookies.get('language') || 'zh'
  },
  mutations: {
    // 在store里面存储
    TOGGLE_SIDEBAR: state => {
      if (state.sidebar.opened) {
        Cookies.set('sidebarStatus', 1)
      } else {
        Cookies.set('sidebarStatus', 0)
      }
      // 菜单栏展开折叠逻辑
      state.sidebar.opened = !state.sidebar.opened
    },
    SET_LANGUAGE: (state, language) => {
      state.language = language
      Cookies.set('language', language)
    }
  },
  actions: {
    // toggleSideBar函数
    toggleSideBar({ commit }) {
      commit('TOGGLE_SIDEBAR')
    },
    setLanguage({ commit }, language) {
      commit('SET_LANGUAGE', language)
    }
  }
}

export default app
