package com.flowable.training.flyable.util;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FlowableUtil {

    /**
     * Determines the scope type based on the given definition or instance ID.
     *
     * @param id the definition or instance ID
     * @return the scope type (either "PROCESS" or "CASE")
     * @throws RuntimeException if the ID is null or invalid
     */
    public static String getScopeType(String id) {
        if (id == null) {
            throw new RuntimeException("DefinitionId must not be null");
        }
        if (id.startsWith("PRC")) {
            return ScopeTypes.PROCESS;
        } else if (id.startsWith("CAS")) {
            return ScopeTypes.CASE;
        } else {
            throw new RuntimeException("Invalid scope type for definitionId " + id);
        }
    }

    /**
     * Helper method to extract the app key from a Flowable app (BAR) input stream.
     * Used to check if we need to deploy the demo apps.
     * Basically just searches for a .app file and takes the name of the file as the key.
     * @param zipInputStream the input stream of the Flowable app (BAR) file
     */
    public static String getAppKeyFromAppZipInputStream(ZipInputStream zipInputStream) {
        try {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (entry.getName().endsWith(".app")) {
                    return entry.getName().substring(0, entry.getName().length() - 4);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("No .app file found in the app zip input stream");
    }

}