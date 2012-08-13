package objects;

import java.util.Arrays;
import java.util.Stack;

public class Thread {
	String eventType;
	String event;
	Scriptable object;
	Object[] wholeScript;
	Object[] script;
	int index;
	Stack<Object[]> stack;
	boolean done = true;
	boolean yield = true;
	long startTime;
	Object temp;
	
	public Thread(Scriptable object, Object[] script) {
		Object[] hat = (Object[]) script[0];
		eventType = (String) hat[0];
		event = (String) hat[1];
		wholeScript = script;
		this.object = object;
		stack = new Stack<Object[]>();
	}
	
	public void start() {
        index = 1;
        stack.clear();
        done = yield = false;
        startTime = System.nanoTime();
        temp = null;
        script = wholeScript;
	}
	
	public void stop() {
        index = 1;
        stack.clear();
        done = yield = true;
        startTime = -1;
        temp = null;
        script = wholeScript;
	}
	
	public void step() {
		if (done) {
		    return;
		}
		yield = false;
		while (!(yield || done)) {
			if (index >= script.length) {
				if (stack.size() == 0) {
					done = true;
				} else {
					popState();
	        	}
			} else {
				evalCommand((Object[]) script[index]);
				index++;
			}
		}
	}
	
	protected void pushState()
    {
        stack.push(new Object[] {
        	script,
        	index,
        	startTime,
        	temp
    	});
        index = 0;
        temp = null;
        startTime = -1;
    }

    protected void popState()
    {
        if (stack.size() == 0) {
            script = new Object[0];
	        index = 0;
	        done = yield = true;
            return;
        }

        Object[] oldState = (Object[]) stack.pop();
        script = (Object[]) oldState[0];
        index = (Integer) oldState[1];
        startTime = (Long) oldState[2];
        temp = (Integer) oldState[3];
    }
    
    protected void evalCommand(Object[] anArray)
    {
        String selector = (String) anArray[0];

        if (selector.equals("doIf")) {
	        if ((Boolean) evalArg(anArray[1])) {
	            evalCmdList(anArray[2], false);
	        }
	        return;
        } else if (selector.equals("doIfElse")) {
            evalCmdList((Boolean) evalArg(anArray[1]) ? anArray[2] : anArray[3], false);
            return;
        } else if (selector.equals("doRepeat")) {
            if (temp == null) {
                temp = (int) Math.round(Util.castDouble(evalArg(anArray[1])));
            }
            if ((Integer) temp <= 0) {
                temp = null;
                return;
            }

            temp = ((Integer) temp) - 1;
            evalCmdList(anArray[2], true);
            yield = true;
            return;
        } else if (selector.equals("doUntil")) {
            if ((Boolean) evalArg(anArray[1])) {
                evalCmdList(anArray[2], true);
            }
            yield = true;
            return;
        } else if (selector.equals("doForever")) {
            evalCmdList(anArray[1], true);
            yield = true;
            return;
        } else if (selector.equals("doReturn")) {
            stop();
            return;
        } else if (selector.equals("wait:elapsed:from:")) {
	        if (startTime == -1) {
	        	startTime = System.nanoTime();
	        	temp = Util.castDouble(evalArg(anArray[1]));
	        	yield = true;
	        	index--;
	            //evalCmdList(new Object[0], true);
	        } else if ((System.nanoTime() - startTime) / 1000000000d < (Double) temp) {
	            //evalCmdList(new object[0], true);
	        	yield = true;
	        	index--;
	        } else {
	        	startTime = -1;
	        }
            return;
        }

        Object[] args = new Object[anArray.length - 1];

        for (int i = 1; i < anArray.length; i++) {
            args[i - 1] = evalArg(anArray[i]);
        }

        object.evalCommand(selector, args);
    }

    protected Object evalArg(Object c)
    {
        if (c instanceof Object[]) {
	        Object[] anArray = (Object[]) c;
	        
            Object[] args = new Object[anArray.length - 1];

            for (int i = 1; i < anArray.length; i++) {
                args[i - 1] = evalArg(anArray[i]);
            }

            return object.evalArg((String) anArray[0], args);
        }
        return c;
    }

    protected void evalCmdList(Object anArrayOrNil, boolean repeat)
    {
        if (!repeat) {
    		index++;
        }
        pushState();
        if (anArrayOrNil == null) {
            script = new Object[0];
        } else {
            script = (Object[]) anArrayOrNil;
        }
        index = -1;
    }
}
