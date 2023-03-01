<template>
	<view class="orderMain">
		<!-- 自定义导航 -->
		<uniNav title="门店订单">
			<view class="-bgf">
				<u-tabs :list="tabs" @click="onChangeTab"></u-tabs>
			</view>
		</uniNav>

		<LoadMoreList :page="page" :data="page.list">
			<view class="orderMain">
				<view class="order -warp30">
					<view class="mb16 orderWarp -bgf" v-for="item in page.list" :key="item.orderId">
						<view class="f_c_a">
							<view class="f f_align_center overfont">
								<i class="icon-mendian iconfont"></i>
								<text class="overfont fz16 ml8">{{item.buyStore.storeName}}</text>
							</view>
							<template v-if="item.isUse == '1' && item.payStatusType == '10007003'">
								<view class="status">待使用</view>
							</template>
							<template v-else>
								<view v-if="item.payStatusType == '10007001' || item.payStatusType == '10007003' "
									class="status">{{$getDict(pay_status_type,item.payStatusType)}}</view>
								<view v-else class="tsize">{{$getDict(pay_status_type,item.payStatusType)}}</view>
							</template>
						</view>
						<view class="f f_align mt12" @click="doOrderDetail(item)">
							<u--image :src="item.goodsInfo.goodsBannerUrlList ? item.goodsInfo.goodsBannerUrlList[0] : ''" radius="8" width="80" height="80" />
							<view class="f f_column f_align w100 ml12">
								<view class="f f_align_start f_align">
									<view class="overfont-2">{{item.goodsName}}</view>
								</view>
								<view style="width: 440rpx;" class="mt8 fz12 tsize overfont">地址：{{item.buyStore.address}}</view>
								<view class="f f_align_center mt8">
									<text class="price mr12">π{{(item.userFinalPrice).toFixed(8)/1}}</text>
									<text
										class="fz12 textLineThrough tsize">门店价：{{(item.listingPrice).toFixed(8)/1}}</text>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</LoadMoreList>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		_userOrderList
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				goodsOrderStatus: '',
				page: {
					pageIndex: 1,
					limit: 10,
					totalPage: 1,
					isLoad: false,
					isDone: false,
					list: [],
				},
				tabs: [{
						name: '全部',
						val: ''
					},
					{
						name: '待付款',
						val: '10007001'
					},
					{
						name: '待核销',
						val: 'isUse'
					},
					{
						name: '已完成',
						val: '10007003'
					},
					{
						name: '退款',
						val: '10007006'
					},
					{
						name: '已取消',
						val: '10007005'
					},
				]
			};
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue')
		},
		computed: {
			...mapState({
				pay_status_type: state => state.dictArrs.pay_status_type
			})
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {
			this.$store.dispatch('getDictlists', 'pay_status_type')
		},
		onShow() {
			this.init();
		},
		methods: {
			init() {
				this.getDataRowListAxios();
			},
			// 查询列表数据
			getDataRowListAxios() {
				this.page.isLoad = true
				const {
					pageIndex,
					limit
				} = this.page;
				_userOrderList({
					page: pageIndex,
					limit: limit,
					isUse: this.goodsOrderStatus == 'isUse' ? '1' : '',
					payStatusType: this.goodsOrderStatus == 'isUse' ? '10007003' : this.goodsOrderStatus
				}).then(res => {
					this.page.isLoad = false
					if (res && res.code == 0) {
						let lists = res.data && res.data.list;
						this.page.totalPage = res.data.totalPage || 0;
						if (pageIndex <= 1) {
							this.page.list = lists
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
			onChangeTab({
				val
			}) {
				this.goodsOrderStatus = val
				this.page.pageIndex = 1
				this.getDataRowListAxios();
			},
			// 订单详情
			doOrderDetail(item) {
				this.$navTo(`../storeOrderDetail/storeOrderDetail?orderId=${item.id}`)
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
				this.page.pageIndex++;
				this.init();
			} else {
				this.page.isDone = true
			}
		},
	}
</script>

<style lang="scss">
	.orderMain {
		.order {
			padding: 30rpx;

			.orderWarp {
				padding: 20rpx;
				border-radius: 10rpx;
			}

			.tsize {
				color: $font-color2;
			}

			.price {
				color: $font-money-color2;
			}

			.status {
				color: #20cf82;
			}

			.payContniue {
				width: 200rpx;
				font-size: 24rpx;
				background-image: linear-gradient(to bottom, #fa4935, #fb8363);
				border-radius: 10rpx;
				color: #FFF;
				font-size: 32rpx;
				margin: 0;
			}
		}
	}
</style>
