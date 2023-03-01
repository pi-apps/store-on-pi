<template>
	<view class="page_warps">
		<!-- 自定义导航 -->
		<uniNav title="提现" />

		<view class="cashAmount">
			<view class="top_shows flex f_align f_align_center" v-if="cards.cardId == ''" 
				@click="NavTo('../card/card?set=true')">
				<view>
					<view class="h1">选择方式</view>
					<view class="h2">去选择或者创建一个提现账号</view>
				</view>
				<u-icon name="arrow-right" size="30" color="#FFF"></u-icon>
			</view>

			<block v-else>
				<view class="drawal_cards pay f_c_a" v-if="cards.cashCardType == '10018001'" @click="NavTo('../card/card?set=true')">
					<view class="f left_methods">
						<u-icon class="mr4 ml4" size="40" color="#FFF" name="zhifubao-circle-fill" />
						<view class="cardInfo">
							<text>支付宝</text>
							<view class="nomber">{{cards.alipayPaymentAccount}}</view>
						</view>
					</view>
					<view class="choose_m">
						<view class="f f_align_center">
							<view>更换方式 </view>
							<u-icon class="ml4" color="#FFF" name="arrow-right" size="16"></u-icon>
						</view>
					</view>
				</view>
				<view class="drawal_cards f_c_a" v-else @click="NavTo('../card/card?set=true')">
					<view class="flex left_methods">
						<i class="iconfont mr4 ml4 icon-yinhangqia"></i>
						<view class="cardInfo">
							<text>{{cards.bankName}}</text>
							<view class="nomber">{{cards.bankcardNum}}</view>
						</view>
					</view>
					<view class="choose_m">
						<view class="f f_align_center">
							<view>更换方式 </view>
							<u-icon class="ml4" color="#FFF" name="arrow-right" size="16"></u-icon>
						</view>
					</view>
				</view>
			</block>
		</view>

		<view class="warps top_case">
			<view class="top_acount">提现金额</view>
			<view class="catwtch_money f_c_a">
				<view class="left_Warp">
					<text class="left">π</text>
					<input @focus="amountChange" type="number" :maxlength="10" placeholder-class="pStyle" v-model="brokerAmountNow" placeholder="请输入提现金额" />
				</view>
				<text class="right" @click="toMaxAmount">全部提现</text>
			</view>
			<view class="watsh">
				<view>可提现余额 <text class="blance">π {{brokerageAmount}}</text></view>
				<text class="tips">提示：预计1个工作日内提现到账；手续费:8%；</text>
			</view>
		</view>
		<view class="warps pay_meth f_align_center f f_column">
			<view class="f f_center f_align_center warp_xy" @click="traggetRead">
				<radio :checked="isRead" style="transform:scale(0.7)" color="#f67112" />
				<view class="f_align_center f ml8">
					<view>请先同意</view>
					<view class="name_xy" @click.stop="goUserXy('10010007')">《服务合作协议》</view>
				</view>
			</view>
			<u-button :loading="loading" loading-text="提现申请中..." :disabled="loading" class="submit_btn f_c_c" @click="formSubmit">
				立即提现
			</u-button>
		</view>
	</view>
</template>

