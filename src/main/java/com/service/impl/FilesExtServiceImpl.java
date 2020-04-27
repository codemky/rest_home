package com.service.impl;

import com.entity.Files;
import com.dao.FilesExtDao;
import com.service.FilesExtService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Files)表服务实现类
 *
 * @author makejava
 * @since 2020-04-20 11:50:01
 */
@Service("filesExtService")
public class FilesExtServiceImpl implements FilesExtService {
    @Resource
    private FilesExtDao filesExtDao;

    
}