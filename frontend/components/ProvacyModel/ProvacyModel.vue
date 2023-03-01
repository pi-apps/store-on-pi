<template>
	<view>
		<u-overlay :show="showProvacy"></u-overlay>
	</view>
</template>

<script>
	export default {
		name:"provacyModel",
		data() {
			return {
				showProvacy:false
			};
		}
	}
</script>

<style lang="scss">

</style>
