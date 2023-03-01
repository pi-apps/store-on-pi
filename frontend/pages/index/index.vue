<template>
	<view class="container">
		<!-- 自定义导航 -->
		<view class="top_uni_nav f_c_a sticky0">
			<view class="fz18">{{$t('app.name')}}</view>
			<view class="f_c_a">
				<u--image @click="showLangChange" class="f_c_c" src="/static/image/home/lang.png" width="20px"
					height="20px" />
				<u--image @click="loginNavTo('/pages/packageA/addInfo/addInfo')" class="f_c_c ml20"
					src="/static/image/home/wz.png" width="20px" height="20px" />
			</view>
		</view>

		<view class="top_warps">
			<u-swiper imgMode="scaleToFill" indicator indicatorActiveColor="#f67112" :list="bannerImg"
				@click="bannerJumps($event,'index')" bgColor="none" keyName="adImgUrl" circular height="140"></u-swiper>
		</view>

		<!-- 首页 icon -->
		<view class="iconWarp" v-if="indexIcon.length && indexIcon.length >0">
			<swiper class="w100" :current="activeIco" @change="indexIcoChange" style="height: 320rpx;">
				<swiper-item v-for="(icons,index) in indexIcon" :key="index">
					<view class="f f_warp ">
						<view class="i_w_item f f_column f_c_c" v-for="(item,ind) in icons" :key="ind"
							@click="jumpAdBannerUrlAuth(item)" :style="{width:item.width}">
							<u--image width="40" height="40" :src="item.url" />
							<text class="overfont mt4" style="width: 90%;text-align: center;">{{item.title}}</text>
							<view class="isNew" v-if="item.isnew && item.isnew != ''">{{item.isnew}}</view>
						</view>
					</view>
				</swiper-item>
			</swiper>
			<!-- 底部自定义样式 bar -->
			<view class="f_c_c">
				<view :class="'bars '+(activeIco == index ? 'active' : '')" v-for="(item,index) in indexIcon.length"
					:key="index">
				</view>
			</view>
		</view>
		<!--  -->
		<view class="__warp">
			<!-- 中间广告展示 -->
			<u-swiper imgMode="scaleToFill" v-if="midBannerImg.length && midBannerImg.length > 0" :list="midBannerImg"
				@click="bannerJumps($event,'middle')" bgColor="none" keyName="adImgUrl" height="82"></u-swiper>
			<!-- 新人必读 -->
			<view class="f_c_a mt12">
				<u--image v-for="(item,index) in bottomImgIco" :key="index" :src="item.adImgUrl" width="168rpx"
					height="124rpx" @click="jumpIndexs(item)" />
			</view>
			<view class="swiperline mt12">
				<view class="swiper_warp_main shop_main">
					<LoadMoreList noMoreText="附近暂无服务门店..." :page="page" :data="page.list">
						<view class="warp_swiper -bgf" v-for="(item,index) in page.list" :key="item.storeId">
							<view class="f f_align_center" @click="doShopDetail(item)">
								<view class="storeHeads f_shrink0 mr20">
									<u--image radius="10" class="f_c_c f_shrink0" :src="item.doorPhotoUrl" width="80"
										height="80" />
									<view v-if="item.tags && item.tags != ''" class="tags overfont">
										{{item.tags.split(',')[0]}}
									</view>
								</view>
								<view class="f f_column f_align w100 overfont">
									<view class="f_c_a">
										<text class="overfont bold fz14">{{item.storeName}}</text>
									</view>
									<text
										class="overfont fz12 address">{{item.provinceName}}{{item.cityName}}{{item.districtName}}{{item.address}}</text>
									<view class="shop_tag f f_warp mt4">
										<view class="shop_tag_item">{{$getDict(store_type,item.storeType)}}</view>
									</view>
								</view>
							</view>
							<!-- 售卖业务 -->
							<view v-if="item.tmerStoreGoodsInfoEntity" class="shop_buy f_column f mt8">
								<text>{{item.tmerStoreGoodsInfoEntity.goodsName}}</text>
								<view class="f_c_a mt4">
									<view class="fz12 price_warp f f_align_center">
										<text class="price">π<text
												class="fz20 bold">{{(item.tmerStoreGoodsInfoEntity.userFinalPrice).toFixed(8)/1}}</text></text>
										<text class="ml8 textLineThrough">{{$t('pages.shop.price')}}
											{{(item.tmerStoreGoodsInfoEntity.listingPrice).toFixed(2)}}</text>
										<text v-if="item.tmerStoreGoodsInfoEntity.arrivalAmount > 0"
											class="ml8">{{$t('pages.shop.shopPrice')}}{{(item.tmerStoreGoodsInfoEntity.arrivalAmount).toFixed(2)}}</text>
									</view>
									<view @click="buyNowGoods(item,item.tmerStoreGoodsInfoEntity)" class="buyNow">
										{{$t('pages.shop.buy')}}
									</view>
								</view>
							</view>
						</view>
					</LoadMoreList>
				</view>
			</view>
		</view>
		<!--  -->
		<!-- <popups ref="indexPops" /> -->
		<!-- 语言切换 -->
		<u-action-sheet @select="selectLangs" :round="10" :actions="langList" :title="$t('lang.title')" :show="showLang"
			:cancelText="$t('all.cancel')" @close="showLang = false"></u-action-sheet>
	</view>
