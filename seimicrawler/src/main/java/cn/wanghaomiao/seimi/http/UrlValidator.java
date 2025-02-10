package cn.wanghaomiao.seimi.http;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class UrlValidator {
    private Cache<String, Pattern> patternCache = Caffeine.newBuilder()
        .maximumSize(10)
        .build();

    public boolean isValidUrl(String urlPattern, String url) {
        Pattern pattern = patternCache.get(urlPattern, key -> Pattern.compile(key));
        return pattern.matcher(url).matches();
    }

    public void addUrlPattern(String urlPattern) {
        patternCache.get(urlPattern, key -> Pattern.compile(key));
    }

    public Pattern getUrlPattern(String urlPattern) {
        return patternCache.get(urlPattern, key -> Pattern.compile(key));
    }

    public boolean isMatch(String urlPattern, String url) {
        Pattern pattern = patternCache.get(urlPattern, key -> Pattern.compile(key));
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    public List<String> findAllMatches(String urlPattern, String url) {
        Pattern pattern = patternCache.get(urlPattern, key -> Pattern.compile(key));
        Matcher matcher = pattern.matcher(url);
        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    public String replaceAll(String urlPattern, String url, String replacement) {
        Pattern pattern = patternCache.get(urlPattern, key -> Pattern.compile(key));
        return pattern.matcher(url).replaceAll(replacement);
    }
}
