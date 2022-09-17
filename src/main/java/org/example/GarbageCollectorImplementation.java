package org.example;


import java.util.*;
import java.util.stream.Collectors;


public class GarbageCollectorImplementation implements GarbageCollector {
  @Override
  public List<ApplicationBean> collect(HeapInfo heap, StackInfo stack) {
    Map<String, ApplicationBean> beans = heap.getBeans();
    Deque<StackInfo.Frame> frames = stack.getStack();


    List<ApplicationBean> listAppBeansFirstLevel = stack.getAllApplicationsFromFrameFirstLevel(frames);
    List<ApplicationBean> listAppBeansWithReferences = stack.getAllAppBean(listAppBeansFirstLevel);

    List<ApplicationBean> result = beans
            .values().stream().filter(el -> !listAppBeansWithReferences.contains(el)).collect(Collectors.toList());



    return result;
  }



}

