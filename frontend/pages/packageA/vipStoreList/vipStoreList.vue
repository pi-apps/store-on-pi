<template>
	<view class="container">
		<!-- 自定义导航 -->
		<uniNav title="适用门店" />
		<!-- 商品 -->
		<view class="order">
			<LoadMoreList :page="page" :data="page.list">
				<template #default>
					<view v-for="(item,index) in page.list" :key="item.goodsId" class="mb24 -bgf f f_column store_warps">
						<view class="f_c_a" style="flex-direction: row;">
							<text class="overfont fz14 bold">{{item.storeName}}</text>
							<view class="tip_c2">{{(item.distance/1000).toFixed(2)}}KM</view>
						</view>
						<view :class="'storeStatus f_baseline mt8 '+(item.status == '0' ? '' : 'isDown')">
							{{item.status == '0' ? '营业中' : '休息中'}}
						</view>

						<view class="f_c_a">
							<view class="f f_align_center">
								<u-icon name="map" color="#9F9F9F" class="mr4" />
								<text style="width: 200px;" class="overfont fz12 tip_c">{{item.address}}</text>
							</view>
							<view class="tip_tag"></view>
							<view class="f_c_e">
								<u--image @click="callphone(item)" src="/static/packageA/image/dianhua@2x.png"
									width="30" height="30" />
								<u--image class="ml12" @click="jumpMapNavgation(item)" src="/static/packageA/image/daohang@2x.png"
									width="30" height="30" />
							</view>
						</view>
					</view>
				</template>
			</LoadMoreList>
		</view>
		
	</view>
</template>

<script>
	import {
		_apiGoodsVipDetail
	} from '@/http/apiReq'
	export default {
		data() {
			return {
				goodsId: '',
				page: {
					pageIndex: 1,
					limit: 10,
					totalPage: 1,
					isLoad: false,
					isDone: false,
					list: [],
				},
				latitude: '',
				longitude: ''
			};
		},
		components: {
			LoadMoreList: () => import('@/components/LoadMoreList/LoadMoreList.vue')
		},
		mounted() {
			// h5原生方法调用回调
		},
		onLoad(options) {
			if (options.goodsId) {
				this.goodsId = options.goodsId;
				this.longitude = options.longitude;
				this.latitude = options.latitude
				this.getGoodsVipDetalInfos();
			}
		},

		onShow() {

		},
		methods: {
			// 查询分类下的 商品
			getGoodsVipDetalInfos() {
				this.page.isLoad = true;
				const {
					pageIndex,
					limit
				} = this.page;
				_apiGoodsVipDetail({
					goodsId: this.goodsId,
					latitude: this.latitude,
					longitude: this.longitude,
					// 联调测试
					// latitude: '30.68144',
					// longitude: '103.8559',
				}).then(res => {
					this.page.isLoad = false;
					if (res && res.code == 0) {
						let lists = res.data && res.data.storeOrderByList;
						this.page.list = lists;
						this.page.isDone = true
					}
				}).catch(() => this.page.isLoad = false)
			},
			// 
			  callphone(item) {
			    const {mobile} = item;
			    if (mobile && mobile != '') {
			      this.$callPhone(mobile)
			    } else {
			      this.$showTaost('该门店暂无电话～')
			    }
			  },
			  // 
			  jumpMapNavgation(item) {
			    const {longitude, latitude, storeName, address} = item;
			    this.$refs.navgationRefs.updataData({
			    	latitude: latitude,
			    	longitude: longitude,
			    	name: storeName,
			    	address: address
			    });
			  },
		},
		/**
		 * 页面相关事件处理函数--监听用户下拉动作
		 */
		onPullDownRefresh: function() {
			this.getGoodsVipDetalInfos();
		},

		/**
		 * 页面上拉触底事件的处理函数
		 */
		onReachBottom: function() {},
	}
</script>

<style lang="scss" scoped>
	.container {
		min-height: 100vh;

		.order {
			padding: 20rpx 24rpx;
			width: 100%;
			box-sizing: border-box;
		}
	}



	/deep/ .store_warps {
		padding: 28rpx;
		border-radius: 12rpx;
		color: #292929;

		.tip_c {
			color: #9F9F9F;
		}

		.tip_tag {
			height: 46rpx;
			width: 2rpx;
			border-radius: 1rpx;
			background-color: #F0F0F0;
		}

		.storeStatus {
			background-color: #FFF0ED;
			border-radius: 4rpx;
			padding: 2rpx 8rpx;
			color: #F7694A;
			font-size: 22rpx;

			&.isDown {
				background-color: #EAEAEA;
				color: #292929;
			}
		}
	}
</style>
