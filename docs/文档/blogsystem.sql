/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : localhost:3306
 Source Schema         : blogsystem

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : 65001

 Date: 26/05/2019 15:10:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `thumbNum` int(11) NULL DEFAULT 0,
  `collectNum` int(11) NULL DEFAULT 0,
  `publishTime` datetime(0) NULL DEFAULT NULL,
  `updateTime` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `user_id`) USING BTREE,
  INDEX `user_article`(`user_id`) USING BTREE,
  INDEX `id`(`id`) USING BTREE,
  CONSTRAINT `user_article` FOREIGN KEY (`user_id`) REFERENCES `user1` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES (1, 1, 'hello', 'hello world hasdufhashdfkjahsdf发生都i好的佛i哈松岛枫哈师大哈佛啊、sdfsadfasdfhasdhflashdfaasdfhkasdhfajskdfhkajshdfjashdkfhjaskdfjkashdkfhaksdfjasdhfkahsdf', 0, 1, '2019-05-11 18:23:50', '2019-05-11 18:23:52');
INSERT INTO `article` VALUES (11, 1, 'title', '<p>content</p>', 0, 0, '2019-05-21 13:22:29', '2019-05-21 13:22:29');
INSERT INTO `article` VALUES (12, 1, 'hellooooooo', '<p>hellooooooo</p>', 0, 0, '2019-05-21 21:28:16', '2019-05-21 21:28:16');
INSERT INTO `article` VALUES (13, 7, 'wcytkNB', '111', 1, 1, '2019-05-23 08:35:28', '2019-05-23 08:35:28');
INSERT INTO `article` VALUES (14, 7, 'wcytkzz', '111', 1, 1, '2019-05-23 08:36:32', '2019-05-23 08:36:32');
INSERT INTO `article` VALUES (15, 7, 'wcyddddd', '<p>wcydddddd</p>', 0, 0, '2019-05-23 08:42:42', '2019-05-23 08:42:42');
INSERT INTO `article` VALUES (16, 7, 'WCYTKZZZZ', '<p><span style=\"font-weight: bold; font-style: italic;\">WCYTKZZZZ</span></p>', 0, 0, '2019-05-23 08:45:47', '2019-05-23 08:45:47');
INSERT INTO `article` VALUES (17, 7, '深入理解 JavaScript 异步系列（5）—— async await', '<p>前面介绍完了<code>Generator</code>的异步处理，可以说是跌跌撞撞，经过各种基础介绍和封装，好容易出了一个比较简洁的异步处理方案，学习成本非常高————这显然不是我们想要的！</p><p>因此，还未发布的 ES7 就干脆自己参照<code>Generator</code>封装了一套异步处理方案————<code>async-await</code>。说是参照，其实可以理解为是<code>Generator</code>的语法糖！</p><p>本节示例代码参照<a href=\"https://github.com/wangfupeng1988/js-async-tutorial/blob/master/part5-async-await/test.js\">这里</a></p><h2><a id=\"user-content-本节内容概述\" href=\"https://github.com/wangfupeng1988/js-async-tutorial/blob/master/part5-async-await/01-async-await-in-es7.md#%E6%9C%AC%E8%8A%82%E5%86%85%E5%AE%B9%E6%A6%82%E8%BF%B0\"></a>本节内容概述</h2><ul><li><code>Generator</code>和<code>async-await</code>的对比</li><li>使用<code>async-await</code>的不同和好处</li><li>接下来...</li></ul><h2><a id=\"user-content-generator和async-await的对比\" href=\"https://github.com/wangfupeng1988/js-async-tutorial/blob/master/part5-async-await/01-async-await-in-es7.md#generator%E5%92%8Casync-await%E7%9A%84%E5%AF%B9%E6%AF%94\"></a><code>Generator</code>和<code>async-await</code>的对比</h2><p>先来一段<code>Generator</code>处理异步的代码，前面已经介绍过了，看不明白的再获取接着看。</p>', 0, 0, '2019-05-23 09:07:44', '2019-05-23 09:07:44');

