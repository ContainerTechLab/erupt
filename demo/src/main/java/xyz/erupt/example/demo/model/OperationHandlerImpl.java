package xyz.erupt.example.demo.model;

import xyz.erupt.annotation.fun.OperationHandler;
import xyz.erupt.annotation.model.BoolAndReason;

/**
 * Created by liyuepeng on 10/10/18.
 */
public class OperationHandlerImpl implements OperationHandler {
    @Override
    public BoolAndReason exec(Object data, Object param) {
        return new BoolAndReason(false, "2333");
    }
}
