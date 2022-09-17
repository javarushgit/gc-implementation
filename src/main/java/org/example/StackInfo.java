package org.example;

import java.util.*;
import java.util.stream.Collectors;


public class StackInfo {
    Deque<Frame> stack = new LinkedList<>();

    public void push(String methodName, ApplicationBean... parameters) {
        stack.push(new Frame(methodName, Arrays.asList(parameters)));
    }

    public Frame pop() {
        return stack.pop();
    }

    public Deque<Frame> getStack() {
        return stack;
    }

    public class Frame {
        String methodName;
        List<ApplicationBean> parameters;

        public String getMethodName() {
            return methodName;
        }

        public List<ApplicationBean> getParameters() {
            return parameters;
        }

        public Frame(String methodName, List<ApplicationBean> parameters) {
            this.methodName = methodName;
            this.parameters = parameters;
        }
    }

    List<ApplicationBean> getAllApplicationsFromFrameFirstLevel(Deque<StackInfo.Frame> frames) {
        List<ApplicationBean> result = new ArrayList<>();
        frames.forEach(el -> result.addAll(el.getParameters()));
        return result;
    }

    List<ApplicationBean> getAllAppBean(List<ApplicationBean> listPreviousLevel) {
        Set<ApplicationBean> set = new HashSet<>();
        for (ApplicationBean applicationBean : listPreviousLevel) {
            List<ApplicationBean> listCurrentlyLevel = applicationBean.
                    getFieldValues().values().stream().filter(Objects::nonNull).collect(Collectors.toList());
            List<ApplicationBean> listNextLevel = null;
            if (listCurrentlyLevel.size() > 0) {
               listNextLevel = getAllAppBean(listCurrentlyLevel);
            }
            if (listNextLevel != null) listCurrentlyLevel.addAll(listNextLevel);
            set.addAll(listCurrentlyLevel);
        }
        set.addAll(listPreviousLevel);

        return  new ArrayList<ApplicationBean>(set);
    }

}


