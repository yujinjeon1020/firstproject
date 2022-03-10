package com.example.firstproject.dto;

import com.example.firstproject.entity.Article;
import lombok.AllArgsConstructor;
import lombok.ToString;

//DTO 클래스
//롬북을 사용하여 리팩토링
@AllArgsConstructor //생성자 자동 생성
@ToString           //toString() 자동 생성
public class ArticleForm {

    private Long id;
    private String title;
    private String content;

    public Article toEntity() {
        return new Article(id, title, content);
    }
}
