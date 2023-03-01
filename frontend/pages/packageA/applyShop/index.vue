<template>
	<view class="store">
		<!-- 自定义导航 -->
		<uniNav title="门店入驻申请" />

		<view class="main">
			<u--form labelPosition="left" :model="modelVal" labelWidth="100">
				<view class="titles">商品基础信息</view>
				<view class="-warp20-30 -bgf" style="border-radius: 10rpx;">
					<u-form-item label="门店名称" prop="storeName" borderBottom>
						<u--input :maxlength="20" inputAlign="right" v-model="modelVal.storeName" border="none"
							placeholder="请输入门店名称"></u--input>
					</u-form-item>
					<u-form-item label="门店地址" prop="address" @click="openLocation" borderBottom>
						<u--input inputAlign="right" disabled disabledColor="#ffffff" v-model="modelVal.address"
							border="none" placeholder="请选择门店所在地址"></u--input>
						<u-icon slot="right" name="map-fill" size="26" color="#f67112" />
					</u-form-item>
					<u-form-item label="门店电话" prop="mobile">
						<u--input :maxlength="11" inputAlign="right" v-model="modelVal.mobile" border="none"
							placeholder="请输入门店电话"></u--input>
					</u-form-item>
				</view>
				<view class="titles">业务范围</view>
				<view class="-warp20-30 -bgf" style="border-radius: 10rpx;">
					<u-form-item label="" prop="businessCannel">
						<u-checkbox-group v-model="modelVal.businessCannel" activeColor="#f67112">
							<u-checkbox :customStyle="{marginRight: '4px'}" label="洗车业务" name="isWash"></u-checkbox>
							<u-checkbox :customStyle="{marginRight: '4px'}" label="保养业务" name="isUpkeep"></u-checkbox>
							<u-checkbox :customStyle="{marginRight: '4px'}" label="修理配件" name="isRepair"></u-checkbox>
							<u-checkbox :customStyle="{marginRight: '4px'}" label="其他业务" name="isOther"></u-checkbox>
						</u-checkbox-group>
					</u-form-item>
				</view>
				<view class="titles">门店图片上传</view>
				<view class="-warp20-30 -bgf" style="border-radius: 10rpx;">
					<u-form-item label="门店logo" prop="doorPhotoUrl" borderBottom>
						<UploadImage v-model="modelVal.doorPhotoUrl" :size="50" :count="1" />
					</u-form-item>
					<u-form-item label="门店图片" prop="photosUrlList">
						<UploadImage v-model="modelVal.photosUrlList" :size="50" :count="9" />
					</u-form-item>
				</view>
				<view class="titles">企业信息</view>
				<view class="-warp20-30 -bgf" style="border-radius: 10rpx;">
					<u-form-item label="营业执照" prop="businessLicenseName" @click="showPicket" borderBottom>
						<u--input inputAlign="right" disabled disabledColor="#ffffff"
							v-model="modelVal.businessLicenseName" border="none" placeholder="营业执照类型"></u--input>
						<u-icon slot="right" name="arrow-right" size="18" color="#f67112" />
					</u-form-item>
					<u-form-item label="营业执照名称" prop="licenseName" borderBottom>
						<u--input :maxlength="30" inputAlign="right" v-model="modelVal.licenseName" border="none"
							placeholder="请输入营业执照名称"></u--input>
					</u-form-item>
					<u-form-item label="门店简称" prop="shortName" borderBottom>
						<u--input :maxlength="7" inputAlign="right" v-model="modelVal.shortName" border="none"
							placeholder="请输入门店简称"></u--input>
					</u-form-item>
					<u-form-item label="营业执照编号" prop="licenseCode" borderBottom>
						<u--input :maxlength="30" inputAlign="right" v-model="modelVal.licenseCode" border="none"
							placeholder="请输入营业执照编号"></u--input>
					</u-form-item>
					<u-form-item label="营业执照" prop="licensePic">
						<UploadImage v-model="modelVal.licensePic" :size="50" :count="1" />
					</u-form-item>
					<template v-if="modelVal.businessLicense == '10011001'">
						<u-form-item label="选择开户行" prop="companyBankName" @click="showBankPick('isCompany')"
							borderBottom>
							<u--input inputAlign="right" disabled disabledColor="#ffffff"
								v-model="modelVal.companyBankName" border="none" placeholder="请选择对公开户行"></u--input>
							<u-icon slot="right" name="arrow-right" size="18" color="#f67112" />
						</u-form-item>
						<u-form-item label="开户银行号" prop="companyBankNo" borderBottom>
							<u--input :maxlength="30" type="number" inputAlign="right" v-model="modelVal.companyBankNo"
								border="none" placeholder="请输入开户银行号"></u--input>
						</u-form-item>
						<u-form-item label="对公账户证明" prop="corporatePic">
							<UploadImage v-model="modelVal.corporatePic" :size="50" :count="1" />
						</u-form-item>
					</template>
				</view>
				<view class="titles">法人信息</view>
				<view class="-warp20-30 -bgf" style="border-radius: 10rpx;">
					<u-form-item label="身份证名" prop="idCardName" borderBottom>
						<u--input :maxlength="18" inputAlign="right" v-model="modelVal.idCardName" border="none"
							placeholder="请输入身份证名"></u--input>
					</u-form-item>
					<u-form-item label="身份证号" prop="idCardNo" borderBottom>
						<u--input type="idcard" :maxlength="18" inputAlign="right" v-model="modelVal.idCardNo"
							border="none" placeholder="请输入身份证号"></u--input>
					</u-form-item>
					<view class="f_c_a">
						<u-form-item label="身份证正面" prop="idCardPic1">
							<UploadImage v-model="modelVal.idCardPic1" :size="50" :count="1" />
						</u-form-item>
						<u-form-item label="身份证反面" prop="idCardPic2">
							<UploadImage v-model="modelVal.idCardPic2" :size="50" :count="1" />
						</u-form-item>
					</view>


					<template v-if="modelVal.businessLicense == '10011002'">
						<u-form-item label="选择开户行" prop="bankName" @click="showBankPick('isOwn')" borderBottom>
							<u--input inputAlign="right" disabled disabledColor="#ffffff" v-model="modelVal.bankName"
								border="none" placeholder="请选择开户行"></u--input>
							<u-icon slot="right" name="arrow-right" size="18" color="#f67112" />
						</u-form-item>
						<u-form-item label="银行卡号" prop="bankNo" borderBottom>
							<u--input :maxlength="30" type="number" inputAlign="right" v-model="modelVal.bankNo"
								border="none" placeholder="请输入开户银行号"></u--input>
						</u-form-item>
						<u-form-item label="银行卡照片" prop="bankPic">
							<UploadImage v-model="modelVal.bankPic" :size="50" :count="1" />
						</u-form-item>
					</template>
				</view>
			</u--form>
		</view>
		<!-- 入住按钮 -->
		<view class="btn_now">
			<view class="f f_center f_align_center warp_xy" @click="traggetRead">
				<u-icon v-if="modelVal.isRead" size="16" name="/static/image/login/gouxuan@2x.png" color="#f67112" />
				<div v-else class="radiuss"></div>
				<view class="f_align_center f ml8">
					<view>请先同意</view>
					<view class="name_xy" @click.stop="goUserXy('10010001')">《用户服务协议</view>
					<view>和</view>
					<view class="name_xy" @click.stop="goUserXy('10010002')">《隐私协议》</view>
				</view>
			</view>
			<u-button @click="storeApplyNow" :loading="isApply" :disabled="isApply" loading-text="入驻申请中..."
				type="primary">立即申请入驻</u-button>
		</view>
		<!-- 选择开户行 -->
		<popPicker :show.sync="showBank" @nodeClick="onChangeBank" />
		<!--  -->
		<u-action-sheet @select="onChangePick" @close="onClose" round="12" :actions="columns" title="选择营业执照类型"
			:show="showPick" cancelText="取消"></u-action-sheet>

		<!-- showOverlay -->
		<u-overlay :show="showOverlay" zIndex="1000">
			<view class="overlay f_c_c">
				<view class="entryStatus f f_column f_c_c" v-if="entryStatus == 0">
					<u-icon name="clock-fill" size="80" color="#EC661B" class="mb32" />
					<text>审核中，请耐心等待...</text>
					<view class="navBacks" @click="$navBack()">立即返回</view>
				</view>
				<view class="entryStatus f f_column f_c_c" v-if="entryStatus == '1'">
					<u-icon name="minus-circle-fill" size="80" color="#e71c19" class="mb32" />
					<text>审核驳回，驳回原因：</text>
					<text>{{entryRemark}}</text>
					<view class="entryRemark f_c_c mt32" @click="closeReadnoew">重新填写</view>
				</view>
				<view class="entryStatus f f_column f_c_c" v-if="entryStatus == '2'">
					<u-icon name="checkmark-circle-fill" size="80" color="#4fc08d" class="mb32" />
					<text>您已经成功入驻门店，无需操作～</text>
					<view class="navBacks" @click="$navBack()">立即返回</view>
				</view>
			</view>
		</u-overlay>
	</view>
