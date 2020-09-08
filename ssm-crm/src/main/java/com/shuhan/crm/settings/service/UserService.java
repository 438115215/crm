package com.shuhan.crm.settings.service;

import com.shuhan.crm.settings.domain.User;

import java.util.Map;

public interface UserService {
    User queryUserByLoginActAndPwd(Map<String,Object> map);
}
