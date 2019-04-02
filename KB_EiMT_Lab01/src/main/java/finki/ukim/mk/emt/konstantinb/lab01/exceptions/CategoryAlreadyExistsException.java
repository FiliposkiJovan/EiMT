package finki.ukim.mk.emt.konstantinb.lab01.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Konstantin Bogdanoski (konstantin.b@live.com)
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST,reason = "Category Already Exists")
public class CategoryAlreadyExistsException extends RuntimeException {
}
