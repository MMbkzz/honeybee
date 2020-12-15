import request from '@/utils/request'

export function getSmsDetail(Id) {
  const params = {
    Id
  }
  return request({
    url: '/api/sms/collection',
    method: 'get',
    params
  })
}
