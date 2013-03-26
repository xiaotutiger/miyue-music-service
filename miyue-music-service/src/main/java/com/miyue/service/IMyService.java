package com.miyue.service;

import java.util.List;

import com.miyue.pojo.MyBean;

/**
 * 
 * @author spring.tu
 *
 */
public interface IMyService {
	List<MyBean>getAllMyDaos();
	MyBean getMyDaoById(Integer id);
	void saveWith();
}
