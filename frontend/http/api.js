// 常用功能封装
import {
	version,
	baseUrl
} from '@/utils';
// Api request 封装
const API = (url, methods = 'POST', data = {}, config = {
	header: "application/json;charset=utf-8",
	isJump: true,
	showToast: true
}) => {
	const Token = uni.getStorageSync('Token');
	const UserInfo = uni.getStorageSync('UserInfo');
	return new Promise((resolve, reject) => {
		uni.showNavigationBarLoading({});
		let durion = setTimeout(() => {
			uni.showLoading({
				title: '加载中...',
			})
		}, 400);
		uni.request({
			url: baseUrl() + url,
			method: methods,
			header: {
				"token": Token,
				"Content-Type": config.header,
				"user_id": UserInfo && UserInfo != '' ? UserInfo.userId : '',
				'Access-Control-Allow-Origin': '*'
			},
			data: data,
			success(res) {
				uni.hideNavigationBarLoading();
				uni.stopPullDownRefresh();
				uni.hideLoading();
				clearTimeout(durion);
				resolve(res.data);
				if (res && res.data.code == 0) {

				} else if (res.data.code == 401) {
					//清除本地存储数据
					uni.removeStorageSync('UserInfo')
					uni.removeStorageSync('Token')
					if (config.isJump) {
						setTimeout(() => {
							uni.redirectTo({
								url: '/pages/login/login',
							})
						}, 1000);
						uni.showToast({
							title: '登录超时，请重新登录～',
							icon: 'none'
						})
					}
				} else {
					if (config.showToast) {
						uni.showToast({
							title: res.data.msg || '未知问题',
							icon: 'none'
						})
					}
				}
			},
			fail(e) {
				uni.hideNavigationBarLoading({});
				uni.stopPullDownRefresh();
				uni.hideLoading();
				clearTimeout(durion);
				reject(e)
			}
		})
	})
};


export default API;
