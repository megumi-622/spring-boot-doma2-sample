package com.sample.domain.dto.group;

import com.sample.domain.dto.common.DomaDtoImpl;
import com.sample.domain.dto.system.UploadFile;

import lombok.Data;
import org.seasar.doma.Column;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.seasar.doma.Transient;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Table(name = "groups")
@Data
public class Group extends DomaDtoImpl{

    @Id
    @Column(name = "group_id")
    Long id;

    // グループ名
    String fullName;

    // 略称
    String shortName;

    // メールアドレス
    @Email
    String email;

    // 電話番号
    @Digits(fraction = 0, integer = 10)
    String tel;

    // 郵便番号
    @NotEmpty
    String zip;

    // 住所
    @NotEmpty
    String address;

    // 添付ファイルID
    Long uploadFileId;

    // 添付ファイル
    @Transient
    UploadFile uploadFile;
}
