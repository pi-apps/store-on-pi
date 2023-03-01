// 系统版本号
export const version = '1.0.0';

// API接口baseUrl封装
export const baseUrl = () => {
	let baseUrl = '';
	if (process.env.NODE_ENV === 'development') {
		// 开发环境
		baseUrl = 'https://pro.pi-store.app' //test
	} else {
		// 生产环境
		baseUrl = 'https://pro.pi-store.app' //pro 
	}
	return baseUrl;
}

