package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
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
            // 3️⃣ 批量插入口味
            dishFlavorMapper.insertBatch(dishFlavors);
        }
    }
}
