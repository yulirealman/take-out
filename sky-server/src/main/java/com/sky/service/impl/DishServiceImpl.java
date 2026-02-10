package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Employee;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;
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

    @Override
    @Transactional
    public void delete(List<Long> ids) {
        /*
        * 业务规则:
            .可以一次删除一个菜品，也可以批量删除菜品
            .起售中的菜品不能删除     //status = 0, 停售中的菜品可以删除
            .被套餐关联的菜品不能删除 //setmeal_dish
            .删除菜品后，关联的口味数据也需要删除掉
        * */
        //判断当前菜品是否能够删除---是否存在起售中的菜品??
        int onSale = dishMapper.countByStatus(ids, StatusConstant.ENABLE);

        if (onSale > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }
        //判断当前菜品是否能够删除---是否被套餐关联了??
        int linked = setmealDishMapper.countByDishIds(ids);

        if (linked > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //删除菜品表中的菜品数据工
        dishFlavorMapper.deleteByDishIds(ids);
        //删除菜品关联的口味数据
        dishMapper.delete(ids);



    }

    @Override
    @Transactional
    public DishVO getByIdWithFlavor(Long id) {

        DishVO dishVO = dishMapper.getByIdWithFlavor(id);
        dishVO.setFlavors(dishFlavorMapper.getByDishId(id));
        return dishVO;

    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish =new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //修改菜品表基本数据
        dishMapper.update(dish);
        // 先删旧数据（不管新数据有没有）
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        //重新插入口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!= null && flavors.size()>0){
            for(DishFlavor flavor : flavors){
                flavor.setDishId(dishDTO.getId());
            }
            //insert batch flavor
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        //todo 添加业务逻辑， 想要停售的话需要判断该菜品是否套餐中关联
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }


}
