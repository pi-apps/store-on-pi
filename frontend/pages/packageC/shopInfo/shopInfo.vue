<template>
	<view class="shopInfo">
		<!-- 顶部透明导航 -->
		<nav-opacity :showBack="true" ref="opacityNav" title="门店详情" />

		<view class="main">
			<u--image :src="storeInfo.doorPhotoUrl" width="100%" height="200px" />
			<!-- title -->
			<view class="storeInfos -warp30 -bgf">
				<view class="overfont fz18 mb8">{{storeInfo.storeName}}</view>
				<view class="f_c_a mt8">
					<text>营业时间：{{storeInfo.openTimeStart}}-{{storeInfo.openTimeEnd}}</text>
					<a :href="'tel:'+storeInfo.mobile"><u-icon @click="" size="30" name="phone" color="#fbd624" /></a>
				</view>
				<text class="overfont-2 fz10">{{storeInfo.provinceName}}{{storeInfo.cityName}}{{storeInfo.districtName}}{{storeInfo.address}}</text>
			</view>
			
			<!-- 商品 -->
			<view class="store_classIfy f">
				<scroll-view :scroll-y="true" class="store_classIfy_left">
					<view 
						v-for="(item,index) in classIfy" :key="index"
						:class="'classIfy_left f_c_c fz12 '+(isClassify.id == item.id ? 'active' : '')"
						@click="saveClasify(item)"
					>
						{{item.serviceName}}
					</view>
				</scroll-view>
				
				<view class="goodsWarps">
					<LoadMoreList :page="page" :data="page.list">
						<view class="store_goods -bgf -warp30 f f_align_center" v-for="item in page.list" :key="item.id">
							<u-icon v-if="goodItem.id == item.id" class="left_ico mr12" size="20" color="#e71c19"
								name="checkmark-circle-fill" />
							<u-icon v-else @click="chooseGoods(item)" class="left_ico mr12" size="20" color="#999"
								name="checkmark-circle" />
							<u-image :src="(item.goodsBannerUrlList && item.goodsBannerUrlList.length && item.goodsBannerUrlList.length > 0) ? item.goodsBannerUrlList[0] : ''" width="80" height="80" radius="9" />
							<view class="f f_column ml12 goodPrice w100" @click="jumpGoodsDetail(item)">
								<view class="f_c_a overfont">
									<text class="overfont-2 fz16">{{item.goodsName}}</text>
									<u-icon name="arrow-right" size="16" />
								</view>
								<view class="f f_align_end mt12">
									<text class="fz20 price"><text class="fz12">π</text> {{item.userFinalPrice}}</text>
									<text class="fz12 ml12 textLineThrough"><text>门店价:</text> {{item.listingPrice}}</text>
								</view>
							</view>
						</view>
					</LoadMoreList>
				</view>
			</view>

			<!-- 立即购买 -->
			<view class="buynow -bgf w100 f_c_a">
				<view class="f f_column">
					<text class="fz22 mb4 -price">
						<text class="fz12">π</text>
						<text v-if="goodItem.userFinalPrice" style="color: #e71c19;" class="bold ml4">{{goodItem.userFinalPrice.toFixed(8)/1}}</text>
						<text v-else style="color: #e71c19;" class="bold ml4">0.00</text>
					</text>
				</view>
				<view class="buyNowBtn" @click="buyNowGoods">立即购买</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		_getStoreInfoById,
		_storeClassList,
		_getStoreGoodsList
	} from '@/http/apiReq'
	import {
		mapState
	} from 'vuex';
	export default {
		data() {
			return {
				storeId: '',
				storeInfo: {
					doorPhotoUrl:''
				},
				
				
				page: {
					pageIndex: 1,
					limit: 10,
					totalPage: 1,
					isLoad: false,
					isDone: false,
					list: [],
				},
				
				classIfy:[],
				isClassify:{
					id:'',
					serviceName:''
				},
				
				
				goodItem: {
					id: '',
					userFinalPrice: 0
				},
				goodsVipPackages: []
			};
		},
		components: {
			'nav-opacity': () => import('@/components/nav-opacity/nav-opacity.vue'),
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue')
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {
			if (options && options.storeId) {
				this.storeId = options.storeId
				this.init();
			}
		},
		onShow() {
			
		},
		methods: {
			// 
			init() {
				this.getStoreInfo();
				this.getClassIfyList();
			},
			// 查询门店详情信息
			getStoreInfo() {
				_getStoreInfoById(this.storeId).then(res => {
					if (res && res.code == 0) {
						this.storeInfo = res.data
					}
				})
			},
			// 查询门店下的商品列表
			getStoreNextGoods() {
				this.page.isLoad = true
				const {
					pageIndex,
					limit
				} = this.page;
				_getStoreGoodsList({
					page: pageIndex,
					limit: limit,
					storeId: this.storeId,
					goodsServiceId:this.isClassify.id
				}).then(res => {
					this.page.isLoad = false
					if (res && res.code == 0) {
						let lists = res.data && res.data.list;
						this.page.totalPage = res.data.totalPage || 0
						if (pageIndex <= 1) {
							this.page.list = lists;
							this.goodItem = lists.length && lists.length > 0 ? lists[0] : {}
						} else {
							let o_list = this.page.list;
							this.page.list = [...o_list, ...lists];
						}
						if (lists.length < limit) {
							this.page.isDone = true
						}
					}
				}).catch(() => this.page.isLoad = false)
			},
			// 
			callphone() {
				const {
					mobile
				} = this.storeInfo;
				if (mobile && mobile != '') {
					this.$callPhone(mobile);
				} else {
					this.$showTaost('该门店暂无电话～')
				}
			},
			// x选择商品
			chooseGoods(item) {
				this.goodItem = item
			},
			//商品详情
			jumpGoodsDetail(item) {
				this.$navTo(`../shopGoods/shopGoods?goodsId=${item.id}&storeId=${this.storeId}`)
			},
			// 下单
			buyNowGoods() {
				const {
					id
				} = this.goodItem;
				if (!id) {
					return this.$showToast('请先选择您要购买的商品～')
				}
				if (id == '') {
					return this.$showToast('请先选择您要购买的商品～')
				}
				this.$navTo(`../confirm/confirm?goodsId=${id}&storeId=${this.storeId}`)
			},
			getClassIfyList(){
				_storeClassList(this.storeId).then(res=>{
					if(res && res.code == 0){
						const { list } = res.data;
						this.classIfy = list;
						if(list.length && list.length > 0){
							this.isClassify = list[0];
							this.getStoreNextGoods();
						}
					}
				})
			},
			saveClasify(item){
				this.isClassify = item
				this.getStoreNextGoods()
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
			let {
				pageIndex,
				totalPage
			} = this.page;
			if (pageIndex < totalPage) {
				this.page.pageIndex++
				this.getStoreNextGoods();
			} else {
				this.page.isDone = true
			}
		},
		/**
		 * scorl
		 * **/
		onPageScroll(e) {
			// 透明导航
			this.$nextTick(() => {
				this.$refs.opacityNav.navScorllPage(e);
			})
		},
	}
</script>

<style lang="scss" scoped>
	.shopInfo {
		min-height: 100vh;
		padding-bottom: 140rpx;
		box-sizing: border-box;

		.storeInfos {
			position: sticky;
			padding: 30rpx;
			border-radius:20rpx 20rpx 0 0;
			margin-top: -50rpx;
			z-index: 10;
			top:80rpx
		}

		.store_goods {
			border-bottom: 1rpx solid rgba(#999, 0.2);

			&:last-child {
				border-bottom: none;
			}

			.goodPrice {
				.price {
					color: $font-money-color2;
				}

				.textLineThrough {
					color: $font-color2;
				}
			}
		}
		
		.store_classIfy{
			height: calc(100vh - 230px);
			position: sticky;
			top: 157px;
			.store_classIfy_left{
				width: 140rpx;
				padding: 10rpx 0;
				background-color: #f1f1f1;
				box-sizing: border-box;
				.classIfy_left{
					padding: 15rpx 0;
					&.active{
						background-color: #fff;
						border-radius: 10rpx;
					}
				}
			}
			.goodsWarps{
				flex: 1;	
			}
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
	}
</style>
