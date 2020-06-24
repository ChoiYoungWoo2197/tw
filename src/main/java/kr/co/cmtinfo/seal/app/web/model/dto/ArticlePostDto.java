package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.core.dto.ModelMapperDtoEntityConverter;
import kr.co.cmtinfo.seal.domain.article.entity.ArticlePost;
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
public class ArticlePostDto extends ModelMapperDtoEntityConverter<ArticlePost> {

    public ArticlePostDto(ArticlePost entity) {
        super(entity);
    }

    protected Long id;

    protected String title;

    protected String content;

    protected String author;

    protected Date createdAt;

    protected Date updatedAt;

    protected Long articlePostGroupId;

    protected String articlePostGroupName;

    public String getCreatedAtText() {
        return DateUtils.toText(this.createdAt);
    }

    public String getUpdatedAtText() {
        return DateUtils.toText(this.createdAt);
    }

}
