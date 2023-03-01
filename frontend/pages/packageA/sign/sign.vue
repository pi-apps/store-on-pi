<template>
	<view class="sign">
		<!-- 自定义导航 -->
		<uniNav :title="$t('pages.sign.title')" bgColor="#f67112" color="#FFF" />

		<view class="top_warp f f_column">
			<text>{{$t('pages.sign.h1')}}</text>
			<text class="mt12 fz22">
				<u-count-to duration="1000" color="#FFF" :endVal="userInfo.amountSigninPoints"
					separator=","></u-count-to>
			</text>
			<u--image class="f_c_c f_baseline r_tips_img" src="/static/packageA/image/tu.png" width="150" height="90" />
		</view>
		<!--  -->
		<view class="bgf_warps">
			<view class="jf_warps">
				<view class="bold">{{$t('pages.sign.h2')}}</view>
				<view class="t_c_999">{{$t('pages.sign.signIn')}}<text class="red">{{lxNums}}</text>{{$t('pages.sign.day')}}</view>
				<view class="b_line" v-if="signConfig.length > 0">
					<u-line-progress class="w100" activeColor="#EC661B" :percentage="proegess" height="4"
						:showText="false" />
					<view class="box_warp f_c_a">
						<view v-for="item in signConfig" :key="item.configId"
							class="lx_sign_box f f_column f_align_center">
							<u--image v-if="lxNums >= item.signDays" class="sign_success"
								src="/static/packageA/image/success@2x.png" width="40" height="40" />
							<view v-else class="box_i f_c_c">+{{item.scoreNum}}</view>
							<text :class="lxNums >= item.signDays ? 'isGo' : ''">第{{item.signDays}}天</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		<!--  -->
		<view class="time_list">
			<view class="sign_w f f_column f_align_center" v-for="(item,index) in weekList" :key="index">
				<view v-if="item.day == toDay">{{$t('pages.sign.today')}}</view>
				<view v-else>{{item.weekZh}}</view>
				<view class="times bold">{{item.day}}</view>
				<view class="b_sign_warp">
					<view v-if="item.isSign == 0">{{$t('pages.sign.signed')}}</view>
					<view v-else-if="item.isSign == 2">已补签</view>
					<block v-else>
						<view v-if="item.isAfterSignOnce" class="sign_bq f f_c_c" @click="signAdds(item)">{{$t('pages.sign.SupSign')}}</view>
						<view v-else class="dolt"></view>
					</block>
				</view>
			</view>

			<view class="more_text f_c_c" @click="jfExchange">
				<text>{{$t('pages.sign.btn')}}</text>
				<i class="iconfont icon-xiangyou1"></i>
			</view>
		</view>

		<!-- 积分商城 -->
		<view class="jf_shop_warp">
			<view class="fz12" style="line-height: 48rpx;">{{remark}}</view>
		</view>

		<!-- 补签 签到 -->
		<Alert :show="toast" :showBtn="false">
			<view class="f_c_c h100">
				<view v-if="isSign" class="sign_warps f f_column f_align_center">
					<u--image mode="scaleToFill" class="f_c_c" style="margin-top: -75px;" src="/static/packageA/image/bg_sign_in@2x.png" width="130" height="120" />
					<text class="fz16 bold mt20">{{$t('pages.sign.signOK')}}</text>
					<text class="mt20"><text class="fz24 bold">{{toDayGetRecore}}</text>{{$t('pages.sign.Point')}}</text>
					<view class="btn_get f_c_c fz16" @click="closeModel">{{$t('pages.sign.get')}}</view>
				</view>
				<view v-else class="sign_warps f f_column f_align_center">
					<text class="fz16 bold mt32">补签{{afterSign.timeStringZh}}</text>
					<text class="mt32 isafter">免费补签</text>
					<view class="btn_get f_c_c fz16" @click="signAddsAxios">立即补签</view>
					<i class="iconfont icon-close" @click="closeModel"></i>
				</view>
			</view>
		</Alert>
	</view>
</template>

