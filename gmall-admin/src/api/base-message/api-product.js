import axios from '@/libs/api.request'

export const getProductInfoList = (searchParam) => {
    return axios.request({
        url: 'product/productInfoList',
        params: searchParam,
        method: 'get'
    })
}

export const getSaleAttr = () => {
    return axios.request({
        url: 'saleAttr/list',
        method: 'get'
    })
}

export const saveProductInfo = (data) => {
    return axios.request({
        url: 'product/save',
        data: data,
        method: 'post'
    })
}

export const productDetail = (id) => {
    return axios.request({
        url: `product/detail/${id}`,
        method: 'get'
    })
}

export const deleteProductInfos = (data) => {
    return axios.request({
        url: 'product/deletes',
        data: data,
        method: 'delete'
    })
}

export const getPmsProductSaleAttr = (productId) => {
    return axios.request({
        url: 'product/saleAttr',
        params: {productId: productId},
        method: 'get'
    })
}
