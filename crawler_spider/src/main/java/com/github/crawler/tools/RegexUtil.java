package com.github.crawler.tools;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    public static MatchResult search(String regex, String content) throws Exception {
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(content);
        if(matcher.find()) return matcher.toMatchResult();
        throw new Exception(String.format("matcher failed, regex=%s,content=%s",regex,
                content.length() > 1000 ? content.substring(0,999) + "...": content));

    }
}
