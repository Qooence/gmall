import { 
    getCatalog1, 
    getCatalog2, 
    getCatalog3, 
    getAttrInfoList, 
    saveAttrInfo,
    attrDetail, 
    deleteAttrInfos 
} from '@/api/base-message/api-attr'

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
        getAttrInfoList({ state, commit },searchParam) {
            return new Promise((resolve, reject) => {
                try {
                  getAttrInfoList(searchParam).then(res => {
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
        saveAttrInfo({ state, commit },data) {
            return new Promise((resolve, reject) => {
                try {
                    saveAttrInfo(data).then(res => {
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
        attrDetail({ state, commit },id) {
            return new Promise((resolve, reject) => {
                try {
                    attrDetail(id).then(res => {
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
        deleteAttrInfos({ state, commit },ids) {
            return new Promise((resolve, reject) => {
                try {
                    deleteAttrInfos(ids).then(res => {
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
