<template>
	<view class="orderDetail">
		<!-- 自定义导航 -->
		<uniNav title="特权订单详情" />
		<view class="radiuwarps">
			<view class="-warp30 -bgf mb8 radius">
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
				<!--  -->
				<view v-if="orderInfo.goodsChannelType != '1'" class="f f_align_center overfont mt32">
					<!-- <i class="iconfont icon-close"></i> -->
					<u--image class="f_c_c" src="/static/image/mall/store-icon.png" width="24" height="24" />
					<text class="overfont fz16 ml8">PI商店自营店</text>
				</view>
				<view v-else class="overfont fz16 mb12">服务项目</view>
				<view class="f f_align mt12">
					<u--image :src="orderInfo.goodsInfo.goodsImgList[0] ? orderInfo.goodsInfo.goodsImgList[0] : ''"
						radius="8" width="80" height="80" />
					<view class="f f_column f_align w100 ml12">
						<view class="f f_align_start f_align">
							<view class="overfont-2">{{orderInfo.goodsInfo.goodsName}}</view>
							<view class="f f_column f_align_end ml12">
								<text class="price">π{{(orderInfo.payPrice).toFixed(2)}}</text>
								<text class="tsize">x{{orderInfo.goodsNum}}</text>
							</view>
						</view>
						<view class="mt12 sku f_baseline">
							规格：{{(orderInfo.attributeName && orderInfo.attributeName.length) ? orderInfo.attributeName.join('/') : '无'}}
						</view>
					</view>
				</view>
			</view>
			<!-- d带核销 -->
			<view v-if="orderInfo.goodsChannelType == '1' && orderInfo.isUse == '1' &&  orderInfo.goodsOrderStatus == '10007003'"
				class="-warp30 mb8 radius -bgf f_column f_c_c order_now">
				<text class="fz18 bold">待使用</text>
				<view class="er_code_warp">
					<tki-qrcode
						v-if="orderInfo.qrNo != ''"
						cid="qrcode1" ref="qrcode"
						:val="orderInfo.qrNo"
						:size="360" unit="upx" 
						background="#fff" 
						foreground="#000" 
						pdground="#000" 
						:onval="false" 
						:loadMake="true" 
						:usingComponents="true" 
						:showLoading="false"
					/>
				</view>
				<text class="fz14">请到店出示二维码进行消费</text>
				<view class="hr" style="margin-top:40rpx"></view>
				<view class="b_text w100 bold fz14">
					<view><text>有效期至：</text>{{orderInfo.effTime}}</view>
					<view class="mt16"><text>券码：</text>{{orderInfo.qrNo}}</view>
				</view>
			</view>
			<!-- 适用门店 -->
			<view class="-warp -bgf mb8 radius">
				<view class="store_list -warp mb8 -bgf fz12" v-if="orderInfo.storeOrderByList.length > 0">
					<view class="f_c_a">
						<text class="fz16 bold">适用门店</text>
						<view class="f_c_e" @click="doUseStore">
							<text>{{orderInfo.storeOrderByList.length}}家门店适用</text>
							<u-icon class="ml4" name="arrow-right" size="12" color="#C4C4C4" />
						</view>
					</view>
					<view class="f_c_a">
						<view class="f f_align_center mt8">
							<u--image radius="5" width="50" height="50"
								:src="orderInfo.storeOrderByList[0].doorPhotoUrl" />
							<view class="f f_column ml8">
								<text class="overfont fz14 bold">{{orderInfo.storeOrderByList[0].storeName}}</text>
								<view
									:class="'storeStatus f_baseline mt8 '+(orderInfo.storeOrderByList[0].status == '0' ? '' : 'isDown')">
									{{orderInfo.storeOrderByList[0].status == '0' ? '营业中' : '休息中'}}
								</view>
							</view>
						</view>
						<u--image @click="callphone" src="/static/packageA/image/dianhua@2x.png" width="40"
							height="40" />
					</view>
					<view class="f_c_a fz12 mt12 b_b_infos">
						<view class="tip_c2">最近{{(orderInfo.storeOrderByList[0].distance/1000).toFixed(2)}}KM</view>
						<view class="tip_tag"></view>
						<text style="width: 200px;"
							class="overfont tip_c">{{orderInfo.storeOrderByList[0].address}}</text>
						<view class="f_c_e" @click="jumpMapNavgation">导航
							<u-icon class="ml4" name="arrow-right" size="12" color="#C4C4C4" />
						</view>
					</view>
				</view>
			</view>
			<!--  -->
			<u-cell-group :border="false" :customStyle="{backgroundColor:'#FFF'}">
				<u-cell title="总金额" :value="'π'+(orderInfo.payPrice).toFixed(2)" :border="false" />
				<u-cell title="配送费用" :value="orderInfo.expressPrice <= 0 ? '商家免运费' : orderInfo.expressPrice"
					:border="false" />
			</u-cell-group>
			<view class="-warp30 f f_c_e -bgf">
				<view>实付<text class="price ml8 fz16">π{{(orderInfo.orderPayPrice).toFixed(2)}}</text></view>
			</view>
			<u-cell-group title="订单信息" :border="false" :customStyle="{backgroundColor:'#FFF'}">
				<u-cell title="订单编号" :border="false">
					<view slot="right-icon" class="f f_align_center">
						<view class="-copy mr4 f_c_c" @click="$copy(orderId)">复制</view>
						<view style="width: 400rpx;" class="overfont bold tipColor">{{orderId}}</view>
					</view>
				</u-cell>
				<u-cell title="订单状态" :value="$getDict(pay_status_type,orderInfo.goodsOrderStatus)" :border="false" />
				<u-cell title="下单时间" :value="orderInfo.createTime" :border="false" />
				<u-cell v-if="orderInfo.goodsOrderStatus == '10007003'" title="物流状态" :border="false" isLink>
					<view slot="value" class="f_c_e">
						{{$getDict(express_status,orderInfo.goodsExpressStatus)}}
						<u-tag @click="watchExprion" class="ml12" text="查询详情" plain size="mini" type="warning"></u-tag>
					</view>
				</u-cell>
			</u-cell-group>
		</view>

		<!--  -->
		<view class="f_c_e fixedBtn -bgf">
			<u-button
				v-if="orderInfo.goodsOrderStatus == '10007001' && orderInfo.remainingTime && orderInfo.remainingTime > 0"
				@click="payContinue" class="payContniue f_c_c">继续支付</u-button>
			<u-button v-if="orderInfo.goodsOrderStatus == '10007003' && orderInfo.goodsExpressStatus=='10012002'"
				@click="confirmGoods" class="payContniue f_c_c">确认收货</u-button>
		</view>

		<!--  -->
		<actionExprion :show.sync="isShowExpriton" :mobile="orderInfo.deliveryMobile" :expressNo="orderInfo.expressNo">
		</actionExprion>
		<!-- 确认收货 -->
		<u-action-sheet description="请确定货物是否已经收到～" round="12" :actions="list" title="确认收货" cancelText="取消" :show="show"
			@select="confirmGoodsAxios" @close="show = false"></u-action-sheet>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		_vipOrderList,
		_confirmGoodsVip
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				isShowExpriton: false,
				show: false,
				orderId: '',
				list: [{
					name: '确认收货'
				}],
				orderInfo: {
					addrId: "",
					goodsOrderStatus: '',
					remainingTime: 0,
					expressNo: '',
					orderPayPrice:0,
					payPrice:0,
					goodsInfo: {
						goodsImgList:[]
					},
					storeOrderByList: []
				}
			};
		},
		computed: {
			...mapState({
				express_status: state => state.dictArrs.express_status,
				pay_status_type: state => state.dictArrs.pay_status_type
			})
		},
		components: {
			actionExprion: () => import('@/components/actionExprion/actionExprion.vue'),
			tkiQrcode:()=>import('@/components/tki-qrcode/tki-qrcode.vue')
		},
		mounted() {
			// h5原生方法调用回调
			const _this = this;
			window['appbackJScopyToPasteboard'] = (code) => {
				if (code == 200) {
					_this.$showToast('复制成功！');
				}
			}
			// //监听获取定位是否成功
			window['appbackJSgetLocation'] = (code) => {
				let res = JSON.parse(code);
				_this.latitude = res.latitude;
				_this.longitude = res.longitude;

				_this.getOrderIdInfo()
			}
		},
		onLoad(options) {
			if (options && options.orderId) {
				this.orderId = options.orderId
			}
			this.$store.dispatch('getDictlists', 'express_status,pay_status_type');
			this.init();
		},
		onShow() {

		},
		methods: {
			init() {
				// 调试
				//this.getOrderIdInfo();
				//h5获取定位
				this.$getLocation();
			},
			// 查询订单详情信息
			getOrderIdInfo() {
				_vipOrderList({
					orderId: this.orderId,
					latitude: this.latitude,
					longitude: this.longitude,
					// 联调测试
					// latitude: '30.68144',
					// longitude: '103.8559'
				}).then(res => {
					console.log(res);
					if (res && res.code == 0) {
						this.orderInfo = res.data.list[0]
					}
				})
			},
			copy() {
				this.$copy(this.orderId);
			},
			// 继续支付
			payContinue() {
				const {
					payChannel,
					payText,
					orderId,
					orderPayPrice,
					goodsChannelType
				} = this.orderInfo;
				if (payChannel) {
					// 继续支付处理
					this.$wxstrPay(payChannel,payText,orderId,orderPayPrice);
				} else {
					this.$navTo(
						`/pages/packageA/pay/pay?orderId=${orderId}&amount=${orderPayPrice}&goodsType=${goodsChannelType == '0' ? '3' : '10002010'}`
					)
				}
			},
			// 查询物流
			watchExprion() {
				this.isShowExpriton = true
			},
			// 确认收货
			confirmGoods() {
				this.show = true;
			},
			confirmGoodsAxios() {
				_confirmGoodsVip(this.orderId).then(res => {
					this.show = false;
					if (res && res.code == 0) {
						this.$showToast('确认收货成功～');
						this.getOrderIdInfo();
					}
				})
			},
			//
			callphone() {
				const [{
					mobile
				}] = this.orderInfo.storeOrderByList;
				if (mobile && mobile != '') {
					this.$callPhone(mobile);
				} else {
					this.$showTaost('该门店暂无电话～')
				}
			},
			// 
			jumpMapNavgation() {
				const [{
					longitude,
					latitude,
					storeName,
					address
				}] = this.orderInfo.storeOrderByList;
				this.$refs.navgationRefs.updataData({
					latitude: latitude,
					longitude: longitude,
					name: storeName,
					address: address
				});
			},
			doUseStore() {
				this.$navTo(
					`/pages/packageA/vipStoreList/vipStoreList?goodsId=${this.orderInfo.goodsId}&latitude=${this.latitude}&longitude=${this.longitude}`
				)
			}
		}
	}
