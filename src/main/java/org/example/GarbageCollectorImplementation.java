package org.example;


import java.util.*;
import java.util.stream.Collectors;


public class GarbageCollectorImplementation implements GarbageCollector {
    @Override
    public List<ApplicationBean> collect(HeapInfo heap, StackInfo stack) {
        Map<String, ApplicationBean> beans = heap.getBeans();
        Deque<StackInfo.Frame> frames = stack.getStack();

        List<ApplicationBean> listAppBeansFirstLevel = getAllApplicationsFromFrameFirstLevel(frames);
        List<ApplicationBean> listAppBeansWithReferences = getAllAppBean(listAppBeansFirstLevel, new ArrayList<>());
        List<ApplicationBean> result = beans
                .values().stream().filter(el -> !listAppBeansWithReferences.contains(el)).collect(Collectors.toList());
        return result;
    }

    private List<ApplicationBean> getAllApplicationsFromFrameFirstLevel(Deque<StackInfo.Frame> frames) {
        List<ApplicationBean> result = new ArrayList<>();
        frames.forEach(frame -> result.addAll(frame.getParameters()));
        return result;
    }

    private List<ApplicationBean> getAllAppBean(List<ApplicationBean> previousList, List<ApplicationBean> chain) {
        List<ApplicationBean> result = new ArrayList<>();
        for (ApplicationBean applicationBean : previousList) {
            if (chain.contains(applicationBean)) {
                return result;
            }
            chain.add(applicationBean);
            result.add(applicationBean);
            List<ApplicationBean> listCurrently = new ArrayList<>(applicationBean.getFieldValues().values());
            if (!listCurrently.isEmpty()) {
                result.addAll(getAllAppBean(listCurrently, chain));
            }
        }
        return result;
    }
}