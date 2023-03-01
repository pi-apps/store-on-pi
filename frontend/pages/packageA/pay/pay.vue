<template>
	<view class="pay">
		<!-- 自定义导航 -->
		<uniNav title="支付订单" />

		<view class="payMoney -bgf f_column f f_c_a">
			<text class="fz12">π<text class="fz24 bold">{{amount}}</text></text>
			<view class="fz12 mt12 order">订单编号：{{orderId}}</view>
		</view>
		<view class="fz12 str mt32 mb8">选择支付方式</view>
		<view class="paywarp -bgf fz16">
			<view class="f_c_a payRow" @click="changepay('1')">
				<view class="f f_align_center">
					<u--image class="f_c_c" src="/static/packageA/image/pay/balance.png" width="20" height="20" />
					<text class="ml8">余额支付</text>
					<view class="ml20 fz12">（余额:π<text class="bold">{{userInfo.balance}}</text>）</view>
				</view>
				<image class="payico" v-if="payChannelType == '1'"
					src="/static/packageA/image/pay/icon_xuanzhong2@2x.png" />
				<view v-else class="radius"></view>
			</view>

			<view class="f_c_a payRow" @click="changepay('0')">
				<view class="f f_align_center">
					<u--image class="f_c_c" src="/static/packageA/image/pay/balance.png" width="20" height="20" />
					<text class="ml8">Pi钱包支付</text>
				</view>
				<image class="payico" v-if="payChannelType == '0'"
					src="/static/packageA/image/pay/icon_xuanzhong2@2x.png" />
				<view v-else class="radius"></view>
			</view>
		</view>

		<view class="btnwarp">
			<u-button :loading="loading" :disabled="loading" loading-text="支付中..." @click="payOrderJumpMeth"
				class="f_c_c w100 realy_buy bold">确认支付</u-button>
		</view>
	</view>
</template>

<script>
	import {
		payAuth,payDone,payCancel,payIncomplete
	} from '@/http/apiReq'
	import {
		mapState
	} from 'vuex';
	export default {
		data() {
			return {
				userInfo: {
					balance: 0
				},
				isHide: false,
				loading: false,
				amount: '',
				orderId: '',
				payChannelType: '0',

				pay_channel_type: [{
						label: '余额支付',
						value: '1',
						status: 0
					},
					{
						label: 'PI支付',
						value: '0',
						status: 0
					},
				]
			};
		},
		async onLoad(options) {
			if (options && options.orderId) {
				this.orderId = options.orderId;
				this.amount = options.amount;
			}
			this.getUserInfos();
			
			uni.showLoading({
				title:"获取授权中..."
			})
			const scopes = ['payments'];
			await window.Pi.authenticate(scopes, (res)=>{
				console.log(100,res)
				return payIncomplete({
					identifier:res.identifier,
					transaction:{
						_link:res.transaction._link,
						txid:res.transaction.txid
					}
				})
			});
			uni.hideLoading();
		},
		methods: {
			// 
			getUserInfos() {
				this.$getUserInfoSync(false, false).then(res => {
					if (res && res.code == 0) {
						this.userInfo = res.data;
					}
				})
			},
			// 
			changepay(item) {
				this.payChannelType = item
			},

			// 下单支付开始
			payOrderJumpMeth() {
				if(this.payChannelType == '0'){
					this.payOrder();
				}else{
					payDone({orderId:this.orderId,payType:'1'}).then(res=>{
						if(res && res.code == 0){
							this.payDoneJump()
						}
					})
				}
			},
			// 下单支付开始
			payOrder() {
				this.loading = true;
				const orderPayment = async (memo, amount, paymentMetadata) => {
					const paymentData = { amount, memo, metadata: paymentMetadata };
					const callbacks = {
					  onReadyForServerApproval,
					  onReadyForServerCompletion,
					  onCancel,
					  onError
					};
					
					const payment = await window.Pi.createPayment(paymentData, callbacks);
					console.log(101,payment);
				}
				// 授权
				const onReadyForServerApproval = (paymentId) => {
					console.log("onReadyForServerApproval", paymentId);
					payAuth({paymentId:paymentId,orderId:this.orderId})
				}
				// 支付成功
				const onReadyForServerCompletion = (paymentId,txid) => {
					console.log("onReadyForServerCompletion", paymentId);
					payDone({paymentId:paymentId,txId:txid,orderId:this.orderId,payType:'0'}).then(res=>{
						if(res && res.code == 0){
							this.payDoneJump();
						}
					})
					this.loading = false;
				}
				// 支付取消
				const onCancel = (paymentId) => {
					console.log("onCancel", paymentId);
					this.loading = false;
					return payCancel(paymentId)
				}
				// 支付失败
				const onError = (error) => {
					console.log("onError", error);
					this.loading = false;
				} 
				
				
				orderPayment('门店订单支付',this.amount,{productId:this.orderId})




				// _getPay({
				// 	orderId: this.orderId,
				// 	payChannelType: this.payChannelType
				// }).then(res => {
				// 	this.loading = false
				// 	if (res && res.code == 0) {
				// 		this.isHide = true;

				// 	}
				// }).catch(() => {
				// 	this.loading = false
				// })
			},
			// 
			payDoneJump(){
				this.$showToast('付款成功～');
				setTimeout(() => {
					this.$redTo('../payDone/payDone?amount=' + this.amount)
				}, 400)
			}
		}
	}
</script>

<style lang="scss" scoped>
	.pay {
		min-height: 100vh;

		.payMoney {
			padding: 100rpx 30rpx 60rpx;

			.order {
				color: $font-color;
			}
		}

		.str {
			color: $font-color;
			padding-left: 30rpx;
		}

		.paywarp {
			padding: 0 30rpx;

			.payRow {
				padding: 40rpx 0;
			}

			.radius {
				width: 30rpx;
				height: 30rpx;
				border: solid 4rpx $font-color2;
				border-radius: 50%;
			}
		}

		.btnwarp {
			padding: 200rpx 30rpx 0;

			.realy_buy {
				background-image: linear-gradient(to bottom, #fa4935, #fb8363);
				border-radius: 18rpx;
				color: #FFF;
				font-size: 32rpx;
			}
		}
	}
</style>
