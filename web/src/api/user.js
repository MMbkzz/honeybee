import request from '@/utils/request'

export function changePass(id, password, oldPassword) {
  const data = {
    id,
    password,
    oldPassword
  }
  return request({
    url: '/api/auth/user/edit_pwd',
    method: 'post',
    data
  })
}

export function deleteUser(id) {
  const data = { id }
  return request({
    url: '/api/auth/user/del_user',
    method: 'post',
    data
  })
}
