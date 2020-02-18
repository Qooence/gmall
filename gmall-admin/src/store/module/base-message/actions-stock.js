import { getSkuList, saveSkuInfo, skuDetail, deleteSkuInfos, productImageList } from '@/api/base-message/api-stock'

export default {
    actions: {
      getSkuList({ state, commit },searchParam) {
          return new Promise((resolve, reject) => {
              try {
                getSkuList(searchParam).then(res => {
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
      saveSkuInfo({ state, commit },data) {
          return new Promise((resolve, reject) => {
              try {
                saveSkuInfo(data).then(res => {
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
      skuDetail({ state, commit },id) {
          return new Promise((resolve, reject) => {
              try {
                skuDetail(id).then(res => {
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
      deleteSkuInfos({ state, commit },ids) {
        return new Promise((resolve, reject) => {
            try {
              deleteSkuInfos(ids).then(res => {
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
      productImageList({ state, commit },productId) {
        return new Promise((resolve, reject) => {
            try {
              productImageList(productId).then(res => {
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
