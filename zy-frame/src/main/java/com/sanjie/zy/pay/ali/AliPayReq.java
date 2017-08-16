package com.sanjie.zy.pay.ali;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.sanjie.zy.pay.ali.alipay.PayResult;
import com.sanjie.zy.widget.ZYToast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TimerTask;

/**
 * 集成支付宝支付
 * Created by LangSanJie on 2017/3/13.
 */

public class AliPayReq {

    /**
     * aliPay sdk flag
     */
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private Activity mActivity;

    //未签名的订单信息
    private String rawAliPayOrderInfo;
    //已签名的订单信息
    private String signedAliPayOrderInfo;

    private Handler mHandler;

    public AliPayReq() {
        super();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SDK_PAY_FLAG:
                        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                        /**
                         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            ZYToast.info("支付成功");
                            if (mOnAliPayListener != null)
                                mOnAliPayListener.onPaySuccess(resultInfo);
                        } else if (TextUtils.equals(resultStatus, "8000")) {
                            ZYToast.info("支付结果确认中...");
                            if (mOnAliPayListener != null)
                                mOnAliPayListener.onPayConfirmimg(resultInfo);
                        } else {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            ZYToast.error("支付失败");
                            if (mOnAliPayListener != null)
                                mOnAliPayListener.onPayFailure(resultInfo);
                        }
                        break;
                    case SDK_CHECK_FLAG:
                        ZYToast.info("检查结果为:" + msg.obj);
                        if(mOnAliPayListener != null)
                            mOnAliPayListener.onPayCheck(msg.obj.toString());
                        break;
                }
            }
        };
    }

    /**
     * 发送支付宝支付请求
     */
    public void send() {
        String orderInfo = rawAliPayOrderInfo;
        //做RSA签名之后的订单信息
        String sign = signedAliPayOrderInfo;
        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new TimerTask() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(mActivity);
                Map<String, String> result = alipay.payV2(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        //必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void check() {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(mActivity);
                // 调用查询接口，获取查询结果
                String version = payTask.getVersion();
                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = version;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();
    }

    /**
     * 创建订单信息
     *
     * @param partner     签约合作者身份ID
     * @param seller      签约卖家支付宝账号
     * @param outTradeNo  商户网站唯一订单号
     * @param subject     商品名称
     * @param body        商品详情
     * @param price       商品金额
     * @param callbackUrl 服务器异步通知页面路径
     * @return
     */
    public static String getOrderInfo(String partner, String seller, String outTradeNo, String subject, String body, String price, String callbackUrl) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + partner + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + seller + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + outTradeNo + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
//		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
//				+ "\"";
        orderInfo += "&notify_url=" + "\"" + callbackUrl
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    public static class Builder {
        //上下文
        private Activity activity;

        //未签名的订单信息
        private String rawAliPayOrderInfo;
        //服务器签名成功的订单信息
        private String signedAliPayOrderInfo;

        public Builder() {
            super();
        }

        public Builder with(Activity activity) {
            this.activity = activity;
            return this;
        }

        /**
         * 设置未签名的订单信息
         *
         * @param rawAliPayOrderInfo
         * @return
         */
        public Builder setRawAliPayOrderInfo(String rawAliPayOrderInfo) {
            this.rawAliPayOrderInfo = rawAliPayOrderInfo;
            return this;
        }

        /**
         * 设置服务器签名成功的订单信息
         *
         * @param signedAliPayOrderInfo
         * @return
         */
        public Builder setSignedAliPayOrderInfo(String signedAliPayOrderInfo) {
            this.signedAliPayOrderInfo = signedAliPayOrderInfo;
            return this;
        }

        public AliPayReq create() {
            AliPayReq aliPayReq = new AliPayReq();
            aliPayReq.mActivity = this.activity;
            aliPayReq.rawAliPayOrderInfo = this.rawAliPayOrderInfo;
            aliPayReq.signedAliPayOrderInfo = this.signedAliPayOrderInfo;

            return aliPayReq;
        }

    }


    /**
     * 支付宝支付订单信息的信息类
     * <p>
     * 官方demo是暴露了商户私钥，pkcs8格式的，这是极其不安全的。官方也建议私钥签名订单这一块放到服务器去处理。
     * 所以为了避免商户私钥暴露在客户端，订单的加密过程放到服务器去处理
     */
    public static class AliOrderInfo {
        String partner;
        String seller;
        String outTradeNo;
        String subject;
        String body;
        String price;
        String callbackUrl;

        /**
         * 设置商户
         *
         * @param partner
         * @return
         */
        public AliOrderInfo setPartner(String partner) {
            this.partner = partner;
            return this;
        }

        /**
         * 设置商户账号
         *
         * @param seller
         * @return
         */
        public AliOrderInfo setSeller(String seller) {
            this.seller = seller;
            return this;
        }

        /**
         * 设置唯一订单号
         *
         * @param outTradeNo
         * @return
         */
        public AliOrderInfo setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
            return this;
        }

        /**
         * 设置订单标题
         *
         * @param subject
         * @return
         */
        public AliOrderInfo setSubject(String subject) {
            this.subject = subject;
            return this;
        }

        /**
         * 设置订单详情
         *
         * @param body
         * @return
         */
        public AliOrderInfo setBody(String body) {
            this.body = body;
            return this;
        }

        /**
         * 设置价格
         *
         * @param price
         * @return
         */
        public AliOrderInfo setPrice(String price) {
            this.price = price;
            return this;
        }

        /**
         * 设置请求回调
         *
         * @param callbackUrl
         * @return
         */
        public AliOrderInfo setCallbackUrl(String callbackUrl) {
            this.callbackUrl = callbackUrl;
            return this;
        }

        /**
         * 创建订单详情
         *
         * @return
         */
        public String createOrderInfo() {
//			(String partner, String seller, String outTradeNo, String subject, String body, String price, String callbackUrl) {
            return getOrderInfo(partner, seller, outTradeNo, subject, body, price, callbackUrl);
        }
    }


    //支付宝支付监听
    private OnAliPayListener mOnAliPayListener;

    public AliPayReq setOnAliPayListener(OnAliPayListener onAliPayListener) {
        this.mOnAliPayListener = onAliPayListener;
        return this;
    }

    /**
     * 支付宝支付监听
     *
     * @author Administrator
     */
    public interface OnAliPayListener {
        public void onPaySuccess(String resultInfo);

        public void onPayFailure(String resultInfo);

        public void onPayConfirmimg(String resultInfo);

        public void onPayCheck(String status);
    }
}
