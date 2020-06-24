package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.core.dto.ModelMapperDtoEntityConverter;
import kr.co.cmtinfo.seal.domain.environmentvariable.entity.EnvironmentVariable;
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
public class EnvironmentVariableDto extends ModelMapperDtoEntityConverter<EnvironmentVariable> {

    public EnvironmentVariableDto(EnvironmentVariable entity) {
        super(entity);
    }

    protected Long id;

    protected String name;

    protected String code;

    protected String value;

    protected String description;

    protected Date createdAt;

    protected Date updatedAt;

    protected Long environmentVariableGroupId;

    protected String environmentVariableGroupName;

    protected String environmentVariableGroupCode;

    protected String environmentVariableGroupDescription;

    protected Date environmentVariableGroupCreatedAt;

    protected Date environmentVariableGroupUpdatedAt;

    public String getCreatedAtText() {
        return DateUtils.toText(this.createdAt);
    }

    public String getUpdatedAtText() {
        return DateUtils.toText(this.updatedAt);
    }

    public String getEnvironmentVariableGroupCreatedAtText() {
        return DateUtils.toText(this.environmentVariableGroupCreatedAt);
    }

    public String getEnvironmentVariableGroupUpdatedAtText() {
        return DateUtils.toText(this.environmentVariableGroupUpdatedAt);
    }
}
