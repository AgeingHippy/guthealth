package com.ageinghippy.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void valueIfNull() {
        assertEquals(Util.valueIfNull(1,2),1);
        assertEquals(Util.valueIfNull(null,2),2);

        assertEquals(Util.valueIfNull("dog","cat"),"dog");
        assertEquals(Util.valueIfNull(null,"cat"),"cat");
    }

}