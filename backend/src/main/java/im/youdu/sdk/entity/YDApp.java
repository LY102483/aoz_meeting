package im.youdu.sdk.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Component
@PropertySource("classpath:application.yml")//读取application.yml文件
public class YDApp {
    @Value("${youdu.buin}")
    public int buin;

    @Value("${youdu.host}")
    public    String host;

    @Value("${youdu.pId}")
    public String appId;
    private String appName;
    private String token;

    @Value("${youdu.appAesKey}")
    public  String appAesKey;

    @Override
    public String toString() {
        return "YDApp{" +
                "buin=" + buin +
                ", host='" + host + '\'' +
                ", appId='" + appId + '\'' +
                ", appName='" + appName + '\'' +
                ", token='" + token + '\'' +
                ", appAesKey='" + appAesKey + '\'' +
                '}';
    }

    public int getBuin() {
        return buin;
    }

    public void setBuin(int buin) {
        this.buin = buin;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppAesKey() {
        return appAesKey;
    }

    public void setAppAesKey(String appAesKey) {
        this.appAesKey = appAesKey;
    }

}
