<template>
	<view class="sticky0" :style="{zIndex: zIndex}">
		<view class="w100 -status" :style="{height:statusBarHeight+'px',backgroundColor:bgColor}"></view>
		<u-navbar v-if="type == 'Nav'" class="status_uni" :titleStyle="{'color': color}" :leftIconColor="color" :leftIcon="leftIcon"
			:title="title" :fixed="false" :autoBack="autoBack" :bgColor="bgColor" :rightText="rightText" @rightClick="rightClickBtn"
			:rightIcon="rightIcon"
		>
		<slot name="left" slot="left"></slot>
		<slot name="center" slot="center"></slot>
		<slot name="right" slot="right"></slot>
		</u-navbar>
		<view class="-warp f f_align_center" :style="{backgroundColor:bgColor}" v-else>
			<view v-if="leftIcon != ''" class="f_c_c f_shrink0 lefIcoView" @click="navBack">
				<u-icon size="20" bold :name="leftIcon" :color="color" />
			</view>
			<u-search @clear="clearMeth" @search="searchMeth" @custom="customMeth" :shape="shape" :showAction="showAction" :actionText="actionText" :placeholder="placeholder" v-model="keyword"></u-search>
			<slot name="search_right"></slot>
		</view>
		
		<slot></slot>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	export default {
		name: "uni-nav",
		data() {
			return {
				keyword:''
			};
		},
		computed: {
			...mapState({
				statusBarHeight: state => state.statusBarHeight
			})
		},
		props: {
			type:{
				type: String,
				default: 'Nav'
			},
			color: {
				type: String,
				default: '#303133'
			},
			bgColor: {
				type: String,
				default: '#FFFFFF'
			},
			zIndex: {
				type: Number,
				default: 1000
			},
			title: {
				type: String,
				default: ''
			},
			leftIcon: {
				type: String,
				default: 'arrow-left'
			},
			rightIcon: {
				type: String,
				default: ''
			},
			rightText: {
				type: String,
				default: ''
			},
			autoBack: {
				type: Boolean,
				default: true
			},
			placeholder: {
				type: String,
				default: '请输入关键字'
			},
			showAction:{
				type: Boolean,
				default: false
			},
			actionText:{
				type: String,
				default: '搜索'
			},
			shape:{
				type: String,
				default: 'round'
			}
		},
		methods:{
			rightClickBtn(){
				this.$emit('rightClick','')
			},
			searchMeth(value){
				this.$emit('search',value)
			},
			customMeth(value){
				this.$emit('custom',value)
			},
			clearMeth(){
				this.$emit('clear','')
			},
			navBack(){
				this.$navBack();
			}
		}
	}
</script>

<style lang="scss" scoped>
	/*每个页面公共css */
	@import '@/common/common.scss';

	/deep/ .status_uni {
		.u-status-bar {
			height: 0 !important;
		}

	}
	.lefIcoView{
		padding: 0 20rpx 0 0;
	}
</style>
