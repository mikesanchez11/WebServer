package com.michaelsanchez.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilsTest {

    @Test
    public void isEmpty_nullCase() {
        assertTrue(StringUtils.isEmpty(null));
    }

    @Test
    public void isEmpty_zeroLengthCase() {
        assertTrue(StringUtils.isEmpty(""));
    }

    @Test
    public void isEmpty_trimWhiteSpaceCase() {
        String[] trimMeAndTest = new String[]{
                "  dogs", "  dogs   ", "dogs  "
        };

        for (int i = 0; i < trimMeAndTest.length; i++) {
            String str = trimMeAndTest[i];
            assertFalse(StringUtils.isEmpty(str));
        }

        assertTrue(StringUtils.isEmpty("\t"));
        assertTrue(StringUtils.isEmpty("\n"));
    }

    @Test
    public void ucFirst_lowerCase() {
        assertEquals("AAA", StringUtils.ucFirst("aAA"));
        assertEquals("A", StringUtils.ucFirst("a"));
    }
}