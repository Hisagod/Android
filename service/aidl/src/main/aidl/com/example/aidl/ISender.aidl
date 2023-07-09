package com.example.aidl;

import com.example.aidl.IReceiver;
import com.example.aidl.IBean;

interface ISender {
    void registerCallback(IReceiver cb);
    void unRegisterCallback(IReceiver cb);

    //处理Client请求
    void onClientRequest(in SenderBean bean);
}
