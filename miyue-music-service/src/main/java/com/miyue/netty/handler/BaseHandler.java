package com.miyue.netty.handler;

import java.util.Map;

import com.miyue.json.request.pojo.BaseRequest;
import com.miyue.netty.vo.HandlerConditionChecker;
import com.miyue.util.JackSonUtil;


/**
 * cdnhandler 用于根据json分发请求
 * @author TWL
 *
 */
public class BaseHandler extends AbstractHandler implements Handler{
	
	/**
	 * key is the optype identity,value is the handler
	 */
	private Map<String, Handler>cdsHandlerMap;
	@Override
	public String doHandler(HandlerConditionChecker handlerConditionChecker) {
		String content = handlerConditionChecker.getContent();
		BaseRequest baseCdnRequest = (BaseRequest)JackSonUtil.json2Bean(content, BaseRequest.class);
		String optype = baseCdnRequest.getOptype();
		Handler handler = cdsHandlerMap.get(optype);
		return handler.doHandler(handlerConditionChecker);
	}

	@Override
	public String getName() {
		return getClassName(BaseHandler.class);
	}

	public void setCdsHandlerMap(Map<String, Handler> cdsHandlerMap) {
		this.cdsHandlerMap = cdsHandlerMap;
	}

}
