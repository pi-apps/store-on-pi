import Vue from 'vue'
import Vuex from 'vuex'
import $http from '../http/api.js'

Vue.use(Vuex)

const store = new Vuex.Store({
	state: {
		statusBarHeight:0,
		socket:null,
		
		isShowIMChat:false,
		isContact:false,
		isNoReadMsg:false,
		
		dictArrs: {}
	},
	mutations: {
		//存字典表数据
		SAVEDICTARRS(state, objs) {
			for (let i in objs) {
				Vue.set(state.dictArrs, i, objs[i]);
			}
		},
		// 下拉
		SAVESELECTLIST: (state, objs) => {
			Vue.set(state.dictArrs,objs.key, objs.val)
		},
		//
		SETSTATUSHEIGHT(state,height){
			state.statusBarHeight = parseInt(height)
		},
		
		//websoct 是否连接
		SET_CHAT_IF_CONTACT: (state, bol) => {
			state.isContact = bol
		},
		//展开在聊天
		SET_CHAT_IF_SHOW: (state, bol) => {
			state.isShowIMChat = bol
		},
		//是否有未读消息
		SET_CHAT_IF_READ: (state, bol) => {
			state.isNoReadMsg = bol
		},
		// socket 保存
		SOCKET_CONTENT_PROTO:( state, websocket)=>{
			state.socket = websocket
		}
	},
	actions: {
		//字典表 多个组合获取
		getDictlists({
			commit
		}, params) {
			return new Promise((resolve) => {
				$http('/cert/dict/queryByType/' + params, 'GET', {}).then(res => {
					if (res && res.code == 0) {
						var lists = res.data;
						commit('SAVEDICTARRS', lists);
						resolve(lists)
					}
				});
			})
		},
		//字典表 多个组合获取
		getSelectLists({
			commit
		}, params) {
			$http('/cert/dict/selectByValue/' + params, 'GET', {}).then(res => {
				if (res && res.code == 0) {
					var list = res.data;
					commit('SAVESELECTLIST', {
						key:params,
						val:list
					})
				}
			})
		}
	}
})

export default store
