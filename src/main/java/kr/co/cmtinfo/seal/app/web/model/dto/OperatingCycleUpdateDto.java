package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.domain.operatingcycle.entity.OperatingCycle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Byeon Jiyoung
 */
@Getter
@Setter
@NoArgsConstructor
public class OperatingCycleUpdateDto extends  OperatingCycleDto {

	public OperatingCycleUpdateDto(OperatingCycle entity) {
		super(entity);
	}

	public ModelMapper mapping(ModelMapper modelMapper) {
		modelMapper.typeMap(this.getClass(), OperatingCycle.class).addMappings(mapping -> {
			mapping.skip(OperatingCycle::setId);
			mapping.skip(OperatingCycle::setCreatedAt);
			mapping.skip(OperatingCycle::setUpdatedAt);
		});
		return modelMapper;
	}

	@NotNull
	@Override
	public Long getId() {
		return super.getId();
	}
	
	@NotBlank
	@Override
	public String getTitle() {
		return super.getTitle();
	}
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Override
	public Date getStartedAt() {
		return super.getStartedAt();
	}
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Override
	public Date getEndedAt() {
		return super.getEndedAt();
	}

}
