<template>
	<view class="mall">
		<!-- 自定义导航 -->
		<uniNav leftIcon="" title="PI商城">
			<view class="top_tabs">
				<u-tabs :current="active" :activeStyle="{color:'#f67112'}" lineColor="#FFAF6C" :list="goodTypeList"
					keyName="typeName" @change="onChangeTab"></u-tabs>
			</view>
		</uniNav>
		<!--  -->
		<view v-if="goodType== 'Home'" class="w100">
			<view class="swiperHeight" v-if="mallBannerImg.length && mallBannerImg.length > 0">
				<u-swiper imgMode="scaleToFill" :list="mallBannerImg" @click="bannerJumps" :radius="0" bgColor="none"
					keyName="adImgUrl" indicator height="128">
				</u-swiper>
			</view>
			<!-- 顶部导航呢 -->
			<view class="mall_top f_c_a">
				<view class="f f_column f_align_center" v-for="(item,index) in top_vip" :key="index"
					@click="doNavTo(item)">
					<u--image width="62" height="62" :src="item.ico" />
					<text class="icon_title">{{item.title}}</text>
				</view>
			</view>
			<view class="main_shop_mall">
				<!--推荐商品 -->
				<view
					v-if="(good_tj_kill.length && good_tj_kill.length > 0) || (good_tj_vip.length && good_tj_vip.length > 0)"
					class="f_c_a">
					<view class="tj_random_goods f_column f_align f"
						v-if="good_tj_kill.length && good_tj_kill.length > 0">
						<!-- 倒计时 -->
						<u-count-down
							v-if="nowTime.remainingTime && nowTime.remainingTime > 0 && nowTime.killStatus != '3'"
							:time="nowTime.remainingTime*1000" autoStart @change="onChangeTime" @finish="ifEndsSenkill">
							<view class="countdown f_c_c">
								<view class="f f_align_center">
									<view class="time_box f_c_c">{{$padZero(timeData.hours)}}</view>:
									<view class="time_box f_c_c">{{$padZero(timeData.minutes)}}</view>:
									<view class="time_box f_c_c">{{$padZero(timeData.seconds)}}</view>
								</view>
							</view>
						</u-count-down>
						<view class="f_c_a">
							<view v-for="item in good_tj_kill" :key="item.goodsId" class="s_l_item f f_column f_align"
								@click="goodsKillInfo(item)">
								<u--image class="f_c_c" radius="4" :src="item.goodsInfo.goodsImgList[0]" width="70"
									height="70" />
								<view class="overfont fz12">{{item.goodsInfo.goodsName}}</view>
								<view class="f f_align_center fz12">
									<view class="price"><text class="fz10">π</text>{{item.price}}</view>
									<view class="textLineThrough fz10 ml4"><text>π</text>{{item.goodsInfo.price}}</view>
								</view>
							</view>
						</view>
						<u--image src="/static/image/mall/join@2x.png" width="300rpx" height="40rpx" />
					</view>
					<view class="tj_random_goods vip f_c_a" v-if="good_tj_vip.length && good_tj_vip.length > 0">
						<view v-for="item in good_tj_vip" :key="item.goodsId" class="s_l_item f f_column f_align"
							@click="goodsInfo(item)">
							<u--image class="f_c_c" radius="4" :src="item.goodsImgList[0]" width="70" height="70" />
							<view class="overfont fz12">{{item.goodsName}}</view>
							<view class="f f_align_center fz12">
								<view class="price"><text class="fz10">π</text>{{item.discountPrice}}</view>
								<view class="textLineThrough fz10 ml4"><text>π</text>{{item.price}}</view>
							</view>
							<view class="low_dow overfont f_c_c">直降{{(item.price-item.discountPrice).toFixed(2)}}元
							</view>
						</view>
					</view>
				</view>
				<!-- 爆款推荐 -->
				<view class="hot_goog_list mt12" v-if="hotGoodsList.length && hotGoodsList.length > 0">
					<view class="f_c_a">
						<view class="f f_align_center">
							<u--image class="f_c_c" src="/static/image/mall/hot.png" width="15" height="14" />
							<text class="g_hot_tt ml4">爆款推荐</text>
						</view>
						<view class="moreList f f_align_center" @click="loadMoreHot">
							查看更多
							<u-icon name="arrow-right" color="#B2B2B2" size="12" />
						</view>
					</view>
					<view class="good_hot">
						<scroll-view :scroll-x="true" style="width: 670rpx;">
							<view class="f f_align_center">
								<view class="g_hot_list f_shrink0" v-for="(item,index) in hotGoodsList"
									@click="goodsInfo(item)" :key="item.goodsId">
									<u--image class="f_shrink0 f_c_c" :src="item.goodsImgList[0]" width="106"
										height="106" radius="5" />
									<view class="f f_column f_align w100">
										<view class="overfont">{{item.goodsName}}</view>
										<view class="mt4">
											<text class="fz10 moneys mr8">π<text
													class="fz16 bold">{{item.discountPrice}}</text></text>
											<text class="textLineThrough fz12">π{{item.price}}</text>
										</view>
									</view>
								</view>
							</view>
						</scroll-view>
					</view>
				</view>
				<!-- 商品分类 -->
				<view class="goods_clasify_main" v-if="isLike && token != ''">
					<!-- 猜你喜欢 -->
					<view class="f_c_c">
						<u-image src="/static/image/mall/cainixihuan@2x.png" width="177" height="18" />
					</view>
					<view class="good_class_list mt20">
						<!-- 商品信息列表展示 -->
						<view class="good_col_left">
							<template v-for="(goods,index) in good_tj_list">
								<view class="good_warps overfont -bgf" v-if="index % 2 == 0" :key="goods.goodsId"
									@click="goodsInfo(goods)">
									<u--image class="f_c_c" :radius="4" :src="goods.goodsImgList[0]" width="100%"
										height="146" />
									<view class="good_b_text">
										<view class="overfont-2">
											{{goods.goodsName}}
										</view>
										<view class="g_b_price mt4">
											<text class="money fz12 mr8">π<text
													class="fz16 bold">{{goods.discountPrice}}</text></text>
											<text class="textLineThrough fz12">π{{goods.price}}</text>
										</view>
										<view class="f_c_c price_sign"
											v-if="goods.amountPoints && goods.amountPoints > 0">
											<view class="bold">积分可抵{{goods.amountPoints}}元</view>
										</view>
										<view class="f_c_c price_sign"
											v-else-if="goods.legalRightIntegral && goods.legalRightIntegral > 0">
											<view class="bold">积分可抵{{goods.legalRightIntegral/100}}元</view>
										</view>
									</view>
								</view>
							</template>
						</view>
						<view class="good_col_right">
							<template v-for="(goods,index) in good_tj_list">
								<view class="good_warps overfont -bgf" v-if="index % 2 != 0" :key="goods.goodsId"
									@click="goodsInfo(goods)">
									<u--image class="f_c_c" :radius="4" :src="goods.goodsImgList[0]" width="100%"
										height="146" />
									<view class="good_b_text">
										<view class="overfont-2">
											{{goods.goodsName}}
										</view>
										<view class="g_b_price mt4">
											<text class="money fz12 mr8">π<text
													class="fz16 bold">{{goods.discountPrice}}</text></text>
											<text class="textLineThrough fz12">π{{goods.price}}</text>
										</view>
										<view class="f_c_c price_sign"
											v-if="goods.amountPoints && goods.amountPoints > 0">
											<view class="bold">积分可抵{{goods.amountPoints}}元</view>
										</view>
										<view class="f_c_c price_sign"
											v-else-if="goods.legalRightIntegral && goods.legalRightIntegral > 0">
											<view class="bold">积分可抵{{goods.legalRightIntegral/100}}元</view>
										</view>
									</view>
								</view>
							</template>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 商品分类--- -->
		<view class="goods_clasify_main mt12" v-else>
			<view class="good_class_list" style="padding: 0 20rpx;">
				<LoadMoreList :page="page" :data="page.list">
					<view style="flex-direction: row;">
						<!-- 商品信息列表展示 -->
						<view class="good_col_left">
							<template v-for="(goods,index) in page.list">
								<view class="good_warps overfont -bgf" v-if="index % 2 == 0" :key="goods.goodsId"
									@click="goodsInfo(goods)">
									<u--image class="f_c_c" :radius="4" :src="goods.goodsImgList[0]" width="100%"
										height="146" />
									<view class="good_b_text">
										<view class="overfont-2">
											{{goods.goodsName}}
										</view>
										<view class="g_b_price mt4">
											<text class="money fz12 mr8">π<text
													class="fz16 bold">{{goods.discountPrice}}</text></text>
											<text class="textLineThrough fz12">π{{goods.price}}</text>
										</view>
										<view class="f_c_c price_sign"
											v-if="goods.amountPoints && goods.amountPoints > 0">
											<view class="bold">积分可抵{{goods.amountPoints}}元</view>
										</view>
										<view class="f_c_c price_sign"
											v-else-if="goods.legalRightIntegral && goods.legalRightIntegral > 0">
											<view class="bold">积分可抵{{goods.legalRightIntegral/100}}元</view>
										</view>
									</view>
								</view>
							</template>
						</view>
						<view class="good_col_right">
							<template v-for="(goods,index) in page.list">
								<view class="good_warps overfont -bgf" v-if="index % 2 != 0" :key="goods.goodsId"
									@click="goodsInfo(goods)">
									<u--image class="f_c_c" :radius="4" :src="goods.goodsImgList[0]" width="100%"
										height="146" />
									<view class="good_b_text">
										<view class="overfont-2">
											{{goods.goodsName}}
										</view>
										<view class="g_b_price mt4">
											<text class="money fz12 mr8">π<text
													class="fz16 bold">{{goods.discountPrice}}</text></text>
											<text class="textLineThrough fz12">π{{goods.price}}</text>
										</view>
										<view class="f_c_c price_sign"
											v-if="goods.amountPoints && goods.amountPoints > 0">
											<view class="bold">积分可抵{{goods.amountPoints}}元</view>
										</view>
										<view class="f_c_c price_sign"
											v-else-if="goods.legalRightIntegral && goods.legalRightIntegral > 0">
											<view class="bold">积分可抵{{goods.legalRightIntegral/100}}元</view>
										</view>
									</view>
								</view>
							</template>
						</view>
					</view>
				</LoadMoreList>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		_listGoods,
		_listGoodsType,
		_getAd,
		_listRandomGoods,
		_apiListSeckillTime,
		_apiListSeckillGoods
	} from '@/http/apiReq'
	import mixin from '@/utils/mixin/mixin.js'
	export default {
		name: 'mall',
		mixins: [mixin],
		data() {
			return {
				token: '',
				mallBannerImg: [],
				page: {
					pageIndex: 1,
					limit: 10,
					totalPage: 1,
					isLoad: false,
					isDone: false,
					list: [],
				},
				top_vip: [{
						title: '特权专区',
						url: '/pages/packageA/vip/vip',
						ico: '/static/image/mall/tequanzhuanqu@2x.png'
					},
					{
						title: '会员专区',
						url: '/pages/packageA/goodsList/goodsList?vip=0&title=会员专区',
						ico: '/static/image/mall/huiyuanzhuanqu@2x.png'
					},
					{
						title: '签到兑换',
						url: '/pages/packageA/sign/sign',
						ico: '/static/image/mall/qiandaoduihuan@2x.png'
					},
					{
						title: '限时秒杀',
						url: '/pages/packageA/senckill/senckill',
						ico: '/static/image/mall/xianshimiaosha@2x.png'
					},
					{
						title: '积分商城',
						url: '/pages/packageA/goodsList/goodsList?vip=1&title=积分商城',
						ico: '/static/image/mall/jifenzhuanqu@2x.png'
					}
				],
				active: 0,
				goodType: 'Home',
				hotGoodsList: [],
				goodTypeList: [],

				// 新增
				good_tj_list: [],
				good_tj_vip: [],
				nowTime: {
					timeId: '',
					killStatus: '',
					remainingTime: 0
				},
				timeData: {},
				good_tj_kill: [],

				isLike: true
			};
		},
		onLoad() {
			this.init();
		},
		onShow() {
			// 个性推荐
			let isLike = uni.getStorageSync('isLike');
			if (isLike !== '') {
				this.isLike = isLike;
			}
			this.token = uni.getStorageSync('Token')
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue')
		},
		methods: {
			// 初始化
			init() {
				this.getAd();
				this.getHotGoods();
				this.getGoodsListType();
				// 查询商品推荐
				this.getGoodsTj();
				// 猜你喜欢
				this.getGoodsTj_list();
			},
			// 车店banner查询
			getAd() {
				_getAd('10016004').then(res => {
					if (res && res.code == 0) {
						this.mallBannerImg = res.data
					}
				})
			},
			// 查询商品====  热销好物品
			getHotGoods() {
				_listGoods({
					hotRecommend: 0,
					page: 1,
					limit: 10
				}).then(res => {
					if (res && res.code == 0) {
						let {
							list
						} = res.data;
						this.hotGoodsList = list
					}
				})
			},
			// 查询分类
			getGoodsListType() {
				_listGoodsType({
					page: 1,
					limit: 30
				}).then(res => {
					if (res && res.code == 0) {
						let {
							list
						} = res.data;

						this.goodTypeList = [{
								typeName: '首页',
								typeId: 'Home'
							},
							...list
						];
						this.page.pageIndex = 1
					}
				})
			},
			// 查询分类下的 商品
			getGoodMainTypeList() {
				this.page.isLoad = true;
				const {
					pageIndex,
					limit
				} = this.page;
				_listGoods({
					page: pageIndex,
					limit: limit,
					hotRecommend: '1',
					goodsType: this.goodType
				}).then(res => {
					this.page.isLoad = false;
					if (res && res.code == 0) {
						let lists = res.data && res.data.list;
						this.page.totalPage = res.data.totalPage || 0;
						if (pageIndex <= 1) {
							this.page.list = lists
						} else {
							let o_list = this.page.list;
							this.page.list = [...o_list, ...lists]
						}
						if (lists.length < limit) {
							this.page.isDone = true;
						}
					}
				}).catch(() => this.page.isLoad = false)
			},
			// 
			onChangeTab(item) {
				const {
					index,
					typeId
				} = item;
				this.page.list = [];
				this.goodType = typeId;
				this.page.pageIndex = 1;
				this.active = index;

				this.getGoodMainTypeList();
			},
			// banner跳转
			bannerJumps(e) {
				this.$toRuntimeUrlIndex(this.mallBannerImg[e])
			},
			// 跳转分类列表页面
			jumpGoodsClass(item) {
				this.$navTo(`/pages/packageA/goodsList/goodsList?typeId=${item.typeId}&title=${item.typeName}`)
			},
			// 验证登录
			doNavTo(item) {
				if (this.$loginModel('/pages/mall/mall')) {
					this.$navTo(item.url)
				}
			},
			// 商品详情
			goodsInfo(goods) {
				this.$navTo(`/pages/packageA/goodDetail/goodDetail?goodsId=${goods.goodsId}&type=1`)
			},
			goodsKillInfo(goods) {
				this.$navTo(`/pages/packageA/goodDetail/goodDetail?goodsId=${goods.seckillId}&type=2`)
			},
			loadMoreHot() {
				this.$navTo('/pages/packageA/goodsList/goodsList?hotRecommend=0&title=爆款推荐')
			},
			// 
			loginNavTo(url) {
				if (this.$loginModel('/pages/mall/mall')) {
					this.$navTo(url)
				}
			},
			// 积分商城猜你喜欢
			getGoodsTj() {
				_listRandomGoods({
					page: 1,
					limit: 2
				}).then(res => {
					if (res && res.code == '0') {
						const {
							list
						} = res.data;
						this.good_tj_vip = list
					}
				})
				// 查询秒杀商品
				_apiListSeckillTime().then(res => {
					if (res && res.code == '0') {
						let kill = res.data.find(item => item.killStatus == '1');
						if (kill) {
							this.nowTime = kill
							_apiListSeckillGoods({
								page: 1,
								limit: 2,
								timeId: kill.timeId
							}).then(req => {
								if (req && req.code == '0') {
									const {
										list
									} = req.data;
									this.good_tj_kill = list
								}
							})
						}
					}
				})
			},
			// 积分商城猜你喜欢
			getGoodsTj_list() {
				_listRandomGoods({
					page: 1,
					limit: 10
				}).then(res => {
					if (res && res.code == '0') {
						const {
							list
						} = res.data;
						this.good_tj_list = list
					}
				})
			},
			// 倒计时
			onChangeTime(e) {
				this.timeData = e
			},
			//倒计时结束
			ifEndsSenkill() {
				this.getGoodsTj();
			}
		},
		/**
		 * 页面相关事件处理函数--监听用户下拉动作
		 */
		onPullDownRefresh: function() {
			this.page.pageIndex = 1
			this.init();
		},
		/**
		 * 页面上拉触底事件的处理函数
		 */
		onReachBottom: function() {
			if (this.goodType == 'Home') return
			let {
				pageIndex,
				totalPage
			} = this.page;
			if (pageIndex < totalPage) {
				this.page.pageIndex++;
				this.getGoodMainTypeList();
			} else {
				this.page.isDone = true
			}
		}
	}
