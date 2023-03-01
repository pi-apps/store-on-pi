<template>
	<view class="">
		<!-- 自定义导航 -->
		<uniNav title="核销" />
		<view class="scan">

			<view class="scanWarp -bgf -warp30">
				<input class="scanInput" maxlength="40" v-model="scanCode" placeholder-class="placeStyle"
					placeholder="请输入核销码" />
				<text>您也可以手动输入核销码,进行核销</text>
			</view>
			<!-- <u-button icon="scan" :plain="true" :disabled="loading" @click="scanCanmer"
				class="f_c_c isNone w100 realy_buy bold mt20">扫码核销</u-button> -->
			<u-button :loading="loading" :disabled="loading" loading-text="查询中..." @click="scanJump"
				class="f_c_c w100 realy_buy bold mt20">确认核销</u-button>
		</view>
	</view>
</template>

<script>
	import {
		_scanInfo
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				loading: false,
				scanCode: ''
			};
		},
		mounted() {
			const _this = this;
			// h5原生方法调用回调
			//扫码核销的回调
			window['appBackJsQrcode'] = (result) => {
				if (result && result != null) {
					_this.scanCode = result
					_this.scanInfo();
				} else {
					_this.$showToast('识别失败');
				}
			}
		},
		onLoad(options) {

		},
		onShow() {
			
		},
		methods: {
			// 扫码核销
			scanCanmer() {
				//this.$scanCode()
			},
			// 输入核销后核销
			scanJump() {
				if (this.scanCode == '') {
					return this.$showToast('请输入核销码');
				}
				this.scanInfo();
			},
			// 查询扫码信息
			scanInfo() {
				this.loading = true
				_scanInfo(this.scanCode).then(res => {
					this.loading = false
					if (res && res.code == 0) {
						this.$navTo(`../scanInfo/scanInfo?scanCode=${this.scanCode}`);
						setTimeout(()=>{
							this.scanCode = ''
						},300)
					}
				}).catch(() => this.loading = false)
			},
		}
	}
</script>

<style lang="scss" scoped>
	.scan {
		min-height: 100vh;
		padding: 30rpx;

		.scanWarp {
			border-radius: 12rpx;
			color: #999;
			font-size: 20rpx;

			.scanInput {
				height: 80rpx;
				padding: 20rpx;
				box-sizing: border-box;
				border-radius: 50rpx;
				border: solid 1rpx #fa4935;
				margin-bottom: 32rpx;
				font-size: 28rpx;
			}
		}

		.realy_buy {
			background-image: linear-gradient(to bottom, #fa4935, #fb8363);
			border-radius: 18rpx;
			color: #FFF;
			font-size: 32rpx;

			&.isNone {
				background-image: none;
				background-color: #fff;
				color: #fa4935;
				margin-top: 400rpx;
			}
		}
	}
</style>
