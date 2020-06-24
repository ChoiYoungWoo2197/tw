package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.domain.operatingcycle.entity.OperatingCycle;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Byeon Jiyoung
 */
@NoArgsConstructor
public class OperatingCycleCreateDto extends OperatingCycleDto {

    public OperatingCycleCreateDto(OperatingCycle entity) {
        super(entity);
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
