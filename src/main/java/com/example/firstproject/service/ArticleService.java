package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service        //서비스 선언! (서비스 객체를 스프링부트에 생성)
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    //Read (모든 목록)
    public List<Article> index() {
        return (List<Article>) articleRepository.findAll();
    }

    //Read (하나의 글)
    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    //Create (글 작성)
    public Article create(ArticleForm form) {
        //1, dto를 entity로 변환
        Article article = form.toEntity();
        //ID가 이미 존재한다면 아무것도 실행되지 않게 함 (새로운 글을 등록하는 것이므로 ID가 존재하지 않는 ID를 입력하게끔)
        if (article.getId() != null) {
            return null;
        }
        //2. repository 이용하여 db에 저장
        return articleRepository.save(article);
    }

    //Update (글 수정)
    public Article update(ArticleForm form, Long id) {
        //1. dto를 entity로 변환
        Article article = form.toEntity();  //article = 업데이트 할 새로운 데이터

        //2. 해당 id를 가지는 데이터 존재유무 찾기
        Article target = articleRepository.findById(id).orElse(null);   //target = 원래 데이터

        //3. 잘못된 요청은 컨트롤러에서 처리하므로 그냥 null해줌 (해당 id를 가지는 데이터가 없거나, JSON의 ID값이 ENTITY ID값과 다를 경우)
        if (target == null || id != article.getId()) {
            return null;
        }

        //4. 업데이트
        //target(원래 데이터)에 article(새로운 데이터)을 패치(복사) - 새로운 데이터가 있는 칼럼만 복사되고 나머지는 원 데이터 유지
        target.patch(article);  //dto에서 entity로 변환한 데이터를 미리 복사해 둠 (수정되지 않는 칼럼들이 null화 되는것을 막음)
        return articleRepository.save(target);
    }

    //Delete (글 삭제)
    public Article delete(@PathVariable Long id) {
        //대상 찾기
        Article target = articleRepository.findById(id).orElse(null);

        //잘못된 요청 처리 (실질적 처리는 컨트롤러에서)
        if (target == null) {
            return null;
        }

        //target이 null이 아닐 경우 대상 삭제 후 응답
        articleRepository.delete(target);
        return target;
    }

    @Transactional  //해당 메소드를 트랜잭션으로 묶는다 (실패시 롤백됨)
    public List<Article> createArticles(List<ArticleForm> dtos) {
        //dto 묶음을 entity 묶음으로 변환
        List<Article> articleList = dtos.stream()
                .map(dto -> dto.toEntity())
                .collect(Collectors.toList());
/*        List<Article> articleList = new ArrayList<>();
        for (int i=0; i<dtos.size(); i++) {
            ArticleForm dto = dtos.get(i);
            Article entity = dto.toEntity();
            articleList.add(entity);
        }   */

        //entity 묶음을 DB로 저장
        articleList.stream()
                .forEach(article -> articleRepository.save(article));

        //강제 예외 발생
        articleRepository.findById(-1L).orElseThrow(
                () -> new IllegalArgumentException("결제 실패!")
        );

        //결과값 반환
        return articleList;
    }
}
