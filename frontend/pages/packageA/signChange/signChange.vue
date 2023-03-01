<template>
	<view class="jfExchange">
		<!-- 自定义导航 -->
		<uniNav title="积分转换" />

		<view class="relative bgwarp">
			<view class="f f_column row">
				<text>签到积分</text>
				<text class="fz24 bold mt8 intergel">{{userInfo.accountEntity.amountSigninPoints}}</text>
			</view>
		</view>

		<view class="mt20 exchange f f_column">
			<text>转为PI积分</text>
			<input v-model="Points" type="number" class="input_style" placeholder-class="placeStyle"
				placeholder="请输入需要转换的签到积分" />
			<text class="tips fz12">签到积分和PI积分的比例是{{1/convertRate}}:1；有效天数：{{convertExpireDay}}天</text>
		</view>

		<u-button :loading="isLoad" loading-text="兑换中..." @click="exchangNow" :disabled="(Points != '' ? false : true) || isLoad" class="f_c_c btn_change">确认转换</u-button>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import { _convertConfig, _convert } from '@/http/apiReq'
	export default {
		data() {
			return {
				isLoad: false,

				Points: '',
				convertExpireDay: 0,
				convertRate: 0.01,

				userInfo: {
					accountEntity: {
						amountSigninPoints: 0
					}
				}
			};
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue')
		},
		onLoad(options) {

		},
		onShow() {
			this.getUserInfoSync();
			this.getExchangConfig();
		},
		methods: {
			//查询配置
			getExchangConfig() {
				_convertConfig().then(res => {
					console.log(res)
					if (res && res.code == 0) {
						this.convertExpireDay = res.data.convertExpireDay;
						this.convertRate = res.data.convertRate
					}
				})
			},

			// 查询用户基本信息
			getUserInfoSync() {
				let userInfo = uni.getStorageSync('UserInfo');
				this.userInfo = userInfo
			},
			// 立即兑换
			exchangNow() {
				if (this.isLoad) {
					return this.$showToast('兑换中，请稍等...');
				}
				if (this.Points == '') {
					return this.$showToast('请填写要转换的签到积分');
				}
				let s = 1 / this.convertRate;
				if (this.userInfo.accountEntity.amountSigninPoints < s) {
					return this.$showToast(`签到积分少于${s}，无法进行兑换～`);
				}
				if (this.Points % s != 0) {
					return this.$showToast(`输入的签到积分只能兑换${s}的整数倍`);
				}
				if (this.Points > this.userInfo.accountEntity.amountSigninPoints) {
					return this.$showToast('签到积分不足，请继续签到');
				}
				this.isLoad = true
				_convert(this.Points).then(res => {
					this.isLoad = false
					if (res && res.code == 0) {
						this.$showToast(res.msg || '兑换PI积分成功～');
						this.getUserInfoSync();
						this.Points = ''
						setTimeout(() => {
							this.$navBack();
						}, 1000)
					}
				}).catch(() => this.isLoad = false)
			},
		}
	}
</script>

<style lang="scss" scoped>
	.jfExchange {
		min-height: 100vh;
		padding: 30rpx;
		background-color: $bg-color-fa;

		.bgwarp {
			height: 200rpx;
			background: url('@/static/packageA/image/signDetail.png') no-repeat center;
			background-size: 100% 100%;
		}

		.row {
			position: absolute;
			top: 30rpx;
			left: 30rpx;

			.intergel {
				color: $color-tt;
			}
		}

		.exchange {
			width: 100%;
			background-color: #fff;
			border-radius: 10rpx;
			padding: 30rpx;
			box-sizing: border-box;

			.input_style {
				height: 100rpx;
				border-bottom: solid 1rpx $font-color2;
				margin: 30rpx 0;
			}

			.tips {
				color: $color-tt;
			}
		}


		.btn_change {
			width: 100%;
			height: 100rpx;
			font-size: 32rpx;
			color: #FFF;
			border-radius: 10rpx;
			background-color: $color-tt;
			margin-top: 100rpx;
		}
	}
</style>
