package com.sample.web.admin.controller.html.groups.groups;

import com.sample.domain.dto.common.Pageable;
import com.sample.web.base.controller.html.BaseSearchForm;

import lombok.Data;

@Data
public class SearchGroupForm extends BaseSearchForm implements Pageable {

    Long id;

    String fullName;

    String shortName;

    String email;

    String tel;
}
