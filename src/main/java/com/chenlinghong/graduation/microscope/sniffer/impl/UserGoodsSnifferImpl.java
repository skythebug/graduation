package com.chenlinghong.graduation.microscope.sniffer.impl;

import com.chenlinghong.graduation.enums.ErrorEnum;
import com.chenlinghong.graduation.enums.UserBehaviorEnum;
import com.chenlinghong.graduation.exception.AsyncBusinessException;
import com.chenlinghong.graduation.microscope.sniffer.UserGoodsSniffer;
import com.chenlinghong.graduation.microscope.util.SessionUtil;
import com.chenlinghong.graduation.repository.domain.GoodsComment;
import com.chenlinghong.graduation.repository.domain.GoodsOrder;
import com.chenlinghong.graduation.service.UserBehaviorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description TODO
 * @Author chenlinghong
 * @Date 2019/4/25 22:50
 * @Version V1.0
 */
@Slf4j(topic = "me")
@Service
public class UserGoodsSnifferImpl implements UserGoodsSniffer {

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private UserBehaviorService userBehaviorService;

    /**
     * 商品浏览
     *
     * @param goodsId
     * @param request
     */
    @Override
    public void click(long goodsId, HttpServletRequest request) {
        if (goodsId <= 0) {
            log.error("UserGoodsSniffer#click: goodsId is illegal. goodsId={}, requst={}. ", goodsId, request);
            throw new AsyncBusinessException(ErrorEnum.PARAM_ILLEGAL);
        }
        long userId = sessionUtil.getUserId(request);
        if (userId <= 0) {
            // 用户未登录，暂不做任何处理
            log.error("UserGoodsSniffer#click: user not logged in. goodsId={}, requst={}. ", goodsId, request);
            /**
             * TODO 其它处理
             */
            throw new AsyncBusinessException(ErrorEnum.NO_USER);
        }
        // 记录用户浏览该商品记录
        int insertResult = userBehaviorService.insert(goodsId, userId, UserBehaviorEnum.CLICK);
        /**
         * TODO 后期对返回结果处理
         */
    }


    /**
     * 添加购物车
     *
     * @param goodsId
     * @param request
     */
    @Override
    public void addToShoppingCart(long goodsId, HttpServletRequest request) {
        if (goodsId <= 0) {
            log.error("UserGoodsSniffer#addToShoppingCart: goodsId is illegal. goodsId={}, requst={}. ", goodsId, request);
            throw new AsyncBusinessException(ErrorEnum.PARAM_ILLEGAL);
        }
        long userId = sessionUtil.getUserId(request);
        if (userId <= 0) {
            // 用户未登录，暂不做任何处理
            log.error("UserGoodsSniffer#addToShoppingCart: user not logged in. goodsId={}, requst={}. ", goodsId, request);
            /**
             * TODO 其它处理
             */
            throw new AsyncBusinessException(ErrorEnum.NO_USER);
        }
        // 记录用户添加购物车行为
        int insertResult = userBehaviorService.insert(goodsId, userId, UserBehaviorEnum.ADD_TO_CART);
        /**
         * TODO 后期对返回结果处理
         */
    }

    /**
     * 购买行为
     *
     * @param goodsOrder
     */
    @Override
    public void purchase(GoodsOrder goodsOrder) {
        purchase(goodsOrder.getUserId(), goodsOrder.getGoodsId(), goodsOrder.getNumber());
    }

    /**
     * 购买行为
     *
     * @param userId
     * @param goodsId
     */
    @Override
    public void purchase(long userId, long goodsId, int frequency) {
        if (userId <= 0 || goodsId <= 0 || frequency <= 0) {
            // 参数非法
            log.error("UserGoodsSniffer#purchase: param is illegal. userId={}, goodsId={}, frequency={}. ",
                    userId, goodsId, frequency);
            throw new AsyncBusinessException(ErrorEnum.PARAM_ILLEGAL);
        }
        // 记录用户添加购物车行为
        /**
         * TODO 需要进行批量插入
         */
        int insertResult = userBehaviorService.insert(goodsId, userId, UserBehaviorEnum.PURCHASE, frequency);
        /**
         * TODO 后期对返回结果处理
         */
    }

    /**
     * 评论
     * @param goodsComment
     */
    @Override
    public void comment(GoodsComment goodsComment) {
    }

}
