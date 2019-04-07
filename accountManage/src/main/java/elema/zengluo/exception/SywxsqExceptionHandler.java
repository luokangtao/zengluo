package elema.zengluo.exception;

import elema.zengluo.pojo.AccountResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.InputMismatchException;

/**
 * 异常拦截器
 * @author luokangtao
 * @create 2019-02-19 10:40
 */
@ControllerAdvice //@ControllerAdvice是controller的一个辅助类，最常用的就是作为全局异常处理的切面类
public class SywxsqExceptionHandler implements Serializable {

    private Logger logger = LogManager.getLogger();

    //上传文件超出限制
    @ResponseBody
    @ExceptionHandler(MaxUploadSizeExceededException.class) //拦截到该异常信息
    public AccountResult MaxUploadSizeExceededException(Exception e){
        logger.info("异常信息(单个上传文件不能超过5MB/多个文件上传不能超过10MB)");//异常信息java.lang.IllegalStateException
        return  new AccountResult(false,"单个上传文件不能超过5MB/多个文件上传不能超过10MB");
    }

}
