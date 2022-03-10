package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.dto.CommentDto;
import com.example.firstproject.entity.Article;
import com.example.firstproject.entity.Comment;
import com.example.firstproject.repository.ArticleRepository;
import com.example.firstproject.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.annotation.Target;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j  //Simple Logging Facade 4(for) Java -> 로깅을 위한 어노테이션
public class ArticleController {

    @Autowired  //스프리부트가 미리 생성해놓은 객체를 가져다가 자동 연결
    private ArticleRepository articleRepository;

    @Autowired
    private CommentService commentService;

    @GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new"; //  /articles/new.mustache 호출
    }

    //Create (글 작성)
    @PostMapping("/articles/create")    //<form>에서 받음
    public String createArticle(ArticleForm form) { //dto
        //System.out.println(form.toString());  //실제 서버에서는 sysout 코드 사용하지 않음 -> 로깅 기능으로 대체
        log.info(form.toString());

        //JPA를 사용하여 DB 이용하기
        //1. DTO(Article Form)를 Entity(Article)로 변환
        Article article = form.toEntity();
        //System.out.println(article.toString());
        log.info(article.toString());

        //2. Repository에게 Entity(article)를 DB안에 저장하게한 후 Entity로 반환
        Article saved = articleRepository.save(article);
        //System.out.println(saved.toString());
        log.info(saved.toString());

        return "redirect:/articles/" + saved.getId();   //id로 한 개의 데이터 조회 (글 작성시 저장된 Entity 중 id 필드를 get해서 URL에 이용)
    }

    //Read  (id로 한 개의 데이터 조회) - Entity로 반환
    @GetMapping("/articles/{id}")   //{id}는 변하는 숫자다라고 알려주는 것임
    public String show(@PathVariable Long id, Model model) {     //@PathVariable 어노테이션 - URL로 부터 받아오는 변수이다.
        log.info("id= " + id);

        //1. id로 데이터를 Entity 타입으로 가져옴
        //Optional<Article> articleEntity = articleRepository.findById(id);      //작동 안됨, 타입 자동완성 단축키 ctrl+shift+enter
        Article articleEntity = articleRepository.findById(id).orElse(null);
        List<CommentDto> commentDtos = commentService.comments(id);

        //2. 가져온 데이터를 model에 등록 (article이라는 이름으로 articleEntity라는 값을 모델에 등록)
        model.addAttribute("article", articleEntity);
        model.addAttribute("commentDtos",commentDtos);

        //3. 최종으로 보여줄 페이지를 설정
        return "articles/show"; //  /articles/show.mustache 호출
    }

    //Read (데이터 목록 조회) - Entity의 묶음으로 반환
    @GetMapping("/articles")
    public String index(Model model) {
        //1. 모든 Article(Entity)을 리스트 형태로 가져온다.
        List<Article> articleEntityList = (List<Article>) articleRepository.findAll();

        //2. 가져온 Article 묶음을 model에 등록한 후 뷰로 전달
        model.addAttribute("articleList", articleEntityList);

        //3. 최종적으로 호출할 뷰 페이지를 설정
        return "articles/index";    //  articles/index.mustache
    }

    //Update(글 수정) 기능을 위한 데이터를 불러와 view에 보여주기
    @GetMapping("/articles/{id}/edit")      //{{id}}로 쓸수 없음 -> mustache 파일에서만 사용 가능
    public String edit(@PathVariable Long id, Model model) {     //@PathVariable 변수타입 변수명- URL에서 해당 파라미터(변수)를 가져온다! 동일한 이름이어야 함
        //1. 수정할 데이터를 Repository를 통해 Entity 타입으로 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);    //.orElse(null) : 있으면 가져오고 없으면 null

        //2. 가져온 Entity 타입의 데이터를 모델에 등록한 후 뷰로 전달
        model.addAttribute("article", articleEntity);

        //3. 뷰 페이지 설정
        return "articles/edit";     //  articles/edit.mustache
    }

    //Update 메소드
    @PostMapping("/articles/update")
    public String update(ArticleForm form) {    //dto
        //1. mustache에서 받아온 파라미터가 저장된 dto를 entity로 변환
        Article articleEntity = form.toEntity();
        log.info(articleEntity.toString()); //로깅

        //2. entity를 DB에 저장
        //2-1: 기존 데이터에서 id로 검색하여 Entity 타입으로 데이터를 가져옴.
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);    //해당 데이터가 없다면 null
        //2-2: 기존 데이터 값을 새로운 값으로 갱신
        if(target != null) {    //해당 데이터가 존재할 경우에만
            articleRepository.save(articleEntity);
        }

        //3. 수정 결과 페이지로 리다이렉트
        return "redirect:/articles/" + articleEntity.getId();
    }

    //Delete 메소드
    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {       //URL에서 해당 이름을 가진 파라미터를 가져온다
        log.info("Deletion Request");

        //1. 삭제 대상을 DB에서 가져온다. (ENTITY 타입으로!!!! - DB와 연결은 무조건 ENTITY 타입으로!!!!)
        Article target = articleRepository.findById(id).orElse(null);
        log.info(target.toString());

        //2. 그 대상을 삭제한다.
        if(target != null) {
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg", "Deletion Completed");   //일회성 Attribute!! (Flash) - 리다이렉트시 메시지 날림
        }

        //3. 결과 페이지로 리다이렉트한다.
        return "redirect:/articles";       //모든 목록 페이지 (공백 있으면 안됨)
    }
}
