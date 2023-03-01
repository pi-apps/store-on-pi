import API from './api'

// token 注册
export const userToken = (data) => {
	return API('/app/unAuth/access/me', 'POST', data);
}


// 首页坦克
export const popTips = () => {
	return API('/cert/popup/tips', 'GET', {});
}

// 支付授权
export const payAuth = (data) => {
	return API('/app/unAuth/access/approve', 'POST', data);
}
// 支付成功
export const payDone = (data) => {
	return API('/app/unAuth/access/complete', 'POST', data);
}
// 支付取消
export const payCancel = (data = '') => {
	return API('/app/unAuth/access/cancelledPayment', 'POST', data);
}
// 支付未完成
export const payIncomplete = (data) => {
	return API('/app/unAuth/access/incomplete', 'POST', data);
}


// 退款
export const orderRefund = (orderId = '') => {
	return API('/mer/auth/store/user/order/refund', 'POST', orderId);
}



// 查询图标
export const _getHomePage = () => {
	return API('/app/unAuth/homePage/getHomePage/', 'GET', {});
}

//查询下啦
export const selectKeys = (data = '') => {
	return API('/cert/dict/selectByValue/' + data, 'GET', {});
}
// 位置banner查询
export const _getAd = (data = '') => {
	return API('/app/unAuth/ad/getAd/' + data, 'GET', {});
}


//获取用户信息
export const _userInfo = (isJump = true, bol = true) => {
	return API('/app/auth/userInfo/info', 'POST', {}, {
		header: "application/json;charset=utf-8",
		isJump: isJump,
		showToast: bol
	});
}

// 我的粉丝数量
export const _mySubRecommendTotal = () => {
	return API('/app/auth/user/mySubRecommendTotal', 'POST', {}, {
		header: "application/json;charset=utf-8",
		isJump: true,
		showToast: false
	});
}
// 我的粉丝列表
export const _mySubRecommendList = (data = {}) => {
	return API('/app/auth/user/mySubRecommendList', 'POST', data);
}
// 我的拓店数量
export const _myExpandStoreTotal = () => {
	return API('/app/auth/user/myExpandStoreTotal', 'POST', {}, {
		header: "application/json;charset=utf-8",
		isJump: true,
		showToast: false
	});
}
// 我的拓店列表
export const _myExpandStoreList = (data = {}) => {
	return API('/app/auth/user/myExpandStoreList', 'POST', data);
}

// 
//修改用户信息
export const _updateUser = (data = {}) => {
	return API('/app/auth/user/updateUser', 'POST', data);
}

// 查询码值
export const queryByType = (type = '') => {
	return API('/cert/dict/queryByType/' + type, 'GET', {});
}

////=====================================================================================================================
//门店入住
export const _storeAplly = (data = {}) => {
	return API('/mer/auth/store/apply/commit', 'POST', data);
}
//门店入住
export const _getStoreInfo = () => {
	return API('/mer/auth/store/apply/getStoreInfo', 'POST', {}, {
		header: "application/json;charset=utf-8",
		isJump: true,
		showToast: false
	});
}

// 商城=================================
// 查询分类
export const _listGoodsType = (data = {}) => {
	return API('/app/unAuth/mall/listGoodsType', 'POST', data);
}

//商品
export const _listGoods = (data = {}) => {
	return API('/app/unAuth/mall/listGoods', 'POST', data);
}

//秒杀
export const _currentSeckillTime = (data = {}) => {
	return API('/app/unAuth/mall/apiCurrentSeckillTime', 'POST', data);
}
// 秒杀商品时间段列表
export const _apiListSeckillTime = (data = {}) => {
	return API('/app/unAuth/mall/apiListSeckillTime', 'POST', data);
}

// 秒杀商品时间段列表 == 下的商品
export const _apiListSeckillGoods = (data = {}) => {
	return API('/app/unAuth/mall/apiListSeckillGoods', 'POST', data);
}

