package project.main.utils.response;

import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailedResponseDto extends ResponseBaseDto{
    private Map<String, String> errors;

    public FailedResponseDto(int httpStatus, String message, String timeStamp, Map<String, String> errors) {
        super(httpStatus, message, timeStamp);
        this.errors = errors;
    }

}
