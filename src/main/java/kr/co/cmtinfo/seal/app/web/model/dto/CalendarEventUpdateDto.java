package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.domain.calendar.entity.CalendarEvent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Youngwoo Choi
 */
@Getter
@Setter
@NoArgsConstructor
public class CalendarEventUpdateDto extends CalendarEventDto{

    public CalendarEventUpdateDto(CalendarEvent entity) {
        super(entity);
    }

    @Override
    public ModelMapper mapping(ModelMapper modelMapper) {
        modelMapper.typeMap(this.getClass(), CalendarEvent.class).addMappings(mapping -> {
            mapping.skip(CalendarEvent::setId);
            mapping.skip(CalendarEvent::setEventCreatedAt);
            mapping.skip(CalendarEvent::setEventUpdatedAt);
        });
        return modelMapper;
    }

    @NotNull
    public Long getId() {
        return id;
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