//特权商品
export const _vipListGoods = (data = {}) => {
	return API('/app/unAuth/vip/listGoods', 'POST', data);
}

// 商品详情  == 普通
export const _apiGoodsInfo = (data = {}) => {
	return API('/app/unAuth/mall/apiGoodsInfo', 'POST', data);
}
// 商品详情  == 秒杀
export const _apiGoodsSeckillInfo = (data = {}) => {
	return API('/app/unAuth/mall/apiGetSeckillGoods', 'POST', data);
}
// 商品详情  == vip
export const _apiGoodsVipDetail = (data = {}) => {
	return API('/app/unAuth/vip/goodsDetail', 'POST', data);
}
// 商品详情  == 签到
export const _apiIntegralGoodsDetail = (data = {}) => {
	return API('/app/auth/integralGoods/goodsDetail', 'POST', data);
}


// 收货地址
// 列表
export const _listAddress = (data = {}) => {
	return API('/app/auth/address/listAddress', 'POST', data);
}
// 地址状态修改
export const _removeAddress = (data = {}) => {
	return API('/app/auth/address/negationStatus', 'POST', data);
}
// 确认收货商城
export const _confirmGoods = (orderId = '') => {
	return API('/app/auth/mall/confirmGoods', 'POST', orderId);
}
// 确认收货商城Vip
export const _confirmGoodsVip = (orderId = '') => {
	return API('/app/auth/vip/confirmGoods', 'POST', orderId);
}

// 国家
export const getAllCountry = () => {
	return API('/cert/area/getAllCountry', 'POST', {});
}
// 省
export const getProviceInfo = (data='') => {
	return API('/cert/area/getAllProvince', 'POST', data);
}
// 市
export const getCityInfo = (data = '') => {
	return API('/cert/area/getCityByProvince', 'POST', data);
}
// 县
export const getCountyInfo = (data = '') => {
	return API('/cert/area/getCountyByCity', 'POST', data);
}
// 保存区域信息
export const saveUserCode = (data = '') => {
	return API('/app/auth/userInfo/bindingLocation', 'POST', data);
}
// 保存语言信息
export const saveUserLang = (data = '') => {
	return API('/app/auth/userInfo/bindingLanguage', 'POST', data);
}
// 保存s上級信息
export const bindingParentId = (data = '') => {
	return API('/app/auth/userInfo/bindingParentId', 'POST', data);
}









// 新增收货地址
export const _addAddress = (data = {}) => {
	return API('/app/auth/address/addAddress', 'POST', data);
}
// 新增收货地址
export const _modifyAddress = (data = {}) => {
	return API('/app/auth/address/modifyAddress', 'POST', data);
}
// 查询收货地址
export const _getAddress = (data = {}) => {
	return API('/app/auth/address/getAddress', 'POST', data);
}
// 查询默认收货地址
export const _apiGetShowAddress = (data = {}) => {
	return API('/app/auth/address/apiGetShowAddress', 'POST', data);
}
// 下单  ===== 普通商品
export const _createMallTradeOrder = (data = {}) => {
	return API('/app/auth/mall/createMallTradeOrder', 'POST', data);
}
// 下单  ===== 秒杀商品
export const _createSeckillTradeOrder = (data = {}) => {
	return API('/app/auth/mall/createSeckillTradeOrder', 'POST', data);
}
// 下单  ===== 特权商品
export const _createVipGoodsOrder = (data = {}) => {
	return API('/app/auth/vip/createVipGoodsOrder', 'POST', data);
}
// 下单  ===== 签到商品
export const _createIntegralOrder = (data = {}) => {
	return API('/app/auth/integralOrder/createIntegralOrder', 'POST', data);
}

// 支付开始========================================================================
// 支付 == 
export const _getPay = (data = {}) => {
	return API('/payment/auth/user/getPay', 'POST', data);
}




