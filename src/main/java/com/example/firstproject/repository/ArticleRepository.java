package com.example.firstproject.repository;

import com.example.firstproject.entity.Article;
import org.springframework.data.repository.CrudRepository;

//Repository는 interface로!!!!
public interface ArticleRepository extends CrudRepository<Article, Long> {  //<관리대상 엔티티, id 타입>

}
