package com.example.firstproject.entity;

import lombok.*;

import javax.persistence.*;

@Entity     //해당 클래스를 테이블명으로 하고, 클래스 내 칼럼을 칼럼명으로 하여 DB에 테이블을 생성한다
//롬북을 사용하여 리팩토링
@AllArgsConstructor //생성자 자동 생성
@NoArgsConstructor  //디폴트 생성자 자동 생성
@ToString           //toString() 자동 생성
@Getter             //모든 Getter 자동 생성
@Setter             //모든 Setter 자동 생성
public class Article {
    @Id //PK 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB가 1, 2, 3, 4, 5, .... 시퀀스? 자동 생성
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    //update 시 update를 원하는 칼럼만 업데이트 되고, 업데이트를 하지 않은 칼럼은 null로 나오기 때문에
    //원래 데이터로부터 patch (복사 시킴) -> 이 과정을 먼저 실행한 후 update 실행하면 
    //업데이트 하지 않은 칼럼이 null이 나올일은 없음
    public void patch(Article article) {
         if(article.title != null) {
             this.title = article.title;
         } else if (article.content != null) {
             this.content = article.content;
         }
    }
}
