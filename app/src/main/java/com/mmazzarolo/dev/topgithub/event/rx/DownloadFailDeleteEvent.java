package com.mmazzarolo.dev.topgithub.event.rx;


import com.mmazzarolo.dev.topgithub.model.Repo;

public class DownloadFailDeleteEvent {

    public Repo deleteRepo;

    public DownloadFailDeleteEvent(Repo deleteRepo) {
        this.deleteRepo = deleteRepo;
    }
}

