// const mixin 混入组件 
import{ mapMutations, mapState } from 'vuex'  
export default {
	data() {
		return {
			mixin:{
				
			}
		};
	},
	
	computed: {
		...mapState({
			dictArr: state => state.dictArrs,
			
			isNoReadMsg: state => state.isNoReadMsg,
			isContact: state => state.isContact
		})
	}
}
