import App from './App'
import Vue from 'vue'

import VueI18n from 'vue-i18n'
import i18nConfig from './lang/index.js'

import store from './store'
import uView from "uview-ui"

import http from './http/api'
import './utils/common.js'


import uniNav from "./components/uni-nav/uni-nav.vue"
import Alert from "./components/toast/toast"
Vue.component('Alert', Alert)
Vue.component('uniNav', uniNav)


Vue.config.productionTip = false

// 引入配置Api请求
Vue.prototype.$http = http
Vue.use(uView);

// 引入多语言
Vue.use(VueI18n);
const i18n = new VueI18n(i18nConfig);



App.mpType = 'app'
const app = new Vue({
	i18n,
	store,
    ...App
})
app.$mount()