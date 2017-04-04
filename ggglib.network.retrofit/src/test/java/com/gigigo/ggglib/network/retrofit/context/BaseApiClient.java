package com.gigigo.ggglib.network.retrofit.context;

import com.gigigo.ggglib.network.retrofit.context.responses.ApiResponseMock;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BaseApiClient {

  @POST("{type}/") Call<ApiResponseMock> testHttpConnection(@Path("type") String callType);
}
