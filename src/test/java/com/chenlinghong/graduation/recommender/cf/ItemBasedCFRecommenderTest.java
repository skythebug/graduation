package com.chenlinghong.graduation.recommender.cf;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemBasedCFRecommenderTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void recommend() throws TasteException {
        ItemBasedCFRecommender recommender = new ItemBasedCFRecommender(dataSource);
        List<RecommendedItem> itemList = recommender.recommend(1);
        System.out.println(itemList);
    }

    @Test
    public void recommend1() {
    }

    @Test
    public void recommendGraduation() {
    }

    @Test
    public void recommendGraduation1() {
    }
}