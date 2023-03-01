<template>
	<view class="address">
		<!-- 自定义导航 -->
		<uniNav title="提现方式管理" />

		<view class="warp_main">
			<LoadMoreList :page="page" :data="page.list">
				<view class="mb24" v-for="item in page.list" :key="item.cardId">
					<view @click="cardInfos(item)" class="methdos_info alipy f_c_a"
						v-if="item.cashCardType == '10018001'">
						<view class=" f f_align_start">
							<u-icon class="mr32" size="34" color="#FFF" name="zhifubao-circle-fill" />
							<view class="f f_column">
								<text class="fz18 bold">支付宝</text>
								<text class="mt32">{{item.alipayPaymentAccount}}</text>
							</view>
						</view>
						<u-icon name="edit-pen-fill" size="20" color="#FFF" />
					</view>
					<view @click="cardInfos(item)" class="methdos_info f_c_a" v-else>
						<view class=" f f_align_start">
							<i class="iconfont icon-yinhangqia mr32"></i>
							<view class="f f_column">
								<text class="fz18 bold">{{item.bankName}}</text>
								<text class="mt32">{{item.bankcardNum}}</text>
							</view>
						</view>
						<u-icon name="edit-pen-fill" size="20" color="#FFF" />
					</view>
				</view>
			</LoadMoreList>
		</view>

		<!-- 新增 -->
		<view class="btn_add w100">
			<u-button @click="addAction" class="f_c_c w100 add_addres">新增</u-button>
		</view>
		<!--  -->
		<u-action-sheet cancel-text="取消" :show="actionSheet" :actions="businessChannel" description="选择添加的方式"
			@close="actionCancel" @select="actionSelect" />
	</view>
</template>

<script>
	import {
		_getCardList
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				set: false,
				actionSheet: false,
				businessChannel: [{
						name: '支付宝',
						value: '10018001'
					},
					{
						name: '银行卡',
						value: '10018002'
					}
				],
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
				],
				page: {
					pageIndex: 1,
					limit: 10,
					totalPage: 1,
					isLoad: false,
					isDone: false,
					list: [],
				},
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
			this.init();
		},
		methods: {
			init() {
				this.page.pageIndex = 1
				this.getCardList();
			},

			// 
			getCardList() {
				this.page.isLoad = true
				const {
					pageIndex,
					limit
				} = this.page;
				_getCardList({
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
							this.page.list = [...o_list, ...lists]
						}
						if (lists.length < limit) {
							this.page.isDone = true
						}
					}
				}).catch(() => this.page.isLoad = false)
			},
			// 
			addAction() {
				this.actionSheet = true
			},
			// 
			actionCancel() {
				this.actionSheet = false
			},
			// 
			actionSelect(e) {
				const {
					name,
					value
				} = e;
				this.actionCancel();
				this.$navTo(`../addMeth/addMeth?name=${name}&cashCardType=${value}`)
			},
			//
			handlers(val) {
				console.log(val)
				const {
					index,
					name
				} = val;
				if (index == 0) {
					let item = JSON.parse(name);
					this.$navTo(`../addMeth/addMeth?cardId=${item.cardId}&cashCardType=${item.cashCardType}`)
				} else {
					this.onClose(JSON.parse(name))
				}
			},
			// 
			cardInfos(item) {
				if (this.set) {
					uni.setStorageSync('cards', item);
					this.$navBack();
				} else {
					this.$navTo(`../addMeth/addMeth?cardId=${item.cardId}&cashCardType=${item.cashCardType}`)
				}
			},
		},
		/**
		 * 页面相关事件处理函数--监听用户下拉动作
		 */
		onPullDownRefresh: function() {
			this.page.pageIndex = 1
			this.init();
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
				this.getCardList();
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

		.icon-yinhangqia {
			font-size: 34px;
			color: #FFF;
		}

		.warp_main {
			padding: 30rpx;

			.methdos_info {
				padding: 24rpx;
				background-color: $color-tt;
				box-shadow: 0 4rpx 4rpx $color-tt;
				border-radius: 12rpx;
				color: #FFF;

				&.alipy {
					background-color: #316bf3;
					box-shadow: 0 4rpx 4rpx #316bf3;
				}



			}

			.r_text_style {
				background-color: $font-money-color2;
				color: #fff;
				width: 130rpx;
				height: 100%;
				border-radius: 12rpx;
				font-size: 36rpx;
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
