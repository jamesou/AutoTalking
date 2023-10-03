package jamesou.autotalking.biz;

import android.util.Log;

import java.util.List;
import java.util.Map;

import jamesou.autotalking.utils.Config;
import jamesou.autotalking.utils.HttpRequest;
import jamesou.autotalking.utils.JsonUtil;

/**
 * Created by ouyangjian on 2016-04-20.
 */
public class MessageWrapper {
    private static String  ROBOT_KEY =  Config.ROBOT_KEY1;
    public static String getFeedBackText(String chatText)
    {
        String feedBackText  = HttpRequest.sendPost(Config.ROBOT_URL, "key=" + ROBOT_KEY + "&info=" + chatText);
        int code  = JsonUtil.getInt(feedBackText, "code", 0);
        if(code!=0)
        {
            String text =  JsonUtil.getString(feedBackText, "text", Config.ROBOT_DEFAULT_FEEDBACK);
            switch (code) {
                case Config.CODE_CONTENT:
                    feedBackText = text;
                    break;
                case Config.CODE_LINKS:
                    feedBackText = text+":"+ JsonUtil.getString(feedBackText, "url", Config.ROBOT_DEFAULT_FEEDBACK);
                    break;
                case Config.CODE_NEWS:
                    List<Map<String,Object>> newsList =  JsonUtil.getListMap("list",feedBackText);
                    feedBackText = text+":";
                    for(Map news:newsList)
                        feedBackText+=news.get("article")+(String)news.get("detailurl")+"\n";
                    break;
                case Config.CODE_TRAIN:
                    List<Map<String,Object>> trainList =  JsonUtil.getListMap("list",feedBackText);
                    feedBackText = text+":";
                    for(Map train:trainList)
                        feedBackText+="车次:"+(String)train.get("trainnum")+",出发:"+train.get("start")+",到达:"+train.get("terminal")+",开车时间:"+train.get("starttime")+",到达时间:"+train.get("endtime")+"\n";
                    break;
                case Config.CODE_FOOD:
                    List<Map<String,Object>> foodList =  JsonUtil.getListMap("list",feedBackText);
                    feedBackText = text+",选取了经典的三款做法:"+"\n";
                    int count=1;
                    for(Map food:foodList) {
                        if(count<=3)
                        feedBackText += "第"+count+"种,材料：" + food.get("info") + "," + food.get("detailurl")+"\n";
                        else
                        break;
                        count++;
                    }
                    break;
                case Config.CODE_SONGS:
                    List<Map<String,Object>> songList =  JsonUtil.getListMap("function",feedBackText);
                    feedBackText = text+":";
                    for(Map song:songList)
                        feedBackText+=song.get("song")+"《"+(String)song.get("singer")+"》;";
                    break;
                case Config.CODE_POEMS:
                    List<Map<String,Object>> poemsList =  JsonUtil.getListMap("function",feedBackText);
                    feedBackText = text+":";
                    for(Map poem:poemsList)
                        feedBackText+=poem.get("author")+"《"+(String)poem.get("name")+"》;";
                    break;
                case Config.CODE_EXCEPTION1:
                    feedBackText = text;
                    break;
                case Config.CODE_EXCEPTION2:
                    feedBackText = text;
                    break;
                case Config.CODE_EXCEPTION3:
                    ROBOT_KEY = Config.ROBOT_KEY2;//设置新的KEY
                    feedBackText = text+",可以尝试重试一下！^_^";
                    break;
                case Config.CODE_EXCEPTION4:
                    feedBackText = text;
                    break;
            }
        }
            else
        feedBackText = "获取数据失败,请检查网络后重试！[呲牙]";
        return feedBackText;
    }

}
