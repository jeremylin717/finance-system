CREATE DATABASE IF NOT EXISTS finance DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE finance;

CREATE TABLE IF NOT EXISTS `finance_user` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(30) UNIQUE NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `nickname` VARCHAR(30),
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `transaction` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `user_id` INT DEFAULT 1,
  `amount` DECIMAL(10,2) NOT NULL,
  `type` TINYINT NOT NULL COMMENT '1:收入 0:支出',
  `category` VARCHAR(20) NOT NULL,
  `description` VARCHAR(200),
  `transaction_date` DATE NOT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `budget` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `user_id` INT DEFAULT 1,
  `category` VARCHAR(20) NOT NULL,
  `month` VARCHAR(7) NOT NULL COMMENT '格式:2026-06',
  `monthly_limit` DECIMAL(10,2) NOT NULL,
  UNIQUE KEY uk_user_category_month (`user_id`, `category`, `month`)
);

CREATE TABLE IF NOT EXISTS `category_dict` (
  `id` INT PRIMARY KEY,
  `category_name` VARCHAR(20) UNIQUE NOT NULL,
  `type` TINYINT NOT NULL COMMENT '1:收入 0:支出',
  `icon` VARCHAR(10)
);

INSERT INTO `category_dict` (`id`, `category_name`, `type`, `icon`) VALUES
(1, '餐饮', 0, '🍜'), (2, '购物', 0, '🛍️'), (3, '教育', 0, '📚'),
(4, '交通', 0, '🚌'), (5, '娱乐', 0, '🎮'), (6, '兼职', 1, '💼'),
(7, '生活费', 1, '💰'), (8, '奖学金', 1, '🏆')
ON DUPLICATE KEY UPDATE
  `category_name` = VALUES(`category_name`),
  `type` = VALUES(`type`),
  `icon` = VALUES(`icon`);
