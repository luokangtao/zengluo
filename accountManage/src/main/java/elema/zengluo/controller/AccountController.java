package elema.zengluo.controller;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import elema.zengluo.constant.ServiceConstant;
import elema.zengluo.pojo.Account;
import elema.zengluo.pojo.AccountException;
import elema.zengluo.pojo.AccountResult;
import elema.zengluo.service.AccountService;
import elema.zengluo.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author luokangtao
 * @create 2019-03-25 10:33
 */
@RestController
@RequestMapping("/accountController")
public class AccountController {

    @Value("${FILE_IMAGES_URL}")
    private String FILE_IMAGES_URL;

    @Autowired
    AccountService accountService;

    AccountResult accountResult;

    /**
     * 新增收入支出账目
     * @param file 图片
     * @param agent 经办人
     * @param paymentType 支付类型
     * @param remark 备注
     * @param money 钱
     * @return
     */
    @RequestMapping("/addAccount")
    public AccountResult addAccount(MultipartFile file,String agent, String paymentType, String remark, String money){

        //新建一个账目对象
        Account account = new Account();
        //经办人
        if(agent==null||"".equals(agent)){
            throw new AccountException("经办人不能为空");
        }
        account.setAgent(agent);
        //支付类型
        if(paymentType==null&&"".equals(paymentType)){
            throw new AccountException("支付类型不能为空");
        }
        account.setPaymentType(paymentType);
        //备注
        if(remark!=null&&!"".equals(remark)){
            account.setRemark(remark);
        }
        //钱
        if(money==null&&"".equals(money)){
            throw new AccountException("金额不能为空");
        }
        //转换string类型
        BigDecimal decimal = new BigDecimal(money);
        //保留2位小数
        BigDecimal scale = decimal.setScale(2);
        account.setMoney(scale);
        //初始化图片地址
        String imgUrl="";

        //图片
        if(file!=null&&file.getSize()>0){
            //获取源文件名字  比如: xxx.jpg 获取到"."位置
            int lastIndexOf = file.getOriginalFilename().lastIndexOf(".")+1;

            if(lastIndexOf==0){
                //抛出异常
                throw new AccountException("文件格式有误"+file.getOriginalFilename());
            }
            //获取后缀名 过滤文件格式  切割后缀名
            String substring = file.getOriginalFilename().substring(lastIndexOf);

            //字符串比较的过程中忽略大小写 (根据阿里巴巴的规范定义常量格式)
            if(!substring.equalsIgnoreCase(ServiceConstant.IMG_FROMAT.JPG)&&
                    !substring.equalsIgnoreCase(ServiceConstant.IMG_FROMAT.PNG)&&
                    !substring.equalsIgnoreCase(ServiceConstant.IMG_FROMAT.GIF)){
                //抛出异常
                throw new AccountException("上传文件格式只支持jpg,png,gif");
            }
            //获取源文件名称
            String filename = file.getOriginalFilename();
            //自定义的工具类获取随机文件名
            String uuidName = UUIDUtils.getUUIDName(filename);
            //他可以和文件服务器建立联系
            Client client = Client.create();
            //linux存放文件地址+文件名.后缀名
             imgUrl = FILE_IMAGES_URL + uuidName;

            try {
                //使用client和文件服务器建立联系
                WebResource resource = client.resource(imgUrl);
                //上传文件
                resource.put(file.getBytes());
            } catch (IOException e) {
                throw new AccountException("网络超时,上传图片失败!");
            }
            //图片地址
            account.setImgUrl(imgUrl);
        }

        //新增
        Integer integer = accountService.addAccount(account);

        //返回结果集
        if(integer<=0){
            if(!"".equals(imgUrl)){
                //如果为false,删除已经上传linux的图片
                delete(imgUrl);
            }
            accountResult=new AccountResult(false,"新增失败");
        }else {
            accountResult=new AccountResult(true,"新增成功");
        }

        return accountResult;
    }

    /**
     * 删除linux文件
     * @param str 文件地址
     * @return
     */
    public boolean delete(String str){
        //他可以和文件服务器建立联系
        Client client = Client.create();
        //使用client和文件服务器建立联系
        WebResource resource = client.resource(str);
        //删除文件
        resource.delete();
        //返回结果集
        return true;
    }

    /**
     * 修改收入支出账目
     * @param agent 经办人
     * @param paymentType 收入/支出
     * @param remark 备注
     * @param money 金额
     * @return
     */
    @RequestMapping("/updateAccount")
    public AccountResult updateAccount(String agent,String paymentType,String remark,String money,Integer id){
        //校验
        if(agent==null&&"".equals(agent)){
            throw new AccountException("经办人不能为空");
        }
        if(paymentType==null&&"".equals(paymentType)){
            throw new AccountException("支付类型不能为空");
        }
        if(remark==null&&"".equals(remark)){
            throw new AccountException("备注不能为空");
        }
        if(money==null&&"".equals(money)){
            throw new AccountException("金额不能为空");
        }

        //组装修改对象
        Account account = new Account();
        //设置id
        account.setId(id);
        //经办人
        account.setAgent(agent);
        //支出/收入
        account.setPaymentType(paymentType);
        //备注
        account.setRemark(remark);
        //转换string类型
        BigDecimal decimal = new BigDecimal(money);
        //保留2位小数
        BigDecimal scale = decimal.setScale(2);
        //钱
        account.setMoney(scale);
        //修改
        Integer integer = accountService.updateAccount(account);

        //返回结果集
        if(integer<=0){
            accountResult=new AccountResult(false,"修改失败,当天时间的数据才可以修改");
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

        //首先查询他有没上传图片,有的话先删除
        String url = accountService.findAccountImgUrl(id);

        //修改
        Integer integer = accountService.deleteAccount(id);
        //返回结果集
        if(integer<=0){
            accountResult=new AccountResult(false,"删除失败,当天时间的数据才可以删除");
        }else {
            if(url!=null&&!"".equals(url)){
                //删除linux里面的图片
                delete(url);
            }
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
    public AccountResult findAccountAll(Integer pageNumber,Integer pageSize,String paymentType,String deteTime){
        //分页条件查询
         accountResult =accountService.findAccountAll(pageNumber,pageSize,paymentType,deteTime);
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
