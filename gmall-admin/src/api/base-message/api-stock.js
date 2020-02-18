import axios from '@/libs/api.request'

export const getSkuList = (searchParam) => {
    return axios.request({
        url: 'sku/list',
        params: searchParam,
        method: 'get'
    })
}

export const saveSkuInfo = (data) => {
    return axios.request({
        url: 'sku/save',
        data: data,
        method: 'post'
    })
}

export const skuDetail = (id) => {
    return axios.request({
        url: `sku/detail/${id}`,
        method: 'get'
    })
}

export const deleteSkuInfos = (data) => {
    return axios.request({
        url: 'sku/deletes',
        data: data,
        method: 'delete'
    })
}

export const productImageList = (productId) => {
    return axios.request({
        url: 'productImage/list',
        params: {productId: productId},
        method: 'get'
    })
}
