package com.littlegreens.netty.client.extra.send;

import com.littlegreens.netty.client.extra.WjProtocol;
import com.littlegreens.netty.client.extra.task.BaseTask;

import java.io.UnsupportedEncodingException;

public interface Sen_i {
    public WjProtocol generateWj(BaseTask baseTask) throws UnsupportedEncodingException;
}
