package com.sample.web.admin.controller.html.groups.groups;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true) // 定義されていないプロパティを無視してマッピングする
@JsonPropertyOrder({ "グループID", "グループ名", "略称", "メールアドレス", "電話番号", "郵便番号", "住所" }) // CSVのヘッダ順
@Data
public class GroupCsv implements Serializable {

    @JsonProperty("グループID")
    Long id;

    @JsonProperty("グループ名")
    String fullName;

    @JsonProperty("略称")
    String shortName;

    @JsonProperty("メールアドレス")
    String email;

    @JsonProperty("電話番号")
    String tel;

    @JsonProperty("郵便番号")
    String zip;

    @JsonProperty("住所")
    String address;
}
