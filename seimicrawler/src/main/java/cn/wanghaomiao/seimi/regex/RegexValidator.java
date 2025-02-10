package cn.wanghaomiao.seimi.regex;

import java.util.ArrayList;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidator {
    private Cache<String, Pattern> patternCache = Caffeine.newBuilder()
        .maximumSize(10)
        .build();

    public boolean isValid(String regex, String input) {
        Pattern pattern = patternCache.get(regex, key -> Pattern.compile(key));
        return pattern.matcher(input).matches();
    }

    public boolean isMatch(String regex, String input) {
        Pattern pattern = patternCache.get(regex, key -> Pattern.compile(key));
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    public List<String> findAllMatches(String regex, String input) {
        Pattern pattern = patternCache.get(regex, key -> Pattern.compile(key));
        Matcher matcher = pattern.matcher(input);
        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    public String replaceAll(String regex, String input, String replacement) {
        Pattern pattern = patternCache.get(regex, key -> Pattern.compile(key));
        return pattern.matcher(input).replaceAll(replacement);
    }

    public String[] split(String regex, String input) {
        Pattern pattern = patternCache.get(regex, key -> Pattern.compile(key));
        return pattern.split(input);
    }
}
