package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @PostMapping
    @ApiOperation("新增套餐")
    public Result save( @RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐");

        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> page(
            @RequestParam Integer page,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status)
    {
        log.info("套餐分页查询,{}, {},{},{},{}", page, pageSize, name, categoryId, status);
        PageResult pageResult = setmealService.page(page, pageSize, name, categoryId, status);
        return Result.success(pageResult);
    }
}
