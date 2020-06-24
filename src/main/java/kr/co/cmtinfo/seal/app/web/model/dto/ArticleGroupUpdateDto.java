package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.domain.article.entity.ArticleGroup;
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
public class ArticleGroupUpdateDto extends ArticleGroupDto {

    public ArticleGroupUpdateDto(ArticleGroup entity) {
        super(entity);
    }

    @Override
    public ModelMapper mapping(ModelMapper modelMapper) {
        modelMapper.typeMap(this.getClass(), ArticleGroup.class).addMappings(mapping -> {
            mapping.skip(ArticleGroup::setId);
            mapping.skip(ArticleGroup::setCreatedAt);
            mapping.skip(ArticleGroup::setUpdatedAt);
        });
        return super.mapping(modelMapper);
    }

    @NotNull
    public Long getId() {
        return id;
    }

    @NotBlank
    public String getName() { return name; }

    @NotBlank
    public String getCode() { return code; }

    public String getSkin() { return skin; }

    public String getDescription() { return description; }

}
