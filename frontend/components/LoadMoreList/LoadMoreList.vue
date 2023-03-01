<template>
	<view class="loadMoresList w100">
		<view>
			<u-skeleton
				v-for="(item, index) in 4" :key="index"
				:rows="row" title :loading="page.isLoad && page.pageIndex <= 1" avatar :avatarSize="avatarSize"
				:avatarShape="avatarShape"
			></u-skeleton>
			<u-skeleton
				:rows="row" title :loading="page.isLoad && page.pageIndex <= 1" avatar :avatarSize="avatarSize"
				:avatarShape="avatarShape"
			>
				<slot></slot>
			</u-skeleton>
		</view>
		<view class="loadMores">
			<!-- 空状态 -->
			<u-empty :icon="icon" width="300rpx" height="300rpx" :text="noMoreText" v-if="!page.isLoad && data.length <= 0" :mode="empytType">
				<slot name="empty"></slot>
			</u-empty>
			<!-- 加载更多状态 -->
			<u-loadmore v-else :status="uLoadStatus" :loading-text="loadText" :loadmore-text="loadMoreText" :nomore-text="noMoreText" :line="true" />
			<slot name="handler" v-if="data.length <= 0"></slot>
		</view>
		
	</view>
</template>

<script>
	export default {
		name: "LoadMoreList",
		props: {
			icon: {
				type: String,
				default: '/static/image/empty-image/zanwushuju@2x.png'
			},
			empytType: {
				type: String,
				default:'search'
			},
			loadMoreText: {
				type: String,
				default: '滚动查看更多',
			},
			noMoreText: {
				type: String,
				default: '这里空空如也…',
			},
			loadText: {
				type: String,
				default: '加载中...'
			},
			
			
			data: {
				type: Array,
				default: ()=>[]
			},
			page: {
				type: Object,
				default: {
					isLoad:true,
					pageIndex:1,
					totalPage:0
				}
			},
			
			row:{
				type:Number,
				default:3
			},
				
			avatarSize:{
				type:Number,
				default:50
			},
			
			avatarShape:{
				type:String,
				default:'circle'
			}
		},
		data() {
			return {

			};
		},
		computed: {
			uLoadStatus() {
				if (this.page.isLoad) {
					return 'loading'
				} else {
					if (this.ifIsDone) {
						return 'nomore'
					} else {
						return 'loadmore'
					}
				}
			},
			ifIsDone() {
				if (this.page.pageIndex >= this.page.totalPage || this.data.length <= 0 || this.page.isDone) {
					return true;
				}else {
					return false;
				}

			}
		},
		mounted() {

		}
	}
</script>

<style lang="scss">
	.loadMoresList{
		.loadMores {
			padding: 24rpx 0;
		}
	}
	
</style>
