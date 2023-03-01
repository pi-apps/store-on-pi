<template>
	<view class="confirm">
		<!-- 自定义导航 -->
		<uniNav title="确认订单" />

		<view v-if="options.goodsType != '10002010' && options.goodsType != '10002023' && (options.goodsType != '3' ||  goodInfo.isPhysicalGoods == '0')"
			class="-warp30 -bgf">
			<view class="f_c_a address" @click="jumpAddress">
				<u--image src="/static/packageA/image/icon_dingwei@2x.png" width="24" height="24" />
				<view class="f_c_a w100 ml12">
					<view v-if="address && address.addrId  && address.addrId != ''" class="f f_column">
						<view class="f f_align_center">
							<text>{{address.userName}}</text>
							<text class="ml8">{{address.userMobile}}</text>
						</view>
						<view class="overfont-2 mt4">
							{{address.provinceName}}{{address.cityName}}{{address.districtName}}{{address.addrDetail}}
						</view>
					</view>
					<text v-else class="place">选择收货地址</text>
					<i class="iconfont icon-xiangyou1"></i>
				</view>
			</view>
		</view>
		<view class="-warp30" v-if="goodInfo.storeOrderByList && goodInfo.storeOrderByList.length">
			<!-- 门店特权 -->
			<view class="store_list -warp mb8 -bgf fz12"
				v-if="(options.goodsType == '10002010' || options.goodsType == '10002023') && goodInfo.storeOrderByList.length > 0">
				<view class="f_c_a">
					<text class="fz16 bold">适用门店</text>
					<view class="f_c_e" @click="doUseStore">
						<text>{{goodInfo.storeOrderByList.length}}家门店适用</text>
						<u-icon class="ml4" name="arrow-right" size="12" color="#C4C4C4" />
					</view>
				</view>
				<view class="f_c_a">
					<view class="f f_align_center mt8">
						<u--image radius="5" width="50" height="50" :src="goodInfo.storeOrderByList[0].doorPhotoUrl" />
						<view class="f f_column ml8">
							<text class="overfont fz14 bold">{{goodInfo.storeOrderByList[0].storeName}}</text>
							<view
								:class="'storeStatus f_baseline mt8 '+(goodInfo.storeOrderByList[0].status == '0' ? '' : 'isDown')">
								{{goodInfo.storeOrderByList[0].status == '0' ? '营业中' : '休息中'}}
							</view>
						</view>
					</view>
					<u--image @click="callphone" src="/static/packageA/image/dianhua@2x.png" width="40" height="40" />
				</view>
				<view class="f_c_a fz12 mt12 b_b_infos">
					<view class="tip_c2">最近{{(goodInfo.storeOrderByList[0].distance/1000).toFixed(2)}}KM</view>
					<view class="tip_tag"></view>
					<text style="width: 160px;" class="overfont tip_c">{{goodInfo.storeOrderByList[0].address}}</text>
					<view class="f_c_e" @click="jumpMapNavgation">导航
						<u-icon class="ml4" name="arrow-right" size="12" color="#C4C4C4" />
					</view>
				</view>
			</view>
		</view>
		<!--  -->
		<view class="-warp -bgf">
			<view v-if="options.goodsType != '10002010'" class="f f_align_center overfont">
				<u--image class="f_c_c" src="/static/image/mall/store-icon.png" width="24" height="24" />
				<!-- <i class="iconfont icon-xiangyou1 mr4"></i> -->
				<text class="overfont">PI商店自营店</text>
				<!-- <text class="overfont">{{goodInfo.providerName}}</text> -->
			</view>
			<view class="goodInfo mt20 f_c_a">
				<u--image class="mr12 f_shrink0 f_c_c" :src="goodInfo.goodsImgList?goodInfo.goodsImgList[0]: ''"
					width="100" height="100" radius="4" />
				<view class="f f_column f_align overfont w100">
					<text class="overfont-2">{{goodInfo.goodsName}}</text>
					<view class="sku mt8 f_baseline overfont">规格：
						<text v-if="options.skuName && options.skuName.length > 0">{{options.skuName.join('/')}}</text>
						<text v-else>无</text>
					</view>
					<view class="f_c_a mt4">
						<text v-if=" options.goodsType == '2' " class="fz12 price">π <text
								class="fz20">{{(killInfo.price).toFixed(2)}}</text></text>
						<template v-else>
							<text
								v-if="userInfo.achievementEntity.saleUserType != '10009001' && goodInfo.vipPrice && goodInfo.vipPrice > 0"
								class="fz12 price">π <text class="fz20">{{(goodInfo.vipPrice).toFixed(2)}}</text></text>
							<text v-else class="fz12 price">π <text
									class="fz20">{{(goodInfo.discountPrice).toFixed(2)}}</text></text>
						</template>

						<text>*{{options.num}}</text>
					</view>
				</view>
			</view>
			<!-- <van-field v-model="remark" label="留言备注" type="textarea" class="remark" placeholder="请输入备注信息" autosize maxlength="{{ 30 }}" border="{{ false }}" input-align="right" /> -->
			<view class="f f_align_center mt20">
				<view>留言备注</view>
				<u--textarea border="none" confirmType="next" v-model="remark" count :maxlength="40"
					placeholder="请输入备注信息" autoHeight></u--textarea>
			</view>
		</view>
		<!-- 下单信息 -->
		<view class="-warp20-30 -bgf mt12">
			<view class="ceil f_c_a">
				<view class="ceil-able">商品总价</view>
				<view>π{{(goodInfo.discountPrice * options.num).toFixed(2)}}</view>
			</view>
			<view class="ceil f_c_a">
				<view class="ceil-able">配送费用</view>
				<view>{{goodInfo.expressPrice <= 0 ? '商家免邮' : 'π' +goodInfo.expressPrice}}</view>
			</view>
			<!-- PI积分抵扣 -->
			<view class="ceil f_c_a" v-if="goodInfo.amountPoints > 0 && options.goodsType == '1'">
				<view class="ceil-able f f_align_center">
					<text>PI积分</text>
					<u--image src="/static/packageA/image/icon_zhuyi1@2x.png" width="15" height="15"
						class="f_c_c ml4 mr4" />
					<text
						class="fz12 tips">已有{{userInfo.accountEntity.amountPoints}}，满{{(goodInfo.amountPoints * options.num).toFixed(2)}}可使用</text>
				</view>
				<u-switch :disabled="loading" v-model="ifIntergal" activeValue="0" inactiveValue="1"
					active-color="#EC661B" @change="onChange($event,'amountPoints')" size="20" />
			</view>
			<!-- 权益积分抵扣 -->
			<view class="ceil f_c_a" v-if="goodInfo.legalRightIntegral >= 100 && options.goodsType == '1' ">
				<view class="ceil-able f f_align_center">
					<text>权益积分</text>
					<u--image src="/static/packageA/image/icon_zhuyi1@2x.png" width="15" height="15"
						class="f_c_c ml4 mr4" />
					<text
						class="fz12 tips">已有{{userInfo.accountEntity.legalRightIntegral}}，满{{(goodInfo.legalRightIntegral * options.num).toFixed(2)}}可使用</text>
				</view>
				<u-switch :disabled="loading" v-model="ifIntergal" activeValue="2" inactiveValue="1"
					active-color="#EC661B" @change="onChange($event,'legalRightIntegral')" size="20" />
			</view>
		</view>

		<!-- 立即购买 -->
		<view class="buynow -bgf w100 f_c_a">
			<view>
				合计 <text class="fz24 mb4 -price">
					<text class="fz12">π</text>
					<text class="bold">
						<u-count-to duration="1000" color="#e71c19" :endVal="payAmount" separator="," :decimals="2">
						</u-count-to>
					</text>
				</text>
			</view>
			<u-button :loading="loading" loading-text="下单中..." :disabled="loading" class="buyNowBtn"
				@click="showActionSheel">提交订单</u-button>
		</view>

	</view>
