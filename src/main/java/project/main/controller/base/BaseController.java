package project.main.controller.base;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

public class BaseController {

    @Value("${app.default_page_size}")
    protected int defaultPageSize;

    @Value("${default_page}")
    protected int defaultPage;

}
