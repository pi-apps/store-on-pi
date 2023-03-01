<template>
	<view class="orderMainStore">
		<!-- 自定义导航 -->
		<uniNav title="门店订单">
			<view class="-bgf">
				<u-tabs :list="isUseList" @click="onChangeTab"></u-tabs>
			</view>
		</uniNav>

		<LoadMoreList :page="page" :data="page.list">
			<view class="orderMainStore">
				<view class="order -warp30">
					<view class="mb24 -bgf -warp" v-for="item in page.list" :key="item.orderId">
						<view class="f f_align">
							<u--image class="f_c_c" :src="item.goodsInfo ? item.goodsInfo.goodsBannerUrlList[0] :''"
								radius="8" width="80" height="80" />
							<view class="f f_column f_align w100 ml12">
								<view class="f_c_a">
									<view class="overfont-2">{{item.goodsName}}</view>
									<text class="price mr12">π{{item.userFinalPrice}}</text>
								</view>
								<view class="f_c_a">
									<view class="mt8 fz12 tsize overfont">{{item.createTime}}</view>
									<view v-if="item.isUse == '0' && JSON.parse(item.useStoreJson).status != 2" class="watchPDF mt12 f_c_c" @click="watchPDF(item)">查看凭证<u-icon size="12" color="#F7694A" name="arrow-right"></u-icon></view>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</LoadMoreList>

		<u-modal :show="show" @confirm="show= false" :closeOnClickOverlay="true" confirmText="确认">
			<u--image @click="privewImage" height="440rpx" mode="aspectFit" :src="base64Image"></u--image>
		</u-modal>
	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex';
	import {
		_storeOrder,
		_storeOrderCert
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				show: false,
				isUse: '1',
				isUseList: [{
						name: '待核销',
						value: '1'
					},
					{
						name: '已核销',
						value: '0'
					},
					{
						name: '已取消',
						value: '2'
					}
				],
				page: {
					pageIndex: 1,
					limit: 10,
					totalPage: 1,
					isLoad: false,
					isDone: false,
					list: [],
				},
				base64Image: ''
			};
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue')
		},
		computed: {
			...mapState({
				pay_status_type: state => state.dictArrs.pay_status_type
			})
		},
		mounted() {
			const _this = this;
			// h5原生方法调用回调
			//监听是否更新成功
			window['appbackJSsaveImgToPhone'] = (code) => {
				if (code == 200) {
					_this.$showToast('保存成功！');
					_this.show = false;
				}
			}
		},
		onLoad(options) {
			this.$store.dispatch('getDictlists', 'pay_status_type')
		},
		onShow() {
			this.init();
		},
		methods: {
			init() {
				this.getDataRowListAxios();
			},
			// 查询列表数据
			getDataRowListAxios() {
				this.page.isLoad = true
				const {
					pageIndex,
					limit
				} = this.page;
				_storeOrder({
					page: pageIndex,
					limit: limit,
					isUse: this.isUse,
					payStatusType: "10007003"
				}).then(res => {
					this.page.isLoad = false
					if (res && res.code == 0) {
						let lists = res.data && res.data.list;
						this.page.totalPage = res.data.totalPage || 0
						if (pageIndex <= 1) {
							this.page.list = lists;
						} else {
							let o_list = this.page.list;
							this.page.list = o_list.concat(lists)
						}
						if (lists.length < limit) {
							this.page.isDone = true
						}
					}
				}).catch(() => this.page.isLoad = false)
			},
			// 
			onChangeTab({
				name,
				value
			}) {
				this.isUse = value;
				this.page.pageIndex = 1
				this.getDataRowListAxios();
			},
			watchPDF(item) {
				_storeOrderCert(item.id).then(res => {
					console.log(res)
					if (res && res.code == '0') {
						this.base64Image = res.data;
						this.show = true;
					}
				})
			},
			downLoad() {
				if (this.base64Image == '') {
					return this.$showToast('暂无法保存')
				}
				uni.downloadFile({
					url: this.base64Image, //选择图片返回的相对路径
					success: res => { //成功的回调
						console.log(res);
						let base64 = 'data:image/jpeg;base64,' + res.data;
						this.$saveImgToPhone(res.data)
					}
				})
			},
			// 图片预览
			privewImage(){
				uni.previewImage({
					urls:[this.base64Image]
				})
			}
		},
		/**
		 * 页面相关事件处理函数--监听用户下拉动作
		 */
		onPullDownRefresh: function() {
			this.page.pageIndex = 1
			this.init();
		},

		/**
		 * 页面上拉触底事件的处理函数
		 */
		onReachBottom: function() {
			let {
				pageIndex,
				totalPage
			} = this.page;
			if (pageIndex < totalPage) {
				this.page.pageIndex++
				this.init();
			} else {
				this.page.isDone = true
			}
		},
	}
</script>

<style lang="scss">
	/deep/ .orderMainStore {
		font-size: 40rpx;
		  color: #292929;
		.watchPDF{
		    font-size: 24rpx;
		    color: #F7694A;
		    padding: 4rpx 14rpx;
		    border: solid 1rpx #F7694A; 
		    border-radius: 24rpx;
		  }

		.tsize {
			color: $font-color2;
		}

		.price {
			 color: #292929;
		}

		.status {
			color: #20cf82;
		}

	}
</style>
