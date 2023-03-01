<template>
	<view>
		<!-- 自定义导航 -->
		<uniNav :title="title" />
		<view class="xy">
			<u-parse @linkTap="jumpToUrls" :content="agreementContext"></u-parse>
		</view>
	</view>
</template>

<script>
	import {
		_getAgreement
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				title: '',
				id: '',
				agreementContext: ''
			};
		},
		onLoad(options) {
			if (options && options.id) {
				this.id = options.id
			}
		},
		onShow() {
			this.getxyContent();
		},
		methods: {
			getxyContent() {
				_getAgreement(this.id).then(res => {
					if (res && res.code == 0) {
						this.agreementContext = res.data.agreementContext;
						this.title = res.data.name;
					}
				})
			},
			jumpToUrls(val){
				if(val.href && val.href != ''){
					this.$openNewPage(val.href)
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.xy {
		font-size: 28rpx;
		padding: 30rpx;
	}
</style>
