package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    Integer countByDishIds(List<Long> ids);


    void insertBatch(List<SetmealDish> setmealDishes);

    void deleteBatch(List<Long> ids);
}
