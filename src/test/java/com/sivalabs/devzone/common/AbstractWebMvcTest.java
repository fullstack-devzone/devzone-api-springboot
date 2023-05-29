package com.sivalabs.devzone.common;

import static com.sivalabs.devzone.utils.TestConstants.PROFILE_TEST;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivalabs.devzone.config.security.SecurityConfig;
import com.sivalabs.devzone.config.security.SecurityUtils;
import com.sivalabs.devzone.config.security.TokenHelper;
import com.sivalabs.devzone.config.security.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles(PROFILE_TEST)
@Import({WebSecurityConfig.class, SecurityConfig.class})
public abstract class AbstractWebMvcTest {
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected UserDetailsService userDetailsService;

    @MockBean
    protected TokenHelper tokenHelper;

    @MockBean
    protected SecurityUtils securityUtils;

    @MockBean
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected ObjectMapper objectMapper;
}
