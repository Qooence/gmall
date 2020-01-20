import axios from '@/libs/api.request'

export const getProductInfoList = (searchParam) => {
    return axios.request({
        url: 'productInfoList',
        params: searchParam,
        method: 'get'
    })
}

export const saveAttrInfo = (data) => {
    return axios.request({
        url: 'saveAttrInfo',
        data: data,
        method: 'post'
    })
}

export const attrDetail = (id) => {
    return axios.request({
        url: `detail/${id}`,
        method: 'get'
    })
}
