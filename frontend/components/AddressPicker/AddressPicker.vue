<template>
	<view class="Picker">
		<u-picker :show="show" ref="uPicker" :columns="columns" keyName="areaName" :title="title" @confirm="confirm"
			@change="changeHandler" @close="closeModel" @cancel="closeModel" :loading="isloading"
			:closeOnClickOverlay="closeOnClickOverlay"></u-picker>
	</view>
</template>

<script>
	import {
		getProviceInfo,
		getCityInfo,
		getCountyInfo
	} from '@/http/apiReq.js'
	export default {
		name: "AddressPicker",
		props: {
			title: {
				type: String,
				default: '请选择地址信息'
			},
			closeOnClickOverlay: {
				type: Boolean,
				default: true
			},
			show: {
				type: Boolean,
				default: false
			},
			defaultIndex: {
				type: Array,
				default: () => []
			}
		},
		model: {
			prop: 'defaultIndex',
			event: 'confirm',
		},
		data() {
			return {
				isloading: false,
				columns: [
					[],
					[],
					[]
				],
				address: []
			};
		},
		mounted() {
			this.getProviceInfo();
		},
		methods: {
			formartArr(list, code) {
				let defaultIndex = 0;
				let defaultValue = list[0];
				for (let i = 0; i < list.length; i++) {
					if (code && list[i].areaCode == code) {
						defaultIndex = i;
						defaultValue = list[i];
					}
				}
				return {
					defaultIndex: defaultIndex,
					defaultValue: defaultValue
				};
			},
			// 
			updatePickets() {
				this.getProviceInfo();
			},
			// 省
			getProviceInfo() {
				const picker = this.$refs.uPicker;
				this.isloading = true;
				getProviceInfo().then(res => {
					if (res && res.code == '0') {
						this.columns[0] = res.data;
						picker.setColumnValues(0, res.data);
						
						if (this.defaultIndex.length && this.defaultIndex.length > 1) {
							// 设置默认选项
							let row = this.formartArr(res.data, this.defaultIndex[0]);
							picker.setIndexs([row.defaultIndex,0,0]);
							
							this.getCityInfo(row.defaultValue,row.defaultIndex);
						} else {
							this.columns.splice(1)
							this.isloading = false;
						}
					}
				}).catch(() => {
					this.isloading = false;
				})
			},
			//市
			getCityInfo(item,defaultIndex) {
				const picker = this.$refs.uPicker;
				getCityInfo(item.areaCode).then(res => {
					if (res && res.code == '0') {
						this.columns[1] = res.data;
						picker.setColumnValues(1, res.data);

						if (this.defaultIndex.length && this.defaultIndex.length > 2) {
							// 设置默认选项
							let row = this.formartArr(res.data, this.defaultIndex[1]);
							picker.setIndexs([defaultIndex,row.defaultIndex,0]);
							
							this.getCountyInfo(row.defaultValue,defaultIndex,row.defaultIndex);
						} else {
							this.columns.splice(2)
							this.isloading = false;
						}
					}
				}).catch(() => {
					this.isloading = false;
				})
			},
			// 县
			getCountyInfo(item,defaultIndex,defaultIndex1) {
				const picker = this.$refs.uPicker;
				getCountyInfo(item.areaCode).then(res => {
					this.isloading = false;
					if (res && res.code == '0') {
						this.columns[2] = res.data;
						picker.setColumnValues(2, res.data);
						
						// 设置默认选项
						let row = this.formartArr(res.data, this.defaultIndex[2]);
						picker.setIndexs([defaultIndex,defaultIndex1,row.defaultIndex]);
					}
				}).catch(() => {
					this.isloading = false;
				})
			},
			//column 切换事件
			changeHandler(e) {
				const {
					columnIndex,
					indexs,
					index,
					// 微信小程序无法将picker实例传出来，只能通过ref操作
					picker = this.$refs.uPicker
				} = e;
				console.log(e)
				let colData = this.columns[columnIndex];
				let rowData = colData[index];
				this.isloading = true;
				if (columnIndex == 0 && this.defaultIndex.length && this.defaultIndex.length > 1) {
					this.getCityInfo(rowData,index);
				} else if (columnIndex == 1 && this.defaultIndex.length && this.defaultIndex.length > 2) {
					this.getCountyInfo(rowData,indexs[0],indexs[1]);
				} else {
					this.isloading = false;
				}
			},
			//确认地址信息
			confirm(e) {
				if (this.isloading) {
					return this.$showToast('查询中，请稍后～')
				};

				let list = e.value;
				let codeList = [];
				let ZhList = [];
				for (let i = 0; i < list.length; i++) {
					codeList.push(list[i].areaCode);
					ZhList.push(list[i].areaName);
				}
				this.$emit('confirm', codeList);
				this.$emit('confirmZh', ZhList);
				this.closeModel();
			},
			closeModel() {
				this.$emit('update:show', false)
			},
		}
	}
</script>

<style lang="scss" scoped>
	.Picker {
		color: #000;
	}
</style>
