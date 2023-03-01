<template>
	<view class="scenlill">
		<!-- 自定义导航 -->
		<uniNav title="限时秒杀" />
		
		<image src="/static/packageA/image/kill.png" class="killImge" />
		<!-- 秒杀时间段 -->
		<view class="top_tiitle" v-if="scenkillTime.length && scenkillTime.length > 0">
			<scroll-view style="height: 110rpx;width:750rpx;" :enhanced="true" :scroll-x="true"
				:show-scrollbar="false" :scroll-left="leftScroll" :scroll-with-animation="true">
				<view class="tab-title f_c_s">
					<view :class="item.timeId == nowTime.timeId ? 'tab-title-1 tab-title-2' : 'tab-title-1'"
						v-for="(item,index) in scenkillTime" :key='index' @click="itemLineTab(item,index+1)">
						<text>{{item.startTime < 10 ? '0'+item.startTime : item.startTime}}:00</text>
						<text class="fz10">{{$getDict(killStatus,item.killStatus)}}</text>
					</view>
				</view>
			</scroll-view>
		</view>
		<view class="relative count mt8">
			<!-- goods -->
			<image class="killImage" src="/static/packageA/image/killTime.png" />
			<!--  -->
			<u-count-down v-if="nowTime.remainingTime && nowTime.remainingTime > 0 && nowTime.killStatus != '3'"
				millisecond :time="nowTime.remainingTime*1000" @change="onChangeTime" autoStart @finish="ifEndsSenkill">
				<view class="countdown f_c_c w100">
					<view class="f f_align_center">
						{{nowTime.killStatus == '2' ? '距开始' :'距结束'}}:
						<view class="time_box">{{$padZero(timeData.hours)}}</view>:
						<view class="time_box">{{$padZero(timeData.minutes)}}</view>:
						<view class="time_box">{{$padZero(timeData.seconds)}}</view>:
						<view class="time_box">{{$padZero(parseInt(timeData.milliseconds/10))}}</view>
					</view>
				</view>
			</u-count-down>
		</view>
		<view class="goodWarp">
			<LoadMoreList :page="page" :data="page.list">
				<view class="gooodList mb12 f_c_a -box" v-for="item in page.list" :key="item.goodsId">
					<u--image :src="item.goodsInfo.goodsImgList[0]" width="200rpx" height="200rpx" radius="6" />
					<view class="w100 ml16 r_warp f_c_a f f_column">
						<text class="overfont-2 fz16 w100">{{item.goodsInfo.goodsName}}</text>
						<view class="f_c_a mt12 w100">
							<!-- <view v-if="item.goodsInfo.amountPoints > 0" class="amount_tag fz10">
								{{item.goodsInfo.amountPoints}}积分可抵{{(item.goodsInfo.amountPoints).toFixed(2)}}元
							</view> -->
						</view>
						<view class="f_c_a mt4 w100">
							<view>
								<text class="fz12 price">π<text class="fz18 bold">{{item.price}}</text></text>
								<text class="fz12 ml8 textLineThrough price_o">π<text class="bold">{{item.goodsInfo.price}}</text></text>
							</view>
							<view @click="buyNowKill(item)" v-if="nowTime.killStatus == '0' || nowTime.killStatus == '1' " class="buyNow">
								马上抢
							</view>
							<view v-else class="buyNow disable">{{$getDict(killStatus,nowTime.killStatus)}}</view>
						</view>
					</view>
				</view>
			</LoadMoreList>
		</view>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		_apiListSeckillTime,
		_apiListSeckillGoods
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				page: {
					pageIndex: 1,
					limit: 10,
					totalPage: 1,
					isLoad: false,
					isDone: false,
					list: []
				},
				timeData: {},
				leftScroll: 0,
				scenkillTime: [],
				nowTime: {
					timeId: '',
					killStatus: ''
				},
				killStatus: [{
						label: '已开抢',
						value: '0'
					},
					{
						label: '抢福利中',
						value: '1'
					},
					{
						label: '即将开始',
						value: '2'
					},
					{
						label: '已结束',
						value: '3'
					}
				],
			};
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue')
		},
		onLoad(options) {

		},
		onShow() {
			this.getSenkillTime();
		},
		methods: {
			// 查询时间段
			getSenkillTime() {
				const _this = this;
				_apiListSeckillTime().then(res => {
					console.log(res);
					if (res && res.code == 0) {
						this.scenkillTime = res.data
						let __index = '';
						let scenkillTime = res.data;
						if (scenkillTime.length > 0) {
							if (scenkillTime.length == 1) {
								__index = 1;
								_this.scroolLeft(__index);
								_this.nowTime = scenkillTime[0]
							} else {
								//有正在秒杀的
								for (let i = 0; i < scenkillTime.length; i++) {
									if (scenkillTime[i].killStatus == '1') {
										//秒杀福利
										__index = i + 1;
										_this.scroolLeft(__index);
										_this.nowTime = scenkillTime[i];
										break;
									}
								};
								//没有正在秒杀==== 即将开始
								if (__index == '') {
									for (let i = 0; i < scenkillTime.length; i++) {
										if (scenkillTime[i].killStatus == '2') {
											//秒杀福利
											__index = i + 1;
											_this.scroolLeft(__index);
											_this.nowTime = scenkillTime[i];
											break;
										}
									};
								}
								//没有正在秒杀 和 即将开始的 ==== 已结束
								if (__index == '') {
									__index = scenkillTime.length;
									_this.nowTime = scenkillTime[scenkillTime.length - 1]
									_this.scroolLeft(__index);
								}
							}
							// 查询时间段下的商品
							_this.getScenkillTimeGoods();
						}
					}
				})
			},
			// 滚动
			scroolLeft(__index) {
				if (__index >= 3) {
					this.leftScroll = (__index - 2) * 90
				} else {
					this.leftScroll = 0
				}
			},
			// 切换时间段
			itemLineTab(item,index) {
				this.nowTime = item;
				this.scroolLeft(index);
				this.getScenkillTimeGoods();
			},
			// 查询时间段下的商品
			getScenkillTimeGoods() {
				const {
					pageIndex,
					limit
				} = this.page;
				this.page.isLoad = true
				_apiListSeckillGoods({
					page: pageIndex,
					limit: limit,
					timeId: this.nowTime.timeId
				}).then(res => {
					this.page.isLoad =  false
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
							this.page.isDone =  true
						}
					}
				}).catch(() => this.page.isLoad =  false)
			},
			// 倒计时
			onChangeTime(e) {
				this.timeData = e
			},
			//倒计时结束
			ifEndsSenkill() {
				if (this.nowTime.killStatus == '1' || this.nowTime.killStatus == '2') {
					this.getSenkillTime();
				}
			},
			buyNowKill(item) {
				this.$navTo(`../goodDetail/goodDetail?goodsId=${item.seckillId}&type=2`)
			},
		},
		/**
		 * 页面相关事件处理函数--监听用户下拉动作
		 */
		onPullDownRefresh: function() {
			this.page.pageIndex = 1
			this.getSenkillTime();
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
				this.page.pageIndex++
				this.getScenkillTimeGoods();
			} else {
				this.page.isDone = true
			}
		}
	}
