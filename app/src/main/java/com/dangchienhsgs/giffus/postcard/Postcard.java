package com.dangchienhsgs.giffus.postcard;

import com.dangchienhsgs.giffus.account.Human;

public class Postcard {
    private Cover cover;
    private Inner inner;

    private Human sender;
    private Human receiver;

    public Cover getCover() {
        return cover;
    }

    public Inner getInner() {
        return inner;
    }

    public Human getSender() {
        return sender;
    }

    public Human getReceiver() {
        return receiver;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public void setInner(Inner inner) {
        this.inner = inner;
    }

    public void setSender(Human sender) {
        this.sender = sender;
    }

    public void setReceiver(Human receiver) {
        this.receiver = receiver;
    }
}
