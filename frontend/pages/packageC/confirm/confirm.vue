<template>
	<view class="confirm">
		<!-- 自定义导航 -->
		<uniNav title="确认订单" />

		<!--  -->
		<view class="-warp30 -bgf f f_align_center mt12">
			<u-image class="mr12" :src="goodsInfo.goodsBannerUrlList ? goodsInfo.goodsBannerUrlList[0] : ''" width="90"
				height="90" radius="8" />
			<view class="f f_column f_align r_warp">
				<view class="storename fz18 overfont">{{goodsInfo.goodsName}}</view>
				<view class="sall mt4">销量：{{storeInfo.totalNum}}</view>
				<view class="fz12 mt4 overfont-2">{{storeInfo.address}}</view>
			</view>
		</view>
		<view class="mt12 -bgf -warp30">
			<u-cell-group>
				<u-cell value-class="bold price fz16" title="商品总金额" :value="'π'+goodsInfo.userFinalPrice.toFixed(8)/1 || 0"
					input-align="right" />
				<view class="f f_align_center mt20">
					<view>订单备注</view>
					<u--textarea border="none" confirmType="next" v-model="remark" count :maxlength="40"
						placeholder="请输入备注信息" autoHeight></u--textarea>
				</view>
			</u-cell-group>
		</view>

		<!-- 立即购买 -->
		<view class="buynow -bgf w100 f_c_a">
			<view class="f f_column">
				<text class="fz22 mb4 -price">
					<text class="fz12">π</text>{{goodsInfo.userFinalPrice.toFixed(8)/1 || 0}}
				</text>
			</view>
			<u-button :loading="loading" loading-text="下单中..." :disabled="loading" class="buyNowBtn"
				@click="buyNowGoods">提交订单</u-button>
		</view>
	</view>
</template>

<script>
	import {
		_storeGoodsInfo,
		_getStoreInfoById,
		_userBuyGoods
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				goodsId: '',
				storeId: '',
				goodsInfo: {},
				storeInfo: {},
				loading: false,
				remark: ''
			};
		},

		onLoad(options) {
			if (options && options.goodsId) {
				this.goodsId = options.goodsId;
				this.storeId = options.storeId
				this.init();
			}
		},
		onShow() {

		},
		methods: {
			// 
			init() {
				this.getGoodsInfos();
				this.getStoreInfo();
			},
			// 查询商品信息
			getGoodsInfos() {
				_storeGoodsInfo(this.goodsId).then(res => {
					if (res && res.code == 0) {
						this.goodsInfo = res.data
					}
				})
			},
			// 查询门店详情信息
			getStoreInfo() {
				_getStoreInfoById(this.storeId).then(res => {
					if (res && res.code == 0) {
						this.storeInfo = res.data
					}
				})
			},
			// 提价哦订单
			buyNowGoods() {
				const {
					goodsId,
					storeId,
					remark
				} = this;
				const {
					userFinalPrice
				} = this.goodsInfo;
				this.loading = true
				_userBuyGoods({
					goodsId: goodsId,
					storeId: storeId,
					orderRemark: remark
				}).then(res => {
					this.loading = false
					if (res && res.code == 0) {
						this.$redTo(
							`/pages/packageA/pay/pay?orderId=${res.data.id}&amount=${userFinalPrice}`
						)
					}
				}).catch(e => {
					this.loading = false
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	.confirm {
		min-height: 100vh;
		padding-bottom: 140rpx;
		box-sizing: border-box;
		color: $font-color3;

		.r_warp {
			height: 90px;
			color: $font-color2;

			.sall {
				color: $font-color;
			}

			.storename {
				color: $font-color3;
			}
		}

		.price {
			color: $font-money-color2;
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
				width: 300rpx;
				margin: 0;
			}

			.price {
				color: $font-money-color2;
			}
		}
	}
</style>
