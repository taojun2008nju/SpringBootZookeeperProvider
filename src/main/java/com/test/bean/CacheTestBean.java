package com.test.bean;

import java.io.Serializable;
import lombok.Data;

/**
 * @author Administrator
 * @date 2020/6/27 16:54:00
 * @description 缓存测试Bean
 */
@Data
public class CacheTestBean implements Serializable {

    private String id;

    private String name;
}
