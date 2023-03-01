<template>
	<u-overlay :show="show">
		<view class="toast">
			<view class="toast_item fz16">
				<view v-if="title != ''" class="toast_title">{{title}}</view>
				<slot>
					<view class="toast_content cf8">
						{{content}}
					</view>
				</slot>
				<view v-if="showBtn" class="w100 f_c_a mt32">
					<view @click="close" class="btn style1 f_c_c" gost>{{letBtnText}}</view>
					<view @click="confirm" class="btn style1 f_c_c bold">{{rightBtnText}}</view>
				</view>
			</view>
		</view>
	</u-overlay>
</template>

<script>
	export default {
		name:"toast",
		props:{
			show:{
				default:false,
				type:Boolean
			},
			showBtn:{
				default:true,
				type:Boolean
			},
			title:{
				type:String,
				default:''
			},
			letBtnText:{
				type:String,
				default:'取消'
			},
			rightBtnText:{
				type:String,
				default:'确定'
			},
			content:{
				type:String,
				default:''
			},
		},
		data() {
			return {
				
			};
		},
		methods:{
			close(){
				this.$emit('update:show',false);
				this.$emit('close',false);
			},
			confirm(){
				this.$emit('confirm',true);
			}
		}
	}
</script>

<style lang="scss" scoped>
	.toast{
		box-sizing: border-box;
		display: flex;
		align-items: center;
		justify-content: center;
		height: 100%;
	}
	.toast_title{
		font-size: 36rpx;
	}
	.toast_content{
		padding: 72rpx 0 28rpx;
		font-size: 28rpx;
	}
</style>
