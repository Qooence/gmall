import { getProductInfoList, getSaleAttr, saveProductInfo,productDetail, deleteProductInfos, getPmsProductSaleAttr } from '@/api/base-message/api-product'

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
      getSaleAttr({ state, commit }) {
          return new Promise((resolve, reject) => {
              try {
                  getSaleAttr().then(res => {
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
      saveProductInfo({ state, commit },data) {
          return new Promise((resolve, reject) => {
              try {
                  saveProductInfo(data).then(res => {
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
      productDetail({ state, commit },id) {
          return new Promise((resolve, reject) => {
              try {
                  productDetail(id).then(res => {
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
      deleteProductInfos({ state, commit },ids) {
        return new Promise((resolve, reject) => {
            try {
              deleteProductInfos(ids).then(res => {
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
      getPmsProductSaleAttr({ state, commit }, productId) {
        return new Promise((resolve, reject) => {
            try {
              getPmsProductSaleAttr(productId).then(res => {
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
