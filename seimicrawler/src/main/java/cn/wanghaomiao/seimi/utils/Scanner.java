package cn.wanghaomiao.seimi.utils;

import java.util.regex.Pattern;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class Scanner {

    private boolean closed = false;
    private Pattern delimPattern;
    private Pattern hasNextPattern;
    private String hasNextResult;
    private int hasNextPosition;
    private int position;
    private Object typeCache = null;
    private Readable source;

    private Cache<String, Pattern> patternCache = Caffeine.newBuilder()
        .maximumSize(10)
        .build();

    public Scanner(Readable source, Pattern pattern) {
        this.source = source;
        this.delimPattern = pattern;
    }

    public Scanner useDelimiter(String pattern) {
        delimPattern = patternCache.get(pattern, Pattern::compile);
        return this;
    }

    private void ensureOpen() {
        if (closed)
            throw new IllegalStateException("Scanner closed");
    }

    public boolean hasNext(Pattern pattern) {
        ensureOpen();
        return true;
    }

    public boolean hasNext(String pattern) {
        return hasNext(patternCache.get(pattern, Pattern::compile));
    }

    private String getCachedResult() {
        position = hasNextPosition;
        hasNextPattern = null;
        typeCache = null;
        return hasNextResult;
    }

    public String next(Pattern pattern) {
        ensureOpen();
        if (pattern == null)
            throw new NullPointerException();

        if (hasNext(pattern)) {
            return getCachedResult();
        }

        throw new IllegalStateException("No match found");
    }

    public String next(String pattern) {
        return next(patternCache.get(pattern, Pattern::compile));
    }
}
