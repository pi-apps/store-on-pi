<template>
	<view class="">
		<!-- 自定义导航 -->
		<uniNav title="预期收益" />
		
		<view class="myIncome">
			<LoadMoreList :data="page.list" :page="page" >
				<view class="myIncome-item f_c_a" v-for="(item,index) in  page.list" :key="index">
				  <u-image src="/static/packageA/image/fulihongbao@2x.png" width="50" height="50" />
				  <view class="f_c_a w100 ml12">
				    <view class="f_column f">
				      <text class="bold fz16">{{item.showText}}</text>
				      <text class="mt10 time">{{item.createTime}}</text>
				    </view>
				    <view class="price">+<text class="bold fz20">{{item.showAmount}}</text>  元</view>
				  </view>
				</view>
			</LoadMoreList>
		</view>
	</view>
</template>

<script>
	import { _sumAmountList } from '@/http/apiReq'
	export default {
		data() {
			return {
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
			
		},
		onShow() {
			this.init()
		},
		methods: {
			init() {
				this.page.pageIndex = 1;
				this.getFansList();
			},
			// 
			getFansList() {
				this.page.isLoad = true
				const {
					pageIndex,
					limit
				} = this.page;
				_sumAmountList({
					page: pageIndex,
					limit: limit
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
				this.getFansList();
			} else {
				this.page.isDone = true
			}
		},
	}
</script>

<style lang="scss" scoped>
	.myIncome{
	  min-height: 100vh;
	  padding: 20rpx 36rpx;
	  color: #292929;
	}
	.myIncome-item{
	    padding: 36rpx 24rpx;
	    background-color: #fff;
	    margin-bottom: 10rpx;
	    border-radius: 12rpx;
	}
	
	.price{
	  color: #FA3F38;
	}
	.time{
	  color: #8D8D8D;
	}
</style>
