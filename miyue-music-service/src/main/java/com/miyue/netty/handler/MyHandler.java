package com.miyue.netty.handler;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.miyue.components.mail.SimpleMailService;
import com.miyue.netty.vo.HandlerConditionChecker;
import com.miyue.pojo.MyBean;
import com.miyue.service.IMyService;


/**
 * your private handler to dohandler
 * @author spring.tu
 *
 */
public class MyHandler extends AbstractHandler implements Handler{
	


	@Autowired
	private SimpleMailService simpleMailService;
	@Resource
	private IMyService myService;
	@Override
	public String doHandler(HandlerConditionChecker handlerConditionChecker){
		logger.debug("hello,you are enter your private handler,Congratulations!");
//		MyBean myBean = myService.getMyDaoById(new Integer(1));
//		System.out.println(myBean.getName());
//		String msgContent = "test for email";
//		simpleMailService.sendMail(msgContent);
//		return myBean.getName();
		return "OK";
	}
	@Override
	public String getName() {
		return getClassName(MyHandler.class);
	}

}