</template>

<script>
	import {
		_apiGoodsInfo,
		_apiGoodsVipDetail,
		_apiGoodsSeckillInfo,
		_apiGetShowAddress,
		_createMallTradeOrder,
		_createVipGoodsOrder,
		_createSeckillTradeOrder
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				loading: false,
				address: {
					addrId: '',
				},
				userInfo: {
					accountEntity: {
						amountPoints: 0
					},
					achievementEntity: {
						saleUserType: '10009001'
					}
				},
				killInfo: {
					seckillTimeSeconds: 0,
					crazyModel: '1',
					inventory: 0,
					price: 0
				},
				goodInfo: {
					discountPrice: 0,
					expressPrice: 0,
					storeOrderByList: []
				},
				options: {
					goodsId: '',
					goodsType: '1'
				},
				remark: '',
				ifIntergal: '1',
				latitude: '',
				longitude: ''
			};
		},
		computed: {
			payAmount: function() {
				console.log(this.options, this.ifIntergal)
				if (this.options.goodsType == '1') {
					// 价格差异化  抵扣
					let saleUserType = this.userInfo.achievementEntity.saleUserType;
					if (this.ifIntergal == '0') {
						if (saleUserType != '10009001' && this.goodInfo.vipPrice && this.goodInfo.vipPrice > 0) {
							return ((this.goodInfo.vipPrice * this.options.num) + this.goodInfo.expressPrice - (this
								.goodInfo.amountPoints * this.options.num)).toFixed(2)
						} else {
							return ((this.goodInfo.discountPrice * this.options.num) + this.goodInfo.expressPrice - (
								this.goodInfo.amountPoints * this.options.num)).toFixed(2)
						}
					} else if (this.ifIntergal == '2') {
						if (saleUserType != '10009001' && this.goodInfo.vipPrice && this.goodInfo.vipPrice > 0) {
							return ((this.goodInfo.vipPrice * this.options.num) + this.goodInfo.expressPrice - ((this.goodInfo.legalRightIntegral / 100) * this.options.num)).toFixed(2)
						} else {
							return ((this.goodInfo.discountPrice * this.options.num) + this.goodInfo.expressPrice - ((
								this.goodInfo.legalRightIntegral / 100) * this.options.num)).toFixed(2)
						}
					} else {
						if (saleUserType != '10009001' && this.goodInfo.vipPrice && this.goodInfo.vipPrice > 0) {
							return ((this.goodInfo.vipPrice * this.options.num) + this.goodInfo.expressPrice).toFixed(
								2)
						} else {
							return ((this.goodInfo.discountPrice * this.options.num) + this.goodInfo.expressPrice)
								.toFixed(2)
						}
					}
				} else if (this.options.goodsType == '2') {
					return ((this.killInfo.price * this.options.num) + this.goodInfo.expressPrice).toFixed(2)
				} else {
					return ((this.goodInfo.discountPrice * this.options.num) + this.goodInfo.expressPrice).toFixed(2)
				}
			}
		},
		mounted() {
			// h5原生方法调用回调
			const _this = this;
			//#ifdef H5  
			// //监听获取定位是否成功
			window['appbackJSgetLocation'] = (code) => {
				let res = JSON.parse(code);
				_this.latitude = res.latitude;
				_this.longitude = res.longitude;

				_this.init()
			}
			// #endif
		},
		onLoad(options) {
			if (options.params) {
				//秒杀商品时 goodsId 为秒杀ID
				let params = JSON.parse(decodeURIComponent(options.params));
				this.options = params;
				this.getUserInfo();
				let addressInfo = wx.getStorageSync('addressInfo');
				if (!addressInfo || addressInfo == '') {
					this.getAddressInfo();
				}
				// 查询商品信息 联调打开
				// this.init();
			}
			this.getLocation();
		},
		onShow() {
			let addressInfo = uni.getStorageSync('addressInfo') || undefined;
			if (addressInfo && addressInfo.addrId) {
				this.address = addressInfo
			}
		},
		methods: {
			// 商品信息
			init() {
				const {
					goodsType
				} = this.options;
				if (goodsType == '1') {
					//普通
					this.getGoodsDetalInfos();
				} else if (goodsType == '2') {
					//秒杀
					this.getGoodsSeckillDetalInfos();
				} else if (goodsType == '3' || goodsType == '10002010' || goodsType == '10002023') {
					//特权 门店特权 课程
					this.getGoodsVipDetalInfos();
				}
			},
			// 获取定位信息
			getLocation() {
				// #ifdef APP
				uni.getLocation({
					type: 'wgs84'
				}).then(res => {
					this.latitude = res.latitude;
					this.longitude = res.longitude
				})
				// #endif

				// #ifdef H5
				//h5获取定位
				this.$getLocation();
				// #endif
			},
			// 查询默认收货地址
			getAddressInfo() {
				_apiGetShowAddress().then(res => {
					if (res && res.code == 0) {
						this.address = res.data
					}
				})
			},
			// 查询用户信息
			getUserInfo() {
				this.$getUserInfoSync().then(res => {
					if (res && res.code == 0) {
						this.userInfo = res.data
					}
				})
			},
			// 查询商品详细信息 == 普通
			getGoodsDetalInfos() {
				const {
					goodsId
				} = this.options;
				_apiGoodsInfo({
					goodsId: goodsId
				}).then(res => {
					if (res && res.code == 0) {
						this.goodInfo = res.data
					}
				})
			},
			// 查询商品详细信息 == 秒杀
			getGoodsSeckillDetalInfos() {
				const {
					goodsId
				} = this.options;
				_apiGoodsSeckillInfo(goodsId).then(res => {
					if (res && res.code == 0) {
						this.goodInfo = res.data.goodsInfo;
						this.killInfo = res.data
					}
				})
			},
			// 查询商品详细信息 == 特权
			getGoodsVipDetalInfos() {
				const {
					goodsId
				} = this.options;
				_apiGoodsVipDetail({
					goodsId: goodsId,
					latitude: this.latitude,
					longitude: this.longitude,
					// 联调测试
					// latitude: '30.68144',
					// longitude: '103.8559'
				}).then(res => {
					if (res && res.code == 0) {
						this.goodInfo = res.data
					}
				})
			},
			onChange(detail, key) {
				// 需要手动对 checked 状态进行更新
				const {
					num
				} = this.options;

				let amount = this.goodInfo.amountPoints;
				let legal = this.goodInfo.legalRightIntegral;

				this.ifIntergal = detail
				if (key == 'amountPoints') {
					let interger = this.userInfo.accountEntity.amountPoints;
					if (detail && interger < amount * num) {
						this.$showToast('PI积分不足无法抵扣～');
						setTimeout(() => {
							this.ifIntergal = '1'
						}, 300)
					}
				} else {
					let interger = this.userInfo.accountEntity.legalRightIntegral;
					if (detail && interger < ((legal / 100) * num)) {
						this.$showToast('权益积分积分不足无法抵扣～');
						setTimeout(() => {
							this.ifIntergal = '1'
						}, 300)
					}
				}
			},
			// 收货地址
			jumpAddress() {
				this.$navTo('/pages/packageA/address/address?set=true')
			},
			// 下单
			showActionSheel() {
				const {
					address,
					ifIntergal,
					remark,
					userInfo,
					goodInfo,
					latitude,
					longitude,
					killInfo,
					payAmount
				} = this;
				const {
					goodsId,
					goodsType,
					num,
					skuId
				} = this.options;

				if ((goodsType != '10002010' && goodsType != '10002023') && (goodsType != '3' || goodInfo.isPhysicalGoods == '0') && (!address || !
						address.addrId || address.addrId == '')) {
					return this.$showToast('请选择收货地址');
				}

				if (payAmount <= 0) {
					return this.$showToast('金额有误，请联系管理员');
				}

				let params = {
					addrId: (address && address.addrId) ? address.addrId : '',
					attributeIds: skuId,
					goodsId: goodsId,
					goodsNum: num,
					isAmountPoints: ifIntergal,
					orderRemark: remark,
					latitude: latitude,
					longitude: longitude
				}
				if (goodsType == '1') {
					//普通商品
					this.createdOrder(params, payAmount, goodsType);
				} else if (goodsType == '2') {
					//秒杀
					this.createdSeckillOrder(params, payAmount, goodsType);
				} else if (goodsType == '3' || goodsType == '10002010' || goodsType == '10002023') {
					//特权商品 门店特权 课程特权 499特权
					this.createdVipOrder(params, payAmount, goodsType);
				}
			},
			//下单
			createdOrder(params, amount, goodsType) {
				this.loading = true
				_createMallTradeOrder(params).then(res => {
					this.loading = false
					if (res && res.code == 0) {
						console.log(params, amount);
						this.$redTo(`../pay/pay?orderId=${res.data}&amount=${amount}&goodsType=${goodsType}`);
					}
				}).catch(() => this.loading = false)
			},
			//下单 == 秒杀商品
			createdSeckillOrder(params, amount, goodsType) {
				const {
					goodsId
				} = this.options;
				params.seckillId = goodsId;
				params.goodsId = this.goodInfo.goodsId;

				this.loading = true
				_createSeckillTradeOrder(params).then(res => {
					this.loading = false
					if (res && res.code == 0) {
						console.log(params, amount);
						this.$redTo(`../pay/pay?orderId=${res.data}&amount=${amount}&goodsType=${goodsType}`);
					}
				}).catch(() => this.loading = false)
			},
			//下单 == 特权商品
			createdVipOrder(params, amount, goodsType) {
				this.loading = true
				_createVipGoodsOrder(params).then(res => {
					this.loading = false
					if (res && res.code == 0) {
						console.log(params, amount);
						this.$redTo(`../pay/pay?orderId=${res.data}&amount=${amount}&goodsType=${goodsType}`);
					}
				}).catch(() => this.loading = false)
			},
			//
			callphone() {
				const [{
					mobile
				}] = this.goodInfo.storeOrderByList;
				if (mobile && mobile != '') {
					this.$callPhone(mobile);
				} else {
					this.$showTaost('该门店暂无电话～')
				}
			},
			// 
			jumpMapNavgation() {
				const [{
					longitude,
					latitude,
					storeName,
					address
				}] = this.goodInfo.storeOrderByList;
				this.$refs.navgationRefs.updataData({
					latitude: latitude,
					longitude: longitude,
					name: storeName,
					address: address
				});
			},
			doUseStore() {
				this.$navTo(
					`../vipStoreList/vipStoreList?goodsId=${this.options.goodsId}&latitude=${this.latitude}&longitude=${this.longitude}`
				)
			}
		}
	}
