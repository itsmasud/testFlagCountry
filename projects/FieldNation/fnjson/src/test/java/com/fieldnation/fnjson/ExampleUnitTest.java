package com.fieldnation.fnjson;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        JsonObject obj = new JsonObject();
        obj.put("this.is.a.test", "works!");
        System.out.println(obj.display());
        assertEquals(4, 2 + 2);
    }
}