<template>
	<u-overlay :show="showPops" zIndex="1001">
		<view class="popStyle">
			<u-image class="autoSize" mode="widthFix" @click="jumpUrls(popData)" radius="12" :src="popData.popupBgUrl"
				width="600rpx" height="800rpx" />
			<u-icon v-if="popData.closeType == '0'" @click="closePoups" class="closePops" name="close-circle" size="40"
				bold color="#FFF" />
			<view class="colseCount" v-else-if="popData.closeType == '1'">{{closeTime}}秒后关闭</view>
		</view>
	</u-overlay>
</template>

<script>
	import dayjs from 'dayjs';
	import {
		popTips
	} from '@/http/apiReq';
	export default {
		name: "popups",
		data() {
			return {
				onpenTime: 0,
				showPops: false,
				popData: {
					popupBgUrl: '',
					closeType: '0',
					closeTime: 10,
					openType: '1'
				},
				closeTime: 10

			};
		},
		created() {
			this.updatePops();
		},
		methods: {
			// 
			updatePops() {
				this.onpenTime++;
				this.getPoupsAxios();
			},
			// 接口调用
			getPoupsAxios() {
				popTips().then(res => {
					if (res && res.code == '0' && res.data) {
						this.popData = res.data;
						this.closeTime = res.data.closeTime;
						//
						let poups = wx.getStorageSync('indexPoups');
						if (poups != '') {
							// 存储
							uni.setStorageSync('indexPoups', {
								openType: res.data.openType,
								openTime: res.data.openTime,
								updateTime: res.data.updateTime,

								showTime: poups.showTime
							})
						}
						this.poupsModels(res.data);
					}
				})
			},
			// 弹框
			poupsModels(datas) {
				console.log(datas.openType, datas.openTime, datas.closeType, datas.updateTime);
				let poups = uni.getStorageSync('indexPoups');
				if (poups == '') {
					this.setPoupModel(datas);
				} else if (datas.openType == '0') {
					//app 打开后就不弹出
					if (datas.updateTime != poups.updateTime) {
						this.setPoupModel(datas);
					}
				} else if (datas.openType == '1') {
					console.log('打开次数', this.onpenTime);
					//app 打开 每次打开
					if (this.onpenTime <= 1) {
						this.setPoupModel(datas);
					}
				} else if (datas.openType == '2') {
					// 每格多少分钟打开
					let nTime = new Date();
					let sTime = new Date(poups.showTime)
					console.log((dayjs().unix() - dayjs(poups.showTime).unix()))
					if ((dayjs().unix() - dayjs(poups.showTime).unix()) >= poups.openTime * 60) {
						this.setPoupModel(datas);
					}
				}
			},
			// 打开弹框
			setPoupModel(datas) {
				this.showPops = true;
				uni.hideTabBar({
					animation: true,
				})
				if (datas.closeType == '1') {
					this.closeCoundown();
				};
				// 存储
				uni.setStorageSync('indexPoups', {
					openType: datas.openType,
					openTime: datas.openTime,
					showTime: dayjs().format('YYYY-MM-DD HH:mm:ss'),
					updateTime: datas.updateTime
				})
			},

			// 倒计时
			closeCoundown() {
				if (this.closeTime > 0) {
					setTimeout(() => {
						this.closeTime--;
						this.closeCoundown();
					}, 1000)
				} else {
					this.closePoups();
				}
			},
			jumpUrls(item) {
				if (item.adUrlType != '10017001') {
					if (this.$loginModel('/pages/index/index')) {
						uni.showTabBar({
							animation: true,
							success: () => {
								this.$toRuntimeUrlIndex(item);
							}
						})
					}
				} 
			},
			// 关闭
			closePoups() {
				this.showPops = false;
			}
		},
		watch: {
			showPops(val) {
				if (val) {
					uni.hideTabBar({
						animation: true,
					});
				} else {
					uni.showTabBar({
						animation: true,
					})
				}
			}
		}
	}
</script>

<style lang="scss">
	.popStyle {
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
		width: 100%;
		height: 100%;

		.closePops {
			margin-top: 20rpx;
		}

		.colseCount {
			margin-top: 20rpx;
			color: #FFF;
		}

		/deep/ .autoSize {
			display: flex !important;
			justify-content: center;
			align-items: center;

			.u-image {
				display: flex;
				align-items: center;
				justify-content: center;
			}

			uni-image {
				border-radius: 24rpx;
			}
		}
	}
</style>
