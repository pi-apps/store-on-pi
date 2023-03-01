<template>
	<view class="addAddress">
		<!-- 自定义导航 -->
		<uniNav :title="title" />
		<view class="-warp20-30 -bgf">
			<u--form labelPosition="left" :model="modelVal" labelWidth="90">
				<u-form-item label="联系人" prop="userName">
					<u--input inputAlign="right" :maxlength="10" v-model="modelVal.userName" border="none" placeholder="请输入联系人"></u--input>
				</u-form-item>
				<u-form-item label="手机号" prop="userMobile">
					<u--input inputAlign="right" :maxlength="11" v-model="modelVal.userMobile" border="none" placeholder="请输入手机号"></u--input>
				</u-form-item>
				<u-form-item label="请选择地区" prop="addrStr" @click="showBankPick">
					<u--input inputAlign="right" disabled disabledColor="#ffffff" v-model="modelVal.addrStr" border="none" placeholder="请选择地区信息"></u--input>
					<u-icon slot="right" name="arrow-right" />
				</u-form-item>
				
				<u-form-item label="详细地址" prop="addrDetail">
					<u--textarea autoHeight inputAlign="right" confirmType="done" v-model="modelVal.addrDetail" border="none" placeholder="请输入详细地址" ></u--textarea>
				</u-form-item>
				
			</u--form>
			
			
		</view>
		
		<view class="-warp30 -bgf f_c_a mt12">
		    <view>是否默认</view>
		    <u-switch v-model="isDefault" active-color="#EC661B" @change="onChange" size="20" />
		</view>
		<!-- 新增地址 -->
		<view class="btn_add w100 f_column f_c_c">
			<u-button :loading="loading" :loading-text="addrId != '' ? '修改中...' : '新增中...'" @click="saveAdress" class="f_c_c w100 add_addres">{{addrId != '' ? '修改地址' : '新增地址'}}</u-button>
			<view v-if="addrId != ''" class="deletes" @click="onClose">删除地址</view>
		</view>
		<!--  -->
		<addressPick v-model="areaCode" :show.sync="show" @confirmZh="getAddresName"  />
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import { _addAddress, _modifyAddress, _getAddress,_removeAddress } from '@/http/apiReq'
	export default {
		data() {
			return {
				loading:false,
				
				title: '新增收货地址',
				show: false,
				actionInfo: ['', '', ''],


				addrId: '',
				modelVal:{
					userName: '',
					userMobile: '',
					addrDetail: '',
					addrStr:''
				},
				areaCode:['','',''],
				isDefault: false
			};
		},
		computed: {
			...mapState({
				statusBarHeight: state => state.statusBarHeight
			})
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue'),
			addressPick: () => import('@/components/AddressPicker/AddressPicker.vue')
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {
			if (options && options.addrId) {
				this.title = '修改收货地址';
				this.addrId = options.addrId;
				this.getAddressInfo();
			}
		},
		onShow() {

		},
		methods: {
			onChange(item) {
				// 需要手动对 checked 状态进行更新
				this.isDefault = item
			},
			// 省市县
			showBankPick() {
				this.show = true
			},
			getAddresName(val) {
				this.modelVal.addrStr = val.join('-');
			},
			//保存
			async saveAdress() {
				const { userName,userMobile,addrDetail  } = this.modelVal;
				
				if (userName == '') {
					return this.$showToast('请输入联系人');
				}
				if (userMobile == '') {
					return this.$showToast('请输入联系人手机号');
				} 
				if (!/^1[3,4,5,6,7,8,9]\d{9}$/.test(userMobile)) {
					return this.$showToast('请输入正确的手机号码');
				} 
				if (this.areaCode.length < 3 && this.areaCode[0] == '') {
					return this.$showToast('请先选择所在地区');
				}
				if (addrDetail == '') {
					return this.$showToast('请输入详细的收货地址');
				};
				let postData = {
					userMobile: userMobile,
					userName: userName,
					addrDetail: addrDetail,
					isDefault: this.isDefault ? '0' : '1',
					provinceCode: this.areaCode[0],
					cityCode: this.areaCode[1],
					districtCode: this.areaCode[2]
				}
				let res = null;
				if (this.addrId && this.addrId != '') {
					postData.addrId = this.addrId;
					res = await _modifyAddress(postData);
				} else {
					res = await _addAddress(postData);
				}
				if (res && res.code == 0) {
					this.$showToast('成功');
					setTimeout(() => {
						this.$navBack();
					}, 1000)
				}
			},
			// 查询地址信息
			getAddressInfo() {
				_getAddress(this.addrId).then(res => {
					if (res && res.code == 0) {
						let datas = res.data;
						const {
							userMobile,
							userName,
							addrDetail,
							isDefault,
							provinceCode,
							cityCode,
							districtCode
						} = datas;
						
						this.modelVal.userMobile = userMobile;
						this.modelVal.userName = userName;
						this.modelVal.addrDetail = addrDetail;
						this.modelVal.addrStr = datas.provinceName + '-' + datas.cityName + '-' + datas.districtName;
						
						this.isDefault = isDefault == 0 ? true : false;
						this.areaCode = [provinceCode,cityCode,districtCode];
					}
				})
			},
			// 删除
			onClose() {
				const _this = this;
				uni.showModal({
					title: '删除',
					content: '删除地址后，需要重新添加',
					confirmText: '立即删除',
					confirmColor: '#f67112',
					success: function(res) {
						if (res.confirm) {
							_removeAddress(_this.addrId).then(res => {
								if (res && res.code == 0) {
									_this.$showToast('删除地址成功～');
									let addres = uni.getStorageSync('addressInfo');
									if (addres && addres != '') {
										if (_this.addrId == addres.addrId) {
											uni.removeStorageSync('addressInfo')
										}
									}
									setTimeout(()=>{
										_this.$navBack();
									},400)
								}
							})
						}
					}
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.addAddress {
		min-height: 100vh;
		background-color: $bg-color-fa;
		color: #646566;

		.btn_add {
			box-sizing: border-box;
			padding: 30rpx 30rpx 60rpx;
			.deletes{
				font-size: 32rpx;
				margin-top: 24rpx;
				color: $font-money-color2;
			}

			.add_addres {
				height: 100rpx;
				background-color: $color-tt;
				box-sizing: border-box;
				color: #FFF;
				font-size: 32rpx;
				border-radius: 12rpx;
			}
		}
	}
</style>
