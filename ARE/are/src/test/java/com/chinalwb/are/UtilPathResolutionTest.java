package com.chinalwb.are;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilPathResolutionTest {

    @Test
    public void buildExternalStorageDocumentPath_returnsMatchingVolumePath() {
        String[] externalDirs = new String[] {
                "/storage/emulated/0/Android/data/com.chinalwb.are/files",
                "/storage/1234-5678/Android/data/com.chinalwb.are/files"
        };

        String resolvedPath = Util.GetPathFromUri4kitkat.buildExternalStorageDocumentPath(
                "1234-5678",
                "Movies/sample.mp4",
                externalDirs);

        assertEquals("/storage/1234-5678/Movies/sample.mp4", resolvedPath);
    }

    @Test
    public void buildExternalStorageDocumentPath_returnsNullWhenVolumeMissing() {
        String[] externalDirs = new String[] {
                "/storage/emulated/0/Android/data/com.chinalwb.are/files"
        };

        String resolvedPath = Util.GetPathFromUri4kitkat.buildExternalStorageDocumentPath(
                "ABCD-EFGH",
                "Movies/sample.mp4",
                externalDirs);

        assertNull(resolvedPath);
    }
}
