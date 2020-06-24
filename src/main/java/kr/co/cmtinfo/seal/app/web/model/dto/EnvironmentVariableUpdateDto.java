package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.domain.environmentvariable.entity.EnvironmentVariable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Yongsu Son
 */
@Getter
@Setter
@NoArgsConstructor
public class EnvironmentVariableUpdateDto extends EnvironmentVariableDto {

    public EnvironmentVariableUpdateDto(EnvironmentVariable entity) {
        super(entity);
    }

    @Override
    public ModelMapper mapping(ModelMapper modelMapper) {
        modelMapper.typeMap(this.getClass(), EnvironmentVariable.class).addMappings(mapping -> {
            mapping.skip(EnvironmentVariable::setId);
            mapping.skip(EnvironmentVariable::setCreatedAt);
            mapping.skip(EnvironmentVariable::setUpdatedAt);
        });
        return modelMapper;
    }

    @NotNull
    public Long getId() {
        return id;
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
    public String getCode() {
        return code;
    }
}