package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.core.validation.annotation.Unique;
import kr.co.cmtinfo.seal.domain.environmentvariable.entity.EnvironmentVariable;
import kr.co.cmtinfo.seal.domain.environmentvariable.service.EnvironmentVariableService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Yongsu Son
 */
@Getter
@Setter
@NoArgsConstructor
public class EnvironmentVariableCreateDto extends EnvironmentVariableDto {

    public EnvironmentVariableCreateDto(EnvironmentVariable entity) {
        super(entity);
    }

    @NotNull
    public Long getEnvironmentVariableGroupId() {
        return environmentVariableGroupId;
    }

    @NotBlank
    public String getName() {
        return name;
    }

    @NotBlank
    @Unique(service = EnvironmentVariableService.class, fieldName = "code", message = "중복 code 입니다.")
    public String getCode() {
        return code;
    }
}