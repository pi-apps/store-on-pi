<template>
	<view class="confirm">
		<!-- 自定义导航 -->
		<uniNav title="确认订单" />

		<view class="-warp30 -bgf">
			<view class="f_c_a address" @click="jumpAddress">
				<u--image src="/static/packageA/image/icon_dingwei@2x.png" width="24" height="24" />
				<view class="f_c_a w100 ml12">
					<view v-if="address && address.addrId  && address.addrId != ''" class="f f_column">
						<view class="f f_align_center">
							<text>{{address.userName}}</text>
							<text class="ml8">{{address.userMobile}}</text>
						</view>
						<view class="overfont-2 mt4">
							{{address.provinceName}}{{address.cityName}}{{address.districtName}}{{address.addrDetail}}
						</view>
					</view>
					<text v-else class="place">选择收货地址</text>
					<i class="iconfont icon-xiangyou1"></i>
				</view>
			</view>
		</view>
		<view class="-warp30 -bgf">
			<view class="goodInfo f_c_a">
				<u--image class="mr12 f_shrink0 f_c_c" :src="goodInfo.goodsVideoPicList ? goodInfo.goodsVideoPicList[0] : ''" width="100" height="100" radius="4" />
				<view class="f f_column f_align overfont w100">
					<text class="overfont-2">{{goodInfo.goodsName}}</text>
					<view class="f_c_a">
						<view class="sku mt8 f_baseline overfont">规格：无 </view>
						<view class="sku mt8 f_baseline overfont">数量：1 </view>
					</view>
				</view>
			</view>
			<!-- <van-field model:value="{{ remark }}" label="留言备注" type="textarea" class="remark" placeholder="请输入备注信息"
				autosize maxlength="{{ 30 }}" border="{{ false }}" input-align="right" /> -->
			<view class="f mt20">
				<view>留言备注</view>
				<u--textarea confirmType="next" border="none" v-model="remark" count :maxlength="40" placeholder="请输入备注信息" autoHeight ></u--textarea>
			</view>
		</view>
		<!-- 下单信息 -->
		<view class="-warp20-30 -bgf mt12">
			<view class="ceil f_c_a">
				<view class="ceil-able">商品总价</view>
				<view class="price_two fz16">
					<text v-if="goodInfo.amountPoints> 0">{{goodInfo.amountPoints}}PI积分</text>
					<text v-if="goodInfo.amountPoints>0 && goodInfo.integralAmount>0">+</text>
					<text v-if="goodInfo.integralAmount>0">{{goodInfo.integralAmount}}签到积分</text>
				</view>
			</view>
			<view class="ceil f_c_a">
				<view class="ceil-able">配送费用</view>
				<view>商家免邮</view>
			</view>
			<!-- <view class="ceil f_c_a">
		        <view class="ceil-able">优惠券</view>
		        <view>无可用</view>
		      </view> -->

		</view>
		<view class="f f_column -warp30">
			<view class="bold">注意事项：</view>
			<view class="mt8 describe">积分兑换商品时请您慎重选择收货地址与所需兑换商品，完成兑换后的商品无法更换收货地址与退货。</view>
		</view>
		<!-- 立即购买 -->
		<view class="buynow -bgf w100">
			<view class="i-warps" @click="showActionSheel">
				<view class="interNumber fz16 f f_align_center bold">
					<text v-if="goodInfo.amountPoints && goodInfo.amountPoints > 0">{{goodInfo.amountPoints}}PI积分</text>
					<text v-if="goodInfo.amountPoints > 0 && goodInfo.integralAmount > 0">+</text>
					<text v-if="goodInfo.integralAmount && goodInfo.integralAmount > 0">{{goodInfo.integralAmount}}签到积分</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		_apiIntegralGoodsDetail,
		_apiGetShowAddress,
		_createIntegralOrder
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				goodsId: '',
				loading: false,
				address: {
					addrId: '',
				},
				userInfo: {
					accountEntity: {
						amountPoints: 0
					}
				},
				goodInfo: {
					discountPrice: 0,
					expressPrice: 0,
				},
				remark: '',
				latitude: '',
				longitude: ''
			};
		},
		mounted() {
			// h5原生方法调用回调
			const _this = this;
			//#ifdef H5  
			// //监听获取定位是否成功
			window['appbackJSgetLocation']=(code)=>{
				let res = JSON.parse(code);
				_this.latitude = res.latitude;
				_this.longitude = res.longitude;
			}
			// #endif
		},
		onLoad(options) {
			if (options.goodsId) {
				this.goodsId = options.goodsId
				// 查询商品信息
				this.getGoodsDetalInfos();
				this.getUserInfo();
				let addressInfo = wx.getStorageSync('addressInfo');
				if (!addressInfo || addressInfo == '') {
					this.getAddressInfo();
				}
			}
			this.getLocation();
		},
		onShow() {
			let addressInfo = wx.getStorageSync('addressInfo') || undefined;
			if (addressInfo && addressInfo.addrId) {
				this.address = addressInfo
			}
		},
		methods: {
			// 获取定位信息
			getLocation() {
				// #ifdef APP
				wx.getLocation({
					type: 'wgs84'
				}).then(res => {
					this.latitude = res.latitude;
					this.longitude = res.longitude
				})
				// #endif
				
				// #ifdef H5
					//h5获取定位
					this.$getLocation();
				// #endif
			},
			// 查询默认收货地址
			getAddressInfo() {
				_apiGetShowAddress().then(res => {
					if (res && res.code == 0) {
						this.address = res.data
					}
				})
			},
			// 查询用户信息
			getUserInfo() {
				this.$getUserInfoSync().then(res => {
					if (res && res.code == 0) {
						this.userInfo = res.data
					}
				})
			},
			// 查询商品详细信息 == 普通
			getGoodsDetalInfos() {
				_apiIntegralGoodsDetail(this.goodsId).then(res => {
					if (res && res.code == 0) {
						this.goodInfo = res.data
					}
				})
			},
			// 收货地址
			jumpAddress() {
				this.$navTo('/pages/packageA/address/address?set=true')
			},
			// 下单
			showActionSheel() {
				let interger = this.userInfo.accountEntity.amountPoints;
				let intergerTwo = this.userInfo.accountEntity.amountSigninPoints;

				let amount = this.goodInfo.amountPoints;
				let amountInteger = this.goodInfo.integralAmount;

				if (this.loading) {
					return this.$showToast('兑换中，请稍后～');
				}
				if (!this.address || !this.address.addrId || this.address.addrId == '') {
					return this.$showToast('请选择收货地址，无法兑换');
				}
				if (this.amount > this.interger) {
					return this.$showToast('PI积分不足，无法兑换');
				}
				if (this.amountInteger > this.intergerTwo) {
					return this.$showToast('签到积分不足');
				}
				let params = {
					addrId: this.address.addrId,
					goodsId: this.goodsId,
					goodsNum: 1,
					orderRemark: this.remark,
					latitude: this.latitude,
					longitude: this.longitude
				}
				//签到商品
				this.createdOrder(params);
			},
			//下单
			createdOrder(params) {
				this.loading = true
				_createIntegralOrder(params).then(res => {
					this.loading = false
					if (res && res.code == 0) {
						this.$showToast('兑换成功～');
						console.log(params, this.goodInfo.amountPoints,this.goodInfo.integralAmount)
						setTimeout(()=>{
							this.$redTo('../payDone/payDone?goodsType=4');
						},400);
					}
				}).catch(() => this.loading = false)
			},
		}
	}
