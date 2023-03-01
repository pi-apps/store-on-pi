<template>
	<u-action-sheet class="styleClass" @close="closeModel" :show="showAction" :title="title" round="12">
		<view class="f f_column f_align_start -warp">
			<view class="mb12 f_c_a w100">
				<text>{{orderExprion.ShipperName}} {{orderExprion.LogisticCode}}</text>
				<u-tag size="mini" text="复制" @click="$copy(orderExprion.LogisticCode)"></u-tag>
			</view>
			<scroll-view scroll-y="true" style="height: 500px;">
				<u-steps :current="0" dot activeColor="#f67112" direction="column">
					<u-steps-item :key="index" v-for="(item,index) in orderExprion.Traces" :title="item.AcceptTime" :desc="item.AcceptStation"></u-steps-item>
				</u-steps>
			</scroll-view>
		</view>
	</u-action-sheet>
</template>

<script>
	import { _orderExprion
	} from '@/http/apiReq'
	export default {
		name:"actionExprion",
		data() {
			return {
				showAction:false,
				orderExprion:{
					ShipperName:'',
					LogisticCode:'',
					Traces:[]
				}
			};
		},
		props:{
			show:{
				type:Boolean,default:false
			},
			title:{
				type:String,default:'详细信息'
			},
			mobile:{
				type:String,default:''
			},
			expressNo:{
				type:String,default:''
			}
		},
		watch:{
			show(val){
				if(val){
					this.watchExprion();
				}
			}
		},
		mounted() {
			const _this = this;
			// h5原生方法调用回调
			window['appbackJScopyToPasteboard'] = (code) => {
				if (code == 200) {
					_this.$showToast('复制成功！');
				}
			}
		},
		methods:{
			// 查询物流
			watchExprion(){
				if(this.mobile == ''){
					return this.$showToast('手机号码有误')
				}
				if(this.expressNo == ''){
					return this.$showToast('快递单号有误')
				}
				_orderExprion({
					mobile:this.mobile,
					logisticsNo:this.expressNo
				}).then(res=>{
					if(res && res.code == 0){
						this.showAction = true;
						this.orderExprion = res.data;
						this.orderExprion.Traces = res.data.Traces.reverse()
					}
				})
			},
			closeModel(){
				this.showAction = false;
				this.$emit('update:show',false)
			}
		}
	}
</script>

<style lang="scss">

</style>