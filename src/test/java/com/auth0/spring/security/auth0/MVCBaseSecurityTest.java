package com.auth0.spring.security.auth0;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import com.auth0.spring.security.auth0.Auth0TokenHelper;

/**
 * Base Spring security tests including spring security filter chain configured
 */

@WebAppConfiguration
@ContextConfiguration("classpath:application-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class MVCBaseSecurityTest {

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private Auth0TokenHelper<Object> tokenHelper;

    protected MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).addFilters(this.springSecurityFilterChain).build();
    }

    protected String generateTokenWithExpirationDate(long value) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("email", "auth0@test.com");
        map.put("email_verified", true);
        //TODO check int overflow???
        return tokenHelper.generateToken(map, value);

    }

    protected ResultActions callUrlWithoutToken(String url) throws Exception {
        return this.mockMvc.perform(get(url));
    }

    protected ResultActions callUrlWithToken(String url, String token) throws Exception {
        return this.mockMvc.perform(get(url).header("Authorization", "Bearer " + token));
    }

}
