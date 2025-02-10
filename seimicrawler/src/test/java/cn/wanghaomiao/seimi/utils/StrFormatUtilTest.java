package cn.wanghaomiao.seimi.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * StrFormatUtil Tester.
 *
 * @author SeimiMaster seimimaster@gmail.com
 * @version 1.0
 */
class StrFormatUtilTest {

    /**
     * Method: parseCharset(String target)
     */
    @Test
    void testParseCharset() throws Exception {
        String t = "text/html; charset=utf-16;text/html; charset=utf-8";
        String r = StrFormatUtil.parseCharset(t);
        System.out.println(r);
        assertEquals("utf-8", r);
    }
}
