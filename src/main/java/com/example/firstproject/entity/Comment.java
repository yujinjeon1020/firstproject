package com.example.firstproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB가 자동 증가시킴
    private Long id;

    @ManyToOne  //여러개의 댓글이(Many) 하나의 Article(글)(one)에 연관됨
    @JoinColumn(name = "article_id")    //join될 칼럼명 입력
    private Article article;

    @Column
    private String nickname;

    @Column
    private String body;
}
