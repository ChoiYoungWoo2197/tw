package kr.co.cmtinfo.seal.app.web.controller.admin;

import kr.co.cmtinfo.seal.app.web.model.dto.EnvironmentVariableGroupCreateDto;
import kr.co.cmtinfo.seal.app.web.model.dto.EnvironmentVariableGroupDto;
import kr.co.cmtinfo.seal.app.web.model.dto.EnvironmentVariableGroupUpdateDto;
import kr.co.cmtinfo.seal.core.web.Filterable;
import kr.co.cmtinfo.seal.core.dto.DtoUtils;
import kr.co.cmtinfo.seal.core.web.redirect.RedirectViewWithValidationResult;
import kr.co.cmtinfo.seal.domain.environmentvariable.entity.EnvironmentVariableGroup;
import kr.co.cmtinfo.seal.domain.environmentvariable.service.EnvironmentVariableGroupService;
import org.modelmapper.ModelMapper;
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

/**
 * @author Yongsu Son
 */
@Controller
@RequestMapping("/admin/environment-variable-group")
public class EnvironmentVariableGroupController {

    private final EnvironmentVariableGroupService environmentVariableGroupService;

    public EnvironmentVariableGroupController(EnvironmentVariableGroupService environmentVariableGroupService) {
        this.environmentVariableGroupService = environmentVariableGroupService;
    }

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * <p>환경변수 그룹 목록 페이지</p>
     * @param pageRequest 페이지네이션 DTO
     * @param model view model
     * @return view 파일 경로
     */
    @GetMapping
    String index(Pageable pageRequest, Filterable filterRequest, Model model) {

        Page<EnvironmentVariableGroupDto> environmentVariableGroupShowDtoPage
                = environmentVariableGroupService.findAll(filterRequest, pageRequest)
                .map((entity) -> modelMapper.map(entity, EnvironmentVariableGroupDto.class));

        model.addAttribute("pages", environmentVariableGroupShowDtoPage);

        return "admin/environment_variable_group/index";
    }

    /**
     * <p>환경변수 그룹 생성 페이지</p>
     * @param model view model
     * @return view 파일 경로
     */
    @GetMapping("create")
    public String create(Model model) {

        if (!model.containsAttribute(DtoUtils.nameOf(EnvironmentVariableGroupCreateDto.class))) {
            model.addAttribute(new EnvironmentVariableGroupCreateDto());
        }

        return "admin/environment_variable_group/create";
    }

    /**
     * <p>생성 처리</p>
     * @param environmentVariableGroupCreateDto 환경변수 그룹 생성 DTO
     * @param bindingResult
     * @param redirectAttributes
     * @return {@link RedirectView}
     */
    @PostMapping
    public RedirectView store(@ModelAttribute @Valid EnvironmentVariableGroupCreateDto environmentVariableGroupCreateDto,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return new RedirectViewWithValidationResult<EnvironmentVariableGroupCreateDto>(
                    "admin.EnvironmentVariableGroup#create", bindingResult, environmentVariableGroupCreateDto, redirectAttributes).toRedirectView();
        }

        EnvironmentVariableGroup environmentVariableGroup
                = environmentVariableGroupService.create(environmentVariableGroupCreateDto.newEntity(EnvironmentVariableGroup.class));

        return new RedirectView(
                MvcUriComponentsBuilder.fromMappingName("admin.EnvironmentVariableGroup#show").arg(0, environmentVariableGroup.getId()).build());
    }

    /**
     * <p>보기 페이지</p>
     * @param environmentVariableGroupId 환경변수 그룹 ID
     * @param model
     * @return view 파일 경로
     */
    @GetMapping("{environmentVariableGroupId}")
    String show(@PathVariable Long environmentVariableGroupId, Model model) {
        EnvironmentVariableGroup environmentVariableGroup = environmentVariableGroupService.findById(environmentVariableGroupId);

        model.addAttribute("environmentVariableGroup", new EnvironmentVariableGroupDto(environmentVariableGroup));
        return "admin/environment_variable_group/show";
    }

    /**
     * <p>수정 페이지</p>
     * @param environmentVariableGroupId 환경변수 그룹 ID
     * @param model
     * @return view 파일 경로
     */
    @GetMapping("{environmentVariableGroupId}/edit")
    String edit(@PathVariable Long environmentVariableGroupId, Model model) {

        if ( ! model.containsAttribute(DtoUtils.nameOf(EnvironmentVariableGroupUpdateDto.class))) {
            EnvironmentVariableGroup environmentVariableGroup
                    = environmentVariableGroupService.findById(environmentVariableGroupId);
            model.addAttribute(new EnvironmentVariableGroupUpdateDto(environmentVariableGroup));
        }

        return "admin/environment_variable_group/edit";
    }

    /**
     * <p>수정 처리</p>
     * @param environmentVariableGroupId 환경변수 그룹 ID
     * @param environmentVariableGroupUpdateDto 환경변수 그룹 수정 DTO
     * @param bindingResult
     * @param redirectAttributes
     * @return {@link RedirectView}
     */
    @PutMapping("{environmentVariableGroupId}")
    RedirectView update(@PathVariable Long environmentVariableGroupId,
                        @ModelAttribute @Valid EnvironmentVariableGroupUpdateDto environmentVariableGroupUpdateDto,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            RedirectViewWithValidationResult redirectViewWithValidationResult
                    = new RedirectViewWithValidationResult<EnvironmentVariableGroupUpdateDto>("admin.EnvironmentVariableGroup#edit", bindingResult, environmentVariableGroupUpdateDto, redirectAttributes);
            redirectViewWithValidationResult.addMappingArg(0, String.valueOf(environmentVariableGroupId));
            return redirectViewWithValidationResult.toRedirectView();
        }

        EnvironmentVariableGroup environmentVariableGroup = environmentVariableGroupService.findById(environmentVariableGroupId);
        environmentVariableGroupService.update(environmentVariableGroupUpdateDto.mergeToEntity(environmentVariableGroup));

        return new RedirectView("/admin/environment-variable-group/" + environmentVariableGroupId);
    }

    /**
     * <p>삭제 처리</p>
     * @param environmentVariableGroupId 환경변수 그룹 ID
     * @return {@link RedirectView}
     */
    @DeleteMapping("{environmentVariableGroupId}")
    RedirectView destroy(@PathVariable Long environmentVariableGroupId) {
        if (environmentVariableGroupService.existsById(environmentVariableGroupId)) {
            environmentVariableGroupService.deleteById(environmentVariableGroupId);
        }
        return new RedirectView(MvcUriComponentsBuilder.fromMappingName("admin.EnvironmentVariableGroup#index").build());
    }

    /**
     * <p>그룹에 포함되어 있는 환경변수 목록 페이지
     * 실제 로직이 있는 {@link EnvironmentVariableController#index} 곳으로 forward 한다.</p>
     * @param environmentVariableGroupId
     * @return
     */
    @GetMapping("{environmentVariableGroupId}/environment-variable")
    String environmentVariableIndex(@PathVariable Long environmentVariableGroupId) {
        return "forward:/" + MvcUriComponentsBuilder.fromMappingName("admin.EnvironmentVariable#index").build() + "?groupId=" + environmentVariableGroupId;
    }

}