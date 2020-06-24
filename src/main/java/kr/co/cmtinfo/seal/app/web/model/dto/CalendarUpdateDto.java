package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.domain.calendar.entity.Calendar;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;

/**
 * @author Youngwoo Choi
 */
@Getter
@Setter
@NoArgsConstructor
public class CalendarUpdateDto extends CalendarDto{

    public CalendarUpdateDto(Calendar entity) { super(entity);}

    @Override
    public ModelMapper mapping(ModelMapper modelMapper) {
        modelMapper.typeMap(this.getClass(), Calendar.class).addMappings(mapping -> {
            mapping.skip(Calendar::setId);
            mapping.skip(Calendar::setCreatedAt);
            mapping.skip(Calendar::setUpdatedAt);
        });
        return modelMapper;
    }

    @NotBlank
    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @NotBlank
    @Override
    public String getCode() {
        return super.getCode();
    }
}
