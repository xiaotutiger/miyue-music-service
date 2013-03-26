package com.miyue.service;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.google.code.ssm.api.ParameterValueKeyProvider;
import com.google.code.ssm.api.ReadThroughSingleCache;
import com.miyue.dao.MyDao;
import com.miyue.pojo.MyBean;

/**
 * 
 * @author spring.tu
 *
 */
@Component
public class MyService implements IMyService{
	@Resource
	private MyDao myDao;
	public List<MyBean> getAllMyDaos(){
		return myDao.getAll();
	}
	@Override
	public void saveWith() {
		
	}
	@ReadThroughSingleCache(namespace = "mystar", expiration = 60)
	public MyBean getMyDaoById(@ParameterValueKeyProvider Integer id) {
		// TODO Auto-generated method stub
		return myDao.get(id);
	}
}
