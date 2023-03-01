<template>
	<view>
		<!-- 自定义导航 -->
		<uniNav title="设置" />
		<view class="-bgf -warp">
			<u-cell-group>
				<u-cell title="个人资料" isLink url="/pages/packageA/userInfo/userInfo" />
				<u-cell title="注销账号" isLink url="/pages/packageA/loginOut/loginOut" />
				<u-cell title="关于我们" isLink url="/pages/packageA/about/about" />
				<u-cell title="个性化推荐">
					<u-switch size="20" slot="value" v-model="value" @change="change" activeColor="#f67112"></u-switch>
				</u-cell>
				<u-cell title="当前版本" :value="'v'+version" :border="false" />
			</u-cell-group>
		</view><!--  -->
		<view class="-warp">
			<u-button :plain="true" type="warning" @click="loginOuts" class="w100 f_c_c mt32 loginout">退出登录</u-button>
		</view>


		<u-action-sheet description="退出登录后，需要重新授权才能登录～" :show="actionSheel" :actions="actions" cancel-text="取消"
			@select="selectInfo" @close="cancel" />
	</view>
</template>

<script>
	import { version } from '@/utils/index.js'
	import {
		mapState
	} from 'vuex';
	import {} from '@/http/apiReq'
	export default {
		data() {
			return {
				value:true,
				version:version,
				actionSheel: false,
				actions: [{
					name: '退出登录',
				}]
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
			this.value = uni.getStorageSync('isLike');
		},
		methods: {
			// 
			loginOuts() {
				this.actionSheel = true
			},
			cancel() {
				this.actionSheel = false
			},
			selectInfo(e) {
				uni.removeStorageSync('Token')
				uni.removeStorageSync('UserInfo')
				uni.removeStorageSync('backUrl');
				this.actionSheel = false
				this.$redTo('/pages/login/login');
			},
			change(e){
				uni.setStorageSync('isLike',e)
			}
		}
	}
</script>

<style lang="scss" scoped>
	.loginout {}
</style>
