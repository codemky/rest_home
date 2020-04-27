package com.service;

import com.entity.criteria.CureCriteria;
import com.entity.vo.DiseaseCureVO;

import java.util.List;

/**
 * (DiseaseCure)表服务接口
 *
 * @author makejava
 * @since 2020-04-20 11:46:12
 */
public interface DiseaseCureExtService {

    public List<DiseaseCureVO> selectByCriteria(CureCriteria cureCriteria);

    public DiseaseCureVO selectById(Integer id);

//    int insertRecord(MultipartFile[] files, DiseaseCureVO cureVO);
//
//    int templateOp(MultipartFile[] files, DiseaseCureVO cureVO);
}