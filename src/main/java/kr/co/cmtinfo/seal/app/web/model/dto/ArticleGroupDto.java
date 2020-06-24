package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.core.dto.ModelMapperDtoEntityConverter;
import kr.co.cmtinfo.seal.core.web.view.annotation.Title;
import kr.co.cmtinfo.seal.domain.article.entity.ArticleGroup;
import kr.co.cmtinfo.seal.library.time.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author jwchang
 */
@Getter
@Setter
@NoArgsConstructor
public class ArticleGroupDto extends ModelMapperDtoEntityConverter<ArticleGroup> {

    public ArticleGroupDto(ArticleGroup entity) {
        super(entity);
    }

    @Title("아이디")
    protected Long id;

    @Title("게시판 코드")
    protected String code;

    @Title("게시판 이름")
    protected String name;

    @Title("스킨")
    protected String skin;

    @Title("설명")
    protected String description;

    @Title("생성일")
    protected Date createdAt;

    @Title("수정일")
    protected Date updatedAt;

    public String getCreatedAtText() {
        return DateUtils.toText(this.createdAt);
    }

    public String getUpdatedAtText() {
        return DateUtils.toText(this.updatedAt);
    }

}