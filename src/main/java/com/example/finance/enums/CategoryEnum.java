package com.example.finance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统内置分类枚举。
 */
@Getter
@AllArgsConstructor
public enum CategoryEnum {

    FOOD("餐饮", 0),
    SHOPPING("购物", 0),
    EDUCATION("教育", 0),
    TRANSPORT("交通", 0),
    ENTERTAINMENT("娱乐", 0),
    PART_TIME("兼职", 1),
    LIVING_EXPENSE("生活费", 1),
    SCHOLARSHIP("奖学金", 1);

    private final String categoryName;

    /**
     * 1 表示收入，0 表示支出。
     */
    private final Integer type;

    public static boolean exists(String categoryName, Integer type) {
        for (CategoryEnum categoryEnum : values()) {
            if (categoryEnum.categoryName.equals(categoryName) && categoryEnum.type.equals(type)) {
                return true;
            }
        }
        return false;
    }
}
