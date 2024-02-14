CREATE TABLE `member` (
                          `createTime` datetime(6) DEFAULT NULL,
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `lastUpdateDate` datetime(6) DEFAULT NULL,
                          `email` varchar(255) DEFAULT NULL,
                          `name` varchar(255) DEFAULT NULL,
                          `password` varchar(255) DEFAULT NULL,
                          `provider` varchar(255) DEFAULT NULL,
                          `providerId` varchar(255) DEFAULT NULL,
                          `refreshTokenId` varchar(255) DEFAULT NULL,
                          `role` varchar(255) DEFAULT NULL,
                          `username` varchar(255) DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `post` (
                        `buyCount` int NOT NULL,
                        `discountRate` int NOT NULL,
                        `price` int NOT NULL,
                        `shipCount` int NOT NULL,
                        `createTime` datetime(6) DEFAULT NULL,
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `lastUpdateDate` datetime(6) DEFAULT NULL,
                        `body` text,
                        `coupon` varchar(255) DEFAULT NULL,
                        `title` varchar(255) DEFAULT NULL,
                        `titleImage` varchar(255) DEFAULT NULL,
                        `member_id` bigint DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        KEY `FKeylqkppvgvhgb5a6pwcjjw27o` (`member_id`),
                        FULLTEXT KEY `fx_title` (`title`) /*!50100 WITH PARSER `ngram` */ ,
                        CONSTRAINT `FKeylqkppvgvhgb5a6pwcjjw27o` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