-- ----------------------------
-- Table structure for user1
-- ----------------------------
DROP TABLE IF EXISTS `user1`;
CREATE TABLE `user1`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `code` int(2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user1
-- ----------------------------
INSERT INTO `user1` VALUES (1, 'asd', '7815696ecbf1c96e6894b779456d330e', 'asd', 1);
INSERT INTO `user1` VALUES (4, 'wcytk', 'fae0b27c451c728867a567e8c1bb4e53', '1051160758@qq.com', 1);
INSERT INTO `user1` VALUES (5, 'dsa', '5f039b4ef0058a1d652f13d612375a5b', 'wcytk@outlook.com', 1);
INSERT INTO `user1` VALUES (6, '111', '698d51a19d8a121ce581499d7b701668', '111', 0);
INSERT INTO `user1` VALUES (7, 'Yzed', '395142af1d696b92843f18e214c05812', '747498885@qq.com', 1);

-- ----------------------------
-- Table structure for user_article_collection
-- ----------------------------
DROP TABLE IF EXISTS `user_article_collection`;
CREATE TABLE `user_article_collection`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `create_time` datetime(0) NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `article_id`(`article_id`) USING BTREE,
  CONSTRAINT `article_id` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user1` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_article_collection
-- ----------------------------
INSERT INTO `user_article_collection` VALUES (6, 1, 4, '2019-05-15 21:45:18');
INSERT INTO `user_article_collection` VALUES (8, 14, 1, '2019-05-24 15:19:17');
INSERT INTO `user_article_collection` VALUES (9, 13, 1, '2019-05-24 15:23:20');

-- ----------------------------
-- Table structure for user_article_thumb
-- ----------------------------
DROP TABLE IF EXISTS `user_article_thumb`;
CREATE TABLE `user_article_thumb`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `article`(`article_id`) USING BTREE,
  INDEX `user`(`user_id`) USING BTREE,
  CONSTRAINT `article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user` FOREIGN KEY (`user_id`) REFERENCES `user1` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_article_thumb
-- ----------------------------
INSERT INTO `user_article_thumb` VALUES (3, 13, 1, '2019-05-24 15:24:16');
INSERT INTO `user_article_thumb` VALUES (4, 14, 1, '2019-05-24 15:24:20');

-- ----------------------------
-- Table structure for user_attention
-- ----------------------------
DROP TABLE IF EXISTS `user_attention`;
CREATE TABLE `user_attention`  (
  `user_id1` int(11) NOT NULL,
  `user_id2` int(11) NOT NULL,
  INDEX `attention1`(`user_id1`) USING BTREE,
  INDEX `attention2`(`user_id2`) USING BTREE,
  CONSTRAINT `attention1` FOREIGN KEY (`user_id1`) REFERENCES `user1` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `attention2` FOREIGN KEY (`user_id2`) REFERENCES `user1` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_attention
-- ----------------------------
INSERT INTO `user_attention` VALUES (7, 4);
INSERT INTO `user_attention` VALUES (7, 5);
INSERT INTO `user_attention` VALUES (7, 6);
INSERT INTO `user_attention` VALUES (1, 7);

-- ----------------------------
-- Table structure for userdetail
-- ----------------------------
DROP TABLE IF EXISTS `userdetail`;
CREATE TABLE `userdetail`  (
  `detail_id` int(11) NOT NULL,
  `neck_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `birthday` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `place` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `personal_sign` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`detail_id`) USING BTREE,
  CONSTRAINT `user_detail` FOREIGN KEY (`detail_id`) REFERENCES `user1` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of userdetail
-- ----------------------------
INSERT INTO `userdetail` VALUES (1, 'asd', 'asd', 'asd', 'asd', 'asda');
INSERT INTO `userdetail` VALUES (4, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `userdetail` VALUES (7, 'Yzed', 'boy', '1911', '111', '111');

SET FOREIGN_KEY_CHECKS = 1;
