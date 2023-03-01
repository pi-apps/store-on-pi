<template>
	<view class="storeMange">
		<!-- 自定义导航 -->
		<uniNav title="门店管理" bgColor="#f67112" color="#FFF" />

		<view class="storeMangeView" height="150">
			<view class="imgs_warps">
				<u--image class="f_c_c" :src="storeInfo.doorPhotoUrl" height="100" width="100" shape="circle" />
			</view>
		</view>
		<!-- 门店入驻状态信息 -->
		<view class="f f_column f_c_c storeStatus" v-if="storeInfo.sysStatus == '2'">
			<view>开户状态：<text :class="storeInfo.tmerExtendEntity.yopMerStatus == '2' ? 'isOpen' : 'isNone'">{{storeInfo.tmerExtendEntity.yopMerStatus | ybExmaineStatus}}</text>
			</view>
			<view  @click="$navTo('../storeInfoAdd/storeInfoAdd')" class="store_btn f_c_c" v-if="(storeInfo.tmerExtendEntity.yopMerStatus == '3' || storeInfo.tmerExtendEntity.yopMerStatus == '1') && (storeInfo.isOld == '0' || storeInfo.isOld == '2')">申请开户</view>
			<view @click="$navTo('../storeOrder/storeOrder')" class="store_btn f_c_c" v-if="storeInfo.tmerExtendEntity.yopMerStatus == '2'">到账明细</view>
		</view>
		<!-- 门店 -->
		<view class="-warp30 mt32">
			<u-cell-group type="card">
				<u-cell title="门店名称" >
					<template #value>
						<view style="width:400rpx;" class="overfont">{{storeInfo.storeName}}</view>
					</template>
				</u-cell>
				<u-cell title="联系电话" :value="storeInfo.mobile" />
				<u-cell title="门店地址">
					<template #value>
						<view style="width:400rpx;" class="overfont">{{storeInfo.address}}</view>
					</template>
				</u-cell>
				<u-cell title="营业时间" :value="startTime+'-'+endTime" isLink @click="showActionTime" />
				<u-cell title="门店状态" is-link @click="showAction">
					<view slot="value">
						{{ storeInfo.sysStatus == '1' ? '平台禁用' : (storeInfo.status == '0' ? '上线营业中' : '下线中') }}
					</view>
				</u-cell>
				<u-cell v-if="storeInfo.sysStatus == '2' && (storeInfo.tmerExtendEntity.yopMerStatus == '3' || storeInfo.tmerExtendEntity.yopMerStatus == '1') && (storeInfo.isOld == '0' || storeInfo.isOld == '2')" title="可提现（元）" :border="false">
					<view slot="value" class="f f_align_center f_end">
						<text>{{ oldStoreBalance.toFixed(2) }}</text>
						<view @click="$navTo('../storeInfoAdd/storeInfoAdd')" class="cashAmount f_c_c fz16">提现</view>
					</view>
				</u-cell>
			</u-cell-group>
			<!-- 修改门店 -->
			<u-button @click="modifyStore" class="f_c_c w100 realy_buy">保存修改 </u-button>
		</view>

		<u-action-sheet description="修改门店的营业状态～" :show="actionSheel" :actions="actions" cancel-text="取消"
			@select="selectInfo" @close="cancel" round="12" />

		<u-datetime-picker :show="show" @cancel="cancelTime" @confirm="startTimeChange" title="开始时间" mode="time"
			v-model="startTime" />
		<u-datetime-picker :show="timeShow" @cancel="cancelTime" @confirm="endTimeChange" title="结束时间" mode="time"
			v-model="endTime" />
	</view>
</template>

<script>
	import {
		_getStoreInfo,
		_switchStore,
		_updateOpenTime,_getBalance
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				show: false,
				timeShow: false,
				storeInfo: {
					tmerExtendEntity:{
						yopMerStatus:''
					}
				},
				actionSheel: false,
				actions: [],
				startTime: '09:00',
				endTime: '20:00',
				oldStoreBalance:0
			};
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {

		},
		onShow() {
			this.getStoreInfo();
		},
		methods: {
			// 修改门店
			modifyStore() {
				const {
					startTime,
					endTime,
					storeInfo
				} = this;
				_updateOpenTime({
					storeId: storeInfo.storeId,
					openTimeStart: startTime,
					openTimeEnd: endTime,
				}).then(res => {
					if (res && res.code == 0) {
						this.$showToast('修改成功～');
						this.getStoreInfo();
					}
				})
			},

			// 查询门店信息
			getStoreInfo() {
				_getStoreInfo().then(res => {
					if (res && res.code == 0) {
						this.storeInfo = res.data;
						this.startTime = res.data.openTimeStart;
						this.endTime = res.data.openTimeEnd
						// 老系统查询可提现余额
						if(res.data.sysStatus == '2' && res.data.isOld == '0'){
						  _getBalance().then(res=>{
							if(res && res.code == '0'){
							  this.oldStoreBalance =res.data
							}
						  })
						}
					}
				})
			},
			showAction() {
				if (this.storeInfo.sysStatus == '1') {
					return this.$showToast('门店已被平台禁用，无法修改～')
				}
				if (this.storeInfo.status == '0') {
					this.actionSheel = true;
					this.actions = [{
						name: '门店下线'
					}]
				} else {
					this.actionSheel = true;
					this.actions = [{
						name: '门店上线营业'
					}]
				}
			},
			cancel() {
				this.actionSheel = false
			},
			selectInfo(e) {
				console.log(e)
				_switchStore().then(res => {
					if (res && res.code == 0) {
						this.$showToast('操作成功～');
						this.cancel();
						this.getStoreInfo();
					}
				})
			},
			showActionTime() {
				this.show = true
			},
			cancelTime() {
				this.show = false;
				this.timeShow = false
			},
			startTimeChange(e) {
				this.startTime = e.value;
				this.timeShow = true
			},
			endTimeChange(e) {
				console.log(e)
				let sArr = this.startTime.split(':');
				let eArr = e.value.split(':');
				if (Number(sArr[0]) > Number(eArr[0])) {
					return this.$showToast('结束时间要大于开始时间！')
				}
				if ((Number(sArr[0]) == Number(eArr[0])) && (Number(sArr[1]) >= Number(eArr[1]))) {
					return this.$showToast('结束时间要大于开始时间！')
				}
				this.endTime = e.value;
				this.timeShow = false;
				this.show = false
			}
		}
	}
</script>

<style lang="scss" scoped>
	.storeMange {
		.storeMangeView {
			width: 100%;
			height: 300rpx;
			background: url('@/static/image/home/homePage.png') no-repeat center;
			background-size: 100% 100%;
		}

		.imgs_warps {
			position: relative;
			top: 160rpx;
		}
		.storeStatus{
		    color: #191919;
		    font-size: 32rpx;
		    margin-top: 60rpx;
		    .isOpen{
		      color: #00CC26;
		    }
		    .isNone{
		      color: #F67114;
		    }
		    .store_btn{
		      color: #F67114;
		      width: 200rpx;height: 60rpx;
		      border-radius: 30rpx;
		      background-color: #F8CAAA;
		      margin-top: 40rpx;
		    }
		  }
		  .cashAmount{
		    border: solid 1rpx #FFD0AF;
		    border-radius: 30rpx;
		    width: 160rpx;
		    height: 60rpx;
		    margin-left: 30rpx;
		    color: #F78E45;
		
		  }
		.realy_buy {
			background-image: linear-gradient(to bottom, #fa4935, #fb8363);
			border-radius: 18rpx;
			color: #FFF;
			font-size: 32rpx;
			margin-top: 200rpx;
		}
	}
</style>
