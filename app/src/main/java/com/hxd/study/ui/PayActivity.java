package com.hxd.study.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alipay.sdk.app.PayTask;
import com.hxd.study.R;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

/**
 * Created by Cazaea on 2017/7/7.
 */
public class PayActivity extends AppCompatActivity implements View.OnClickListener {

    // 支付方式
    private LinearLayout mAliPay;
    private LinearLayout mWechartPay;

    // 微信支付相关
    private IWXAPI wechartApi;

    private static final int SDK_PAY_FLAG = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        mAliPay = findViewById(R.id.ll_ali_pay);
        mWechartPay = findViewById(R.id.ll_wechart_pay);

        mAliPay.setOnClickListener(this);
        mWechartPay.setOnClickListener(this);
    }

    /**
     * 支付宝支付
     */
    private void aliPay() {

        final String orderInfo = "app_id=2018051860110781&method=alipay.trade.app.pay&format=JSON&charset=utf-8&sign_type=RSA2&timestamp=2019-05-22%2016%3A20%3A41&version=1.0&biz_content=%7B%22body%22%3A%22%E6%BF%A0%E5%AF%93%E6%94%AF%E4%BB%98%22%2C%22subject%22%3A%22%E6%BF%A0%E5%AF%93%E6%94%AF%E4%BB%98%22%2C%22out_trade_no%22%3A%22LD522132412069745%22%2C%22total_amount%22%3A%221.00%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22disable_pay_channels%22%3A%22credit_group%22%2C%22timeout_express%22%3A%221440m%22%7D&notify_url=https%3A%2F%2Fnotify.pingxx.com%2Fnotify%2Fcharges%2Fch_rLibLSf5K4q1GWvPKSnDCmvH&sign=HMeCOjCJkm9m9z2yoqLjwXAvlwldgbSMUjqhYfXr4a0z2FIyqLFh%2FbJpjv5AS7Zn2Kq6PoWL8xZidwQYOr744QhVeWhXaFM6OH0we%2Bo6XX5kF1Hg%2F2X7Y7dciLZkgwFiqfhFsAvenOoBB4vJCRXJOh4J2qLpT5mEhTF5m1dLbyGiKcCkza4eV6WDxckwTur%2Fkdja9QMd5eZQFfApFCFuhlY8uAdA6pPfwGiJ9KvXu9bQGAoW4AK%2BUnJIbTpB0AjvviSx4OgO8fgZVMgE5EloTcJndjk4idzUUd3Tbl2jPvaUapiCz77II%2BpCP8mFpklx3PY8lSA1cjT9VNvVkIn1mQ%3D%3D";

        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    /**
     * 支付宝支付结果回调
     */
    class AliPayResult {
        private String resultStatus;
        private String result;
        private String memo;

        public AliPayResult(Map<String, String> rawResult) {
            if (rawResult == null) {
                return;
            }

            for (String key : rawResult.keySet()) {
                if (TextUtils.equals(key, "resultStatus")) {
                    resultStatus = rawResult.get(key);
                } else if (TextUtils.equals(key, "result")) {
                    result = rawResult.get(key);
                } else if (TextUtils.equals(key, "memo")) {
                    memo = rawResult.get(key);
                }
            }
        }

        @Override
        public String toString() {
            return "resultStatus={" + resultStatus + "};memo={" + memo
                    + "};result={" + result + "}";
        }

        /**
         * @return the resultStatus
         */
        public String getResultStatus() {
            return resultStatus;
        }

        /**
         * @return the memo
         */
        public String getMemo() {
            return memo;
        }

        /**
         * @return the result
         */
        public String getResult() {
            return result;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            if (msg.what == SDK_PAY_FLAG) {
                @SuppressWarnings("unchecked")
                AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
                /*
                 * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    Toast.makeText(PayActivity.this, "支付成功:" + payResult, Toast.LENGTH_SHORT).show();
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Toast.makeText(PayActivity.this, "支付失败:" + payResult, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    /**
     * 微信支付
     */
    private void wechartPay() {
        // 调起支付前,先向微信注册APPID
        wechartApi  = WXAPIFactory.createWXAPI(this, "wxe9f42c22b4f32e67");
        // 调起支付
        PayReq req = new PayReq();
        //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
        req.appId = "wxe9f42c22b4f32e67";
        req.partnerId = "1508227371";
        req.prepayId = "wx2316513599315467a86b1ab92378428500";
        req.nonceStr = "9667b0e9c40af54b2f337118c05c0d36";
        req.timeStamp = "1558601496";
        req.packageValue = "Sign=WXPay";
        req.sign = "F0980E285E0961DF0D41E62550F0E288";
        Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
        wechartApi.sendReq(req);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_ali_pay:
                aliPay();
                break;
            case R.id.ll_wechart_pay:
                wechartPay();
                break;
        }
    }
}