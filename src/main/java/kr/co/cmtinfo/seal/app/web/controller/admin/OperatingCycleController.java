package kr.co.cmtinfo.seal.app.web.controller.admin;

import kr.co.cmtinfo.seal.app.web.model.dto.OperatingCycleCreateDto;
import kr.co.cmtinfo.seal.app.web.model.dto.OperatingCycleDto;
import kr.co.cmtinfo.seal.app.web.model.dto.OperatingCycleUpdateDto;
import kr.co.cmtinfo.seal.core.dto.DtoUtils;
import kr.co.cmtinfo.seal.core.web.Filterable;
import kr.co.cmtinfo.seal.core.web.redirect.RedirectViewWithValidationResult;
import kr.co.cmtinfo.seal.domain.operatingcycle.entity.OperatingCycle;
import kr.co.cmtinfo.seal.domain.operatingcycle.service.OperatingCycleService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author Byeon Jiyoung
 */
@Controller
@RequestMapping("/admin/operating-cycle")
public class OperatingCycleController {

	private final OperatingCycleService operatingCycleService;

	public OperatingCycleController(OperatingCycleService operatingCycleService) {
		this.operatingCycleService = operatingCycleService;
	}

	private final ModelMapper modelMapper = new ModelMapper();

	private final String uploadPath = "C:" + File.separator + "upload";

	/**
	 * <p>운영주기 목록 페이지</p>
	 * @param pageRequest
	 * @param filterRequest
	 * @param model
	 * @return
	 */
	@GetMapping
	public String index(Pageable pageRequest, Filterable filterRequest, Model model) {

		Page<OperatingCycleDto> operatingCycleDtoPage
				= operatingCycleService.findAll(filterRequest, pageRequest)
				.map((entity) -> modelMapper.map(entity, OperatingCycleDto.class));

		model.addAttribute("pages", operatingCycleDtoPage);

		return "admin/operating_cycle/index";
	}

	/**
	 * <p>운영주기 생성 페이지</p>
	 * @param model
	 * @return
	 */
	@GetMapping("create")
	public String create(Model model) {

		if(!model.containsAttribute(DtoUtils.nameOf(OperatingCycleCreateDto.class))) {
			model.addAttribute(new OperatingCycleCreateDto());
		}

		return "admin/operating_cycle/create";
	}

	/**
	 * <p>운영주기 생성처리</p>
	 * @param operatingCycleCreateDto
	 * @param bindingResult
	 * @param redirectAttributes
	 * @return {@link RedirectView}
	 */
	@PostMapping
	public RedirectView store(@ModelAttribute @Valid OperatingCycleCreateDto operatingCycleCreateDto,
							  BindingResult bindingResult, RedirectAttributes redirectAttributes,
							  MultipartHttpServletRequest multiRequest) throws IOException {

		List<MultipartFile> fileList = multiRequest.getFiles("file");

		File newFile = new File(uploadPath);

		if(!newFile.exists()) {
			newFile.mkdir();
		}

		for(MultipartFile multipartFile : fileList) {
			String fileName = multipartFile.getOriginalFilename();
			long fileSize = multipartFile.getSize();

			String saveName = UUID.randomUUID().toString() + "_" + fileName + "_" + fileSize;

			multipartFile.transferTo(new File(uploadPath + File.separator + saveName));
		}

		if(bindingResult.hasErrors()) {
			return new RedirectViewWithValidationResult<OperatingCycleCreateDto>(
					"admin.OperatingCycle#create", bindingResult, operatingCycleCreateDto, redirectAttributes).toRedirectView();
		}

		OperatingCycle operatingCycle
				= operatingCycleService.create(operatingCycleCreateDto.newEntity(OperatingCycle.class));

		return new RedirectView(MvcUriComponentsBuilder.fromMappingName("admin.OperatingCycle#show").arg(0, operatingCycle.getId()).build());
	}

