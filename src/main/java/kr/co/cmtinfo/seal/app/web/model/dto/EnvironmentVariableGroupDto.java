package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.core.dto.ModelMapperDtoEntityConverter;
import kr.co.cmtinfo.seal.core.web.view.annotation.Title;
import kr.co.cmtinfo.seal.domain.environmentvariable.entity.EnvironmentVariableGroup;
import kr.co.cmtinfo.seal.library.time.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author Yongsu Son
 */
@Getter
@Setter
@NoArgsConstructor
public class EnvironmentVariableGroupDto
        extends ModelMapperDtoEntityConverter<EnvironmentVariableGroup> {

    public EnvironmentVariableGroupDto(EnvironmentVariableGroup entity) {
        super(entity);
    }

    @Title("아이디")
    public Long id;

    @Title("이름")
    public String name;

    @Title("코드")
    public String code;

    @Title("설명")
    public String description;

    @Title("생성일")
    public Date createdAt;

    @Title("수정일")
    public Date updatedAt;

    public String getCreatedAtText() {
        return DateUtils.toText(this.createdAt);
    }

    public String getUpdatedAtText() {
        return DateUtils.toText(this.updatedAt);
    }

}