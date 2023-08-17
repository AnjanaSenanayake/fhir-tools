/*
 * Copyright (c) 2023, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.healthcare.fhir.codegen.ballerina.project.tool.generator;

import org.wso2.healthcare.codegen.tool.framework.commons.core.ToolContext;
import org.wso2.healthcare.codegen.tool.framework.commons.exception.CodeGenException;
import org.wso2.healthcare.codegen.tool.framework.fhir.core.AbstractFHIRTemplateGenerator;
import org.wso2.healthcare.fhir.codegen.ballerina.project.tool.BallerinaProjectConstants;
import org.wso2.healthcare.fhir.codegen.ballerina.project.tool.config.BallerinaProjectToolConfig;
import org.wso2.healthcare.fhir.codegen.ballerina.project.tool.config.IncludedIGConfig;
import org.wso2.healthcare.fhir.codegen.ballerina.project.tool.model.BallerinaService;

import java.util.HashMap;
import java.util.Map;

/**
 * Generator class to wrap all the generator classes in Ballerina project generator.
 */
public class BallerinaProjectGenerator extends AbstractFHIRTemplateGenerator {

    public BallerinaProjectGenerator(String targetDir) throws CodeGenException {
        super(targetDir);
    }

    @Override
    public void generate(ToolContext toolContext, Map<String, Object> generatorProperties) throws CodeGenException {

        BallerinaProjectToolConfig ballerinaProjectToolConfig = (BallerinaProjectToolConfig) generatorProperties.get("config");
        Map<String, BallerinaService> serviceMap = (Map<String, BallerinaService>) generatorProperties.get("serviceMap");
        Map<String, String> dependenciesMap = (Map<String, String>) generatorProperties.get("dependenciesMap");
        //evaluate usage of ? typed map as generator properties.

        for (Map.Entry<String, BallerinaService> entry : serviceMap.entrySet()) {
            Map<String, Object> projectProperties = new HashMap<>();
            projectProperties.put("service", entry.getValue());
            projectProperties.put("resourceType", entry.getKey());
            projectProperties.put("config", ballerinaProjectToolConfig);
            projectProperties.put("dependencies", dependenciesMap);

            String basePackage = dependenciesMap.get("basePackage");
            String servicePackage = dependenciesMap.get("servicePackage");
            String igPackage = dependenciesMap.get("igPackage");
            projectProperties.put("basePackageImportIdentifier", basePackage.substring(basePackage.lastIndexOf(".") + 1));
            projectProperties.put("servicePackageImportIdentifier", servicePackage.substring(servicePackage.lastIndexOf(".") + 1));
            projectProperties.put("igPackageImportIdentifier", igPackage.substring(igPackage.lastIndexOf(".") + 1));
            projectProperties.put("projectAPIPath", this.getTargetDir() + entry.getKey().toLowerCase() + BallerinaProjectConstants.PROJECT_API_SUFFIX);

            ServiceGenerator balServiceGenerator = new ServiceGenerator(this.getTargetDir());
            balServiceGenerator.generate(toolContext, projectProperties);

            TomlGenerator tomlGenerator = new TomlGenerator(this.getTargetDir());
            tomlGenerator.generate(toolContext, projectProperties);

            MetaGenerator metaGenerator = new MetaGenerator(this.getTargetDir());
            metaGenerator.generate(toolContext, projectProperties);

            TestGenerator testGenerator = new TestGenerator(this.getTargetDir());
            testGenerator.generate(toolContext, projectProperties);
        }
    }
}
