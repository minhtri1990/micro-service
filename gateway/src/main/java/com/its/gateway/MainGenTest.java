package com.its.gateway;

import com.viettel.GenControllerTest;
import com.viettel.GenDTOTest;
import com.viettel.GenServiceTest;

import java.io.IOException;

public class MainGenTest {
    public static void main(String[] args) throws NoSuchMethodException,
            IOException, ClassNotFoundException {
        // gọi các hàm gen test ở đây
        GenDTOTest.genAllInModule(null,null);
        GenControllerTest.genAllInModule(null,null);
        GenServiceTest.genAllInModule(null,null);
    }
}
