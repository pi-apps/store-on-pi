<template>
	<view class="template">
		<!-- 自定义导航 -->
		<uniNav title="积分订单详情" />

		<view class="-warp30 -bgf">
			<view class="f_c_a address" v-if="orderInfo.addrId  && orderInfo.addrId != ''">
				<u--image src="/static/packageB/image/icon_daishouhuo @2x.png" width="30" height="30" />
				<view class="f_c_a w100 ml12">
					<view class="f f_column">
						<view class="f f_align_center">
							<text>{{orderInfo.deliveryUserName}}</text>
							<text class="ml8">{{orderInfo.deliveryMobile}}</text>
						</view>
						<view class="overfont-2 mt12">{{orderInfo.deliveryAddress}}</view>
					</view>
				</view>
			</view>
			<view class="f f_align mt32">
				<u--image :src="orderInfo.goodsInfo.goodsVideoPic? orderInfo.goodsInfo.goodsVideoPic : ''" radius="8"
					width="80" height="80" />
				<view class="f f_column f_align w100 ml12">
					<view class="f f_align_start f_align">
						<view class="overfont-2">{{orderInfo.goodsInfo.goodsName}}</view>
						<view class="f f_column f_align_end ml12">
							<text class="tipColor">x{{orderInfo.goodsNum}}</text>
						</view>
					</view>
					<view class="mt12 sku f_baseline">
						规格：{{(orderInfo.attributeName && orderInfo.attributeName.length) ? orderInfo.attributeName.join('/') : '无'}}
					</view>
				</view>
			</view>
		</view>
		<u-cell-group :border="false" :customStyle="{backgroundColor:'#FFF'}">
			<u-cell title="总金额"
				:value="(orderInfo.amountPoints && orderInfo.amountPoints > 0 ? orderInfo.amountPoints+'PI积分' : '')+orderInfo.integralAmount && orderInfo.integralAmount > 0 ? orderInfo.integralAmount+'签到积分' : ''"
				:border="false" />
			<u-cell title="配送费用" value="商家免运费" :border="false" />
		</u-cell-group>
		<view class="-warp30 f f_c_e -bgf">
			<view>实付
				<text class="price ml8 fz16">
					<text
						v-if="orderInfo.amountPoints && orderInfo.amountPoints > 0">{{orderInfo.amountPoints}}PI积分</text>
					<text
						v-if="orderInfo.integralAmount && orderInfo.integralAmount > 0">{{orderInfo.integralAmount}}签到积分</text>
				</text>
			</view>
		</view>
		<u-cell-group title="订单信息" :border="false" :customStyle="{backgroundColor:'#FFF'}">
			<u-cell title="订单编号" :border="false">
				<view slot="right-icon" class="f f_align_center">
					<view class="-copy mr4 f_c_c" @click="$copy(orderId)">复制</view>
					<view style="width: 400rpx;" class="overfont bold tipColor">{{orderId}}</view>
				</view>
			</u-cell>
			<u-cell title="订单状态" :border="false">
				<view slot="value" class="f_c_e">
					{{orderInfo.goodsOrderStatus == '10012001' ? '待发货' : '已发货'}}
					<u-tag v-if="orderInfo.goodsOrderStatus == '10012002'" @click="watchExprion" class="ml12"
						text="物流详情" plain size="mini" type="warning"></u-tag>
				</view>
			</u-cell>
			<u-cell title="下单时间" :value="orderInfo.createTime" :border="false" />
		</u-cell-group>

		<!--  -->
		<actionExprion :show.sync="isShowExpriton" :mobile="orderInfo.deliveryMobile" :expressNo="orderInfo.expressNo">
		</actionExprion>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		_integralOrderList
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				orderId: '',
				isShowExpriton: false,
				orderInfo: {
					goodsOrderStatus: '',
					remainingTime: 0,
					goodsInfo: {},
					deliveryMobile: '',
					expressNo: ''
				}
			};
		},
		components: {
			actionExprion: () => import('@/components/actionExprion/actionExprion.vue')
		},
		mounted() {
			const _this = this;
			// h5原生方法调用回调
			window['appbackJScopyToPasteboard'] = (code) => {
				if (code == 200) {
					_this.$showToast('复制成功！');
				}
			}
		},
		onLoad(options) {
			if (options.orderId) {
				this.orderId = options.orderId
			}
		},
		onShow() {
			this.getOrderIdInfo();
		},
		methods: {
			// 查询订单详情信息
			getOrderIdInfo() {
				_integralOrderList({
					orderId: this.orderId
				}).then(res => {
					if (res && res.code == 0) {
						this.orderInfo = res.data.list[0]
					}
				})
			},
			copy() {
				this.$copy(this.orderId);
			},
			// 查询物流
			watchExprion() {
				this.isShowExpriton = true
			},
		},
		onPullDownRefresh() {
			this.getOrderIdInfo();
		},
	}
</script>

<style lang="scss" scoped>
	.orderDetail {
		padding-bottom: 180rpx;
		min-height: 100vh;
		box-sizing: border-box;

		.address {
			background-color: $bg-color-fa;
			padding: 10rpx 30rpx;
			height: 140rpx;
			border-radius: 20rpx;

			.place {
				font-size: 32rpx;
				color: $font-color2;
			}
		}

		.price {
			color: $font-money-color2;
		}

		.tipColor {
			color: #939393;
		}

		.sku {
			background-color: $bg-color-fa;
			color: $font-color2;
			padding: 4rpx 18rpx 4rpx 0;
		}


		.fixedBtn {
			width: 100%;
			box-sizing: border-box;
			position: fixed;
			bottom: 0;
			left: 0;
			right: 0;
			padding: 20rpx 30rpx 80rpx;

			.payContniue {
				width: 220rpx;
				background-image: linear-gradient(to bottom, #fa4935, #fb8363);
				border-radius: 10rpx;
				color: #FFF;
				font-size: 32rpx;
			}
		}
	}
</style>