<script>
	import { _cashAmount } from '@/http/apiReq'
	export default {
		data() {
			return {
				brokerageAmount: 0.00,
				brokerAmountNow: '',
				cards: {
					cardId: '',
					cashCardType: '10001003',
					bankName: '--',
					alipayPaymentAccount: '--',
					bankcardNum: '--'
				},
				isRead: true,
				loading: false
			};
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {

		},
		onShow() {
			let casrd = wx.getStorageSync('cards');
			if (casrd && casrd.userCardId != '') {
				this.cards = casrd
			}
			this.getUserInfos();
		},
		methods: {
			// 
			getUserInfos() {
				this.$getUserInfoSync().then(res => {
					if (res && res.code == 0) {
						this.brokerageAmount = res.data.accountEntity.amountBalance
					}
				})
			},
			// 登录
			NavTo(url) {
				this.$navTo(url)
			},
			// 
			traggetRead() {
				this.isRead = !this.isRead
			},
			// 最大
			toMaxAmount() {
				let max = parseInt(this.brokerageAmount / 10);
				this.brokerAmountNow =  max * 10
			},
			// 
			goUserXy(id) {
				this.$navTo('../xy/xy?id=' + id)
			},
			// 验证
			//提交
			formSubmit() {
				const {
					brokerAmountNow,
					brokerageAmount,
					isRead,
					cards
				} = this;
				if (cards.cardId == '') {
					return this.$showToast('请选择提现方式')
				}
				if (brokerAmountNow == '') {
					return this.$showToast('输入要提现的金额')
				}
				if (brokerAmountNow <= 0) {
					return this.$showToast('提现金额不能小于等于0')
				}
				if (brokerAmountNow > brokerageAmount) {
					return this.$showToast('提现金额最大不能超过可提现余额')
				}
				if (brokerAmountNow % 10 != 0) {
					return this.$showToast('提现金额只能是10的整数倍')
				}
				if (!isRead) {
					return this.$showToast('请先阅读协议内容')
				}

				this.withCash();
			},
			// git 
			withCash() {
				const {
					cardId
				} = this.cards;
				this.loading = true
				_cashAmount({
					amount: this.brokerAmountNow,
					userCardId: cardId
				}).then(res => {
					this.loading = false
					if (res && res.code == 0) {
						this.brokerAmountNow = ''
						this.$showToast('提现申请成功～');
						setTimeout(() => {
							this.$navBack()
						}, 1000)
					}
				}).catch(() => this.loading = false)
			},
		}
	}
</script>

<style lang="scss" scoped>
	.page_warps {
		width: 100%;

		.top_shows {
			background: linear-gradient(90deg, #333 0%, #4D4D4D 100%);
			color: #fff;
			border-radius: 12rpx;
			padding: 32rpx 48rpx;

			.h1 {
				font-size: 36rpx;
				font-weight: bold;
				margin-bottom: 10rpx;
			}

			.h2 {
				font-size: 24rpx;
				margin-bottom: 20rpx;
			}
		}

		.cashAmount {
			padding: 24rpx;
			background-color: #fff;

			.drawal_cards {
				height: 240rpx;
				background-color: $color-tt;
				border-radius: 20rpx;
				color: #fff;
				font-size: 28rpx;
				.icon-yinhangqia{
					font-size: 60rpx;
				}

				&.pay {
					background-color: #108EE9;
				}

				.left_methods {
					.iocn {
						margin-left: 48rpx;
						width: 60rpx;
						height: 60rpx;
					}

					.cardInfo {
						display: flex;
						flex-direction: column;
						margin-left: 50rpx;
						font-size: 36rpx;

						.nomber {
							font-size: 28rpx;
							margin-top: 50rpx;
						}
					}
				}
			}

			.choose_m {
				padding-right: 20rpx;
			}
		}





		.warps {
			background-color: #fff;
			margin-top: 30rpx;
			padding: 0 24rpx
		}

		.watsh {
			padding: 30rpx 0;
			color: #666;
			font-size: 28rpx;
			border-bottom: solid 1px #ededed;

			.blance {
				color: #DD2828;
			}

			.tips {
				color: #999;
				font-size: 24rpx;
			}
		}

		.top_case {
			padding: 30rpx 24rpx;

			.top_acount {
				color: #333;
				font: 28rpx;
			}
		}

		.catwtch_money {
			height: 130rpx;
			border-bottom: solid 1rpx #ededed;
			padding-left: 15rpx;
			margin-top: 20rpx;

			.left_Warp {
				display: flex;
				align-items: center;

				.left {
					font-size: 32rpx;
				}
			}

			.right {
				width: 130rpx;
				flex-shrink: 0;
				font-size: 28rpx;
				color: $color-tt;
			}

			input {
				font-weight: bold;
				height: 90rpx;
				line-height: 90rpx;
				color: #000;
				font-size: 60rpx;
				margin-left: 15rpx;
			}
		}

		.pay_meth {
			padding: 100rpx 30rpx;

			.warp_xy {
				text-align: center;
				font-size: 24rpx;

				.radios {
					width: 30rpx;
					height: 30rpx;
					margin-right: 10rpx;
				}

				.name_xy {
					color: $color-tt;
				}
			}

			.uni-form-item {
				display: flex;
				align-items: center;
				border-bottom: solid 1rpx #E6E6E6;
				height: 88rpx;
				color: #333;
				font-size: 28rpx;

				.title {
					width: 200rpx;
					flex-shrink: 0;
				}

				input {
					height: 88rpx;
					width: 100%;
				}

				.right {
					width: 150rpx;
					flex-shrink: 0;
					font-size: 28rpx;
				}
			}

			.submit_btn {
				width: 700rpx;
				height: 90rpx;
				color: #fff;
				background-color:$color-tt;
				border-radius: 50rpx;
				margin-top: 30rpx;
				font-size: 32rpx;
			}
		}
	}
</style>
