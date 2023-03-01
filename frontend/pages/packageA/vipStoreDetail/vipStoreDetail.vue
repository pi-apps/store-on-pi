<template>
	<view class="goodInfo">
		<!-- 顶部透明导航 -->
		<nav-opacity :showBack="true" ref="opacityNav" title="商品详情" />
		<view class="main">
			<!-- 顶部轮播 -->
			<u-swiper imgMode="scaleToFill" :list="goodInfo.goodsImgList" bgColor="none" height="400">
			</u-swiper>
			<!--  商品信息 -->
			<view class="-bgf -warp30 mb12 good_infos f f_column">
				<view class="f_c_a mb12">
					<view class="f f_align_center">
						<view class="fz12 -price">π<text
								class="fz24 bold">{{(goodInfo.discountPrice).toFixed(2)}}</text>
						</view>
						<view class="textLineThrough f_align_end ml12">
							<text>π{{(goodInfo.price).toFixed(2)}}</text>
						</view>
					</view>
				</view>
				<view class="fz16 bold overfont-2">{{goodInfo.goodsName}}</view>
				<view class="f_c_a mt8">
					<view v-if="goodInfo.amountPoints > 0" class="amount_tag">赠送{{goodInfo.amountPoints}}PI积分
					</view>
				</view>
			</view>
			<!-- 适用门店 -->
			<view class="store_list -warp mb8 -bgf fz12"
				v-if="goodInfo.storeOrderByList && goodInfo.storeOrderByList.length > 0">
				<view class="f_c_a">
					<text class="fz16 bold">适用门店</text>
					<view class="f_c_e" @click="doUseStore">
						<text>{{goodInfo.storeOrderByList.length}}家门店适用</text>
						<u-icon class="ml4" name="arrow-right" size="12" color="#C4C4C4" />
					</view>
				</view>
				<view class="f_c_a">
					<view class="f f_align_center mt8">
						<u--image radius="5" width="50" height="50" :src="goodInfo.storeOrderByList[0].doorPhotoUrl" />
						<view class="f f_column ml8">
							<text class="overfont fz14 bold">{{goodInfo.storeOrderByList[0].storeName}}</text>
							<view
								:class="'storeStatus f_baseline mt8 '+(goodInfo.storeOrderByList[0].status == '0' ? '' : 'isDown')">
								{{goodInfo.storeOrderByList[0].status == '0' ? '营业中' : '休息中'}}
							</view>
						</view>
					</view>
					<u--image @click="callphone" src="/static/packageA/image/dianhua@2x.png" width="40" height="40" />
				</view>
				<view class="f_c_a fz12 mt12 b_b_infos">
					<view class="tip_c">最近{{(goodInfo.storeOrderByList[0].distance/1000).toFixed(2)}}KM</view>
					<view class="tip_tag"></view>
					<text style="width: 200px;" class="overfont tip_c">{{goodInfo.storeOrderByList[0].address}}</text>
					<view class="f_c_e" @click="jumpMapNavgation">导航
						<u-icon class="ml4" name="arrow-right" size="12" color="#C4C4C4" />
					</view>
				</view>
			</view>
			<!-- 选择规格 -->
			<view class="-bgf mb12">
				<u-cell-group>
					<u-cell
						v-if="(goodInfo.isPhysicalGoods && goodInfo.isPhysicalGoods == '0') || (goodInfo.goodsSpecificationList && goodInfo.goodsSpecificationList.length > 0 )"
						@click="showActionSheel" title="选择规格:"
						:value="skuName.length && skuName.length > 0 ? (skuName.join('/')+';数量 '+num) : '请选择规格'"
						is-link />
					<u-cell :border="false" :title="'晋升值：'+goodInfo.achievement" value="" />
				</u-cell-group>
			</view>
			<!-- 使用流程 -->
			<u--image v-if="goodInfo.goodsChannelType == '1'" class="mb8" src="/static/packageA/image/liucheng@2x.png"
				width="100%" height="108" />
			<u--image v-else-if="goodInfo.goodsChannelType == '3'" class="mb8" src="/static/packageA/image/liucheng@3x.png"
				width="100%" height="108" />
			<u--image v-else class="mb8" src="/static/packageA/image/liucheng1@2x.png" width="100%" height="108" />
			<!-- 商品详情 -->
			<view class="-bgf -warp30 mb12">
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
					<text class="fz22 mb4 -price">
						<text class="fz12">π</text>
						<u-count-to duration="1000" color="#e71c19" :endVal="goodInfo.discountPrice" separator=","
							:decimals="2"></u-count-to>
					</text>
					<view v-if="skuName.length && skuName.length >0" class="fz12 sku">{{skuName.join('/')}}
					</view>
				</view>
				<view class="buyNowBtn" @click="showActionSheel">立即购买</view>
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
						<view class="fz12 -price">π<text
								class="fz24 bold">{{(goodInfo.discountPrice).toFixed(2)}}</text></view>
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
					<u-number-box integer :disabled="goodInfo.goodsChannelType != '0' ? true : false " v-model="num"
						:min="1" :max="99" @change="onChangeNum" />
				</view>
				<u-button @click="buyGoods" class="f_c_c w100 realy_buy">立即购买</u-button>
			</view>
		</u-action-sheet>

		<!-- 购买须知 -->
		<u-overlay :show="showXy" :zIndex="100">
			<view class="f_c_c f_column" style="height: 100vh;">
				<view class="xy_warps f f_column f_align_center">
					<text class="fz18 bold">购买须知</text>
					<scroll-view :scroll-y="true" class="overXycontent mt10">
						<u-parse :content="xyContent"></u-parse>
					</scroll-view>
					<view class="w100 fz14 mt10" style="color: #F7694A;">
						请阅读并同意购买须知
					</view>
					<view class="f_c_a w100 mt20">
						<view class="btn_xy f_c_c bold goast" @click="cancelBuy">不同意</view>
						<view @click="confirmBuys" :class="'btn_xy f_c_c bold '+(overTime > 0 ? 'goast' : '')">
							{{ overTime > 0 ? overTime+'秒后' : '同意' }}
						</view>
					</view>
				</view>

			</view>
		</u-overlay>
	</view>
