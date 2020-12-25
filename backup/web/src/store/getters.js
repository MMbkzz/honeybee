const getters = {
  sidebar: state => state.app.sidebar,
  language: state => state.app.language,

  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  collectChanged: state => state.tagsView.collectChanged,

  avatar: state => state.user.avatar,
  name: state => state.user.name,
  introduction: state => state.user.introduction,
  status: state => state.user.status,
  roles: state => state.user.roles,
  setting: state => state.user.setting,
  permission_routers: state => state.permission.routers,
  addRouters: state => state.permission.addRouters,
  errorLogs: state => state.errorLog.logs,

  token: state => state.user.token,
  userInfo: state => state.user.userInfo,
  messageCount: state => state.user.messageCount,
  ldapUrl: state => state.user.ldapUrl
}
export default getters
