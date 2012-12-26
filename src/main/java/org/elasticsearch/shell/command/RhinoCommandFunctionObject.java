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
package org.elasticsearch.shell.command;

import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;

import java.lang.reflect.Member;

/**
 * Custom {@link FunctionObject} used to register commands to the shell through Rhino.
 * Commands will be available in form of javascript functions.
 *
 * @author Luca Cavanna
 */
public class RhinoCommandFunctionObject extends FunctionObject {

    private final Command command;

    public RhinoCommandFunctionObject(String name, Command command, Member methodOrConstructor, Scriptable scope) {
        super(name, methodOrConstructor, scope);
        if (!command.getClass().isAnnotationPresent(ExecutableCommand.class)) {
            throw new IllegalArgumentException("The provided command object [" + command.getClass().getSimpleName() + "] is not annotated with the " + ExecutableCommand.class.getSimpleName() + " annotation");
        }
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
