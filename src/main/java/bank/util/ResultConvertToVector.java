package bank.util;

import bank.pojo.AccountVO;

import java.util.List;
import java.util.Vector;

public class ResultConvertToVector {



    public static Vector hashMapListToVector(List<AccountVO> result){
        Vector<Vector> vectors = new Vector();
        for (AccountVO item:result) {
//            vectors.add()
            Vector vector = new Vector();
            vector.add(item.getAccount_no());
            vector.add(item.getAccount_name());
            vector.add(item.getAccount_password());
            vector.add(item.getAccount_money());

            vector.add(item.getAccount_status());
            vector.add(item.getAccount_modify_time());

            vectors.add(vector);
        }
        return vectors;
    }


}
