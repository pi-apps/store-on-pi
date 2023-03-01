<template>
	<view class="scanInfos">
		<!-- 自定义导航 -->
		<uniNav title="确认核销" />

		<view class="scanInfo">
			<view class="-bgf item" v-if="scanGoods.goodsInfoJson">
				<u-cell-group title="券码信息">
					<u-cell title="服务项目" :value="scanGoods.goodsInfoJson.goodsName" />
					<u-cell title="券码" :value="scanGoods.qrNo" />
					<u-cell title="实付金额" :value="'π'+(scanGoods.fee ? scanGoods.fee : scanGoods.orderPayPrice)" />
					<u-cell title="下单时间" :value="scanGoods.createTime" />
					<u-cell title="支付时间" :value="scanGoods.payTime" :border=" false" />
				</u-cell-group>
			</view>
			<template v-else>
				<view class="-bgf mb24 item">
					<u-cell-group title="核销门店">
						<u-cell title="门店名称"  :border=" false" :value="scanGoods.useStoreJson.storeName" />
					</u-cell-group>
				</view>
				<view class="-bgf item">
					<u-cell-group title="券码信息">
						<u-cell title="服务项目" :value="scanGoods.goodsName" />
						<u-cell title="券码" :value="scanGoods.qrNo" />
						<u-cell title="实付金额" :value="'π'+scanGoods.userFinalPrice" />
						<u-cell title="下单时间" :value="scanGoods.createTime" />
						<u-cell title="支付时间" :value="scanGoods.payTime" :border=" false" />
					</u-cell-group>
				</view>
			</template>
			<u-button :loading="loading" :disabled="loading" :loading-text="loadingText" @click="useScan"
				class="f_c_c w100 realy_buy bold mt20">立即核销</u-button>
		</view>
	</view>
</template>

<script>
	import {
		_scanInfo,
		_useScan
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				loading: false,
				loadingText: '查询中...',
				scanCode: '',
				scanGoods: {
					id: '',
					useStoreJson: {},
					goodsInfoJson:{}
				}
			};
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {
			if (options && options.scanCode) {
				this.scanCode = options.scanCode

				this.scanInfo();
			}
		},
		onShow() {

		},
		methods: {
			// 查询扫码信息
			scanInfo() {
				this.loading = true
				this.loadingText = '查询中...'
				_scanInfo(this.scanCode).then(res => {
					this.loading = false
					if (res && res.code == 0) {
						let datas = res.data;
						if(datas.goodsChannelType){
							datas.goodsInfoJson = datas.goodsInfoJson ? JSON.parse(datas.goodsInfoJson) : null;
							
							this.scanGoods = datas;
						}else{
							datas.useStoreJson = datas.useStoreJson ? JSON.parse(datas.useStoreJson) : null;
							datas.goodsInfoJson = datas.goodsInfoJson ? JSON.parse(datas.goodsInfoJson) : null;
							
							this.scanGoods = datas
						}
					} else {
						setTimeout(() => {
							this.$navBack();
						}, 600)
					}
				}).catch(() => this.loading = false)
			},

			// 查询扫码信息
			useScan() {
				uni.showModal({
					title: '确认核销',
					content: '确认核销信息，核销后该订单将被使用～',
					confirmText: '立即核销',
					confirmColor: '#f67112',
					success: (res) => {
						if (res.confirm) {
							if(this.scanGoods.goodsInfoJson){
								this.useScanAxiosGoods();
							}else{
								this.useScanAxios();
							}
						}
					},
				})

			},
			// 核销券码
			useScanAxios() {
				const {
					userFinalPrice,
					id
				} = this.scanGoods;
				this.loading = true
				this.loadingText = '核销中...'
				_useScan(id).then(res => {
					this.loading = false
					if (res && res.code == 0) {
						uni.showToast({
							title: '核销成功',
							icon: 'success'
						});
						setTimeout(() => {
							this.$redTo(
								`../scanDone/scanDone?scanCode=${this.scanCode}&amount=${userFinalPrice}`
								)
						}, 1000)
					}
				}).catch(() => this.loading = false)
			},
			// 
			useScanAxiosGoods(){
				const {
					orderPayPrice,
					orderId,
					fee
				} = this.scanGoods;
				this.loading = true
				this.loadingText = '核销中...'
				_useScan(orderId).then(res => {
					this.loading = false
					if (res && res.code == 0) {
						uni.showToast({
							title: '核销成功',
							icon: 'success'
						});
						setTimeout(() => {
							this.$redTo(
								`../scanDone/scanDone?scanCode=${this.scanCode}&amount=${orderPayPrice ? orderPayPrice : fee}`
								)
						}, 1000)
					}
				}).catch(() => this.loading = false)
			}
			
		}
	}
</script>

<style lang="scss" scoped>
	.scanInfos{
		background-color: #f5f5f5;
		min-height: 100vh;
	}
	.scanInfo {
		padding: 20rpx;
		.item{
			border-radius: 12rpx;
		}
		.realy_buy {
			background-image: linear-gradient(to bottom, #fa4935, #fb8363);
			border-radius: 18rpx;
			color: #FFF;
			font-size: 32rpx;
			margin-top: 100rpx
		}
	}
</style>
