<template>
	<view class="moreIco">
		<!-- 自定义导航 -->
		<uniNav title="更多功能" />

		<view class="iconWarp">
			<view class="f icon-bar" v-for="(item,index) in homeIcon" :key="index">
				<view class="i_w_item f f_column f_c_c" v-for="(ico,index) in item" :key="index"
					@click="jumpAdBannerUrlAuth(ico)">
					<u--image width="40" height="40" :src="ico.url" />
					<text>{{ico.title}}</text>
					<view class="isNew" v-if="ico.isnew && ico.isnew != ''">{{ico.isnew}}</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		_getHomePage
	} from '@/http/apiReq'
	import {
		version
	} from '@/utils/index.js'
	export default {
		data() {
			return {
				homeIcon: []
			};
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {

		},
		onShow() {
			this.getHomePage();
		},
		methods: {
			// 查询首页图标
			getHomePage() {
				_getHomePage(this.$sys().platform == 'ios' ? '1' : '0', version).then(res => {
					if (res && res.code == 0) {
						let row = res.data;
						if (row.length && row.length > 0) {
							let iconList = row.filter((item) => item.linksUrl.indexOf('moreIcon') === -1);

							let tepm = 4;
							let col = Math.ceil(iconList.length / tepm);
							let iconGroup = [];
							for (let i = 0; i < col; i++) {
								iconGroup[i] = iconList.slice(i * tepm, (i + 1) * tepm);
							}
							this.homeIcon = iconGroup;
						}

					}
				})
			},
			// icon 跳转
			jumpAdBannerUrlAuth(item) {
				if (item.isLogin == '0') {
					if (this.$loginModel('/pages/index/index')) {
						this.$toRuntimeUrlIndex(item);
					}
				} else {
					this.$toRuntimeUrlIndex(item);
				}
			},
		}
	}
</script>

<style lang="scss" scoped>
	.moreIco {
		min-height: 100vh;
		background-color: #FAFAFA;
	}

	.iconWarp {
		box-sizing: border-box;
		font-size: 24rpx;
		margin-top: 30rpx;

		.icon-bar {
			background-color: #fff;
			margin-bottom: 10rpx;
			padding: 0 20rpx;
		}

		.i_w_item {
			width: 25%;
			position: relative;
			margin: 36rpx 0;
			color: #292929;
			
			.isNew {
				position: absolute;
				padding: 2rpx 10rpx;
				font-size: 20rpx;
				background-color: rgba($font-money-color2, 0.6);
				border-radius: 14rpx 0 14rpx 0;
				right: -10rpx;
				top: -10rpx;
				color: #FFF;
			}
		}

		.bars {
			width: 8rpx;
			height: 8rpx;
			border-radius: 50%;
			background-color: #D8D8D8;
			transition: width 0.2s;

			&.active {
				width: 28rpx;
				height: 8rpx;
				border-radius: 5rpx;
				background-image: linear-gradient(to right, #FCA281, #F7694A);
			}
		}
	}
</style>
