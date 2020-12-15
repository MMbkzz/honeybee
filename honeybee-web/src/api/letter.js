import request from '@/utils/request'

// 收件箱站内信详情
export function getReceiveDetail(transferId) {
  const params = { transferId }
  return request({
    url: '/api/sitemsg/get_inbox_msg_detail',
    method: 'get',
    params
  })
}

// 已发送站内信详情
export function getSendDetail(transferId) {
  const params = { transferId }
  return request({
    url: '/api/sitemsg/get_sent_msg_detail',
    method: 'get',
    params
  })
}

// 删除收件箱站内信
export function deleteMsg(msgIds) {
  const params = {
    msgIds
  }
  return request({
    url: '/api/sitemsg/del_receive_msg',
    method: 'get',
    params
  })
}
