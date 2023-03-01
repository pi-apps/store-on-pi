<template>
	<view class="myNextStore">
		<!-- 自定义导航 -->
		<uniNav type="search" class="dropMenu" placeholder="通过手机号搜索商家" @search="storeMainListKeyWord"
			@clear="storeMainListKeyWord" />

		<view class="fansList -warp30">
			<LoadMoreList :page="page" :data="page.list">
				<view class="mb16 storeWarp -bgf -warp -box f f_column w100" v-for="item in page.list" :key="item.userId">
					<view class="fz12 colors">
						<u-icon class="mr4" name="clock" color="#999" />入驻时间：{{item.createTime}}
					</view>
					<view class="mt12 f f_align_center" style="flex-direction: row;">
						<u--image radius="12" width="70" height="70" :src="item.doorPhotoUrl"></u--image>
						<view class="f_column f ml20 f_align" style="height: 60px;">
							<text class="overfont-2 fz16 bold">{{item.storeName}}</text>
							<text>{{$phoneFormat(item.mobile)}}</text>
						</view>
					</view>
				</view>
			</LoadMoreList>
		</view>
	</view>
</template>

<script>
	import {
		_myExpandStoreList
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				mobile: '',
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
		onShow() {
			this.init();
		},
		methods: {
			init() {
				this.page.pageIndex = 1;
				this.getNextStoreList();
			},
			//
			storeMainListKeyWord(val) {
				this.page.pageIndex = 1;
				this.mobile = val;
				this.getNextStoreList();
			},
			// 
			getNextStoreList() {
				this.page.isLoad = true
				const {
					pageIndex,
					limit
				} = this.page;
				_myExpandStoreList({
					page: pageIndex,
					limit: limit,
					mobile: this.mobile
				}).then(res => {
					this.page.isLoad = false;
					if (res && res.code == 0) {
						let lists = res.data && res.data.list;
						this.page.totalPage = res.data.totalPage || 0;
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
				this.page.pageIndex++;
				this.getNextStoreList();
			} else {
				this.page.isDone = true
			}
		},
	}
</script>

<style lang="scss" scoped>
	.myNextStore {
		min-height: 100vh;
	}
	/deep/ .fansList{
		.storeWarp {
			.colors {
				color: #999;
				display: flex;
				flex-direction: row;
				align-items: center;
			}
		}
	}
</style>
