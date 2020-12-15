import request from '@/utils/request'

// 获取资源树
export function getResourceTree() {
  return request({
    url: '/api/auth/resource/get',
    method: 'get',
    params: {}
  })
}
// 删除资源
export function deleteResource(id) {
  const data = { id }
  return request({
    url: '/api/auth/resource/delete',
    method: 'post',
    data
  })
}
// 新增资源
export function addResource({ name, descr, code, parent_id, category_id, attr1, attr2, attr3, attr4, attr5 }) {
  const data = {
    name,
    descr,
    code,
    parent_id,
    category_id,
    attr1,
    attr2,
    attr3,
    attr4,
    attr5
  }
  return request({
    url: '/api/auth/resource/add',
    method: 'post',
    data
  })
}
// 编辑资源
export function editResource({ id, name, descr, code, status, category_id, attr1, attr2, attr3, attr4, attr5 }) {
  const data = {
    id,
    name,
    descr,
    code,
    status,
    category_id,
    attr1,
    attr2,
    attr3,
    attr4,
    attr5
  }
  return request({
    url: '/api/auth/resource/update',
    method: 'post',
    data
  })
}
// 资源详情
export function getResourceDetail(resource_id) {
  const params = { resource_id }
  return request({
    url: '/api/auth/resource/category',
    method: 'get',
    params
  })
}

// 资源禁用
export function stopResource(id) {
  const data = {
    id
  }
  return request({
    url: '/api/auth/resource/disable',
    method: 'post',
    data
  })
}

// 资源启用
export function startResource(id) {
  const data = {
    id
  }
  return request({
    url: '/api/auth/resource/enable',
    method: 'post',
    data
  })
}

// 资源类别
export function getTypes() {
  return request({
    url: '/api/auth/category/get_categorys',
    method: 'get',
    params: {}
  })
}
// 校验资源编码唯一性
export function checkResourceCode(code, id) {
  var params = {
    code
  }
  if (id) {
    params.id = id
  }
  return request({
    url: '/api/auth/resource/codecheck',
    method: 'get',
    params
  })
}
