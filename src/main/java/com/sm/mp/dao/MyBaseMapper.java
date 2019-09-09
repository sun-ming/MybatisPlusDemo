package com.sm.mp.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface MyBaseMapper<T> extends BaseMapper<T> {

    /**
     * 删除所有
     *
     * @return 影响行数
     */
    int deleteAll();

    int insertBatchSomeColumn(List<T> entityList);
}
