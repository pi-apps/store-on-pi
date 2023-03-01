<script>
	export default {
		onLaunch: function() {
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	@import '@/common/iconfont/iconfont.css';
	/*每个页面公共css */
	@import '@/common/common.scss';
	
	uni-modal{
		z-index: 999999!important;
	}
	uni-modal .uni-modal{
		border-radius: 20rpx;
	}
	
	uni-tabbar.uni-tabbar-bottom .uni-tabbar{
		padding-bottom: 20rpx!important;
	}

	page {
		background-color: #FAFAFA;
		font-size: 28rpx;
	}
</style>
