package com.dreamwin.download.db;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dreamwin.cclib.db.sober.Sober;
import com.dreamwin.cclib.db.sober.SoberException;
import com.dreamwin.cclib.druid.DruidException;
import com.dreamwin.cclib.druid.DruidManager;

public class SoberFactory {

	private static final Logger LOGGER = LogManager.getLogger(SoberFactory.class);

	private static Map<String, Sober> pool = new HashMap<String, Sober>();

	public static boolean initialize(String ormPackagePath, String configName, boolean showSql, DB... dbs) {
		try {
			DruidManager.init(configName);

			for (DB db : dbs) {
				Sober sober = new Sober(ormPackagePath, DruidManager.getProvider(db.getAlias()));
				sober.setShowSql(showSql);

				pool.put(db.getAlias(), sober);
			}

			return true;
		} catch (DruidException e) {
			LOGGER.error("", e);
		} catch (SoberException e) {
			LOGGER.error("", e);
		}

		return false;
	}

	public static Sober getSober(DB db) {
		if (db == null || !pool.containsKey(db.getAlias())) {
			return null;
		}

		return pool.get(db.getAlias());
	}

}
