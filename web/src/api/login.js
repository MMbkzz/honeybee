import request from '@/utils/request'

export function loginByUsername(username, password) {
  const data = {
    login_name: username,
    password: password
  }
  // return request({
  //  url: '/login/login',
  //  method: 'post',
  //  data
  // })
  return request({
    url: 'api/auth/login',
    method: 'post',
    params: data,
    data: data,
    headers: { 'Authorization': 'Basic ' + Base64.encode(username + ':' + password) }
  })
}

export function logout(token) {
  return request({
    url: '/api/auth/logout',
    method: 'post',
    data: token
  })
}

export function getUserInfo(token) {
  return request({
    url: '/user/info',
    method: 'get',
    params: { token }
  })
}

