package com.mmazzarolo.dev.topgithub.model;

import com.google.auto.value.AutoValue;
import com.mmazzarolo.dev.topgithub.db.DbRepoModel;
import com.squareup.sqldelight.RowMapper;

/**
 * Created by Arison on 2017/1/9.
 */
@AutoValue
public abstract class DbRepo implements DbRepoModel {

    public static final Factory<DbRepo> FACTORY =
            new Factory<>((Creator<DbRepo>) (_id, name, last_modify, absolute_path, net_url, is_folder, download_id, factor, is_unzip)
                    -> new AutoValue_DbRepo(_id, name, last_modify, absolute_path, net_url, is_folder, download_id, factor, is_unzip));

    public static final RowMapper<DbRepo> FOR_TEAM_MAPPER = FACTORY.select_allMapper();
}
