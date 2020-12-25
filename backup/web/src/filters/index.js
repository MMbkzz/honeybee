function pluralize(time, label) {
  if (time === 1) {
    return time + label
  }
  return time + label + 's'
}

export function timeAgo(time) {
  const between = Date.now() / 1000 - Number(time)
  if (between < 3600) {
    return pluralize(~~(between / 60), ' minute')
  } else if (between < 86400) {
    return pluralize(~~(between / 3600), ' hour')
  } else {
    return pluralize(~~(between / 86400), ' day')
  }
}

export function parseTime(time, cFormat) {
  if (arguments.length === 0) {
    return null
  }

  if ((time + '').length === 10) {
    time = +time * 1000
  }

  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    date = new Date(parseInt(time))
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key]
    if (key === 'a') return ['一', '二', '三', '四', '五', '六', '日'][value - 1]
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return time_str
}

export function formatTime(time, option) {
  time = +time * 1000
  const d = new Date(time)
  const now = Date.now()

  const diff = (now - d) / 1000

  if (diff < 30) {
    return '刚刚'
  } else if (diff < 3600) { // less 1 hour
    return Math.ceil(diff / 60) + '分钟前'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前'
  } else if (diff < 3600 * 24 * 2) {
    return '1天前'
  }
  if (option) {
    return parseTime(time, option)
  } else {
    return d.getMonth() + 1 + '月' + d.getDate() + '日' + d.getHours() + '时' + d.getMinutes() + '分'
  }
}
// 性别
export function sexFormat(sex) {
  if (sex == '1') {
    return '男'
  } else if (sex == '2') {
    return '女'
  } else {
    return ''
  }
}

// 状态（0：停用，1：启用）
export function statusFormat(status) {
  if (status == '0') {
    return '停用'
  } else if (status == '1') {
    return '启用'
  } else {
    return ''
  }
}

/* 数字 格式化*/
export function nFormatter(num, digits) {
  const si = [
    { value: 1E18, symbol: 'E' },
    { value: 1E15, symbol: 'P' },
    { value: 1E12, symbol: 'T' },
    { value: 1E9, symbol: 'G' },
    { value: 1E6, symbol: 'M' },
    { value: 1E3, symbol: 'k' }
  ]
  for (let i = 0; i < si.length; i++) {
    if (num >= si[i].value) {
      return (num / si[i].value + 0.1).toFixed(digits).replace(/\.0+$|(\.[0-9]*[1-9])0+$/, '$1') + si[i].symbol
    }
  }
  return num.toString()
}

export function html2Text(val) {
  const div = document.createElement('div')
  div.innerHTML = val
  return div.textContent || div.innerText
}

export function toThousandslsFilter(num) {
  return (+num || 0).toString().replace(/^-?\d+/g, m => m.replace(/(?=(?!\b)(\d{3})+$)/g, ','))
}

/* dsp过滤器 */
// 服务模型类型 （data: '资产模型'; message: '消息模型'; api: 'api模型'）
export function modelTypeTransition(value) {
  if (value) {
    if (value === 'asset') {
      return '资产服务'
    } else if (value === 'ability') {
      return '能力服务'
    }
  }
}
// 服务状态 （active: '启用'； inactive: '停用'）
export function serviceStateTranst(value) {
  if (value) {
    if (value === 'active') {
      return '启用'
    } else if (value === 'inactive') {
      return '禁用'
    }
  }
}
// 字数截取
export function textLimit(val, num) {
  if (val) {
    let temp = val
    if (val.length > num) {
      temp = val.substring(0, num) + '...'
    }
    return temp
  }
}
// 服务运维管理界面 状态(unknown 未知 normal 正常 stopping 待停止 stopped 停止)
export function clusterStateTranst(value) {
  if (value) {
    if (value === 'unknown') {
      return '未知'
    } else if (value === 'normal') {
      return '正常'
    } else if (value === 'stopping') {
      return '待停止'
    } else {
      return '停止'
    }
  }
}
// 服务运维管理界面 状态（published 已发布 initialized 待发布 rebalance 重发布）
export function clusterInstanceTranst(value) {
  if (value) {
    if (value === 'published') {
      return '已发布'
    } else if (value === 'initialized') {
      return '待发布'
    } else if (value === 'rebalance') {
      return '重发布'
    }
  }
}
// 服务审计日志操作状态
export function auditStateTranst(value) {
  if (value) {
    if (value === '200') {
      return '正常'
    } else if (value === '500') {
      return '失败'
    }
  }
}
// 服务用户状态 （enabled: '启用'； disabled: '禁用'）
export function serviceUserStateTranst(value) {
  if (value) {
    if (value === 'enabled') {
      return '启用'
    } else if (value === 'disabled') {
      return '禁用'
    }
  }
}
// 数据服务管理 （read: '读'; write: '写'）
export function operationType(val) {
  if (val) {
    if (val === 'read') {
      return '读'
    } else if (val === 'write') {
      return '写'
    }
  }
}
