package com.service;

import com.entity.User;
import java.util.List;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2020-04-20 11:46:12
 */
public interface UserExtService {

    /**
     * 批量添加用户
     * @param users
     * @return 添加结果
     */
    int insertList(List<User> users);

}