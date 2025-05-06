package com.ageinghippy.controller.rest;

import com.ageinghippy.model.entity.UserPrinciple;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestRestController {

    //    private final Properties properties;
    private final CacheManager cacheManager;

    @GetMapping("/time")
    public String getTest(Authentication authentication) {
        String userName = "Unknown User";
        if (authentication != null) {
            UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
            userName = userPrinciple.getUsername();
        }

        return MessageFormat.format("{0} accessed server on {1}", userName, (new Date()).toString());
    }

    @GetMapping("/cache")
    public List<CacheEntry> getCacheDetails(Authentication authentication) {
        List<CacheEntry> cacheDetail = new ArrayList<>();

        for (String cacheName : cacheManager.getCacheNames()) {
            Map<Object, Object> cache = (Map<Object, Object>) cacheManager.getCache(cacheName).getNativeCache();

            cache.keySet().forEach(key -> {
                cacheDetail.add(new CacheEntry(cacheName, key.toString(), cache.get(key)));
            });
        }

        return cacheDetail;
    }

    record CacheEntry(String name, String key, Object value) implements Serializable {
    }


}
