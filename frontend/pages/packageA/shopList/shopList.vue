<template>
	<view class="container">
		<!-- 自定义导航 -->
		<uniNav :title="title" bgColor="#FFF" class="dropMenu">
			<template #right>
				<view class="f_column f_c_c r_he_order"
					@click="loginNavTo('/pages/packageA/addInfo/addInfo')">
					<u-icon name="map-fill" size="22" color="#f67112"></u-icon>
					<text class="mt4">{{areaName ? areaName : '选择地区'}}</text>
				</view>
			</template>
		</uniNav>

		<!-- 主体类容 -->
		<view class="swiper_warp_main">
			<LoadMoreList :page="page" :data="page.list" icon="/static/image/empty-image/zanwuchedian@2x.png">
				<!-- 门店 -->
				<view class="warp_swiper -bgf" v-for="(item,index) in page.list" :key="item.storeId">
					<view class="f f_align_center" @click="doShopDetail(item)">
						<view class="storeHeads f_shrink0 mr12">
							<u--image radius="10" class="f_c_c f_shrink0" :src="item.doorPhotoUrl" width="74"
								height="74" />
							<view v-if="item.tags && item.tags != ''" class="tags overfont">{{item.tags.split(',')[0]}}
							</view>
						</view>
						<view class="f f_column w100 overfont">
							<view class="f_c_a">
								<view class="f f_align_center overfont">
									<text class="overfont bold fz14">{{item.storeName}}</text>
								</view>
							</view>
							<view class="overfont mt4 f f_align_center">
								<u--image src="/static/image/shop/weizhi@2x.png" width="20rpx" height="20rpx" />
								<text
									class="overfont fz12 address ml4">{{item.provinceName}}{{item.cityName}}{{item.districtName}}{{item.address}}</text>
							</view>
							<view class="shop_tag f f_warp mt4">
								<view class="shop_tag_item">{{$getDict(store_type,item.storeType)}}</view>
							</view>
						</view>
					</view>
					<!-- 售卖业务 -->
					<view v-if=" item.tmerStoreGoodsInfoEntity " class="shop_buy f_column f mt8">
						<view class="f_c_a mt4">
							<view class="f f_column f_align">
								<text>{{item.tmerStoreGoodsInfoEntity.goodsName}}</text>
								<view class="fz12 price_warp f f_align_center">
									<text class="price">π<text
											class="fz20 bold">{{(item.tmerStoreGoodsInfoEntity.userFinalPrice).toFixed(8)/1}}</text></text>
									<text
										class="ml8 textLineThrough">原价{{(item.tmerStoreGoodsInfoEntity.listingPrice).toFixed(2)}}</text>
									<text v-if="item.tmerStoreGoodsInfoEntity.arrivalAmount > 0"
										class="ml8">到店支付{{(item.tmerStoreGoodsInfoEntity.arrivalAmount).toFixed(2)}}</text>
								</view>
							</view>
							<view @click="buyNowGoods(item,item.tmerStoreGoodsInfoEntity)" class="buyNow">抢购</view>
						</view>
					</view>
				</view>
			</LoadMoreList>
		</view>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		_storeList,
		_getAd
	} from '@/http/apiReq.js'
	export default {
		name: 'shop',
		data() {
			return {
				title:'1',
				classId:'',
				
				areaName:'',
				page: {
					pageIndex: 1,
					limit: 10,
					totalPage: 1,
					isLoad: false,
					isDone: false,
					list: [],
				},
			};
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue')
		},
		computed: {
			...mapState({
				store_type: state => state.dictArrs.store_type
			})
		},
		onLoad(option) {
			if(option){
				this.classId = option.id;
				this.title = option.title;
			}
			this.init();
			// 查询码值
			this.$store.dispatch('getDictlists', 'store_type')
		},
		onShow() {
			this.areaName = uni.getStorageSync('areaName');
		},
		methods: {
			init() {
				this.storeMainList();
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
					storeType: this.classId
				}).then(res => {
					this.page.isLoad = false;
					if (res && res.code == 0) {
						let lists = res.data && res.data.list;
						this.page.totalPage = res.data.totalPage || 0;
						if (pageIndex <= 1) {
							this.page.list = lists;
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
			doShopDetail(item) {
				if (this.$loginModel('/pages/shop/shop')) {
					this.$navTo(`/pages/packageC/shopInfo/shopInfo?storeId=${item.storeId}`)
				}
			},
			// 下单
			buyNowGoods(item, goods) {
				if (this.$loginModel('/pages/shop/shop')) {
					this.$navTo(`/pages/packageC/confirm/confirm?goodsId=${goods.id}&storeId=${item.storeId}`)
				}
			},
			//
			loginNavTo(url) {
				if (this.$loginModel('/pages/packageA/shopList/shopList')) {
					this.$navTo(url)
				}
			},
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
			let {
				pageIndex,
				totalPage
			} = this.page;
			if (pageIndex < totalPage) {
				this.page.pageIndex++;
				this.storeMainList();
			} else {
				this.page.isDone = true
			}
		}
	}
</script>

<style lang="scss" scoped>
	.container {

		/deep/ .dropMenu {
			.r_he_order {
				font-size: 20rpx;
				color: #292929;
				margin-left: 18rpx;
			}

			.u-navbar,
			.-status {
				z-index: 110;
				position: relative;
			}

			.HMfilterDropdown {
				position: relative;
				overflow-y: visible;

				.sub-menu-class {
					z-index: 98;
				}
			}

		}


		.middle_ad {
			height: 300rpx;
		}

		.swiper_warp_main {
			width: 100%;
			padding: 20rpx;
			box-sizing: border-box;
			overflow-y: auto;


			.warp_swiper {
				width: 100%;
				box-sizing: border-box;
				border-radius: 10rpx;
				padding: 20rpx;
				box-shadow: 0 2px 4px rgba(0, 0, 0, .12);
				margin-bottom: 22rpx;

				.storeHeads {
					width: 74px;
					height: 74px;
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

				.address {
					color: $font-color2;
				}

				.shop_tag {
					.shop_tag_item {
						font-size: 24rpx;
						padding: 4rpx 10rpx;
						color: #D3B43A;
						background-color: #FBF6E1;
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
						background: #FF4B4B;
						padding: 10rpx 40rpx;
						border-radius: 32rpx;
						color: #FFF;
						box-shadow: 0 4rpx 8rpx rgba(#DD1717, .3);
					}
				}
			}
		}
	}
</style>
