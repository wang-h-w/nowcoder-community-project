package com.nowcoder.community.config;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.utils.CommunityConstant;
import com.nowcoder.community.utils.CommunityUtil;
import com.nowcoder.community.utils.CookieUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@Configuration
public class SecurityConfig implements CommunityConstant {

    @Autowired
    private UserService userService;

    /**
     * configure(WebSecurity web)
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/resources/**");
    }

    /**
     * configure(HttpSecurity http)：管理授权
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 授权
        http.authorizeHttpRequests()
                .requestMatchers(
                        "/user/setting",
                        "/user/upload",
                        "/discuss/add",
                        "/discuss/add/**",
                        "/letter/**",
                        "/like",
                        "/follow",
                        "/unfollow")
                .hasAnyAuthority(
                        AUTHORITY_USER,
                        AUTHORITY_ADMIN,
                        AUTHORITY_MODERATOR)
                .requestMatchers(
                        "/discuss/top",
                        "/discuss/wonderful")
                .hasAnyAuthority(
                        AUTHORITY_MODERATOR)
                .requestMatchers(
                        "/discuss/delete")
                .hasAnyAuthority(
                        AUTHORITY_ADMIN)
                .anyRequest().permitAll() // 除了上述指明的请求外，其余都允许
                .and().csrf().disable(); // 关闭CSRF验证

        // 权限不够时的处理
        http.exceptionHandling()
                // 未登录时
                .authenticationEntryPoint((request, response, authException) -> {
                    String xRequestWith = request.getHeader("x-requested-with");
                    if ("XMLHttpRequest".equals(xRequestWith)) {
                        // 异步请求
                        response.setContentType("application/plain;charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        writer.write(CommunityUtil.getJsonString(403, "您还没有登陆！"));
                    } else {
                        // 同步请求
                        response.sendRedirect(request.getContextPath() + "/login");
                    }
                })
                // 权限不足时
                .accessDeniedHandler((request, response, authException) -> {
                    String xRequestWith = request.getHeader("x-requested-with");
                    if ("XMLHttpRequest".equals(xRequestWith)) {
                        // 异步请求
                        response.setContentType("application/plain;charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        writer.write(CommunityUtil.getJsonString(403, "您没有访问此功能的权限！"));
                    } else {
                        // 同步请求
                        response.sendRedirect(request.getContextPath() + "/denied");
                    }
                });

        // 覆盖登出的默认逻辑，此时Spring Security只有再等到/securitylogout时才会执行内置的登出逻辑
        // 而本项目中根本没有/securitylogout，所以永不会执行
        http.logout().logoutUrl("/securitylogout");

        // 必须这么做才能在后续登录中从SecurityContext中找到认证信息
        // 因为SecurityContextHolder中保存的Authentication信息会先被SecurityContextPersistenceFilter删除
        http.addFilterBefore((servletRequest, servletResponse, filterChain) -> {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            String ticket = CookieUtil.getValue(request, "ticket");
            if (ticket != null) {
                LoginTicket loginTicket = userService.findLoginTicket(ticket);
                if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                    User user = userService.findUserById(loginTicket.getUserId());
                    // 构建用户认证结果，并存入SecurityContext，以便于Security进行授权验证
                    // Authentication是用于封装认证信息的接口，三个参数依次为：principal主要信息（通常是用户对象）、credentials证书（通常是密码）、authorities权限
                    // 这里由于是手动创建Authentication对象并加入ContextHolder，而非用AuthenticationManagerBuilder配置返回，因此Service不用实现UserDetailsService接口
                    Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), userService.getAuthorities(user.getId()));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        }, UsernamePasswordAuthenticationFilter.class); // 必须加在UsernamePasswordAuthenticationFilter之前

        return http.build();
    }
}