<script>
	import {
		_sign,
		_signConfig,
		_signBaseScoreNum,
		_signDays,
		_signLog,
		_afterSign
	} from '@/http/apiReq'
	import dayjs from 'dayjs';
	import {
		mapState
	} from 'vuex';
	export default {
		data() {
			return {
				page: {
					pageIndex: 1,
					limit: 10,
					totalPage: 1,
					isLoad: false,
					isDone: false,
					list: [],
				},
				remark:'',
				amountPoints: 0, //补签需要的PI积分
				signBaseScoreNum: 0, //每日签到获取基础积分
				lxNums: 0, // 连续签到天数

				proegess: 0,
				signConfig: [],
				toDay: '',
				weekList: [],
				ZhWeek: ['日', '一', '二', '三', '四', '五', '六'],

				toast: false,
				isSign: false,
				toDayGetRecore: 0, // 当日签到成功获取的积分

				afterSign: {
					timeString: "",
					timeStringZh: ""
				},
				userInfo: {
					amountSigninPoints: 0
				}
			};
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue')
		},
		onLoad(options) {
			this.goodsList();
		},
		onShow() {
			this.signToday();
		},
		methods: {
			// 签到
			signToday() {
				_sign().then(res => {
					console.log(111, res)
					if (res && res.code == 0) {
						this.getUserInfoSync();
						this.toast = true;
						this.isSign = true;
						this.toDayGetRecore = res.data
					}
					// 查询用户信息
					this.getUserInfoSync();
					// 获取各种配置信息
					this.getToday();
				});
			},
			// 获取今日信息
			getToday() {
				let day = new Date();
				let d = day.getDay();
				let t = day.getDate();
				let sunday = day.setDate(t + (7 - d));

				let weeklist = [];
				for (let i = 13; i >= 0; i--) {
					let lsd = new Date(sunday);
					let rowDayDate = lsd.setDate(lsd.getDate() - i);
					let Week = new Date(rowDayDate).getDay();
					weeklist.push({
						weekZh: this.ZhWeek[Week],
						week: Week,
						day: dayjs(rowDayDate).format('D'),
						timeString: dayjs(rowDayDate).format('YYYY-MM-DD'),
						timeStringZh: dayjs(rowDayDate).format('M月D日'),
						isSign: 1,
						isAfterSignOnce: false
					})
				}
				this.toDay = t;
				this.weekList = weeklist
				// 获取签到配置
				this.getSignConfig();
				//查询基础陪配置
				this.signBaseScoreNumMoth();
				//查询签到记录
				this.getSignReload();
			},
			// 查询用户基本信息
			getUserInfoSync() {
				this.$getUserInfoSync().then(res => {
					console.log(res);
					if (res && res.code == 0) {
						this.userInfo = res.data
					}
				})
			},
			//查询基础签到配置
			signBaseScoreNumMoth() {
				_signBaseScoreNum().then(res => {
					if (res && res.code == 0) {
						const {
							signBaseScoreNum,
							amountPoints,
							remark
						} = res.data;
						this.amountPoints = amountPoints;
						this.signBaseScoreNum = signBaseScoreNum;
						this.remark = remark;
					}
				})
			},
			// 获取签到配置
			getSignConfig() {
				_signConfig().then(res => {
					if (res && res.code == 0) {
						this.signConfig = res.data;
						// 查询最近的连续签到记录
						this.get30Day();
					}
				})
			},
			// 查询签到记录
			getSignReload() {
				_signLog().then(res => {
					console.log(res);
					if (res && res.code == 0) {
						let list = res.data;
						// 只能进行一次一次签到
						let isSign = [];
						for (let i = 0; i < this.weekList.length; i++) {
							for (let k = 0; k < list.length; k++) {
								if (this.weekList[i].timeString == list[k].signDate) {
									if (list[k].isAfterSign == 0) {
										this.weekList[i].isSign = 2;
									} else {
										this.weekList[i].isSign = 0;
									}
								}
							};
							// 只能补签后面三天的
							let d1 = dayjs().subtract(1, 'day').format('D');
							let d2 = dayjs().subtract(2, 'day').format('D');
							let d3 = dayjs().subtract(3, 'day').format('D');
							console.log(d1, d2, d3)
							if (this.weekList[i].day == d1 || this.weekList[i].day == d2 || this.weekList[i].day ==
								d3) {
								isSign.push({
									_index: i,
									...this.weekList[i]
								})
							}
						}
						// 判断后面三天是否已经签到
						let indexSign = isSign.find((item, index) => {
							return item.isSign == 1;
						});
						if (indexSign && indexSign._index) {
							this.weekList[indexSign._index].isAfterSignOnce = true;
						}
						console.log(this.weekList)
						//this.weekList = weekList
					}
				})
			},
			//查询最近的签到记录
			get30Day() {
				_signDays().then(res => {
					if (res && res.code == 0) {
						this.lxNums = res.data || 0;
						this.getProess();
					}
				})
			},
			//获取进度条
			getProess() {
				console.log(this.lxNums, this.signConfig)
				if (this.signConfig.length > 0) {
					if (this.lxNums >= this.signConfig[this.signConfig.length - 1].signDays) {
						this.proegess = 100
					} else if (this.lxNums <= this.signConfig[0].signDays) {
						this.proegess = 0
					} else {
						let len = 0;
						for (let i = 0; i < this.signConfig.length; i++) {
							if (this.lxNums >= this.signConfig[i].signDays) {
								len = i;
							}
						}
						if (len > 0) {
							len += 1;
						}
						this.proegess = Math.floor(100 / this.signConfig.length) * len
					}
				}
			},
			// 补签
			signAdds(item) {
				console.log(item)
				return this.$showToast(this.$t('pages.sign.toast'))
				this.toast = true;
				this.isSign = false;
				this.afterSign = item
			},
			// PI积分 补签
			signAddsAxios() {
				const {
					timeString
				} = this.afterSign;
				_afterSign({
					afterSignDate: timeString,
					type: '1'
				}).then(res => {
					this.toast = false
					this.$showToast(res.msg || this.$t('pages.sign.toast_sign'));
					if (res && res.code == 0) {
						// 查询用户信息
						this.getUserInfoSync();
						// 获取各种配置信息
						this.getToday();
					}
				})
			},
			// 
			closeModel() {
				this.toast = false
			},
			// 
			jfExchange() {
				return this.$showToast(this.$t('pages.sign.toast'))
				this.$navTo('../signChange/signChange')
			}
		}
	}
