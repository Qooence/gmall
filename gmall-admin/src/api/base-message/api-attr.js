import axios from '@/libs/api.request'

export const getCatalog1 = () => {
    return axios.request({
        url: 'getCatalog1',
        method: 'get'
    })
}

export const getCatalog2 = (catalog1Id) => {
    return axios.request({
        url: 'getCatalog2',
        params: {catalog1Id},
        method: 'get'
    })
}

export const getCatalog3 = (catalog2Id) => {
    return axios.request({
        url: 'getCatalog3',
        params: {catalog2Id},
        method: 'get'
    })
}

export const getAttrInfoList = (searchParam) => {
    return axios.request({
        url: 'attrInfoList',
        params: searchParam,
        method: 'get'
    })
}
