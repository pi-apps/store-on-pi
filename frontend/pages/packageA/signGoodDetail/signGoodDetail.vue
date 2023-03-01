<template>
	<view class="goodInfo">
		<!-- 自定义导航 -->
		<uniNav title="商品详情" />
		<!-- 顶部轮播 -->
		<u-swiper :list="goodInfo.goodsVideoPicList" bgColor="none" height="400">
		</u-swiper>
		<!--  商品信息 -->
		<view class="-bgf -warp30 mb12 good_infos f f_column">
			<view class="f_c_a mb12">
				<view class="amount_tag">
					<text v-if="goodInfo.amountPoints && goodInfo.amountPoints > 0">{{goodInfo.amountPoints}}PI积分</text>
					<text v-if="goodInfo.amountPoints > 0 && goodInfo.integralAmount > 0">+</text>
					<text v-if="goodInfo.integralAmount && goodInfo.integralAmount > 0">{{goodInfo.integralAmount}}签到积分</text>
				</view>
			</view>
			<view class="fz16 bold overfont-2">{{goodInfo.goodsName}}</view>
		</view>
		<!-- 商品详情 -->
		<view class="-bgf -warp30 mb12">
			<view class="fz16 mb12">商品详情</view>
			<view class="imgDetal f f_column" v-for="(item,index) in goodsDetailListSync" :key="index">
				<image :src="item" style="width: 100%;" mode="widthFix" />
			</view>
		</view>
		<!-- 立即购买 -->
		<view class="buynow -bgf w100 f_c_a">
			<view class="fz12 -price f_c_a">
				<text v-if="goodInfo.amountPoints && goodInfo.amountPoints >0">{{goodInfo.amountPoints}}PI积分</text>
				<text v-if="goodInfo.amountPoints > 0 && goodInfo.integralAmount >0">+</text>
				<text v-if="goodInfo.integralAmount && goodInfo.integralAmount >0">{{goodInfo.integralAmount}}签到积分</text>
			</view>
			<view class="buyNowBtn" @click="buyGoods">立即兑换</view>
		</view>
		
	</view>
</template>

<script>
	import {
		_apiIntegralGoodsDetail
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				showShare: false,
				params: {},
				
				goodsId: '',
				goodInfo: {
					achievement: 0,
					amountPoints: 0,
					discountPrice: 0
				},
				scrollIndex: 0,
				goodsDetailListSync: [],
				num: 1
			};
		},
		computed: {

		},
		components: {
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {
			if (options.goodsId) {
				this.goodsId = options.goodsId
				this.getGoodsDetalInfos();
			}
		},
		onShow() {

		},
		methods: {
			// 查询商品详细信息 == 普通
			getGoodsDetalInfos() {
				_apiIntegralGoodsDetail(this.goodsId).then(res => {
					if (res && res.code == 0) {
						this.goodInfo = res.data;
						this.goodsDetailListSync = [res.data.goodsDetailList[0] || '']
					}
				})
			},
			// 商品下单 +=====+++==123=+!@#+!+@#+!+@#+
			buyGoods() {
				const {
					goodsId
				} = this.goodInfo;
				// 确认订单
				let backUrl = `/pages/packageA/signGoodDetail/signGoodDetail?goodsId=${goodsId}`;
				if (this.$loginModel(backUrl)) {
					this.$navTo(`../signConfirm/confirm?goodsId=${goodsId}`)
				}
			},
		
		},
		/**
		 * 页面上拉触底事件的处理函数
		 */
		onReachBottom() {
			// 流处理图片 防止过多加载
			const {
				goodsDetailList
			} = this.goodInfo;
			this.scrollIndex++
			if (goodsDetailList[this.scrollIndex] && this.scrollIndex < goodsDetailList.length) {
				this.goodsDetailListSync.push(goodsDetailList[this.scrollIndex]);
			}
		},
	}
</script>

<style lang="scss" scoped>
	.goodInfo {
		background-color: $bg-color-fa;
		min-height: 100vh;
		padding-bottom: 200rpx;

		.noborder .van-cell:after {
			border: none;
		}

		.kill_warp {
			padding: 20rpx 30rpx;
			position: relative;

			.countdown_warps {
				width: 690rpx;
				height: 120rpx;
				position: absolute;
				top: 20rpx;
				box-sizing: border-box;
				padding: 0 10rpx 0 120rpx;

				.kill-price {
					color: #FFF;
				}

				.countdown {
					.time_box {
						background-color: $font-money-color2;
						color: #FFF;
						width: 40rpx;
						box-sizing: border-box;
						padding: 2rpx 8rpx;
						border-radius: 10rpx;
						margin: 0 10rpx;
						font-size: 24rpx;
					}
				}
			}

		}

		.good_infos {
			.share {
				padding: 8rpx 14rpx;
				background-color: $color-tt;
				font-size: 24rpx;
				color: #FFF;
				border-radius: 24rpx;
			}

			.amount_tag {
				color: $font-money-color2;
				padding: 4rpx 10rpx;
				border-radius: 8rpx;
				background-color: rgba($font-money-color2, 0.1);
			}
		}

		.imgDetal {
			width: 100%;
			box-sizing: border-box;
		}



		.buynow {
			position: fixed;
			box-sizing: border-box;
			bottom: 0;
			padding: 20rpx 30rpx 60rpx;
			border-top: solid 1rpx rgba($font-color2, 0.2);

			.buyNowBtn {
				background-image: linear-gradient(to bottom, #fa4935, #fb8363);
				padding: 15rpx 40rpx;
				color: #FFF;
				border-radius: 8rpx;
			}

			.price {
				color: $font-money-color2;
			}

			.sku {
				color: $font-color2
			}
		}
	}
</style>
