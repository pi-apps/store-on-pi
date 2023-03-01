<template>
	<view class="f f_warp">
		<template v-if="showPhoto">
			<view class="fileItem" v-for="(item,index) in fileList" :key="index">
				<view class="close f f_c_c" @click="close(index)">
					<u-icon name="close-circle" size="16"></u-icon>
				</view>
				<u--image :radius="radius" @click="previewImage(index)" :width="width+'rpx'" :height="height+'rpx'" :src="item"></u--image>
			</view>
		</template>
		<!-- 插槽 -->
		<slot name="handler" :uploadHandler="upload">
			<view :style="{'width':width+'rpx','height':height+'rpx','borderRadius':radius+'px'}" v-if="fileList.length < count" class="upload f f_c_c" @click="upload">
				<u-icon name="plus" size="16"></u-icon>
			</view>
		</slot>
		
		<u-toast ref="uToast"></u-toast>
	</view>
</template>

<script>
	import $upload from '@/utils/upload.js'
	export default {
		name:"UploadFile",
		props:{
			showPhoto:{
				type:Boolean,
				default:true
			},
			width:{
				type:Number,
				default:140
			},
			height:{
				type:Number,
				default:140
			},
			sourceType:{
				type:Array,
				default:()=>['album','camera']
			},
			size:{
				type:Number,
				default:5
			},
			count:{
				type:Number,
				default:2
			},
			fileList:{
				type:Array,
				default:()=>[]
			},
			// 是否截取图片 0 不截取
			cropWidth:{
				type:Number,
				default:0
			},
			cropHeight:{
				type:Number,
				default:0
			},
			radius:{
				type:Number,
				default:18
			}
		},
		model: {
			prop: 'fileList',
			event: 'UploadDone',
		},
		data() {
			return {
				
			};
		},
		methods:{
			//打开设备
			upload(){
				uni.chooseImage({
					count:this.count,
					sourceType:this.sourceType,
					crop:(this.cropWidth <= 0 || this.cropHeight <= 0) ? false : {
						width:this.cropWidth,
						height:this.cropHeight
					},
					success:async (res) => {
						console.log(res);
						let tempFiles =  res.tempFiles;
						let tempFilesPath = [];
						if((tempFiles.length + this.fileList.length ) > this.count){
							return this.$refs.uToast.show({type: 'error',message: `最多只能上传${this.count}张`});
						}
						for(let i=0;i<tempFiles.length;i++){
							let size = tempFiles[i].size/1024/1024;
							if(this.size != 0 && size > this.size) {
								this.$refs.uToast.show({type: 'error',message: `文件最大只能${this.size}MB`});
								continue
							}
							tempFilesPath.push(tempFiles[i].path);
						}
						if(tempFilesPath.length > 0){
							this.uploadAxios(tempFilesPath);
						}
					}
				})
			},
			// 准备上传
			async uploadAxios(res){
				for(let i=0;i<res.length;i++){
					let result = await $upload(res[i]);
					this.$emit('UploadDone',[...this.fileList,result])
				}
			},
			// 关闭图片
			close(index){
				let list = this.fileList;
				list.splice(index,1);
				this.$emit('UploadDone',list);
			},
			// 图片预览
			previewImage(index){
				this.$previewImage(this.fileList,index);
			}
		}
	}
</script>

<style lang="scss" scoped>
	.upload{
		width: 210rpx;
		height: 210rpx;
		background-color: $tt-bg;
		border-radius: 36rpx;
	}
	.fileItem{
		margin-right: 10rpx;
		margin-bottom: 10rpx;
		position: relative;
		.close{
			position: absolute;
			width: 40rpx;height: 40rpx;
			z-index: 30;
			right: 12rpx;top:12rpx;
		}
	}
</style>
