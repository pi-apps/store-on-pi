<template>
	<view class="myIncome">
		<!-- 自定义导航 -->
		<uniNav title="我的收益" bgColor="#F56E4A" color="#FFF" />

		<view class="incomeTop">
			<view class="-bgf bg_warps">
				<view class="f f_align_center">
					<u--image class="f_c_c f_shrink0 mr20" :src="userInfo.userHeadUrl" width="64" height="64"
						shape="circle" />
					<view class="r_user_name f f_column f_align">
						<text class="fz18 bold overfont">{{userInfo.nickName ? userInfo.nickName : 'PI商店用户'}}</text>
						<view class="f f_align_center userTag f_baseline">
							<u--image class="mr4 f_c_c"
								:src="'/static/image/user/'+(useType[userInfo.achievementEntity.saleUserType])"
								width="18" height="18" />
							<text>{{$getDict(sale_user_type,userInfo.achievementEntity.saleUserType)}}</text>
						</view>
					</view>
				</view>
				<view class="f_c_a mySylones">
					<view class="f_c_c f_column sy_item">
						<view class="fz18 bold">{{(userInfo.achievementEntity.achievementTotal).toFixed(2)}}</view>
						<text class="mt8">晋升值(总)</text>
					</view>
					<view class="f_c_c f_column sy_item">
						<view class="fz18 bold">{{userInfo.achievementEntity.achievementPersonal}}</view>
						<text class="mt8">个人晋升值</text>
					</view>
					<view class="f_c_c f_column sy_item">
						<view class="fz18 bold">{{userInfo.achievementEntity.achievementMax}}</view>
						<text class="mt8">晋升值(Max)</text>
					</view>
				</view>
			</view>
			<view class="bg_warps">
				<view class="fz16 bold">我的钱包</view>
			</view>
			<view class="f_c_a">
				<view class="bg_warps -bgf f_c_a w50">
					<view>
						<view class="fz16 bold">余额</view>
						<view class="mt10 f f_align_center"
							@click="isLoginNavTo('/pages/packageA/cashAmount/cashAmount')">
							<text class="cashs">立即提现</text>
							<u-icon class="ml4" name="arrow-right" size="12" />
						</view>
						<view @click="isLoginNavTo('/pages/packageA/cash/cash')" class="mt10 amount">{{(userInfo.accountEntity.amountBalance).toFixed(2)}}</view>
					</view>
					<u--image @click="isLoginNavTo('/pages/packageA/cash/cash')" src="/static/packageA/image/yue@3x@2x.png" width="46" height="46" />
				</view>
				<view class="bg_warps -bgf f_c_a w50" style="height: 220rpx;">
					<view>
						<view class="f relative">
							<view class="fz16 bold">预期收益</view>
							<u-icon @click="showTips = !showTips" class="ml4"
								name="/static/packageA/image/icon_zhuyi1@2x.png" />
							<view class="tips_warps" v-if="showTips">
								<view class="tip"></view>
								没有特殊事件发生时根据已知信息所预测能得到的收益，未来的资产收益(预期收益)是不确定的。
							</view>
						</view>
						<view class="mt32 amount f f_align_center" @click="isLoginNavTo('/pages/packageA/comeList/comeList')">
							<text>{{userSumAmount.toFixed(2)}}</text>
							<u-icon class="ml4" name="arrow-right" size="12" />
						</view>
					</view>
					<u--image src="/static/packageA/image/shouyi@2x.png" width="46" height="46" />
				</view>
			</view>

			<view class="bg_warps">
				<view class="fz16 bold">我的推广</view>
			</view>

			<view class="f_c_a">
				<view class="bg_warps -bgf f_c_a w50" @click="isLoginNavTo('/pages/packageB/myNextStore/myNextStore')">
					<view>
						<view class="fz16 bold">门店商家</view>
						<view class="mt10 f f_align_center">
							<text class="watch">立即查看</text>
							<u-icon class="ml4" name="arrow-right" size="12" />
						</view>
						<view class="mt10 amount">{{userStoreNumber}}</view>
					</view>
					<u--image src="/static/packageA/image/mendiansahngjia@2x.png" width="46" height="46" />

				</view>
				<view class="bg_warps -bgf f_c_a w50" @click="isLoginNavTo('/pages/packageB/myFans/myFans')"
					data-url="">
					<view>
						<view class="fz16 bold">我的粉丝</view>
						<view class="mt10 f f_align_center">
							<text class="cashs">查看粉丝</text>
							<u-icon class="ml4" name="arrow-right" size="12" />
						</view>
						<view class="mt10 amount">{{userFansNumber}}</view>
					</view>
					<u--image src="/static/packageA/image/wodefensi@2x.png" width="46" height="46" />
				</view>
			</view>


		</view>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		_mySubRecommendTotal,
		_myExpandStoreTotal,
		_sumAmount
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				useType: {
					"10009001": 'yingzi.png',
					"10009002": 'chezi.png',
					"10009003": 'yundou.png',
				},
				userInfo: {
					userHeadUrl: "",
					nickName: "",
					accountEntity: {
						amountBalance: 0
					},
					achievementEntity: {
						saleUserType: '10009001',
						achievementTotal: 0, //总
						achievementPersonal: 0, //个人
						achievementMax: 0, //大
					}
				},
				showTips: false,
				userFansNumber: 0,
				userStoreNumber: 0,
				userSumAmount: 0
			};
		},
		computed: {
			...mapState({
				sale_user_type: state => state.dictArrs.sale_user_type
			})
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue')
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {
			this.$store.dispatch('getDictlists', 'sale_user_type');
			this.getUserInfo();
			this.getUserNum();
		},
		onShow() {

		},
		methods: {
			// 查询用户信息
			getUserInfo() {
				this.$getUserInfoSync(false, false).then(res => {
					if (res && res.code == 0) {
						this.userInfo = res.data
					}
				})
			},
			// 查询数量
			getUserNum() {
				_mySubRecommendTotal().then(res => {
					if (res && res.code == '0') {
						this.userFansNumber = res.data
					}
				})
				_myExpandStoreTotal().then(res => {
					if (res && res.code == '0') {
						this.userStoreNumber = res.data
					}
				})
				_sumAmount().then(res => {
					if (res && res.code == '') {
						this.userSumAmount =  res.data
					}
				})
			},
			// 登录
			isLoginNavTo(url) {
				if (this.$loginModel('/pages/user/user')) {
					this.$navTo(url)
				}
			},
		}
	}