</template>

<script>
	import {
		_getbank,
		_storeAplly,
		_getStoreInfo
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				showOverlay: false,
				entryRemark: '',
				entryStatus: '',
				isApply: false,
				isOwn: false,

				showPick: false,
				columns: [{
						name: '企业',
						key: '10011001'
					},
					{
						name: '个体工商户',
						key: '10011002'
					}
				],
				showBank: false,

				modelVal: {
					isRead: false,

					storeName: '',
					longitude: '',
					latitude: '',
					address: '',
					mobile: '',
					doorPhotoUrl: [],
					photosUrlList: [],

					businessCannel: [],

					businessLicense: '',
					businessLicenseName: '',
					shortName: '',
					licenseName: '',
					licenseCode: '',
					licensePic: [],

					bankCode: "",
					companyBankName: '',
					companyBankNo: '',
					corporatePic: [],

					idCardName: '',
					idCardNo: '',
					idCardPic1: [],
					idCardPic2: [],


					bankName: '',
					bankNo: '',
					bankPic: []
				}


			};
		},
		components: {
			UploadImage: () => import('@/components/UploadImage/UploadImage.vue')
		},
		mounted() {
			// h5原生方法调用回调
			//#ifdef H5  
			const _this = this;
			// //监听获取定位是否成功
			window['appBackJsgetOnelocationAddr'] = (code) => {
				console.log('原生回调：==', code);
				let res = JSON.parse(code);
				_this.modelVal.address = res.addr;
				_this.modelVal.latitude = res.latitude;
				_this.modelVal.longitude = res.longitude;
			}
			// #endif
		},
		onLoad(options) {
			this.getStoreInfos();
		},
		onShow() {

		},
		methods: {
			// 打开地图位置选择
			openLocation() {
				// #ifdef APP
				///使用 gcj02 国测局坐标系
				uni.chooseLocation({
					success: (result) => {
						console.log(result)
						this.address = result.address;
						this.longitude = result.longitude;
						this.latitude = result.latitude
					},
				})
				// #endif
				// #ifdef H5
				this.$openMapAddress();
				// #endif
			},
			// 关闭pick
			onClose() {
				this.showPick = false
				this.showBank = false
			},
			// 
			showPicket() {
				this.showPick = true
			},
			// 打开开户行选择
			showBankPick(keys) {
				console.log(keys)
				this.showBank = true;
				this.isOwn = (keys == 'isCompany' ? false : true)
			},
			// 
			onChangePick({
				name,
				key
			}) {
				this.modelVal.businessLicense = key;
				this.modelVal.businessLicenseName = name;
				this.modelVal.bankCode = '';
				this.showPick = false
			},
			// 选择开户行
			onChangeBank({
				bankCode,
				bankName
			}) {
				if (this.isOwn) {
					this.modelVal.companyBankName = "";
					this.modelVal.bankName = bankName;
					this.modelVal.bankCode = bankCode;
					this.modelVal.showBank = false
				} else {
					this.modelVal.companyBankName = bankName;
					this.modelVal.bankCode = bankCode;
					this.modelVal.bankName = "";
					this.modelVal.showBank = false
				}
			},

			//申请商家入住
			storeApplyNow() {
				let {
					isRead,
					storeName,
					longitude,
					latitude,
					address,
					mobile,
					doorPhotoUrl,
					photosUrlList,
					businessCannel,
					businessLicense,
					licenseName,
					shortName,
					licenseCode,
					licensePic,
					companyBankName,
					companyBankNo,
					corporatePic,
					idCardName,
					idCardNo,
					idCardPic1,
					idCardPic2,
					bankName,
					bankNo,
					bankPic,
					bankCode
				} = this.modelVal;
				console.log(this.modelVal);
				// 验证
				if (storeName == '') {
					return this.$showToast('请填写门店名称');
				}
				if (longitude == '' || latitude == '') {
					return this.$showToast('请在地图上选择门店所在位置');
				}
				if (mobile == '') {
					return this.$showToast('请填写门店电话');
				}
				if (!this.$ExpReg.phone.test(mobile)) {
					return this.$showToast('请填写正确等手机号');
				}
				if (businessCannel.length <= 0) {
					return this.$showToast('请选择一项门店经营范围');
				}
				if (doorPhotoUrl.length <= 0) {
					return this.$showToast('请上传您的门店LOGO');
				}
				if (photosUrlList.length <= 0) {
					return this.$showToast('请上传门店轮播展示图');
				}

				if (businessLicense == '') {
					return this.$showToast('请先选择营业执照的类型');
				}
				if (licenseName == '') {
					return this.$showToast('请填写营业执照上的名称');
				}
				if (shortName == '') {
					return this.$showToast('请填写门店简称');
				}
				if (licenseCode == '') {
					return this.$showToast('请填写营业执照上的代码');
				}
				if (licensePic.length <= 0) {
					return this.$showToast('请上传营业执照照片');
				}
				// 对公
				if (businessLicense == '10011001' && bankCode == '') {
					return this.$showToast('请选择对公开户行名称');
				}
				if (businessLicense == '10011001' && companyBankNo == '') {
					return this.$showToast('请填写对公开户银行号');
				}
				if (businessLicense == '10011001' && corporatePic.length <= 0) {
					return this.$showToast('请上传对公账户证明照片');
				}

				// 个人或法人身份信息
				if (idCardName == '') {
					return this.$showToast('请填写身份证上的姓名');
				}
				if (idCardNo == '') {
					return this.$showToast('请填写身份证号码');
				}
				if (idCardPic1.length <= 0) {
					return this.$showToast('请上传身份证正面');
				}
				if (idCardPic2.length <= 0) {
					return this.$showToast('请上传身份证反面');
				}
				// 对个体工商户
				if (businessLicense == '10011002' && bankCode == '') {
					return this.$showToast('请选择开户行');
				}
				if (businessLicense == '10011002' && bankNo == '') {
					return this.$showToast('请填写开户银行号');
				}
				if (businessLicense == '10011002' && bankPic.length <= 0) {
					return this.$showToast('请上传银行卡照片');
				}
				if (!isRead) {
					return this.$showToast('请先仔细阅读协议并同意协议内容');
				}


				let postData = {
					storeName: storeName,
					longitude: longitude,
					latitude: latitude,
					address: address,
					mobile: mobile,
					doorPhotoUrl: doorPhotoUrl[0],
					photosUrlList: photosUrlList,

					isWash: businessCannel.indexOf('isWash') != -1 ? '0' : '',
					isUpkeep: businessCannel.indexOf('isUpkeep') != -1 ? '0' : '1',
					isRepair: businessCannel.indexOf('isRepair') != -1 ? '0' : '1',
					isOther: businessCannel.indexOf('isOther') != -1 ? '0' : '1',

					businessLicense: businessLicense,
					licenseName: licenseName,
					shortName: shortName,
					licenseCode: licenseCode,
					licensePic: licensePic[0],

					bankCode: bankCode,
					companyBankName: companyBankName,
					companyBankNo: companyBankNo,
					corporatePic: corporatePic[0],

					idCardName: idCardName,
					idCardNo: idCardNo,
					idCardPic1: idCardPic1[0],
					idCardPic2: idCardPic2[0],

					bankName: bankName,
					bankNo: bankNo,
					bankPic: bankPic[0]
				};


				this.applyAxiosPost(postData);

			},

			//商家入住申请接口调用
			applyAxiosPost(postData) {
				console.log(postData);
				this.isApply = true
				_storeAplly({
					...postData
				}).then(res => {
					this.isApply = false
					if (res && res.code == 0) {
						this.$showToast(res.msg)
						setTimeout(() => {
							this.$navBack();
						}, 500)
					}
				}).catch(() => this.isApply = false)
			},
			// c查询商家信息
			getStoreInfos() {
				_getStoreInfo().then(res => {
					if (res && res.code == 0) {
						const {
							entryStatus,
							entryRemark
						} = res.data.tmerExtendEntity
						if (entryStatus) {
							this.showOverlay = true;
							this.entryStatus = entryStatus;
							this.entryRemark = entryRemark
						};
						// 赋值
						let data = res.data;
						let tmerExtendEntity = res.data.tmerExtendEntity;
						this.modelVal = {
							storeName: data.storeName,
							longitude: data.location.longitude,
							latitude: data.location.latitude,
							address: data.address,
							mobile: data.mobile,

							businessLicenseName: tmerExtendEntity.businessLicense == '10011001' ? '企业' :
								'个体工商户',
							businessLicense: tmerExtendEntity.businessLicense,

							licenseName: tmerExtendEntity.licenseName,
							licenseCode: tmerExtendEntity.licenseCode,

							bankCode: tmerExtendEntity.bankCode,
							companyBankNo: tmerExtendEntity.companyBankNo,
							bankCode: tmerExtendEntity.bankCode,
							bankNo: tmerExtendEntity.bankNo,
							companyBankName: tmerExtendEntity.companyBankName,
							bankName: tmerExtendEntity.bankName,
							shortName: tmerExtendEntity.shortName,

							idCardName: tmerExtendEntity.idCardName,
							idCardNo: tmerExtendEntity.idCardNo,

							doorPhotoUrl: data.doorPhotoUrl ? [data.doorPhotoUrl] : [],
							photosUrlList: data.photosUrlList,

							licensePic: tmerExtendEntity.licensePic ? [tmerExtendEntity.licensePic] : [],
							idCardPic1: tmerExtendEntity.idCardPic1 ? [tmerExtendEntity.idCardPic1] : [],
							idCardPic2: tmerExtendEntity.idCardPic2 ? [tmerExtendEntity.idCardPic2] : [],
							corporatePic: tmerExtendEntity.corporatePic ? [tmerExtendEntity.corporatePic] : [],
							bankPic: tmerExtendEntity.bankPic ? [tmerExtendEntity.bankPic] : [],
						};
						let businessCannel = [];
						if (data.isWash == '0') {
							businessCannel.push('isWash')
						}
						if (data.isUpkeep == '0') {
							businessCannel.push('isUpkeep')
						}
						if (data.isRepair == '0') {
							businessCannel.push('isRepair')
						}
						if (data.isOther == '0') {
							businessCannel.push('isOther')
						}
						this.modelVal.businessCannel = businessCannel;
					}
				})
			},
			// 
			closeReadnoew() {
				this.showOverlay = false
			},
			// 
			goUserXy(id) {
				this.$navTo('../xy/xy?id=' + id)
			},
			// 
			traggetRead() {
				this.modelVal.isRead = !this.modelVal.isRead
			}
		}
	}
