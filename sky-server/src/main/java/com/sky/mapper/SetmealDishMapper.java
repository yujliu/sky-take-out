package com.sky.mapper;


import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    List<Long> getSetmealIdsByDishIds(List<Long> ids);

    //@AutoFill(value = OperationType.INSERT)
    void insertBatch(List<SetmealDish> setmealDishes);


    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void deleteBySetmealId(Long id);


    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getSetmealIdsByDishId(Long id);
}
