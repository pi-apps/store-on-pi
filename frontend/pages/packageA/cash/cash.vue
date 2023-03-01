<template>
	<view class="cash">
		<!-- 自定义导航 -->
		<uniNav title="余额" bgColor="#FF3100" color="#FFF" />

		<view class="balance f_c_c">
			<view class="balance_box">
				<view class="f_c_a">
					<view class="">
						<text class="bold">我的余额</text>
						<view class="mt12 amount">
							<text class="fz12">π</text>
							<u-count-to duration="1000" :decimals="2" color="#FF4600"
								:endVal="userInfo.accountEntity.amountBalance" separator="," bold></u-count-to>
						</view>
					</view>
					<view v-if="userInfo.accountEntity.amountBalance > 0" class="cash_btn f_c_c bold"
						@click="NavTo('../cashAmount/cashAmount')">去提现</view>
				</view>
				<view class="bb_amount">
					<text class="bold">累计提现: <text class="fz10">π</text>
						<u-count-to duration="1000" :decimals="2" color="#969696" fontSize="16" bold :endVal="cashAmount" separator=",">
						</u-count-to>
					</text>
				</view>
			</view>

		</view>

		<view class="cash_title fz18 bold">提现明细</view>

		<LoadMoreList :page="page" :data="page.list">
			<view class="cash_row_list -warp">
				<!-- 骨架屏幕 -->
				<view class="warp_swiper mb12 f f_align_start -warp -bgf -box" v-for="(item,index) in page.list"
					:key="item.cashOrderId">
					<u-icon v-if="item.tuserCardEntity && item.tuserCardEntity.cashCardType == '10018001'"
						color="#108EE9" class="mr32" size="34" name="zhifubao-circle-fill" />
					<i v-else class="iconfont mr32 icon-yinhangqia"></i>
					<view class="w100">
						<view class="f_c_a overfont">
							<text v-if="item.tuserCardEntity && item.tuserCardEntity.cashCardType == '10018001'"
								class="fz18">支付宝</text>
							<text v-else
								class="fz18 overfont">{{item.tuserCardEntity && item.tuserCardEntity.bankName}}</text>
							<text
								:class="item.cashApprovalState == '10020003' ? 'success' : 'tFonts'">{{$getDict(cash_approval_state,item.cashApprovalState)}}</text>
						</view>
						<view class="amount fz22 mt12 bold"><text class="fz12">π</text>{{item.amount}}
						</view>
						<view class="tFonts fz12 mt4">{{item.createTime}}</view>
						<view v-if="item.cashApprovalState == '10020004'" class="tFonts fz12 mt4">驳回原因：{{item.remark}}
						</view>
					</view>
				</view>

			</view>
		</LoadMoreList>

		<u-modal :show="showCash" title="实名认证" content='应国家法律法规，需要先进行实名认证，才能够继续后续操作！' confirmText="立即认证"
			@confirm="doJumpAuth"></u-modal>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		_cashAmountRoad,
		_sumCashamount
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				showCash: false,
				cashAmount: 0,
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
						amountBalance:0
					}
				},
			};
		},
		computed: {
			...mapState({
				cash_approval_state: state => state.dictArrs.cash_approval_state
			})
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue')
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {
			this.$store.dispatch('getDictlists', 'cash_approval_state')
		},
		onShow() {
			this.init();
		},
		methods: {
			// 
			init() {
				this.getRowList();
				this.getUserInfo();
			},
			// 查询用户信息
			getUserInfo() {
				this.$getUserInfoSync().then(res => {
					if (res && res.code == 0) {
						this.userInfo = res.data;
						if (!res.data.authEntity || !res.data.authEntity.certName) {
							this.showCash = true
						}
					}
				})
				_sumCashamount().then(res => {
					if (res && res.code == '0') {
						this.cashAmount = res.data
					}
				})
			},

			// 
			getRowList() {
				this.page.isLoad = true
				const {
					pageIndex,
					limit
				} = this.page;
				_cashAmountRoad({
					page: pageIndex,
					limit: limit
				}).then(res => {
					this.page.isLoad = false;
					if (res && res.code == 0) {
						let lists = res.data && res.data.list;
						this.page.totalPage = res.data.totalPage || 0
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
			// 登录
			NavTo(url) {
				const {
					accountEntity
				} = this.userInfo
				if (accountEntity.amountBalance > 0) {
					this.$navTo(url)
				} else {
					this.$showToast('余额不足，无法提现～')
				}
			},

			doJumpAuth() {
				this.$redTo('/pages/packageA/auth/auth')
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
				this.getRowList();
			} else {
				this.page.isDone = true
			}
		},
	}
</script>

<style lang="scss" scoped>
	.cash {
		.warp_swiper {
			box-sizing: border-box;

			.icon-yinhangqia {
				font-size: 60rpx;
				color: #f67112;
			}

			.tFonts {
				color: #999;
			}

			.amount {
				color: $font-money-color2;
			}

			.success {
				color: $color-success;
			}
		}

		.balance {
			background-color: #FF3100;
			padding: 50rpx 30rpx 0;
			height: 100rpx;
			font-size: 32rpx;
			position: relative;

			.balance_box {
				width: 686rpx;
				background-color: #fff;
				color: #292929;
				position: absolute;
				border-radius: 16rpx;
				padding: 42rpx 36rpx 24rpx;
				box-sizing: border-box;
				top: 60rpx;

				.amount {
					color: #FF4600;
				}

				.cash_btn {
					width: 144rpx;
					height: 64rpx;
					border: solid 2rpx #FF4500;
					padding: 4rpx 20rpx;
					border-radius: 32rpx;
					font-size: 28rpx;
					color: #FF4300;
				}
			}

			.bb_amount {
				color: #969696;
				font-size: 28rpx;
				padding-top: 28rpx;
				border-top: 1px solid #F5F5F5;
				margin-top: 24rpx;
			}

		}

		.cash_title {
			padding: 24rpx 32rpx;
			margin-top: 200rpx;
			color: #292929;
		}
	}
</style>
