<template>
	<view class="store">
		<!-- 自定义导航 -->
		<uniNav title="开户信息" />

		<view class="main">
			<u--form labelPosition="left" :model="modelVal" labelWidth="100">
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
						<u-form-item label="" prop="idCardPic1">
							<UploadImage :width="309" :height="240" v-model="modelVal.idCardPic1" :size="50" :count="1">
								<template #handler="{ uploadHandler }"  v-if="modelVal.idCardPic1.length == 0">
									<u--image @click="uploadHandler()" src="/static/packageC/image/sfz@3x.png" width="309rpx" height="240rpx" />
								</template>
							</UploadImage>
						</u-form-item>
						<u-form-item label="" prop="idCardPic2">
							<UploadImage :width="309" :height="240" v-model="modelVal.idCardPic2" :size="50" :count="1">
								<template #handler="{ uploadHandler }" v-if="modelVal.idCardPic2.length == 0">
									<u--image @click="uploadHandler()" src="/static/packageC/image/sfz2@3x.png" width="309rpx" height="240rpx" />
								</template>
							</UploadImage>
						</u-form-item>
					</view>
				</view>
				<view class="titles">企业信息</view>
				<view class="-warp20-30 -bgf" style="border-radius: 10rpx;">
					<u-form-item label="" prop="licensePic">
						<UploadImage :width="309" :height="240" v-model="modelVal.licensePic" :size="50" :count="1">
							<template #handler="{ uploadHandler }" v-if="modelVal.licensePic.length == 0">
								<u--image @click="uploadHandler()" src="/static/packageC/image/yyzz@3x.png" width="309rpx" height="240rpx" />
							</template>
						</UploadImage>
					</u-form-item>
					<u-form-item label="营业执照" prop="businessLicenseName" @click="showPicket" borderBottom>
						<u--input inputAlign="right" disabled disabledColor="#ffffff"
							v-model="modelVal.businessLicenseName" border="none" placeholder="营业执照类型"></u--input>
						<u-icon slot="right" name="arrow-down" size="18" color="#f67112" />
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
				</view>
				<view class="titles">银行卡信息</view>
				<view class="-warp20-30 -bgf" style="border-radius: 10rpx;">
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
						<u-form-item label="" prop="bankPic">
							<UploadImage :width="309" :height="240" v-model="modelVal.bankPic" :size="50" :count="1">
								<template #handler="{ uploadHandler }" v-if="modelVal.bankPic.length == 0">
									<view class="newPic">
										<u--image @click="uploadHandler()" src="/static/packageC/image/card@3x.png" width="309rpx"
											height="240rpx" />
										<text class="tips">上传银行卡图片</text>
									</view>
								</template>
							</UploadImage>
						</u-form-item>
					</template>
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
						<u-form-item label="" prop="corporatePic">
							<UploadImage :width="309" :height="240" v-model="modelVal.corporatePic" :size="50"
								:count="1">
								<template #handler="{ uploadHandler }" v-if="modelVal.corporatePic.length == 0">
									<view @click="uploadHandler()" class="newPic">
										<u--image src="/static/packageC/image/card@3x.png" width="309rpx"
											height="240rpx" />
										<text class="tips">上传对公账户证明</text>
									</view>
								</template>
							</UploadImage>
						</u-form-item>
					</template>
				</view>

			</u--form>
		</view>
		<!-- 入住按钮 -->
		<view class="btn_now">
			<u-button @click="storeApplyNow" :loading="isApply" :disabled="isApply" loading-text="入住申请中..."
				type="primary">确认</u-button>
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
				</view>
			</view>
		</u-overlay>
	</view>
</template>

<script>
	import {
		_getbank,
		_updateCompanyInfo,
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
					storeId: '',
					longitude: '',
					latitude: '',

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
		},
		onLoad(options) {
			this.getStoreInfos();
		},
		onShow() {

		},
		methods: {
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
					storeId,
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


				let postData = {
					storeId: storeId,
					longitude: longitude,
					latitude: latitude,

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
				this.isApply = true
				_updateCompanyInfo({
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
						// 赋值
						let data = res.data;
						let tmerExtendEntity = data.tmerExtendEntity;

						const {
							yopMerStatus,
							yopRemark
						} = tmerExtendEntity
						if (yopMerStatus != 3) {
							this.showOverlay = true;
							this.entryStatus = yopMerStatus;
							this.entryRemark = yopRemark
						};

						Object.keys(this.modelVal).forEach((item) => {
							console.log(item, tmerExtendEntity[item])
							if (tmerExtendEntity[item]) {
								this.modelVal[item] = tmerExtendEntity[item] || '';
							}
							if (data[item]) {
								this.modelVal[item] = data[item] || '';
							}
						})

						this.modelVal.businessLicenseName = tmerExtendEntity.businessLicense == '10011001' ? '企业' :
							'个体工商户';
						this.modelVal.licensePic = tmerExtendEntity.licensePic ? [tmerExtendEntity.licensePic] :
					[];
						this.modelVal.idCardPic1 = tmerExtendEntity.idCardPic1 ? [tmerExtendEntity.idCardPic1] :
					[];
						this.modelVal.idCardPic2 = tmerExtendEntity.idCardPic2 ? [tmerExtendEntity.idCardPic2] :
					[];
						this.modelVal.corporatePic = tmerExtendEntity.corporatePic ? [tmerExtendEntity
							.corporatePic] : [];
						this.modelVal.bankPic = tmerExtendEntity.bankPic ? [tmerExtendEntity.bankPic] : [];
					}
				})
			},
			// 
			closeReadnoew() {
				this.showOverlay = false
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
		.newPic{
		    position: relative;
		    .tips{
		      font-size: 28rpx;
		      color: #FFF;
		      width: 100%;
		      position: absolute;
		      bottom: 0;
		      left: 0;
		      line-height: 60rpx;
		      text-align: center;
		    }
		  }

		.btn_now {
			position: fixed;
			bottom: 100rpx;
			padding: 0 30rpx;
			box-sizing: border-box;
			width: 100%;
			z-index: 200;

			.u-button--primary {
				width: 100%;
				color: #FFF;
				background-color: #f67112;
				border-radius: 40rpx;
				border: solid 1px #f67112;
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
		}
	}
</style>
