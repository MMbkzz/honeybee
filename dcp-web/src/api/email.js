import request from '@/utils/request'

export function getEmailDetail(Id) {
  const params = {
    Id
  }
  return request({
    url: '/api/email/send/selectEmail',
    method: 'get',
    params
  })
}
