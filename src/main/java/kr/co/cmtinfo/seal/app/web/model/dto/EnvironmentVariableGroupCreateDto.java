package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.core.validation.annotation.Unique;
import kr.co.cmtinfo.seal.domain.environmentvariable.entity.EnvironmentVariableGroup;
import kr.co.cmtinfo.seal.domain.environmentvariable.service.EnvironmentVariableGroupService;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Yongsu Son
 */
@NoArgsConstructor
public class EnvironmentVariableGroupCreateDto extends EnvironmentVariableGroupDto {

    public EnvironmentVariableGroupCreateDto(EnvironmentVariableGroup entity) {
        super(entity);
    }

    @NotBlank
    @Override
    public String getName() {
        return super.getName();
    }

    @NotBlank
    @Unique(service = EnvironmentVariableGroupService.class, fieldName = "code", message = "중복 code 입니다.")
    @Override
    public String getCode() {
        return super.getCode();
    }
}