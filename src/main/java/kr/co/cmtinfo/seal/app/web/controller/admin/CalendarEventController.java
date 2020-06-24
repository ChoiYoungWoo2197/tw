package kr.co.cmtinfo.seal.app.web.controller.admin;


import com.sun.org.apache.xpath.internal.operations.Mod;
import kr.co.cmtinfo.seal.app.web.model.dto.CalendarEventCreateDto;
import kr.co.cmtinfo.seal.app.web.model.dto.CalendarEventDto;
import kr.co.cmtinfo.seal.app.web.model.dto.CalendarEventUpdateDto;
import kr.co.cmtinfo.seal.app.web.model.dto.EnvironmentVariableGroupDto;
import kr.co.cmtinfo.seal.core.web.Filterable;
import kr.co.cmtinfo.seal.domain.calendar.entity.Calendar;
import kr.co.cmtinfo.seal.domain.calendar.entity.CalendarEvent;
import kr.co.cmtinfo.seal.domain.calendar.service.CalendarEventService;
import kr.co.cmtinfo.seal.domain.calendar.service.CalendarService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Youngwoo Choi
 */
@Controller
@RequestMapping("/admin/calendar-event")
public class CalendarEventController {
    private final CalendarEventService calendarEventService;

    private final CalendarService calendarService;

    public CalendarEventController(CalendarEventService calendarEventService,
                                   CalendarService calendarService) {
        this.calendarEventService = calendarEventService;
        this.calendarService = calendarService;
    }

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * <p>캘린더 이벤트 데이타 받기</p>
     * @param calendarId
     * @return list
     */
    @GetMapping("index")
    @ResponseBody
    public List<CalendarEventDto> index(@RequestParam(required = false, name = "calendarId") Long calendarId) {

        Calendar calendar = calendarService.findById(calendarId);
        List<CalendarEventDto> list = new ArrayList<>();
        for (int i =0; i < calendar.getCalendarEvents().size(); i++) {
            CalendarEventDto calendarEventDto = new CalendarEventDto(calendar.getCalendarEvents().get(i));
            list.add(calendarEventDto);
        }
        return list;
    }

    /**
     * <p>캘린더 그룹 목록 페이지</p>
     * @param calendarId
     * @param model
     * @return view 파일 경로
     */
    @GetMapping
    String show(@RequestParam(required = false) Long calendarId, Model model) {
        model.addAttribute("calendarId", calendarId);
        return "admin/calendar_event/show";
    }



    /**
     * <p>캘린더 이벤트 저장</p>
     * @param calendarEventCreateDto
     * @return s
     */
    @PostMapping
    @ResponseBody
    public String store(@RequestBody CalendarEventCreateDto calendarEventCreateDto) {
        calendarEventService.create(calendarEventCreateDto.newEntity(CalendarEvent.class));
        return "s";
    }

    /**
     * <p>캘린더 이벤트 수정</p>
     * @param calendarEventUpdateDto
     * @return s
     */
    @PutMapping
    @ResponseBody
    public String update(@RequestBody CalendarEventUpdateDto calendarEventUpdateDto) {
        CalendarEvent calendarEvent = calendarEventService.findById(calendarEventUpdateDto.getId());

        calendarEventUpdateDto.mergeToEntity(calendarEvent);
        calendarEventService.update(calendarEvent);

        return "s";
    }

    /**
     * <p>캘캘린더 이벤트 삭제</p>
     * @param calendarEventUpdateDto
     * @return s
     */
    @DeleteMapping
    @ResponseBody
    public String destroy(@RequestBody CalendarEventUpdateDto calendarEventUpdateDto) {
        calendarEventService.destroy(calendarEventUpdateDto.getId());
        return "s";
    }
}
