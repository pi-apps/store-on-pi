import en from '../locale/en.json'
import zhCn from '../locale/zh-Hans.json'
import zhHk from '../locale/zh-Hans.json'
import ko from '../locale/ko.json'


const messages = {
	en,
	'zh-Hans':zhCn,
	'zh-Hant':zhHk,
	'ko':ko,
}


export default {
	locale: uni.getLocale(),// 获取已设置的语言
	messages
}