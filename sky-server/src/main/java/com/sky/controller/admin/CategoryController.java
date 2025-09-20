package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@ApiOperation("Category relevant Interface")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @ApiOperation("addCategory")
    public Result save(@RequestBody CategoryDTO categoryDTO){
        categoryService.save(categoryDTO);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status,Long id){
        categoryService.startOrStop(status,id);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(Long id){
        categoryService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Category>> list(Integer type){
        List<Category> list = categoryService.list(type);
        return Result.success(list);

    }








}
