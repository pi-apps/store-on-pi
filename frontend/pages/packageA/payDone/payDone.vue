<template>
	<view>
		<!-- 自定义导航 -->
		<uniNav title="支付成功" />
		<view class="payDones f f_column f_align_center">
			<u--image src="/static/packageA/image/chongzhichenggong@2x.png" width="155" height="155" />

			<view class="fz20 bold mt20">付款成功</view>
			<view class="amout bold mt20"><text class="fz12">π</text>{{amount}}</view>

			<view style="margin-top: 160rpx;" class="f_c_a w100" wx:else>
				<u-button @click="payDone" class="f_c_c w100 realy_buy_now gost bold">返回首页</u-button>
				<u-button @click="payDoneOrder" class="f_c_c w100 realy_buy_now bold">查看订单</u-button>
			</view>


		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				amount: '',
			};
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {
			if (options && (options.amount)) {
				this.amount = options.amount;
			}
		},
		onShow() {

		},
		methods: {
			// 
			payDone() {
				this.$tabTo('/pages/index/index')
			},
			payDoneOrder() {
				// 门店
				this.$redTo('/pages/packageB/storeOrder/storeOrder')
			},
		}
	}
</script>

<style lang="scss" scoped>
	.payDones {
		width: 100%;
		padding: 82rpx;
		box-sizing: border-box;

		.tipist {
			font-size: 24rpx;
			color: rgb(236, 100, 26);
			position: fixed;
			bottom: 200rpx;
		}

		.realy_buy_now {
			width: 256rpx;
			height: 76rpx;
			background-color: #F7694A;
			font-size: 28rpx;
			color: #FFF;
			border-radius: 12rpx;
			border: none;

			&.gost {
				border: solid 1px #F7694A;
				color: #F7694A;
				background-color: #FFF;
			}
		}

		.amout {
			font-size: 60rpx;
			color: #fa4935;
		}
	}
</style>
