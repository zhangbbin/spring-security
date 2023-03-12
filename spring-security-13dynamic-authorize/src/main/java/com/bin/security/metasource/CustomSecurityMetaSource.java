package com.bin.security.metasource;

import com.bin.entity.Menu;
import com.bin.entity.Role;
import com.bin.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

@Component
public class CustomSecurityMetaSource implements FilterInvocationSecurityMetadataSource {

    private final MenuService menuService;

    @Autowired
    public CustomSecurityMetaSource(MenuService menuService) {
        this.menuService = menuService;
    }

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 自定义动态资源权限元数据信息
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //1.当前的请求对象
        String requestURI = ((FilterInvocation) object).getRequest().getRequestURI();
        //2.查询所有的菜单
        List<Menu> allMenu = menuService.getAllMenu();
        for (Menu menu : allMenu){
            if(antPathMatcher.match(menu.getPattern(),requestURI)) {
                String[] roles = menu.getRoles().stream().map(Role::getName).toArray(String[]::new);
                return SecurityConfig.createList(roles);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
