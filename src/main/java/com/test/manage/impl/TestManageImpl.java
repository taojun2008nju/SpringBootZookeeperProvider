package com.test.manage.impl;

import com.test.manage.TestManage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @date 2020/6/27 19:06:00
 * @description TestManage实现类
 */
@Slf4j
@Component
public class TestManageImpl implements TestManage {

    @Override
    public String test(String text) {
        return "manage_" + text;
    }
}
