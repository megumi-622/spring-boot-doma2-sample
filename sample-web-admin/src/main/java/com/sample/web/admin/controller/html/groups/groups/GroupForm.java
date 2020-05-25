package com.sample.web.admin.controller.html.groups.groups;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.sample.web.base.validator.annotation.ContentType;
import com.sample.web.base.controller.html.BaseForm;

import lombok.Data;

@Data
public class GroupForm extends BaseForm {

    Long id;

    // グループ名
    @NotEmpty
    String fullName;

    // 略称
    String shortName;

    // メールアドレス
    @NotEmpty
    @Email
    String email;

    // 電話番号
    @Digits(fraction = 0, integer = 0)
    String tel;

    // 郵便番号
    String zip;

    // 住所
    String address;

    // 添付ファイル
    @ContentType(allowed = { MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE })
    transient MultipartFile groupImage; // serializableではないのでtransientにする
}
