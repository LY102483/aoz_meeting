package im.youdu.sdk.client;

import com.aoz.aozmeeting.POJO.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import im.youdu.sdk.entity.YdApi;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import im.youdu.sdk.exception.ServiceException;
import im.youdu.sdk.util.Helper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

import im.youdu.sdk.entity.Const;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.yml")//读取application.yml文件
public class IdentifyClient {
    @Value("${youdu.host}")
    public String host;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


    @Override
    public String toString() {
        return "IdentifyClient{" +
                "host='" + host + '\'' +
                '}';
    }

    public User idetify(String ydToken) throws HttpRequestException, ParamParserException, ServiceException {
        String url = this.uriIdentify(ydToken);
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpRsp = null;
        try {
            httpRsp = httpClient.execute(httpGet);
            HttpEntity entity = httpRsp.getEntity();
            entity.getContent();
            JsonObject getResult = Helper.readHttpResponse(httpRsp);
            JsonElement status = getResult.get("status");
            if(!status.isJsonObject()){
                throw new ParamParserException(Const.ErrMsg_MissSection_Status, null);
            }
            JsonObject statusObj = status.getAsJsonObject();
            int code = statusObj.get("code").getAsInt();
            this.parseStatusCode(code, statusObj.get("message").getAsString());

            JsonElement jUserElememt = getResult.get("userInfo");
            if(!jUserElememt.isJsonObject()){
                throw new ParamParserException(Const.ErrMsg_MissSection_UserInfo, null);
            }
            JsonObject jUserObj = jUserElememt.getAsJsonObject();
            User userInfo=new User();
            userInfo.setGid(jUserObj.get(Const.UserInfo_Gid).getAsLong());
            userInfo.setUsername(jUserObj.get(Const.UserInfo_Account).getAsString());
            return userInfo;
        } catch (IOException e) {
            throw new HttpRequestException(0, e.getMessage(), e);
        }finally {
            Helper.close(httpRsp);
            Helper.close(httpClient);
        }
    }

    private void parseStatusCode(int code, String msg) throws ServiceException {
        if(code==0){
            return;
        }
        switch (code){
            case 1:
                throw new ServiceException("查找不到token对应的用户",null);
            case 10:
                throw new ServiceException("错误的token或者token已过期",null);
             default:
                throw new ServiceException("其他错误："+msg,null);
        }
    }

    private String uriIdentify(String token) {
        return String.format("%s%s%s?token=%s", YdApi.SCHEME, this.host, YdApi.API_IDENTIFY,token);
    }
}
