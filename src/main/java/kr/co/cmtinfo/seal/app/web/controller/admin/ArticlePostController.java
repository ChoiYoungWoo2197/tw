package kr.co.cmtinfo.seal.app.web.controller.admin;

import kr.co.cmtinfo.seal.app.web.model.dto.ArticlePostCreateDto;
import kr.co.cmtinfo.seal.app.web.model.dto.ArticlePostDto;
import kr.co.cmtinfo.seal.app.web.model.dto.ArticlePostUpdateDto;
import kr.co.cmtinfo.seal.core.dto.DtoUtils;
import kr.co.cmtinfo.seal.core.web.Filterable;
import kr.co.cmtinfo.seal.core.web.redirect.RedirectViewWithValidationResult;
import kr.co.cmtinfo.seal.domain.article.entity.ArticleComment;
import kr.co.cmtinfo.seal.domain.article.entity.ArticleGroup;
import kr.co.cmtinfo.seal.domain.article.entity.ArticlePost;
import kr.co.cmtinfo.seal.domain.article.service.ArticleCommentService;
import kr.co.cmtinfo.seal.domain.article.service.ArticleGroupService;
import kr.co.cmtinfo.seal.domain.article.service.ArticlePostService;
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
import java.util.List;

/**
 * @author jwchang
 */
@Controller
@RequestMapping("/admin/article-post")
public class ArticlePostController {

    private final ArticlePostService articlePostService;

    private final ArticleGroupService articleGroupService;

    private final ArticleCommentService articleCommentService;

    public ArticlePostController(ArticlePostService articlePostService,
                                 ArticleGroupService articleGroupService,
                                 ArticleCommentService articleCommentService) {
        this.articlePostService = articlePostService;
        this.articleGroupService = articleGroupService;
        this.articleCommentService = articleCommentService;
    }

    private final ModelMapper modelMapper = new ModelMapper();

    @ModelAttribute(name = "articleGroup")
    public List<ArticleGroup> articleGroup() {
        return articleGroupService.findAll();
    }

    /**
     * 게시물 목록 페이지
     * //@param groupId 게시판 그룹 ID
     * @param pageRequest 페이지네이션 DTO
     * @param model view model
     * @return view 파일 경로
     */
    @GetMapping
    public String index(Pageable pageRequest, Filterable filterRequest, Model model) {

        Page<ArticlePostDto> articlePostDtoPage = articlePostService.findAll(filterRequest, pageRequest)
                .map((entity) -> modelMapper.map(entity, ArticlePostDto.class));

        model.addAttribute("pages", articlePostDtoPage);

        return "admin/article_post/index";
    }

    /**
     * <p>환경변수 그룹 생성 페이지</p>
     * @param model view model
     * @return view 파일 경로
     */
    @GetMapping("create")
    public String create(@RequestParam(required = false, name = "groupId") Long articlePostGroupId, Model model) {

        if (!model.containsAttribute(DtoUtils.nameOf(ArticlePostCreateDto.class))) {
            model.addAttribute(new ArticlePostCreateDto());
        }

        model.addAttribute("articlePostGroupId", articlePostGroupId);

        return "admin/article_post/create";
    }

    /**
     * <p>생성 처리</p>
     * @param articlePostCreateDto 환경변수 그룹 생성 DTO
     * @param bindingResult
     * @param redirectAttributes
     * @return {@link RedirectView}
     */
    @PostMapping
    public RedirectView store(@ModelAttribute @Valid ArticlePostCreateDto articlePostCreateDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            RedirectViewWithValidationResult redirectViewWithValidationResult
                    = new RedirectViewWithValidationResult<ArticlePostCreateDto>("admin.ArticlePost#create", bindingResult, articlePostCreateDto, redirectAttributes);
            redirectViewWithValidationResult.addUrlQuery("groupId", String.valueOf(articlePostCreateDto.getArticlePostGroupId()));
            return redirectViewWithValidationResult.toRedirectView();
        }

        ArticlePost articlePost = articlePostService.create(articlePostCreateDto.newEntity(ArticlePost.class));

        return new RedirectView(MvcUriComponentsBuilder.fromMappingName("admin.ArticlePost#show").arg(0, articlePost.getId()).build());
    }

    /**
     * <p>보기 페이지</p>
     * @param postId 게시물 ID
     * @param model
     * @return view 파일 경로
     */
    @GetMapping("{postId}")
    public String show(@PathVariable Long postId, Model model) {
        ArticlePost articlePost = articlePostService.findById(postId);

        ArticlePostDto articlePostDto = new ArticlePostDto(articlePost);
        List<ArticleComment> articleCommentPage = articleCommentService.findAll();

        model.addAttribute(articlePostDto);
        model.addAttribute("comments", articleCommentPage);

        return "admin/article_post/show";
    }

    /**
     * <p>수정 페이지</p>
     * @param postId 환경변수 그룹 ID
     * @param model
     * @return view 파일 경로
     */
    @GetMapping("{postId}/edit")
    public String edit(@PathVariable Long postId, Model model) {
        if ( ! model.containsAttribute("articlePostUpdateDto")) {
            model.addAttribute(new ArticlePostUpdateDto(articlePostService.findById(postId)));
        }
        return "admin/article_post/edit";
    }

    /**
     * <p>수정 처리</p>
     * @param postId 환경변수 그룹 ID
     * @param articlePostUpdateDto 환경변수 그룹 수정 DTO
     * @param bindingResult
     * @param redirectAttributes
     * @return {@link RedirectView}
     */
    @PutMapping("{postId}")
    public RedirectView update(@PathVariable Long postId,
                        @ModelAttribute @Valid ArticlePostUpdateDto articlePostUpdateDto,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            RedirectViewWithValidationResult redirectViewWithValidationResult
                    = new RedirectViewWithValidationResult<ArticlePostUpdateDto>("admin.ArticlePost#edit", bindingResult, articlePostUpdateDto, redirectAttributes);
            redirectViewWithValidationResult.addMappingArg(0, String.valueOf(postId));
            return redirectViewWithValidationResult.toRedirectView();
        }

        ArticlePost articlePost = articlePostService.findById(postId);
        articlePostService.update(articlePost);

        return new RedirectView(MvcUriComponentsBuilder.fromMappingName("admin.ArticlePost#show")
                .arg(0, articlePost.getId()).build());
    }

    /**
     * <p>삭제 처리</p>
     * @param postId 환경변수 그룹 ID
     * @return {@link RedirectView}
     */
    @DeleteMapping("{postId}")
    public RedirectView destroy(@PathVariable Long postId) {
        ArticlePost articlePost = articlePostService.findById(postId);

        if(articlePost == null){
            articlePostService.delete(articlePost);
        }

        String path = MvcUriComponentsBuilder.fromMappingName("admin.ArticleGroup#articlePostIndex")
                .arg(0, articlePost.getArticleGroup().getId()).build();

        return new RedirectView(path);
    }

}
