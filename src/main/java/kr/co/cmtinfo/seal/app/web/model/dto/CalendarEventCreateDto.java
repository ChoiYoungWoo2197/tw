package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.domain.calendar.entity.CalendarEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Youngwoo Choi
 */
@Getter
@Setter
@NoArgsConstructor
public class CalendarEventCreateDto extends CalendarEventDto{
    public CalendarEventCreateDto(CalendarEvent entity) {
        super(entity);
    }

    @NotNull
    public Long getCalendarId() {
        return calendarId;
    }

    @NotBlank
    public String getEventHeader() {
        return eventHeader;
    }

}
