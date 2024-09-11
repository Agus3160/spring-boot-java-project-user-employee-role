package project.main.utils.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBaseDto implements Serializable {
    private int httpStatus;
    private String message;
    private String timeStamp;
}
