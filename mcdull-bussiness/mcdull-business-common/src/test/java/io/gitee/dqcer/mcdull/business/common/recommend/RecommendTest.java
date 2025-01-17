package io.gitee.dqcer.mcdull.business.common.recommend;

import cn.hutool.core.io.BOMInputStream;
import io.gitee.dqcer.mcdull.business.common.recommend.dto.RelateDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class RecommendTest {

    private static final Logger log = LoggerFactory.getLogger(RecommendTest.class);

//    @Test
    void userCfRecommend() throws IOException {
        List<RelateDTO<Integer, Integer>> relateList = getRelateList();

        final Integer targetUserId = 2;
        List<Integer> itemIds = Recommend.userCfRecommend(targetUserId, relateList);
        log.info("用户 {} 可能喜欢的物品id集为： {}", targetUserId, itemIds);

    }

    private static List<RelateDTO<Integer, Integer>> getRelateList() throws IOException {
        ClassPathResource resource = new ClassPathResource("relate.data");
        List<RelateDTO<Integer, Integer>> relateList = new ArrayList<>();
        BufferedReader in = new BufferedReader(new InputStreamReader(new BOMInputStream(resource.getInputStream())));
        String line;
        while ((line = in.readLine()) != null) {
            String newline = line.replaceAll("[\t]", " ");
            String[] ht = newline.split(" ");
            Integer userId = Integer.parseInt(ht[0]);
            Integer itemId = Integer.parseInt(ht[1]);
            Integer rating = Integer.parseInt(ht[2]);
            RelateDTO dto = new RelateDTO();
            dto.setIndex(rating);
            dto.setUseId(userId);
            dto.setItemId(itemId);
            relateList.add(dto);
        }
        return relateList;
    }

//    @Test
    void itemCfRecommend() throws IOException {
        final Integer targetItem = 2;
        List<Integer> itemIds = Recommend.itemCfRecommend(targetItem, getRelateList());
        log.info("根据物品 {} ，猜测喜欢它的人可能也喜欢 以下物品id集： {}", targetItem, itemIds);
    }
}