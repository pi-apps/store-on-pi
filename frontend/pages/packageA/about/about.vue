<template>
	<view class="template">
		<!-- 自定义导航 -->
		<uniNav title="PI商店" />
		
		 <view class="f f_column f_c_c -warp30">
		    <u--image class="mb20" src="/static/packageA/image/about.png" width="200" height="80" />
			<view class="er_code_warp">
				<u--image :src="qrCodeUrl" width="360rpx" height="360rpx" />
			</view>
		    <text class="mt32" style="color: #999;">扫描二维码，您的朋友也可以使用PI商店App</text>
		  </view>
		  <view class="mt12"></view>
		  <u-cell-group>
		      <u-cell url="../xy/xy?id=10010001" title="用户服务协议" isLink />
		      <u-cell url="../xy/xy?id=10010002" title="隐私政策" isLink />
		      <u-cell title="联系我们" url="../contact/contact" isLink />
		  </u-cell-group>
	</view>
</template>

<script>
	import { mapState } from 'vuex';
	import { baseUrl } from '@/utils/index.js'
	export default {
		data() {
			return {
				userInfo:{
					
				},
				qrCodeUrl:''
			};
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {
			
		},
		onShow() {
			let userInfo = wx.getStorageSync('UserInfo');
			    if(userInfo && userInfo != ''){
			      this.userInfo = userInfo;
			      this.qrCodeUrl = baseUrl()+`/cert/access/getQrCode.jpeg?invitation=${userInfo.invitation}`;
			    }
		},
		methods:{
			
		}
	}
</script>

<style lang="scss" scoped>
	
</style>
