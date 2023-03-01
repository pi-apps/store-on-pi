<template>
	<view class="vip">
		<!-- 自定义导航 -->
		<uniNav title="权益充值" />
		<view class="top_banner_main">
			<view class="f_s_a userInfos">
				<view class="f f_align_center">
					<view class="userHead mr8 f_c_c">
						<u--image class="f_c_c f_shrink0" :src="userInfo.userHeadUrl" width="52" height="52"
							shape="circle" />
					</view>
					<view class="r_user_name f f_column f_align">
						<text style="width: 160px;"
							class="fz16 overfont">{{userInfo.nickName ? userInfo.nickName : 'PI商店用户'}}</text>
						<view class="f f_align_center userTag f_baseline">
							<u--image class="mr4 f_c_c"
								:src="'/static/image/user/'+useType[userInfo.achievementEntity.saleUserType]" width="18"
								height="18" />
							<text>{{$getDict(sale_user_type,userInfo.achievementEntity.saleUserType)}}</text>
						</view>
					</view>
				</view>
				<view class="right_xy f_c_e" @click="xyJumps">
					<text>权益协议</text>
					<u-icon size="12" color="#65452F" name="arrow-right" />
				</view>
			</view>
		</view>

		<!--  -->
		<view class="vip_is_now">
			<view class="vip_title f f_align_center">尊享四大特权</view>
			<scroll-view style="width: 750rpx;" :scroll-x="true" :show-scrollbar="false">
				<view class="f f_align_center">
					<u--image class="ml20" src="/static/packageA/image/vip/car.png" radius="8" width="87"
						height="109" />
					<u--image class="ml20" src="/static/packageA/image/vip/seckill.png" radius="8" width="87"
						height="109" />
					<u--image class="ml20" src="/static/packageA/image/vip/sign.png" radius="8" width="87"
						height="109" />
					<u--image class="ml20" src="/static/packageA/image/vip/interger.png" radius="8" width="87"
						height="109" />
				</view>
			</scroll-view>
		</view>
		<!--  -->
		<view class="top_banner -bgf" @click="jumpMoreVip">
			<u--image src="/static/packageA/image/vip/vip1.png" width="100%" height="99" />
		</view>
		<!--  -->
		<view v-for="(items,index) in vipGoodList" :key="index">
			<view class="service mt12" v-if="items.list.length && items.list.length > 0">
				<view class="f_c_c mb12 fz20 t_bar">·<text class="bar">{{items.title}}</text>·</view>
				<scroll-view :scroll-x="true">
					<view class="f f_align_center" style="padding-bottom: 5px;">
						<view v-for="(item,ins) in items.list" :key="ins"
							:class="'goods_item '+(goods.goodsId == item.goodsId ? 'active' : '')"
							@click="selectGoods(item)">
							<u--image :src="item.goodsImgList[0]" width="128" height="128" radius="10" />
							<view class="r_goods_info f f_column overfont">
								<text class="fz14 bold overfont mt8">{{item.goodsName}}</text>
								<view class="jf_warp f_baseline mt4">赠送{{item.amountPoints}}PI积分</view>
								<view class="f f_align_center price">
									<view class="fz20"><text class="fz12">π</text>{{item.discountPrice.toFixed(2)}}
									</view>
									<view class="textLineThrough fz12 ml8">π{{item.price}}</view>
								</view>
							</view>
							<u--image v-if="goods.goodsId == item.goodsId" class="f_c_c icon_tip"
								src="/static/packageA/image/vip/change.png" width="46" height="46" />
						</view>
					</view>
				</scroll-view>
			</view>
		</view>
		<view class="buttom_buy" v-if="goods.goodsId">
			<u-button class="f_c_c buy" @click="vipGoodsRunDetail">
				<text class="mr12"><text class="fz12">π</text>{{ goods.discountPrice }}</text><text>立即开通</text>
			</u-button>
			<image class="tip_btn" src="/static/packageA/image/vip/vip@2x.png" />
		</view>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		_vipListGoods
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				vipGoodList: [{
						title: '超值权益包',
						list: []
					},
					{
						title: '商城权益品',
						list: []
					},
					{
						title: '门店权益品',
						list: []
					}
				],
				vipGoods: [],
				vipStoreGoods: [],

				goods: {
					goodsId: ''
				},
				latitude: '',
				longitude: '',

				userInfo: {
					userId: '',
					userHeadUrl: "",
					nickName: "",
					achievementEntity: {
						saleUserType: '10009001'
					}
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
		onLoad(options) {
			this.$store.dispatch('getDictlists', 'sale_user_type');
			this.init();
		},
		mounted() {
			const _this = this;
			//#ifdef H5  
			// //监听获取定位是否成功
			window['appbackJSgetLocation'] = (code) => {
				let res = JSON.parse(code);
				_this.latitude = res.latitude;
				_this.longitude = res.longitude;
				// 查询油站
				_this.getVipGoodList();
			}
			// #endif
		},
		onShow() {

		},
		methods: {
			// 
			init() {
				// 连调打开
				// this.getVipGoodList();
				//h5获取定位
				this.$getLocation();
				this.getUserInfo();
			},
			// 查询用户信息
			getUserInfo() {
				let userInfo = uni.getStorageSync('UserInfo');
				if(userInfo != ''){
					this.userInfo = userInfo;
				}
				this.$getUserInfoSync(true, false).then(res => {
					if (res && res.code == 0) {
						this.userInfo = res.data;
					}
				});
			},
			// 查询特权商品
			getVipGoodList() {
				_vipListGoods({
					limit: 100,
					page: 1,
					latitude: this.latitude,
					longitude: this.longitude,
					// 联调测试
					// latitude: '30.68144',
					// longitude: '103.8559',
				}).then(res => {
					if (res && res.code == 0) {
						this.vipGoodList[0].list.splice(0)
						this.vipGoodList[1].list.splice(0)
						this.vipGoodList[2].list.splice(0)

						let list = res.data.list || [];

						for (let i = 0; i < list.length; i++) {
							if (list[i].goodsChannelType == '1') {
								this.vipGoodList[2].list.push(list[i])
							} else if (list[i].goodsChannelType == '3') {
								this.vipGoodList[0].list.push(list[i])
							} else if (list[i].goodsChannelType == '0') {
								this.vipGoodList[1].list.push(list[i])
							}
						}

						if (this.vipGoodList[0].list.length && this.vipGoodList[0].list.length > 0) {
							this.goods = this.vipGoodList[0].list[0];
						} else if (this.vipGoodList[1].list.length && this.vipGoodList[1].list.length > 0) {
							this.goods = this.vipGoodList[1].list[0];
						} else if (this.vipGoodList[2].list.length && this.vipGoodList[2].list.length > 0) {
							this.goods = this.vipGoodList[2].list[0];
						}
					}
				})
			},
			//选择商品
			selectGoods(item) {
				this.goods = item
			},
			// 特权商品 type = 3
			vipGoodsRunDetail() {
				const {
					goodsId,
					goodsChannelType
				} = this.goods;
				if (goodsId && goodsId != '') {
					if (goodsChannelType == '1') {
						this.$navTo(`../vipStoreDetail/vipStoreDetail?goodsId=${goodsId}&type=10002010`)
					} else if (goodsChannelType == '3') {
						this.$navTo(`../vipStoreDetail/vipStoreDetail?goodsId=${goodsId}&type=10002023`)
					} else {
						this.$navTo(`../goodDetail/goodDetail?goodsId=${goodsId}&type=3`)
					}
				} else {
					this.$showToast('请选择权益商品')
				}
			},
			xyJumps() {
				this.$navTo('../xy/xy?id=10010008')
			},
			jumpMoreVip() {
				this.$navTo('../moreVips/moreVips')
			}
		}
	}
