package elema.zengluo.mapper;

import elema.zengluo.pojo.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author luokangtao
 * @create 2019-03-25 11:01
 */
@Mapper
public interface AccountMapper {

    /**
     * 添加收入/支出账单
     * @param account
     * @return
     */
    Integer addAccount(Account account);

    /**
     * 根据主键id修改收入支出账目
     * @param account
     * @return
     */
    Integer updateAccount(Account account);

    /**
     * 根据主键id删除账目
     * @param id
     * @return
     */
    Integer deleteAccount(Integer id);

    /**
     * 根据条件分页查询
     * @return
     * @param paymentType
     *
     */
    List<Account> findAccountAll(@Param("paymentType") String paymentType,@Param("deteTime0") String deteTime0,@Param("deteTime1") String deteTime1,@Param("deteTime2") String deteTime2,@Param("deteTime3") String deteTime3);

    /**
     * 根据主键id查找对象
     * @param id
     * @return
     */
    Account findAccountOne(Integer id);

    /**
     * 根据id查询图片地址
     * @param id
     * @return
     */
    String findAccountImgUrl(Integer id);

}
