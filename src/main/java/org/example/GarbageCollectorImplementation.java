package org.example;


import java.util.*;
import java.util.stream.Collectors;


public class GarbageCollectorImplementation implements GarbageCollector {
    @Override
    public List<ApplicationBean> collect(HeapInfo heap, StackInfo stack) {
        Map<String, ApplicationBean> beans = heap.getBeans();
        Deque<StackInfo.Frame> frames = stack.getStack();


        List<ApplicationBean> listAppBeansFirstLevel = stack.getAllApplicationsFromFrameFirstLevel(frames);


        List<ApplicationBean> listChain = new ArrayList<>();
        List<ApplicationBean> listAppBeansWithReferences = getAllAppBean(listAppBeansFirstLevel, new ArrayList<>());//////////////////////

        List<ApplicationBean> result = beans
                .values().stream().filter(el -> !listAppBeansWithReferences.contains(el)).collect(Collectors.toList());


        return result;
    }

    List<ApplicationBean> getAllAppBean(List<ApplicationBean> prev, List<ApplicationBean> chain) {
        List<ApplicationBean> result = new ArrayList<>();
        for (int i = 0; i < prev.size(); i++) {
            ApplicationBean applicationBean = prev.get(i);
            if (chain.contains(applicationBean)) {
                return result;
            }
            chain.add(applicationBean);
            result.add(applicationBean);
            List<ApplicationBean> listCurrently = new ArrayList<>(applicationBean.getFieldValues().values());
            if (listCurrently.isEmpty()) {
                continue;
            } else {
                result.addAll(getAllAppBean(listCurrently, chain));
            }

        }


        return result;


    }


//    List<ApplicationBean> getApplicationsForApplication(List<ApplicationBean> list, List<ApplicationBean> chain) {
//
//        List<ApplicationBean>
//    }


}

