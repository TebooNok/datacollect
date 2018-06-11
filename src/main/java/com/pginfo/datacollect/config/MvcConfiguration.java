package com.pginfo.datacollect.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    Logger logger = LoggerFactory.getLogger(MvcConfiguration.class);

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        Map<String, String> viewControlMap = viewControlMap();
        logger.debug("Set view controllers");

        for (Map.Entry entry : viewControlMap.entrySet()) {
            registry.addViewController(entry.getKey().toString()).setViewName(entry.getValue().toString());
        }
    }

    static Map<String, String> viewControlMap() {
        Map<String, String> viewControlMap = new HashMap<>();

        viewControlMap.put("/404", "404");
        viewControlMap.put("/404.html", "404");

        viewControlMap.put("/device_setup", "device_setup");
        viewControlMap.put("/device_setup.html", "device_setup");

        viewControlMap.put("/hook", "hook");
        viewControlMap.put("/hook.html", "hook");

        viewControlMap.put("/ifream_together", "ifream_together");
        viewControlMap.put("/ifream_together.html", "ifream_together");

        viewControlMap.put("/images-icons", "images-icons");
        viewControlMap.put("/images-icons.html", "images-icons");

        viewControlMap.put("/index", "index");
        viewControlMap.put("/index.html", "index");
        viewControlMap.put("/", "index");
        viewControlMap.put("/home", "index");

        viewControlMap.put("/login", "login");
        viewControlMap.put("/login.html", "login");

        viewControlMap.put("/police", "police");
        viewControlMap.put("/police.html", "police");

        viewControlMap.put("/police_detail", "police_detail");
        viewControlMap.put("/police_detail.html", "police_detail");

        viewControlMap.put("/police_handle", "police_handle");
        viewControlMap.put("/police_handle.html", "police_handle");

        viewControlMap.put("/police_over", "police_over");
        viewControlMap.put("/police_over.html", "police_over");

        viewControlMap.put("/police_sure", "police_sure");
        viewControlMap.put("/police_sure.html", "police_sure");

        viewControlMap.put("/qiao", "qiao");
        viewControlMap.put("/qiao.html", "qiao");

        viewControlMap.put("/queryData", "queryData");
        viewControlMap.put("/queryData.html", "queryData");

        viewControlMap.put("/Search", "Search");
        viewControlMap.put("/Search.html", "Search");

        viewControlMap.put("/Search_dot", "Search_dot");
        viewControlMap.put("/Search_dot.html", "Search_dot");

        viewControlMap.put("/Search_paragraph", "Search_paragraph");
        viewControlMap.put("/Search_paragraph.html", "Search_paragraph");

        viewControlMap.put("/searchDuan", "searchDuan");
        viewControlMap.put("/searchDuan.html", "searchDuan");

        viewControlMap.put("/searchPoint", "searchPoint");
        viewControlMap.put("/searchPoint.html", "searchPoint");

        viewControlMap.put("/set", "set");
        viewControlMap.put("/set.html", "set");

        viewControlMap.put("/setPermission", "setPermission");
        viewControlMap.put("/setPermission.html", "setPermission");

        viewControlMap.put("/tables", "tables");
        viewControlMap.put("/tables.html", "tables");

        viewControlMap.put("/tesrr", "tesrr");
        viewControlMap.put("/tesrr.html", "tesrr");

        viewControlMap.put("/text", "text");
        viewControlMap.put("/text.html", "text");

        viewControlMap.put("/threshold-value", "threshold-value");
        viewControlMap.put("/threshold-value.html", "threshold-value");

        viewControlMap.put("/device_mediate", "device_mediate");
        viewControlMap.put("/device_mediate.html", "device_mediate");

        viewControlMap.put("/device_sensor", "device_sensor");
        viewControlMap.put("/device_sensor.html", "device_sensor");

        return viewControlMap;
    }
}
