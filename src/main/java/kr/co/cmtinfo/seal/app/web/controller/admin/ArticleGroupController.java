package kr.co.cmtinfo.seal.app.web.controller.admin;

import kr.co.cmtinfo.seal.app.web.model.dto.ArticleGroupCreateDto;
import kr.co.cmtinfo.seal.app.web.model.dto.ArticleGroupDto;
import kr.co.cmtinfo.seal.app.web.model.dto.ArticleGroupUpdateDto;
import kr.co.cmtinfo.seal.core.web.Filterable;
import kr.co.cmtinfo.seal.core.dto.DtoUtils;
import kr.co.cmtinfo.seal.core.web.redirect.RedirectViewWithValidationResult;
import kr.co.cmtinfo.seal.domain.article.entity.ArticleGroup;
import kr.co.cmtinfo.seal.domain.article.service.ArticleGroupService;
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
 * @author jwchang
 */
@Controller
@RequestMapping("/admin/article-group")
public class ArticleGroupController {

    private final ArticleGroupService articleGroupService;

    public ArticleGroupController(ArticleGroupService articleGroupService) {
        this.articleGroupService = articleGroupService;
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

        Page<ArticleGroupDto> articleGroupShowDtoPage
                = articleGroupService.findAll(filterRequest, pageRequest)
                .map((entity) -> modelMapper.map(entity, ArticleGroupDto.class));

        model.addAttribute("pages", articleGroupShowDtoPage);

        return "admin/article_group/index";
    }

    /**
     * <p>환경변수 그룹 생성 페이지</p>
     * @param model view model
     * @return view 파일 경로
     */
    @GetMapping("create")
    public String create(Model model) {

        if (!model.containsAttribute(DtoUtils.nameOf(ArticleGroupCreateDto.class))) {
            model.addAttribute(new ArticleGroupCreateDto());
        }

        return "admin/article_group/create";
    }

    /**
     * <p>생성 처리</p>
     * @param articleGroupCreateDto 게시판 그룹 생성 DTO
     * @param bindingResult
     * @param redirectAttributes
     * @return {@link RedirectView}
     */
    @PostMapping
    public RedirectView store(@ModelAttribute @Valid ArticleGroupCreateDto articleGroupCreateDto,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return new RedirectViewWithValidationResult<ArticleGroupCreateDto>(
                    "admin.ArticleGroup#create", bindingResult, articleGroupCreateDto, redirectAttributes).toRedirectView();
        }

        ArticleGroup articleGroup
                = articleGroupService.create(articleGroupCreateDto.newEntity(ArticleGroup.class));

        return new RedirectView(
                MvcUriComponentsBuilder.fromMappingName("admin.ArticleGroup#show").arg(0, articleGroup.getId()).build());
    }

    /**
     * <p>보기 페이지</p>
     * @param articleGroupId 환경변수 그룹 ID
     * @param model
     * @return view 파일 경로
     */
    @GetMapping("{articleGroupId}")
    String show(@PathVariable Long articleGroupId, Model model) {
        ArticleGroup articleGroup = articleGroupService.findById(articleGroupId);

        model.addAttribute("articleGroup", new ArticleGroupDto(articleGroup));
        return "admin/article_group/show";
    }

    /**
     * <p>수정 페이지</p>
     * @param articleGroupId 환경변수 그룹 ID
     * @param model
     * @return view 파일 경로
     */
    @GetMapping("{articleGroupId}/edit")
    String edit(@PathVariable Long articleGroupId, Model model) {

        if ( ! model.containsAttribute(DtoUtils.nameOf(ArticleGroupUpdateDto.class))) {
            ArticleGroup articleGroup
                    = articleGroupService.findById(articleGroupId);
            model.addAttribute(new ArticleGroupUpdateDto(articleGroup));
        }

        return "admin/article_group/edit";
    }

    /**
     * <p>수정 처리</p>
     * @param articleGroupId 환경변수 그룹 ID
     * @param articleGroupUpdateDto 환경변수 그룹 수정 DTO
     * @param bindingResult
     * @param redirectAttributes
     * @return {@link RedirectView}
     */
    @PutMapping("{articleGroupId}")
    RedirectView update(@PathVariable Long articleGroupId,
                        @ModelAttribute @Valid ArticleGroupUpdateDto articleGroupUpdateDto,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            RedirectViewWithValidationResult redirectViewWithValidationResult
                    = new RedirectViewWithValidationResult<ArticleGroupUpdateDto>("admin.ArticleGroup#edit", bindingResult, articleGroupUpdateDto, redirectAttributes);
            redirectViewWithValidationResult.addMappingArg(0, String.valueOf(articleGroupId));
            return redirectViewWithValidationResult.toRedirectView();
        }

        ArticleGroup articleGroup = articleGroupService.findById(articleGroupId);
        articleGroupService.update(articleGroupUpdateDto.mergeToEntity(articleGroup));

        return new RedirectView("/admin/article-group/" + articleGroupId);
    }

    /**
     * <p>삭제 처리</p>
     * @param articleGroupId 환경변수 그룹 ID
     * @return {@link RedirectView}
     */
    @DeleteMapping("{articleGroupId}")
    RedirectView destroy(@PathVariable Long articleGroupId) {
        if (articleGroupService.existsById(articleGroupId)) {
            articleGroupService.deleteById(articleGroupId);
        }
        return new RedirectView(MvcUriComponentsBuilder.fromMappingName("admin.ArticleGroup#index").build());
    }

    /**
     * <p>그룹에 포함되어 있는 환경변수 목록 페이지
     * 실제 로직이 있는 {@link ArticlePostController#index} 곳으로 forward 한다.</p>
     * @param articleGroupId
     * @return
     */
    @GetMapping("{articleGroupId}/article-post")
    String articleIndex(@PathVariable Long articleGroupId) {
        return "forward:/" + MvcUriComponentsBuilder.fromMappingName("admin.ArticlePost#index").build() + "?groupId=" + articleGroupId;
    }



}
