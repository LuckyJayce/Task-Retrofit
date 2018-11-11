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

import com.shizhefei.mvc.RequestHandle;
import com.shizhefei.mvc.ResponseSender;
import com.shizhefei.task.IAsyncTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

final class ResponseTask<T> implements IAsyncTask<Response<T>> {
    private final Call<T> call;

    ResponseTask(Call<T> originalCall) {
        call = originalCall.clone();
    }

    @Override
    public RequestHandle execute(final ResponseSender<Response<T>> sender) throws Exception {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                sender.sendData(response);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                if (t instanceof Exception) {
                    sender.sendError((Exception) t);
                } else {
                    sender.sendError(new Exception(t));
                }
            }
        });

        return new RequestHandle() {
            @Override
            public void cancle() {
                call.cancel();
            }

            @Override
            public boolean isRunning() {
                return false;
            }
        };
    }
}