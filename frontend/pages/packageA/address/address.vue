<template>
	<view class="address">
		<!-- 自定义导航 -->
		<uniNav title="收货地址" />

		<view class="warp_main">
			<LoadMoreList :page="page" :data="page.list">
				<view class="addres_info f_c_a mb24" v-for="item in page.list" :key="item.addrId">
					<view class="f f_column" @click="addressDetail(item)">
						<view class="f f_align_center">
							<text>{{item.userName}}</text>
							<text class="ml8">{{item.userMobile}}</text>
							<view class="isDefault ml8" v-if="item.isDefault == 0">默认</view>
						</view>
						<view class="overfont-2 mt12">
							{{item.provinceName}}{{item.cityName}}{{item.districtName}}{{item.addrDetail}}
						</view>
					</view>
					<u--image @click="addressDetailJump(item.addrId)"
						src="/static/packageA/image/icon_xiugai.png" width="20" height="20" />
				</view>
			</LoadMoreList>
		</view>

		<!-- 新增地址 -->
		<view class="btn_add w100">
			<u-button @click="addAddress" class="f_c_c w100 add_addres">新增地址</u-button>
		</view>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		_listAddress,
		_removeAddress
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				set: false,
				page: {
					pageIndex: 1,
					limit: 10,
					totalPage: 1,
					isLoad: false,
					isDone: false,
					list: [],
				},
				options2: [{
						text: '修改',
						style: {
							backgroundColor: '#f67112'
						}
					},
					{
						text: '删除',
						style: {
							backgroundColor: '#e71c19'
						}
					}
				]
			};
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue')
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {
			if (options && options.set) {
				this.set = true
			}
		},
		onShow() {
			this.addressList();
		},
		methods: {
			// 
			addressList() {
				this.page.isLoad = true
				const {
					pageIndex,
					limit
				} = this.page;
				_listAddress({
					page: pageIndex,
					limit: limit
				}).then(res => {
					this.page.isLoad = false
					if (res && res.code == 0) {
						let lists = res.data && res.data.list;
						this.page.totalPage = res.data.totalPage || 0
						if (pageIndex <= 1) {
							this.page.list = lists
						} else {
							let o_list = this.page.list;
							this.page.list = [...o_list, ...lists];
						}
						if (lists.length < limit) {
							this.page.isDone = true
						}
					}
				}).catch(() => this.page.isLoad = false)
			},
			addAddress() {
				this.$navTo('../addAddress/addAddress')
			},
			handlers(val) {
				console.log(val)
				const {
					index,
					name
				} = val;
				if (index == 0) {
					this.addressDetailJump(name)
				} else {
					this.onClose(name)
				}
			},
			// 点击
			addressDetail(item) {
				if (this.set) {
					uni.setStorageSync('addressInfo', item);
					this.$navBack();
				}
			},

			// 去详情
			addressDetailJump(addrId) {
				this.$navTo(`../addAddress/addAddress?addrId=${addrId}`)
			},
			// 删除
			onClose(item) {
				console.log(item);
				const _this = this;
				uni.showModal({
					title: '删除',
					content: '删除地址后，需要重新添加',
					confirmText: '立即删除',
					confirmColor: '#f67112',
					success: function(res) {
						if (res.confirm) {
							_removeAddress(item).then(res => {
								if (res && res.code == 0) {
									_this.page.pageIndex = 1
									_this.addressList();
									_this.$showToast('删除地址成功～');
									let addres = uni.getStorageSync('addressInfo');
									if (addres && addres != '') {
										if (item == addres.addrId) {
											uni.removeStorageSync('addressInfo')
										}
									}

								}
							})
						}
					}
				})
			}
		},
		/**
		 * 页面相关事件处理函数--监听用户下拉动作
		 */
		onPullDownRefresh: function() {
			this.page.pageIndex = 1
			this.addressList();
		},

		/**
		 * 页面上拉触底事件的处理函数
		 */
		onReachBottom: function() {
			let {
				pageIndex,
				totalPage
			} = this.page;
			if (pageIndex < totalPage) {
				this.page.pageIndex++
				this.addressList();
			} else {
				this.page.isDone = true
			}
		},
	}
</script>

<style lang="scss" scoped>
	.address {
		min-height: 100vh;
		background-color: $bg-color-fa;
		padding-bottom: 140rpx;
		box-sizing: border-box;

		.warp_main {
			padding: 30rpx;
			.addres_info {
				padding: 24rpx;
				background-color: #fff;
				border-radius: 12rpx;

				.isDefault {
					padding: 4rpx 10rpx;
					background-color: $color-tt;
					color: #FFF;
					font-size: 20rpx;
					border-radius: 10rpx;
				}
			}

			.r_text_style {
				background-color: $font-money-color2;
				color: #fff;
				width: 130rpx;
				height: 100%;

				.r_text_style.edit {
					background-color: $color-tt;
				}
			}
		}




		.btn_add {
			box-sizing: border-box;
			padding: 30rpx 30rpx 60rpx;
			position: fixed;
			bottom: 0;
			z-index: 100;

			.add_addres {
				background-color: $color-tt;
				box-sizing: border-box;
				color: #FFF;
				font-size: 32rpx;
				border-radius: 12rpx;
			}
		}
	}
</style>
