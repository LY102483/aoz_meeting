package com.aoz.aozmeeting.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author LiuYang
 * @version 1.0
 * @since JDK8
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")  // "/*"表示拦截所有请求
@Slf4j
public class LoginCheckFilter implements Filter {

    // 路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // 将ServletRequest和ServletResponse转换成HttpServletRequest和HttpServletResponse
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取session
        HttpSession session   =   request.getSession();

        //设置即使被拦截状态码也依然为200
        response.setStatus(200);

        Object userId = session.getAttribute("type");

        // 1.获取本次处理的url
        String requestURI = request.getRequestURI();

        // 2.公共接口(不需要处理)
        String[] publicApi = new String[]{
                "/api/v1/user/save",    //用户注册
                "/api/v1/user/login",
                "/api/v1/user/quickLogin",
                "/api/v1/user/logout",
                "/api/v1/department/getAllDepartment",   //获取部门列表(注册时需要用户选定部门)
                "/api/v1/department/updateAllDepartment"
        };

        //管理员专属接口
        String[] adminApi=new String[]{
                "/api/v1/**/admin/**"
        };

        // 判断本次请求是否需要处理
        boolean needProcess = check(publicApi, requestURI);

        // 3.如果不需要处理，则直接放行
        if (needProcess) {
            log.info("本次请求{}不需要处理，直接放行！", requestURI);
            filterChain.doFilter(request, response);    // 直接放行
            return;                                     // 结束方法
        }
        //session中userId不为空
        if(request.getSession().getAttribute("userId") != null){
            boolean isAdmin=check(adminApi,requestURI);
            // 检查是否为管理员专属接口
            if (isAdmin) {
                if(request.getSession().getAttribute("type")!=null){
                    filterChain.doFilter(request, response);    // 放行
                    log.info("本次请求为管理员专属请求{},用户id={},用户类型{},已登录，直接放行！", requestURI, request.getSession().getAttribute("userId"),request.getSession().getAttribute("type"));
                    return;
                }else{
                    log.info("本次请求为管理员专属请求{},用户id={}为普通用户无权限，已拦截！", requestURI, request.getSession().getAttribute("userId"));
                    return;
                }

            }
            //普通用户
            if(!isAdmin){
                filterChain.doFilter(request, response);    // 放行
                log.info("本次请求{},用户id={},用户类型{},已登录，直接放行", requestURI, request.getSession().getAttribute("userId"),request.getSession().getAttribute("type"));
                return;
            }
        }


        // 5.如果未登录，则返回未登录结果
        // 即只要通过输出流,包装通用返回结果类，返回未登录结果即可
        log.info("本次请求{}，用户未登录，已拦截！", requestURI);
    }


    /**
     * 路径匹配<br>
     * 判断本次请求是否需要处理。
     *
     * @param urls       不需要处理的url
     * @param requestURI 本次请求的url
     * @return true：本次请求的url匹配其中一个不需要处理的urls，不处理；false：否则需要处理
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            if (PATH_MATCHER.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }

}
