package elema.zengluo.pojo;

/**
 * @author luokangtao
 * @create 2019-03-28 1:14
 */
public class AccountException  extends RuntimeException{

    private String  message;

    //提供无参数的构造方法
    public AccountException(){};

    //提供一个有参数的构造方法，可自动生成
    public AccountException(String message){this.message = message;}

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
