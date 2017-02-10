package com.kinisoftware.simpletodo.repository.model;

import com.kinisoftware.simpletodo.repository.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

@Table(database = MyDatabase.class)
public class Task extends BaseModel implements Serializable {

    @PrimaryKey(autoincrement = true)
    @Column
    int id;

    @Column
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
