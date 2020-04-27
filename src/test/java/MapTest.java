
import com.util.MyFileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.util.MyMD5.*;

/**
 * ClassName MapTest
 * Description TODO
 *
 * @author mokuanyuan
 * @version 1.0
 * @date 2020/3/28 15:58
 **/
//@ContextConfiguration(locations={"classpath:spring-mybatis.xml"})
//@RunWith(SpringJUnit4ClassRunner.class)
public class MapTest {

    @Test
    public void test1(){

        String input = "123456";
        System.out.println("MD5加密" + "\n"
                + "明文：" + input + "\n"
                + "无盐密文：" + MD5WithoutSalt(input));
        String saltHash = MD5WithSalt(input);
        System.out.println("带盐密文：" + saltHash);


        System.out.println("===============校验");
        System.out.println("加盐密文——"+MD5(input+getSaltFromHash(saltHash)));
        System.out.println("提取的加盐密文——"+MD5(getHashFromSaltHash(saltHash)));
        System.out.println(MD5(input+getSaltFromHash(saltHash)).equals(getHashFromSaltHash(saltHash)) );


    }

    @Test
    public void test2(){

//        String s1 = MyFileUtil.tomcatRoot;
//        String s2 = MyFileUtil.uploadRoot;
//        System.out.println(s1);
//        System.out.println(s2);

        System.out.println(LocalDateTime.now());
        System.out.println(LocalDate.now());

    }

}
