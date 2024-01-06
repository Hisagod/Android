package com.example.aidl;

import com.example.aidl.ICallback;
import com.example.aidl.IBean;

interface ISender {
    void registerCallback(ICallback cb);
    void unRegisterCallback(ICallback cb);

    //处理Client请求
    void onClientRequest(in SenderBean bean);

    void test(in TestBean bean);
}