	/**
	 * <p>운영주기 보기 페이지</p>
	 * @param operatingCycleId
	 * @param model
	 * @return
	 */
	@GetMapping("{operatingCycleId}")
	public String show(@PathVariable Long operatingCycleId, Model model) {

		OperatingCycle operatingCycle = operatingCycleService.findById(operatingCycleId);

		model.addAttribute("operatingCycle", new OperatingCycleDto(operatingCycle));

		return "admin/operating_cycle/show";
	}

	/**
	 * <p>운영주기 수정 페이지</p>
	 * @param operatingCycleId
	 * @param model
	 * @return
	 */
	@GetMapping("{operatingCycleId}/edit")
	public String edit(@PathVariable Long operatingCycleId, Model model) {

		if(!model.containsAttribute(DtoUtils.nameOf(OperatingCycleUpdateDto.class))) {
			OperatingCycle operatingCycle = operatingCycleService.findById(operatingCycleId);
			model.addAttribute(new OperatingCycleUpdateDto(operatingCycle));
		}

		return "admin/operating_cycle/edit";
	}

	/**
	 * <p>운영주기 수정처리</p>
	 * @param operatingCycleId
	 * @param operatingCycleUpdateDto
	 * @param bindingResult
	 * @param redirectAttributes
	 * @return
	 */
	@PutMapping("{operatingCycleId}")
	public RedirectView update (@PathVariable Long operatingCycleId,
								@ModelAttribute @Valid OperatingCycleUpdateDto operatingCycleUpdateDto,
								BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		if(bindingResult.hasErrors()) {
			RedirectViewWithValidationResult redirectViewWithValidationResult
					= new RedirectViewWithValidationResult<OperatingCycleUpdateDto>("admin.OperatingCycle#edit", bindingResult, operatingCycleUpdateDto, redirectAttributes);
			redirectViewWithValidationResult.addMappingArg(0, String.valueOf(operatingCycleId));
			return redirectViewWithValidationResult.toRedirectView();
		}

		OperatingCycle operatingCycle = operatingCycleService.findById(operatingCycleId);
		operatingCycleService.update(operatingCycleUpdateDto.mergeToEntity(operatingCycle));

		return new RedirectView(MvcUriComponentsBuilder.fromMappingName("admin.OperatingCycle#show").arg(0, operatingCycle.getId()).build());
	}

	/**
	 * <p>운영주기 삭제처리</p>
	 * @param operatingCycleId
	 * @return
	 */
	@DeleteMapping("{operatingCycleId}")
	public RedirectView destroy(@PathVariable Long operatingCycleId) {

		if(operatingCycleService.existById(operatingCycleId)) {
			operatingCycleService.deleteById(operatingCycleId);
		}

		return new RedirectView(MvcUriComponentsBuilder.fromMappingName("admin.OperatingCycle#index").build());
	}

	@PostMapping("upload")
	@ResponseBody
	public String upload(MultipartHttpServletRequest multiRequest) {

		Iterator<String> iterator = multiRequest.getFileNames();

		File newFile = new File(uploadPath);

		if(!newFile.exists()) {
			newFile.mkdir();
		}

		String result = "success";

		while (iterator.hasNext()) {
			MultipartFile multipartFile = multiRequest.getFile(iterator.next());

			String fileName = multipartFile.getOriginalFilename();
			long fileSize = multipartFile.getSize();

			String saveName = UUID.randomUUID().toString() + "_" + fileName + "_" + fileSize;

			try {
				multipartFile.transferTo(new File(uploadPath + File.separator + saveName));
			} catch (IOException e) {
				e.printStackTrace();
				result = "fail";
			}
		}

		return result;
	}

	@PostMapping("file-delete")
	public void fileDelete(@RequestParam("fileName") String fileName) {

		String deleteFile = uploadPath + File.separator + fileName;

		File file = new File(deleteFile);

		if(file.exists()) {
			file.delete();
		}
	}

//	@PostMapping("upload2")
//	public void upload2(@RequestParam("file") MultipartFile file) throws IOException {
//		String uploadPath = "C:" + File.separator + "upload";
//		Path path = Paths.get(uploadPath + file.getOriginalFilename());
//
//		byte[] bytes = file.getBytes();
//		Files.write(path,bytes);
//	}

}


