package com.im.yuan.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author: YuanJiaMin
 * @date: 2021/4/8 11:45 上午
 * @describe: 会话历史记录模型
 */
public class ConversationHistoryRecordModel implements Serializable {
    private static final long serialVersionUID = 1243958463878946828L;
    /**
     * 主键id
     */
    private Long id;
    /**
     * 会话id：min(from_user_id,to_user_id)+":"+max(from_user_id,to_user_id)
     */
    private String conversationId;
    /**
     * 发送者id
     */
    private Long fromUserId;
    /**
     * 接收者id
     */
    private Long toUserId;
    /**
     * 消息内容
     */
    private String message;
    /**
     * 发送时间
     */
    private Date createTime;
    /**
     * 状态（0有效 1过期）
     */
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
