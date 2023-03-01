<template>
	<view class="orderDetail">
		<!-- 自定义导航 -->
		<uniNav title="订单详情" />

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
			<!--  -->
			<view class="f f_align_center overfont mt32">
				<!-- <i class="iconfont icon-close"></i> -->
				<u--image class="f_c_c" src="/static/image/mall/store-icon.png" width="24" height="24" />
				<text class="overfont fz16 ml8">PI商店自营店</text>
			</view>
			<view class="f f_align mt12">
				<u--image :src="orderInfo.goodsInfo.goodsImgList[0] ? orderInfo.goodsInfo.goodsImgList[0] : ''"
					radius="8" width="80" height="80" />
				<view class="f f_column f_align w100 ml12">
					<view class="f f_align_start f_align">
						<view class="overfont-2">{{orderInfo.goodsInfo.goodsName}}</view>
						<view class="f f_column f_align_end ml12">
							<text class="price">π{{(orderInfo.unitPrice).toFixed(2)}}</text>
							<text class="tsize">x{{orderInfo.goodsNum}}</text>
						</view>
					</view>
					<view class="mt12 sku f_baseline">
						规格：{{(orderInfo.attributeName && orderInfo.attributeName.length) ? orderInfo.attributeName.join('/') : '无'}}
					</view>
				</view>
			</view>
		</view>
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

		<!--  -->
		<view class="f_c_e fixedBtn -bgf">
			<u-button v-if="orderInfo.goodsOrderStatus == '10007001' && orderInfo.remainingTime && orderInfo.remainingTime > 0" @click="payContinue" class="payContniue f_c_c">继续支付</u-button>
			<u-button v-if="orderInfo.goodsOrderStatus == '10007003' && orderInfo.goodsExpressStatus=='10012002'" @click="confirmGoods" class="payContniue f_c_c">确认收货</u-button>
		</view>
		
		<!--  -->
		<actionExprion :show.sync="isShowExpriton" :mobile="orderInfo.deliveryMobile" :expressNo="orderInfo.expressNo"></actionExprion>
		
		<!-- 确认收货 -->
		<u-action-sheet 
			description="请确定货物是否已经收到～" round="12"
			:actions="list" title="确认收货" cancelText="取消" :show="show"
			@select="confirmGoodsAxios" @close="show = false"
		
		></u-action-sheet>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		_orderList,_confirmGoods
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				isShowExpriton:false,
				show:false,
				orderId:'',
				list: [{name:'确认收货'}],
				orderInfo: {
					goodsOrderStatus: '',
					remainingTime: 0,
					expressNo:''
				}
			};
		},
		computed: {
			...mapState({
				express_status: state => state.dictArrs.express_status,
				pay_status_type: state => state.dictArrs.pay_status_type
				
			})
		},
		components:{
			actionExprion:()=>import('@/components/actionExprion/actionExprion.vue')
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
			if (options && options.orderId) {
				this.orderId = options.orderId
			}
			this.$store.dispatch('getDictlists', 'express_status,pay_status_type')
		},
		onShow() {
			this.getOrderIdInfo();
		},
		methods: {
			// 查询订单详情信息
			getOrderIdInfo() {
				_orderList({
					orderId: this.orderId
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
					isSeckill,
					orderPayPrice
				} = this.orderInfo;
				if (payChannel) {
					// 继续支付处理
					this.$wxstrPay(payChannel,payText,orderId,orderPayPrice);
				} else {
					this.$navTo(
						`/pages/packageA/pay/pay?orderId=${orderId}&amount=${orderPayPrice}&goodsType=${isSeckill == '0' ? '2' : '1'}`
					)
				}
			},
			// 查询物流
			watchExprion(){
				this.isShowExpriton = true
			},
			// 确认收货
			confirmGoods(){
				this.show = true;
			},
			confirmGoodsAxios(){
				_confirmGoods(this.orderId).then(res=>{
					this.show = false;
					if(res && res.code == 0){
						this.$showToast('确认收货成功～');
						this.getOrderIdInfo();
					}
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.orderDetail {
		padding-bottom: 180rpx;
		min-height: 100vh;
		box-sizing: border-box;
		
		/deep/ .styleClass{
			.u-action-sheet{
				text-align: left;
				line-height: 30rpx;
				.u-text__value{
					margin-bottom: 12rpx;
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
