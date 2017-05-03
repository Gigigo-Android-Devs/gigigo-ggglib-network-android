package com.gigigo.ggglib.network.retrofit.context.mappers;

import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;
import com.gigigo.ggglib.network.retrofit.context.responses.ApiResultDataMock;

public class MapperTest implements ExternalClassToModelMapper<ApiResultDataMock, DataMock> {

  @Override public DataMock externalClassToModel(ApiResultDataMock apiResultDataMock) {
    DataMock dataMock = new DataMock();
    dataMock.setTest(apiResultDataMock.getTest());
    return dataMock;
  }
}
