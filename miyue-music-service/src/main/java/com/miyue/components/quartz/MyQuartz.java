package com.miyue.components.quartz;

import org.apache.log4j.Logger;

/**
 * 
 * @author feize
 *	dispatch the mathod by properties's quartz config
 */
public class MyQuartz {

	private static final Logger logger = Logger.getLogger("quartz");
	
	public void startQuartz(){
		logger.debug("starting quartz......");
	}
}
