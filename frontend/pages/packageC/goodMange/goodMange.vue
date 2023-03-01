<template>
	<view class="goodMange">
		<!-- 自定义导航 -->
		<uniNav title="商品管理" />
		<view class="-warp">
			<LoadMoreList :page="page" :data="page.list">
				<view class="goodsList f bgf mb24" v-for="item in page.list" :key="item.id">
					<u--image class="f_c_c f_shrink0 mr12"
						:src="item.goodsBannerUrlList ? item.goodsBannerUrlList[0] :''" width="80" height="80"
						radius="5" />
					<view class="w100 overfont">
						<view class="overfont">{{item.goodsName}}</view>
						<view class="mt4 f f_align_center">
							<text class="fz16 mr8 price">π{{item.userFinalPrice}}</text>
							<text class="textLineThrough fz12">π{{item.listingPrice}}</text>
						</view>
						<view class="f f_align_center mt4">
							<u-tag size="mini" type="warning" plain class="mr12" :text="$getDict(bunessTypeList,item.businessChannelType)">
							</u-tag>
							<u-tag size="mini" :type="item.goodsStatus == '1' ? 'error' : 'success'" plain :text="item.goodsStatus == '1' ? '下架' : '上架'">
							</u-tag>
						</view>
					</view>
					<view @click="handlers(item)" :class="item.goodsStatus == '1' ? 'status f_c_c f_shrink0' : 'status f_c_c f_shrink0 none' ">点击{{item.goodsStatus == '1' ? '上架' : '下架'}}</view>
				</view>
			</LoadMoreList>
		</view>
		<view class="fixed_btn">
			<u-button @click="addGoods" class="f_c_c w100 realy_buy">新增商品</u-button>
		</view>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		_getStoreInfo,
		_getGoodList,
		_updateGoods
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				page: {
					pageIndex: 1,
					limit: 10,
					totalPage: 1,
					isLoad: false,
					isDone: false,
					list: [],
				}
			};
		},
		computed: {
			...mapState({
				bunessTypeList: state => state.dictArrs['10000003']
			})
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue')
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {
			this.$store.dispatch('getSelectLists', '10000003')
		},
		onShow() {
			this.init();
		},
		methods: {
			init() {
				this.goodsList();
			},
			optionsAction(item){
				if(item.goodsStatus == '1'){
					return [{
						text: '上架',
						backgroundColor:'#f67112'
					}]
				}else{
					return [{
						text: '下架',
					}]
				}
			},
			// 上下架
			handlers(item) {
				uni.showModal({
					title: '商品上下架',
					content: `确定要${item.goodsStatus == '1' ? '上架' : '下架'} 商品：${item.goodsName}吗？`,
					confirmText: `立即${item.goodsStatus == '1' ? '上架' : '下架'}`,
					confirmColor: '#f67112',
					success: (res) =>  {
						if (res.confirm) {
							_updateGoods({
								id: item.id,
								goodsStatus: item.goodsStatus == '1' ? '0' : '1'
							}).then(res => {
								if (res && res.code == 0) {
									this.$showToast(res.msg);
									this.page.pageIndex =  1
									this.goodsList();
								}
							})
						}
					},
				})
			},
			// 查询商品详情
			goodsList() {
				const {
					pageIndex,
					limit
				} = this.page;
				this.page.isLoad = true;
				_getGoodList({
					page: pageIndex,
					limit: limit
				}).then(res => {
					this.page.isLoad = false;
					if (res && res.code == 0) {
						let lists = res.data && res.data.list;
						this.page.totalPage = res.data.totalPage || 0;
						if (pageIndex <= 1) {
							this.page.list = lists;
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
			addGoods() {
				this.$navTo('../addGoods/addGoods')
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
				this.page.pageIndex++
				this.init();
			} else {
				this.page.isDone = true
			}
		},
	}
</script>

<style lang="scss" scoped>
	.goodMange {
		min-height: 100vh;
		box-sizing: border-box;
		padding-bottom: 240rpx;

		/deep/ .goodsList {
			border-radius: 12rpx;
			padding: 12rpx;
			.status{
				color: #20cf82;
				&.none{
					color:$font-money-color2
				}
			}

			.btn_t {
				padding: 4rpx 12rpx;
				border-radius: 10rpx;
				background-color: rgba($color-tt, 0.8);
				color: #FFF;
				font-size: 20rpx;
			}

			.textLineThrough {
				color: $font-color2;
			}

			.price {
				color: $font-money-color2;
			}
		}

		.r_text_style {
			width: 130rpx;
			height: 100%;
			background-color: $color-tt;
			border-radius: 0 12rpx 12rpx 0;
			font-size: 36rpx;
			color: #FFF;

			&.isDown {
				background-color: $font-color;
			}
		}

		.fixed_btn {
			padding: 20rpx 30rpx 120rpx;
			position: fixed;
			bottom: 0;
			left: 0;
			right: 0;
			z-index: 100;

			.realy_buy {
				background-image: linear-gradient(to bottom, #fa4935, #fb8363);
				border-radius: 18rpx;
				color: #FFF;
				font-size: 32rpx;
			}
		}
	}
</style>
