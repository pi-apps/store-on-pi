<template>
	<view class="template">
		<!-- 自定义导航 -->
		<uniNav :title="$t('all.commonpent.address')" :rightText="$t('all.commonpent.clear')"
			@rightClick="clearInfos()" />

		<view class="store_classIfy">
			<scroll-view :scroll-y="true" class="classify_item_left">
				<view :class="'classify_item f_c_c '+ (areaCode_code == item.areaCode ? 'active' : '')"
					v-for="(item,index) in countryList" :key="index" @click="getProviceInfo(item)">
					{{item.areaName}}
				</view>
			</scroll-view>
			<scroll-view :scroll-y="true" class="store_classIfy_warp">
				<view>{{$t('all.commonpent.allCity')}}</view>
				<view class="f f_warp mt10">
					<view :class="'item_warp '+(proviceCode == item.areaCode ? 'active' : '')"
						v-for="(item,index) in proviceList" :key="index" @click="getCityInfo(item)">{{item.areaName}}
					</view>
				</view>
				<view v-if="getCityList.length > 0">{{$t('all.commonpent.allCity1')}}</view>
				<view class="f f_warp mt10">
					<view :class="'item_warp '+(cityCode == item.areaCode ? 'active' : '')"
						v-for="(item,index) in getCityList" :key="index" @click="getCountyInfo(item)">
						{{item.areaName}}
					</view>
				</view>
				<view v-if="getcountryList.length > 0">{{$t('all.commonpent.allCity2')}}</view>
				<view class="f f_warp mt10">
					<view :class="'item_warp '+(countryCode == item.areaCode ? 'active' : '')"
						v-for="(item,index) in getcountryList" @click="nextJumps(item)" :key="index">{{item.areaName}}
					</view>
				</view>
			</scroll-view>
		</view>
	</view>
</template>

<script>
	import {
		getAllCountry,
		getProviceInfo,
		getCityInfo,
		getCountyInfo,
		saveUserCode
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				areaCode_code: '',
				countryList: [],

				proviceCode: '',
				proviceList: [],

				cityCode: '',
				getCityList: [],

				countryCode: '',
				getcountryList: [],
			};
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {
			this.getCountry();
		},
		onShow() {

		},
		methods: {
			getCountry() {
				this.proviceCode = '';
				this.cityCode = '';
				this.countryCode = '';
				getAllCountry().then(res => {
					if (res && res.code == 0) {
						this.countryList = res.data || [];
						if (this.countryList.length && this.countryList.length > 0) {
							this.getProviceInfo(this.countryList[0]);
						}
					}
				})
			},
			getProviceInfo({
				areaCode
			}) {
				console.log(areaCode)
				this.areaCode_code = areaCode;
				this.proviceCode = '';
				this.cityCode = '';
				this.countryCode = '';
				getProviceInfo(areaCode).then(res => {
					if (res && res.code == 0) {
						this.proviceList = res.data || [];
						this.getCityList = [];
						this.getcountryList = [];
					}
				})
			},
			getCityInfo({
				areaCode,
				areaName
			}) {
				this.proviceCode = areaCode;
				this.cityCode = '';
				this.countryCode = '';
				getCityInfo(areaCode).then(res => {
					if (res && res.code == 0) {
						this.getCityList = res.data || [];
						this.getcountryList = [];
						if (this.getCityList.length <= 0) {
							uni.setStorageSync('areaName', areaName)
							this.jumpDataInfos();
						}
					}
				})
			},
			getCountyInfo({
				areaCode,
				areaName
			}) {
				this.cityCode = areaCode;
				this.countryCode = '';
				getCountyInfo(areaCode).then(res => {
					if (res && res.code == 0) {
						this.getcountryList = res.data || [];
						if (this.getcountryList.length <= 0) {
							uni.setStorageSync('areaName', areaName)
							this.jumpDataInfos();
						}
					}
				})
			},
			nextJumps({
				areaCode,
				areaName
			}) {
				this.countryCode = areaCode;
				uni.setStorageSync('areaName', areaName)
				this.jumpDataInfos();
			},
			jumpDataInfos() {
				saveUserCode({
					countryCode: this.areaCode_code,

					provinceCode: this.proviceCode,
					cityCode: this.cityCode,
					districtCode: this.countryCode
				}).then(res => {
					this.$navBack();
				})
			},
			clearInfos() {
				saveUserCode({
					countryCode: '',

					provinceCode: '',
					cityCode: '',
					districtCode: ''
				}).then(res => {
					this.$navBack();
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.store_classIfy {
		display: flex;
		height: calc(100vh - 44px);

		.classify_item_left {
			width: 200rpx;
			font-size: 20rpx;
			background-color: #f1f1f1;
			padding: 15rpx 0;
			box-sizing: border-box;

			.classify_item {
				padding: 20rpx 0;

				&.active {
					background-color: #fff;
				}
			}
		}

		.store_classIfy_warp {
			flex: 1;
			padding: 20rpx 0 20rpx 20rpx;
			box-sizing: border-box;
			font-size: 24rpx;

			.item_warp {
				padding: 10rpx 20rpx;
				border: solid 2rpx #f1f1f1;
				border-radius: 10rpx;
				font-size: 20rpx;
				margin-right: 20rpx;
				margin-bottom: 20rpx;

				&.active {
					border-color: #f67112;
				}
			}
		}
	}
</style>
