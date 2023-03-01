import Vue from 'vue';
import store from '@/store/index.js'
import {
	_userInfo,
	userToken,
	payIncomplete
} from '../http/apiReq.js'

import {
	wxVersion
} from '@/utils/index.js'

//路由跳转
Vue.prototype.$tabTo = (url) => {
	uni.switchTab({
		url: url
	})
}
//路由跳转
Vue.prototype.$navTo = (url) => {
	uni.navigateTo({
		url: url
	})
}
//路由跳转 
Vue.prototype.$redTo = (url) => {
	uni.redirectTo({
		url: url
	})
}
//路由跳转 
Vue.prototype.$relTo = (url) => {
	uni.reLaunch({
		url: url
	})
}
//路由跳转 
Vue.prototype.$navBack = () => {
	uni.navigateBack({})
}

//路由跳转 
Vue.prototype.$navBackRefresh = () => {
	uni.setStorageSync('isRefresh', true);
	uni.navigateBack({})
}

//提示信息
Vue.prototype.$showToast = (msg) => {
	uni.showToast({
		title: msg,
		duration: 2000,
		icon: 'none'
	})
}
//系统信息
Vue.prototype.$sys = () => {
	return uni.getSystemInfoSync()
}

//系统信息
Vue.prototype.$clearStorageSync = () => {
	uni.removeStorageSync('Token');
	uni.removeStorageSync('UserInfo');
}

// 手机号脱敏
Vue.prototype.$phoneFormat = (phone) => {
	var str = phone.substring(0, 3) + "****" + phone.substring(7);
	return str;
}


//复制内容到剪切板
/*
 * @params data 复制copy内容
 */
Vue.prototype.$copy = (str) => {
	uni.setClipboardData({
		data: data,
		success: function(res) {
			uni.showToast({
				title: '复制成功',
				icon: 'none',
				duration: 2000
			})
		}
	})
}
//拨打电话
/*
 * @params phoneNum 电话号码
 */
Vue.prototype.$callPhone = (phoneNum) => {
	if (!phoneNum || phoneNum == '' || phoneNum == null) {
		return uni.showToast({
			title: '该商家还没有联系方式',
			duration: 2000,
			icon: 'none'
		});
	};
	uni.makePhoneCall({
		phoneNumber: phoneNum,
		fail: (e) => {
			uni.showToast({
				title: '拨打失败，请允许app才能使用',
				duration: 2000,
				icon: 'none'
			})
		}
	})
}


// 图片预览
/*
 *	urls 图片数据 Array<String>
 *	current 默认展示第几个
 */
Vue.prototype.$previewImage = (urls, current = 0) => {
	uni.previewImage({
		urls: urls,
		current: current || 0,
		indicator: 'number'
	})
}


// 定位-原生
Vue.prototype.$getLocation = () => {
	console.log('定位获取开始:')
	return new Promise((resolve, reject) => {
		uni.getLocation({
			type: 'wgs84',
			success: function(res) {
				resolve(res)
			},
			fail: function(error) {
				reject(error)
			}
		})
	})

}

// 打开webview

Vue.prototype.$openNewPage = (url, bol) => {
	console.log('==原生方法调用：webview==', url);
	if (bol == undefined) {
		bol = true;
	}
	if (bol && url.indexOf('wx_referer_auth') != -1) {
		url = decodeURIComponent(url);
	}
	if (uni.getSystemInfoSync().platform == 'ios') {
		if (window.webkit) {
			window.webkit.messageHandlers.openNewPage.postMessage(url);
		}
	} else {
		if (window.taoyouAppNative) {
			window.taoyouAppNative.openNewPage(url);
		}
	};
}

// 打开webview
Vue.prototype.$openNewPageLink = (url) => {
	console.log('==内部：webview==', url);
	//银行卡
	let link = encodeURIComponent(JSON.stringify({
		url: url
	}));
	uni.navigateTo({
		url: '/pages/packageA/webview/index?link=' + link
	})
}

// 打开webview 环境变量跳转
Vue.prototype.$openNewPageEnv = (url) => {
	if (process.env.NODE_ENV === 'development') {
		//银行卡
		console.log('==内部本地：webview==', url);
		let link = encodeURIComponent(JSON.stringify({
			url: url
		}));
		uni.navigateTo({
			url: '/pages/packageA/webview/index?link=' + link
		})
	} else {
		console.log('==原生方法调用：webview==', url);
		if (url.indexOf('wx_referer_auth') != -1) {
			url = decodeURIComponent(url);
		}
		if (uni.getSystemInfoSync().platform == 'ios') {
			if (window.webkit) {
				window.webkit.messageHandlers.openNewPage.postMessage(url);
			}
		} else {
			if (window.taoyouAppNative) {
				window.taoyouAppNative.openNewPage(url);
			}
		};
	}
}


/**
 * 
 *
 *
 **/

