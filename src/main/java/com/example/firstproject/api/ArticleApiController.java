package com.example.firstproject.api;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import com.example.firstproject.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController     //Rest API용 컨트롤러. 데이터 (JSON)를 반환
public class ArticleApiController {

    @Autowired  //DI, 생성 객체를 가져와 연결
    private ArticleService articleService;
/*
    @Autowired
    private ArticleRepository articleRepository;
*/
    //GET (READ) - 전체 목록을 리스트 타입으로 받아오기
    @GetMapping("/api/articles")
    public List<Article> index() {
        return articleService.index();      //서비스를 통해 데이터 가져옴
    }

    //GET (READ) - 하나의 글을 Entity 타입으로 받아오기
    @GetMapping("api/articles/{id}")
    public Article show(@PathVariable Long id) {
        return articleService.show(id);
    }

    //POST (CREATE)
    @PostMapping("api/articles/")
    public ResponseEntity<Article> create(@RequestBody ArticleForm form) {
    //===>RestAPI에서 DTO를 ENTITY로 변환할 때 DTO 객체앞에 @RequestBody 어노테이션 필수!!
        Article created = articleService.create(form);

        //에러 처리
        return (created != null) ?      //삼항연산자
                ResponseEntity.status(HttpStatus.OK).body(created) : //null이 아니면
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);    //null이면
    }

    //PATCH (UPDATE) - 특정 id값에 해당하는 글을 수정
    @PatchMapping("api/articles/{id}")
    public ResponseEntity<Article> update(@RequestBody ArticleForm form, @PathVariable Long id) {
        //ResponseEntity -> 잘못된 요청일 경우 응답 화면에 상태코드를 싣어 보내기 위함
        Article updated = articleService.update(form, id);
        return (updated != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updated) :        //옳은 요청시 업데이트 내용 리턴
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    //DELETE (DELETE)
    @DeleteMapping("api/article/{id}")
    public ResponseEntity<Article> delete(@PathVariable Long id) {
        Article deleted = articleService.delete(id);
        return (deleted != null) ?
                ResponseEntity.status(HttpStatus.OK).body(null) :        //옳은 요청 - deletion은 결과 없음
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    //트랜잭션 -> 실패시 롤백
    @PostMapping("/api/transaction-test")
    public ResponseEntity<List<Article>> transactionTest(@RequestBody List<ArticleForm> dtos) {
        List<Article> createdList = articleService.createArticles(dtos);
        return (createdList != null) ?
            ResponseEntity.status(HttpStatus.OK).body(createdList) :
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();  //body(null)
    }
}
