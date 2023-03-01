<template>
	<view class="user">
		<!-- 顶部透明导航 -->
		<nav-opacity :showBack="false" ref="opacityNav" :title="$t('tabBar.user')" />
		<view class="top_user f_c_c f_column">
			<view class="w100 --status" :style="{height:statusBarHeight+30+'px'}"></view>
			<!-- 顶部设置入口 -->
			<!-- <view class="f_c_e w100 top_status">
				<u--image @click="isLoginNavTo('/pages/packageA/setting/setting')" width="24" height="24"
					src="/static/image/user/shezhi@3x.png" />
			</view> -->
			<view class="f_c_a userMain" v-if="userInfo.userId != ''">
				<view class="f f_align_center">
					<view class="userHead mr20 f_c_c">
						<u-avatar mode="aspectFill" class="f_c_c f_shrink0" :src="userInfo.userHeadUrl" size="64" />
					</view>
					<view class="r_user_name f f_column f_align">
						<text style="width: 160px;"
							class="fz18 bold overfont">{{userInfo.userName}}</text>
						<!-- <view class="f f_align_center userTag f_baseline">
							<u--image class="mr4 f_c_c"
								:src="'/static/image/user/'+useType[userInfo.achievementEntity.saleUserType]" width="18"
								height="18" />
							<text>{{$getDict(sale_user_type,userInfo.achievementEntity.saleUserType)}}</text>
						</view> -->
					</view>
				</view>
				<u--image @click="isLoginNavTo('/pages/packageA/sign/sign')" width="142rpx" height="56rpx"
					src="/static/image/user/sign@3x.png" />
			</view>
			<view class="f f_align_center userMain" v-else @click="getUserAuth()">
				<view class="userHead mr20 f_c_c">
					<u-avatar class="f_c_c f_shrink0" src="/static/image/logo.png" size="64" />
				</view>
				<view class="f f_column">
					<text class="fz18 bold">{{$t('page.user.login')}}</text>
					<text class="fz12 mt8">{{$t('page.user.login1')}}</text>
				</view>
			</view>
			<!--  -->
			<view class="userInfo f_c_a fz12 mt12">
				<view class="f f_column f_align_center">
					<text class="fz16 mb4">
						<u-count-to duration="1000" color="#292929" :endVal="userInfo.amountSigninPoints"
							separator=","></u-count-to>
					</text>
					<text>{{$t('pages.user.point')}}</text>
				</view>
				<view class="f f_column f_align_center">
					<text class="fz16 mb4">{{userInfo.balance.toFixed(8)/1}}</text>
					<text>{{$t('pages.user.amount')}}</text>
				</view>
			</view>
		</view>

		<!-- 我的订单 -->
		<view class="-warp20-30 f f_column">
			<!-- 特权专区 -->
			<!-- <view class="f_c_a syswarp w100" style="padding: 0;">
				<u--image @click="isLoginNavTo('/pages/packageA/vip/vip')" width="690rpx" height="122rpx"
					src="/static/image/user/chebaoquanyi@2x.png" />
			</view> -->
			<!--我的系统的订单信息 -->
			<view class="syswarp w100 -bgf mt12 noRadius">
				<view class="f f_warp orderItem w100">
					<view class="o_ico_row_sys f f_column f_align_center fz12" v-for="(item,index) in orderItem"
						:key="index" @click="doNavTo(item)">
						<u--image class="f_c_c mb8" :src="item.icon" width="31" height="30" />
						<text>{{$t('pages.user.order')}}</text>
					</view>
				</view>
			</view>

			<!-- 我的收益 -->
			<!-- <template v-if="userInfo.saleUserType != '10009001'">
				<view class="syswarp w100 -bgf mt12">
					<view class="ml12 fz16 bold">我的收益</view>
					<view class="f_c_a mySylones">
						<view class="f_c_c f_column sy_item">
							<u-count-to bold duration="1000" fontSize="18" color="#292929"
								:endVal="userInfo.achievementTotal" separator="," :decimals="2">
							</u-count-to>
							<text class="mt8">晋升值(总)</text>
						</view>
						<view class="f_c_c f_column sy_item"
							@click="isLoginNavTo('/pages/packageB/myNextStore/myNextStore')">
							<u-count-to bold duration="1000" fontSize="18" color="#292929" :endVal="userStoreNumber"
								separator=",">
							</u-count-to>
							<text class="mt8">我的拓店</text>
						</view>
						<view class="f_c_c f_column sy_item" @click="isLoginNavTo('/pages/packageB/myFans/myFans')">
							<u-count-to bold duration="1000" fontSize="18" color="#292929" :endVal="userFansNumber"
								separator=",">
							</u-count-to>
							<text class="mt8">我的粉丝</text>
						</view>
						<view class="f_c_c f_column sy_item bl"
							@click="isLoginNavTo('/pages/packageA/myIncome/myIncome')">
							<u--image src="/static/image/user/quanbuzichan@3x.png" width="30" height="25" />
							<text class="mt8">全部</text>
						</view>
					</view>
				</view>
			</template> -->
			<!-- 系统功能 -->
			<!-- <view class="ml10 fz16 mt16 bold">常用功能</view>
			<view class="syswarp w100 f f_warp mt12  -bgf" style="padding-top: 0;">
				<view class="o_ico_row f f_column f_align_center fz12 mt12" v-for="(item,index) in sysItem" :key="index"
					@click="doNavTo(item)">
					<u--image class="f_c_c mb8" :src="item.icon" width="30" height="30" />
					<text>{{ item.title }}</text>
				</view>
			</view> -->
			
			
			<!-- 商家板块 -->
			<!-- <view v-if="userInfo && userInfo.isStore && userInfo.isStore == '0'" class="syswarp w100 -bgf mt20">
				<view class="ml20 fz16 bold">门店管理</view>
				<view class="store_main f f_align_center">
					<u--image class="f_c_c" :src="storeInfo.doorPhotoUrl" shape="circle" width="45" height="45" />
					<u--image v-if="storeInfo.authTagId == '10021001'" class="f_c_c v163_tip"
						src="/static/image/user/163v@2x.png" width="86" height="44" />
					<u--image v-else class="f_c_c v163_tip" src="/static/image/user/putong@2x.png" width="86"
						height="44" />
					<view class="ml12 overfont f f_column">
						<text class="overfont title bold">{{storeInfo.storeName}}</text>
						<text class="overfont mt4 fz12">{{storeInfo.address}}</text>
					</view>
				</view>
				<view class="f f_warp w100 mt12">
					<view class="o_ico_row f f_column f_align_center fz12 mt12" v-for="(item,index) in storeItem"
						:key="index" @click="doNavTo(item)">
						<u--image class="f_c_c mb8" :src="item.icon" width="30" height="30" />
						<text>{{ item.title }}</text>
					</view>
				</view>
			</view>
			<view v-else class="syswarp w100 -bgf mt20" @click="isLoginNavTo('/pages/packageA/applyShop/index')">
				<view class="ml20 fz16 bold">门店管理</view>
				<view class="store_main f f_align_center">
					<u--image class="f_c_c" src="/static/image/logo.png" shape="circle" width="45" height="45" />
					<view class="ml12 overfont f f_column">
						<text class="overfont title bold">未入驻</text>
						<text class="overfont mt4 fz12">申请入驻</text>
					</view>
				</view>
				<view class="store_main">
					<u--image class="f_c_c" radius="4" src="/static/image/user/into@2x.png" width="600rpx"
						height="124rpx" />
				</view>
			</view> -->
		</view>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import mixin from '@/utils/mixin/mixin.js'
	import {
		_getStoreInfo
	} from '@/http/apiReq.js'
	export default {
		name: 'user',
		mixins: [mixin],
		data() {
			return {
				userInfo: {
					userId: '',
					amountSigninPoints: 0,
					balance:0,
					userHeadUrl: "",
					userName: ""
				},
				orderItem: [{
						title: '我的订单',
						icon: '/static/image/user/wodedingdan@2x.png',
						isLogin: true,
						url: '/pages/packageB/storeOrder/storeOrder'
					},
				],
				sysItem: [
					{
						title: '设置',
						icon: '/static/image/user/shezhi@2x.png',
						isLogin: true,
						url: '/pages/packageA/setting/setting'
					},
				],
				storeItem: [{
						title: '我的门店',
						icon: '/static/image/user/s_store@2x.png',
						isLogin: true,
						url: '/pages/packageC/storeMange/storeMange'
					},
					{
						title: '商品管理',
						icon: '/static/image/user/s_goods@2x.png',
						isLogin: true,
						url: '/pages/packageC/goodMange/goodMange'
					},
					{
						title: '门店订单',
						icon: '/static/image/user/wodedingdan@2x.png',
						isLogin: true,
						url: '/pages/packageC/storeOrder/storeOrder'
					},
					{
						title: '核销',
						icon: '/static/image/user/s_scan@2x.png',
						isLogin: true,
						url: '/pages/packageC/scan/scan'
					}
				],
				userFansNumber: 0,
				userStoreNumber: 0,
				useType: {
					"10009001": 'yingzi.png',
					"10009002": 'chezi.png',
					"10009003": 'yundou.png',
				},
				// 新增
				good_tj_vip: []
			};
		},
		components: {
			'nav-opacity': () => import('@/components/nav-opacity/nav-opacity.vue')
		},
		computed: {
			...mapState({
				statusBarHeight: state => state.statusBarHeight,
				sale_user_type: state => state.dictArrs.sale_user_type
			})
		},
		onLoad() {
			this.$store.dispatch('getDictlists', 'sale_user_type');
		},
		onShow() {
			this.getUserInfo();
		},
		methods: {
			// 查询用户信息
			getUserInfo() {
				this.userInfo = uni.getStorageSync('UserInfo') || {
					userId: '',
					amountSigninPoints: 0,
					balance:0,
					userHeadUrl: "",
					userName: ""
				};
				this.$getUserInfoSync(false, false).then(res => {
					if (res && res.code == 0) {
						this.userInfo = res.data;
					} else if (res.code == 401) {
						this.userInfo = {
							userId: '',
							amountSigninPoints: 0,
							balance:0,
							userHeadUrl: "",
							userName: ""
						}
					}
				})
			},
			// 登录
			isLoginNavTo(url) {
				if (this.$loginModel('/pages/user/user',()=>{
					this.getUserInfo();
				})) {
					this.$navTo(url)
				}
			},
			// 验证登录
			doNavTo(item) {
				console.log(item)
				if (item.isLogin) {
					this.isLoginNavTo(item.url);
				} else {
					this.$navTo(item.url);
				}
			},
			getUserAuth(){
				this.$loginModel('/pages/user/user',()=>{
					this.getUserInfo();
				})
			}
		},
		onPageScroll(e) {
			// 透明导航
			this.$nextTick(() => {
				this.$refs.opacityNav.navScorllPage(e);
			})
		}
	}
