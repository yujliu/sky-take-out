package com.sky.service.impl;

import com.github.pagehelper.Constant;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetMealServiceimpl implements SetMealService {

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private SetMealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;

    @Override
    @Transactional
    public void save(SetmealDTO setmealDTO) {
        //复制到Setmeal
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);

        //得到list SetmealDish 改id，插入
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealId);
        }
        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    @Transactional
    public void deleteBach(List<Long> ids) {
        //如要删起售套餐报错
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        //删两个表中的
        for (Long id : ids) {
            setmealMapper.deleteById(id);
            setmealDishMapper.deleteBySetmealId(id);
        }
    }

    @Override
    @Transactional
    public void startOrStop(Integer status, Long id) {
        //套餐包含停售的菜品报错
        if (status == StatusConstant.ENABLE){
            List<Dish> dishList = dishMapper.getDishBySetmealId(id);
            if (dishList != null && !dishList.isEmpty()){
                for (Dish dish : dishList) {
                    if (dish.getStatus() == StatusConstant.DISABLE){
                        throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                }
            }
        }

        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmeal.setId(id);
        setmealMapper.update(setmeal);
    }

    @Override
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        //复制到Setmeal,更新
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        //setmealDish表里的全删
        setmealDishMapper.deleteBySetmealId(setmeal.getId());

        //得到新的，改id，插入
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        Long semealId = setmealDTO.getId();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(semealId);
        }
        setmealDishMapper.insertBatch(setmealDishes);

    }

    @Override
    public SetmealVO getById(Long id) {
        //回显，先查setmeal
        Setmeal setmeal = setmealMapper.getById(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);

        //再查对应菜品,一起封装到VO
        List<SetmealDish> setmealDishList = setmealDishMapper.getSetmealIdsByDishId(id);
        setmealVO.setSetmealDishes(setmealDishList);
        return setmealVO;
    }
}
