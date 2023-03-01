<template>
	<view>
		<!-- 自定义导航 -->
		<uniNav :title="title" />
		<view class="container">
			<view class="good_class_list">
				<LoadMoreList :page="page" :data="page.list">
					<template #default>
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
					</template>
				</LoadMoreList>
			</view>
		</view>

	</view>
	</view>
</template>

<script>
	import {
		_listGoods
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				title: '',
				page: {
					pageIndex: 1,
					limit: 10,
					totalPage: 1,
					isLoad: false,
					isDone: false,
					list: [],
				},
				option: {

				}
			};
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue')
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {
			if (options) {
				this.option = options;

				this.title = options.title || '商品列表';
				this.init();
			}
		},
		onShow() {

		},
		methods: {
			// 
			init() {
				this.getGoodMainTypeList();
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
					...this.option
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
			// 商品详情
			goodsInfo(goods) {
				this.$navTo(`/pages/packageA/goodDetail/goodDetail?goodsId=${goods.goodsId}&type=1`)
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
				this.getGoodMainTypeList();
			} else {
				this.page.isDone = true
			}
		},
	}
</script>

<style lang="scss" scoped>
	.container {
		min-height: 100vh;
		box-sizing: border-box;
		padding: 30rpx;

		/deep/ .good_class_list {
			min-height: 600rpx;
			flex-direction: row;
			padding-top: 0;
		}

	}
</style>
