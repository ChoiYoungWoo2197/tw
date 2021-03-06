package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.core.dto.ModelMapperDtoEntityConverter;
import kr.co.cmtinfo.seal.domain.article.entity.ArticleComment;
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
public class ArticleCommentDto extends ModelMapperDtoEntityConverter<ArticleComment> {

    public ArticleCommentDto(ArticleComment entity) {
        super(entity);
    }

    protected Long id;

    protected String content;

    protected String author;

    protected Date createdAt;

    protected Date updatedAt;

    protected Long postId;

    public String getCreateAtText() {
        return DateUtils.toText(this.createdAt);
    }

    public String getUpdateAtText() {
        return DateUtils.toText(this.createdAt);
    }

}
