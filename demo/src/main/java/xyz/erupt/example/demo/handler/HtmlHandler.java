package xyz.erupt.example.demo.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liyuepeng on 2019-10-18.
 */
public class HtmlHandler implements xyz.erupt.annotation.fun.HtmlHandler {
    @Override
    public Map<String, Object> getData(String[] params) {
        Map<String, Object> map = new HashMap<>();
        map.put("number", 123);
        map.put("str", "xxxxxx");
        map.put("arr", new String[]{
                "xxxx", "xxxx2", "xxxx3"
        });
        return map;
    }
}
