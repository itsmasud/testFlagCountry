package com.fieldnation.fnjson;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.fieldnation.fnjson.annotations.Json;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class EnumTest {


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

    @Test
    public void enum_test() throws Exception {
        EnumTestClass instance = Unserializer.unserializeObject(EnumTestClass.class, new JsonObject(EnumJson));

        assertThat(instance.te, is(equalTo(TestEnum.ORD3)));
        System.out.print(Serializer.serializeObject(instance).display());
    }

    @Test
    public void displayTest() {
        try {
            JsonObject obj = new JsonObject();
            obj.put("this.is.a.test", "works!");
            System.out.println(obj.display());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}