package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.domain.article.entity.ArticleComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author jwchang
 */
@Getter
@Setter
@NoArgsConstructor
public class ArticleCommentUpdateDto extends ArticleCommentDto {

    public ArticleCommentUpdateDto(ArticleComment entity) {
        super(entity);
    }

    public ModelMapper mapping(ModelMapper modelMapper) {
        modelMapper.typeMap(this.getClass(), ArticleComment.class).addMappings(mapping -> {
            mapping.skip(ArticleComment::setId);
            //mapping.skip(ArticleComment::setArticlePost);
            mapping.skip(ArticleComment::setCreatedAt);
            mapping.skip(ArticleComment::setUpdatedAt);
        });
        return modelMapper;
    }

    @NotNull
    public Long getId() {
        return id;
    }

    @NotBlank
    public String getContent() { return content; }

    @NotBlank
    public String getAuthor() { return author; }

}
