package com.sky.service;


import com.sky.dto.SetmealDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    void saveWithDish(SetmealDTO setmealDTO);

    PageResult page(Integer page, Integer pageSize, String name, Long categoryId, Integer status);

    void delete(List<Long> ids);

    SetmealVO getById(Long id);

    void update(SetmealDTO setmealDTO);


    void updateStatus(Integer status, Long id);
}
