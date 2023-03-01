<template>
	<view class="goodInfo">
		<!-- 顶部透明导航 -->
		<nav-opacity :showBack="true" ref="opacityNav" title="商品详情" />
		<view class="main">
			<!-- 顶部轮播 -->
			<u-swiper imgMode="scaleToFill" :list="goodInfo.goodsImgList" bgColor="none" height="400">
			</u-swiper>
			<!-- 秒杀信息 -->
			<view class="kill_warp" v-if="goodsType == '2'">
				<view class="f_c_a countdown_warps">
					<view class="fz12 kill-price">π<text class="fz24 bold">{{(killInfo.price).toFixed(2)}}</text>
					</view>
					<view class="f_column f_c_a">
						<text class="-price">距结束还剩：</text>
						<u-count-down v-if="killInfo.seckillTimeSeconds && killInfo.seckillTimeSeconds > 0" millisecond
							:time="killInfo.seckillTimeSeconds*1000" autoStart @change="onChangeTime"
							@finish="ifEndsSenkill">
							<view class="countdown f_c_c w100">
								<view class="f f_align_center">
									<view class="time_box">{{$padZero(timeData.hours)}}</view>:
									<view class="time_box">{{$padZero(timeData.minutes)}}</view>:
									<view class="time_box">{{$padZero(timeData.seconds)}}</view>:
									<view class="time_box">{{$padZero(parseInt(timeData.milliseconds/10))}}</view>
								</view>
							</view>
						</u-count-down>
					</view>
				</view>
			</view>
			<!--  商品信息 -->
			<view class="goods_wraps">
				<view class="-bgf mb12 good_infos f f_column radius5">
					<view class="f_c_a">
						<view>
							<block v-if="goodsType == '1'">
								<view v-if="goodInfo.amountPoints > 0" class="amount_tag">
									{{goodInfo.amountPoints}}PI积分抵{{(goodInfo.amountPoints).toFixed(2)}}元
								</view>
								<view v-else-if="goodInfo.legalRightIntegral > 0" class="amount_tag">
									{{goodInfo.legalRightIntegral}}权益积分抵{{(goodInfo.legalRightIntegral/100).toFixed(2)}}元
								</view>
							</block>
							<block v-else-if="goodsType == '3'">
								<view v-if="goodInfo.amountPoints > 0" class="amount_tag">
									赠送{{goodInfo.amountPoints}}PI积分
								</view>
							</block>
						</view>
					</view>

					<view class="f_c_a mb12" v-if="goodsType != '2' ">
						<view class="f f_align_center">
							<view
								v-if="userInfo.achievementEntity.saleUserType != '10009001' && goodInfo.vipPrice && goodInfo.vipPrice > 0"
								class="fz12 -price">π<text class="fz24 bold">{{(goodInfo.vipPrice).toFixed(2)}}</text>
							</view>
							<view v-else class="fz12 -price">π<text
									class="fz24 bold">{{(goodInfo.discountPrice).toFixed(2)}}</text></view>
							<u--image
								v-if="userInfo.achievementEntity.saleUserType != '10009001' && goodInfo.vipPrice && goodInfo.vipPrice > 0"
								class="ml8 mt8" src="/static/packageA/image/goods/ico@2x.png" width="76rpx"
								height="32rpx" />
							<view class="textLineThrough f_align_end ml8 mt8">
								<text>π{{(goodInfo.price).toFixed(2)}}</text>
							</view>
						</view>
					</view>
					<!-- 会员swiper -->
					<swiper
						v-if="goodInfo.vipPrice && goodInfo.vipPrice > 0 && userInfo.achievementEntity.saleUserType == '10009001'"
						class="vipPrice_swiper" :autoplay="true">
						<swiper-item @click="jumpVipUrl('/pages/packageA/vip/vip')">
							<view class="swiper_item_bar f f_align_center">
								<view class="bold fz12">开通PI权益，预计此单可省<text
										class="price">{{(goodInfo.discountPrice-goodInfo.vipPrice).toFixed(2)}}元</text>
								</view>
								<u--image class="f_c_c ml8" src="/static/packageA/image/goods/jinru@2x.png"
									width="28rpx" height="28rpx" />
							</view>
						</swiper-item>
						<!-- <swiper-item @click="jumpVipUrl('/pages/packageA/moreTiks/moreTiks')">
							<view class="swiper_item_bar bg_01 f f_align_center">
								<view class="bold fz12">开通抖音课程，预计此单可省<text
										class="price">{{(goodInfo.discountPrice-goodInfo.vipPrice).toFixed(2)}}元</text>
								</view>
								<u--image class="f_c_c ml8" src="/static/packageA/image/goods/jinru2@2x.png"
									width="28rpx" height="28rpx" />
							</view>
						</swiper-item> -->
					</swiper>
					<view class="fz16 bold overfont-2">{{goodInfo.goodsName}}</view>

				</view>
				<!-- 选择规格 -->
				<view class="-bgf mb12 radius5">
					<u-cell-group :border="false">
						<u-cell
							v-if="(goodInfo.isPhysicalGoods && goodInfo.isPhysicalGoods == '0') || (goodInfo.goodsSpecificationList && goodInfo.goodsSpecificationList.length > 0 )"
							@click="showActionSheel" title="选择规格:"
							:value="skuName.length && skuName.length > 0 ? (skuName.join('/')+';数量 '+num) : '请选择规格'"
							is-link />
						<u-cell :title="'晋升值：'+goodInfo.achievement" value="" />
						<u-cell :border="false" :title="goodInfo.expressRemark" value="" />
					</u-cell-group>
				</view>
			</view>

			<!-- 商品详情 -->
			<view class="mb12">
				<view class="fz16 mb12 f_c_c">
					<u--image src="/static/packageA/image/detail@2x.png" width="213" height="21" />
				</view>
				<view class="imgDetal f f_column" v-for="(item,index) in goodsDetailListSync" :key="index">
					<image :src="item" style="width: 100%;" mode="widthFix" />
				</view>
			</view>

			<!-- 立即购买 -->
			<view class="buynow -bgf w100 f_c_a">
				<view class="f f_column">
					<text v-if="goodsType == '2'" class="fz22 mb4 -price">
						<text class="fz12">π</text>
						<u-count-to duration="1000" color="#e71c19" :endVal="killInfo.price" separator=","
							:decimals="2"></u-count-to>
					</text>
					<text v-else class="fz22 mb4 -price">
						<text class="fz12">π</text>
						<u-count-to
							v-if="userInfo.achievementEntity.saleUserType != '10009001' && goodInfo.vipPrice && goodInfo.vipPrice > 0"
							duration="1000" color="#e71c19" :endVal="goodInfo.vipPrice" separator="," :decimals="2">
						</u-count-to>
						<u-count-to v-else duration="1000" color="#e71c19" :endVal="goodInfo.discountPrice"
							separator="," :decimals="2"></u-count-to>
					</text>
					<view v-if="skuName.length && skuName.length >0" class="fz12 sku">{{skuName.join('/')}}
					</view>
				</view>
				<view class="buyNowBtn" @click="showActionSheel">{{goodsType == '2' ? '立即抢购' : '立即购买'}}</view>
			</view>
		</view>

		<!-- 规格选择 -->
		<u-action-sheet class="goodsDetail" :show="actionSheel" title="请先选择商品规格哦" @close="onClose" round="12">
			<view class="goodSku -warp30">
				<!-- 商品 -->
				<view class="f f_align_center mb24">
					<u--image radius="8" class="f_shrink0" width="100" height="100"
						:src="goodInfo.goodsImgList ? goodInfo.goodsImgList[0] : ''" />
					<view class="ml12 f_column" style="height: 80px;text-align: left;">
						<view class="fz16 bold overfont-2">{{goodInfo.goodsName}}</view>
						<view class="f f_align_center">
							<view v-if="goodsType == '2'" class="fz12 -price">π<text
									class="fz24 bold">{{(killInfo.price).toFixed(2)}}</text></view>
							<template v-else>
								<view
									v-if="userInfo.achievementEntity.saleUserType != '10009001' && goodInfo.vipPrice && goodInfo.vipPrice > 0"
									class="fz12 -price">π<text
										class="fz24 bold">{{(goodInfo.vipPrice).toFixed(2)}}</text></view>
								<view v-else class="fz12 -price">π<text
										class="fz24 bold">{{(goodInfo.discountPrice).toFixed(2)}}</text></view>
							</template>
							<u--image
								v-if="userInfo.achievementEntity.saleUserType != '10009001' && goodInfo.vipPrice && goodInfo.vipPrice > 0"
								class="ml8 mt8" src="/static/packageA/image/goods/ico@2x.png" width="76rpx"
								height="32rpx" />
						</view>
					</view>
				</view>
				<!-- 规格 -->
				<view class="specificak" v-for="(item,index) in goodInfo.goodsSpecificationList"
					:key="item.specificationId">
					<view class="sp_title">{{item.specificationName}}：</view>
					<view class="f f_warp sku">
						<block v-for="(sku, _index) in item.goodsAttributeList" :key="sku.attributeId">
							<view @click="skuSelect(index,sku)"
								:class="'sku_btn '+ (goods_sku[index] && goods_sku[index].attributeId == sku.attributeId ? 'active' : '')">
								{{sku.attributeName}}
								<view v-if="((index+1) == goodInfo.goodsSpecificationList.length) && sku.inventor <= 0"
									class="isNo">缺货</view>
							</view>
						</block>
					</view>
				</view>
				<!-- 数量 -->
				<view class="f_c_a sku_numer">
					<text>购买数量：</text>
					<u-number-box integer :disabled="goodsType == '2' ? true : false " v-model="num" :min="1" :max="99"
						@change="onChangeNum" />
				</view>
				<u-button @click="buyGoods" class="f_c_c w100 realy_buy">{{goodsType == '2' ? '立即抢购' : '立即购买'}}
				</u-button>
			</view>
		</u-action-sheet>

	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		_apiGoodsInfo,
		_apiGoodsSeckillInfo,
		_apiGoodsVipDetail
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				showShare: false,

				actionSheel: false,
				goodsId: '',
				goodsType: '1',
				killInfo: {
					seckillTimeSeconds: 0,
					crazyModel: '1',
					inventory: 0,
					price: 0
				},
				timeData: {},
				goodInfo: {
					price: 0,
					achievement: 0,
					amountPoints: 0,
					discountPrice: 0
				},
				scrollIndex: 0,
				goodsDetailListSync: [],
				num: 1,
				goods_sku: [],
				skuName: [],

				params: {},
				userInfo: {
					achievementEntity: {
						saleUserType: '10009001',
					}
				}
			};
		},
		computed: {
			...mapState({
				statusBarHeight: state => state.statusBarHeight
			})
		},
		components: {
			'nav-opacity': () => import('@/components/nav-opacity/nav-opacity.vue')
		},
		onLoad(options) {
			if (options.goodsId) {
				this.goodsId = options.goodsId;
				this.goodsType = options.type
				this.init();
			}
		},
		onShow() {
			this.actionSheel = false;
			this.getUserInfo();
		},
		methods: {
			// 查询用户信息
			getUserInfo() {
				this.$getUserInfoSync(false, false).then(res => {
					if (res && res.code == 0) {
						this.userInfo = res.data
					} else if (res.code == 401) {
						this.userInfo = {
							achievementEntity: {
								saleUserType: '10009001',
							}
						}
					}
				})
			},
			// 
			init() {
				if (this.goodsType == '1') {
					//普通
					this.getGoodsDetalInfos();
				} else if (this.goodsType == '2') {
					//秒杀
					this.getGoodsSeclillGoodsInfo();
				} else if (this.goodsType == '3') {
					//特权
					this.getGoodsVipDetalInfos();
				}
			},
			// 查询商品详细信息 == 普通
			getGoodsDetalInfos() {
				_apiGoodsInfo({
					goodsId: this.goodsId
				}).then(res => {
					if (res && res.code == 0) {
						this.goodInfo = res.data;
						this.goodsDetailListSync = [res.data.goodsDetailList[0] || '']
					}
				})
			},
			// 查询商品详细信息 == 秒杀
			getGoodsSeclillGoodsInfo() {
				_apiGoodsSeckillInfo(this.goodsId).then(res => {
					if (res && res.code == 0) {
						let goodsInfo = res.data.goodsInfo
						this.killInfo = res.data;
						this.goodInfo = goodsInfo,
							this.goodsDetailListSync = [goodsInfo.goodsDetailList[0] || '']
					}
				})
			},
			// 查询商品详细信息 == 特权
			getGoodsVipDetalInfos() {
				_apiGoodsVipDetail({
					goodsId: this.goodsId
				}).then(res => {
					if (res && res.code == 0) {
						this.goodInfo = res.data;
						this.goodsDetailListSync = [res.data.goodsDetailList[0] || ''];
					}
				})
			},
			// 展开
			showActionSheel() {
				this.actionSheel = true
			},
			// 选择数量
			onChangeNum(e) {
				this.num = e.detail
			},
			// 关闭
			onClose() {
				this.actionSheel = false
			},
			// 选择规格
			skuSelect(index, sku) {
				const {
					goodsSpecificationList
				} = this.goodInfo;
				let isSallIndex = goodsSpecificationList.length;
				if (index + 1 == isSallIndex && sku.inventor <= 0) {
					return this.$showToast(`所选规格 ${sku.attributeName} 库存不足`);
				}

				this.$set(this.goods_sku, index, sku)
				this.$set(this.skuName, index, sku.attributeName)
			},
			// 商品下单 +=====+++==123=+!@#+!+@#+!+@#+
			buyGoods() {
				const {
					goods_sku,
					goodInfo,
					num,
					skuName,
					goodsType,
					goodsId,
					killInfo
				} = this;
				const {
					goodsSpecificationList
				} = goodInfo;
				// 虚拟商品没有SKU
				if (goodsSpecificationList && goodsSpecificationList.length && goodsSpecificationList.length > 0) {
					for (let i = 0; i < goodsSpecificationList.length; i++) {
						if (!goods_sku[i]) {
							return this.$showToast(`请选择规格：${goodsSpecificationList[i].specificationName}`);
						}
					}
					// 购买库存判断
					let _len = goodsSpecificationList.length - 1;
					if (goods_sku[_len] && goods_sku[_len].inventor <= 0) {
						return this.$showToast(`规格:${goods_sku[i].attributeName},库存不足～`);
					}
					// 购买数量限制
					if (num > goods_sku[_len].inventor) {
						return this.$showToast(`最多购买数量:${goods_sku[_len].inventor}`);
					};
				}
				// 秒杀库存限制
				if (goodsType == '2' && killInfo.crazyModel == '0' && num > killInfo.inventory) {
					return this.$showToast('您慢了一步，当前商品已被抢完');
				};
				// 合并参数
				let skuId = Array.from(goods_sku, ({
					attributeId
				}) => attributeId);
				let postJson = encodeURIComponent(JSON.stringify({
					skuId: skuId,
					goodsId: goodsId,
					skuName: skuName,
					num: num,
					goodsType: goodsType
				}));
				// 确认订单
				let backUrl = `/pages/packageA/goodDetail/goodDetail?goodsId=${goodsId}&type=${goodsType}`;
				console.log(backUrl)
				if (this.$loginModel(backUrl)) {
					this.$navTo(`../confirm/confirm?params=${postJson}`)
				}
			},
			// 倒计时
			onChangeTime(e) {
				this.timeData = e
			},
			//倒计时结束
			ifEndsSenkill() {
				this.$showToast('当前商品秒杀结束，请重新选择～')
				setTimeout(() => {
					this.$navBack();
				}, 700)
			}
			/**
			 * scorl
			 * **/
			,
			jumpVipUrl(url) {
				const {
					goodsType,
					goodsId
				} = this;
				// 确认订单
				let backUrl = `/pages/packageA/goodDetail/goodDetail?goodsId=${goodsId}&type=${goodsType}`;
				console.log(backUrl)
				if (this.$loginModel(backUrl)) {
					this.$navTo(url)
				}
			}
		},
		onPageScroll(e) {
			// 透明导航
			this.$nextTick(() => {
				this.$refs.opacityNav.navScorllPage(e);
			})
		},
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

		/deep/ .goodsDetail {
			.u-transition {
				z-index: 100 !important;
			}

			.u-transition:first-child {
				z-index: 90 !important;
			}
		}

		.noborder .van-cell:after {
			border: none;
		}

		.kill_warp {
			padding: 20rpx 30rpx;
			position: relative;
			background: url('@/static/image/mall/seckill-icon.png') no-repeat center;
			background-size: 100% 100%;
			height: 120rpx;
			border-radius: 12rpx;

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

		.goods_wraps {
			padding: 20rpx 24rpx;

			.radius5 {
				border-radius: 12rpx;
			}

			.vipPrice_swiper {
				height: 80rpx;
				margin-bottom: 8rpx;

				.swiper_item_bar {
					height: 80rpx;
					color: #929292;
					padding-left: 110rpx;
					background: url('@/static/packageA/image/goods/chebao@2x.png') no-repeat center;
					background-size: 100% 100%;

					&.bg_01 {
						background: url('@/static/packageA/image/goods/douyin@2x.png') no-repeat center;
						background-size: 100% 100%;
					}

					.price {
						color: #F7694A;
					}
				}
			}
		}

		.good_infos {
			padding: 18rpx 16rpx;
			box-sizing: border-box;

			.textLineThrough {
				color: #999;
			}

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

			.sku {
				color: $font-color2
			}
		}

		.goodSku {
			.specificak {
				margin-bottom: 30rpx;

				.sp_title {
					text-align: left;
				}

				.sku {
					margin-top: 24rpx;

					.sku_btn {
						background-color: $bg-color-f5;
						padding: 16rpx 30rpx;
						margin-right: 30rpx;
						margin-bottom: 20rpx;
						border-radius: 12rpx;
						border: solid 1rpx $bg-color-f5;
						position: relative;

						&:last-child {
							margin-right: 0;
						}

						&.active {
							color: $font-money-color2;
							background-color: rgba($font-money-color2, 0.09);
							border-color: $font-money-color2;
						}

						.isNo {
							position: absolute;
							top: -20rpx;
							right: -22rpx;
							padding: 4rpx 10rpx;
							font-size: 20rpx;
							background-color: rgba($font-color2, 0.4);
							color: #FFF;
							border-radius: 20rpx 0 20rpx 0;

						}
					}
				}
			}

			.sku_numer {
				padding: 10rpx 0 50rpx;
			}

			.realy_buy {
				background-image: linear-gradient(to bottom, #fa4935, #fb8363);
				border-radius: 18rpx;
				color: #FFF;
				font-size: 32rpx;
			}
		}
	}
</style>
