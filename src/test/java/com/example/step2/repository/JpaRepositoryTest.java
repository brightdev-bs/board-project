package com.example.step2.repository;

import com.example.step2.domain.Article;
import com.example.step2.domain.config.JpaConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {


    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository,
                             @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select test")
    @Test
    void test() {
        // Given

        // When
        List<Article> articles = articleRepository.findAll();

        // Then
        Assertions.assertThat(articles)
                .isNotNull()
                .hasSize(123);
    }

    @DisplayName("insert test")
    @Test
    void insertTest() {
        // Given
        long count = articleRepository.count();
        Article article = Article.of("new article", "new content", "#spring");

        // When
        Article savedArticle = articleRepository.save(article);

        // Then
        Assertions.assertThat(articleRepository.count()).isEqualTo(count + 1);

    }

    @DisplayName("update test")
    @Test
    void updateTest() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#srpingBoot";
        article.setHashtag(updatedHashtag);

        // When
        Article savedArticle = articleRepository.saveAndFlush(article);

        // Then
        Assertions.assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashtag);

    }

    @DisplayName("delete test")
    @Test
    void deleteTest() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long count = articleRepository.count();
        long count1 = articleCommentRepository.count();
        long deletedCommentSize = article.getArticleCommentSet().size();


        // When
        articleRepository.delete(article);

        // Then
        Assertions.assertThat(articleRepository.count()).isEqualTo(count - 1);
        Assertions.assertThat(articleCommentRepository.count()).isEqualTo(count1 - deletedCommentSize);

    }
}