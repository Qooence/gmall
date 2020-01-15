import { getCatalog1, getCatalog2, getCatalog3, getAttrInfoList } from '@/api/product/spu'

export default {
    actions: {
        getCatalog1() {
            return new Promise((resolve, reject) => {
                try {
                    getCatalog1().then(res => {
                        const data = res.data
                        resolve(data)
                    }).catch(err => {
                        reject(err)
                    })
                } catch (error) {
                    reject(error)
                }
            })
        },
        getCatalog2({ state, commit },catalog1Id) {
            return new Promise((resolve, reject) => {
                try {
                    getCatalog2(catalog1Id).then(res => {
                        const data = res.data
                        resolve(data)
                    }).catch(err => {
                        reject(err)
                    })
                } catch (error) {
                    reject(error)
                }
            })
        },
        getCatalog3({ state, commit },catalog2Id) {
            return new Promise((resolve, reject) => {
                try {
                    getCatalog3(catalog2Id).then(res => {
                        const data = res.data
                        resolve(data)
                    }).catch(err => {
                        reject(err)
                    })
                } catch (error) {
                    reject(error)
                }
            })
        },
        getAttrInfoList({ state, commit },catalog3Id) {
            return new Promise((resolve, reject) => {
                try {
                    getCatalog3(catalog3Id).then(res => {
                        const data = res.data
                        resolve(data)
                    }).catch(err => {
                        reject(err)
                    })
                } catch (error) {
                    reject(error)
                }
            })
        }
    }
}