package com.fieldnation.fnjson;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.fieldnation.fnjson.annotations.Json;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        enum_test();
    }

    private enum TestEnum {
        @Json(name = "ord1")ORD1("ord1"),
        @Json(name = "ord2")ORD2("ord2"),
        @Json(name = "ord3")ORD3("ord3"),
        @Json(name = "ord4")ORD4("ord4"),
        @Json(name = "ord5")ORD5("ord5"),
        @Json(name = "ord6")ORD6("ord6"),
        @Json(name = "ord7")ORD7("ord7"),
        @Json(name = "ord8")ORD8("ord8");

        private String value;

        TestEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private static class EnumTestClass {
        @Json
        public TestEnum te;

        public EnumTestClass() {
        }
    }

    private static final String EnumJson = "{te:ord3}";

    public void enum_test() throws Exception {
        EnumTestClass instance = Unserializer.unserializeObject(EnumTestClass.class, new JsonObject(EnumJson));

        assertEquals(instance.te, TestEnum.ORD3);

        System.out.print(Serializer.serializeObject(instance).display());
    }
}