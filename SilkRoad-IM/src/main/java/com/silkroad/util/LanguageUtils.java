package com.silkroad.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XJNT-CY on 2014/7/25.
 */
public class LanguageUtils {

    private static final Logger Log = LoggerFactory.getLogger(LanguageUtils.class);

    public LanguageUtils() {
    }

    public String transLanguage(String language, String content) {
        int temp = 0;
        try {

            if (language.equals("we")) {
                // 汉语翻译维语

                String name = java.net.URLEncoder.encode(content, "UTF-8");
                name = java.net.URLEncoder.encode(name, "UTF-8");
                Log.debug("========", name);
                String json = HttpTookit.doGet("http://www.zyun168.com/jeecg/zyunWhzdJsonController.do?datagrid&key="
                        + name);
                Log.debug("========", json);
                String result = java.net.URLDecoder.decode(json, "UTF-8");

                String msgs = content + "\r\nwe-> " + result;

                Log.debug("========", msgs);
                //result = content + "\r\n  we:" + result;

                // TextMessageBody txtBody = new
                // TextMessageBody(result);
                // message.addBody(txtBody);
                content = msgs;
            } else {
                if (language.equals("zh")) {

                    int count = 0;
                    String regEx = "[\\u4e00-\\u9fa5]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(content);
                    while (m.find()) {
                        for (int i = 0; i <= m.groupCount(); i++) {
                            // key_filter=key_filter+m.group(i);
                            count = count + 1;
                        }
                    }

                    if (count == 0) {

                        String name = java.net.URLEncoder
                                .encode(content,
                                        "UTF-8");
                        name = java.net.URLEncoder.encode(name, "UTF-8");
                        String json = HttpTookit
                                .doGet("http://www.zyun168.com/jeecg/zyunWhzdJsonController.do?datagrid&key="
                                        + name);

                        String result = java.net.URLDecoder
                                .decode(json, "UTF-8");

                        if (result.equals("字库查询不到结果")) {
                            // 继续调用百度的接口

                        } else {
                            // 返回维语
                            String msgs = content + "\r\nzh-> " + result;

                            //result = content+ "\r\n  zh:" + result;

                            // TextMessageBody txtBody = new
                            // TextMessageBody(result);
                            // message.addBody(txtBody);

                            content = msgs;

                            temp = 1;
                            // return;
                        }

                    }
                }

                if (temp == 0) {

                    String msgs = java.net.URLEncoder.encode(content, "UTF-8");

                    String json = HttpTookit
                            .doGet("http://openapi.baidu.com/public/2.0/bmt/translate?"
                                    + "client_id=F8stkIn68UgFlyWSKTg1zKi5&q="
                                    + msgs
                                    + "&from=auto&to="
                                    + language);

                    JSONObject jsonObject = new JSONObject(json);
                    String to = jsonObject.getString("to");

                    JSONArray array = jsonObject.getJSONArray("trans_result");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject data = array.getJSONObject(i);
                        String dst = data.getString("dst");
                        content = content + "\r\n" + to + "-> " + dst;

                    }
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }

        return content;
    }

    public String UnicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }
}
