package com.gigigo.ggglib.network.retrofit.context;

import com.gigigo.ggglib.network.responses.ApiResultResponseMock;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BaseApiClient {

  @POST("{type}/") Call<ApiResultResponseMock> testHttpConnection(@Path("type") String callType);
}
