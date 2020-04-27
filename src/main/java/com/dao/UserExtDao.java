package com.dao;

import com.entity.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (User)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-20 11:46:12
 */
public interface UserExtDao {

    /**
     * 批量新增对象
     */
    int insertList(@Param("users") List<User> users);

}