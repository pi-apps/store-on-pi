<template>
	<view class="payDones">
		<!-- 自定义导航 -->
		<uniNav title="核销成功" />
		<view class=" f f_column f_align_center">
			<u-icon class="mt32" size="100" color="#fa4935" name="checkmark-circle-fill" />
			<view class="amout bold mt20"><text class="fz12">π</text>{{amount || 0}}</view>
			<view class="fz14 bold mt20">{{scanCode}}</view>
			<view class="fz12 bold mt20">核销成功</view>
			<u-button @click="payDone" class="f_c_c w100 realy_buy bold">完成</u-button>
		</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				amount: '',
				scanCode: ''
			};
		},

		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {
			if (options && options.amount) {
				this.amount = options.amount;
				this.scanCode = options.scanCode
			}
		},
		onShow() {

		},
		methods: {
			// 
			payDone() {
				this.$navBack();
			},

		}
	}
</script>

<style lang="scss" scoped>
	.payDones {
		width: 100%;

		.realy_buy {
			background-image: linear-gradient(to bottom, #fa4935, #fb8363);
			border-radius: 18rpx;
			color: #FFF;
			font-size: 32rpx;
			width: 700rpx;
			margin-top: 400rpx;
			height: 90rpx;
		}

		.amout {
			font-size: 60rpx;
			color: #fa4935;
		}
	}
</style>
