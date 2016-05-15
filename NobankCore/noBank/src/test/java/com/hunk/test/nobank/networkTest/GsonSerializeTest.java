package com.hunk.test.nobank.networkTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.Core;
import com.hunk.nobank.contract.ContractGson;
import com.hunk.nobank.contract.LoginResp;
import com.hunk.nobank.contract.Money;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.contract.adapter.DateAdapter;
import com.hunk.nobank.contract.type.LoginStateEnum;
import com.hunk.nobank.model.LoginReqPackage;
import com.hunk.test.utils.NetworkHandlerStub;
import com.hunk.test.utils.TestNoBankApplication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class,
        application = TestNoBankApplication.class,
        sdk = 21)
public class GsonSerializeTest {

    private NetworkHandlerStub mNetworkHandlerStub;

    @Before
    public void prepare() {
        // set next response
        mNetworkHandlerStub =
                (NetworkHandlerStub) Core.getInstance().getNetworkHandler();

        mNetworkHandlerStub.clear();
    }

    @Test
    public void testSerializeRealRequest() {
        String expected =
                "{\"Request\":{\"Username\":\"username\",\"Password\":\"userPassword\",\"RememberMe\":false}}";
        LoginReqPackage loginReqPackage = new LoginReqPackage("username", "userPassword", false);

        String jsonReq = mNetworkHandlerStub.getRealRequest(loginReqPackage.getRequest());

        assertEquals(expected, jsonReq);
    }

    @Test
    public void testDeSerializeRealResponse() {
        LoginReqPackage loginReqPackage = new LoginReqPackage("username", "userPassword", false);

        LoginResp resp = new LoginResp();
        resp.AllAccountIds = Arrays.asList("123", "456");
        resp.loginState = LoginStateEnum.NeedVerifySecurityQuestion;
        RealResp<LoginResp> realResp = new RealResp<>();
        realResp.Response = resp;
        String json = mNetworkHandlerStub.getGson().toJson(realResp);

        RealResp<LoginResp> realResp1 = mNetworkHandlerStub.getRealResponse(json, loginReqPackage);

        assertNotNull(realResp1);
        assertNotNull(realResp1.Response);
        assertEquals(LoginStateEnum.NeedVerifySecurityQuestion, realResp1.Response.loginState);
        assertNotNull(realResp1.Response.AllAccountIds);
        assertEquals(2, realResp1.Response.AllAccountIds.size());
        assertEquals("123", realResp1.Response.AllAccountIds.get(0));
        assertEquals("456", realResp1.Response.AllAccountIds.get(1));
    }

    @Test
    public void testSerializeDateClassHappyPath() {
        Date originalDate = new Date();
        String json = ContractGson.getInstance().toJson(originalDate);
        Date genDate = ContractGson.getInstance().fromJson(json, Date.class);
        assertEquals(originalDate.getTime(), genDate.getTime());
    }

    @Test
    public void testSerializeDateClassUnhappyPath() {
        String json = ContractGson.getInstance().toJson(null, new TypeToken<Date>(){}.getType());
        assertEquals("null", json);
        Date genDate = ContractGson.getInstance().fromJson(json, Date.class);
        assertTrue(genDate == null);
    }

    @Test
    public void testSerializeDateClassPerformance() {
        // warm up start
        // If we don't setup this code in here, then the test result won't be accurate.
        // who run first will have a delay time, it impacts the execution time result.
        Gson oldGson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new OldDateAdapter())
                .create();

        Gson newGson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateAdapter())
                .create();
				
        for (int i = 0; i < Integer.MAX_VALUE / 1000; i++) {
            String json = oldGson.toJson(new Date());
            oldGson.fromJson(json, Date.class);
        }

        for (int i = 0; i < Integer.MAX_VALUE / 1000; i++) {
            String json = newGson.toJson(new Date());
            newGson.fromJson(json, Date.class);
        }
        // warm up end

        // start testing

        long newExecutionTime = System.currentTimeMillis();
        for (int i = 0; i < Integer.MAX_VALUE / 1000; i++) {
            String json = newGson.toJson(new Date());
            newGson.fromJson(json, Date.class);
        }
        newExecutionTime = System.currentTimeMillis() - newExecutionTime;

        long oldExecutionTime = System.currentTimeMillis();
        for (int i = 0; i < Integer.MAX_VALUE / 1000; i++) {
            String json = oldGson.toJson(new Date());
            oldGson.fromJson(json, Date.class);
        }
        oldExecutionTime = System.currentTimeMillis() - oldExecutionTime;
        // compare result
        System.out.println("oldExecutionTime=" + oldExecutionTime +
                ", newExecutionTime=" + newExecutionTime);
        assertTrue(oldExecutionTime > newExecutionTime);
    }

    private static class OldDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
        public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(dateFormat.format(src));
        }

        @Override
        public Date deserialize(
                JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String jsonStr = json.getAsString();
            try {
                if (jsonStr.length() != 0) {
                    return dateFormat.parse(jsonStr);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Test
    public void testSerializeMoneyClassHappyPath() {
        Money originalMoney = new Money("50");
        assertEquals("50.00", originalMoney.string());

        String expectedJson = "\"50.00\"";
        String json = ContractGson.getInstance().toJson(originalMoney);
        assertEquals(expectedJson, json);

        Money genMoney = ContractGson.getInstance().fromJson(json, Money.class);
        assertEquals(0, originalMoney.getValue().compareTo(genMoney.getValue()));
        assertEquals("50.00", genMoney.string());
    }


    @Test
    public void testSerializeMoneyClassUnhappyPath() {
        String json = ContractGson.getInstance().toJson(null, new TypeToken<Money>(){}.getType());
        assertEquals("null", json);
        Money genMoney = ContractGson.getInstance().fromJson(json, Money.class);
        assertTrue(genMoney == null);
    }

    @Test
    public void testSerializeLoginStateEnumHappyPath() {
        LoginResp loginResp = new LoginResp();
        loginResp.loginState = LoginStateEnum.Logined;
        String json = ContractGson.getInstance().toJson(loginResp);
        assertEquals("{\"loginState\":\"Logined\"}", json);

        LoginResp deSerializeObj = ContractGson.getInstance().fromJson(json, LoginResp.class);
        assertEquals(LoginStateEnum.Logined, deSerializeObj.loginState);
    }
}
