// Copyright (c) 2022, WSO2 LLC. (http://www.wso2.com). All Rights Reserved.

// This software is the property of WSO2 LLC. and its suppliers, if any.
// Dissemination of any information or reproduction of any material contained
// herein is strictly forbidden, unless permitted by WSO2 in accordance with
// the WSO2 Software License available at: https://wso2.com/licenses/eula/3.2
// For specific language governing the permissions and limitations under
// this license, please see the license as well as any agreement you’ve
// entered into with WSO2 governing the purchase of this software and any
// associated services.

package org.wso2.healthcare.fhir.ballerina.packagegen.tool.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.consensys.cava.toml.TomlArray;
import net.consensys.cava.toml.TomlTable;
import org.wso2.healthcare.fhir.ballerina.packagegen.tool.ToolConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Included IGs related config.
 */
public class IncludedIGConfig {

    private final String name;
    private final boolean isEnable;
    private String baseIGPackage;

    private final List<String> includedProfiles = new ArrayList<>();
    private final List<String> excludedProfiles = new ArrayList<>();

    public IncludedIGConfig(JsonObject implementationGuide) {
        this.name = implementationGuide.getAsJsonPrimitive(ToolConstants.CONFIG_PROFILE_IG).getAsString();
        this.isEnable = implementationGuide.getAsJsonPrimitive(ToolConstants.CONFIG_ENABLE).getAsBoolean();

        if(implementationGuide.getAsJsonPrimitive(ToolConstants.CONFIG_PROFILE_IG_BASE) != null)
            this.baseIGPackage = implementationGuide.getAsJsonPrimitive(ToolConstants.CONFIG_PROFILE_IG_BASE).getAsString();

        JsonArray includedProfileArray = implementationGuide.getAsJsonArray("includedProfiles");
        JsonArray excludedProfileArray = implementationGuide.getAsJsonArray("excludedProfiles");

        populateIncludedProfiles(includedProfileArray);
        populateExcludedProfiles(excludedProfileArray);
    }

    public IncludedIGConfig(TomlTable implementationGuide) {
        this.name = implementationGuide.getString(ToolConstants.CONFIG_PROFILE_IG);
        Boolean enabledVal = implementationGuide.getBoolean(ToolConstants.CONFIG_ENABLE);
        if (enabledVal != null) {
            this.isEnable = enabledVal;
        } else {
            this.isEnable = false;
        }

        if (implementationGuide.getString(ToolConstants.CONFIG_PROFILE_IG_BASE_TOML) != null)
            this.baseIGPackage = implementationGuide.getString(ToolConstants.CONFIG_PROFILE_IG_BASE_TOML);

        TomlArray includedProfileArray = implementationGuide.getArray("includedProfiles");
        TomlArray excludedProfileArray = implementationGuide.getArray("excludedProfiles");

        if (includedProfileArray != null)
            populateIncludedProfiles(includedProfileArray);
        if (excludedProfileArray != null)
            populateExcludedProfiles(excludedProfileArray);
    }

    private void populateIncludedProfiles(JsonArray profiles) {
        for (int i = 0; i < profiles.size(); i++) {
            String resource = profiles.get(i).getAsJsonPrimitive().getAsString();
            includedProfiles.add(resource);
        }
    }

    private void populateIncludedProfiles(TomlArray profiles) {
        if (profiles != null) {
            for (int i = 0; i < profiles.size(); i++) {
                String resource = (String) profiles.get(i);
                includedProfiles.add(resource);
            }
        }
    }

    private void populateExcludedProfiles(JsonArray profiles) {
        if (profiles != null) {
            for (int i = 0; i < profiles.size(); i++) {
                String resource = profiles.get(i).getAsJsonPrimitive().getAsString();
                excludedProfiles.add(resource);
            }
        }
    }

    private void populateExcludedProfiles(TomlArray profiles) {
        for (int i = 0; i < profiles.size(); i++) {
            String resource = (String) profiles.get(i);
            excludedProfiles.add(resource);
        }
    }

    public String getName() {
        return name;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public String getBaseIGPackage() {
        return baseIGPackage;
    }

    public List<String> getIncludedProfiles() {
        return includedProfiles;
    }

    public List<String> getExcludedProfiles() {
        return excludedProfiles;
    }
}
