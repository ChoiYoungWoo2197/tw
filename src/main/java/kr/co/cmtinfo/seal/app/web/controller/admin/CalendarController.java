package kr.co.cmtinfo.seal.app.web.controller.admin;

import kr.co.cmtinfo.seal.app.web.model.dto.CalendarCreateDto;
import kr.co.cmtinfo.seal.app.web.model.dto.CalendarDto;
import kr.co.cmtinfo.seal.app.web.model.dto.CalendarUpdateDto;
import kr.co.cmtinfo.seal.core.web.Filterable;
import kr.co.cmtinfo.seal.core.dto.DtoUtils;
import kr.co.cmtinfo.seal.core.web.redirect.RedirectViewWithValidationResult;
import kr.co.cmtinfo.seal.domain.calendar.entity.Calendar;
import kr.co.cmtinfo.seal.domain.calendar.service.CalendarService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.List;

/**
 * @author Youngwoo Choi
 */
@Controller
@RequestMapping("/admin/calendars")
public class CalendarController {
    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * <p>캘린더 그룹 목록 페이지</p>
     * @param paginationDto 페이지네이션 DTO
     * @param model view model
     * @return view 파일 경로
     */
    @GetMapping
    String index(Pageable pageRequest, Filterable filterRequest, Model model) {

        Page<CalendarDto> calendarShowDtoPage
                = calendarService.findAll(filterRequest, pageRequest)
                .map((entity) -> modelMapper.map(entity, CalendarDto.class));

/*        for (int i=0; i< calendarShowDtoPage.getContent().size(); i++) {
            String text = calendarService.deleteTag(calendarShowDtoPage.getContent().get(i).getDescription());
            calendarShowDtoPage.getContent().get(i).setDescription(text);
        }*/

        model.addAttribute("pages", calendarShowDtoPage);
        return "admin/calendar/index";
    }

    /**
     * <p>캘린더 그룹 생성 페이지</p>
     * @param model view model
     * @return view 파일 경로
     */
    @GetMapping("create")
    public String create(Model model) {

        if (!model.containsAttribute(DtoUtils.nameOf(CalendarCreateDto.class))) {
            model.addAttribute(new CalendarCreateDto());
        }

        return "admin/calendar/create";
    }

    /**
     * <p>생성 처리</p>
     * @param calendarCreateDto 환경변수 그룹 생성 DTO
     * @param bindingResult
     * @param redirectAttributes
     * @return {@link RedirectView}
     */
    @PostMapping
    public RedirectView store(@ModelAttribute @Valid CalendarCreateDto calendarCreateDto,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return new RedirectViewWithValidationResult<CalendarCreateDto>(
                    "admin.Calendar#create", bindingResult, calendarCreateDto, redirectAttributes).toRedirectView();
        }


        Calendar calendar
                = calendarService.create(calendarCreateDto.newEntity(Calendar.class));

        return new RedirectView(
                MvcUriComponentsBuilder.fromMappingName("admin.Calendar#show").arg(0, calendar.getId()).build());
    }



    /**
     * <p>보기 페이지</p>
     * @param calendarId 캘린더 그룹 ID
     * @param model
     * @return view 파일 경로
     */
    @GetMapping("{calendar}")
    String show(@PathVariable Long calendar, Model model) {
        Calendar calendarInfo = calendarService.findById(calendar);
        model.addAttribute("calendar", new CalendarDto(calendarInfo));
        return "admin/calendar/show";
    }

    /**
     * <p>수정 페이지</p>
     * @param calendarId 캘린더 그룹 ID
     * @param model
     * @return view 파일 경로
     */
    @GetMapping("{calendar}/edit")
    String edit(@PathVariable Long calendar, Model model) {

        if ( ! model.containsAttribute(DtoUtils.nameOf(CalendarUpdateDto.class))) {
            Calendar calendarInfo
                    = calendarService.findById(calendar);
            model.addAttribute(new CalendarUpdateDto(calendarInfo));
        }

        return "admin/calendar/edit";
    }

    /**
     * <p>수정 처리</p>
     * @param calendar 캘린더 그룹 ID
     * @param calendarUpdateDto 캘린더 그룹 수정 DTO
     * @param bindingResult
     * @param redirectAttributes
     * @return {@link RedirectView}
     */
    @PutMapping("{calendar}")
    RedirectView update(@PathVariable Long calendar,
                        @ModelAttribute @Valid CalendarUpdateDto calendarUpdateDto,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            RedirectViewWithValidationResult redirectViewWithValidationResult
                    = new RedirectViewWithValidationResult<CalendarUpdateDto>("admin.Calendar#edit", bindingResult, calendarUpdateDto, redirectAttributes);
            redirectViewWithValidationResult.addMappingArg(0, String.valueOf(calendar));
            return redirectViewWithValidationResult.toRedirectView();
        }

        Calendar calendarInfo = calendarService.findById(calendar);
        calendarService.update(calendarUpdateDto.mergeToEntity(calendarInfo));

        return new RedirectView(
                MvcUriComponentsBuilder.fromMappingName("admin.Calendar#show").arg(0, calendar).build());

    }

    /**
     * <p>삭제 처리</p>
     * @param calendar 환경변수 그룹 ID
     * @return {@link RedirectView}
     */
    @DeleteMapping("{calendar}")
    RedirectView destroy(@PathVariable Long calendar) {
        if (calendarService.existsById(calendar)) {
            calendarService.deleteById(calendar);
        }
        return new RedirectView(MvcUriComponentsBuilder.fromMappingName("admin.Calendar#index").build());
    }

    /**
     * <p>이미지 업로드</p>
     * @param model
     */
    @PostMapping(value = "fileupload")
    public void fileUpload(@RequestParam("upload") MultipartFile uploadfile, HttpServletResponse response) throws Exception{
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        //파일 저장
        String path = System.getProperty("user.dir") + "/src/main/resources/static/images/ckeditor/";

         ;//저장경로
        OutputStream out = new FileOutputStream(new File(path + uploadfile.getOriginalFilename()));
        out.write(uploadfile.getBytes());

        //이미지 뿌려주기
        PrintWriter printWriter = response.getWriter();
        printWriter.println("{\"filename\" : \""+uploadfile.getOriginalFilename()+"\", \"uploaded\" : 1, \"url\":\""+"/image/ckeditor/" +  uploadfile.getOriginalFilename()+"\"}");
        printWriter.flush();
    }

    /**
     * <p>그룹에 포함되어 있는 환경변수 목록 페이지
     * 실제 로직이 있는 {@link CalendarEvent#show} 곳으로 forward 한다.</p>
     * @param calendarId
     * @return
     */
    @GetMapping("{calendarId}/calendar-event")
    String calendarEventShow(@PathVariable Long calendarId) {
        return "forward:/" + MvcUriComponentsBuilder.fromMappingName("admin.CalendarEvent#show").build() + "?calendarId=" + calendarId;
    }

}
