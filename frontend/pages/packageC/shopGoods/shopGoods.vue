<template>
	<view class="shopInfo">
		<!-- 自定义导航 -->
		<uniNav title="服务介绍" />

		<view class="goodsWarps -bgf">
			<!-- 顶部轮播 -->
			<u-swiper :list="goodsInfo.goodsBannerUrlList" bgColor="none" height="220">
			</u-swiper>
			<!-- title -->
			<view class="storeInfos -bgf">
				<view class="f_c_a">
					<view class="overfont fz18">{{goodsInfo.goodsName}}</view>
					<view class="price fz18 bold"><text class="fz12">π</text>{{goodsInfo.userFinalPrice.toFixed(8)/1}}</view>
				</view>
				<view class="shop_midle f_c_a mt8">
					<text></text>
					<text class="textLineThrough">门店价：{{goodsInfo.listingPrice}}</text>
				</view>
			</view>
		</view>
		<!-- 门店信息 -->
		<view class="-warp20-30 -bgf mt12 f f_align_center">
			<u--image :src="storeInfo.doorPhotoUrl" width="90" height="90" radius="8" />
			<view class="f f_column f_align r_warp ml12">
				<view class="storename fz18">{{storeInfo.storeName}}</view>
				<!-- <view class="sall">销量：{{storeInfo.totalNum}}</view> -->
				<view class="fz12 overfont-2">{{storeInfo.address}}</view>
			</view>
		</view>
		<!-- 详情介绍介绍 -->
		<view class="-warp20-30 -bgf mt12">
			<view class="fz16 mb12">商品详情</view>
			<view class="imgDetal f f_column" v-for="(item,index) in goodsDetailListSync" :key="index">
				<image :src="item" style="width: 100%;" mode="widthFix" />
			</view>
		</view>


		<!-- 立即购买 -->
		<view class="buynow -bgf w100 f_c_a">
			<view class="f f_column">
				<text class="fz22 mb4 -price">
					<text class="fz12">π</text>
					<u-count-to duration="1000" color="#e71c19" :endVal="goodsInfo.userFinalPrice" separator="," :decimals="8"></u-count-to>
				</text>
			</view>
			<view class="buyNowBtn" @click="buyNowGoods">立即购买</view>
		</view>
	</view>
</template>

<script>
	import {
		_storeGoodsInfo,
		_getStoreInfoById
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				goodsId: '',
				storeId: '',
				goodsInfo: {},
				storeInfo: {},
				scrollIndex: 0,
				goodsDetailListSync: []
			};
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue')
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {
			if (options && options.goodsId) {
				this.goodsId = options.goodsId
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
			// 
			getGoodsInfos() {
				_storeGoodsInfo(this.goodsId).then(res => {
					if (res && res.code == 0) {
						this.goodsInfo = res.data
						this.goodsDetailListSync = [res.data.goodsDetailsPhotosUrllList[0] || '']
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

			// 下单
			buyNowGoods() {
				if (this.goodsId == '') {
					return this.$showToast('请先选择您要购买的服务～')
				}
				this.$navTo(`../confirm/confirm?goodsId=${this.goodsId}&storeId=${this.storeId}`)
			},
		},
		/**
		 * 页面上拉触底事件的处理函数
		 */
		onReachBottom() {
			// 流处理图片 防止过多加载
			const {
				goodsDetailsPhotosUrllList
			} = this.goodsInfo;
			this.scrollIndex++;
			if (goodsDetailsPhotosUrllList[this.scrollIndex] && this.scrollIndex < goodsDetailsPhotosUrllList.length) {
				this.goodsDetailListSync.push(goodsDetailsPhotosUrllList[this.scrollIndex]);
			}
		},
	}
</script>

<style lang="scss" scoped>
	.shopInfo {
		min-height: 100vh;
		padding-bottom: 140rpx;
		box-sizing: border-box;
		color: $font-color3;

		.goodsWarps {
			padding: 0 30rpx;
		}

		.storeInfos {
			padding: 20rpx 0;

			.price {
				color: $font-money-color2;
			}

			.shop_midle {
				font-size: 24rpx;
				color: $font-color2;

				.star {
					color: #f6ef83
				}

				.middle {
					color: $font-color2;
					margin: 0 12rpx;
				}
			}
		}

		.desacribe {
			color: $font-color;
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
	}
</style>
