package com.aoz.aozmeeting.config;


import com.aoz.aozmeeting.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * <p>Project:  - WebMvcConfig 用于静态资源映射
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 扩展mvc消息转换器
     * <p>由于原本的自带消息转换器不能够处理好前端js数据丢失的问题，例如19位的id会被转换成18位，所以需要自定义消息转换器，这个转换器在common的JacksonConfig中已经定义好了，
     * 可以将long类型的数据转换成String类型的数据传到前端，这样前端就不会丢失数据了。
     *
     * @param converters 消息转换器列表
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // 设置对象转换器，底层使用Jackson将对象转换成json字符串
        converter.setObjectMapper(new JacksonObjectMapper());
        // 将自定义的消息转换器添加到消息转换器列表中
        converters.add(0, converter); // 0表示将自定义的消息转换器放在第一个位置

        log.info("自定义消息转换器已经添加到消息转换器列表中");
    }
}
