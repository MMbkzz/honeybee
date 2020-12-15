import request from '@/utils/request'

export function getLogs(pageNo, pageSize, loginName) {
  const params = {
    pageNo,
    pageSize
  }
  if (loginName) {
    params.loginName = loginName
  }
  return request({
    url: '/api/auth/logs/get_logininfo',
    method: 'get',
    params
  })
}
