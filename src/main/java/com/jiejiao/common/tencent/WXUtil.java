package com.jiejiao.common.tencent;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiejiao.common.utils.JedisPoolUtil;
import com.jiejiao.common.utils.StringUtil;
import com.jiejiao.common.utils.config.ConfigUtil;
import com.jiejiao.common.utils.http.RequestUtil;

/**
 * 微信公共类
 * @author shizhiguo
 * @date 2016年12月16日 下午5:07:31
 */
public class WXUtil {
	
	private static String AppID=ConfigUtil.get("wx_app_id");
	private static String AppSecret=ConfigUtil.get("wx_app_secret");
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
	//检验access_token
	private static String WXCheckAccessTokenUrl="https://api.weixin.qq.com/sns/auth";
	//微信公众号 全局唯一接口调用凭据
	private static String WXBaseAccessTokenUrl="https://api.weixin.qq.com/cgi-bin/token";
	//base_access_token缓存key
	private static String WXBaseAccessTokenRedisKey=ConfigUtil.get("wx_access_token_redis_key");
	//发送模版消息接口
	private static String WXSendTemplateMsgUrl="https://api.weixin.qq.com/cgi-bin/message/template/send";
	
	
	/**
	 * 获取全局access_token
	 * @author shizhiguo
	 * @return 
	 * @date 2017年2月8日 上午11:06:26
	 */
	public static String getBaseAccessToken(){
		
		String token = JedisPoolUtil.get(WXBaseAccessTokenRedisKey);
		
		if (StringUtil.isNullOrEmpty(token)) {
			
			String param="?grant_type=client_credential&appid="+AppID+"&secret="+AppSecret;
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
			url = WXCodeUrl + "?appid="+AppID
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
	 * 获取access_token和openid
	 * @author shizhiguo
	 * @date 2016年12月16日 下午5:52:14
	 * @param code
	 */
	public static WxConfig getAccessToken(String code){
		String url=WXAccessTokenUrl + "?appid="+AppID+"&secret="+AppSecret+"&code="+code+"&grant_type=authorization_code";
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
			log("WXUtil.isError==>"+res);
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
	public static boolean sendTemplateMsg(String userOpenId,String templateId,String url,Map<String, Object> dataMap){
		
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

		return !isError(res);
		
	}
	
	
	
	/**
	 * 输出日志
	 * @author shizhiguo
	 * @date 2016年12月16日 下午6:43:11
	 */
	private static void log(Object obj){
		System.out.println(obj.toString());
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
	
	public static void main(String[] args) {
		String json="{\"access_token\":\"a19BvoJ1jgLWR-WDA4cEwBTvbJ5IZLBdJLO40SFsld1h1qiqJ5yqbfHCZw9aOpKWaXwvxjhV7JEloTo2iioSS4KsFV85ArpyGODaSdgxzzw\",\"expires_in\":7200,\"refresh_token\":\"jRmeiRHkBFXtJ-AStZWmOGOtbrYWhRX5PTz4pOrMYNW3VvORAFGEnOfewzL9Qx2KrkpRGFYZ7tPWLz-s0WArv_SgY0InQpnORu2Zv0k4dhk\",\"openid\":\"oK2NYwpCnvrPYVNqJWoHZELVI2M0\",\"scope\":\"snsapi_userinfo\",\"unionid\":\"oXUsNxNSLxBL6edD18Y-PnZpOgyM\"}";
		WxConfig c = JSON.parseObject(json,WxConfig.class);
		//WxConfig config=(new WXUtil()).new WxConfig();
		//config.setAccess_token("access_token");
		System.out.println(c);
	}

	
	
}

