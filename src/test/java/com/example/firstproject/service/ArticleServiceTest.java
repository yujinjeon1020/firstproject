package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest     //해당 클래스는 스프링 부트와 연동되어 테스팅 된다!
class ArticleServiceTest {

    @Autowired
    ArticleService articleService;  //테스트하는 클래스

    //Read
    @Test           //테스트 메소드
    void index() {
        //예상
        Article a = new Article(1L, "aaaa", "1111");
        Article b = new Article(1L, "bbbb", "2222");
        Article c = new Article(1L, "cccc", "3333");
        List<Article> expected = new ArrayList<>(Arrays.asList(a, b, c));

        //실제
        List<Article> articles = articleService.index();

        //예상과 실제를 비교
        assertEquals(expected.toString(), articles.toString());
    }

    //Read
    @Test
    void show_successful_존재하는_id_입력() {
        //예상
        Long id = 1L;
        Article expected = new Article(id, "aaaa", "1111");

        //실제
        Article article = articleService.show(id);

        //비교
        assertEquals(expected.toString(), article.toString());
    }

    @Test
    void show_failed_존재하지_않는_id_입력() {
        //예상
        Long id = -1L;
        Article expected = null;

        //실제
        Article article = articleService.show(id);

        //비교
        assertEquals(expected, article);    //null은 toString() 메소드 사용 불가
    }

    //Create
    @Test
    @Transactional      //데이터 조회(Read)를 제외한 모든 메소드(C,U,D)가 실행 후 Rollback 되도록 함!
    void create_successful_title과_content만_있는_dto_입력() {
        //예상
        String title = "dddd";
        String content = "4444";
        ArticleForm dto = new ArticleForm(null, title, content);
        Article expected = new Article(4L, title, content);

        //실제
        Article article = articleService.create(dto);

        //비교
        assertEquals(expected.toString(), article.toString());
    }

    @Test
    @Transactional      //데이터 조회(Read)를 제외한 모든 메소드(C,U,D)가 실행 후 Rollback 되도록 함!
    void create_failed_id값을_넣을_필요가_없는데_id값을_입력() {
        //예상
        String title = "dddd";
        String content = "4444";
        ArticleForm dto = new ArticleForm(null, title, content);
        Article expected = null;

        //실제
        Article article = articleService.create(dto);

        //비교
        assertEquals(expected, article);
    }
}