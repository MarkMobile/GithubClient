package com.mmazzarolo.dev.topgithub.event.rx;

public class DownloadRepoMessageEvent {
    public String message;

    public DownloadRepoMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
