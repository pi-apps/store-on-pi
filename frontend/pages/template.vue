<template>
	<view class="template">
		<!-- 自定义导航 -->
		<uniNav title="" />
		
		<view class="main">
			
			
		</view>
	</view>
</template>

<script>
	import { mapState } from 'vuex';
	import {  } from '@/http/apiReq'
	export default {
		data() {
			return {
				
			};
		},
		computed: {
			...mapState({
				statusBarHeight: state => state.statusBarHeight
			})
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
			
		},
		methods:{
			
		}
	}
</script>

<style lang="scss" scoped>
	
</style>
