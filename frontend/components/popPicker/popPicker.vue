<template>
	<!-- 选择开户行 -->
	<u-popup :show="show" round @close="onClose" :round="12">
		<view class="-warp">
			<view class="ttle f_c_c">{{title}}</view>
			<u-search v-model="banknameKeyword" placeholder="请输入开户行名称进行搜索" @search="getBankListVal"
				@custom="getBankListVal" :show-action="true" actionText="搜索" :animation="true"></u-search>
			<view class="item">
				<scroll-view :scroll-y="true" style="height: 500px;">
					<view class="item-list" v-for="(item,index) in bankList" :key="index" @click="nodeClick(item)">
						<view>{{item.bankName}}</view>
					</view>
					<u-empty icon="/static/image/empty-image/none.png" width="300rpx" height="300rpx" text="没有查询到开户行"
						v-if="isLoading && bankList.length <= 0">
					</u-empty>
				</scroll-view>
			</view>
		</view>
	</u-popup>
</template>

<script>
	import {
		_getbank2
	} from '@/http/apiReq';
	export default {
		name: "popPicker",
		data() {
			return {
				bankList: '',
				banknameKeyword: '',
				isLoading: false,
			};
		},
		props: {
			show: {
				type: Boolean,
				default: false
			},
			title: {
				type: String,
				default: '选择开户行'
			}

		},
		watch: {
			show: function(val) {
				if (val) {
					this.getBankList();
				}
			}
		},
		created() {

		},
		methods: {
			// 
			getBankListVal(val) {
				this.banknameKeyword = val;
				this.getBankList();
			},
			// 查询开户行
			getBankList() {
				this.isLoading = true
				_getbank2({
					bankName: this.banknameKeyword || '',
					limit: 30
				}).then(res => {
					this.isLoading = false
					if (res && res.code == 0) {
						this.bankList = res.data.list;
					}
				}).catch(() => this.isLoading = false)
			},
			nodeClick(item) {
				this.$emit('nodeClick', item);
				setTimeout(() => {
					this.$emit('update:show', false)
				}, 300)
			},
			onClose(){
				this.$emit('update:show', false)
			}
		}
	}
</script>

<style lang="scss" scoped>
	.ttle {
		padding: 0 0 30rpx;
	}

	.item {
		padding: 20rpx 0;

		.item-list {
			padding: 20rpx 0;

			&:active,
			&.active {
				background-color: $color-tt;
				color: #FFF;
				border-radius: 40px;
				padding-left: 20rpx;
			}
		}
	}
</style>