</template>

<script>
	import {
		_getAd,
		_getHomePage,
		_storeList,
		saveUserLang
	} from '@/http/apiReq.js'
	import mixin from '@/utils/mixin/mixin.js'
	import {
		version
	} from '@/utils/index.js'
	import {
		mapState
	} from 'vuex';
	export default {
		name: 'index',
		mixins: [mixin],
		data() {
			return {
				activeIco: 0,
				page: {
					pageIndex: 1,
					limit: 10,
					totalPage: 1,
					isLoad: false,
					isDone: false,
					list: [],
				},

				latitude: '',
				longitude: '',

				// 顶部banner图片
				bannerImg: [],
				// 中间banner图片
				midBannerImg: [],
				// 
				bottomImgIco: [],

				indexIcon: [],

				showLang: false,
				langList: [{
						name: 'English',
						lang: 'en',
						api: 'english'
					},
					{
						name: '简体中文',
						lang: 'zh-Hans',
						api: 'chinese_simplified'
					},
					{
						name: '繁體中文',
						lang: 'zh-Hant',
						api: 'chinese_traditional'
					},
					{
						name: '한국인',
						lang: 'ko',
						api: 'korean'
					}
				]
			}
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue'),
			'popups': () => import('@/components/popups/popups.vue')
		},
		computed: {
			...mapState({
				store_type: state => state.dictArrs.store_type
			})
		},
		onLoad() {
			this.init();
			// 查询码值
			this.$store.dispatch('getDictlists', 'store_type')
		},
		onShow() {
			this.getHomePage();
			// 
			this.storeMainList();
			//
			// this.$nextTick(() => {
			// 	if (this.$refs.indexPops) {
			// 		this.$refs.indexPops.updatePops();
			// 	}
			// });
		},
		methods: {
			init() {
				this.getAd();
			},
			// 查询门店列表信息
			storeMainList() {
				this.page.isLoad = true;
				const {
					pageIndex,
					limit
				} = this.page;
				_storeList({
					page: pageIndex,
					limit: limit,
				}).then(res => {
					this.page.isLoad = false;
					if (res && res.code == 0) {
						let lists = res.data && res.data.list
						this.page.list = lists;
						this.page.totalPage = res.data.totalPage;
						if (lists.length < limit) {
							this.page.isDone = false;
						}
					}
				}).catch(() => this.page.isLoad = false)
			},
			// 首页banner查询
			getAd() {
				_getAd('10016001').then(res => {
					if (res && res.code == 0) {
						this.bannerImg = res.data
					}
				})

				setTimeout(() => {
					_getAd('10016002').then(res => {
						if (res && res.code == 0) {
							this.midBannerImg = res.data
						}
					})
				}, 200)

				setTimeout(() => {
					_getAd('10016006').then(res => {
						if (res && res.code == 0) {
							this.bottomImgIco = res.data
						}
					})
				}, 400)
			},
			// 查询首页图标
			getHomePage() {
				_getHomePage(this.$sys().platform == 'ios' ? '1' : '0', version).then(res => {
					if (res && res.code == 0) {
						let row = res.data;
						let width = res.data[0].width;
						let numIco = Math.ceil(100 / parseInt(width)) * 2;
						let len = Math.ceil(row.length / numIco);
						let rowList = [];
						for (let i = 0; i < len; i++) {
							rowList[i] = row.slice(i * numIco, (i + 1) * numIco)
						};
						this.indexIcon = rowList
					}
				})
			},
			// banner跳转
			bannerJumps(e, key) {
				if (key == 'index') {
					if (this.bannerImg[e].adUrlType != '10017001') {
						if (this.$loginModel('/pages/index/index', () => {
								this.$getUserInfoSync(false, false);
							})) {
							this.$toRuntimeUrlIndex(this.bannerImg[e]);
						}
					}
				} else {
					if (this.midBannerImg[e].adUrlType != '10017001') {
						if (this.$loginModel('/pages/index/index', () => {
								this.$getUserInfoSync(false, false);
							})) {
							this.$toRuntimeUrlIndex(this.midBannerImg[e]);
						}
					}
				}
			},
			// icon 跳转
			jumpAdBannerUrlAuth(item) {
				if (item.isLogin == '0') {
					if (this.$loginModel('/pages/index/index', () => {
							this.$getUserInfoSync(false, false);
						})) {
						this.$toRuntimeUrlIndex(item);
					}
				} else {
					this.$toRuntimeUrlIndex(item);
				}
			},
			// 商家详情信息
			doShopDetail(item) {
				if (this.$loginModel('/pages/index/index', () => {
						this.$getUserInfoSync(false, false);
					})) {
					this.$navTo(`/pages/packageC/shopInfo/shopInfo?storeId=${item.storeId}`)
				}
			},
			// 下单
			buyNowGoods(item, goods) {
				if (this.$loginModel('/pages/shop/shop', () => {
						this.$getUserInfoSync(false, false);
					})) {
					this.$navTo(`/pages/packageC/confirm/confirm?goodsId=${goods.id}&storeId=${item.storeId}`)
				}
			},
			// 
			loginNavTo(url) {
				if (this.$loginModel('/pages/index/index', () => {
						this.$getUserInfoSync(false, false);
					})) {
					this.$navTo(url)
				}
			},
			indexIcoChange(e) {
				this.activeIco = e.detail.current
			},

			jumpIndexs(item) {
				if (item.adUrlType != '10017001') {
					if (this.$loginModel('/pages/index/index', () => {
							this.$getUserInfoSync(false, false);
						})) {
						this.$toRuntimeUrlIndex(item);
					}
				}
			},
			showLangChange() {
				if (this.$loginModel('/pages/index/index', () => {
						this.$getUserInfoSync(false, false);
					})) {
					this.showLang = true
				}
			},
			// 切换语言
			selectLangs({
				api,
				lang
			}) {
				uni.setLocale(lang)
				this.$i18n.locale = lang;
				saveUserLang(api).then(res => {
					if (res && res.code == 0) {
						this.getHomePage();
						// 
						this.storeMainList();
						// 
						this.init();
					}
				})
			}
		},
		onPullDownRefresh() {
			this.init();
			// 
			this.getHomePage();
			// 
			this.storeMainList();
		}
	}
