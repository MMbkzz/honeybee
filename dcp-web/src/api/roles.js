import request from '@/utils/request'

export function checkCode(code, id) {
  const data = { id, code }
  return request({
    url: '/api/auth/role/verify_role_exists',
    method: 'post',
    data
  })
}

export function checkName(name, id) {
  const data = { id, name }
  return request({
    url: '/api/auth/role/verify_role_exists',
    method: 'post',
    data
  })
}

export function getPermissionTree() {
  return request({
    url: 'api/auth/role/build_resource_operations',
    method: 'get'
  })
}
// 删除角色
export function deleteRole(id) {
  const data = {
    id
  }
  return request({
    url: 'api/auth/role/del_role',
    method: 'post',
    data
  })
}
