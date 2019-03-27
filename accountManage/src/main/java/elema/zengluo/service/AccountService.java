package elema.zengluo.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import elema.zengluo.mapper.AccountMapper;
import elema.zengluo.pojo.Account;
import elema.zengluo.pojo.AccountResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author luokangtao
 * @create 2019-03-25 11:00
 */
@Service
public class AccountService {

    @Resource
    private  AccountMapper accountMapper;
    //时间格式
    private  SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 新增收入支出账单
     * @param account
     * @return
     */
    @Transactional
    public Integer addAccount(Account account) {
        //创建时间
        account.setCreateTime(new Date());
        //新增
        Integer integer = accountMapper.addAccount(account);
        //返回结果集
        return integer;

    }

    /**
     * 根据主键id修改收入支出账目
     * @param account
     * @return
     */
    @Transactional
    public Integer updateAccount(Account account) {
        //修改时间
        account.setUpdateTime(new Date());
        //修改
        Integer integer=accountMapper.updateAccount(account);
        //返回结果集
        return  integer;
    }

    /**
     * 根据主键id删除账目
     * @param id
     * @return
     */
    @Transactional
    public Integer deleteAccount(Integer id) {
        //删除
        Integer integer = accountMapper.deleteAccount(id);
        //返回结果集
        return integer;
    }

    /**
     * 分页查询
     * @param pageNumber
     * @param pageSize
     * @param paymentType
     * @param deteTime
     * @return
     */
    public AccountResult findAccountAll(Integer pageNumber, Integer pageSize, String paymentType, String deteTime) {
        //调用分页插件 当前页 当前页条数
        PageHelper.startPage(pageNumber,pageSize);
        //根据条件分页查询
        Page<Account> page =(Page<Account>)accountMapper.findAccountAll(paymentType,deteTime);
        //获取总页数
        long pageTotal = page.getTotal();
        //获取结果集
        List<Account> result = page.getResult();
        //组装结果集
        AccountResult accountResult = new AccountResult(true, "查询成功", pageTotal, result);
        //返回结果集
        return accountResult;
    }

    /**
     * 根据主键id查找
     * @param id
     * @return
     */
     public Account findAccountOne(Integer id){

        //根据id查找
        Account account = accountMapper.findAccountOne(id);
        //返回结果集
        return account;
     }
}
