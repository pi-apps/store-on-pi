<template>
	<view class="myFans">
		<!-- 自定义导航 -->
		<uniNav type="search" class="dropMenu" placeholder="通过手机号搜索粉丝" @search="storeMainListKeyWord"
			@clear="storeMainListKeyWord" />

		<view class="fansList -warp30">
			<LoadMoreList :page="page" :data="page.list">
				<view class="mb16 fansWarp -bgf -warp -box" v-for="item in page.list" :key="item.userId">
					<view class="fz12 colors f f_align_center">
						<u-icon class="mr4" name="clock" color="#999" />注册时间：{{item.createTime}}
					</view>
					<view class="mt12 f f_align_center">
						<u--image radius="12" width="54" height="54" :src="item.userHeadUrl" />
						<view class="f_column f ml20 f_align" style="height: 60px;">
							<view class="f f_align_center">
								<text class="overfont fz16">{{item.nickName || 'PI商店用户'}}</text>
								<view v-if="item.achievementEntity && item.achievementEntity.saleUserType"
									class="f f_align_center userTag f_baseline ml12">
									<u--image class="mr4 f_c_c"
										:src="'/static/image/user/'+useType[item.achievementEntity.saleUserType]"
										width="16" height="16" />
									<text>{{$getDict(sale_user_type,item.achievementEntity.saleUserType)}}</text>
								</view>
							</view>
							<text class="fz12 tt_h5">晋升值（总）：{{item.achievementEntity.achievementTotal}}</text>
							<text class="fz12 tt_h5">个人晋升值：{{item.achievementEntity.achievementPersonal}}</text>
						</view>
					</view>
					<view class="f_c_a mt10 bb_info">
						<text>{{$phoneFormat(item.mobile)}}</text>
						<view class="bb_middle"></view>
						<view class="f f_align_center">
							<text>邀请码：{{item.invitation}}</text>
							<u-icon @click="$copy(item.invitation)" class="ml4"
								name="/static/packageB/image/fuzhi@2x.png" />
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
		_mySubRecommendList
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				mobile: '',
				page: {
					pageIndex: 1,
					limit: 10,
					totalPage: 1,
					isLoad: false,
					isDone: false,
					list: [],
				},
				useType: {
					"10009001": 'yingzi.png',
					"10009002": 'chezi.png',
					"10009003": 'yundou.png',
				}
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
			const _this = this;
			// h5原生方法调用回调
			window['appbackJScopyToPasteboard'] = (code) => {
				if (code == 200) {
					_this.$showToast('复制成功！');
				}
			}
		},
		onLoad(options) {
			this.$store.dispatch('getDictlists', 'sale_user_type')
		},
		onShow() {
			this.init();
		},
		methods: {
			init() {
				this.page.pageIndex = 1;
				this.getFansList();
			},
			//
			storeMainListKeyWord(val) {
				this.page.pageIndex = 1;
				this.mobile = val;
				this.getFansList();
			},
			// 
			getFansList() {
				this.page.isLoad = true
				const {
					pageIndex,
					limit
				} = this.page;
				_mySubRecommendList({
					page: pageIndex,
					limit: limit,
					mobile: this.mobile
				}).then(res => {
					this.page.isLoad = false;
					if (res && res.code == 0) {
						let lists = res.data && res.data.list;
						this.page.totalPage = res.data.totalPage || 0;
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
				this.getFansList();
			} else {
				this.page.isDone = true
			}
		},
	}
</script>

<style lang="scss" scoped>
	.myFans {
		min-height: 100vh;

		.fansWarp {
			color: #292929;

			.userTag {
				padding: 4rpx 16rpx;
				background-color: #fff;
				box-shadow: 0 0 8rpx rgba(#F7694A, 0.14);
				border-radius: 28rpx;
				font-size: 20rpx;
				color: #A3A3A3;
			}

			.tt_h5 {
				color: #969696;
			}

			.bb_info {
				font-size: 26rpx;
				padding: 0 24rpx;

				.bb_middle {
					width: 2rpx;
					height: 20rpx;
					background-color: rgba(#707070, 0.5);
				}
			}
		}
	}
</style>
