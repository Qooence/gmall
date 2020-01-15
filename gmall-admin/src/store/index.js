import Vue from 'vue'
import Vuex from 'vuex'

import user from './module/user'
import app from './module/app'
import spu from './module/product/spu'

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
    spu
  }
})
