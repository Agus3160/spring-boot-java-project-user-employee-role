package project.main.utils.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponseDto extends ResponseBaseDto {
    private Object data;

    public SuccessResponseDto(int httpStatus, String message, Object data) {
        super(httpStatus, message, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        this.data = data;
    }
}