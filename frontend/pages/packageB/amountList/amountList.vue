<template>
	<view class="myIntegersrow">
		<!-- 自定义导航 -->
		<uniNav title="PI积分" bgColor="#FFAA00" color="#FFF" />
		<view class="top_bgs f f_align">
			<view class="bold" style="font-size: 60rpx;">{{userInfo.accountEntity.amountPoints}}</view>
			<view class="btnm_use f_c_c -bgf bold" @click="$navTo('/pages/packageA/goodsList/goodsList?vip=1&title=积分商城')">去使用</view>
		</view>
		<LoadMoreList :page="page" :data="page.list">
			<view class="order -bgf">
				<view class="sign_list_box -bgf">
					<view class="f_c_c">
						<u--image class="f_c_c absolute" src="/static/packageB/image/xiangqing@2x.png" width="426rpx"
							height="4rpx" />
						<text class="bold fz16 tt_text">积分明细</text>
					</view>
					<view class="interWarps f f_column f_align" v-for="(item,index) in page.list" :key="item.orderId">
						<view class="f_c_a w100">
							<view class="bold fz16">
								{{$getDict(scoreType,item.sourceType)}}
							</view>
							<view class="f f_align_center">
								<text v-if="item.accountTrend == '0'"
									class="bold add fz20">+{{item.changeAmountPoint}}</text>
								<text v-else class="bold downs fz20">-{{item.changeAmountPoint}}</text>
							</view>
						</view>
						<text class="mt16">{{item.createTime}}</text>
					</view>
				</view>
			</view>
		</LoadMoreList>
	</view>
</template>

<script>
	import {
		_getAmountPointChangeInfo
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				scoreType: [{
						label: "购买特权赠送",
						value: '1'
					},
					{
						label: "商城退货退款返还",
						value: '2'
					},
					{
						label: "积分商城退货返还",
						value: '3'
					},
					{
						label: "签到积分转换",
						value: '4'
					},
					{
						label: "购买商品抵扣",
						value: '5'
					},
					{
						label: "补签消耗",
						value: '6'
					},
					{
						label: "系统迁移",
						value: '9'
					}
				],
				page: {
					pageIndex: 1,
					limit: 10,
					totalPage: 1,
					isLoad: false,
					isDone: false,
					list: [],
				},
				
				userInfo: {
					accountEntity: {
						amountPoints: 0,
					}
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

		},
		onShow() {
			this.init();
		},
		methods: {
			init() {
				this.getDataRowListAxios();
				this.getUserInfo();
			},
			getUserInfo() {
				this.$getUserInfoSync(false, false).then(res => {
					if (res && res.code == 0) {
						this.userInfo = res.data;
					}
				});
			},
			// 查询列表数据
			getDataRowListAxios() {
				this.page.isLoad = true
				const {
					pageIndex,
					limit
				} = this.page;
				_getAmountPointChangeInfo({
					page: pageIndex,
					limit: limit,
					goodsOrderStatus: this.goodsOrderStatus
				}).then(res => {
					this.page.isLoad = false;
					if (res && res.code == 0) {
						let lists = res.data && res.data.list;
						this.page.totalPage = res.data.totalPage || 0;
						if (pageIndex <= 1) {
							this.page.list = lists || []
						} else {
							let o_list = this.page.list;
							this.page.list = [...o_list, ...lists];
						}
						if (lists.length < limit) {
							this.page.isDone = true
						}
					}
				}).catch(() => this.page.isLoad = false)
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
	.myIntegersrow {
		min-height: 100vh;
	}

	.top_bgs {
		height: 240rpx;
		background-color: #FFAA00;
		padding: 40rpx 62rpx 0;
		box-sizing: border-box;
		color: #FFF;

		.btnm_use {
			width: 188rpx;
			height: 72rpx;
			font-size: 32rpx;
			color: #FFAA00;
			border-radius: 40rpx;
		}
	}

	/deep/ .order {
		padding: 32rpx;

		.sign_list_box {
			padding: 32rpx;
			border-radius: 16rpx;
			position: relative;
			top: -100rpx;

			.tt_text {
				color: #FFAA00;
			}
		}

		.interWarps {
			padding: 30rpx 0;
			border-bottom: 1px solid rgba(#000, 0.1);

			&:last-child {
				border-bottom: none;
			}

			.f_c_a {
				flex-direction: row;
			}

			.downs {
				color: #20cf82;
			}

			.add {
				color: #FFAA00;
			}
		}
	}
</style>
