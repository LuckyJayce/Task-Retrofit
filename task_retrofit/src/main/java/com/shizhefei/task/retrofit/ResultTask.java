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
import com.shizhefei.task.function.Func3;
import com.shizhefei.task.tasks.ProxyLinkTask;
import com.shizhefei.task.tasks.Tasks;

import retrofit2.Response;

final class ResultTask<T> extends ProxyLinkTask<Result<T>> {
    private final IAsyncTask<Response<T>> upstream;

    ResultTask(IAsyncTask<Response<T>> upstream) {
        this.upstream = upstream;
    }

    @Override
    protected IAsyncTask<Result<T>> getTask() {
        return Tasks.create(upstream).concatMap(new Func3<Code, Exception, Response<T>, IAsyncTask<Result<T>>>() {
            @Override
            public IAsyncTask<Result<T>> call(Code code, Exception e, Response<T> tResponse) throws Exception {
                if (code == Code.SUCCESS) {
                    Result<T> re = Result.response(tResponse);
                    return Tasks.just(re);
                }
                Result<T> error = Result.error(e);
                return Tasks.just(error);
            }
        });
    }
}