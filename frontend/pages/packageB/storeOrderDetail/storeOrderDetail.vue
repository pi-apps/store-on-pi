<template>
	<view class="storeOrder">
		<!-- 自定义导航 -->
		<uniNav title="门店订单详情" />

		<view class="top_status f f_align_center fz18"
			v-if="orderInfo.isUse == '1' && orderInfo.payStatusType == '10007003'">待核销</view>
		<view v-else class="top_status f f_align_center fz18">{{$getDict(pay_status_type,orderInfo.payStatusType)}}
		</view>
		<!--  -->
		<view v-if="orderInfo.isUse == '1' &&  orderInfo.payStatusType == '10007003'"
			class="-warp30 mt12 -bgf f_column f_c_c order_now">
			<text class="fz18 bold">待使用</text>
			<view class="er_code_warp">
				<tki-qrcode v-if="qrCodeUrl != ''" cid="qrcode1" ref="qrcode" :val="qrCodeUrl" :size="360" unit="upx"
					background="#fff" foreground="#000" pdground="#000" :onval="false" :loadMake="true"
					:usingComponents="true" :showLoading="false" />
			</view>
			<text class="fz14">请到店出示二维码进行消费</text>
			<view class="hr" style="margin-top:40rpx"></view>
			<view class="b_text w100 bold fz14">
				<view><text>有效期至：</text>{{orderInfo.effTime}}</view>
				<view class="mt16"><text>券码：</text>{{orderInfo.qrNo}}</view>
			</view>
		</view>
		<!--  -->
		<view class="-warp30 -bgf f f_align_center mt12">
			<u--image radius="4" class="f_c_c mr12" :src="orderInfo.buyStore ? orderInfo.buyStore.doorPhotoUrl : ''" width="90"
				height="90" />
			<view class="f f_column w100 overfont">
				<text class="overfont bold fz14">{{orderInfo.buyStore ? orderInfo.buyStore.storeName : ''}}</text>
				<text class="shop_midle mt12">销量：{{orderInfo.buyStore ? orderInfo.buyStore.totalNum : ''}}</text>
				<text class="overfont fz12 address mt4">{{orderInfo.buyStore ? orderInfo.buyStore.address : ''}}</text>
			</view>
		</view>
		<view class="-warp30 -bgf f_c_a mt12">
			<text>合计：</text>
			<text class="fz12 price">π<text class="fz20 bold">{{(orderInfo.userFinalPrice || 0).toFixed(8)/1}}</text></text>
		</view>

		<view class="mt12"></view>
		<u-cell-group title="订单信息">
			<u-cell title="订单号">
				<view slot="right-icon" class="f f_align_center">
					<view style="width: 400rpx;" class="overfont bold tipColor">{{orderId}}</view>
				</view>
			</u-cell>
			<u-cell title="下单时间" :value="orderInfo.createTime" />
			<u-cell v-if="orderInfo.useTime" title="核销时间" :value="orderInfo.useTime" />
			<u-cell title="下单备注" :value="orderInfo.orderRemark" :border=" false " />
		</u-cell-group>
		<!-- 立即购买 -->
		<view class="buynow -bgf w100 f_c_a">
			<view class="f f_column">
				<text class="fz22 mb4 -price">
					<text class="fz12">π</text>{{(orderInfo.userFinalPrice || 0).toFixed(8)/1}}
				</text>
			</view>
			<view class="buyNowBtn" v-if="orderInfo.isUse == '1' && orderInfo.payStatusType == '10007003'" @click="jumpCallStore">退款</view>
		</view>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		_getOrderDetails,orderRefund
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				orderId: '',
				orderInfo: {
					buyStore: {}
				},
				qrCodeUrl: ''
			};
		},
		components: {
			tkiQrcode: () => import('@/components/tki-qrcode/tki-qrcode.vue')
		},
		computed: {
			...mapState({
				pay_status_type: state => state.dictArrs.pay_status_type
			})
		},
		onLoad(options) {
			if (options.orderId) {
				this.orderId = options.orderId
			}
			this.$store.dispatch('getDictlists', 'pay_status_type')
		},
		onShow() {
			this.getOrderIdInfo();
		},
		methods: {
			// 查询订单详情信息
			getOrderIdInfo() {
				_getOrderDetails(this.orderId).then(res => {
					if (res && res.code == 0) {
						this.orderInfo = res.data
						if (res.data.isUse == '1' && res.data.payStatusType == '10007003') {
							this.qrCodeUrl = res.data.qrNo
						}
					}
				})
			},
			copy() {
				this.$copy(this.orderId);
			},
			jumpCallStore() {
				orderRefund(this.orderId).then((res=>{
					if(res && res.code == 0){
						this.$showToast('退款成功～')
						this.getOrderIdInfo();
					}
				}))
			},
		}
	}
</script>

<style lang="scss" scoped>
	.storeOrder {
		min-height: 100vh;
		padding-bottom: 160rpx;
		box-sizing: border-box;

		.price {
			color: $font-money-color2;
		}

		.order_now {
			color: #333;

			.b_text {
				padding-top: 40rpx;
			}
		}


		.top_status {
			height: 140rpx;
			padding: 30rpx;
			box-sizing: border-box;
			color: #FFF;
			background-image: linear-gradient(to bottom, #fa4935, #fb8363);
		}

		.tipColor {
			color: #939393;
		}

		.shop_midle {
			font-size: 24rpx;

			.middle {
				color: $font-color2;
				margin: 0 12rpx;
			}
		}

		.address {
			color: $font-color2;
		}

		.buynow {
			position: fixed;
			box-sizing: border-box;
			bottom: 0;
			padding: 10rpx 30rpx 60rpx;

			.buyNowBtn {
				background-image: linear-gradient(to bottom, #fa4935, #fb8363);
				padding: 15rpx 40rpx;
				color: #FFF;
				border-radius: 8rpx;
			}

			.price {
				color: $font-money-color2;
			}
		}
	}
</style>
