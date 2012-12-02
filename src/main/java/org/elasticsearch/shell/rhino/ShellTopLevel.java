/*
 * Licensed to Luca Cavanna (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.shell.rhino;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Singleton;
import org.elasticsearch.common.inject.name.Named;
import org.elasticsearch.shell.command.Command;
import org.mozilla.javascript.*;
import org.mozilla.javascript.tools.ToolErrorReporter;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

@Singleton
public class ShellTopLevel extends ImporterTopLevel {

    private final PrintStream out;
    private final Map<String, Command> commands;

    @Inject
    ShellTopLevel(@Named("shellOutput") PrintStream out, Map<String, Command> commands){

        this.out = out;
        this.commands = commands;

        //TODO default imports (check mvel)

        Context context = Context.enter();
        try {
            initStandardObjects(context, false);
        } finally {
            Context.exit();
        }

        //defineProperty("h", new Test(), ScriptableObject.DONTENUM);

        defineFunctionProperties("executeCommand", commands.keySet(), getClass(), ScriptableObject.DONTENUM);

    }

    public void defineFunctionProperties(String staticMethodName, Set<String> commandNames, Class<?> clazz, int attributes) {
        Method[] methods = MethodUtils.getMethodList(clazz);
        Method m = MethodUtils.findSingleMethod(methods, staticMethodName);
        if (m == null) {
            throw new RuntimeException("Method " + staticMethodName + " not found");
        }
        for (String commandName : commandNames) {
            FunctionObject f = new FunctionObject(commandName, m, this);
            defineProperty(commandName, f, attributes);
        }
    }

    @SuppressWarnings("unused")
    public static Object executeCommand(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
        if (funObj instanceof FunctionObject) {
            ShellTopLevel shellTopLevel = getInstance(funObj);
            String functionName = ((FunctionObject)funObj).getFunctionName();
            //TODO Arguments!!!!
            return shellTopLevel.commands.get(functionName).execute();
        }

        throw new RuntimeException("Unable to determine the command to run");
    }

    private static ShellTopLevel getInstance(Function function)
    {
        Scriptable scope = function.getParentScope();
        if (!(scope instanceof ShellTopLevel))
            throw reportRuntimeError("msg.bad.shell.function.scope",
                    String.valueOf(scope));
        return (ShellTopLevel)scope;
    }

    static RuntimeException reportRuntimeError(String msgId, String msgArg)
    {
        String message = ToolErrorReporter.getMessage(msgId, msgArg);
        return Context.reportRuntimeError(message);
    }
}
