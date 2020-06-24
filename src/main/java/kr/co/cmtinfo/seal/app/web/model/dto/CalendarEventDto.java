package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.core.dto.ModelMapperDtoEntityConverter;
import kr.co.cmtinfo.seal.core.web.view.annotation.Title;
import kr.co.cmtinfo.seal.domain.calendar.entity.CalendarEvent;
import kr.co.cmtinfo.seal.library.time.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author Youngwoo Choi
 */
@Getter
@Setter
@NoArgsConstructor
public class CalendarEventDto extends ModelMapperDtoEntityConverter<CalendarEvent> {

    public CalendarEventDto(CalendarEvent entity) {
        super(entity);
    }

    protected Long id;

    protected String eventHeader;

    protected Date eventStart;

    protected Date eventEnd;

    protected Date eventCreatedAt;

    protected Date eventUpdatedAt;

    protected Long calendarId;

    protected String calendarTitle;

    protected String calendarCode;

    protected String calendarDescription;

    protected Date calendarCreatedAt;

    protected Date calendarUpdatedAt;


    public String getCreatedAtText() {
        return DateUtils.toText(this.eventCreatedAt);
    }

    public String getUpdateAtText() {
        return DateUtils.toText(this.eventUpdatedAt);
    }

    public String getCalendarCreatedAtText() {
        return DateUtils.toText(this.calendarCreatedAt);
    }

    public String getCalendarUpdatedAtText() {
        return DateUtils.toText(this.calendarUpdatedAt);
    }
}
