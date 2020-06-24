package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.domain.article.entity.ArticlePost;
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
public class ArticlePostUpdateDto extends ArticlePostDto {

    public ArticlePostUpdateDto(ArticlePost entity) {
        super(entity);
    }

    @Override
    public ModelMapper mapping(ModelMapper modelMapper) {
        modelMapper.typeMap(this.getClass(), ArticlePost.class).addMappings(mapping -> {
            mapping.skip(ArticlePost::setId);
            mapping.skip(ArticlePost::setCreatedAt);
            mapping.skip(ArticlePost::setUpdatedAt);
        });
        return modelMapper;
    }

    @NotNull
    public Long getId() {
        return id;
    }

    @NotBlank
    public String getTitle() { return title; }

    @NotBlank
    public String getContent() { return content; }

    @NotBlank
    public String getAuthor() { return author; }

}