////=====================================================================================================================
// 立即签到
export const _sign = (data = {}) => {
	return API('/app/auth/sign/sign', 'POST', data, {
		header: "application/json;charset=utf-8",
		isJump: true,
		showToast: false
	});
}

// 连续签到 - 补签
export const _afterSign = (data = {}) => {
	return API('/app/auth/sign/afterSign', 'POST', data);
}

// 连续签到 -配置
export const _signConfig = (data = {}) => {
	return API('/app/auth/sign/signConfig', 'POST', data);
}
// 查询基础分数
export const _signBaseScoreNum = (data = {}) => {
	return API('/app/auth/sign/signBaseScoreNum', 'POST', data);
}
// 查询最近30天的 签到天数
export const _signDays = (data = {}) => {
	return API('/app/auth/sign/signDays', 'POST', data);
}
// 查询最近的签到记录
export const _signLog = (data = {}) => {
	return API('/app/auth/sign/signLog', 'POST', data);
}
// 查询积分商品
export const _integralGoods = (data = {}) => {
	return API('/app/auth/integralGoods/goodsList', 'POST', data);
}

// 查询积分兑换配置信息
export const _convertConfig = (data = {}) => {
	return API('/app/auth/amountPoints/convertConfig', 'POST', data);
}
// 积分兑换
export const _convert = (amount = '') => {
	return API('/app/auth/amountPoints/convert', 'POST', amount);
}













////======================订单===============================================================================================
// 商城订单
export const _orderList = (data = {}) => {
	return API('/app/auth/mall/orderList', 'POST', data);
}
// 特权订单
export const _vipOrderList = (data = {}) => {
	return API('/app/auth/vip/orderList', 'POST', data);
}
// 积分商城订单
export const _integralOrderList = (data = {}) => {
	return API('/app/auth/integralOrder/orderList', 'POST', data);
}
// 签到积分记录
export const _signDetail = (data = {}) => {
	return API('/app/auth/sign/detail', 'POST', data);
}
//PI积分记录
export const _getAmountPointChangeInfo = (data = {}) => {
	return API('/app/auth/user/getAmountPointChangeInfo', 'POST', data);
}

// 门店==========================================================================================================
// 门店列表
export const _storeList = (data = {}) => {
	return API('/mer/unAuth/store/storeList', 'POST', data);
}
// 门店详情
export const _getStoreInfoById = (data = {}) => {
	return API('/mer/unAuth/store/storeDetail', 'POST', data);
}
// 门店分类
export const _storeClassList = (id = '') => {
	return API('/mer/unAuth/store/service/getList', 'POST', id);
}





// 门店 商品列表
export const _getStoreGoodsList = (data = {}) => {
	return API('/mer/unAuth/store/getStoreGoodsList', 'POST', data);
}
// 门店 商品详情
export const _storeGoodsInfo = (data = {}) => {
	return API('/mer/unAuth/store/getStoreGoods', 'POST', data);
}

// 门店上下线
export const _switchStore = (data = {}) => {
	return API('/mer/auth/store/account/switchStore', 'POST', data);
}
// 门店营业时间
export const _updateOpenTime = (data = {}) => {
	return API('/mer/auth/store/account/updateOpenTime', 'POST', data);
}
// 门店商品列表
export const _getGoodList = (data = {}) => {
	return API('/mer/auth/store/goods/getList', 'POST', data);
}

// 获取特权套餐包
export const _getStoreGoodsListPackage = (data = {}) => {
	return API('/mer/unAuth/store/getStoreGoodsListActivities', 'POST', data);
}

