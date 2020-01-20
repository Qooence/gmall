import Vue from 'vue'
import Vuex from 'vuex'

import user from './module/user'
import app from './module/app'
import actionsAttr from './module/base-message/actions-attr'
import actionsProduct from './module/base-message/actions-product'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    //
  },
  mutations: {
    //
  },
  actions: {
    //
  },
  modules: {
    user,
    app,
    actionsAttr,
    actionsProduct
  }
})