//需要登录
Vue.prototype.$loginModel = (backUrl, callback) => {
	let token = wx.getStorageSync('Token');
	console.log(backUrl,token)
	if (token && token != '') {
		return true;
	} else {
		uni.showModal({
			title: '授权登录',
			content: '您还没有授权，需要授权后才能继续使用！',
			confirmText: '立即授权',
			confirmColor: '#f67112',
			success: async (res) => {
				if (res.confirm) {
					uni.showLoading({
						title: '授权中...'
					})
					const scopes = ['username', 'payments'];
					const authResult = await window.Pi.authenticate(scopes, (res) => {
						console.log(100, res)
						return payIncomplete({
							identifier:res.identifier,
							transaction:{
								_link:res.transaction._link,
								txid:res.transaction.txid
							}
						})
					});
					console.log(111, authResult)
					const {
						accessToken,
						user
					} = authResult;
					// 
					userToken({
						accessToken: accessToken,
						uid: user.uid,
						userName: user.username
					}).then(res => {
						console.log(3333, res)
						if (res && res.code == 0) {
							uni.setStorageSync('Token', accessToken)
							callback()
						} else {
							uni.removeStorageSync('Token')
						}
					}).catch(() => {
						uni.removeStorageSync('Token')
					})
					console.log(2, authResult)
				}
			}
		});

	};
}



//计算亮点之间的距离
Vue.prototype.$getDistance = (lat1, lng1, lat2, lng2) => {
	var radLat1 = lat1 * Math.PI / 180.0;
	var radLat2 = lat2 * Math.PI / 180.0;
	var a = radLat1 - radLat2;
	var b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;
	var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
		Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
	s = s * 6378.137; // EARTH_RADIUS;
	s = Math.round(s * 10000) / 10000;
	return s;
};



// 常用正则验证
Vue.prototype.$ExpReg = {
	phone: /^1[3,4,5,6,7,8,9]\d{9}$/,
	pw: /^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{8,20}$/,
	pw1: /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$/,
	number: /^[0-9]*[1-9][0-9]*$/,
	fixed2: /^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/,
	tell: /((\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)/
};


// 查询用户信息
Vue.prototype.$getUserInfoSync = (isLogin = true, isToast = true) => {
	return new Promise((resolve, reject) => {
		_userInfo(isLogin, isToast).then(res => {
			resolve(res)
			if (res && res.code == 0) {
				uni.setStorageSync('UserInfo', res.data);
			} else if (res.code == 999) {
				if (isLogin) {
					setTimeout(() => {
						uni.switchTab({
							url: '/pages/index/index'
						})
					}, 400);
					uni.showToast({
						title: '授权超时，请重新授权～',
						icon: 'none'
					})
				}
			} else {
				uni.removeStorageSync('UserInfo')
				uni.removeStorageSync('Token')
			}
		}).catch(e => reject(e))
	})
};


//字典表转化
Vue.prototype.$getDict = (list, key, label, value) => {
	label = label || 'label';
	value = value || 'value';
	if (list && list.length > 0) {
		let row = list.find(item => item[value] == key);
		return row ? row[label] : '-'
	} else {
		return '-'
	}

}

// banner跳转
Vue.prototype.$toRuntimeUrlIndex = (item) => {
	
	let tabRoute = ['shop/shop', '/user/user', '/index/index', '/mall/mall'];
	if ((item.linksUrl && item.linksUrl != '') || item.adUrlType == '10017004') {
		let n = item.linksUrl;
		//系统内部跳转哦
		console.log(n)
		if (item.adUrlType == '10017003' || item.adUrlType == '10017005') {
			let bol = false;
			for (let i = 0; i < tabRoute.length; i++) {
				if (n.indexOf(tabRoute[i]) != -1) {
					bol = true;
				}
			}
			if (bol) {
				wx.switchTab({
					url: n
				})
			} else {
				wx.navigateTo({
					url: n
				});
			}
		} else if (item.adUrlType == '10017002') {
			//打开webview 外部地址
			console.log('原生方法调用webview：', n)
			//打开webview 地址
			if (n.indexOf('wx_referer_auth') != -1) {
				n = decodeURIComponent(n);
			}
			//银行卡
			let link = encodeURIComponent(JSON.stringify({
				url: n
			}));
			uni.navigateTo({
				url: '/pages/packageA/webview/index?link=' + link
			})
		}
	};
}

//阻止滚动
Vue.prototype.$noneMove = () => {}

// rgb转换
Vue.prototype.$hexToRgba = (hex) => {
	var red = parseInt("0x" + hex.slice(1, 3));
	var green = parseInt("0x" + hex.slice(3, 5));
	var blue = parseInt("0x" + hex.slice(5, 7));
	return (red + ',' + green + ',' + blue)
}


// rgb转换
Vue.prototype.$padZero = (num) => {
	return parseInt(num) < 10 ? ('0' + num) : parseInt(num);
}

// =========================== =========================== =========================== =========================== ===========================

//过滤器 手机号脱敏
Vue.filter('phone', function(value) {
	let str = '';
	if (value && value != '') {
		let s = value.substring(0, 3);
		let e = value.substring(value.length - 4, value.length);
		str = s + '****' + e
	}
	return str
})
