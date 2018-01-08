/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.architecture.blueprints.todoapp.data.source.local;

import android.util.Log;

import com.example.android.architecture.blueprints.todoapp.data.Task;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * The Room Database that contains the Task table.
 */
public class ToDoDatabase implements TasksDao {

    private Realm realm;

    public ToDoDatabase() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public List<Task> getTasks() {
        return realm.where(Task.class).findAll();
    }

    @Override
    public Task getTaskById(String taskId) {
        return realm.where(Task.class).equalTo("mId", taskId).findFirst();
    }

    @Override
    public void insertTask(Task task) {
        realm.beginTransaction();
        realm.copyToRealm(task); // Persist unmanaged objects
        realm.commitTransaction();
    }

    @Override
    public void updateTask(final Task task) {
        Task managedTask = realm.where(Task.class).equalTo("mId", task.getId()).findFirst();
        realm.beginTransaction();
        managedTask.setmTitle(task.getTitle());
        managedTask.setmCompleted(task.isCompleted());
        managedTask.setmDescription(task.getDescription());
        realm.commitTransaction();

    }

    @Override
    public void updateCompleted(final String taskId, final boolean completed) {
        Task managedTask = realm.where(Task.class).equalTo("mId", taskId).findFirst();
        Log.d("TODO:Kushank", managedTask.getTitle());
        realm.beginTransaction();
        managedTask.setmCompleted(completed);
        realm.commitTransaction();

    }

    @Override
    public void deleteTaskById(final String taskId) {
        Task managedTask = realm.where(Task.class).equalTo("mId", taskId).findFirst();
        realm.beginTransaction();
        managedTask.deleteFromRealm();
        realm.commitTransaction();

    }

    @Override
    public void deleteTasks() {
        RealmResults<Task> managedTask = realm.where(Task.class).findAll();
        realm.beginTransaction();
        managedTask.deleteAllFromRealm();
        realm.commitTransaction();

    }

    @Override
    public void deleteCompletedTasks() {
        RealmResults<Task> managedTask = realm.where(Task.class).equalTo("mCompleted", true).findAll();
        realm.beginTransaction();
        managedTask.deleteAllFromRealm();
        realm.commitTransaction();
    }
}
