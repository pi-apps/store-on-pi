/*
* filePath 请求文件路径
*/ 

import { baseUrl } from '@/utils';

const $uplaod = (filePath) =>{
	let token = uni.getStorageSync('Token') || '';
	/// 默认
	return new Promise((resolve, reject)=>{
		uni.showLoading({
			title:'上传中...',
			mask:true
		});
		let fileUrl = [];
		uni.uploadFile({
			url:baseUrl()+'/baseuser/auth/uploadFile/upload',
			filePath:filePath,
			header:{
				"token": token,
			},
			name:'file',
			success:function(res){
				uni.hideLoading();
				let data = JSON.parse(res.data);
				if(data.code == '0'){
					let url = data.data;
					resolve(url)
				}else if(data.code == 401){
				  uni.showToast({
					title: res.msg || '登录超时，请重新登录～',
					icon: 'none'
				  })
				  setTimeout(()=>{
					uni.redirectTo({
					  url: '/pages/login/login',
					})
				  },500)
				}else{
					uni.showToast({
						title:data.msg || '上传失败',
						icon:'none',
						duration:2000
					})
					reject(data)
				}
				
			},
			fail:function(e){
				uni.hideLoading();
				uni.showToast({
					title:'上传失败',
					icon:'none',
					duration:2000
				})
				reject(e)
			}
		})
	});
}

export default $uplaod;