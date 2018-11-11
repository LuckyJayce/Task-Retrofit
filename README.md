# 说明

该类库用于适配retrofit生成对应的Task

## Gradle导入：

```
compile 'com.shizhefei:task-core:1.0.0'
compile 'com.shizhefei:task-tasks:1.0.0'
compile 'com.shizhefei:task-retrofit-adapter:1.0.0'
compile 'com.squareup.retrofit2:retrofit:2.0.2'
compile 'com.squareup.retrofit2:converter-gson:2.0.2'
```

用法：

```
interface MyService {
    @GET("user/login" )
    IAsyncTask<UserInfo> login(@Query("username") String username, @Query("password") String password);
}
```



```
Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
MyService service = retrofit.create(MyService.class);

IAsyncTask<UserInfo> task = service.login("1111", "ssss");
```