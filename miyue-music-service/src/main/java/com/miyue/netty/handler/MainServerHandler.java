package com.miyue.netty.handler;

import static org.jboss.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.http.HttpRequest;
import com.miyue.netty.vo.HandlerConditionChecker;
import com.miyue.netty.vo.MiyueTechRequest;
import com.miyue.util.NettyUtils;

/**
 * 
 * @author spring.tu
 *
 */
public class MainServerHandler extends SimpleChannelUpstreamHandler{
	
	private static final Logger logger = Logger.getLogger("mainHandler");
	
	/**
	 * key is the uri identity,value is the handler
	 */
	private ConcurrentMap<String, Handler>urlHandlerMap;

	@Override
    public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e)
            throws Exception {
		logger.info("enter the messageReceived method...");
		String uri = null;
		byte bytes[] = null;
		HttpRequest httpRequest = null;
		//define request to warp data
		MiyueTechRequest skyMobiRequest = new MiyueTechRequest();
		// do the http request 
		httpRequest = (HttpRequest) e.getMessage();
		uri = httpRequest.getUri();
		String jsonResult = null;
		logger.debug("uri is:" + uri);
		if(httpRequest != null){
			logger.debug("do the params's process....");
			NettyUtils.processNettyRequestParams(skyMobiRequest, httpRequest);
		}
		String key = null;
		if(uri.indexOf("?") != -1){
			key = StringUtils.substring(uri,1,uri.indexOf("?"));
		}else{
			key = StringUtils.substring(uri, 1);
		}
		logger.debug("uri key is:" + key);
		Handler handler = urlHandlerMap.get(key);
		if(handler != null){
			logger.debug("hello I'm "+ handler.getName() +". Welcome to enter the handler....");
			HandlerConditionChecker handlerConditionChecker = new HandlerConditionChecker(bytes);
			handlerConditionChecker.setUriName(key);
			handlerConditionChecker.setHttpRequest(httpRequest);
			handlerConditionChecker.setE(e);
			List<Map.Entry<String, String>> headers = httpRequest.getHeaders();
			skyMobiRequest.setHeaders(headers);
			handlerConditionChecker.setSkyMobiRequest(skyMobiRequest);
			try {
				jsonResult = handler.doHandler(handlerConditionChecker);
				if(StringUtils.isNotEmpty(jsonResult)){
					NettyUtils.writeToClient(jsonResult, e);
				}else{
					NettyUtils.codeErrorResponse(500, e);
				}
			} catch (Exception e2) {
				logger.error(e2.getMessage());
			}
		}
	}
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        Channel ch = e.getChannel();
        Throwable cause = e.getCause();
        if (cause instanceof TooLongFrameException) {
        	NettyUtils.sendError(ctx, BAD_REQUEST);
            return;
        }
        logger.info("channel exception :" + cause.getMessage());
        //cause.printStackTrace();
        if (ch.isConnected()) {
            logger.info("channel exception connected :" + cause.getMessage());
            NettyUtils.sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }
	public void setUrlHandlerMap(ConcurrentMap<String, Handler> urlHandlerMap) {
		this.urlHandlerMap = urlHandlerMap;
	}
}
