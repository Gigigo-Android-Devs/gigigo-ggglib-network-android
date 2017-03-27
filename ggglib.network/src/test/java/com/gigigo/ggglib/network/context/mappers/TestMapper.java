package com.gigigo.ggglib.network.context.mappers;

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.ggglib.network.context.responses.DataTestMock;
import com.gigigo.ggglib.network.context.responses.ApiDataTestMock;

public class TestMapper implements ExternalClassToModelMapper<ApiDataTestMock, DataTestMock> {

  @Override public DataTestMock externalClassToModel(ApiDataTestMock apiDataTestMock) {
    DataTestMock dataTestMock = new DataTestMock();
    dataTestMock.setTest(apiDataTestMock.getTest());
    return dataTestMock;
  }
}
