package com.sanjie.zy.pay;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.sanjie.zy.pay.ali.AliPayAPI;
import com.sanjie.zy.pay.ali.AliPayReq;
import com.sanjie.zy.pay.wechat.WechatPayReq;

/**
 * Created by LangSanJie on 2017/3/13.
 */

public class PayTestActivity extends AppCompatActivity {

    public void wechatPay(){
        String appid        = "";
        String partnerid    = "";
        String prepayid     = "";
        String noncestr     = "";
        String timestamp    = "";
        String sign         = "";

        WechatPayReq wechatPayReq = new WechatPayReq.Builder()
                .with(this)
                .setAppId(appid)
                .setPartnerId(partnerid)
                .setPrepayId(prepayid)//预支付码
//								.setPackageValue(wechatPayReq.get)//"Sign=WXPay"
                .setNonceStr(noncestr)
                .setTimeStamp(timestamp)//时间戳
                .setSign(sign)//签名
                .create()
                .setOnWechatPayListener(new WechatPayReq.OnWechatPayListener() {
                    @Override
                    public void onPaySuccess(int errorCode) {

                    }

                    @Override
                    public void onPayFailure(int errorCode) {

                    }
                });


        PayAPI.getInstance().sendPayRequest(wechatPayReq);

    }

    public void aliPay(){
        String partner          = "";
        String seller           = "";

        Activity activity       = this;
        String outTradeNo       = "";
        String price            = "";
        String orderSubject     = "";
        String orderBody        = "";
        String callbackUrl      = "";


        String rawAliOrderInfo = new AliPayReq.AliOrderInfo()
                .setPartner(partner) //商户PID || 签约合作者身份ID
                .setSeller(seller)  // 商户收款账号 || 签约卖家支付宝账号
                .setOutTradeNo(outTradeNo) //设置唯一订单号
                .setSubject(orderSubject) //设置订单标题
                .setBody(orderBody) //设置订单内容
                .setPrice(price) //设置订单价格
                .setCallbackUrl(callbackUrl) //设置回调链接
                .createOrderInfo(); //创建支付宝支付订单信息


        //TODO 这里需要从服务器获取用商户私钥签名之后的订单信息
        String signAliOrderInfo = getSignAliOrderInfoFromServer(rawAliOrderInfo);

        AliPayReq aliPayReq = new AliPayReq.Builder()
                .with(activity)//Activity实例
                .setSignedAliPayOrderInfo(signAliOrderInfo)
                .create()//
                .setOnAliPayListener(new AliPayReq.OnAliPayListener() {
                    @Override
                    public void onPaySuccess(String resultInfo) {

                    }

                    @Override
                    public void onPayFailure(String resultInfo) {

                    }

                    @Override
                    public void onPayConfirmimg(String resultInfo) {

                    }

                    @Override
                    public void onPayCheck(String status) {

                    }
                });//
        AliPayAPI.getInstance().sendPayReq(aliPayReq);

        PayAPI.getInstance().sendPayRequest(aliPayReq);
    }

    /**
     * 获取签名后的支付宝订单信息  (用商户私钥RSA加密之后的订单信息)
     * @param rawAliOrderInfo
     * @return
     */
    private String getSignAliOrderInfoFromServer(String rawAliOrderInfo) {
        return null;
    }
}
