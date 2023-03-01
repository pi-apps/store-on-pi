<template>
	<view class="orderMain">
		<!-- 自定义导航 -->
		<uniNav title="商城订单">
			<view class="-bgf">
				<u-tabs :list="tabs" @click="onChangeTab"></u-tabs>
			</view>
		</uniNav>

		
		<LoadMoreList :page="page" :data="page.list">
			<view class="order -warp30 -bgf">
				<view class="mb24" v-for="item in page.list" :key="item.orderId">
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
									<text class="price">π{{(item.unitPrice).toFixed(2)}}</text>
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
								<text class="bold">订单金额 π{{(item.orderPayPrice).toFixed(2)}}</text>
							</view>
						</view>
					</view>
					<!--  -->
					<view class="f_c_e mt20">
						<u-button v-if="item.goodsOrderStatus == '10007001' && item.remainingTime && item.remainingTime > 0" @click="payContinue(item)" class="payContniue f_c_c">继续支付</u-button>
						<u-button v-if="item.goodsOrderStatus == '10007003' && item.goodsExpressStatus=='10012002'" @click="confirmGoods(item)" class="payContniue f_c_c">确认收货</u-button>
					</view>
				</view>
			</view>
		</LoadMoreList>
		<!-- 确认收货 -->
		<u-action-sheet 
			description="请确定货物是否已经收到～" round="12"
			:actions="list" title="确认收货" cancelText="取消" :show="show"
			@select="confirmGoodsAxios" @close="show = false"
		
		></u-action-sheet>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		_orderList,_confirmGoods
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				goodsOrderStatus: '',
				show:false,
				orderId:'',
				list: [{name:'确认收货'}],
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
				]
			};
		},
		computed: {
			...mapState({
				express_status: state => state.dictArrs.express_status,
				pay_status_type: state => state.dictArrs.pay_status_type,
			})
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue')
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {
			this.$store.dispatch('getDictlists', 'express_status,pay_status_type')
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
				let status = this.goodsOrderStatus;
				let goodsExpressStatus = '';
				if (this.goodsOrderStatus == '10012002' || this.goodsOrderStatus == '10012003') {
					goodsExpressStatus = this.goodsOrderStatus;
					status = '10007003';
				}
				_orderList({
					page: pageIndex,
					limit: limit,
					goodsOrderStatus: status,
					goodsExpressStatus: goodsExpressStatus
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
			// 
			payContinue(item) {
				const {
					payChannel,
					payText,
					orderId,
					isSeckill,
					orderPayPrice
				} = item;
				if (payChannel) {
					// 继续支付处理
					this.$wxstrPay(payChannel,payText,orderId,orderPayPrice);
				} else {
					this.$navTo(
						`/pages/packageA/pay/pay?orderId=${orderId}&amount=${orderPayPrice}&goodsType=${isSeckill == '0' ? '2' : '1'}`
					)
				}
			},
			// 订单详情
			doOrderDetail(item) {
				this.$navTo(`../orderDetail/orderDetail?orderId=${item.orderId}`)
			},
			// 确认收货
			confirmGoods(item){
				this.orderId = item.orderId;
				this.show = true;
			},
			confirmGoodsAxios(){
				_confirmGoods(this.orderId).then(res=>{
					this.show = false;
					if(res && res.code == 0){
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
		.order {
			padding: 30rpx;

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
