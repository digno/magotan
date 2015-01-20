package com.leadtone.where.service.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.mongodb.morphia.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leadtone.where.MsgConstants;
import com.leadtone.where.WhereBiz;
import com.leadtone.where.bean.Activity;
import com.leadtone.where.bean.ActivityUser;
import com.leadtone.where.bean.Geometry;
import com.leadtone.where.bean.Location;
import com.leadtone.where.dao.ActivityDao;
import com.leadtone.where.dao.LocationDao;
import com.leadtone.where.notify.NotificationSender;
import com.leadtone.where.protocol.beans.Content;
import com.leadtone.where.protocol.converter.ResponseContentHelper;
import com.leadtone.where.utils.DateProvider;


@Service
public class GeoServiceImpl {
	
	private Logger log = Logger.getLogger(GeoServiceImpl.class);

	private DateProvider dateProvider = DateProvider.DEFAULT;
	
	@Autowired
	private LocationDao locDao;
	
	@Autowired
	private ActivityDao activityDao;
	
	private String loc_type = "user_loc_notify";
	
	@WhereBiz("report")
	public Content geoReport(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		try {
			String aid = (String) contentMap.get("aid");
			String mobile = (String) contentMap.get("mobile");
			String address = (String) contentMap.get("address");
			Location saveLocation = new Location();
			saveLocation.setAddress(address);
			saveLocation.setAid(aid);
			saveLocation.setMobile(mobile);
			saveLocation.setUtime(dateProvider.getDate());
			if (saveLocation.getAid() !=null &&  saveLocation.getMobile() != null ){
				Geometry geometry = new Geometry();
				BeanUtils.populate(geometry, (Map) contentMap.get("loc"));
				if (geometry.getCoordinates()==null || geometry.getCoordinates().size()!=2){
					resultContent = ResponseContentHelper
							.genSimpleResponseContentWithoutType(
									MsgConstants.ERROR_CODE_5,
									"report GEOInfo failed, require [<longitude> , <latitude>]");
					return resultContent;
				}
				saveLocation.setLoc(geometry);
				Key<Location> result = locDao.save(saveLocation);
				if (result.getId()!=null){
				log.info("Key :" +result.getId()+ " aid :" +aid + " member :" + mobile + " loc :" + geometry.toString());
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_0,
								"aid " +aid + " member " + mobile + " report GEOInfo  successed!");
				Activity a = activityDao.findActiviytByAid(aid);	
				if(a!=null){
					NotificationSender.sendNotify(MsgConstants.ACTIVITY,getMembersMobile(a), genSimpleNotificationContent(loc_type,contentMap));
				}
				}
			} else {
				resultContent = ResponseContentHelper
						.genSimpleResponseContentWithoutType(
								MsgConstants.ERROR_CODE_4,
								"report GEOInfo failed, mobile and aid.");
			}
			
			
		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("geoReport Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}
	
	private Content genSimpleNotificationContent(String loc_type,
			HashMap<String,Object> contentMap) {
		Content resultContent = new Content();
		resultContent.setType(loc_type);
		resultContent.setData(contentMap);
		return resultContent;
	}

	// 1： 找到此aid 下有多少用户
	// 2：依次查询此用户的GEO
	// 3: 组合成一组并下发
	@WhereBiz("get_members_geo")
	public Content getMembersGeoInfo(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Location> locs = new ArrayList<Location>();
			String aid = (String) contentMap.get("aid");
			Activity activity = activityDao.findActiviytByAid(aid);
			if (activity!=null){
				List<ActivityUser> members = activity.getMembers();
				for (ActivityUser m : members){
					Location l = locDao.getMemberLocationByAid(aid, m.getMobile());
					if (l!=null){
					locs.add(l);
					}
				}
			}
			resultMap.put(MsgConstants.RESULT, MsgConstants.ERROR_CODE_0);
			resultMap.put(MsgConstants.LOCS, locs);
			resultMap.put("aid", activity.getAid());
			resultMap.put("title", activity.getTitle());
			resultContent.setData(resultMap);
		} catch (Exception e) {
			resultContent = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
			log.error("geoReport Error : " + e.getMessage());
			log.error(e);
		}
		return resultContent;
	}
	
private  List<String> getMembersMobile(Activity a){
		
		List<String> to =  new ArrayList<String>();
		for (ActivityUser m : a.getMembers()){
			to.add(m.getMobile());
		}
		return to;
	} 

}