</script>

<style lang="scss" scoped>
	.confirm {
		background-color: $bg-color-fa;
		min-height: 100vh;

		.address {
			background-color: $bg-color-fa;
			padding: 10rpx 30rpx;
			height: 100rpx;
			border-radius: 20rpx;

			.place {
				font-size: 32rpx;
				color: $font-color2;
			}
		}

		.store_list {
			color: #292929;

			.tip_c {
				color: #9F9F9F;
			}

			.tip_c2 {
				color: #F7694A;
			}

			.b_b_infos {
				padding-top: 16rpx;
				border-top: solid 1px #F7F7F7;

				.tip_tag {
					height: 20rpx;
					width: 2rpx;
					border-radius: 1rpx;
					background-color: #F0F0F0;
				}
			}

			.storeStatus {
				background-color: #FFF0ED;
				border-radius: 4rpx;
				padding: 2rpx 8rpx;
				color: #F7694A;
				font-size: 22rpx;

				&.isDown {
					background-color: #EAEAEA;
					color: #292929;
				}
			}
		}

		.goodInfo {
			.sku {
				background-color: $bg-color-fa;
				color: $font-color2;
				padding: 4rpx 18rpx 4rpx 0;
			}

			.price {
				color: $font-money-color2;
			}
		}

		.remark .van-cell {
			padding: 30rpx 0;
		}

		.ceil {
			padding: 18rpx 0;
			color: $font-color;
			font-size: 28rpx;

			.ceil-able {
				font-size: 32rpx;
				color: $font-color3;

				.tips {
					font-size: 24rpx;
					color: $font-color2;
				}
			}
		}


		.buynow {
			position: fixed;
			box-sizing: border-box;
			bottom: 0;
			padding: 10rpx 30rpx 60rpx;

			.buyNowBtn {
				width: 300rpx;
				background-image: linear-gradient(to bottom, #fa4935, #fb8363);
				padding: 20rpx 40rpx;
				color: #FFF;
				border-radius: 8rpx;
				margin: 0;
			}

			.price {
				color: $font-money-color2;
			}
		}
	}
</style>
