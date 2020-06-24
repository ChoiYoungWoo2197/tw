package kr.co.cmtinfo.seal.app.web.controller.admin;

import kr.co.cmtinfo.seal.app.web.model.dto.EnvironmentVariableCreateDto;
import kr.co.cmtinfo.seal.app.web.model.dto.EnvironmentVariableDto;
import kr.co.cmtinfo.seal.app.web.model.dto.EnvironmentVariableUpdateDto;
import kr.co.cmtinfo.seal.core.exception.ResourceNotFoundException;
import kr.co.cmtinfo.seal.core.dto.DtoUtils;
import kr.co.cmtinfo.seal.core.web.redirect.RedirectViewWithValidationResult;
import kr.co.cmtinfo.seal.domain.environmentvariable.entity.EnvironmentVariable;
import kr.co.cmtinfo.seal.domain.environmentvariable.entity.EnvironmentVariableGroup;
import kr.co.cmtinfo.seal.domain.environmentvariable.service.EnvironmentVariableGroupService;
import kr.co.cmtinfo.seal.domain.environmentvariable.service.EnvironmentVariableService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Yongsu Son
 */
@Controller
@RequestMapping("/admin/environment-variable")
public class EnvironmentVariableController {

    private final EnvironmentVariableService environmentVariableService;

    private final EnvironmentVariableGroupService environmentVariableGroupService;

    public EnvironmentVariableController(EnvironmentVariableService environmentVariableService,
                                         EnvironmentVariableGroupService environmentVariableGroupService) {
        this.environmentVariableService = environmentVariableService;
        this.environmentVariableGroupService = environmentVariableGroupService;
    }

    @ModelAttribute(name = "environmentVariableGroups")
    public List<EnvironmentVariableGroup> environmentVariableGroups() {
        return environmentVariableGroupService.findAll();
    }

    /**
     * <p>환경변수 목록 페이지</p>
     * @param groupId 환경변수 그룹 ID
     * @param pageRequest 페이지네이션 DTO
     * @param model
     * @return view 파일 경로
     */
    @GetMapping
    public String index(@RequestParam(required = false) Long groupId,
                        Pageable pageRequest, Model model) {

        EnvironmentVariableGroup environmentVariableGroup = environmentVariableGroupService.findById(groupId);

        if (environmentVariableGroup == null) {
            throw new ResourceNotFoundException();
        }

        Page<EnvironmentVariable> pages = environmentVariableService.findByEnvironmentVariableGroup(environmentVariableGroup, pageRequest);

        model.addAttribute(environmentVariableGroup);
        model.addAttribute("pages", pages);

        return "/admin/environment_variable/index";
    }

    /**
     * <p>환경변수 생성 페이지</p>
     * @param environmentVariableGroupId 환경변수 그룹 ID
     * @param model
     * @return view 파일 경로
     */
    @GetMapping("create")
    public String create(@RequestParam(required = false, name = "groupId") Long environmentVariableGroupId, Model model) {
        if ( ! model.containsAttribute(DtoUtils.nameOf(EnvironmentVariableCreateDto.class))) {
            model.addAttribute(new EnvironmentVariableCreateDto());
        }

        model.addAttribute("environmentVariableGroupId", environmentVariableGroupId);

        return "/admin/environment_variable/create";
    }

    /**
     * <p>환경변수 생성 처리</p>
     * @param environmentVariableCreateDto 환경변수 생성 DTO
     * @param bindingResult
     * @param redirectAttributes
     * @return {@link RedirectView}
     */
    @PostMapping
    public RedirectView store(@ModelAttribute @Valid EnvironmentVariableCreateDto environmentVariableCreateDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            RedirectViewWithValidationResult redirectViewWithValidationResult
                    = new RedirectViewWithValidationResult<EnvironmentVariableCreateDto>("admin.EnvironmentVariable#create", bindingResult, environmentVariableCreateDto, redirectAttributes);
            redirectViewWithValidationResult.addUrlQuery("groupId", String.valueOf(environmentVariableCreateDto.getEnvironmentVariableGroupId()));
            return redirectViewWithValidationResult.toRedirectView();
        }

        EnvironmentVariable environmentVariable
                = environmentVariableService.create(environmentVariableCreateDto.newEntity(EnvironmentVariable.class));

        return new RedirectView(MvcUriComponentsBuilder.fromMappingName("admin.EnvironmentVariable#show").arg(0, environmentVariable.getId()).build());
    }

    /**
     * <p>환경변수 보기 페이지</p>
     * @param environmentVariableId
     * @param model
     * @return view 파일 경로
     */
    @GetMapping("{environmentVariableId}")
    public String show(@PathVariable Long environmentVariableId, Model model) {
        EnvironmentVariable environmentVariable = environmentVariableService.findById(environmentVariableId);

        EnvironmentVariableDto environmentVariableDto = new EnvironmentVariableDto(environmentVariable);

        model.addAttribute(environmentVariableDto);

        return "/admin/environment_variable/show";
    }

    /**
     * <p>환경변수 수정 페이지</p>
     * @param environmentVariableId
     * @param model
     * @return view 파일 경로
     */
    @GetMapping("{environmentVariableId}/edit")
    public String edit(@PathVariable Long environmentVariableId, Model model) {
        if ( ! model.containsAttribute("environmentVariableUpdateDto")) {
            model.addAttribute(new EnvironmentVariableUpdateDto(environmentVariableService.findById(environmentVariableId)));
        }
        return "/admin/environment_variable/edit";
    }

    /**
     * <p>환경변수 수정 페이지</p>
     * @param environmentVariableId 환경변수 ID
     * @param environmentVariableUpdateDto 환경변수 수정 DTO
     * @param bindingResult
     * @param redirectAttributes
     * @return {@link RedirectView}
     */
    @PutMapping("{environmentVariableId}")
    public RedirectView update(@PathVariable Long environmentVariableId,
                         @ModelAttribute @Valid EnvironmentVariableUpdateDto environmentVariableUpdateDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            RedirectViewWithValidationResult redirectViewWithValidationResult
                    = new RedirectViewWithValidationResult<EnvironmentVariableUpdateDto>("admin.EnvironmentVariable#edit", bindingResult, environmentVariableUpdateDto, redirectAttributes);
            redirectViewWithValidationResult.addMappingArg(0, String.valueOf(environmentVariableId));

            return redirectViewWithValidationResult.toRedirectView();
        }

        EnvironmentVariable environmentVariable = environmentVariableService.findById(environmentVariableId);

        environmentVariableUpdateDto.mergeToEntity(environmentVariable);
        environmentVariableService.update(environmentVariable);

        return new RedirectView(MvcUriComponentsBuilder.fromMappingName("admin.EnvironmentVariable#show")
                .arg(0, environmentVariable.getId()).build());
    }

    /**
     * <p>환경변수 삭제 처리</p>
     * @param environmentVariableId 환경변수 ID
     * @return {@link RedirectView}
     */
    @DeleteMapping("{environmentVariableId}")
    public RedirectView destroy(@PathVariable Long environmentVariableId) {
        EnvironmentVariable environmentVariable = environmentVariableService.findById(environmentVariableId);

        if (environmentVariable == null) {
            environmentVariableService.destroy(environmentVariableId);
        }

        String path = MvcUriComponentsBuilder.fromMappingName("admin.EnvironmentVariableGroup#environmentVariableIndex")
                .arg(0, environmentVariable.getEnvironmentVariableGroup().getId()).build();

        return new RedirectView(path);
    }
}