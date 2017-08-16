package com.sanjie.zy.pay;

import com.sanjie.zy.pay.ali.AliPayAPI;
import com.sanjie.zy.pay.ali.AliPayReq;
import com.sanjie.zy.pay.wechat.WechatPayAPI;
import com.sanjie.zy.pay.wechat.WechatPayReq;

/**
 * Created by LangSanJie on 2017/3/13.
 */

public class PayAPI {

    private static final Object mLock = new Object();
    private static PayAPI mInstance;

    public static PayAPI getInstance(){
        if(mInstance == null){
            synchronized (mLock){
                if(mInstance == null){
                    mInstance = new PayAPI();
                }
            }
        }
        return mInstance;
    }


    /**
     * 支付宝支付请求
     * @param aliPayRe
     */
    public void sendPayRequest(AliPayReq aliPayRe){
        AliPayAPI.getInstance().sendPayReq(aliPayRe);
    }




    /**
     * 微信支付请求
     * @param wechatPayReq
     */
    public void sendPayRequest(WechatPayReq wechatPayReq){
        WechatPayAPI.getInstance().sendPayReq(wechatPayReq);
    }
}
