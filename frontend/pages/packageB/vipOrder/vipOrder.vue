<template>
	<view class="orderMain">
		<!-- 自定义导航 -->
		<uniNav title="特权订单">
			<view class="order -bgf">
				<view :class="'tab_magin f_c_a active'+goodsChannelType">
					<view @click="onChangeTabType('0')"
						:class="'f_c_c t_pane '+(goodsChannelType == '0' ? 'active' : '')">
						商品特权</view>
					<view @click="onChangeTabType('1')"
						:class="'f_c_c t_pane '+(goodsChannelType == '1' ? 'active' : '')">
						门店特权</view>
					<view @click="onChangeTabType('3')"
						:class="'f_c_c t_pane '+(goodsChannelType == '3' ? 'active' : '')">
						超值权益包</view>
				</view>
				<view class="-bgf">
					<u-tabs :list="tabs" @click="onChangeTab"></u-tabs>
				</view>
			</view>
		</uniNav>

		<LoadMoreList :page="page" :data="page.list">
			<view class="order">
				<view class="mb12 order_warp -bgf" v-for="item in page.list" :key="item.orderId">
					<view class="f_c_a">
						<view class="f f_align_center overfont">
							<u--image class="f_c_c" src="/static/image/mall/store-icon.png" width="24" height="24" />
							<text class="overfont fz16 ml8">PI商店自营店</text>
						</view>
						<text v-if="item.goodsOrderStatus == '10007001' || item.goodsOrderStatus == '10007003' "
							class="status">{{$getDict(pay_status_type,item.goodsOrderStatus)}}</text>
						<text v-else class="tsize">{{$getDict(pay_status_type,item.goodsOrderStatus)}}</text>
					</view>
					<view class="f f_align mt12" @click="doOrderDetail(item)">
						<u--image :src="item.goodsInfo.goodsImgList? item.goodsInfo.goodsImgList[0] :''" radius="8"
							width="80" height="80" />
						<view class="f f_column f_align w100 ml12">
							<view class="f f_align_start f_align">
								<view class="overfont-2">{{item.goodsInfo.goodsName}}</view>
								<view class="f f_column f_align_end ml12">
									<text class="price">π{{item.payPrice}}</text>
									<text class="tsize">x{{item.goodsNum}}</text>
								</view>
							</view>
							<view class="mt12 tsize">
								规格：{{(item.attributeName && item.attributeName.length) ? item.attributeName.join('/') : '无'}}
							</view>
							<view v-if="item.goodsOrderStatus == '10007003'" class="fz12 mt8 tsize">
								{{$getDict(express_status,item.goodsExpressStatus)}}
							</view>
							<view class="f_c_a mt12">
								<text class="fz12 tsize">共{{item.goodsNum}}件商品</text>
								<text class="bold">订单金额 π{{item.orderPayPrice}}</text>
							</view>
						</view>
					</view>
					<!--  -->
					<view class="f_c_e mt20">
						<u-button
							v-if="item.goodsOrderStatus == '10007001' && item.remainingTime && item.remainingTime > 0"
							@click="payContinue(item)" class="payContniue f_c_c">继续支付</u-button>
						<u-button v-if="item.goodsOrderStatus == '10007003' && item.goodsExpressStatus=='10012002'"
							@click="confirmGoods(item)" class="payContniue f_c_c">确认收货</u-button>
					</view>
				</view>
			</view>
		</LoadMoreList>
		<!-- 确认收货 -->
		<u-action-sheet description="请确定货物是否已经收到～" round="12" :actions="list" title="确认收货" cancelText="取消" :show="show"
			@select="confirmGoodsAxios" @close="show = false"></u-action-sheet>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		_vipOrderList,
		_confirmGoodsVip
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				goodsChannelType: '0',
				goodsOrderStatus: '',
				show: false,
				orderId: '',
				list: [{
					name: '确认收货'
				}],
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
						name: '已付款',
						val: '10007003'
					},
					{
						name: '已发货',
						val: '10012002'
					},
					{
						name: '已签收',
						val: '10012003'
					},
					{
						name: '退款',
						val: '10007006'
					},
					{
						name: '已取消',
						val: '10007005'
					},
				],
				latitude: '',
				longitude: ''
			};
		},
		computed: {
			...mapState({
				express_status: state => state.dictArrs.express_status,
				pay_status_type: state => state.dictArrs.pay_status_type
			})
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue')
		},
		mounted() {
			// h5原生方法调用回调
			const _this = this;
			//#ifdef H5  
			// //监听获取定位是否成功
			window['appbackJSgetLocation'] = (code) => {
				let res = JSON.parse(code);
				_this.latitude = res.latitude;
				_this.longitude = res.longitude;

				_this.getDataRowListAxios()
			}
			// #endif
		},
		onLoad(options) {
			this.$store.dispatch('getDictlists', 'express_status,pay_status_type')
		},
		onShow() {
			this.init();
		},
		methods: {
			init() {
				// 调试
				//this.getDataRowListAxios();
				//h5获取定位
				this.$getLocation();
			},
			// 查询列表数据
			getDataRowListAxios() {
				this.page.isLoad = true
				const {
					pageIndex,
					limit
				} = this.page;
				let status = this.goodsOrderStatus;
				let goodsExpressStatus = '';
				if (this.goodsOrderStatus == '10012002' || this.goodsOrderStatus == '10012003') {
					goodsExpressStatus = this.goodsOrderStatus;
					status = '10007003';
				}
				_vipOrderList({
					page: pageIndex,
					limit: limit,
					goodsChannelType: this.goodsChannelType,
					goodsOrderStatus: status,
					goodsExpressStatus: goodsExpressStatus,
					latitude: this.latitude,
					longitude: this.longitude,
					// 联调测试
					// latitude: '30.68144',
					// longitude: '103.8559'
				}).then(res => {
					this.page.isLoad = false
					if (res && res.code == 0) {
						let lists = res.data && res.data.list;
						this.page.totalPage = res.data.totalPage || 0
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
			// 
			onChangeTab({
				val
			}) {
				this.goodsOrderStatus = val;
				this.page.pageIndex = 1;
				this.getDataRowListAxios();
			},
			onChangeTabType(val) {
				this.goodsChannelType = val;
				this.page.pageIndex = 1;
				this.getDataRowListAxios();
			},
			// 
			payContinue(item) {
				const {
					payChannel,
					payText,
					orderId,
					orderPayPrice,goodsChannelType
				} = item;
				if (payChannel) {
					// 继续支付处理
					this.$wxstrPay(payChannel,payText,orderId,orderPayPrice);
				} else {
					this.$navTo(
						`/pages/packageA/pay/pay?orderId=${orderId}&amount=${orderPayPrice}&goodsType=${goodsChannelType == '0' ? '3' : '10002010'}`
					)
				}
			},
			// 订单详情
			doOrderDetail(item) {
				this.$navTo(`../vipOrderDetail/vipOrderDetail?orderId=${item.orderId}`)
			},
			// 确认收货
			confirmGoods(item) {
				this.orderId = item.orderId;
				this.show = true;
			},
			confirmGoodsAxios() {
				_confirmGoodsVip(this.orderId).then(res => {
					this.show = false;
					if (res && res.code == 0) {
						this.$showToast('确认收货成功～');
						this.init();
					}
				})
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

<style lang="scss" scoped>
	.orderMain {
		min-height: 100vh;
		background-color: #f5f5f5;
	}
	.order {
		padding: 20rpx 26rpx 0;
	
		.tab_magin {
			width: 100%;
			height: 100rpx;
			background-image: url('@/static/image/main/0@2x.png');
			background-repeat: no-repeat;
			background-position: center;
			background-size: 100% 100%;
	
			&.active0 {
				background-image: url('@/static/image/main/0@2x.png');
				background-repeat: no-repeat;
				background-position: center;
				background-size: 100% 100%;
			}
			&.active1 {
				background-image: url('@/static/image/main/1@2x.png');
				background-repeat: no-repeat;
				background-position: center;
				background-size: 100% 100%;
			}
			&.active3 {
				background-image: url('@/static/image/main/3@2x.png');
				background-repeat: no-repeat;
				background-position: center;
				background-size: 100% 100%;
			}
	
			.t_pane {
				width: 50%;
				height: 100rpx;
				font-size: 32rpx;
				color: #808080;
	
				&.active {
					font-weight: bold;
					color: #F7694A;
				}
			}
		}
	
		.order_warp {
			padding: 20rpx 30rpx;
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
</style>
