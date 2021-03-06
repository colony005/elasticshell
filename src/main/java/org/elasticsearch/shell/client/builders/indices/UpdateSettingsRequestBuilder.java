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
import org.elasticsearch.action.admin.indices.settings.UpdateSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.UpdateSettingsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.shell.client.builders.AbstractRequestBuilderJsonOutput;
import org.elasticsearch.shell.json.JsonToString;
import org.elasticsearch.shell.json.StringToJson;

import java.io.IOException;

/**
 * @author Luca Cavanna
 *
 * Request builder for update settings API
 */
@SuppressWarnings("unused")
public class UpdateSettingsRequestBuilder<JsonInput, JsonOutput> extends AbstractRequestBuilderJsonOutput<UpdateSettingsRequest, UpdateSettingsResponse, JsonInput, JsonOutput> {

    public UpdateSettingsRequestBuilder(Client client, JsonToString<JsonInput> jsonToString, StringToJson<JsonOutput> stringToJson) {
        super(client, new UpdateSettingsRequest(), jsonToString, stringToJson);
    }

    public UpdateSettingsRequestBuilder<JsonInput, JsonOutput> indices(String... indices) {
        request.indices(indices);
        return this;
    }

    public UpdateSettingsRequestBuilder<JsonInput, JsonOutput> settings(JsonInput source) {
        request.settings(jsonToString(source));
        return this;
    }

    @Override
    protected ActionFuture<UpdateSettingsResponse> doExecute(UpdateSettingsRequest request) {
        return client.admin().indices().updateSettings(request);
    }

    @Override
    protected XContentBuilder toXContent(UpdateSettingsRequest request, UpdateSettingsResponse response, XContentBuilder builder) throws IOException {
        builder.startObject()
                .field(Fields.OK, true)
                .endObject();
        return builder;
    }
}
