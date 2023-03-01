<template>
	<view class="addGoods">
		<!-- 自定义导航 -->
		<uniNav title="添加商品" />

		<view class="-warp20-30">

			<u--form labelPosition="left" :model="modelVal" labelWidth="90">
				<view class="titles">商品基础信息</view>
				<view class="-bgf formWawrp">
					<u-form-item label="服务类型" prop="businessChannelTypeText" @click="showService" borderBottom>
						<u--input inputAlign="right" disabled disabledColor="#ffffff"
							v-model="modelVal.businessChannelTypeText" border="none" placeholder="请选择服务类型"></u--input>
						<u-icon slot="right" name="arrow-right" />
					</u-form-item>
					<u-form-item v-if="modelVal.businessChannelType != '' " label="商品名称" prop="goodsName" borderBottom>
						<u--input @focus="jumpGoodName" :maxlength="20" inputAlign="right" v-model="modelVal.goodsName"
							placeholder="请输入商品名称" border="none"></u--input>
					</u-form-item>

					<u-form-item label="商品简介" prop="goodsDescribe" borderBottom>
						<u--input :maxlength="20" inputAlign="right" v-model="modelVal.goodsDescribe"
							placeholder="请输入商品一句话简介" border="none"></u--input>
					</u-form-item>

					<u-form-item label="排序" prop="level" borderBottom>
						<u--input :maxlength="4" type="number" inputAlign="right" v-model="modelVal.level"
							placeholder="优先级越小越靠前,最大999" border="none"></u--input>
					</u-form-item>

					<u-form-item label="项目介绍" prop="goodsServiceDescribe">
						<u--textarea confirmType="next" autoHeight :maxlength="100" inputAlign="right"
							v-model="modelVal.goodsServiceDescribe" placeholder="请输入商品服务项目介绍" border="none">
						</u--textarea>
					</u-form-item>
				</view>
				<view class="titles mt12">商品金额信息</view>
				<view class="-bgf formWawrp">
					<u-form-item label="挂牌价" prop="listingPrice" borderBottom>
						<u--input :maxlength="8" inputAlign="right" v-model="modelVal.listingPrice"
							placeholder="请输入商品挂牌价(划线价)" border="none"></u--input>
					</u-form-item>
					<u-form-item label="成交价" prop="userFinalPrice">
						<u--input :maxlength="8" inputAlign="right" v-model="modelVal.userFinalPrice"
							placeholder="请输入用户成交价" border="none"></u--input>
					</u-form-item>
				</view>
				<view class="titles mt12">商品图片信息</view>
				<view class="-bgf formWawrp">
					<u-form-item label="轮播图片" prop="goodsBannerUrlList" borderBottom>
						<UploadImage :count="9" :width="150" :height="150" v-model="modelVal.goodsBannerUrlList"
							:size="50" />
					</u-form-item>
					<u-form-item label="商品详情图片" prop="goodsDetailsPhotosUrllList" borderBottom>
						<UploadImage :count="9" :width="150" :height="150" v-model="modelVal.goodsDetailsPhotosUrllList"
							:size="50" />
					</u-form-item>
				</view>



			</u--form>
		</view>






		<view class="fixed_btn">
			<u-button :loading="isLoading" :loading-text="modelVal.goodsId != '' ? '商品修改中...' : '商品新增中...'"
				@click="addGoods" class="f_c_c w100 realy_buy">新增商品</u-button>
		</view>
		<!--  -->
		<u-action-sheet cancel-text="取消" :show="showActionSheet" :actions="businessChannel" description="选择商品所属的服务"
			@close="actionCancel" @select="actionSelect" />
	</view>
</template>

