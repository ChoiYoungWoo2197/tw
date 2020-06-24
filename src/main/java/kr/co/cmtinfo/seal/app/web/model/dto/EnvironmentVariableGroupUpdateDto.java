package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.domain.environmentvariable.entity.EnvironmentVariableGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;

/**
 * @author Yongsu Son
 */
@Getter
@Setter
@NoArgsConstructor
public class EnvironmentVariableGroupUpdateDto extends EnvironmentVariableGroupDto {

    public EnvironmentVariableGroupUpdateDto(EnvironmentVariableGroup entity) {
        super(entity);
    }

    @Override
    public ModelMapper mapping(ModelMapper modelMapper) {
        modelMapper.typeMap(this.getClass(), EnvironmentVariableGroup.class).addMappings(mapping -> {
            mapping.skip(EnvironmentVariableGroup::setId);
            mapping.skip(EnvironmentVariableGroup::setCreatedAt);
            mapping.skip(EnvironmentVariableGroup::setUpdatedAt);
        });
        return modelMapper;
    }

    @NotBlank
    @Override
    public String getName() {
        return super.getName();
    }

    @NotBlank
    @Override
    public String getCode() {
        return super.getCode();
    }
}
