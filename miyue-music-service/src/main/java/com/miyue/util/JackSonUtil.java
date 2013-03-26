package com.miyue.util;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @author spring.tu
 * to use jacksonutil to get the best performences
 */

public class JackSonUtil {
	private static Logger LOG = Logger.getLogger(JackSonUtil.class);

	public static Object json2Bean(String json, Class cls) {
		if(StringUtils.isEmpty(json)){
			return null;
		}
		
		try {
			JSONObject o = JSONObject.fromObject(json);
			if(o != null){
				return JSONObject.toBean(o, cls); 
			}
		} catch (Exception e) {
			LOG.error("JackSonUtil json2Object error!" + e.getMessage());
		}
        return  null;
    }
	public static Object json2ComplexBean(String json, Class cls,Map<String, Class>classMap) {
		if(StringUtils.isEmpty(json)){
			return null;
		}
		try {
			JSONObject o = JSONObject.fromObject(json);
			if(o != null){
				return JSONObject.toBean(o, cls,classMap); 
			}
		} catch (Exception e) {
			LOG.error("JackSonUtil json2Object error!" + e.getMessage());
		}
        return  null;
    }
	public static List json2Array(String json, Class cls) {
		if(StringUtils.isEmpty(json)){
			return null;
		}
		try {
			JSONArray o = JSONArray.fromObject(json); 
			if(o != null){
				return (List)JSONArray.toCollection(o, cls);
			}
		} catch (Exception e) {
			LOG.error("JackSonUtil json2List error!" + e.getMessage());
		}
        return  null;
    } 
	
	public static String array2Json(Object object) {   
		if(object == null){
			return null;
		}
		try {
			JSONArray jsonArray = JSONArray.fromObject(object); 
			return jsonArray.toString();  
		} catch (Exception e) {
			LOG.error("JackSonUtil array2Json error!" + e.getMessage());
		}
        return  null;
    }
	
	public static String bean2Json(Object object) { 
		if(object == null){
			return null;
		}
		
		 try {
			 JSONObject jsonObject = JSONObject.fromObject(object);
			 return jsonObject.toString();  
		 } catch (Exception e) {
			LOG.error("JackSonUtil bean2Json error!" + e.getMessage());
		}
	    return  null;
	}
	public static void main(String[] args) {
//		UpStreamSubjectData upStreamSubjectData = new UpStreamSubjectData();
//		List<GameUpDownUnit> gameUpDownUnits = new ArrayList<GameUpDownUnit>();
//		GameUpDownUnit gameUpDownUnit = new GameUpDownUnit();
//		gameUpDownUnit.setDislikes(1);
//		gameUpDownUnit.setLikes(1);
//		gameUpDownUnit.setItemid(1);
//		gameUpDownUnits.add(gameUpDownUnit);
//		upStreamSubjectData.setProVersion(1);
//		upStreamSubjectData.setReqSegment(1);
//		upStreamSubjectData.setSegmentVer(1);
//		upStreamSubjectData.setLikeDisLikeUpload(gameUpDownUnits);
//		String json = JackSonUtil.bean2Json(upStreamSubjectData);
//		Map<String, Class> classMap = new HashMap<String, Class>();
//		classMap.put("likeDisLikeUpload", GameUpDownUnit.class);
//		UpStreamSubjectData streamSubjectData = (UpStreamSubjectData) JackSonUtil.json2ComplexBean(json, UpStreamSubjectData.class,classMap);
//		List<GameUpDownUnit> likeDisLikeUpload = streamSubjectData.getLikeDisLikeUpload();
//		System.out.println(streamSubjectData.getProVersion());
		/**
		 * {"segmentVer":0,"likeDisLikeUpload":[{"dislikes":1,"likes":1,"itemid":1}],"reqSegment":0,"proVersion":1000}
		 */
		Set<String>test = new HashSet<String>();
		test.add("aa");
		test.add("bb");
		test.add("cc");
		String json = array2Json(test);
		System.out.println(json2Array(json, String.class));
	}
}
