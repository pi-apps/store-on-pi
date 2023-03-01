<template>
	<view class="setName">
		<!-- 自定义导航 -->
		<uniNav :title="$t('pages.bindUser.title')" />

		<view class="-warp30">

			<u--input v-model="userName" :maxlength="30" class="warpInput" :placeholder="$t('pages.bindUser.placholder')" />
			<u-button :loading="loading" :disabled="loading" loading-text="修改中..." @click="updateUserName"
				class="f_c_c w100 realy_buy bold">{{$t('pages.bindUser.confirm')}}</u-button>
		</view>
	</view>
</template>

<script>
	import {
		bindingParentId
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				loading: false,
				userName: ''
			};
		},
		onShow() {
			// let userInfo = wx.getStorageSync('UserInfo')
			// if (userInfo && userInfo.userId) {
			// 	this.userName = userInfo.nickName
			// }
		},
		methods: {
			// 
			updateUserName() {
				this.loading = true
				bindingParentId(this.userName).then(res => {
					this.loading = false
					if (res && res.code == 0) {
						this.$showToast(this.$t('pages.bindUser.success'))
						setTimeout(() => {
							this.$navBack();
						}, 400)
					}
				}).catch(() => {
					this.loading = false
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	.setName {
		.warpInput {
			padding: 20rpx;
			margin-bottom: 120rpx;
			margin-top: 50rpx;
			border: 1px solid #fb8363;
			border-radius: 22rpx;
			font-size: 16px;
		}

		.realy_buy {
			background-image: linear-gradient(to bottom, #fa4935, #fb8363);
			border-radius: 18rpx;
			color: #FFF;
			font-size: 32rpx;
		}
	}
</style>
