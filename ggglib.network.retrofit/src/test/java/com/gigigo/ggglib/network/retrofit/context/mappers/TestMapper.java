package com.gigigo.ggglib.network.retrofit.context.mappers;

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.ggglib.network.retrofit.context.responses.ApiDataTestMock;

public class TestMapper implements ExternalClassToModelMapper<ApiDataTestMock, DataTestMock> {

  @Override public DataTestMock externalClassToModel(ApiDataTestMock apiDataTestMock) {
    DataTestMock dataTestMock = new DataTestMock();
    dataTestMock.setTest(apiDataTestMock.getTest());
    return dataTestMock;
  }
}
