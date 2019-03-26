package elema.zengluo.controller;

import elema.zengluo.pojo.Account;
import elema.zengluo.pojo.AccountResult;
import elema.zengluo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luokangtao
 * @create 2019-03-25 10:33
 */
@RestController
@RequestMapping("/accountController")
public class AccountController {

    @Autowired
    AccountService accountService;

    AccountResult accountResult;

    /**
     * 新增收入支出账目
     * @param account
     * @return
     */
    @RequestMapping("/addAccount")
    public AccountResult addAccount(@RequestBody Account account){

        //新增
        Integer integer = accountService.addAccount(account);

        //返回结果集
        if(integer<=0){
            accountResult=new AccountResult(false,"新增失败");
        }else {
            accountResult=new AccountResult(true,"新增成功");
        }

        return accountResult;
    }

    /**
     * 根据主键id修改收入支出账目
     * @param Account
     * @return
     */
    @RequestMapping("/updateAccount")
    public AccountResult updateAccount(@RequestBody Account Account){

        //修改
        Integer integer = accountService.updateAccount(Account);

        //返回结果集
        if(integer<=0){
            accountResult=new AccountResult(false,"修改失败");
        }else {
            accountResult=new AccountResult(true,"修改成功");
        }

        return accountResult;
    }

    /**
     * 根据主键id删除
     * @param id
     * @return
     */
    @RequestMapping("/deleteAccount")
    public AccountResult deleteAccount(Integer id){

        //修改
        Integer integer = accountService.deleteAccount(id);

        //返回结果集
        if(integer<=0){
            accountResult=new AccountResult(false,"删除失败");
        }else {
            accountResult=new AccountResult(true,"删除成功");
        }

        return accountResult;
    }

    /**
     * 分页查询
     * @param pageNumber 当前页
     * @param pageSize  当前页条数
     * @return
     */
    @RequestMapping("/findAccountAll")
    public AccountResult findAccountAll(@RequestBody Account account, Integer pageNumber,Integer pageSize){
        //分页条件查询
         accountResult =accountService.findAccountAll(account,pageNumber,pageSize);
         //返回结果集
         return  accountResult;

    }

    /**
     * 根据主键id查找账目
     * @param id
     * @return
     */
    @RequestMapping("/findAccountOne")
    public AccountResult findAccountOne(Integer id){
        //根据id查询
        Account account = accountService.findAccountOne(id);
        //获取结果集
        accountResult= new AccountResult(true,"查询成功",account);
        //返回结果集
        return accountResult;
    }

}
