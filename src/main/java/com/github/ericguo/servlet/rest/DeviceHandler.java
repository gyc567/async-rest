package com.github.ericguo.servlet.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class DeviceHandler extends Handler {
    @Override
    public void get(String[] args) throws Exception {
        Map ret = new HashMap();
        List result = new ArrayList<>();//Db.query("select id, name, description from devices");

        for (Object record : result) {
            Object id = ((Map) record).get("id");
            Map device;
            if (!ret.containsKey(id)) {
                device = new HashMap();
                ret.put(id, device);
            } else {
                device = (Map) ret.get(id);
            }
            device.put("name", ((Map) record).get("name"));
            device.put("description", ((Map) record).get("description"));
        }
        writeJsonObject(ret);
    }
}