</script>

<style lang="scss" scoped>
	.mall {
		min-height: 100vh;
		padding-bottom: 50rpx;

		// 倒计时样式
		.countdown {
			position: absolute;
			top: 14rpx;
			right: 0rpx;
			color: #FF2E2E;
			font-size: 24rpx;
			font-weight: bold;

			.time_box {
				background-color: #FF2E2E;
				color: #FFF;
				width: 40rpx;
				height: 40rpx;
				box-sizing: border-box;
				border-radius: 6rpx;
				margin: 0 10rpx;
			}
		}

		.top_tabs {
			padding-bottom: 10rpx;
			background-color: #fff;
		}

		.swiperHeight {
			height: 256rpx;
		}

		.mall_top {
			padding: 24rpx 26rpx 30rpx;
			font-size: 24rpx;
			color: #292929;

			.icon_title {
				margin-top: -10rpx;
			}
		}

		// 推荐商品 warp
		.tj_random_goods {
			width: 48%;
			height: 346rpx;
			padding: 68rpx 20rpx 18rpx;
			box-sizing: border-box;
			position: relative;
			color: #292929;
			background: url('~@/static/image/mall/miaosha@2x.png') no-repeat center;
			background-size: 100%;

			&.vip {
				background: url('~@/static/image/mall/jifneshasngcheng@2x.png') no-repeat center;
				background-size: 100%;
			}

			.relImage {
				position: absolute;
				width: 100%;
				left: 0;
				top: 0;
				z-index: 0
			}

			.s_l_item {
				width: 140rpx;
				height: 100%;
				position: relative;
				z-index: 11;

				.price {
					color: #E94033;
					font-weight: bold;
				}

				.price_goll {
					background-color: #E94033;
					padding: 4rpx 12rpx;
					border-radius: 22rpx;
					color: #FFF;
					font-weight: bold;
				}

				.textLineThrough {
					color: #DFDFDF;
					font-weight: normal;
				}

				.low_dow {
					padding: 4rpx 12rpx;
					background-image: linear-gradient(#FF6562, #D8312A);
					font-size: 20rpx;
					color: #FFDCA9;
					border-radius: 6rpx;
					width: 90%;
					box-sizing: border-box;
				}
			}
		}

		.main_shop_mall {
			padding: 20rpx;
			background-color: $bg-color-f5;

			// 热销好物
			.hot_goog_list {
				padding: 20rpx;
				background-image: linear-gradient(#FFEFEF, #FFDADA);
				border-radius: 12rpx;

				.g_hot_tt {
					color: #FF2E2E;
					font-size: 32rpx;
					font-weight: bold;
				}

				.moreList {
					color: #B2B2B2;
					font-size: 24rpx;
				}

				.good_hot {
					margin-top: 12rpx;

					.g_hot_list {
						width: 236rpx;
						margin-right: 16rpx;
						box-sizing: border-box;
						padding: 14rpx 12rpx;
						background-color: #FFF;
						border-radius: 10rpx;

						&:last-child {
							margin-bottom: 0;
						}

						.moneys {
							color: $font-money-color;
						}

						.textLineThrough {
							color: #DFDFDF;
						}

					}
				}
			}

			// 商品分类开始
			.goods_clasify_main {
				margin-top: 36rpx;
			}
		}
	}
</style>