</script>

<style lang="scss" scoped>
	.confirm {
		background-color: $bg-color-fa;
		min-height: 100vh;

		.address {
			background-color: $bg-color-fa;
			padding: 10rpx 30rpx;
			height: 100rpx;
			border-radius: 20rpx;

			.place {
				font-size: 32rpx;
				color: $font-color2;
			}
		}

		.goodInfo {
			.sku {
				background-color: $bg-color-fa;
				color: $font-color2;
				padding: 4rpx 18rpx 4rpx 0;
			}

			.price {
				color: $font-money-color2;
			}
		}

		.remark .van-cell {
			padding: 30rpx 0;
		}

		.ceil {
			padding: 18rpx 0;
			color: $font-color;
			font-size: 28rpx;

			.price_two {
				color: $font-money-color3;
			}

			.ceil-able {
				font-size: 32rpx;
				color: $font-color3;

				.tips {
					font-size: 24rpx;
					color:$font-color2;
				}
			}
		}

		.describe {
			color: $font-color;
		}


		.buynow {
			position: fixed;
			box-sizing: border-box;
			bottom: 0;
			padding: 20rpx 30rpx 50rpx;
			height: 190rpx;
			.i-warps{
				height: 80rpx;
				background: url('@/static/packageA/image/signPay.png') no-repeat center;
				background-size: 100% 100%;
				border-radius: 16rpx;
			}
			.interNumber {
				color: #FFF;
				position: absolute;
				top: 0;
				height: 120rpx;
				padding-left: 40rpx;
			}
		}
	}
</style>
