package com.jiejiao.common.tencent;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiejiao.common.utils.JedisPoolUtil;
import com.jiejiao.common.utils.StringUtil;
import com.jiejiao.common.utils.config.ConfigUtil;
import com.jiejiao.common.utils.http.RequestUtil;
import com.jiejiao.common.utils.log.LogKit;

/**
 * 微信公共类
 * @author shizhiguo
 * @date 2016年12月16日 下午5:07:31
 */
public class WXUtil {
	
	private static final Logger logger =LoggerFactory.getLogger(WXUtil.class);
	
	private static String WXAppID=ConfigUtil.get("wx_app_id");
	private static String WXAppSecret=ConfigUtil.get("wx_app_secret");
	//state参数值
	private static String WXStateStr=ConfigUtil.get("wx_state_str");
	//获取code
	private static String WXCodeUrl="https://open.weixin.qq.com/connect/oauth2/authorize";
	//获取网页授权access_token (和微信基础access_token不同,基础token是全局的;而网页授权token是跟访问用户关联的,最好每次都重新获取)
	private static String WXAccessTokenUrl="https://api.weixin.qq.com/sns/oauth2/access_token";
	//刷新网页授权access_token
	private static String WXRefreshTokenUrl="https://api.weixin.qq.com/sns/oauth2/refresh_token";
	//获取用户信息
	private static String WXUserInfoUrl="https://api.weixin.qq.com/sns/userinfo";
	//拉取公众号用户信息（可判断用户是否关注公众号）
	private static String WXPullUserInfoUrl="https://api.weixin.qq.com/cgi-bin/user/info"; 
	//检验access_token
	private static String WXCheckAccessTokenUrl="https://api.weixin.qq.com/sns/auth";
	//微信公众号 全局唯一接口调用凭据
	private static String WXBaseAccessTokenUrl="https://api.weixin.qq.com/cgi-bin/token";
	//base_access_token缓存key
	private static String WXBaseAccessTokenRedisKey=ConfigUtil.get("wx_access_token_redis_key");
	//发送模版消息接口
	private static String WXSendTemplateMsgUrl="https://api.weixin.qq.com/cgi-bin/message/template/send";
	//发送客服消息
	private static String WXSendKefuMsgUrl="https://api.weixin.qq.com/cgi-bin/message/custom/send";
	//微信企业付款接口
	private static String WXQyPayUrl="https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
	//微信服务号 商户号
	private static String WxMchId=ConfigUtil.get("wx_mch_id");
	//微信服务号商户号密钥
	private static String WxMchKeySecret=ConfigUtil.get("wx_mch_key_secret");
	//微信公众号带参数二维码生成
	private static String WxParamQrcodeCreateUrl="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
	
	
	
	/**
	 * 获取全局access_token
	 * @author shizhiguo
	 * @return 
	 * @date 2017年2月8日 上午11:06:26
	 */
	public static String getBaseAccessToken(){
		
		String token = JedisPoolUtil.get(WXBaseAccessTokenRedisKey);
		
		if (StringUtil.isNullOrEmpty(token)) {
			
			String param="?grant_type=client_credential&appid="+WXAppID+"&secret="+WXAppSecret;
			String res = RequestUtil.getUrl(WXBaseAccessTokenUrl+param);
			
			JSONObject obj = JSON.parseObject(res);
			Integer errcode = obj.getInteger("errcode");
			if(errcode!=null){
				log(obj.getString("errmsg"));
			}else{
				
				token=obj.getString("access_token");
				
				JedisPoolUtil.set(WXBaseAccessTokenRedisKey, token, obj.getIntValue("expires_in")-1000);
				
			}
			
		}
		return token;
	}
	
