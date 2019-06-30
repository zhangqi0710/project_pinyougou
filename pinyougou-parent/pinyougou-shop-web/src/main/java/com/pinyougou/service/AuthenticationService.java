package com.pinyougou.service;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证类
 */
public class AuthenticationService implements UserDetailsService {

    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据username查询商家信息
        TbSeller tbSeller = sellerService.findOne(username);
        if(tbSeller != null){
            if ("1".equals(tbSeller.getStatus())){
                //该商户存在,spring-security框架会自动将数据库查询的信息和商家输入的信息进行验证
                List<GrantedAuthority> grantAuths = new ArrayList();
                grantAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));
                User user = new User(username,tbSeller.getPassword(),grantAuths);
                return user;
            }else {
                return null;
            }
        }else {
            return null;
        }
    }
}