</template>

<script>
	import {
		_apiGoodsVipDetail,
		_getAgreement
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				showXy: false,
				xyContent: '',
				overTime: 10,


				showShare: false,
				params: {},

				actionSheel: false,
				goodsId: '',
				goodsType: '1',

				goodInfo: {
					price: 0,
					achievement: 0,
					amountPoints: 0,
					discountPrice: 0,
					storeOrderByList: []
				},
				scrollIndex: 0,
				goodsDetailListSync: [],
				num: 1,
				goods_sku: [],
				skuName: [],
				latitude: '',
				longitude: ''
			};
		},
		mounted() {
			const _this = this;
			//#ifdef H5  
			// //监听获取定位是否成功
			window['appbackJSgetLocation'] = (code) => {
				let res = JSON.parse(code);
				_this.latitude = res.latitude;
				_this.longitude = res.longitude;
				// 查询商品
				_this.getGoodsVipDetalInfos();
			}
			// #endif
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
			this.showXy = false;
		},
		methods: {
			// 
			init() {
				//特权 本地联调打开
				// this.getGoodsVipDetalInfos();
				//h5获取定位
				this.$getLocation();
			},
			// 查询商品详细信息 == 特权
			getGoodsVipDetalInfos() {
				_apiGoodsVipDetail({
					goodsId: this.goodsId,
					latitude: this.latitude,
					longitude: this.longitude,
					// 联调测试
					// latitude: '30.68144',
					// longitude: '103.8559',
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
					goodsId
				} = this;
				const {
					goodsSpecificationList,
					goodsChannelType
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

				// 
				if (goodsChannelType == '3') {
					this.getXyAuto();
					this.actionSheel = false;
					this.showXy = true;
					this.overTime = 10;
					this.countDownLay();
				} else {
					this.buyGoodsConfirm();
				}
			},
			buyGoodsConfirm() {
				const {
					goods_sku,
					num,
					skuName,
					goodsType,
					goodsId
				} = this;
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
			
			// 
			callphone() {
				const [{
					mobile
				}] = this.goodInfo.storeOrderByList;
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
				}] = this.goodInfo.storeOrderByList;
				this.$refs.navgationRefs.updataData({
					latitude: latitude,
					longitude: longitude,
					name: storeName,
					address: address
				});
			},
			doUseStore() {
				this.$navTo(
					`../vipStoreList/vipStoreList?goodsId=${this.goodsId}&latitude=${this.latitude}&longitude=${this.longitude}`
				)
			},
			getXyAuto() {
				_getAgreement('10010010').then(res => {
					if (res && res.code == '0') {
						this.xyContent = res.data.agreementContext
					}
				})
			},
			countDownLay() {
				if (this.overTime > 0) {
					setTimeout(() => {
						this.overTime--
						this.countDownLay();
					}, 1000)
				}
			},
			cancelBuy() {
				this.showXy = false;
				this.overTime = 0;
			},
			confirmBuys() {
				if (this.overTime > 0) {
					this.$showToast(`还需等待${this.overTime}秒,请仔细阅读购买须知！`)
				} else {
					this.cancelBuy()
					this.buyGoodsConfirm();
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

		.xy_warps {
			width: 580rpx;
			height: 776rpx;
			border-radius: 40rpx;
			background-color: #fff;
			padding: 50rpx 28rpx 54rpx;
			box-sizing: border-box;
			color: #292929;

			.overXycontent {
				background-color: #FAFAFA;
				padding: 20rpx 26rpx;
				box-sizing: border-box;
				height: 420rpx;
				border-radius: 12rpx;
				color: #9A9A9A;
				font-size: 24rpx;
			}

			.btn_xy {
				width: 224rpx;
				height: 80rpx;
				background-color: #F7694A;
				border-radius: 40rpx;
				color: #FFF;

				&.goast {
					background-color: #fff;
					color: #C1C1C1;
					border: solid 2rpx #C1C1C1;
				}
			}
		}

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

		.good_infos {
			border-radius: 24rpx;
			margin-top: -24rpx;
			position: relative;
			z-index: 10;

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
				border-radius: 36rpx;
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