	/**
	 * 拉取公众号用户信息（判断用户是否关注公众号）
	 * shizhiguo	
	 * 2017年3月4日 下午2:06:58
	 */
	public static JSONObject pullPublicUserInfo(String openid){
		JSONObject map=null;
		try {
			
			String param="?access_token="+getBaseAccessToken()+"&openid="+openid+"&lang=zh_CN";
			String res = RequestUtil.getUrl(WXPullUserInfoUrl+param);
			
			if(!isError(res)){
				map=JSON.parseObject(res);
			}
			
		} catch (Exception e) {
			log("pullPublicUserInfo:ERROR==>"+e.getMessage());
		}
		return map;
	}
	
	
	/**
	 * 微信网页授权
	 * 1 第一步：用户同意授权，获取code
	 * @author shizhiguo
	 * @date 2016年12月16日 下午5:17:51
	 * @param redirectUrl 回调地址
	 * @param isNotifyUser true：scope=snsapi_userinfo  false:snsapi_base(静默)
	 * @return
	 */
	public static void authorize(String redirectUrl,boolean isNotifyUser,HttpServletResponse response){
		String url="";
		try {
			url = WXCodeUrl + "?appid="+WXAppID
							+ "&redirect_uri=" + URLEncoder.encode(redirectUrl,"utf-8") 
							+ "&response_type=code"
							+ "&scope="+(isNotifyUser?"snsapi_userinfo":"snsapi_base")
							+ "&state=" + WXStateStr 
							+ "#wechat_redirect";
			log("WXUtil.authorize==>"+url);
			//跳转授权页面
			response.sendRedirect(url);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 返回微信网页授权url
	 * 1 第一步：用户同意授权，获取code
	 * @author shizhiguo
	 * @date 2016年12月16日 下午5:17:51
	 * @param redirectUrl 回调地址
	 * @param isNotifyUser true：scope=snsapi_userinfo  false:snsapi_base(静默)
	 * @return
	 */
	public static String authorizeUrl(String redirectUrl,boolean isNotifyUser,HttpServletResponse response){
		String url="";
		try {
			url = WXCodeUrl + "?appid="+WXAppID
					+ "&redirect_uri=" + URLEncoder.encode(redirectUrl,"utf-8") 
					+ "&response_type=code"
					+ "&scope="+(isNotifyUser?"snsapi_userinfo":"snsapi_base")
					+ "&state=" + WXStateStr 
					+ "#wechat_redirect";
			log("WXUtil.authorize==>"+url);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return url;
	}
	/**
	 * 返回微信网页授权url
	 * 1 第一步：用户同意授权，获取code
	 * @author shizhiguo
	 * @date 2016年12月16日 下午5:17:51
	 * @param redirectUrl 回调地址
	 * @param isNotifyUser true：scope=snsapi_userinfo  false:snsapi_base(静默)
	 * @return
	 */
	public static String authorizeUrl(String redirectUrl,boolean isNotifyUser,String appid,HttpServletResponse response){
		String url="";
		try {
			url = WXCodeUrl + "?appid="+appid
					+ "&redirect_uri=" + URLEncoder.encode(redirectUrl,"utf-8") 
					+ "&response_type=code"
					+ "&scope="+(isNotifyUser?"snsapi_userinfo":"snsapi_base")
					+ "&state=" + WXStateStr 
					+ "#wechat_redirect";
			log("WXUtil.authorize==>"+url);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return url;
	}
	
	/**
	 * 获取access_token和openid
	 * @author shizhiguo
	 * @date 2016年12月16日 下午5:52:14
	 * @param code
	 */
	public static WxConfig getAccessToken(String code){
		String url=WXAccessTokenUrl + "?appid="+WXAppID+"&secret="+WXAppSecret+"&code="+code+"&grant_type=authorization_code";
		log("WXUtil.getAccessToken==>"+url);
		// 获取响应内容
		String content = RequestUtil.getUrl(url);
		log("WXUtil.getAccessToken==>"+content);
		if(isError(content)){
			return null;
		}
		WxConfig wxconfig = JSON.parseObject(content,WxConfig.class);
		log("WXUtil.getAccessToken==>"+wxconfig.toString());
		return wxconfig;
	}
	
	/**
	 * 获取access_token和openid
	 * @author shizhiguo
	 * @date 2016年12月16日 下午5:52:14
	 * @param code
	 */
	public static WxConfig getAccessToken(String code,String appid,String appsecret){
		String url=WXAccessTokenUrl + "?appid="+appid+"&secret="+appsecret+"&code="+code+"&grant_type=authorization_code";
		log("WXUtil.getAccessToken==>"+url);
		// 获取响应内容
		String content = RequestUtil.getUrl(url);
		log("WXUtil.getAccessToken==>"+content);
		if(isError(content)){
			return null;
		}
		WxConfig wxconfig = JSON.parseObject(content,WxConfig.class);
		log("WXUtil.getAccessToken==>"+wxconfig.toString());
		return wxconfig;
	}
	
	/**
	 * 检查返回内容 失败
	 * 错误返回true  ,成功返回false
	 * @author shizhiguo
	 * @date 2016年12月16日 下午6:01:43
	 * @param res
	 */
	public static boolean isError(String res){
		JSONObject json = JSON.parseObject(res);
		if (json.get("errcode")!=null && json.getIntValue("errcode")!=0) {
			error("WXUtil.isError==>"+res);
			return true;
		}
		return false;
		
	}
	
	/**
	 * 获取用户信息
	 * @author shizhiguo
	 * @date 2016年12月16日 下午6:30:02
	 * @param wxconfig
	 */
	public static WxUserInfo getUserInfo(WxConfig wxconfig){
		if(wxconfig==null){
			return null;
		}
		String wx_userinfo_url=WXUserInfoUrl+"?access_token="+wxconfig.getAccess_token()+"&openid="+wxconfig.getOpenid()+"&lang=zh_CN";
		log("WXUtil.getUserInfo==>"+wx_userinfo_url);
		String res = RequestUtil.getUrl(wx_userinfo_url);
		if(isError(res)){
			return null;
		}
		WxUserInfo user = JSON.parseObject(res,WxUserInfo.class);
		log("WXUtil.getUserInfo==>"+user.toString());
		return user;
	}
	
	
	/**
	 * 发送模版消息
	 * @author shizhiguo
	 * @date 2017年2月8日 下午3:35:16
	 */
	public static JSONObject sendTemplateMsg(String userOpenId,String templateId,String url,Map<String, Object> dataMap){
		
		String postUrl=WXSendTemplateMsgUrl+"?access_token="+getBaseAccessToken();
		
		Map<String, Object> map=new HashMap<String,Object>();
		
		map.put("touser", userOpenId);//oK2NYwpCnvrPYVNqJWoHZELVI2M0
		map.put("template_id", templateId);//st5Zb8jO-p4bb9WzzZg7agmsWp1qionFpgQhRJPn4mE
		map.put("url", url);
		map.put("data", dataMap);
		
		String json = JSON.toJSONString(map);
		
		log(json);
		
		String res = RequestUtil.postUrl(postUrl, json);
		
		log(res);
		JSONObject obj=JSON.parseObject(res);
		return obj;
	}
	
	/**
	 * 发送客服消息
	 * @author shizhiguo
	 * @date 2017年4月1日 下午3:02:44
	 * @param openid
	 * @param type 类型  image/text
	 * @param msg 
	 * @param media_id 多媒体文件id
	 */
	public static boolean sendKefuMsg(String openid,String type,String msg,String media_id){
		try {
			String url=WXSendKefuMsgUrl+"?access_token="+getBaseAccessToken();
			JSONObject json = new JSONObject();
			json.put("touser", openid);
			json.put("msgtype", type);
			JSONObject contentJson = new JSONObject();
			if ("image".equals(type)) {
				contentJson.put("media_id", media_id);
			}else if ("text".equals(type)) {
				contentJson.put("content", msg);
			}
			json.put(type, contentJson);
			String res = RequestUtil.postUrl(url, json.toJSONString());
			LogKit.info("发送客服消息,返回结果==>"+res);
			return isError(res);
		} catch (Exception e) {
			LogKit.error(e + "\tException Line==>" + e.getStackTrace()[0].getLineNumber());
		}
		return false;
	}
	
	/**
	 *  企业付款
	 * @author shizhiguo
	 * @date 2017年2月9日 下午1:57:46
	 * @param openId  支付用户openid
	 * @param userRealName  用户真实姓名
	 * @param amount  金额
	 * @param tradeNo 交易订单号
	 * @param desc 描述信息
	 * @param ip  调用接口的ip
	 * @return
	 */
	public static Map<String, Object> qyPay(String openId,Double amount,String tradeNo,String desc,String ip){
		return qyPay(openId, "temp_name", amount, tradeNo, desc, ip);
	}
	
	/**
	 *  企业付款
	 * @author shizhiguo
	 * @date 2017年2月9日 下午1:57:46
	 * @param openId  支付用户openid
	 * @param userRealName  用户真实姓名
	 * @param amount  金额
	 * @param tradeNo 交易订单号
	 * @param desc 描述信息
	 * @param ip  调用接口的ip
	 * @return
	 */
	public static Map<String, Object> qyPay(String openId,String userRealName,Double amount,String tradeNo,String desc,String ip){
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mch_appid", WXAppID);
		map.put("mchid", WxMchId);
		map.put("nonce_str", RandomStringGenerator.getRandomStringByLength(16));
		map.put("partner_trade_no", tradeNo);
		map.put("openid", openId);
		map.put("check_name", "NO_CHECK");
		map.put("re_user_name", userRealName);
		map.put("amount", ((Double)(amount*100)).intValue());
		map.put("desc", desc);
		map.put("spbill_create_ip", ip);
		
		String sign = Signature.getSign(map);
		map.put("sign", sign);
		
		
		HttpsRequest request=null;
		Map<String, Object> return_map=null;
		try {
			request = new HttpsRequest();
		
			String res = request.sendPost(WXQyPayUrl,map);
			
			log("请求返回的数据:"+res);
			
			return_map = XMLParser.getMapFromXML(res);
			
		} catch (Exception e) {
			LogKit.error(e + "\tException Line==>" + e.getStackTrace()[0]);
		}
		
		return return_map;
	}
	
	/**
	 * 获取带参数二维码
	 * @author shizhiguo
	 * @date 2017年4月6日 下午3:20:49
	 * @param expire_day有效天数，最大30天
	 * @param scene_id 场景值ID(1--100000)
	 * @return
	 */
	public static String getParamQrcode(Integer expire_day,Integer scene_id){
		String qrcode_url=null;
		try {
			String postUrl=WxParamQrcodeCreateUrl.replace("TOKEN", getBaseAccessToken());
			JSONObject jObject=new JSONObject();
			jObject.put("expire_seconds", expire_day * 3600 * 24);
			jObject.put("action_name", "QR_SCENE");//二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久,QR_LIMIT_STR_SCENE为永久的字符串参数值
			JSONObject action_info=new JSONObject();
			action_info.put("scene_id", scene_id);
			jObject.put("action_info", action_info);
			String res = RequestUtil.postUrl(postUrl, jObject.toJSONString());
			LogKit.info("生成带参数二维码：返回结果==>"+res);
			JSONObject json = JSON.parseObject(res);
			if (json!=null) {
				String ticket = json.getString("ticket");//获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
				String url = json.getString("url");
				qrcode_url=url;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return qrcode_url;
	}
	
	
	
	
	
	/**
	 * 输出日志
	 * @author shizhiguo
	 * @date 2016年12月16日 下午6:43:11
	 */
	private static void log(Object obj){
		logger.info(obj.toString());
	}
	/**
	 * 输出日志
	 * @author shizhiguo
	 * @date 2016年12月16日 下午6:43:11
	 */
	private static void error(Object obj){
		logger.error(obj.toString());
	}
	
	/**
	 * 校验state
	 * @author shizhiguo
	 * @date 2016年12月19日 上午11:26:16
	 * @param state
	 * @return
	 */
	public static boolean validateState(String state){
		if(!WXStateStr.equals(state)){
			log("WXUtil.validateState==>state错误");
			return false;
		}
		return true;
	}
	

	
	
}

