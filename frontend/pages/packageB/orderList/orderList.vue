<template>
	<view class="">
		<!-- 自定义导航 -->
		<uniNav title="我的订单" />
		<view class="orders">

			<view class="order-item -bgf f_c_a" 
				v-for="(item,index) in orderItem" :key="index"
				v-if="!item.isStore || (item.isStore=='0' && isStore=='0')"
				@click="jumpOrders(item)">
				<view class="f f_align_center">
					<u--image class="f_c_c" :src="item.icon" width="63" height="62" />
					<view>{{item.title}}</view>
				</view>
				<u-icon name="arrow-right" size="15" color="#C4C4C4" />
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				isStore: '1',
				orderItem: [{
						title: '商城订单',
						icon: '/static/packageB/image/shangchengdingdan@2x.png',
						url: '/pages/packageB/shopOrder/shopOrder'
					},
					{
						title: '门店订单',
						icon: '/static/packageB/image/mendiandingdan@2x.png',
						url: '/pages/packageB/storeOrder/storeOrder'
					},
					{
						title: '特权订单',
						icon: '/static/packageB/image/tequandingdan@2x.png',
						url: '/pages/packageB/vipOrder/vipOrder'
					},
					{
						title: '课程订单',
						icon: '/static/packageB/image/krcheng@2x.png',
						url: '/pages/packageB/tiksOrder/tiksOrder'
					},
					{
						title: '签到商城订单',
						icon: '/static/packageB/image/sign@2x.png',
						url: '/pages/packageB/intergerOrder/intergerOrder'
					},
					{
						title: '加油记录',
						icon: '/static/packageB/image/jiayoudingdan@2x.png',
						url: '/pages/packageB/oilOrder/oilOrder'
					},
					{
						title: '车享宝订单',
						icon: '/static/packageB/image/chexiangbaodingdan@2x.png',
						url: '/pages/packageB/unCarOrder/unCarOrder'
					},
					{
						title: '代驾订单',
						icon: '/static/packageB/image/daijiadingdan@2x.png',
						url: '/pages/packageB/ECarOrder/ECarOrder'
					},
					{
						title: '话费订单',
						icon: '/static/packageB/image/chongzhidingdan@2x.png',
						url: '/pages/packageB/hfOrder/hfOrder'
					},
					{
						title: '折扣充值订单',
						icon: '/static/packageB/image/rechange@2x.png',
						url: '/pages/packageB/qyOrder/qyOrder'
					},
					{
						title: '软包定制订单',
						icon: '/static/packageB/image/ruanbaodingzhi@2x.png',
						url: '/pages/packageB/rbOrder/rbOrder',
						isStore:'0' 
					},
				],
			};
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {

		},
		onShow() {
			this.getUserInfo();
		},
		methods: {
			jumpOrders(item) {
				if (this.$loginModel('/pages/packageB/orderList/orderList')) {
					this.$navTo(item.url)
				}
			},
			// 查询用户信息
			getUserInfo() {
				this.$getUserInfoSync(false, false).then(res => {
					if (res && res.code == '0') {
						this.isStore = res.data.isStore
					}
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	.orders {
		min-height: 100vh;
		background-color: #F5F5F5;
		padding: 20rpx 24rpx;
		box-sizing: border-box;

		.order-item {
			padding: 32rpx 44rpx 16rpx 16rpx;
			border-radius: 12rpx;
			margin-bottom: 20rpx;
			font-size: 32rpx;
			color: #292929;
		}
	}
</style>
