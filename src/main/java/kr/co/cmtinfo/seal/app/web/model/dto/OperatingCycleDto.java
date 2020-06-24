package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.core.dto.ModelMapperDtoEntityConverter;
import kr.co.cmtinfo.seal.core.web.view.annotation.Title;
import kr.co.cmtinfo.seal.domain.operatingcycle.entity.OperatingCycle;
import kr.co.cmtinfo.seal.library.time.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author Byeon Jiyoung
 */
@Getter
@Setter
@NoArgsConstructor
public class OperatingCycleDto extends ModelMapperDtoEntityConverter<OperatingCycle> {

	public OperatingCycleDto(OperatingCycle entity) {
		super(entity);
	}

	@Title("아이디")
	private Long id;

	@Title("제목")
	private String title;

	@Title("설명")
	private String description;

	@Title("시작일")
	private Date startedAt;

	@Title("종료일")
	private Date endedAt;

	@Title("생성일")
	private Date createdAt;

	@Title("수정일")
	private Date updatedAt;

	public String getStartedAtText() {
		return DateUtils.toText(this.startedAt);
	}

	public String getEndedAtText() {
		return DateUtils.toText(this.endedAt);
	}

	public String getCreatedAtText() {
		return DateUtils.toText(this.createdAt);
	}

	public String getUpdatedAtText() {
		return DateUtils.toText(this.updatedAt);
	}

}
