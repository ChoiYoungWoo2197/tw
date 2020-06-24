package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.domain.article.entity.ArticlePost;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author jwchang
 */
@Getter
@Setter
@NoArgsConstructor
public class ArticlePostCreateDto extends ArticlePostDto {

    public ArticlePostCreateDto(ArticlePost entity) {
        super(entity);
    }

    @NotNull
    public Long getArticlePostGroupId() {
        return articlePostGroupId;
    }

    @NotBlank
    public String getTitle() {
        return title;
    }

    @NotBlank
    public String getContent() {
        return content;
    }

    @NotBlank
    public String getAuthor() {
        return author;
    }

}