</script>

<style lang="scss" scoped>
	.store {
		background-color: rgba(#f1f1f1, 0.6);
		padding-bottom: 240rpx;
		min-height: 100vh;

		.main {
			padding: 0 20rpx;
		}

		.titles {
			color: #999;
			margin: 30rpx 0 20rpx 30rpx;
		}

		.icon-weizhi {
			color: #f67112;
		}

		.cell-upload-item {
			padding: 20rpx;
			color: #646566;
			font-size: 24rpx;
		}

		.btn_now {
			position: fixed;
			bottom: 0rpx;
			padding: 20rpx 30rpx 50rpx;
			box-sizing: border-box;
			width: 100%;
			z-index: 200;
			background-color: #fff;

			.warp_xy {
				text-align: center;
				font-size: 24rpx;
				margin-bottom: 20px;

				.radiuss {
					border-radius: 50%;
					height: 25rpx;
					width: 25rpx;
					border: solid 4rpx #f67112;
				}

				.radios {
					width: 30rpx;
					height: 30rpx;
					margin-right: 10rpx;
				}

				.name_xy {
					color: $color-tt;
				}
			}

			.u-button--primary {
				width: 100%;
				background-color: #f67112;
				border-radius: 40rpx;
				border: solid 1px #f67112;
				color: #FFF;
			}
		}
	}

	.overlay {
		width: 100%;
		height: 100%;
		
		.entryStatus {
			color: #FFF;

			.iconfont {
				margin-bottom: 100rpx;
				font-size: 100rpx;
			}

			.entryRemark {
				width: 200rpx;
				height: 80rpx;
				background-color: $color-tt;
				border-radius: 10rpx;
			}
			
			.navBacks{
				color: #FFF;
				margin-top: 60rpx;
			}
		}
	}
</style>
