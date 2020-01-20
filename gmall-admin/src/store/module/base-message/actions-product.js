import { getProductInfoList, saveAttrInfo,attrDetail } from '@/api/base-message/api-product'

export default {
    actions: {
        getProductInfoList({ state, commit },searchParam) {
            return new Promise((resolve, reject) => {
                try {
                    getProductInfoList(searchParam).then(res => {
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
        // saveAttrInfo({ state, commit },data) {
        //     return new Promise((resolve, reject) => {
        //         try {
        //             saveAttrInfo(data).then(res => {
        //             const data = res.data
        //             resolve(data)
        //           }).catch(err => {
        //             reject(err)
        //           })
        //         } catch (error) {
        //             reject(error)
        //         }
        //     })
        // },
        // attrDetail({ state, commit },id) {
        //     return new Promise((resolve, reject) => {
        //         try {
        //             attrDetail(id).then(res => {
        //             const data = res.data
        //             resolve(data)
        //           }).catch(err => {
        //             reject(err)
        //           })
        //         } catch (error) {
        //             reject(error)
        //         }
        //     })
        // }
    }
}