</script>

<style lang="scss" scoped>
	.myIncome {
		min-height: 100vh;
		background-color: #F6F6F6;

		.incomeTop {
			height: 180rpx;
			background-color: #F56E4A;
			padding: 0 20rpx;
			box-sizing: border-box;


		}

		.bg_warps {
			border-radius: 20rpx;
			padding: 30rpx;
			box-sizing: border-box;

			&.w50 {
				width: 48%;
			}

			.mySylones {
				padding: 20rpx 24rpx 0;
				color: #292929;
				margin-top: 20rpx;
			}

			.cashs {
				color: #F7694A;
			}

			.watch {
				color: #BC9AFF;
			}

			.amount {
				color: #8B8B8B;
			}
		}

		.r_user_name {
			height: 120rpx;
			color: #292929;

			.userTag {
				padding: 4rpx 16rpx;
				background-color: #fff;
				box-shadow: 0 0 8rpx rgba(#F7694A, 0.14);
				border-radius: 28rpx;
				font-size: 20rpx;
				color: #A3A3A3;
			}
		}

		.tips_warps {
			position: absolute;
			border-radius: 12rpx;
			padding: 20rpx 12rpx;
			background-color: rgba(#3C3C3C, 0.5);
			font-size: 20rpx;
			color: #FFF;
			width: 368rpx;
			right: -100rpx;
			z-index: 100;
			top: 50rpx;

			.tip {
				position: absolute;
				top: -10rpx;
				left: calc(50% - 5rpx);
				width: 0;
				height: 0;
				border-left: 10rpx solid transparent;
				border-right: 10rpx solid transparent;
				border-bottom: 10rpx solid rgba(#3C3C3C, 0.5);
			}
		}
	}
</style>
