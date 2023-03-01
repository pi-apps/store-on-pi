<template>
	<view>
		<!-- 自定义导航 -->
		<uniNav :title="title" />
		<web-view :src="url"></web-view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				url:'',
				title:''
			};
		},
		onLoad(options) {
		    if(options.link){
		      let urlObj = JSON.parse(decodeURIComponent(options.link));
		      console.log(urlObj)
		      this.url = urlObj.url
		      this.title = urlObj.title || 'PI商店'
		    }
		},
	}
</script>

<style lang="scss">
	uni-web-view{
		top: 100px!important;
	}
</style>
