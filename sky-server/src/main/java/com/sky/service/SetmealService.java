package com.sky.service;


import com.sky.dto.SetmealDTO;
import com.sky.result.PageResult;

import java.util.List;

public interface SetmealService {

    void saveWithDish(SetmealDTO setmealDTO);

    PageResult page(Integer page, Integer pageSize, String name, Long categoryId, Integer status);

    void delete(List<Long> ids);
}
