import request from '@/utils/request'



// 查询【请填写功能名称】列表

export function listSubscribe(query) {

  return request({

    url: '/car/carSubscribe/list',

    method: 'get',

    params: query

  })

}



// 查询【请填写功能名称】详细

export function getSubscribe(subscribeId) {

  return request({

    url: '/car/carSubscribe/' + subscribeId,

    method: 'get'

  })

}



// 新增【请填写功能名称】

export function addSubscribe(data) {

  return request({

    url: '/car/carSubscribe',

    method: 'post',

    data: data

  })

}



// 修改【请填写功能名称】

export function updateSubscribe(data) {

  return request({

    url: '/car/carSubscribe',

    method: 'put',

    data: data

  })

}



// 删除【请填写功能名称】

export function delSubscribe(subscribeId) {

  return request({

    url: '/car/carSubscribe/' + subscribeId,

    method: 'delete'

  })

}



// 导出【请填写功能名称】

export function exportSubscribe(query) {

  return request({

    url: '/car/carSubscribe/export',

    method: 'get',

    params: query

  })

}

// 下载用户导入模板
export function importTemplate() {
  return request({
    url: '/car/carSubscribe/importTemplate',
    method: 'get'
  })
}
