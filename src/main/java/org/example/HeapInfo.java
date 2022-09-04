package org.example;

import java.util.Map;


public class HeapInfo {

  private final Map<String, ApplicationBean> beans;

  public HeapInfo(Map<String, ApplicationBean> beans) {
    this.beans = beans;
  }

  public Map<String, ApplicationBean> getBeans() {
    return beans;
  }
}