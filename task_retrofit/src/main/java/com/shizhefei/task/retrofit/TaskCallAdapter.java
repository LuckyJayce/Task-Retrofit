/*
 * Copyright (C) 2016 Jake Wharton
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
package com.shizhefei.task.retrofit;

import com.shizhefei.task.IAsyncTask;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;

final class TaskCallAdapter<R> implements CallAdapter<R, Object> {
    private final Type responseType;
    private final boolean isResult;
    private final boolean isBody;

    TaskCallAdapter(Type responseType, boolean isResult, boolean isBody) {
        this.responseType = responseType;
        this.isResult = isResult;
        this.isBody = isBody;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public Object adapt(Call<R> call) {
        IAsyncTask<Response<R>> responseTask = new ResponseTask<>(call);
        IAsyncTask<?> observable;
        if (isResult) {
            observable = new ResultTask<>(responseTask);
        } else if (isBody) {
            observable = new BodyTask<>(responseTask);
        } else {
            observable = responseTask;
        }
        return observable;
    }
}