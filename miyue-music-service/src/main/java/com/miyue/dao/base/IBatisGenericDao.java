package com.miyue.dao.base;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import com.ibatis.common.util.PaginatedList;
import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.miyue.util.Page;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.util.Assert;

/**
 * IBatis Dao的泛型基类.
 * <p/>
 * 继承于Spring的SqlMapClientDaoSupport,提供分页函数和若干便捷查询方法，并对返回值作了泛型类型转换.
 *
 * @author spring.tu
 * @see SqlMapClientDaoSupport
 */
@SuppressWarnings("unchecked")
public class IBatisGenericDao extends SqlMapClientDaoSupport {

	public static final String POSTFIX_INSERT = ".insert";

	public static final String POSTFIX_UPDATE = ".update";

	public static final String POSTFIX_DELETE = ".delete";

	public static final String POSTFIX_DELETE_PRIAMARYKEY = ".deleteByPrimaryKey";

	public static final String POSTFIX_SELECT = ".select";

	public static final String POSTFIX_SELECTMAP = ".selectByMap";

	public static final String POSTFIX_SELECTSQL = ".selectBySql";

	public static final String POSTFIX_COUNT = ".count";

	/**
	 * 根据ID获取对象
	 */
	public <T> T get(Class<T> entityClass, Serializable id) {

		T o = (T) getSqlMapClientTemplate().queryForObject(entityClass.getName() + POSTFIX_SELECT, id);
		if (o == null)
			throw new ObjectRetrievalFailureException(entityClass, id);
		return o;
	}

	/**
	 * 获取全部对象
	 */
	public <T> List<T> getAll(Class<T> entityClass) {
		return getSqlMapClientTemplate().queryForList(entityClass.getName() + POSTFIX_SELECT, null);
	}

	/**
	 * 新增对象
	 */
	public void insert(Object o) {
		getSqlMapClientTemplate().insert(o.getClass().getName() + POSTFIX_INSERT, o);
	}

	/**
	 * 保存对象
	 */
	public void update(Object o) {
		getSqlMapClientTemplate().update(o.getClass().getName() + POSTFIX_UPDATE, o);
	}

	/**
	 * 删除对象
	 */
	public void remove(Object o) {
		getSqlMapClientTemplate().delete(o.getClass().getName() + POSTFIX_DELETE, o);
	}

	/**
	 * 根据ID删除对象
	 */
	public <T> void removeById(Class<T> entityClass, Serializable id) {
		getSqlMapClientTemplate().delete(entityClass.getName() + POSTFIX_DELETE_PRIAMARYKEY, id);
	}

	/**
	 * map查询.
	 *
	 * @param map 包含各种属性的查询
	 */
	public <T> List<T> find(Class<T> entityClass, Map map) {
		if (map == null)
			return this.getSqlMapClientTemplate().queryForList(entityClass.getName() + POSTFIX_SELECT, null);
		else {
			map.put("findBy", "True");
			return this.getSqlMapClientTemplate().queryForList(entityClass.getName() + POSTFIX_SELECTMAP, map);
		}
	}

	/**
	 * sql 查询.
	 *
	 * @param sql 直接sql的语句(需要防止注入式攻击)
	 */
	public <T> List<T> find(Class<T> entityClass, String sql) {
		Assert.hasText(sql);
		if (StringUtils.isEmpty(sql))
			return this.getSqlMapClientTemplate().queryForList(entityClass.getName() + POSTFIX_SELECT, null);
		else
			return this.getSqlMapClientTemplate().queryForList(entityClass.getName() + POSTFIX_SELECTSQL, sql);
	}

	/**
	 * 根据属性名和属性值查询对象.
	 *
	 * @return 符合条件的对象列表
	 */
	public <T> List<T> findBy(Class<T> entityClass, String name, Object value) {
		Assert.hasText(name);
		Map map = new HashMap();
		map.put(name, value);
		return find(entityClass, map);
	}

	/**
	 * 根据属性名和属性值查询对象.
	 *
	 * @return 符合条件的唯一对象
	 */
	public <T> T findUniqueBy(Class<T> entityClass, String name, Object value) {
		Assert.hasText(name);
		Map map = new HashMap();
		try {
			PropertyUtils.getProperty(entityClass.newInstance(), name);
			map.put(name, value);
			map.put("findUniqueBy", "True");
			return (T) getSqlMapClientTemplate().queryForObject(entityClass.getName() + POSTFIX_SELECTMAP, map);
		} catch (Exception e) {
			logger.error("Error when propertie on entity," + e.getMessage(), e.getCause());
			return null;
		}

	}

	/**
	 * 根据属性名和属性值以Like AnyWhere方式查询对象.
	 */
	public <T> List<T> findByLike(Class<T> entityClass, String name, String value) {
		Assert.hasText(name);
		Map map = new HashMap();
		map.put(name, value);
		map.put("findLikeBy", "True");
		return getSqlMapClientTemplate().queryForList(entityClass.getName() + POSTFIX_SELECTMAP, map);

	}

	/**
	 * 判断对象某些属性的值在数据库中不存在重复
	 *
	 * @param tableName 数据表名字
	 * @param names	 在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password" FIXME how about in different schema?
	 */
	public boolean isNotUnique(Object entity, String tableName, String names) {
		try {
			String primarykey;
			Connection con = getSqlMapClient().getCurrentConnection();
			ResultSet dbMetaData = con.getMetaData().getPrimaryKeys(con.getCatalog(), null, tableName);
			dbMetaData.next();
			if (dbMetaData.getRow() > 0) {
				primarykey = dbMetaData.getString(4);
				if (names.indexOf(primarykey) > -1)
					return false;
			} else {
				return true;
			}

		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return false;
	}

	/**
	 * 分页查询函数，使用PaginatedList.
	 *
	 * @param pageNo 页号,从0开始.
	 */
	public Page pagedQuery(Class entityClass, Object parameterObject, int pageNo, int pageSize) {

		int startIndex = Page.getStartOfPage(pageNo, pageSize);
		Integer totalCount = (Integer) this.getSqlMapClientTemplate().queryForObject(
				entityClass.getName() + POSTFIX_COUNT, null);

		if (totalCount == null)
			return new Page();
		List list;
		int totalPageCount=0;
		
		if (pageSize > 0 && pageNo > 0) {
			totalPageCount=(totalCount/pageSize);
			totalPageCount+=((totalCount%pageSize)>0)?1:0;

		
			list = getSqlMapClientTemplate().queryForList(entityClass.getName() + POSTFIX_SELECT,
					parameterObject, pageNo, pageSize);

			if (totalPageCount >= pageNo) {
				((PaginatedList) list).gotoPage(pageNo);

			} else {
				// FIXME is this logic correct?
				// FIX  list.size() error,use totalPageCount
				((PaginatedList) list).gotoPage(totalPageCount-1);

			}
		} else {
			list = getSqlMapClientTemplate().queryForList(entityClass.getName() + POSTFIX_SELECT, null);
		}
		return new Page(startIndex, totalCount, pageSize, list);
	}

}
