package com.sunmi.doublescreen.doublescreenapp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        String s = "你好1，这是一个345测试提取24数字的测试5，希望645能够6成功10";

        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                StringBuilder sb = new StringBuilder();
                sb.append(s.charAt(i));
                for (int j = i + 1; j < s.length(); j++) {
                    if (Character.isDigit(s.charAt(j))) {
                        ++i;
                        sb.append(s.charAt(j));
                    } else {
                        break;
                    }
                }
                System.out.println(sb.toString());
            }
        }

        assertEquals(4, 2 + 2);
    }
}