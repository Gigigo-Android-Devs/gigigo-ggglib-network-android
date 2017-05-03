package com.gigigo.ggglib.network.retrofit;

import com.gigigo.ggglib.network.client.NetworkClient;
import com.gigigo.ggglib.network.executors.NetworkExecutor;
import com.gigigo.ggglib.network.responses.ApiGenericResponse;
import com.gigigo.ggglib.network.responses.GitHubErrorResponse;
import com.gigigo.ggglib.network.responses.GitHubResultData;
import com.gigigo.ggglib.network.responses.utils.ResponseUtils;
import com.gigigo.ggglib.network.retrofit.client.RetrofitNetworkClientBuilder;
import com.gigigo.ggglib.network.retrofit.context.GitHubApiClient;
import com.gigigo.ggglib.network.retrofit.context.collaborators.GithubRetryOnErrorPolicyImpl;
import com.gigigo.ggglib.network.retrofit.executors.RetrofitNetworkExecutorBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class GitHubNetworkExecutorTest {

  @Before public void setUp() throws Exception {

  }

  @After public void tearDown() throws Exception {
  }

  /**
   * Measure of time and allocated objects
   *
   * @throws Exception
   */
  @Test public void apiServiceCloneExecutorTest() throws Exception {
    ResponseUtils.printMemoryInform();

    for (int i = 0; i < 1; i++) {
      Thread t = new Thread(new Runnable() {
        @Override public void run() {

          long startTime = System.currentTimeMillis();

          NetworkClient networkClient = new RetrofitNetworkClientBuilder("https://api.github.com",
              GitHubApiClient.class).build();

          NetworkExecutor networkExecutor =
              new RetrofitNetworkExecutorBuilder(networkClient, GitHubErrorResponse.class).retryOnErrorPolicy(
                  new GithubRetryOnErrorPolicyImpl()).build();

          GitHubApiClient apiClient = (GitHubApiClient) networkClient.getApiClient();

          ApiGenericResponse apiGenericResponse = networkExecutor.call(apiClient.getOneUser());

          GitHubResultData githubData = (GitHubResultData) apiGenericResponse.getResult();

          long stopTime = System.currentTimeMillis();
          long elapsedTime = stopTime - startTime;

          assertNotNull(apiGenericResponse);
          assertEquals(apiGenericResponse.getHttpResponse().getHttpStatus(), 200);

          assertNotNull(githubData);
          assertEquals(githubData.getName(), "gigigo-android");
          assertTrue(elapsedTime > 100);

          System.out.println("Time for request -->" + elapsedTime);
        }
      });
      t.start();
    }

    Thread.sleep(3000);

    ResponseUtils.printMemoryInform();
  }
}