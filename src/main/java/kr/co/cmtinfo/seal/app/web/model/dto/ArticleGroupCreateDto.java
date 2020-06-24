package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.core.validation.annotation.Unique;
import kr.co.cmtinfo.seal.domain.article.entity.ArticleGroup;
import kr.co.cmtinfo.seal.domain.article.service.ArticleGroupService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author jwchang
 */
@Getter
@Setter
@NoArgsConstructor
public class ArticleGroupCreateDto extends ArticleGroupDto {

    public ArticleGroupCreateDto(ArticleGroup entity) {
        super(entity);
    }

    @NotBlank
    @Override
    public String getName() {
        return name;
    }

    @NotBlank
    @Unique(service = ArticleGroupService.class, fieldName = "code", message = "중복 code 입니다.")
    @Override
    public String getCode() {
        return code;
    }
}