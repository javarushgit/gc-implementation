package org.example;

import java.util.*;


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





//    List<ApplicationBean> getAplicationsInsideAplication(List<ApplicationBean> prev, List<ApplicationBean> listChain) {
//        List<ApplicationBean> result = new ArrayList<>();
//        for (ApplicationBean applicationBean : prev) {
//            System.out.println(applicationBean.getFieldValues().values());
//            if (listChain.contains(applicationBean)) {listChain = new ArrayList<>(); return result;}
//            listChain.add(applicationBean);//!!!!!!!!!!!!!!1
//            result.addAll(applicationBean.getFieldValues().values());
//            List<ApplicationBean> listCurrentlyLevel = new ArrayList<>(applicationBean.
//                    getFieldValues().values());
//            if (listCurrentlyLevel.size() > 1) result.addAll(getAllAppBean(listCurrentlyLevel));
//        }
//        return result;
//    }






}