</script>

<style lang="scss" scoped>
	.scenlill {
		min-height: 100vh;
		background-color: $bg-color-fa;

		.top_tiitle {
			background-color: #fef1e9;
			position: sticky;
			top: 0;

			.tab-title {
				box-sizing: border-box;
				height: 110rpx;
				padding: 13rpx 0;

				.tab-title-1 {
					flex-shrink: 0;
					margin: 0 30rpx;
					width: 136rpx;
					height: 76rpx;
					border-radius: 12rpx;
					display: flex;
					flex-direction: column;
					align-items: center;
					justify-content: space-around;
					color: $color-tt;
					font-size: 30rpx;
				}

				.tab-title-2 {
					background: $color-tt;
					color: #FFF;
				}
			}
		}

		// 
		.countdown {
			position: absolute;
			top: 25rpx;
			color: $color-tt;
			left: 20rpx;

			.time_box {
				background-color: $color-tt;
				color: #FFF;
				width: 50rpx;
				box-sizing: border-box;
				padding: 2rpx 8rpx;
				border-radius: 10rpx;
				margin: 0 10rpx;
			}
		}

		// /
		.goodWarp {
			padding: 30rpx;
			position: relative;
			z-index: 10;
			top: -150rpx;

			.gooodList {
				padding: 20rpx;
				background-color: #FFF;
				border-radius: 16rpx;

				.r_warp {
					height: 200rpx;
					.sall {
						color: $color-tt;
						background-color: rgba($color-tt, 0.2);
						padding: 8rpx 14rpx;
						border-radius: 12rpx;
					}

					.amount_tag {
						color: $color-tt;
					}

					.price {
						color: $font-money-color;
					}
					.price_o{
						color: #CCC;
					}

					.buyNow {
						background-image: linear-gradient(to bottom, #fbc64a, #fa9527);
						padding: 10rpx 40rpx;
						border-radius: 22rpx;
						color: #FFF;

						&.disable {
							background-image: none;
							background-color: rgba($font-color2, 0.3);
						}
					}
				}
			}

		}
	}
</style>
