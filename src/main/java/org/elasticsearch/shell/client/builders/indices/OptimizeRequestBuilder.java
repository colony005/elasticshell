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
package org.elasticsearch.shell.client.builders.indices;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.optimize.OptimizeRequest;
import org.elasticsearch.action.admin.indices.optimize.OptimizeResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.shell.client.builders.AbstractRequestBuilderJsonOutput;
import org.elasticsearch.shell.json.JsonToString;
import org.elasticsearch.shell.json.StringToJson;

import java.io.IOException;

import static org.elasticsearch.rest.action.support.RestActions.buildBroadcastShardsHeader;

/**
 * @author Luca Cavanna
 *
 * Request builder for optimize API
 */
@SuppressWarnings("unused")
public class OptimizeRequestBuilder<JsonInput, JsonOutput> extends AbstractRequestBuilderJsonOutput<OptimizeRequest, OptimizeResponse, JsonInput, JsonOutput> {

    public OptimizeRequestBuilder(Client client, JsonToString<JsonInput> jsonToString, StringToJson<JsonOutput> stringToJson) {
        super(client, new OptimizeRequest(), jsonToString, stringToJson);
    }

    public OptimizeRequestBuilder<JsonInput, JsonOutput> indices(String... indices) {
        request.indices(indices);
        return this;
    }

    public OptimizeRequestBuilder<JsonInput, JsonOutput> waitForMerge(boolean waitForMerge) {
        request.waitForMerge(waitForMerge);
        return this;
    }

    public OptimizeRequestBuilder<JsonInput, JsonOutput> maxNumSegments(int maxNumSegments) {
        request.maxNumSegments(maxNumSegments);
        return this;
    }

    public OptimizeRequestBuilder<JsonInput, JsonOutput> onlyExpungeDeletes(boolean onlyExpungeDeletes) {
        request.onlyExpungeDeletes(onlyExpungeDeletes);
        return this;
    }

    public OptimizeRequestBuilder<JsonInput, JsonOutput> flush(boolean flush) {
        request.flush(flush);
        return this;
    }

    public OptimizeRequestBuilder<JsonInput, JsonOutput> refresh(boolean refresh) {
        request.refresh(refresh);
        return this;
    }

    @Override
    protected ActionFuture<OptimizeResponse> doExecute(OptimizeRequest request) {
        return client.admin().indices().optimize(request);
    }

    @Override
    protected XContentBuilder toXContent(OptimizeRequest request, OptimizeResponse response, XContentBuilder builder) throws IOException {
        builder.startObject();
        builder.field(Fields.OK, true);
        buildBroadcastShardsHeader(builder, response);
        builder.endObject();
        return builder;
    }
}
