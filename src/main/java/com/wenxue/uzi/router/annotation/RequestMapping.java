package com.wenxue.uzi.router.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * @author yl
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface RequestMapping {

    /**
     * value
     * @return
     */
    String value() default "";

    /**
     * order
     * @return
     */
    int order() default 0;

}
