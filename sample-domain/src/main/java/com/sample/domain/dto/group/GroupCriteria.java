package com.sample.domain.dto.group;

import lombok.Data;

@Data
public class GroupCriteria extends Group {

    // 住所がNULLのデータに絞り込む
    Boolean onlyNullAddress;
}
