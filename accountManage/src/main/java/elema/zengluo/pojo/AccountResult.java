package elema.zengluo.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 封装结果集
 * @author luokangtao
 * @create 2019-03-25 11:47
 */
public class AccountResult implements Serializable {

    //是否成功
    private Boolean success;

    //返回信息
    private String message;

    //实体类
    private Account account;

    //总条数
    private Long pageTotal;

    //集合实体类
    private List<Account> list;

    public AccountResult(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public AccountResult(Boolean success, String message, Account account) {
        this.success = success;
        this.message = message;
        this.account = account;
    }

    public AccountResult(Boolean success, String message, List<Account> list) {
        this.success = success;
        this.message = message;
        this.list = list;
    }

    public AccountResult(Boolean success, String message, Long pageTotal, List<Account> list) {
        this.success = success;
        this.message = message;
        this.pageTotal = pageTotal;
        this.list = list;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Account getAccount() {
        return account;
    }

    public Long getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Long pageTotal) {
        this.pageTotal = pageTotal;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Account> getList() {
        return list;
    }

    public void setList(List<Account> list) {
        this.list = list;
    }
}
