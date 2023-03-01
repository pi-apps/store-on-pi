<template>
	<view class="orderMain">
		<!-- 自定义导航 -->
		<uniNav title="积分订单">
			<view class="-bgf">
				<u-tabs :list="tabs" @click="onChangeTab"></u-tabs>
			</view>
		</uniNav>
		<LoadMoreList :page="page" :data="page.list">
			<view class="order -warp30 -bgf">
				<view class="mb24" v-for="(item,index) in page.list" :key="item.orderId">
					<view class="f f_align mt12" @click="doOrderDetail(item)">
						<u--image :src="item.goodsInfo.goodsVideoPic ? item.goodsInfo.goodsVideoPic : ''" radius="8"
							width="80" height="80" />
						<view class="f f_column f_align w100 ml12">
							<view class="f f_align_start f_align">
								<view class="overfont-2">{{item.goodsInfo.goodsName}}</view>
								<view class="f f_column f_align_end ml12">
									<text class="tsize">x{{item.goodsNum}}</text>
								</view>
							</view>
							<view class="f_c_a mt12">
								<text
									:class="item.goodsOrderStatus == '10012001' ? 'tsize' : 'status'">{{ item.goodsOrderStatus == '10012001' ? '待发货' : '已发货'}}</text>
								<view class="f f_align_center f_end">
									<block v-if="item.amountPoints && item.amountPoints > 0">
										<text class="bold price">{{item.amountPoints}}</text>PI积分
									</block>
									<text v-if="item.amountPoints > 0 && item.integralAmount > 0">+</text>
									<block v-if="item.integralAmount && item.integralAmount > 0">
										<text class="bold price">{{item.integralAmount}}</text>签到积分
									</block>
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
		_integralOrderList
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				tabs: [{
						name: '全部',
						val: ''
					},
					{
						name: '待发货',
						val: '10012001'
					},
					{
						name: '已发货',
						val: '10012002'
					}
				],
				goodsOrderStatus: '',
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
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {

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
				_integralOrderList({
					page: pageIndex,
					limit: limit,
					goodsOrderStatus: this.goodsOrderStatus
				}).then(res => {
					this.page.isLoad = false
					if (res && res.code == 0) {
						let lists = res.data && res.data.list;
						this.page.totalPage = res.data.totalPage || 0
						if (pageIndex <= 1) {
							this.page.list = lists
						} else {
							let o_list = this.page.list;
							this.page.list = [...o_list, ...lists]
						}
						if (lists.length < limit) {
							this.page.isDone = true
						}
					}
				}).catch(() => this.page.isLoad = false)
			},
			// 
			onChangeTab({val}) {
				this.goodsOrderStatus = val
				this.page.pageIndex = 1
				this.getDataRowListAxios();
			}, // 订单详情
			doOrderDetail(item) {
				this.$navTo(`../interOrderDetail/interOrderDetail?orderId=${item.orderId}`)
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
.orderMain{
  .order{
    padding: 30rpx;
    .tsize{
      color:$font-color2;
    }
    .price{
      color: $font-money-color2;
    }
    .status{
      color: #20cf82;
    }




    .payContniue{
      width: 200rpx;
      font-size: 24rpx;
      background-image: linear-gradient(to bottom, #fa4935, #fb8363);
      border-radius: 10rpx;
      color: #FFF;
      font-size: 32rpx;
    }
  }
}
</style>
