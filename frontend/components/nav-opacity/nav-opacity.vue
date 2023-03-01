<template>
	<view class="navOpacity" :style="{
			paddingTop:statusBarHeight+'px',
			backgroundColor: `rgba(${rgb1},${opacity})`,
			zIndex: zIndex
		}">
		<view class="nav f_c_a">
			<view class="left">
				<view v-show="showBack" @click="navBack" class="left left_ico f_c_c"
					:style="{backgroundColor: `rgba(246,113,18,${opacityIco})`}">
					<u-icon size="20" name="arrow-left" />
				</view>
			</view>
			<view :style="{color: color, opacity:opacity}">{{title}}</view>
			<view class="right"></view>
		</view>
	</view>

</template>

<script>
	import {
		mapState
	} from 'vuex'
	export default {
		data() {
			return {
				rate: 0.007,
				opacity: 0,
				opacityIco: 0.3
			}
		},
		props: {
			title: {
				type: String,
				default: ''
			},
			rightText: {
				type: String,
				default: ''
			},
			zIndex: {
				type: Number,
				default: 100
			},
			color: {
				type: String,
				default: "#1A1A1A"
			},
			bgColor: {
				type: String,
				default: "#FFFFFF"
			},
			showBack: {
				type: Boolean,
				default: false
			}
		},
		computed: {
			...mapState({
				statusBarHeight: state => state.statusBarHeight
			}),
			rgb1:function(){
				return this.$hexToRgba(this.bgColor)
			}
			
		},
		methods: {
			//滚动事件
			navScorllPage(e) {
				this.opacity = e.scrollTop * this.rate > 1 ? 1 : e.scrollTop * this.rate;
				this.opacityIco = (0.3 - e.scrollTop * 0.0007) < 0 ? 0 : (0.3 - e.scrollTop * 0.0007);
			},
			// 返回
			navBack() {
				this.$navBack();
			}
		}
	}
</script>

<style lang="scss" scoped>
	.navOpacity {
		height: 80rpx;
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		z-index: 100000;

		.nav {
			width: 100%;
			height: 100%;
			box-sizing: border-box;
			padding: 0 30rpx;

			.left,
			.right {
				width: 60rpx;
				height: 60rpx;
			}

			.left_ico {
				background-color: rgba(#000, 0.1);
				border-radius: 50%;

				.iconfont {
					font-size: 32rpx;
				}
			}
		}
	}
</style>