// 门店商品新增
export const _publishgood = (data = {}) => {
	return API('/mer/auth/store/goods/publishgoods', 'POST', data);
}
// 门店商品上下架
export const _updateGoods = (data = {}) => {
	return API('/mer/auth/store/goods/updateGoods', 'POST', data);
}
// 门店商品名称
export const _keywordList = (data = {}) => {
	return API('/mer/auth/store/goods/service/getList', 'POST', data);
}
// 门店商品下单
export const _userBuyGoods = (data = {}) => {
	return API('/mer/auth/store/user/order/userBuyGoods', 'POST', data);
}
// 门店商品订单
export const _userOrderList = (data = {}) => {
	return API('/mer/auth/store/user/order/userOrderList', 'POST', data);
}
// 门店商品订单 详情
export const _getOrderDetails = (id = '') => {
	return API('/mer/auth/store/user/order/getOrderDetails', 'POST', id);
}

// 商家 = 订单
export const _storeOrder = (data = {}) => {
	return API('/mer/auth/store/account/storeOrder', 'POST', data);
}
// 查询核销吗
export const _scanInfo = (data = {}) => {
	return API('/mer/auth/store/account/scanCode', 'POST', data);
}
// 核销
export const _useScan = (data = {}) => {
	return API('/mer/auth/store/account/use', 'POST', data);
}
// 查看凭证
export const _storeOrderCert = (data = {}) => {
	return API('/mer/auth/store/account/storeOrderCert', 'POST', data);
}

// 门店提现余额
export const _getBalance = () => {
	return API('/mer/auth/store/account/getBalance', 'POST', {});
}

// 修改门店企业信息
export const _updateCompanyInfo = (data = {}) => {
	return API('/mer/auth/store/account/updateCompanyInfo', 'POST', data);
}


// 
// 实名认证
export const _saveOrUpdate = (data = {}) => {
	return API('/app/auth/user/userAuth/saveOrUpdate', 'POST', data);
}
// 实名认证 查询
export const _queryUserAuth = (data = {}) => {
	return API('/app/auth/user/userAuth/queryUserAuth', 'POST', data, {
		header: "application/json;charset=utf-8",
		isJump: true,
		showToast: false
	});
}
// 实名认证 OCR
export const _imgOCR = (url = '') => {
	return API('/app/auth/user/userAuth/certOCR', 'POST', url);
}

// 卡列表
export const _getCardList = (data = {}) => {
	return API('/app/auth/user/userCard/getList', 'POST', data);
}
// 新增
export const _saveCard = (data = {}) => {
	return API('/app/auth/user/userCard/save', 'POST', data);
}

// 修改
export const _updateCard = (data = {}) => {
	return API('/app/auth/user/userCard/update', 'POST', data);
}

// 开户行
export const _getbank2 = (data = {}) => {
	return API('/app/auth/user/userCard/getbank', 'POST', data);
}

// 查询
export const _queryByIdCard = (id = '') => {
	return API('/app/auth/user/userCard/queryById', 'POST', id);
}

// 删除
export const _deleteByIdCard = (id = '') => {
	return API('/app/auth/user/userCard/delete', 'POST', id);
}

// 客服
export const _getCustService = (data = {}) => {
	return API('/app/unAuth/custService/getCustService', 'get', data);
}

// 协议
export const _getAgreement = (agreementId = '') => {
	return API('/app/unAuth/agreement/getAgreement/' + agreementId, 'get', {});
}

// 提现
export const _cashAmount = (data = {}) => {
	return API('/app/auth/user/userCash/createCashOrder', 'POST', data);
}

// 提现记录
export const _cashAmountRoad = (data = {}) => {
	return API('/app/auth/user/userCash/getlist', 'POST', data);
}

// 物流查询
export const _orderExprion = (data = {}) => {
	return API('/app/auth/user/order/userLogistics', 'POST', data);
}

// 20221020 新版UI新增
// 普通猜你喜欢
export const _listRandomGoods = (data = {}) => {
	return API('/app/unAuth/mall/listRandomGoods', 'POST', data);
}

// 特权猜你喜欢
export const _vipListRandomGoods = (data = {}) => {
	return API('/app/unAuth/vip/listRandomGoods', 'POST', data);
}