CREATE TABLE IF NOT EXISTS groups(
  group_id INT(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'グループID'
  , full_name VARCHAR(40) NOT NULL COMMENT '正式名称'
  , short_name VARCHAR(40) NOT NULL COMMENT '略称'
  , email VARCHAR(100) DEFAULT NULL COMMENT 'メールアドレス'
  , tel VARCHAR(20) DEFAULT NULL COMMENT '電話番号'
  , zip VARCHAR(20) DEFAULT NULL COMMENT '郵便番号'
  , address VARCHAR(100) DEFAULT NULL COMMENT '住所'
  , upload_file_id INT(11) unsigned DEFAULT NULL COMMENT '添付ファイル'
  , created_by VARCHAR(50) NOT NULL COMMENT '登録者'
  , created_at DATETIME NOT NULL COMMENT '登録日時'
  , updated_by VARCHAR(50) DEFAULT NULL COMMENT '更新者'
  , updated_at DATETIME DEFAULT NULL COMMENT '更新日時'
  , deleted_by VARCHAR(50) DEFAULT NULL COMMENT '削除者'
  , deleted_at DATETIME DEFAULT NULL COMMENT '削除日時'
  , version INT(11) unsigned NOT NULL DEFAULT 1 COMMENT '改訂番号'
  , PRIMARY KEY (group_id)
  , KEY idx_group (email, deleted_at)
) COMMENT='グループ';