package com.service.impl;

import com.entity.User;
import com.dao.UserExtDao;
import com.service.UserExtService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2020-04-20 11:50:01
 */
@Service("userExtService")
public class UserExtServiceImpl implements UserExtService {
    @Resource
    private UserExtDao userExtDao;

    @Override
    public int insertList(List<User> users) {
        return userExtDao.insertList(users);
    }
}