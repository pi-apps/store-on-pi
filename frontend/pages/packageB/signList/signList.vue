<template>
	<view class="myIntegers">
		<!-- 自定义导航 -->
		<uniNav title="我的积分" bgColor="#FF4800" color="#FFF" />
		<view class="top_bgs f f_align">
			<view class="bold" style="font-size: 60rpx;">{{userInfo.accountEntity.amountSigninPoints}}</view>
			<view class="btnm_use f_c_c -bgf bold" @click="$navTo('/pages/packageA/sign/sign')">去使用</view>
		</view>
		<LoadMoreList :page="page" :data="page.list">
			<view class="myIntegers">
				<view class="order -bgf">
					<view class="sign_list_box -bgf">
						<view class="f_c_c">
							<u--image class="f_c_c absolute" src="/static/packageB/image/xiangqing1@2x.png" width="426rpx"
								height="4rpx" />
							<text class="bold fz16 tt_text">积分明细</text>
						</view>
						<view class="interWarps f f_column f_align" v-for="(item,index) in page.list" :key="index">
							<view class="f_c_a">
								<view class="bold fz16">
									<block v-if="item.scoreType == 0">
										<text v-if="item.isAfterSign == '0'" class="bold">补签</text>
										<block v-else>
											<text v-if="item.configId" class="bold">连续签到{{item.signDays}}天</text>
											<text v-else class="bold">日常签到</text>
										</block>
									</block>
									<text v-else-if="item.scoreType == '1'">兑换积分商品</text>
									<text v-else-if="item.scoreType == '3'">转换PI积分</text>
								</view>
								<view class="f f_align_center">
									<text v-if="item.accountTrend == '0'" class="bold add fz20">+{{item.scoreNum}}</text>
									<text v-else class="bold downs fz20">-{{item.scoreNum}}</text>
								</view>
							</view>
							<text class="mt16">{{item.createTime}}</text>
						</view>
					</view>
				</view>
			</view>
		</LoadMoreList>
	</view>
</template>

<script>
	import {
		_signDetail
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				scoreType: [{
					label: "",
					value: '0'
				}],
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
						amountSigninPoints: 0,
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
				_signDetail({
					page: pageIndex,
					limit: limit,
					goodsOrderStatus: this.goodsOrderStatus
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
	.top_bgs {
		height: 240rpx;
		background-color: #FF4800;
		padding: 40rpx 62rpx 0;
		box-sizing: border-box;
		color: #FFF;

		.btnm_use {
			width: 188rpx;
			height: 72rpx;
			font-size: 32rpx;
			color: #FF4F00;
			border-radius: 40rpx;
		}
	}

	/deep/ .myIntegers {
		.order {
			padding: 32rpx;

			.sign_list_box {
				padding: 32rpx;
				border-radius: 16rpx;
				position: relative;
				top: -100rpx;
				
				    .tt_text{
				      color: #FF3800;
				    }
			}
		}

		.interWarps {
			padding: 30rpx 0;
			border-bottom: 1px solid rgba(#000, 0.1);

			&:last-child {
				border-bottom: none;
			}

			.downs {
				color: #20cf82;
			}

			.add {
				color: $font-money-color2;
			}
		}
	}
</style>
