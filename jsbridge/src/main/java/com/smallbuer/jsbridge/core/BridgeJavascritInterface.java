package com.smallbuer.jsbridge.core;

import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import java.util.Map;

/**
 * Created on 2019/7/10.
 * Author: bigwang
 * Description:
 */
public class BridgeJavascritInterface extends BaseJavascriptInterface {

    private IWebView mWebView;
    private BridgeTiny mBridge;

    public BridgeJavascritInterface(Map<String, OnBridgeCallback> callbacks,BridgeTiny bridge ,IWebView webView) {
        super(callbacks);
        this.mWebView = webView;
        this.mBridge = bridge;
    }

    @Override
    public String send(String data) {
        return "it is default response";
    }

    @JavascriptInterface
    public void handler(String handlerName,String data, String callbackId) {
        if(TextUtils.isEmpty(handlerName)){
            return;
        }
        if(mBridge.getMessageHandlers().containsKey(handlerName)){
            BridgeHandler bridgeHandler = mBridge.getMessageHandlers().get(handlerName);
            bridgeHandler.handler(mWebView.getContext(),data, new CallBack(callbackId));
        }
    }

    public class CallBack implements CallBackFunction{
        private String callbackId ;
        public CallBack(String callbackId){
            this.callbackId =  callbackId;
        }
        @Override
        public void onCallBack(String data) {
            mBridge.sendResponse(data,callbackId);
        }
    }

}