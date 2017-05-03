package com.gigigo.ggglib.network.retrofit.context.mappers;

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.ggglib.network.retrofit.context.responses.ApiResultDataMock;

public class TestMapper implements ExternalClassToModelMapper<ApiResultDataMock, DataTestMock> {

  @Override public DataTestMock externalClassToModel(ApiResultDataMock apiResultDataMock) {
    DataTestMock dataTestMock = new DataTestMock();
    dataTestMock.setTest(apiResultDataMock.getTest());
    return dataTestMock;
  }
}
