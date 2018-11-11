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

import com.shizhefei.task.Code;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.ProxyTask;
import com.shizhefei.task.function.Func3;
import com.shizhefei.task.tasks.ProxyLinkTask;
import com.shizhefei.task.tasks.Tasks;

import retrofit2.Response;

final class BodyTask<T> extends ProxyLinkTask<T> {
    private final IAsyncTask<Response<T>> upstream;

    BodyTask(IAsyncTask<Response<T>> upstream) {
        this.upstream = upstream;
    }

    @Override
    protected IAsyncTask<T> getTask() {
        return Tasks.create(upstream).concatMap(new Func3<Code, Exception, Response<T>, IAsyncTask<T>>() {
            @Override
            public IAsyncTask<T> call(Code code, Exception e, Response<T> tResponse) throws Exception {
                if (tResponse.isSuccessful()) {
                    return Tasks.just(tResponse.body());
                }
                return null;
            }
        });
    }
}