</script>

<style lang="scss" scoped>
	.orderDetail {
		padding-bottom: 180rpx;
		min-height: 100vh;
		box-sizing: border-box;

		/deep/ .styleClass {
			.u-action-sheet {
				text-align: left;
				line-height: 30rpx;

				.u-text__value {
					margin-bottom: 12rpx;
				}
			}
		}

		.radiuwarps {
			padding: 22rpx;
		}

		.radius {
			border-radius: 12rpx;
		}

		.order_now {
			color: #333;

			.b_text {
				padding-top: 40rpx;
			}
		}

		// 

		.store_list {
			color: #292929;

			.tip_c {
				color: #9F9F9F;
			}

			.tip_c2 {
				color: #F7694A;
			}

			.b_b_infos {
				padding-top: 16rpx;
				border-top: solid 1px #F7F7F7;

				.tip_tag {
					height: 20rpx;
					width: 2rpx;
					border-radius: 1rpx;
					background-color: #F0F0F0;
				}
			}

			.storeStatus {
				background-color: #FFF0ED;
				border-radius: 4rpx;
				padding: 2rpx 8rpx;
				color: #F7694A;
				font-size: 22rpx;

				&.isDown {
					background-color: #EAEAEA;
					color: #292929;
				}
			}
		}


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
				margin: 0;
			}
		}
	}
</style>
