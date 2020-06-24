package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.domain.article.entity.ArticleComment;
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
public class ArticleCommentCreateDto extends ArticleCommentDto {

    public ArticleCommentCreateDto(ArticleComment entity) {
        super(entity);
    }

    @NotNull
    public Long getPostId() { return postId; }

    @NotBlank
    public String getContent() {
        return content;
    }

    @NotBlank
    public String getAuthor() {
        return author;
    }

}
