package com.miyue.util;

import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.util.CharsetUtil;

import com.miyue.netty.vo.MiyueTechRequest;

/**
 * 
 * @author feize
 * 
 */
public class NettyUtils {

	private static final Logger logger = Logger.getLogger("common");

	/**
	 * get the httpheader's params
	 * 
	 * @param params
	 * @param key
	 * @return
	 */
	public static String extractParamsValue(Map<String, List<String>> params,
			String key) {
		if (StringUtils.isEmpty("key")) {
			logger.error("the key is empty.");
			return "";
		}
		if (params == null || params.size() == 0) {
			logger.error("sorry, the params is null.");
			return "";
		}
		List<String> valueList = params.get(key);
		if (valueList == null || valueList.size() == 0) {
			return "";
		}
		if (valueList.size() > 1) {
			logger.error("the size() of valueList [key :" + key + "]  is +"
					+ valueList.size()
					+ ", i will use the last index of valueList[value:"
					+ valueList.get(valueList.size() - 1) + "]");
			return valueList.get(valueList.size() - 1);
		}
		return valueList.get(0);
	}

	/**
	 * @param create
	 *            channelBuffer
	 */
	public static ChannelBuffer generateChannelBuffer(String content) {
		if (logger.isDebugEnabled()) {
			logger.debug("adv json:" + content);
		}
		ChannelBuffer buffer = new DynamicChannelBuffer(2048);
		try {
			buffer.writeBytes(content.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error("write json to buffer error", e);
		}
		return buffer;
	}

	/**
	 * default is false to the client
	 * 
	 * @param content
	 * @param e
	 */
	public static void writeToClient(String content, final MessageEvent e) {
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
		response.setContent(generateChannelBuffer(content));
		response.setHeader("Content-Type", "text/html;charset=UTF-8");
		response.setHeader("Content-Length", response.getContent()
				.writerIndex());

		if (e.getChannel().isOpen() && e.getChannel().isWritable()) {
			ChannelFuture future = e.getChannel().write(response);
			future.addListener(CLOSE_CHANNEL);
		}
	}
	
	/**
	 * 处理netty的请求参数
	 * @return
	 */
	public static void processNettyRequestParams(MiyueTechRequest skyMobiRequest,HttpRequest request){
		QueryStringDecoder queryStringDecoder = new QueryStringDecoder(
				request.getUri());
		Map<String, List<String>> params = queryStringDecoder.getParameters();

		skyMobiRequest.setUrl(request.getHeader("url") == null ? ""
				: request.getHeader("url").trim());

		skyMobiRequest.setHost(request.getHeader("host") == null ? ""
				: request.getHeader("host"));
	}
	
	static ChannelFutureListener CLOSE_CHANNEL = new ChannelFutureListener() {
        // Logger logger = Logger.getLogger("CLOSE CHANNEL");
        public void operationComplete(ChannelFuture future) {
            logger.info("channel close!");
            future.getChannel().disconnect();
            future.getChannel().close();
        }
    };
    
	public static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, status);
        response.setHeader("Content-Type", "text/plain; charset=UTF-8");
        response.setContent(ChannelBuffers.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));

        // Close the connection as soon as the error message is sent.
        ctx.getChannel().write(response).addListener(CLOSE_CHANNEL);
    }
	public static void codeErrorResponse(int code,final MessageEvent e) {
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
		HttpResponseStatus httpResponseStatus = new HttpResponseStatus(code, "no data response from server");
		response.setStatus(httpResponseStatus);
		response.setHeader("Content-Type", "text/html;charset=UTF-8");
		response.setHeader("Content-Length", response.getContent()
				.writerIndex());

		if (e.getChannel().isOpen() && e.getChannel().isWritable()) {
			ChannelFuture future = e.getChannel().write(response);
			future.addListener(CLOSE_CHANNEL);
		}
	}
}
