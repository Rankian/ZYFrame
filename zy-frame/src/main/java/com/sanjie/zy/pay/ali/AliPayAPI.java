package com.sanjie.zy.pay.ali;

/**
 * 支付宝支付API
 *
 * 使用:
 *
 * AliPayAPI.getInstance().apply(config).sendPayReq(aliPayReq);
 *
 * Created by LangSanJie on 2017/3/13.
 */

public class AliPayAPI {

    private Config mConfig;

    private static final Object mLoak = new Object();
    private static AliPayAPI instance;

    public static AliPayAPI getInstance(){
        if(instance == null){
            synchronized (mLoak){
                if(instance == null){
                    instance = new AliPayAPI();
                }
            }
        }
        return instance;
    }

    /**
     * 配置支付宝配置
     * @param config
     * @return
     */
    public AliPayAPI apply(Config config){
        this.mConfig = config;
        return this;
    }

    /**
     * 发送支付宝支付请求
     * @param aliPayReq
     */
    public void sendPayReq(AliPayReq aliPayReq){
        aliPayReq.send();
    }

    public static class Config{
        //ali pay config
        // 商户私钥，pkcs8格式
        private String aliRsaPrivate;
        // 支付宝公钥
        private String aliRsaPublic;
        // 商户PID
        // 签约合作者身份ID
        private String partner;
        // 商户收款账号
        // 签约卖家支付宝账号
        private String seller;

        public String getAliRsaPrivate() {
            return aliRsaPrivate;
        }

        public void setAliRsaPrivate(String aliRsaPrivate) {
            this.aliRsaPrivate = aliRsaPrivate;
        }

        public String getAliRsaPublic() {
            return aliRsaPublic;
        }

        public void setAliRsaPublic(String aliRsaPublic) {
            this.aliRsaPublic = aliRsaPublic;
        }

        public String getPartner() {
            return partner;
        }

        public void setPartner(String partner) {
            this.partner = partner;
        }

        public String getSeller() {
            return seller;
        }

        public void setSeller(String seller) {
            this.seller = seller;
        }

        public static class Builder{
            //ali pay config
            // 商户私钥，pkcs8格式
            private String aliRsaPrivate;
            // 支付宝公钥
            private String aliRsaPublic;
            // 商户PID
            // 签约合作者身份ID
            private String partner;
            // 商户收款账号
            // 签约卖家支付宝账号
            private String seller;

            public Builder() {
                super();
            }

            public Builder setRsaPrivate(String aliRsaPrivate){
                this.aliRsaPrivate = aliRsaPrivate;
                return this;
            }

            public Builder setRsaPublic(String aliRsaPublic){
                this.aliRsaPublic = aliRsaPublic;
                return this;
            }

            public Builder setPartner(String partner){
                this.partner = partner;
                return this;
            }

            public Builder setSeller(String seller){
                this.seller = seller;
                return this;
            }

            public Config create(){
                Config conf = new Config();
                conf.aliRsaPrivate = this.aliRsaPrivate;
                conf.aliRsaPublic = this.aliRsaPublic;
                conf.partner = this.partner;
                conf.seller = this.seller;
                return conf;
            }
        }
    }
}
