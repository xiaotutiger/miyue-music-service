package com.miyue.components.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 纯文本邮件服务类.
 * 
 */
public class SimpleMailService {
	private static Logger logger = LoggerFactory.getLogger("mail");
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private SimpleMailMessage msg;
	
	private String mailTo;

	/**
	 * 发送纯文本邮件.
	 * @param content 邮件正文
	 */
	public void sendMail(String content) {
		mailTo = msg.getTo()[0];
		if(mailTo.contains(";")){
			setTo(mailTo.split(";"));
		}
		//设置邮件正文
		msg.setText(content);
		//异步发送邮件
		new Thread(new Runnable() {  
            public void run() {  
        		try {
        			mailSender.send(msg);
        			if (logger.isInfoEnabled()) {
        				logger.debug("纯文本邮件已发送至{}", mailTo);
        			}
        		} catch (Exception e) {
        			logger.error("发送邮件失败", e);
        		} 
            }  
        }).start();  
	}

	public void setFrom(String from) {
		msg.setFrom(from);
	}
	/**
	 * 单人发送地址设置
	 * @param to
	 */
	public void setTo(String to) {
		msg.setTo(to);
	}
	/**
	 * 多人发送地址设置
	 * @param to
	 */
	public void setTo(String[] to) {
		msg.setTo(to);
	}

	public void setSubject(String subject) {
		msg.setSubject(subject);
	}
	
	

}
