package elema.zengluo.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import elema.zengluo.mapper.AccountMapper;
import elema.zengluo.pojo.Account;
import elema.zengluo.pojo.AccountResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author luokangtao
 * @create 2019-03-25 11:00
 */
@Service
public class AccountService {

    Logger logger = Logger.getLogger("AccountService");
    @Resource
    private  AccountMapper accountMapper;

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
     * 修改收入支出账目
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
        if(paymentType.equals("全部")){
            paymentType=null;
        }
        if(deteTime.equals("全部")){
            deteTime=null;
        }
        String deteTime0=null;
        String deteTime1=null;
        String deteTime2=null;
        String deteTime3=null;

        if(deteTime!=null&&deteTime.equals("今天")){
            deteTime0="0";
        }
        if(deteTime!=null&&deteTime.equals("昨天")){
            deteTime1="1";
        }
        if(deteTime!=null&&deteTime.equals("本周")){
            deteTime2="2";
        }
        if(deteTime!=null&&deteTime.equals("本月")){
            deteTime3="3";
        }
        logger.info("====>paymentType:"+paymentType+" <===> deteTime:"+deteTime+"<====");
        //根据条件分页查询
        Page<Account> page =(Page<Account>)accountMapper.findAccountAll(paymentType,deteTime0,deteTime1,deteTime2,deteTime3);
        //查询其他
        paymentType="其他";
        BigDecimal qita = accountMapper.findSum(paymentType,deteTime0,deteTime1,deteTime2,deteTime3);
        if(qita==null){
            qita=BigDecimal.valueOf(0L);
        }
        //查询收入
        paymentType="收入";
        BigDecimal shouru=accountMapper.findSum(paymentType,deteTime0,deteTime1,deteTime2,deteTime3);
        if(shouru==null){
            shouru=BigDecimal.valueOf(0L);
        }
        //查询支出
        paymentType="支出";
        BigDecimal zhichu=accountMapper.findSum(paymentType,deteTime0,deteTime1,deteTime2,deteTime3);
        if(zhichu==null){
            zhichu=BigDecimal.valueOf(0L);
        }
        //获取总页数
        long pageTotal = page.getTotal();
        //获取结果集
        List<Account> result = page.getResult();
        //组装结果集
        AccountResult accountResult = new AccountResult(true, "查询成功",pageTotal,qita,shouru,zhichu,result);
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

    /**
     * 根据id查询图片地址
     * @param id
     * @return
     */
    public String findAccountImgUrl(Integer id) {
        //根据id查询图片
        String url = accountMapper.findAccountImgUrl(id);
        //返回值
        return  url;
    }
}
