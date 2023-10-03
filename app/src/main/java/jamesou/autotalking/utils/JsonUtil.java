package jamesou.autotalking.utils;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.Iterator;
    import java.util.List;
    import java.util.Map;
    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;
/**
 * Created by ouyangjian on 2016-04-20.
 */
    public class JsonUtil {

        /**
         * 获取String数组数据
         * @param key
         * @param jsonString
         * @return
         */
        public static List<String> getList(String key,String jsonString){
            List<String> list = new ArrayList<String>();
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                for(int i=0;i<jsonArray.length();i++){
                    String msg = jsonArray.getString(i);
                    list.add(msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }
        /**
         * 获取对象的Map集合数据
         * @param key
         * @param jsonString
         * @return
         */
        public static List<Map<String,Object>> getListMap(String key,String jsonString){
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    Map<String,Object> map = new HashMap<String, Object>();
                    Iterator<String> iterator = jsonObject2.keys();
                    while(iterator.hasNext()){
                        String json_key = iterator.next();
                        Object json_value = jsonObject2.get(json_key);
                        if(json_value==null){
                            json_value = "";
                        }
                        map.put(json_key, json_value);
                    }
                    list.add(map);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return list;
        }

        public static Integer getInt(String jsonData, String key, Integer defaultValue) {
            if (StringUtil.isEmpty(jsonData)) {
                return defaultValue;
            }
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                return getInt(jsonObject, key, defaultValue);
            } catch (JSONException e) {
                    e.printStackTrace();
                return defaultValue;
            }
        }
    /**
     * get Int from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     *         <li>if jsonObject is null, return defaultValue</li>
     *         <li>if key is null or empty, return defaultValue</li>
     *         <li>if {@link JSONObject#getInt(String)} exception, return defaultValue</li>
     *         <li>return {@link JSONObject#getInt(String)}</li>
     *         </ul>
     */
    public static Integer getInt(JSONObject jsonObject, String key, Integer defaultValue) {
        if (jsonObject == null || StringUtil.isEmpty(key)) {
            return defaultValue;
        }
        try {
            return jsonObject.getInt(key);
        } catch (JSONException e) {
                e.printStackTrace();
            return defaultValue;
        }
    }

    public static String getString(String jsonData, String key, String defaultValue) {
        if (StringUtil.isEmpty(jsonData)) {
            return defaultValue;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return getString(jsonObject, key, defaultValue);
        } catch (JSONException e) {
                e.printStackTrace();
            return defaultValue;
        }
    }
    /**
     * get String from jsonObject
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return <ul>
     *         <li>if jsonObject is null, return defaultValue</li>
     *         <li>if key is null or empty, return defaultValue</li>
     *         <li>if {@link JSONObject#getString(String)} exception, return defaultValue</li>
     *         <li>return {@link JSONObject#getString(String)}</li>
     *         </ul>
     */
    public static String getString(JSONObject jsonObject, String key, String defaultValue) {
        if (jsonObject == null || StringUtil.isEmpty(key)) {
            return defaultValue;
        }

        try {
            return jsonObject.getString(key);
        } catch (JSONException e) {
                e.printStackTrace();
            return defaultValue;
        }
    }
    }