</script>

<style lang="scss" scoped>
	.user {
		min-height: 100vh;
		padding-bottom: 140rpx;
		box-sizing: border-box;
		background-color: #F5F5F5;

		.goods_clasify_main {
			padding: 36rpx 20rpx 40rpx;
		}

		.top_user {
			padding: 20rpx 30rpx;
			background: url('@/static/image/user/user_bg@3x.png') no-repeat center;
			background-size: 100% 100%;
			color: #292929;

			.top_status {
				box-sizing: border-box;
				padding: 0 0 12rpx 0;
			}

			.userMain {
				color: #292929;
				width: 100%;

				.r_user_name {
					height: 120rpx;

					.userTag {
						padding: 8rpx 16rpx;
						background-color: #fff;
						box-shadow: 0 0 8rpx rgba(#F7694A, 0.14);
						border-radius: 28rpx;
						font-size: 20rpx;
						color: #A3A3A3;
					}
				}
			}

			// 
			.userInfo {
				box-sizing: border-box;
				width: 100%;
				color: #292929;
				padding: 20rpx 30rpx;
			}
		}

		// 
		.syswarp {
			padding: 30rpx 0;
			border-radius: 24rpx;

			&.noRadius {
				border-radius: 24rpx 24rpx 0 0;
			}


			.mySylones {
				padding: 30rpx;
				color: #8C8C8C;

				.sy_item {
					width: 25%;

					&.bl {
						border-left: 4rpx solid #F5F5F5;
					}
				}
			}

			.o_ico_row {
				width: 25%;
			}

			.o_ico_row_sys {
				width: 20%;
			}
		}

		.store_main {
			padding: 22rpx 40rpx 0;
			color: #818181;
			position: relative;

			.title {
				color: #292929;
				width: 400rpx;
			}

			.v163_tip {
				position: absolute;
				right: -35rpx;
				top: -50rpx;
			}
		}
	}
</style>
