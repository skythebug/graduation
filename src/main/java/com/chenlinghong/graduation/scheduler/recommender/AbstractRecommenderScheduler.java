package com.chenlinghong.graduation.scheduler.recommender;

import com.chenlinghong.graduation.common.PageDto;
import com.chenlinghong.graduation.constant.NumericConstant;
import com.chenlinghong.graduation.converter.RecommenderConverter;
import com.chenlinghong.graduation.recommender.AbstractGraduationMahoutRecommender;
import com.chenlinghong.graduation.repository.domain.User;
import com.chenlinghong.graduation.scheduler.recommender.dto.RecommendDto;
import com.chenlinghong.graduation.scheduler.recommender.dto.RecommendGoodsDto;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.util.List;

/**
 * @Description  推荐执行器抽象类
 * @Author chenlinghong
 * @Date 2019/5/1 22:17
 * @Version V1.0
 */
public abstract class AbstractRecommenderScheduler {

    protected RecommenderConverter recommenderConverter;

    protected AbstractGraduationMahoutRecommender recommender;

    public RecommendDto recommend(final long userId) throws TasteException {
        /**
         * 默认推荐10条数据
         */
        return recommend(userId, NumericConstant.TEN);
    }

    public RecommendDto<RecommendGoodsDto> recommend(long userId, int recommendNum) throws TasteException {
        if (userId <= 0 || recommendNum <= 0) {
            // log.error("UserBasedCFRecommenderScheduler#recommend: param is illegal. userId={}, " +
            //         "recommendNum={}.", userId, recommendNum);
            return null;
        }
        /**
         * 1、填充User信息
         * 2、获取推荐结果
         */
        // 填充userId
        RecommendDto result = new RecommendDto<>();
        result.setUserId(userId);
        // 填充User基本信息
        User user = getUser(userId);
        result.setUser(user);
        // 填充推荐数据
        PageDto<RecommendGoodsDto> data = recommendByCF(userId, recommendNum);
        result.setData(data);
        return result;
    }

    /**
     * 获取推荐结果
     *
     * @param userId
     * @return
     */
    protected PageDto<RecommendGoodsDto> recommendByCF(final long userId, final int recommendNum) throws TasteException {
        /**
         * 获取推荐结果
         */
        List<RecommendedItem> recommendData = recommender.recommend(userId, recommendNum);
        if (recommendData == null || recommendData.size() <= 0) {
            return null;
        }
        /**
         * 转换数据
         */
        List<RecommendGoodsDto> goodsList = recommenderConverter.convert(recommendData);
        PageDto<RecommendGoodsDto> result = new PageDto<>(goodsList);
        return result;
    }

    /**
     * 获取User
     *
     * @param userId
     * @return
     */
    protected User getUser(long userId) {
        /**
         * TODO 从缓存中读取User
         */
        return null;
    }

}
