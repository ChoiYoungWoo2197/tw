package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.core.validation.annotation.Unique;
import kr.co.cmtinfo.seal.domain.calendar.entity.Calendar;
import kr.co.cmtinfo.seal.domain.calendar.service.CalendarService;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Youngwoo Choi
 */
@NoArgsConstructor
public class CalendarCreateDto extends CalendarDto{

    public CalendarCreateDto(Calendar entity) {
        super(entity);
    };

    @NotBlank
    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @NotBlank
    @Unique(service = CalendarService.class, fieldName = "code", message = "중복 code 입니다.")
    @Override
    public String getCode() {
        return super.getCode();
    }
}
