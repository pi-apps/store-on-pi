<template>
	<view class="template">
		<!-- 自定义导航 -->
		<uniNav title="商品名称" />
		<view class="-warp20-30 search_warp">
			<u-search v-model="value" :focus="true" :animation="true" :show-action="true" actionText="搜索" placeholder="请输入商品名称关键字进行搜索"
				@search="onSearch" @custom="onSearch" />
				
			<LoadMoreList class="mt32" icon="/static/image/empty-image/no_search.png" :page="page" :data="page.list">
				<view class="keywords" v-for="(item,index) in page.list" :key="index" @click="chooseKeyword(item)" >{{item.serviceName}}</view>
			</LoadMoreList>
		</view>
	</view>
</template>

<script>
	import {
		_keywordList
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				businessChannelType:'',
				value: '',
				searchData: [],
				
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
		mounted() {
			// h5原生方法调用回调
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue')
		},
		onLoad(options) {
			if (options && options.businessChannelType) {
				this.businessChannelType = options.businessChannelType
			}
		},
		onShow() {

		},
		methods: {
			onSearch(val) {
				console.log(val)
				this.value = val
				_keywordList({
					businessType: this.businessChannelType,
					serviceName: val,
					limit: 15,
					page: 1
				}).then(res => {
					if (res && res.code == 0) {
						let list = res.data.list;
						list.unshift({
							serviceName: val
						});
						this.page.list = list;
					}
				})
			},
			chooseKeyword(item) {
				if (item) {
					uni.setStorageSync('goodsName', item.serviceName);
					this.$navBack();
				}
			},
		}
	}
</script>

<style lang="scss" scoped>
.searchInput{
  height: 62rpx;
  margin: 15rpx 0;
  border: solid 1rpx rgba(#999,0.2);
  border-radius: 60rpx;
  font-size: 28rpx;
}
.search_warp{
  .keywords{
    padding: 20rpx 0;
  }
}

</style>
