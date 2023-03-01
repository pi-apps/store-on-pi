<template>
	<view class="loadMores">
		<!-- 空状态 -->
		<u-empty :icon="icon" width="168rpx" height="137rpx" :text="noMoreText" v-if="!isLoad && data <= 0"
			:mode="empytType">
			<slot name="empty"></slot>
		</u-empty>
		<!-- 加载更多状态 -->
		<u-loadmore v-else :status="uLoadStatus" :loading-text="loadText" :loadmore-text="loadMoreText"
			:nomore-text="noMoreText" :line="true" />
		<slot v-if="data <= 0"></slot>
	</view>
</template>

<script>
	export default {
		name: "LoadMore",
		props: {
			icon: {
				type: String,
				default: '/static/image/empty-image/none.png'
			},
			empytType: {
				type: String,
				default: 'search',
			},
			loadText: {
				type: String,
				default: '加载中...',
			},
			loadMoreText: {
				type: String,
				default: '滚动查看更多',
			},
			noMoreText: {
				type: String,
				default: '这里空空如也…',
			},
			data: {
				type: Number,
				default: 0
			},
			isLoad: {
				type: Boolean,
				default: true
			},
			total: {
				type: Number,
				default: 0
			},
			page: {
				type: Number,
				default: 1
			}
		},
		data() {
			return {

			};
		},
		computed: {
			uLoadStatus() {
				if (this.isLoad) {
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
				if (this.page >= this.total || this.total <= 0) {
					return true;
				} else {
					return false;
				}

			}
		},
		mounted() {

		}
	}
</script>

<style lang="scss">
	.loadMores {
		padding: 24rpx 0;
	}
</style>
