package com.hunk.stubserver;

import java.util.HashMap;
import java.util.Map;

import com.hunk.stubserver.feature.LoginActionHandler;

public class Dispather {

	private Map<String, ActionHandler> mRouteMap;

	public Dispather() {
		super();
		mRouteMap = new HashMap<String, ActionHandler>();
		
		mRouteMap.put("login", new LoginActionHandler());
	}

	public ActionHandler dispather(String url) {
		return mRouteMap.get(url);
	}

}
