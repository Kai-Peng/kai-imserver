package im.kai.server.service.user.filter.validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultHeaderValidator implements HeaderValidator {
    @Override
    public boolean verify(String[] headers, String[] extrasHeaders, HttpServletRequest request, HttpServletResponse response) {
        return false;
    }
}
