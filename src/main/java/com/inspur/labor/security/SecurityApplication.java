package com.inspur.labor.security;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspur.labor.security.util.*;
import com.inspur.msy.component.greenchannel.utils.GreenChannelGlobalConfig;
import com.inspur.msy.component.greenchannel.web.filter.BaseAuthorizeSpringFilter;
import com.inspur.msy.component.greenchannel.web.utils.AuthTokenCheckUtils;
import com.inspur.msy.component.greenchannel.web.utils.MsySecurityContextHolder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author 耿鹏
 */
@SpringBootApplication
@MapperScan("com.inspur.labor.**.dao")
@ImportResource("classpath:uid-dict.xml")
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
        // 关闭green-channel时间校验
        GreenChannelGlobalConfig.getInstance().setVerifyTimestamp(false);
    }


    /**
     * 配置 测试过滤器过滤器
     *
     * @return
     */
    @Bean
    public BaseAuthorizeSpringFilter authorizeSpringFilter() {
//        return new GreenChannelAuthorizeSpringFilter();
        return new BaseAuthorizeSpringFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Credentials", "true");
                response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT");
                response.setHeader("Access-Control-Max-Age", "3600");
                response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, GREEN-CHANNEL-AUTH, Referer, User-Agent ,X-CSRF-TOKEN");
                if (Constant.OPTIONS.equals(request.getMethod())) {
                    filterChain.doFilter(request, response);
                    return;
                }
                if (null == request) {
                    this.logger.error("请求令牌校验失败！！！");
                    AuthTokenCheckUtils.failureReponse(response);
                } else {
                    String authToken = request.getHeader("GREEN-CHANNEL-AUTH");
                    logger.info("拦截的token:" + authToken);
                    if (authToken == null) {
                        AuthTokenCheckUtils.failureReponse(response);
                    } else {
                        try {
                            JWT jwt = JWTUtil.parseToken(authToken);
                            Map<String, Object> tokenInfo = JsonUtils.toJavaObject(jwt.getPayload().getClaimsJson(), Map.class);
                            MsySecurityContextHolder.setUser(tokenInfo);
                            filterChain.doFilter(request, response);
                        } catch (Exception e) {
                            logger.error("JWT解析错误");
                            if (!AuthTokenCheckUtils.parseAndVerify(request)) {
                                this.logger.error("请求令牌校验失败！！！");
                                AuthTokenCheckUtils.failureReponse(response);
                            } else {
                                filterChain.doFilter(request, response);
                            }
                        } finally {
                            MsySecurityContextHolder.clearUser();
                        }
                    }
                }
            }
        };
    }


    /**
     * 配置 green-channel过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> registerLoginCheckFilter(BaseAuthorizeSpringFilter authorizeSpringFilter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(authorizeSpringFilter);

        registrationBean.addUrlPatterns("/municipal/*", "/admin/*", "/sponsor/*", "/api/*");
        registrationBean.setName("authorizeSpringFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> uacRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new UacHessainRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(20000);
        factory.setConnectTimeout(20000);
        return factory;
    }

    @Bean
    @Autowired
    public ConversionService getConversionService(GlobalFormDateConvert globalDateConvert) {
        ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
        Set<Converter> converters = new HashSet<>();
        converters.add(globalDateConvert);
        factoryBean.setConverters(converters);
        return factoryBean.getObject();
    }

    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        //设置日期格式
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(GlobalJsonDateConvert.INSTANCE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        //设置中文编码格式
        List<MediaType> list = new ArrayList<MediaType>();
        MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
        list.add(mediaType);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);
        return mappingJackson2HttpMessageConverter;
    }

}
