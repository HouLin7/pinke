package com.gome.work.core.model.im;

/**
 * Created by songzhiyang on 2016/11/28.
 */

import java.io.Serializable;

/**
 *
 "birthday": 0,
 "phoneNumber": 18166546204,
 "gender": 0,
 "nickName": "",
 "autograph": "",
 "tokenValidity": 1480925702228,
 "avatar": "",
 "userName": "",
 "region": "",
 "userId": 17,
 "token": "acbeeb48856c4fd88938652b326d95a6"
 */

public class IMLoginBean implements Serializable{
    public int birthday;
    public String phoneNumber = "";
    public int gender;
    public String nickName = "";
    public String autograph = "";
    public long tokenValidity;
    public String avatar = "";
    public String userName = "";
    public String region = "";
    public long userId;
    public String token = "";
}