</script>

<style lang="scss" scoped>
	.vip {
		min-height: 100vh;
		background-color: $bg-color-fa;
		padding-bottom: 260rpx;

		.top_banner {
			padding: 30rpx 30rpx 0;
			background-color: #fff;
		}

		.top_banner_main {
			margin-top: 30rpx;
			position: relative;
			background: url('@/static/packageA/image/vip/vip.png') no-repeat center;
			background-size: 100% 100%;
			height: 268rpx;
			box-sizing: border-box;

			.userInfos {
				width: 100%;
				box-sizing: border-box;
				padding: 60rpx 80rpx 0;

				.r_user_name {
					height: 104rpx;
					color: #65452F;

					.userTag {
						padding: 4rpx 16rpx;
						background-color: #FFF3DA;
						box-shadow: 0 0 8rpx rgba(#FFF3DA, 0.14);
						border-radius: 28rpx;
						font-size: 20rpx;
						color: #BE9964;
					}
				}

				.right_xy {
					color: #65452F;
					font-size: 24rpx;
				}
			}
		}

		.vip_is_now {
			background-color: #fff;
			padding: 20rpx 0;
			position: relative;
			z-index: 10;
			margin-top: -80rpx;

			.vip_title {
				margin-left: 40rpx;
				margin-bottom: 30rpx;
				font-size: 30rpx;

				&::before {
					content: "";
					width: 8rpx;
					height: 35rpx;
					background-color: #F7694A;
					margin-right: 10rpx;
					border-radius: 24rpx;
				}
			}
		}

		.service {
			padding: 30rpx;
			background-color: #fff;

			.t_bar {
				color: #939393;
			}

			.bar {
				color: #F16E4D;
			}

			.s_item {
				border-radius: 12rpx;
				padding: 24rpx 30rpx;
				background-color: $bg-color-fa;
				margin-top: 12rpx;

				.tips {
					color: $font-color2;
				}
			}

			.goods_item {
				width: 296rpx;
				box-sizing: border-box;
				padding: 20rpx 20rpx 10rpx;
				box-sizing: border-box;
				box-shadow: 0 6rpx 12rpx rgba(#000, 0.07);
				border-radius: 20rpx;
				color: $font-color3;
				position: relative;
				margin-left: 20rpx;
				border: solid 1rpx #FFF;

				&:first-child {
					margin-left: 0;
				}

				.r_goods_info {
					.price {
						color: $font-money-color2;
					}

					.jf_warp {
						padding: 6rpx 26rpx;
						border-radius: 8rpx;
						font-size: 20rpx;
						color: #FFAA35;
						border: solid 1rpx #FFAA35;
					}

					.textLineThrough {
						color: #DFDFDF;
					}
				}

				&.active {
					border-color: #fbd624;
				}

				.icon_tip {
					position: absolute;
					bottom: 0;
					right: 0;
				}
			}
		}

		.buttom_buy {
			width: 100%;
			box-sizing: border-box;
			padding: 20rpx 30rpx 80rpx;
			background-color: #fff;
			position: fixed;
			bottom: 0;

			.tip_btn {
				position: absolute;
				right: 34rpx;
				top: 0rpx;
				width: 224rpx;
				height: 40rpx;
				z-index: 100;
			}

			.buy {
				width: 690rpx;
				height: 100rpx;
				color: #512416;
				box-sizing: border-box;
				background-color: #E5CA91;
				border-radius: 12rpx;
				font-size: 36rpx;
				position: relative;

			}
		}
	}
</style>
