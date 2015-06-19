ALTER TABLE `chris`.`app_config`   
  ADD COLUMN `qiniu_ak` VARCHAR(40) NULL  COMMENT '七牛AK' AFTER `duoshuo_key`,
  ADD COLUMN `qiniu_sk` VARCHAR(40) NULL  COMMENT '七牛SK' AFTER `qiniu_ak`,
  ADD COLUMN `qiniu_bn` VARCHAR(63) NULL  COMMENT '七牛bucketName' AFTER `qiniu_sk`;
UPDATE app_config SET VERSION='1.4';