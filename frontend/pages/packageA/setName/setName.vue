<template>
	<view class="setName">
		<!-- 自定义导航 -->
		<uniNav title="修改昵称" />

		<view class="-warp30">

			<u--input v-model="userName" :maxlength="10" class="warpInput" placeholder="请输入新昵称" />
			<u-button :loading="loading" :disabled="loading" loading-text="修改中..." @click="updateUserName"
				class="f_c_c w100 realy_buy bold">确认修改</u-button>
		</view>
	</view>
</template>

<script>
	import {
		_updateUser
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				loading: false,
				userName: ''
			};
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {

		},
		onShow() {
			let userInfo = wx.getStorageSync('UserInfo')
			if (userInfo && userInfo.userId) {
				this.userName = userInfo.nickName
			}
		},
		methods: {
			// 
			updateUserName() {
				this.loading = true
				_updateUser({
					nickName: this.userName
				}).then(res => {
					this.loading = false
					if (res && res.code == 0) {
						this.$showToast('昵称修改成功～')
						setTimeout(() => {
							this.$tabTo('/pages/user/user');
						}, 700)
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
