package com.chenlinghong.graduation.microscope.actuator.impl;

import com.chenlinghong.graduation.common.PageDto;
import com.chenlinghong.graduation.constant.NumericConstant;
import com.chenlinghong.graduation.enums.ErrorEnum;
import com.chenlinghong.graduation.enums.UserBehaviorEnum;
import com.chenlinghong.graduation.exception.AsyncBusinessException;
import com.chenlinghong.graduation.microscope.actuator.UserGoodsPreferenceActuator;
import com.chenlinghong.graduation.microscope.calculation.UserGoodsPreferenceCalculation;
import com.chenlinghong.graduation.microscope.sniffer.util.UserBehaviorUtil;
import com.chenlinghong.graduation.repository.domain.UserBehavior;
import com.chenlinghong.graduation.repository.domain.UserPreference;
import com.chenlinghong.graduation.service.UserBehaviorService;
import com.chenlinghong.graduation.service.UserPreferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description 用户-商品偏好执行器实现类
 * @Author chenlinghong
 * @Date 2019/4/26 16:18
 * @Version V1.0
 */
@Slf4j
@Service
public class UserGoodsPreferenceActuatorImpl implements UserGoodsPreferenceActuator {

    @Autowired
    private UserPreferenceService preferenceService;

    @Autowired
    private UserBehaviorService behaviorService;

    @Autowired
    private UserGoodsPreferenceCalculation preferenceCalculation;

    @Override
    public void refresh(UserPreference userPreference) {
        if (userPreference == null) {
            log.error("UserGoodsPreferenceActuator#refresh: param is null.");
            throw new AsyncBusinessException(ErrorEnum.PARAM_IS_NULL);
        }
        int result = preferenceService.update(userPreference);
        /**
         * TODO 校验结果
         */
    }

    @Override
    public void refresh(long userId) {
        /**
         * 1、获取用户指定时间窗口的所有行为
         * 2、分不同的商品进行计算每个商品的偏好
         * 3、将商品偏好分为两大类，已存在用户偏好记录和尚不存在
         * 4、对已存在的用户偏好记录，执行批量更新操作
         * 5、对不存在的用户偏好记录，执行批量插入操作
         *
         * 注：该方式所需资源较多，谨慎使用
         */
        
    }

    @Override
    public void refresh(long userId, long goodsId) {
        Date startTime = getDefaultStartTime();
        refresh(userId, goodsId, startTime);
    }

    @Override
    public void refresh(long userId, long goodsId, Date startTime) {
        PageDto behaviorDto = behaviorService.listByUserAndGoodsAndStartTime(userId, goodsId, startTime);
        if (behaviorDto.getTotalCount() <= 0) {
            // 为获取到数据
            log.error("UserGoodsPreferenceActuator#refresh: no data. userId={}, goodsId={}, " +
                    "startTime={}. ", userId, goodsId, startTime);
            throw new AsyncBusinessException(ErrorEnum.BEHAVIOR_DATA_NOT_EXISTS);
        }
        int preference = 0;
        List<UserBehavior> behaviorList = behaviorDto.getData();
        for (UserBehavior behavior : behaviorList) {
            preference += preferenceCalculation.calculation(behavior.getBehavior());
        }
        refresh(userId, goodsId, preference);
    }

    @Override
    public void refresh(long userId, long goodsId, int preference) {
        UserPreference userPreference = new UserPreference(userId, goodsId, preference);
        refresh(userPreference);
    }

    @Override
    public void append(UserBehavior userBehavior) {
        if (userBehavior == null) {
            log.error("UserGoodsPreferenceActuator#append: param is null.");
            throw new AsyncBusinessException(ErrorEnum.PARAM_IS_NULL);
        }
        int preference = preferenceCalculation.calculation(userBehavior.getBehavior());
        appendByPreference(userBehavior.getUserId().longValue(), userBehavior.getGoodsId().longValue(), preference);
    }

    @Override
    public void append(long userId, long goodsId, int behavior) {
        UserBehaviorEnum behaviorEnum = UserBehaviorUtil.getByBehavior(behavior);
        append(userId, goodsId, behaviorEnum);
    }

    @Override
    public void append(long userId, long goodsId, UserBehaviorEnum behaviorEnum) {
        if (userId <= 0 || goodsId <= 0 || behaviorEnum == null) {
            log.error("UserGoodsPreferenceActuator#append: param is illegal. userId={}, goodsId={}, " +
                    "behaviorEnum={}. ", userId, goodsId, behaviorEnum);
            throw new AsyncBusinessException(ErrorEnum.PARAM_ILLEGAL);
        }
        appendByPreference(userId, goodsId, behaviorEnum.getFactor());
    }

    @Override
    public void appendByPreference(long usrId, long goodsId, int preference) {
        if (usrId <= 0 || goodsId <= 0 || preference < 0) {
            log.error("UserGoodsPreferenceActuator#appendByPreference: param is illegal. userId={}, goodsId={}," +
                    " preference={}. ", usrId, goodsId, preference);
            throw new AsyncBusinessException(ErrorEnum.PARAM_ILLEGAL);
        }
        UserPreference userPreference = preferenceService.getByUserAndGoods(usrId, goodsId);
        if (userPreference == null) {
            log.error("UserGoodsPreferenceActuator#appendByPreference: user preference is not exists. userId={}, " +
                    "goodsId={}, preference={}. ", usrId, goodsId, preference);
            throw new AsyncBusinessException(ErrorEnum.USER_PREFERENCE_NOT_EXISTS);
        }
        preference += userPreference.getPreference() == null ? 0 : userPreference.getPreference();
        userPreference.setPreference(preference);
        refresh(userPreference);
    }


    /**
     * 获取默认时间窗口，三个月
     *
     * @return
     */
    private synchronized Date getDefaultStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        // 设置为三个月之前的时间
        calendar.add(Calendar.MONTH, NumericConstant.THREE * -1);
        return calendar.getTime();
    }
}
