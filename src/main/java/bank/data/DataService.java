package bank.data;

import bank.pojo.AccountVO;
import bank.util.JdbcUtil;
import bank.util.ResultConvertToVector;

import java.math.BigDecimal;
import java.util.*;

public class DataService {

    public List<AccountVO> getTableData(Integer account_no){
        JdbcUtil jdbcUtil = new JdbcUtil();
        String sql = "select account_no,account_name,account_password,account_money,account_status,DATE_FORMAT(account_modify_time, '%Y-%m-%d %H:%i:%s') as account_modify_time  from tb_account  ";
//        HashMap hashMap = new HashMap();
        Map<String,List<?>> resultType = new HashMap<>();
        resultType.put("account_no", Arrays.asList("setAccount_no",int.class));
        resultType.put("account_name",Arrays.asList("setAccount_name",String.class));
        resultType.put("account_password",Arrays.asList("setAccount_password",String.class));
        resultType.put("account_money",Arrays.asList("setAccount_money", BigDecimal.class));
        resultType.put("account_status",Arrays.asList("setAccount_status", String.class));
        resultType.put("account_modify_time",Arrays.asList("setAccount_modify_time",String.class));
        if (account_no != null){
            sql+=" where account_no=?";
            ArrayList paramList = new ArrayList();
            paramList.add(account_no);
            List<AccountVO> resultList = jdbcUtil.query(sql,paramList,resultType, AccountVO.class);
            return resultList;
        }else {
            List<AccountVO> resultList = jdbcUtil.query(sql,resultType, AccountVO.class);
            if (resultList != null && resultList.size() > 0){

                return resultList;
            }
            return null;
        }
    }
}
