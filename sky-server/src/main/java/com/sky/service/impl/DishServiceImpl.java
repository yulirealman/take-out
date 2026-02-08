package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Employee;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        //copy dishDto to dish
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //insert dish
        dishMapper.insert(dish);
        // get flavors
        List<DishFlavor> dishFlavors =dishDTO.getFlavors();
        // bind dish id
        if(dishFlavors!= null && dishFlavors.size()>0){
            for(DishFlavor flavor : dishFlavors){
                flavor.setDishId(dish.getId());
            }
            //insert batch flavor
            dishFlavorMapper.insertBatch(dishFlavors);
        }
    }

    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {

        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.page(dishPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());

    }
}