</script>

<style lang="scss" scoped>
	.sign {

		// 
		.sign_warps {
			width: 550rpx;
			border-radius: 20rpx;
			background-color: #fff;
			position: relative;

			.isafter {
				color: $font-color2;
			}

			.btn_get {
				margin: 30rpx 0;
				background-color: #fa412d;
				width: 420rpx;
				height: 100rpx;
				border-radius: 20rpx;
				color: #fff;
			}

			.icon-close {
				font-size: 100rpx;
				color: rgba(#FFF, 0.8);
				position: absolute;
				bottom: -140rpx;
			}
		}

		.top_warp {
			height: 340rpx;
			background-color: #f67112;
			padding: 30rpx 60rpx;
			box-sizing: border-box;
			color: #FFF;
			position: relative;

			.button_vip {
				color: #fde8a9;
				border: solid 2rpx #fde8a9;
				padding: 4rpx 20rpx;
				border-radius: 30rpx;
			}

			.r_tips_img {
				position: absolute;
				bottom: 40rpx;
				right: 0;
				z-index: 10;
			}
		}

		.bgf_warps {
			width: 100%;
			box-sizing: border-box;
			padding: 0 32rpx 32rpx;
			background-color: #fff;

			.jf_warps {
				position: relative;
				z-index: 1;
				box-sizing: border-box;
				background-color: #fff;
				border-radius: 16rpx;
				padding: 32rpx 30rpx;
				margin-top: -70rpx;
				box-shadow: 0rpx 4rpx 12rpx #EFEFF0;

				.red {
					color: $color-tt;
					padding: 0 10rpx;
				}

				.bold {
					font-size: 32rpx;
					margin-bottom: 4rpx;
				}

				.b_line {
					height: 8rpx;
					background-color: #E6E5E5;
					width: 100%;
					margin: 74rpx 0;
				}

				.box_warp {
					position: relative;
					top: -42rpx;

					.lx_sign_box {
						font-size: 22rpx;
						color: #999999;

						.isGo {
							color: #EC661B;
						}

						.box_i {
							width: 80rpx;
							height: 80rpx;
							font-size: 24rpx;
							background-color: #E6E6E6;
							border-radius: 50%;
							margin-bottom: 11rpx;
						}
					}
				}
			}
		}

		// 
		.time_list {
			display: flex;
			flex-wrap: wrap;
			padding: 32rpx;
			background-color: #fff;

			// 
			.more_text {
				width: 100%;
				color: $font-color3
			}

			.sign_w {
				width: 14.28%;
				font-size: 24rpx;
				color: #999;
				margin-bottom: 32rpx;

				.times {
					font-size: 32rpx;
					color: #1A1A1A;
					margin-top: 14rpx;
				}

				.b_sign_warp {
					margin-top: 11rpx;
					height: 28rpx;

					.dolt {
						width: 8rpx;
						height: 8rpx;
						background-color: #999;
						border-radius: 50%;
					}

					.sign_bq {
						width: 64rpx;
						height: 28rpx;
						font-size: 20rpx;
						border-radius: 14rpx;
						border: solid 1rpx #EC661B;
						color: #EC661B;
					}
				}
			}
		}

		// 积分商品
		.jf_shop_warp {
			padding: 32rpx 32rpx 100rpx;
			display: flex;

			.jf_warp_list_r {
				box-sizing: border-box;
				width: 332rpx;
				background-color: #fff;
				border-radius: 16rpx;
				padding: 18rpx 24rpx 15rpx;
				margin-bottom: 22rpx;

				.goods_title {
					color: #0E0E0D;
					margin-bottom: 21rpx;
				}

				.jf_nums {
					font-size: 20rpx;
					color: #98A1A5;

					.nums {
						font-size: 30rpx;
						color: $color-tt;
						margin-left: 4rpx;
					}
				}

				.now_get {
					color: #FFF;
					border-radius: 30rpx;
					height: 60rpx;
					background-image: linear-gradient(to right, #fbc148, #fb992a);
				}
			}
		}
	}
</style>
