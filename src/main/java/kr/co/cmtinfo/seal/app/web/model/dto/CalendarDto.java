package kr.co.cmtinfo.seal.app.web.model.dto;

import kr.co.cmtinfo.seal.core.dto.ModelMapperDtoEntityConverter;
import kr.co.cmtinfo.seal.core.web.view.annotation.Title;
import kr.co.cmtinfo.seal.domain.calendar.entity.Calendar;
import kr.co.cmtinfo.seal.library.time.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
/**
 * @author Youngwoo Choi
 */
@Getter
@Setter
@NoArgsConstructor
public class CalendarDto
        extends ModelMapperDtoEntityConverter<Calendar> {

    public CalendarDto(Calendar entity) {super(entity);}

    @Title("아이디")
    private Long id;

    @Title("타이틀")
    private String title;

    @Title("코드")
    private String code;

    @Title("설명")
    private String description;

    @Title("생성일")
    private Date createdAt;

    @Title("수정일")
    private Date updatedAt;

    public String getCreatedAtText() {
        return DateUtils.toText(this.createdAt);
    }

    public String getUpdatedAtText() {
        return DateUtils.toText(this.updatedAt);
    }
}