</script>

<style lang="scss" scoped>
	.container {
		min-height: 100vh;
		padding-bottom: 120rpx;
		box-sizing: border-box;

		.top_uni_nav {
			background-color: #f67112;
			padding: 20rpx;
			color: #FFF;
		}

		.top_warps {
			height: 310rpx;
			background: url('@/static/image/home/homePage.png') no-repeat center center;
			background-size: 100% 100%;
			position: relative;
			padding: 0 20rpx;
			box-sizing: border-box;

			.u-swiper {
				top: 20rpx;
			}
		}

		.middle_ad {
			height: 140rpx;
			width: 100%;
		}

		.__warp {
			box-sizing: border-box;
			padding: 20rpx 20rpx 0;
			background-color: #FAFAFA;
		}

		.iconWarp {
			box-sizing: border-box;
			padding: 20rpx 0 0;
			font-size: 24rpx;

			.i_w_item {
				width: 20%;
				padding: 20rpx 0;
				box-sizing: border-box;
				position: relative;

				.isNew {
					position: absolute;
					padding: 2rpx 10rpx;
					font-size: 20rpx;
					background-color: rgba($font-money-color2, 0.6);
					border-radius: 14rpx 0 14rpx 0;
					right: -10rpx;
					top: -10rpx;
					color: #FFF;
				}
			}

			.bars {
				width: 8rpx;
				height: 8rpx;
				border-radius: 50%;
				background-color: #D8D8D8;
				transition: width 0.2s;

				&.active {
					width: 28rpx;
					height: 8rpx;
					border-radius: 5rpx;
					background-image: linear-gradient(to right, #FCA281, #F7694A);
				}
			}
		}

		.swiperline {
			height: 800rpx;

			.swiper_warp_main {
				width: 100%;
				padding: 5rpx;
				box-sizing: border-box;
				overflow-y: auto;

				.warp_swiper {
					width: 100%;
					box-sizing: border-box;
					border-radius: 10rpx;
					padding: 20rpx;
					box-shadow: 0 2px 4px rgba(0, 0, 0, .12);
					margin-bottom: 22rpx;
				}

				// 门店
				&.shop_main {
					.storeHeads {
						width: 80px;
						height: 80px;
						position: relative;

						.tags {
							width: 100%;
							position: absolute;
							bottom: 0;
							left: 0;
							text-align: center;
							font-size: 20rpx;
							color: #FFF;
							background-color: rgba($font-money-color, 0.7);
							border-radius: 0 0 10rpx 10rpx;
						}
					}

					.diant {
						color: $font-money-color;
					}

					.shop_midle {
						font-size: 24rpx;

						.star {
							color: $color-success
						}

						.middle {
							color: $font-color2;
							margin: 0 12rpx;
						}
					}

					.address {
						color: $font-color2;
					}

					.shop_tag {
						.shop_tag_item {
							font-size: 24rpx;
							padding: 4rpx 10rpx;
							color: $font-color;
							background-color: rgba($bg-color-f5, 0.7);
							border-radius: 8rpx;
							margin-right: 12rpx;
						}
					}

					.shop_buy {
						padding: 12rpx;
						border-radius: 20rpx;
						background-color: rgba($bg-color-fa, 0.8);

						.price_warp {
							color: $font-color2;

							.price {
								color: $font-money-color;
							}
						}

						.buyNow {
							background-image: linear-gradient(to bottom, #fa442b, #fb8362);
							padding: 10rpx 30rpx;
							border-radius: 10rpx;
							color: #FFF;
						}
					}
				}
			}

			// 
			.swiper-shop-article {
				padding: 20rpx;

				.lable {
					color: #999;
				}
			}
		}
	}
</style>