<script>
	import {
		_getStoreInfo,
		_publishgood
	} from '@/http/apiReq'
	import upload from '@/utils/upload.js'
	export default {
		data() {
			return {
				isLoading: false,
				showActionSheet: false,
				modelVal: {
					goodsId: '',
					goodsName: '',
					goodsDescribe: '',
					goodsServiceDescribe: '',
					level: 999,

					businessChannelType: '',
					businessChannelTypeText: '',

					listingPrice: '',
					userFinalPrice: '',

					goodsBannerUrlList: [],
					goodsDetailsPhotosUrllList: [],

				},
				businessChannel: []
			};
		},
		components: {
			UploadImage: () => import('@/components/UploadImage/UploadImage.vue')
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {
			this.getStoreInfo();
		},
		onShow() {
			let goodsName = uni.getStorageSync('goodsName');
			const {
				businessChannelType
			} = this.modelVal;
			if (goodsName && businessChannelType != '') {
				this.modelVal.goodsName = goodsName
			}
		},
		methods: {
			// 新增商品
			addGoods() {
				const {
					goodsName,
					goodsDescribe,
					goodsServiceDescribe,
					level,
					businessChannelType,
					listingPrice,
					userFinalPrice,
					goodsBannerUrlList,
					goodsDetailsPhotosUrllList
				} = this.modelVal;
				if (businessChannelType == '') {
					return this.$showToast('请先选择服务类型');
				}
				if (goodsName == '') {
					return this.$showToast('请先选择商品名称');
				}
				if (goodsDescribe == '') {
					return this.$showToast('请先输入商品一句话简介');
				}
				if (level == '') {
					return this.$showToast('请输入商品排序');
				}

				if (listingPrice == '') {
					return this.$showToast('请输入商品的挂牌价（划线价）');
				}
				if (parseFloat(listingPrice) <= 0 || parseFloat(userFinalPrice) <= 0) {
					return this.$showToast('金额必须大于0');
				}
				if (!this.$ExpReg.fixed2.test(listingPrice)) {
					return this.$showToast('挂牌价最多两位小数');
				}

				if (userFinalPrice == '') {
					return this.$showToast('请输入商品的用户成交价）');
				}

				if (!this.$ExpReg.fixed2.test(userFinalPrice)) {
					return this.$showToast('成交价最多两位小数');
				}
				
				if(parseFloat(userFinalPrice) >= parseFloat()){
					return this.$showToast('用户成交价必须小于商品挂牌价');
				}

				if (goodsBannerUrlList == '') {
					return this.$showToast('请上传商品轮播图片');
				}
				if (goodsDetailsPhotosUrllList == '') {
					return this.$showToast('请上传商品详情介绍图片');
				}

				let postData = {
					goodsName: goodsName,
					goodsDescribe: goodsDescribe,
					goodsServiceDescribe: goodsServiceDescribe,
					level: level,
					businessChannelType: businessChannelType,
					listingPrice: listingPrice,
					userFinalPrice: userFinalPrice,
					goodsBannerUrlList: goodsBannerUrlList,
					goodsDetailsPhotosUrllList: goodsDetailsPhotosUrllList
				};
				console.log(postData)
				this.addGoodsAxios(postData);
			},
			// 新增商品请求
			addGoodsAxios(data) {
				this.isLoading = true
				_publishgood(data).then(res => {
					this.isLoading = false
					if (res && res.code == 0) {
						this.$showToast(this.modelVal.goodsId == '' ? '新增商品成功' : '修改商品成功～');
						setTimeout(() => {
							this.$navBack()
						}, 800)
					}
				}).catch(() => this.isLoading = false)
			},
			// 查询商家所拥有的服务
			getStoreInfo() {
				_getStoreInfo().then(res => {
					if (res && res.code == 0) {
						let businessChannel = [];
						const {
							isOther,
							isRepair,
							isUpkeep,
							isWash
						} = res.data;
						if (isWash == '0') {
							businessChannel.push({
								name: '洗车服务',
								value: '10002004'
							})
						}
						if (isUpkeep == '0') {
							businessChannel.push({
								name: '保养服务',
								value: '10002005'
							})
						}
						if (isRepair == '0') {
							businessChannel.push({
								name: '维修服务',
								value: '10002007'
							})
						}
						if (isOther == '0') {
							businessChannel.push({
								name: '其他服务',
								value: '10002008'
							})
						}

						this.businessChannel = businessChannel
					}
				})
			},
			// 
			showService() {
				this.showActionSheet = true
			},
			// 
			actionCancel() {
				this.showActionSheet = false
			},
			// 
			actionSelect({
				value,
				name
			}) {
				uni.removeStorageSync('goodsName');
				this.modelVal.businessChannelType = value;
				this.modelVal.businessChannelTypeText = name;
				this.modelVal.showActionSheet = false;
				this.modelVal.goodsName = ''
			},
			// 
			jumpGoodName() {
				this.$navTo('../goodsSearch/goodsSearch?businessChannelType=' + this.modelVal.businessChannelType)
			},
		}
	}
</script>

<style lang="scss" scoped>
	.addGoods {
		min-height: 100vh;
		box-sizing: border-box;
		padding-bottom: 240rpx;

		.titles {
			color: $font-color2;
			font-size: 24rpx;
			margin-bottom: 20rpx;
			margin-left: 30rpx;
		}

		.formWawrp {
			padding: 0 20rpx 20rpx;
			border-radius: 12rpx;
		}

		.cell-upload-item {
			padding: 20rpx;
			color: #646566;
			font-size: 24rpx;
		}

		.fixed_btn {
			padding: 20rpx 30rpx 120rpx;
			position: fixed;
			bottom: 0;
			left: 0;
			right: 0;

			.realy_buy {
				background-image: linear-gradient(to bottom, #fa4935, #fb8363);
				border-radius: 18rpx;
				color: #FFF;
				font-size: 32rpx;
			}
		}
	}
</style>
