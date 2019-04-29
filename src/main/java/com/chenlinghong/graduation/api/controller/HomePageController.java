package com.chenlinghong.graduation.api.controller;

import com.chenlinghong.graduation.api.vo.HomePageVo;
import com.chenlinghong.graduation.common.ResultUtil;
import com.chenlinghong.graduation.common.ResultVo;
import com.chenlinghong.graduation.microscope.util.SessionUtil;
import com.chenlinghong.graduation.service.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 首页
 * @Author chenlinghong
 * @Date 2019/4/29 9:14
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/home")
public class HomePageController {

    @Autowired
    private SessionUtil microscopeSessionUtil;

    @Autowired
    private HomePageService homePageService;

    /**
     * 获取首页数据
     *
     * @param request
     * @return
     */
    @GetMapping(value = {"/home", "/recommend"})
    public ResultVo<HomePageVo> home(HttpServletRequest request) {
        long userId = microscopeSessionUtil.getUserId(request);
        HomePageVo result = homePageService.get(userId);
        return ResultUtil.success(result);
    }

}
